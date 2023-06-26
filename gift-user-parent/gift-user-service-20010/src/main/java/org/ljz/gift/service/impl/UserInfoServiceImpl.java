package org.ljz.gift.service.impl;

import org.ljz.gift.domain.UserInfo;
import org.ljz.gift.mapper.UserInfoMapper;
import org.ljz.gift.service.IUserInfoService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员基本信息 服务实现类
 * </p>
 *
 * @author ljz
 * @since 2023-06-23
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

}
