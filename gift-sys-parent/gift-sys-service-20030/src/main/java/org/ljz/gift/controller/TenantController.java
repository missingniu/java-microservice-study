package org.ljz.gift.controller;

import org.ljz.gift.service.ITenantService;
import org.ljz.gift.domain.Tenant;
import org.ljz.gift.query.TenantQuery;
import com.baomidou.mybatisplus.plugins.Page;
import org.ljz.gift.util.AjaxResult;
import org.ljz.gift.util.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tenant")
public class TenantController {
    @Autowired
    public ITenantService tenantService;


    /**
     * 保存和修改公用的
     * @param tenant  传递的实体
     * @return Ajaxresult转换结果
     */
    @PutMapping
    public AjaxResult addOrUpdate(@RequestBody Tenant tenant){
        try {
            if( tenant.getId()!=null)
                tenantService.updateById(tenant);
            else
                tenantService.insert(tenant);
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
            tenantService.deleteById(id);
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

            Tenant tenant = tenantService.selectById(id);
            return AjaxResult.me().setResultObj(tenant);
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
            List< Tenant> list = tenantService.selectList(null);
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
    @PostMapping
    public AjaxResult json(@RequestBody TenantQuery query)
    {
        //自定义sql,完成关联查询
        PageList<Tenant> pageList = tenantService.selectPageList(query);
        return AjaxResult.me().setResultObj(pageList);
    }
    @PostMapping("/settlement")
    public AjaxResult handleSettlement(@RequestBody Tenant tenant){
        return tenantService.dealSettlement(tenant);

    }
}
