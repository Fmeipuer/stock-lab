package com.stocklab.exception;

import com.stocklab.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Description
 * @Author flm
 * @Date 2026/02/04 22:/31
 * @Version 1.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /* ================= 业务异常 ================= */

    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBizException(BusinessException ex) {
        log.warn("业务异常：{}", ex.getMessage());
        return Result.error(ex.getErrorCode(), ex.getErrorMsg());
    }


    /* ================= 系统异常兜底 ================= */

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception ex) {
        log.error("系统异常", ex);
        return Result.error(500, "系统繁忙，请稍后再试");
    }

}

