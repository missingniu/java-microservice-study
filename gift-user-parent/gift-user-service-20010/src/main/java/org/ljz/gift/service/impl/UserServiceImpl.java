package org.ljz.gift.service.impl;

import org.ljz.gift.domain.User;
import org.ljz.gift.mapper.UserMapper;
import org.ljz.gift.service.IUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员登录账号 服务实现类
 * </p>
 *
 * @author ljz
 * @since 2023-06-20
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
