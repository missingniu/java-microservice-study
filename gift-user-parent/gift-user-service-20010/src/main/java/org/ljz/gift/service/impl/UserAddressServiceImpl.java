package org.ljz.gift.service.impl;

import org.ljz.gift.domain.UserAddress;
import org.ljz.gift.mapper.UserAddressMapper;
import org.ljz.gift.service.IUserAddressService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 收货地址 服务实现类
 * </p>
 *
 * @author ljz
 * @since 2023-06-23
 */
@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements IUserAddressService {

}
