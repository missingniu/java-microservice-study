package org.ljz.gift.controller;

import org.ljz.gift.service.IUserInfoService;
import org.ljz.gift.domain.UserInfo;
import org.ljz.gift.query.UserInfoQuery;
import com.baomidou.mybatisplus.plugins.Page;
import org.ljz.gift.util.AjaxResult;
import org.ljz.gift.util.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userInfo")
public class UserInfoController {
    @Autowired
    public IUserInfoService userInfoService;


    /**
     * 保存和修改公用的
     * @param userInfo  传递的实体
     * @return Ajaxresult转换结果
     */
    @PutMapping
    public AjaxResult addOrUpdate(@RequestBody UserInfo userInfo){
        try {
            if( userInfo.getId()!=null)
                userInfoService.updateById(userInfo);
            else
                userInfoService.insert(userInfo);
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setMessage("保存对象失败！"+e.getMessage());
        }
    }
    /**
    * 删除对象信息
    * @param id
    * @return
    */
    @DeleteMapping(value="/{id}")
    public AjaxResult delete(@PathVariable("id") Long id){
        try {
            userInfoService.deleteById(id);
            return AjaxResult.me();
        } catch (Exception e) {
        e.printStackTrace();
            return AjaxResult.me().setMessage("删除对象失败！"+e.getMessage());
        }
    }
	
    //获取用户
    @GetMapping("/{id}")
    public AjaxResult get(@PathVariable("id")Long id)
    {
        try {

            UserInfo userInfo = userInfoService.selectById(id);
            return AjaxResult.me().setResultObj(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("获取一个失败！"+e.getMessage());
        }
    }


    /**
    * 查看所有的员工信息
    * @return
    */
    @GetMapping
    public AjaxResult list(){

        try {
            List< UserInfo> list = userInfoService.selectList(null);
            return AjaxResult.me().setResultObj(list);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("获取所有失败！"+e.getMessage());
        }
    }


    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */

    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @PostMapping("/list")
    public AjaxResult json(@RequestBody UserInfoQuery query)
    {
        Page<UserInfo> page = new Page<UserInfo>(query.getPage(),query.getRows());
        page = userInfoService.selectPage(page);
        PageList pageList = new PageList<UserInfo>(page.getTotal(),page.getRecords());
        return AjaxResult.me().setResultObj(pageList);
    }
}
