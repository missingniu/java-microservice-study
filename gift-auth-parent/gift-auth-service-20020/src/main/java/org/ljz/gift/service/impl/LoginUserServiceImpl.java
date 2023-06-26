package org.ljz.gift.service.impl;

import org.ljz.gift.domain.LoginUser;
import org.ljz.gift.mapper.LoginUserMapper;
import org.ljz.gift.service.ILoginUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ljz
 * @since 2023-06-23
 */
@Service
public class LoginUserServiceImpl extends ServiceImpl<LoginUserMapper, LoginUser> implements ILoginUserService {

}
