package org.ljz.gift.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import org.ljz.gift.domain.User;
import org.ljz.gift.mapper.UserMapper;
import org.ljz.gift.query.UserQuery;
import org.ljz.gift.service.IUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ljz.gift.util.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ljz
 * @since 2023-06-20
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public PageList<User> selectPageList(UserQuery query) {

        Page<User> page = new Page<>(query.getPage(), query.getRows());
        //自定义mapper方法，返回关联数据对象
        List<User> data = userMapper.loadPageList(page,query);
        return new PageList<>(page.getTotal(),data);
    }
}
