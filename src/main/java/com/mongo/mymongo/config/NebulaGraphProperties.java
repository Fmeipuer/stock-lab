/* Copyright (c) 2022 vesoft inc. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.mongo.mymongo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Nebula Graph 配置属性类
 * 支持从 Nacos 动态刷新配置
 */
//@RefreshScope
//@Component
@ConfigurationProperties(prefix = "nebula.graph")
@Data
public class NebulaGraphProperties {

    /**
     * graphd 地址列表，例：["127.0.0.1:9669","10.0.0.2:9669"]
     */
    private List<String> addresses;

    /**
     * 认证用户名
     */
    private String user;

    /**
     * 认证密码
     */
    private String password;

    /**
     * 连接池最大连接数
     */
    private Integer maxConnSize = 100;

    /**
     * 连接超时时间（毫秒）
     */
    private Integer timeout = 1000;

    /**
     * 空闲连接超时时间（毫秒）
     */
    private Integer idleTime = 0;
    /**
     * 检查空闲连接的间隔时间，单位为毫秒，-1表示不检查
     */
    private int intervalIdle = 30000;

    /**
     * 间隔时间（毫秒）
     */
    private Integer intervalTime = 100;

    /**
     * 重试次数
     */
    private Integer retryTimes = 3;

    /**
     * 是否启用 SSL
     */
    private Boolean enableSsl = false;

    /**
     * SSL 证书文件路径（CA签名）
     */
    private String sslCaCertPath;

    /**
     * SSL 证书文件路径（CA签名）
     */
    private String sslCertPath;

    /**
     * SSL 私钥文件路径（CA签名）
     */
    private String sslKeyPath;

    /**
     * SSL 证书文件路径（自签名）
     */
    private String sslSelfCertPath;

    /**
     * SSL 私钥文件路径（自签名）
     */
    private String sslSelfKeyPath;

    /**
     * SSL 自签名证书密码
     */
    private String sslSelfPassword;

}
