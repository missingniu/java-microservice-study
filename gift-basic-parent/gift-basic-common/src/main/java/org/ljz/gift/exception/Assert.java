package org.ljz.gift.exception;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Objects;

public class Assert {

    public static void isBlank(String str,ResponseCode code){
        // 如果字符串不为空，就抛出异常
        if(StringUtils.isNotBlank(str)){
            throw new BusinessException(code);
        }
    }

    // 断言字符串不为空
    public static void isNotBlank(String str,ResponseCode code){
        // 如果字符串为空，就抛出异常
        if(StringUtils.isBlank(str)){
            throw new BusinessException(code);
        }
    }

    // 断言对象为空
    public static void isNull(Object obj,ResponseCode code){
        // 如果对象不为空，就抛出异常
        if(Objects.nonNull(obj)){
            throw new BusinessException(code);
        }
    }

    // 断言对象不为空
    public static void isNotNull(Object obj,ResponseCode code){
        // 如果对象为空，就抛出异常
        if(Objects.isNull(obj)){
            throw new BusinessException(code);
        }
    }

    // 断言集合为空
    public static void isNull(Collection collection,ResponseCode code){
        // 如果集合不为空，就抛出异常
        if(collection != null && collection.size() > 0){
            throw new BusinessException(code);
        }
    }

    // 断言集合不为空
    public static void isNotNull(Collection collection,ResponseCode code){
        // 如果集合为空，就抛出异常
        if(collection == null || collection.size() == 0){
            throw new BusinessException(code);
        }
    }

    // 断言两个字符串值相等
    public static void isEq(String str1,String str2,ResponseCode code){
        // 如果两个字符串不相等，就抛出异常
        if(!str1.equals(str2)){
            throw new BusinessException(code);
        }
    }
    public static void isEq(Object str1,String str2,ResponseCode code){
        // 如果两个字符串不相等，就抛出异常
        if(!str1.equals(str2)){
            throw new BusinessException(code);
        }
    }


    public static void isEqIgnore(String str1,String str2,ResponseCode code) {
        if(!str1.equalsIgnoreCase(str2)){
            throw new BusinessException(code);
        }
    }
}
