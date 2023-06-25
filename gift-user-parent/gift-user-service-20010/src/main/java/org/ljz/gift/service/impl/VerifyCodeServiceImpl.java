package org.ljz.gift.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.apache.commons.lang3.StringUtils;
import org.ljz.gift.constants.BasicConstant;
import org.ljz.gift.domain.User;
import org.ljz.gift.dto.RegisterDTO;
import org.ljz.gift.exception.Assert;
import org.ljz.gift.exception.BusinessException;
import org.ljz.gift.exception.ResponseCode;
import org.ljz.gift.service.IUserService;
import org.ljz.gift.service.IVerifyCodeService;
import org.ljz.gift.util.AjaxResult;
import org.ljz.gift.util.StrUtils;
import org.ljz.gift.util.VerifyCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

@Service
public class VerifyCodeServiceImpl implements IVerifyCodeService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IUserService userService;
    String code = "";
    @Override
    public AjaxResult sendVerifyCode(String mobilePhone) {
        //第一做参数校验
        Assert.isNotBlank(mobilePhone, ResponseCode.RESPONSE_CODE_400);
        //判断验证码是否为空，如果为空就生成验证码，如果有就判断是过期，如果没有过掉过期时间就抛出异常
        String verifyCode = (String)redisTemplate.opsForValue().get(BasicConstant.VERIFY_CODE+mobilePhone);
        if(StrUtil.isNotBlank(verifyCode)){
            //验证码
            code = verifyCode.split(":")[0];
            String time = verifyCode.split(":")[1];
            if(System.currentTimeMillis()-Long.parseLong(time)<=60*1000){
                throw new BusinessException(ResponseCode.RESPONSE_CODE_4008.getMessage(),ResponseCode.RESPONSE_CODE_4008.getCode());
            }
        }else{
            code = VerifyCodeUtils.generateVerifyCode(6);
        }
        //把验证码存入redis中
        String registerCode = code+":"+System.currentTimeMillis() ;
        redisTemplate.opsForValue().set(BasicConstant.VERIFY_CODE + mobilePhone, registerCode, 5,TimeUnit.MINUTES);
        System.out.println(code);
        return AjaxResult.me();
    }



    @Override
    public String getVerifyCode(String phone) {
        String verifyCode = redisTemplate.opsForValue().get(BasicConstant.VERIFY_CODE + phone).toString().split(":")[0];
        if(StrUtil.isNotBlank(verifyCode))
            return verifyCode;
        return "";
    }
}
