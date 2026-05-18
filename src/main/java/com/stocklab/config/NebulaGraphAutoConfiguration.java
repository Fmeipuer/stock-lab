/* Copyright (c) 2022 vesoft inc. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.stocklab.config;

import com.vesoft.nebula.client.graph.NebulaPoolConfig;
import com.vesoft.nebula.client.graph.data.CASignedSSLParam;
import com.vesoft.nebula.client.graph.data.HostAddress;
import com.vesoft.nebula.client.graph.data.SelfSignedSSLParam;
import com.vesoft.nebula.client.graph.net.NebulaPool;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.UnknownHostException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Nebula Graph 自动装配配置类
 * 基于 NebulaPool 实现，支持动态切换 space
 */
@Configuration
@EnableConfigurationProperties(NebulaGraphProperties.class)
public class NebulaGraphAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(NebulaGraphAutoConfiguration.class);

    private NebulaPool nebulaPool;

    @Bean
    public NebulaPool nebulaPool(NebulaGraphProperties properties) throws UnknownHostException {
        List<HostAddress> hostAddresses = properties.getAddresses().stream()
                .map(addr -> {
                    String[] parts = addr.split(":");
                    String host = parts[0].trim();
                    int port = parts.length > 1 ? Integer.parseInt(parts[1].trim()) : 9669;
                    return new HostAddress(host, port);
                })
                .collect(Collectors.toList());

        NebulaPoolConfig config = new NebulaPoolConfig();
        config.setMaxConnSize(properties.getMaxConnSize());
        config.setTimeout(properties.getTimeout());
        config.setIdleTime(properties.getIdleTime());
        config.setIntervalIdle(properties.getIntervalIdle());

        // SSL 配置
        if (properties.getEnableSsl()) {
            config.setEnableSsl(true);
            
            // 优先使用 CA 签名证书
            if (properties.getSslCaCertPath() != null && 
                properties.getSslCertPath() != null && 
                properties.getSslKeyPath() != null) {
                config.setSslParam(new CASignedSSLParam(
                        properties.getSslCaCertPath(),
                        properties.getSslCertPath(),
                        properties.getSslKeyPath()
                ));
                log.info("缺少 SSL 相关配置");
            }
            // 使用自签名证书
            else if (properties.getSslSelfCertPath() != null && 
                     properties.getSslSelfKeyPath() != null && 
                     properties.getSslSelfPassword() != null) {
                config.setSslParam(new SelfSignedSSLParam(
                        properties.getSslSelfCertPath(),
                        properties.getSslSelfKeyPath(),
                        properties.getSslSelfPassword()
                ));
                log.info("缺少 SSL 相关配置");
            }
            else {
                log.warn("SSL 启用，但是缺少 SSL 相关配置，默认使用不安全的 SSL 连接");
            }
        }

        NebulaPool pool = new NebulaPool();
        boolean initResult = pool.init(hostAddresses, config);
        if (!initResult) {
            throw new IllegalStateException("NebulaPool 初始化失败");
        }
        
        this.nebulaPool = pool;
        log.info("NebulaPool 初始化成功 hosts={}, maxConnSize={}, ssl={}",
                properties.getAddresses(), properties.getMaxConnSize(), properties.getEnableSsl());
        return pool;
    }

    @PreDestroy
    public void close() {
        if (this.nebulaPool != null) {
            try {
                this.nebulaPool.close();
                log.info("NebulaPool 关闭成功");
            } catch (Exception e) {
                log.error("NebulaPool 关闭失败", e);
            }
        }
    }
}
