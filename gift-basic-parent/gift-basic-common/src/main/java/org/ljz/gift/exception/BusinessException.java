package org.ljz.gift.exception;

/**
 * 业务类的异常
 */
public class BusinessException extends RuntimeException{




    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    private Integer code;
    public BusinessException(String message,Integer code) {
        super(message);
        this.code = code;
    }
    public BusinessException(ResponseCode code){
        super(code.getMessage());
        this.code = code.getCode();
    }
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    protected BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
