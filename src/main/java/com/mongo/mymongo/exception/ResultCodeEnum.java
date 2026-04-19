package com.mongo.mymongo.exception;

import lombok.Data;

/**
 * @Description
 * @Author flm
 * @Date 2026/02/04 21:/54
 * @Version 1.0
 */
public enum ResultCodeEnum {

 SUCCESS(200, "成功"),
 INNER(500, "内部错误"),
 SERVER(501, "服务错误"),
 REDIRECT(401, "token auth error,please redirect"),
 OSS_QUERY_ERROR(50001, "OSS上传参数转化失败"),
 OSS_MD5_ERROR(50002, "OSS文件MD5校验失败"),
 EXCEL_EXPORT_ERROR(50101, "导出Excel异常"),
 BAD_REQUEST(400, "校验参数异常"),
 UNAUTHORIZED(402, "未认证"),
 NOT_PERMISSION(403, "访问受限，无权限"),
 NOT_FOUND(404, "资源，服务未找到"),
 TOKEN_INVALID(405, "TOKEN失效"),
 NOT_ROLE(406, "角色未知异常"),
 BUSINESS_ERROR(502, "业务处理错误"),
 FEIGN_ERROR(503, "Feign调用异常");

 private final Integer code;
 private final String msg;

 private ResultCodeEnum(Integer code, String msg) {
  this.code = code;
  this.msg = msg;
 }

 public Integer getCode() {
  return this.code;
 }

 public String getMsg() {
  return this.msg;
 }

}
