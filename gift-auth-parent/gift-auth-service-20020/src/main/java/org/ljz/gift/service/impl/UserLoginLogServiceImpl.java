package org.ljz.gift.service.impl;

import org.ljz.gift.domain.UserLoginLog;
import org.ljz.gift.mapper.UserLoginLogMapper;
import org.ljz.gift.service.IUserLoginLogService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 登录记录 服务实现类
 * </p>
 *
 * @author ljz
 * @since 2023-06-23
 */
@Service
public class UserLoginLogServiceImpl extends ServiceImpl<UserLoginLogMapper, UserLoginLog> implements IUserLoginLogService {

}
