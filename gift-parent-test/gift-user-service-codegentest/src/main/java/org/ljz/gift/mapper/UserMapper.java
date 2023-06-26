package org.ljz.gift.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import org.ljz.gift.domain.User;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.ljz.gift.query.UserQuery;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ljz
 * @since 2023-06-20
 */
public interface UserMapper extends BaseMapper<User> {

    List<User> loadPageList(Page<User> page, UserQuery query);
}
