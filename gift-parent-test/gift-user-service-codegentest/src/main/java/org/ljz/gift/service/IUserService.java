package org.ljz.gift.service;

import org.ljz.gift.domain.User;
import com.baomidou.mybatisplus.service.IService;
import org.ljz.gift.query.UserQuery;
import org.ljz.gift.util.PageList;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ljz
 * @since 2023-06-20
 */
public interface IUserService extends IService<User> {

    PageList<User> selectPageList(UserQuery userQuery);
}
