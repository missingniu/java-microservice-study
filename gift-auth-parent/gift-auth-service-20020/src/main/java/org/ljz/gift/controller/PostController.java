package org.ljz.gift.controller;

import org.ljz.gift.service.IPostService;
import org.ljz.gift.domain.Post;
import org.ljz.gift.query.PostQuery;
import com.baomidou.mybatisplus.plugins.Page;
import org.ljz.gift.util.AjaxResult;
import org.ljz.gift.util.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {
    @Autowired
    public IPostService postService;


    /**
     * 保存和修改公用的
     * @param post  传递的实体
     * @return Ajaxresult转换结果
     */
    @PutMapping
    public AjaxResult addOrUpdate(@RequestBody Post post){
        try {
            if( post.getId()!=null)
                postService.updateById(post);
            else
                postService.insert(post);
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
            postService.deleteById(id);
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

            Post post = postService.selectById(id);
            return AjaxResult.me().setResultObj(post);
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
            List< Post> list = postService.selectList(null);
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
    public AjaxResult json(@RequestBody PostQuery query)
    {
        Page<Post> page = new Page<Post>(query.getPage(),query.getRows());
        page = postService.selectPage(page);
        PageList pageList = new PageList<Post>(page.getTotal(),page.getRecords());
        return AjaxResult.me().setResultObj(pageList);
    }
}
