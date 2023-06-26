package org.ljz.gift.controller;

import org.ljz.gift.service.IUserLoginLogService;
import org.ljz.gift.domain.UserLoginLog;
import org.ljz.gift.query.UserLoginLogQuery;
import com.baomidou.mybatisplus.plugins.Page;
import org.ljz.gift.util.AjaxResult;
import org.ljz.gift.util.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userLoginLog")
public class UserLoginLogController {
    @Autowired
    public IUserLoginLogService userLoginLogService;


    /**
     * 保存和修改公用的
     * @param userLoginLog  传递的实体
     * @return Ajaxresult转换结果
     */
    @PutMapping
    public AjaxResult addOrUpdate(@RequestBody UserLoginLog userLoginLog){
        try {
            if( userLoginLog.getId()!=null)
                userLoginLogService.updateById(userLoginLog);
            else
                userLoginLogService.insert(userLoginLog);
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
            userLoginLogService.deleteById(id);
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

            UserLoginLog userLoginLog = userLoginLogService.selectById(id);
            return AjaxResult.me().setResultObj(userLoginLog);
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
            List< UserLoginLog> list = userLoginLogService.selectList(null);
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
    public AjaxResult json(@RequestBody UserLoginLogQuery query)
    {
        Page<UserLoginLog> page = new Page<UserLoginLog>(query.getPage(),query.getRows());
        page = userLoginLogService.selectPage(page);
        PageList pageList = new PageList<UserLoginLog>(page.getTotal(),page.getRecords());
        return AjaxResult.me().setResultObj(pageList);
    }
}
