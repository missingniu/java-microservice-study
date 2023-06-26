package org.ljz.gift.service;

import org.ljz.gift.domain.User;
import com.baomidou.mybatisplus.service.IService;
import org.ljz.gift.dto.RegisterDTO;
import org.ljz.gift.util.AjaxResult;

/**
 * <p>
 * 会员登录账号 服务类
 * </p>
 *
 * @author ljz
 * @since 2023-06-23
 */
public interface IUserService extends IService<User> {

    AjaxResult sendVerifyCodeByPhone(String mobilePhone);

    AjaxResult registerByPhone(RegisterDTO dto);
}
