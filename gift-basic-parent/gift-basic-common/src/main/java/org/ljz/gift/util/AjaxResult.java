package org.ljz.gift.util;

import org.ljz.gift.exception.ResponseCode;

//Ajax请求响应对象的类
public class AjaxResult {
    private Integer code = ResponseCode.RESPONSE_CODE_200.getCode();
    private Boolean success = true;
    private String message = ResponseCode.RESPONSE_CODE_200.getMessage();
    private Object resultObject;

    public Boolean getSuccess() {
        return success;
    }

    public AjaxResult setSuccess(Boolean success) {
        this.success = success;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public AjaxResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getResultObj() {
        return resultObject;
    }

    public AjaxResult setResultObj(Object resultObject) {
        this.resultObject = resultObject;
        return this;
    }

    public static AjaxResult me() {
        return new AjaxResult();
    }

    public Integer getCode() {
        return code;
    }

    public AjaxResult setCode(Integer code) {
        this.code = code;
        return this;
    }
}
