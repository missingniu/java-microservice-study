package org.ljz.gift.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.ljz.gift.client.AuthClient;
import org.ljz.gift.domain.LoginUser;
import org.ljz.gift.domain.User;
import org.ljz.gift.domain.UserInfo;
import org.ljz.gift.dto.RegisterDTO;
import org.ljz.gift.exception.Assert;
import org.ljz.gift.exception.BusinessException;
import org.ljz.gift.exception.ResponseCode;
import org.ljz.gift.mapper.UserInfoMapper;
import org.ljz.gift.mapper.UserMapper;
import org.ljz.gift.service.IUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ljz.gift.service.IVerifyCodeService;
import org.ljz.gift.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * <p>
 * 会员登录账号 服务实现类
 * </p>
 *
 * @author ljz
 * @since 2023-06-23
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private IVerifyCodeService verifyCodeService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private AuthClient authClient;
    @Override
    public AjaxResult sendVerifyCodeByPhone(String mobilePhone) {
        //第一步校验手机号是否存在
        Wrapper<User> wrapper = new EntityWrapper<User>().eq("phone", mobilePhone);
       int count = userMapper.selectCount(wrapper);
        if(count>0){
            //表明手机号已经存在，抛出异常信息
            throw new BusinessException(ResponseCode.ResponseCode_CODE_4010);
        }
        return verifyCodeService.sendVerifyCode(mobilePhone);
    }

    @Override
    public AjaxResult registerByPhone(RegisterDTO dto) {
        String verifyCode = verifyCodeService.getVerifyCode(dto.getMobilePhone());
        Assert.isNotBlank(verifyCode,ResponseCode.ResponseCode_CODE_40012);
        //判断验证码和redis中的验证码是否一样，如果不一致就抛出异常信息
        Assert.isEqIgnore(verifyCode,dto.getVerifyCode(),ResponseCode.RESPONSE_CODE_4007);
        //保存数据到数据库，首先需要保存到授权中心的loginInfo表中
        LoginUser loginUser = new LoginUser();
        loginUser.setUsername(UUID.randomUUID().toString());
        loginUser.setPassword(dto.getPassword());
        loginUser.setTel(dto.getMobilePhone());
        loginUser.setType(1);//代表是用户登录
        //保存登录表信息
         AjaxResult result= authClient.addOrUpdate(loginUser);
         if(!result.getSuccess()){
             return result;
         }
         //设置loginId
        Long loginId = Long.valueOf(result.getResultObj().toString());
         //保存用户信息
        User user = new User();
        user.setCreateTime(new Date().getTime());
        user.setPassword(dto.getPassword());
        user.setLoginId(loginId);
        user.setPhone(dto.getMobilePhone());
        user.setUpdateTime(new Date().getTime());
        userMapper.insert(user);
        //保存用户详细信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setCreateTime(new Date().getTime());
        userInfo.setUpdateTime(new Date().getTime());
        userInfo.setGrowScore(5);//新用户初始值为5
        userInfoMapper.insert(userInfo);
        return AjaxResult.me();
    }
}
