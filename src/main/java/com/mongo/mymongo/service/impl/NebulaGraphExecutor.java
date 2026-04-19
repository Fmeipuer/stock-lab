/* Copyright (c) 2022 vesoft inc. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.mongo.mymongo.service.impl;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import com.mongo.mymongo.config.NebulaGraphProperties;
import com.mongo.mymongo.service.GraphExecutor;
import com.vesoft.nebula.client.graph.data.ResultSet;
import com.vesoft.nebula.client.graph.exception.IOErrorException;
import com.vesoft.nebula.client.graph.net.NebulaPool;
import com.vesoft.nebula.client.graph.net.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Nebula Graph 执行器实现
 * 基于 NebulaPool 实现，支持动态切换 space
 */
@Service
public class NebulaGraphExecutor implements GraphExecutor {

    private static final Logger log = LoggerFactory.getLogger(NebulaGraphExecutor.class);

    private final NebulaPool nebulaPool;
    private final NebulaGraphProperties properties;

    // Session 缓存，避免频繁创建和销毁
//    private final Map<String, Session> sessionCache = new ConcurrentHashMap<>();

    public NebulaGraphExecutor(NebulaPool nebulaPool, NebulaGraphProperties properties) {
        this.nebulaPool = nebulaPool;
        this.properties = properties;
    }

    @Override
    public ResultSet execute(String spaceName, String ngql) {
        Session session = getSession(spaceName);
        try {
            return session.execute(ngql);
        } catch (IOErrorException e) {
            // 执行失败时清除该 space 的 Session 缓存
//            clearSessionCache(spaceName);
            throw new RuntimeException("执行 nGQL 失败: " + e.getMessage(), e);
        } finally {
            // 释放 Session
            session.release();
        }
    }

    @Override
    public ResultSet executeWithRetry(String spaceName, String ngql, int retryTimes) {
        int attempts = 0;
        RuntimeException lastEx = null;
        while (attempts <= retryTimes) {
            try {
                return execute(spaceName, ngql);
            } catch (RuntimeException ex) {
                lastEx = ex;
                attempts++;
                if (attempts > retryTimes) break;

                // 重试前清除缓存，确保下次重试时创建新的 Session
//                clearSessionCache(spaceName);

                try {
                    Thread.sleep(Math.max(50, properties.getIntervalTime()));
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
                log.warn("nGQL 执行 重试 {}/{} for space '{}': {}",
                        attempts, retryTimes, spaceName, ex.getMessage());
            }
        }
        throw lastEx != null ? lastEx : new RuntimeException("执行 nGQL 失败");
    }

    @Override
    public ResultSet executeWithParameter(String spaceName, String ngql, Map<String, Object> paramMap) {
        Session session = getSession(spaceName);
        try {
            return session.executeWithParameter(ngql, paramMap);
        } catch (IOErrorException e) {
            // 执行失败时清除该 space 的 Session 缓存
//            clearSessionCache(spaceName);
            log.error("执行 nGQL 失败:", e);
            throw new RuntimeException("执行 nGQL 失败" + e.getMessage(), e);
        } finally {
            // 释放 Session
            session.release();
        }
    }

    @Override
    public JSONObject executeJson(String spaceName, String ngql) {
        Session session = getSession(spaceName);
        try {
            String resp = session.executeJson(ngql);
            JSONObject result = JSONUtil.parseObj(resp).getJSONArray("errors").getJSONObject(0);
            if (result.getInt("code") != 0) {
                log.error(String.format("Execute: `%s', failed: %s",
                        ngql, result.getStr("message"))
                );
                throw new RuntimeException("执行 nGQL 失败 ");
            } else {
                return JSONUtil.parseObj(resp).getJSONArray("results").getJSONObject(0);
            }
        } catch (IOErrorException e) {
            // 执行失败时清除该 space 的 Session 缓存
//            clearSessionCache(spaceName);
            log.error("执行 nGQL 失败:", e);
            throw new RuntimeException("执行 nGQL 失败 " + e.getMessage(), e);
        } finally {
            // 释放 Session
            session.release();
        }
    }

    @Override
    public String executeJsonWithParameter(String spaceName, String ngql, Map<String, Object> paramMap) {
        Session session = getSession(spaceName);
        try {
            return session.executeJsonWithParameter(ngql, paramMap);
        } catch (IOErrorException e) {
            // 执行失败时清除该 space 的 Session 缓存
//            clearSessionCache(spaceName);
            log.error("执行 nGQL 失败:", e);
            throw new RuntimeException("执行 nGQL 失败 " + e.getMessage(), e);
        } finally {
            // 释放 Session
            session.release();
        }
    }

    /**
     * 获取或创建指定 space 的 Session
     *
     * @param spaceName space 名称
     * @return Session 对象
     */
    private Session getSession(String spaceName) {
        if (StrUtil.isNotEmpty(spaceName)) {
            // 先从缓存中获取
//            Session cachedSession = sessionCache.get(spaceName);
//            if (cachedSession != null) {
//                log.debug("使用缓存session space: {}", spaceName);
//                return cachedSession;
//            }
            // 缓存中没有，创建新的 Session
            try {
                Session session = nebulaPool.getSession(properties.getUser(), properties.getPassword(), false);

                // 切换到指定 space
                session.execute("USE " + spaceName + ";");

                // 将新创建的 Session 放入缓存
//                sessionCache.put(spaceName, session);
                log.debug("创建新的session space: {}", spaceName);
                return session;
            } catch (Exception e) {
                log.error("创建session失败 space '{}': {}", spaceName, e.getMessage(), e);
                // 如果创建失败，确保缓存中没有该 space 的记录
//                sessionCache.remove(spaceName);
                throw new RuntimeException("创建session失败 space '" + spaceName + "': " + e.getMessage(), e);
            }
        } else {
            try {
                return nebulaPool.getSession(properties.getUser(), properties.getPassword(), false);
            } catch (Exception e) {
                log.error("创建session失败: {}", e.getMessage(), e);
                throw new RuntimeException("创建session失败: " + e.getMessage(), e);
            }
        }
    }

    /**
     * 清理指定 space 的 Session 缓存
     *
     * @param spaceName space 名称
     */
//    public void clearSessionCache(String spaceName) {
//        Session session = sessionCache.remove(spaceName);
//        if (session != null) {
//            try {
//                session.release();
//                log.debug("释放 session space: {}", spaceName);
//            } catch (Exception e) {
//                log.warn("释放session失败 space '{}': {}", spaceName, e.getMessage());
//            }
//        }
//    }

    /**
     * 清理所有 Session 缓存
     */
//    public void clearAllSessionCache() {
//        sessionCache.forEach((spaceName, session) -> {
//            try {
//                session.release();
//                log.debug("释放 session space: {}", spaceName);
//            } catch (Exception e) {
//                log.warn("释放 session 失败 space '{}': {}", spaceName, e.getMessage());
//            }
//        });
//        sessionCache.clear();
//    }

    /**
     * 获取当前缓存的 Session 数量
     *
     * @return 缓存中的 Session 数量
     */
//    public int getSessionCacheSize() {
//        return sessionCache.size();
//    }

    /**
     * 获取当前缓存的所有 space 名称
     *
     * @return space 名称集合
     */
//    public java.util.Set<String> getCachedSpaceNames() {
//        return sessionCache.keySet();
//    }
}
