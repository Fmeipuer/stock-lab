package com.mongo.mymongo.exception;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
/**
 * @Description
 * @Author flm
 * @Date 2026/02/04 21:/51
 * @Version 1.0
 */

public class BusinessException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 4843777456195536990L;
    private Integer errorCode;
    private String errorMsg;
    private Map<String, Object> data;

    public BusinessException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
        this.errorCode = ResultCodeEnum.INNER.getCode();
    }

    public BusinessException(Integer errorCode, String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
        this.errorCode = errorCode;
    }

    public BusinessException(Integer errorCode, String errorMsg, Map<String, Object> data) {
        super(errorMsg);
        this.errorMsg = errorMsg;
        this.errorCode = errorCode;
        this.data = data;
    }

    public void put(String key, Object value) {
        if (this.data == null) {
            this.data = new HashMap();
        }

        this.data.put(key, value);
    }

    public Integer getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Map<String, Object> getData() {
        return this.data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}

