package org.ljz.gift;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ljz.gift.domain.User;
import org.ljz.gift.query.BaseQuery;
import org.ljz.gift.query.UserQuery;
import org.ljz.gift.service.IUserService;
import org.ljz.gift.util.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = APPStart.class)
public class MybatisPlusTest {
    @Autowired
    private IUserService userService;
    @Test
    public void test() throws Exception{
        UserQuery userQuery = new UserQuery();
        userQuery.setPage(1);//页数
        userQuery.setRows(10);//数据
        userQuery.setKeyword("zs");
        Page<User> objectPage = new Page<>(userQuery.getPage(),userQuery.getRows());
        Wrapper<User> objectWrapper = new EntityWrapper<>();
        if(StringUtils.isNotBlank(userQuery.getKeyword()))
        objectWrapper.like("name","zs");
        Page<User> userPage = userService.selectPage(objectPage, objectWrapper);
        System.out.println(objectPage.getRecords());
        System.out.println(objectPage.getTotal());
        System.out.println(userPage.getRecords());


    }
    @Test
    public void test02() throws Exception{
        UserQuery userQuery = new UserQuery();
        userQuery.setPage(1);
        userQuery.setRows(10);
        userQuery.setKeyword("");
       PageList<User> pageList =  userService.selectPageList(userQuery);
        System.out.println(pageList.getTotal());
        System.out.println(pageList.getRows());

    }
}
