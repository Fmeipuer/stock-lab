package com.stocklab.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应状态码枚举
 *
 * @author system
 * @since 2025-07-15
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    /**
     * 成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 失败
     */
    ERROR(500, "操作失败"),

    /**
     * 参数错误
     */
    PARAM_ERROR(400, "参数错误"),

    /**
     * 未授权
     */
    UNAUTHORIZED(401, "未授权"),

    /**
     * 禁止访问
     */
    FORBIDDEN(403, "禁止访问"),

    /**
     * 资源不存在
     */
    NOT_FOUND(404, "资源不存在"),

    /**
     * 请求方法不支持
     */
    METHOD_NOT_ALLOWED(405, "请求方法不支持"),

    /**
     * 数据已存在
     */
    DATA_EXISTS(409, "数据已存在"),

    /**
     * 数据不存在
     */
    DATA_NOT_EXISTS(410, "数据不存在"),

    /**
     * 系统异常
     */
    SYSTEM_ERROR(500, "系统异常"),

    /**
     * 业务异常
     */
    BUSINESS_ERROR(600, "业务异常"),

    /**
     * 数据库异常
     */
    DATABASE_ERROR(700, "数据库异常"),

    /**
     * 网络异常
     */
    NETWORK_ERROR(800, "网络异常"),

    // ========================= 参数验证异常 =========================

    /**
     * 参数验证失败
     */
    VALIDATION_ERROR(1001, "参数验证失败"),

    /**
     * 必填参数缺失
     */
    REQUIRED_PARAM_MISSING(1002, "必填参数缺失"),

    /**
     * 参数格式错误
     */
    PARAM_FORMAT_ERROR(1003, "参数格式错误"),

    /**
     * 参数值超出范围
     */
    PARAM_OUT_OF_RANGE(1004, "参数值超出范围"),

    // ========================= 数据库异常 =========================

    /**
     * 数据插入失败
     */
    DATA_INSERT_ERROR(2001, "数据插入失败"),

    /**
     * 数据更新失败
     */
    DATA_UPDATE_ERROR(2002, "数据更新失败"),

    /**
     * 数据删除失败
     */
    DATA_DELETE_ERROR(2003, "数据删除失败"),

    /**
     * 数据查询失败
     */
    DATA_QUERY_ERROR(2004, "数据查询失败"),

    /**
     * 数据重复
     */
    DATA_DUPLICATE(2005, "数据重复"),

    /**
     * 外键约束违反
     */
    FOREIGN_KEY_CONSTRAINT(2006, "外键约束违反"),

    // ========================= 文件操作异常 =========================

    /**
     * 文件上传失败
     */
    FILE_UPLOAD_ERROR(3001, "文件上传失败"),

    /**
     * 文件下载失败
     */
    FILE_DOWNLOAD_ERROR(3002, "文件下载失败"),

    /**
     * 文件不存在
     */
    FILE_NOT_EXISTS(3003, "文件不存在"),

    /**
     * 文件格式不支持
     */
    FILE_FORMAT_NOT_SUPPORTED(3004, "文件格式不支持"),

    /**
     * 文件大小超限
     */
    FILE_SIZE_EXCEEDED(3005, "文件大小超限"),

    // ========================= 权限相关异常 =========================

    /**
     * 登录已过期
     */
    LOGIN_EXPIRED(4001, "登录已过期"),

    /**
     * 账号被锁定
     */
    ACCOUNT_LOCKED(4002, "账号被锁定"),

    /**
     * 账号被禁用
     */
    ACCOUNT_DISABLED(4003, "账号被禁用"),

    /**
     * 权限不足
     */
    INSUFFICIENT_PERMISSIONS(4004, "权限不足"),

    /**
     * 访问频率过高
     */
    ACCESS_RATE_LIMITED(4005, "访问频率过高"),

    // ========================= 业务逻辑异常 =========================

    /**
     * 业务规则验证失败
     */
    BUSINESS_RULE_VIOLATION(5001, "业务规则验证失败"),

    /**
     * 状态不允许此操作
     */
    INVALID_STATUS_OPERATION(5002, "状态不允许此操作"),

    /**
     * 数据已被其他用户修改
     */
    DATA_MODIFIED_BY_OTHERS(5003, "数据已被其他用户修改"),

    /**
     * 操作超时
     */
    OPERATION_TIMEOUT(5004, "操作超时"),

    /**
     * 资源不足
     */
    INSUFFICIENT_RESOURCES(5005, "资源不足"),

    // ========================= 第三方服务异常 =========================

    /**
     * 第三方服务不可用
     */
    THIRD_PARTY_SERVICE_UNAVAILABLE(6001, "第三方服务不可用"),

    /**
     * 第三方服务响应超时
     */
    THIRD_PARTY_SERVICE_TIMEOUT(6002, "第三方服务响应超时"),

    /**
     * 第三方服务返回错误
     */
    THIRD_PARTY_SERVICE_ERROR(6003, "第三方服务返回错误"),

    // ========================= 导入导出异常 =========================
    IMPORT_FILE_ERROR(7001, "导入异常"),

    /**
     * 核心指标数据有重复数据
     */
    CORE_INDEX_DUPLICATE(7002, "核心指标数据有重复数据，请检查！"),
    /**
     * 时间范围错误
     */
    INVALID_DATE_RANGE(7003,"开始时间不能大于结束时间"),
    /**
     * dify接口异常
     */
    DIFY_ERROR(7004,"Dify接口异常"),
    /**
     * markdown转word异常
     */
    MARKDOWN_TO_WORD_ERROR(7005, "Markdown 转 Word 异常"),



    MERGE_AUDIO_ERROR(8000,"执行 ffmpeg 命令超时或出错");

    ;

    /**
     * 响应码
     */
    private final Integer code;

    /**
     * 响应消息
     */
    private final String message;
}
