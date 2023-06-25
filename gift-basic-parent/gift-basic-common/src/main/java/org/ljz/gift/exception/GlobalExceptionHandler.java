package org.ljz.gift.exception;


import org.ljz.gift.util.AjaxResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Iterator;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 处理业务异常类的异常
      * @param e
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public AjaxResult businessExceptionHandler(BusinessException e){
        e.printStackTrace();
        return AjaxResult.me().setSuccess(false).setCode(e.getCode()).setMessage(e.getMessage());
    }
    @ExceptionHandler(Exception.class)
    public AjaxResult exceptionExceptionHandler(Exception e){
        e.printStackTrace();
        return AjaxResult.me().setSuccess(false).setCode(ResponseCode.RESPONSE_CODE_500.getCode()).setMessage("网络异常!请稍后再试");
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public AjaxResult MethodArgumentNotValidException(MethodArgumentNotValidException e){
        e.printStackTrace();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        StringBuffer sb = new StringBuffer();
        Iterator<FieldError> iterator = fieldErrors.iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next().getDefaultMessage());
            sb.append(",");
        }
        String message = sb.substring(0, sb.length() - 1);
        return AjaxResult.me().setSuccess(false).setCode(ResponseCode.RESPONSE_CODE_400.getCode()).setMessage(message);
    }
}
