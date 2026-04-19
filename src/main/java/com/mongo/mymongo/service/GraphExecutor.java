/* Copyright (c) 2022 vesoft inc. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.mongo.mymongo.service;

import cn.hutool.json.JSONObject;
import com.vesoft.nebula.client.graph.data.ResultSet;

import java.util.Map;

/**
 * Nebula Graph 执行器接口
 * 支持动态切换 space 执行 nGQL
 */
public interface GraphExecutor {

    /**
     * 执行 nGQL（指定 space）
     * @param spaceName space 名称
     * @param ngql nGQL 语句
     * @return 执行结果
     */
    ResultSet execute(String spaceName, String ngql);

    /**
     * 执行 nGQL（指定 space，带重试）
     * @param spaceName space 名称
     * @param ngql nGQL 语句
     * @param retryTimes 重试次数
     * @return 执行结果
     */
    ResultSet executeWithRetry(String spaceName, String ngql, int retryTimes);

    /**
     * 执行带参数的 nGQL（指定 space）
     * @param spaceName space 名称
     * @param ngql nGQL 语句
     * @param paramMap 参数映射
     * @return 执行结果
     */
    ResultSet executeWithParameter(String spaceName, String ngql, Map<String, Object> paramMap);

    /**
     * 执行 nGQL 并返回 JSON 格式结果（指定 space）
     * @param spaceName space 名称
     * @param ngql nGQL 语句
     * @return JSON 格式的执行结果
     */
    JSONObject executeJson(String spaceName, String ngql);

    /**
     * 执行 nGQL 并返回 JSON 格式结果（指定 space，带参数）
     * @param spaceName space 名称
     * @param ngql nGQL 语句
     * @param paramMap 参数映射
     * @return JSON 格式的执行结果
     */
    String executeJsonWithParameter(String spaceName, String ngql, Map<String, Object> paramMap);
}
