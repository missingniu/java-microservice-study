<template>
  <section>
    <!--工具条-->
    <el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
      <el-form :inline="true" :model="query">
        <el-form-item>
          <el-input v-model="query.keyword" placeholder="关键字"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" v-on:click="search">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleAdd">新增</el-button>
        </el-form-item>
      </el-form>
    </el-col>

    <!--列表-->
    <el-table :data="pageInfo.rows" highlight-current-row v-loading="listLoading" @selection-change="selsChange" style="width: 100%;">
      <el-table-column type="selection" width="55">
      </el-table-column>
      <el-table-column type="index" width="60">
      </el-table-column>
      <el-table-column prop="name" label="名称" sortable>
      </el-table-column>
      <el-table-column prop="sn" label="标识" sortable>
      </el-table-column>
      <el-table-column label="操作">
        <template scope="scope">
          <el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDel(scope.$index, scope.row)">删除</el-button>
          <el-button type="danger" size="small" @click="handlePermission(scope.$index, scope.row)">设置权限</el-button>
          <el-button type="danger" size="small" @click="handleMenu(scope.$index, scope.row)">设置菜单</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!--工具条-->
    <el-col :span="24" class="toolbar">
      <el-button type="danger" @click="batchRemove" :disabled="this.sels.length===0">批量删除</el-button>
      <el-pagination
          layout="prev, pager, next"
          @current-change="handleCurrentChange"
          :page-size="query.pageSize"
          :total="pageInfo.total" style="float:right;">
      </el-pagination>
    </el-col>

    <el-dialog title="新增/修改" :visible.sync="saveFormVisible" :close-on-click-modal="false">
      <el-form :model="saveForm" label-width="80px" :rules="saveFormRules" ref="saveForm">
        <!--prop="name" 引用saveFormRules规则中的 name规则-->
        <el-form-item label="名称" prop="name">
          <el-input v-model="saveForm.name" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="标识" prop="sn">
          <el-input v-model="saveForm.sn" auto-complete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="saveFormVisible = false">取消</el-button>
        <el-button type="primary" @click.native="save" :loading="addLoading">提交</el-button>
      </div>
    </el-dialog>

    <!--给角色设置权限-->
    <el-dialog title="设置权限" :visible.sync="setPermissionVisible" :close-on-click-modal="false">
      <el-form :model="rolePermission" label-width="80px" ref="setPermissionForm">
        <!--indeterminate 属性用以表示 checkbox 的不确定状态，一般用于实现全选的效果  indeterminate的值如果为true,就表示当前子项目下有值被选中了会显示出一个横杠-->
        <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
        <!--循环后端返回的一级权限-->
        <div class="checkbox-table" v-for="(p,indexkey) in permissionTree" :key="p.sn">
          <template>
            <!--显示一级权限-->
            <el-checkbox class="check1" style="font-weight: 600;margin-bottom: 15px "
                         v-model='rolePermission.permissionSns' :label="p.sn"
                         @change='handleCheck(1,indexkey)'>
              {{p.name}}
            </el-checkbox>
            <div style="margin-bottom: 20px;">
              <!--循环一级权限的子权限-->
              <div v-for="c in p.children" class="line-check" :key="c.sn"
                   style="display: inline-block; margin-left: 20px;margin-bottom: 20px;" >
                <el-checkbox class="check2" @change='handleCheck(2,indexkey)'
                             v-model="rolePermission.permissionSns" :label="c.sn">
                  {{c.name}}
                </el-checkbox>
              </div>
            </div>
          </template>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="setPermissionVisible=false">取消</el-button>
        <el-button type="primary" @click.native="saveRolePermission" :loading="saveLoading">提交</el-button>
      </div>
    </el-dialog>
    <!--    给角色设置菜单-->
    <el-dialog title="设置菜单" :visible.sync="setMenuVisible" :close-on-click-modal="false">
      <el-form :model="roleMenu" label-width="80px" ref="setMenuForm">
        <!--indeterminate 属性用以表示 checkbox 的不确定状态，一般用于实现全选的效果  indeterminate的值如果为true,就表示当前子项目下有值被选中了会显示出一个横杠-->
        <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleMenuCheckAllChange">全选</el-checkbox>
        <!--循环后端返回的一级权限-->
        <div class="checkbox-table" v-for="(p,indexkey) in menuTree" :key="p.id">
          <template>
            <!--显示一级菜单-->
            <el-checkbox class="check1" style="font-weight: 600;margin-bottom: 15px "
                         v-model='roleMenu.menuId' :label="p.id"
                         @change='handleMenuCheck(1,indexkey)'>
              {{p.name}}
            </el-checkbox>
            <div style="margin-bottom: 20px;">
              <!--循环一级菜单的子菜单-->
              <div v-for="c in p.children" class="line-check" :key="c.id"
                   style="display: inline-block; margin-left: 20px;margin-bottom: 20px;" >
                <el-checkbox class="check2" @change='handleMenuCheck(2,indexkey)'
                             v-model="roleMenu.menuId" :label="c.id">
                  {{c.name}}
                </el-checkbox>
              </div>
            </div>
          </template>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="setMenuVisible = false">取消</el-button>
        <el-button type="primary" @click.native="saveRoleMenu" :loading="saveLoading">提交</el-button>
      </div>
    </el-dialog>
  </section>
</template>

<script>

import {addUser, removeUser} from "../../api/api";
import util from "../../common/js/util";

export default {
  data() {
    return {
      listLoading: false,
      sels: [],//列表选中行

      saveFormRules: {
        name: [
          { required: true, message: '请输入名称', trigger: 'blur' }
        ],
        sn: [
          { required: true, message: '请输入标识', trigger: 'blur' }
        ]
      },

      // query对象是前端用来向后端传递参数的
      query:{
        currentPage:1,
        pageSize:10,
        keyword:""
      },
      // pageInfo它是用来接收后端返回的数据的.用来渲染table表格和分页条的
      pageInfo:{
        total:0,
        rows:[]
      },

      saveForm:{
        id:null,
        name:"",
        sn:""
      },
      addLoading:false,    // 保存按钮,加载框
      saveFormVisible:false,   // 用来弹窗是否显示的
      cancelFormVisible:false,//取消弹框
      setPermissionVisible:false,
      setMenuVisible:false,
      isIndeterminate:false,
      checkAll:[],
      permissionTree:[],    // 装所有一级权限以及它的二级权限
      allPermissionSns:[],    // 装所有权限(一级权限,二级权限)的sn的,用来处理全选的
      allMenu:[],
      menuTree:[],//装所有一级菜单和二级菜单
      rolePermission:{
        roleId:null,
        permissionSns:[]    // 用来接收当前所有被勾选中的权限sn的
      },
      roleMenu:{
        roleId:null,
        menuId:[]
      },
      saveLoading:false   // 设置权限那个加载框
    }
  },
  methods: {
    handleCheckAllChange(val){
      //  val是当前全选框的选中状态 true 表明全选框被选中了  false 全选框取消勾选
      this.rolePermission.permissionSns = val ? this.allPermissionSns : [];
      this.isIndeterminate = false;
    },
    handleMenuCheckAllChange(val){
      //  val是当前全选框的选中状态 true 表明全选框被选中了  false 全选框取消勾选
      this.roleMenu.menuId = val ? this.allMenu : [];
      this.isIndeterminate = false;
    },
    // 处理选择事件  type 1:一级权限2:二级权限   a：一级权限的索引位置
    handleCheck(type, a = 0) {
      // 多选框
      // 在已选中的权限列表中，查看是否存在一级权限 >-1 存在  <0 不存在
      let indexOf = this.rolePermission.permissionSns.indexOf(this.permissionTree[a].sn);
      if (type == 2) { // 二级权限点击
        let index = 0;  //
        // 拿到当前a这个下标所对应的一级权限的儿子
        this.permissionTree[a].children.map(item => {
          // 已选中的权限列表中，是否包含当前一级权限下的某个子权限
          if (this.rolePermission.permissionSns.indexOf(item.sn) > -1) {
            index += 1;
          }
        })
        if (index > 0) {  // 已选中的权限列表中，包含当前一级权限下的某个子权限
          if (indexOf < 0) {  // 已选中的权限列表中，没有当前一级权限，则添加到已选中的权限列表中
            this.rolePermission.permissionSns.push(this.permissionTree[a].sn);
          }
        } else {  // // 已选中的权限列表中，不包含当前一级权限下的某个子权限
          if (indexOf > -1) {// 已选中的权限列表中，有当前一级权限，则删除
            this.rolePermission.permissionSns.splice(indexOf, 1);
          }
        }
      } else {  // 一级菜单点击
        if (indexOf > -1) { // 已选中的权限中，包含当前一级权限，则将该一级权限下所有的子权限选中
          this.permissionTree[a].children.map(item => {
            if (this.rolePermission.permissionSns.findIndex((n) => n == item.sn) < 0) {
              this.rolePermission.permissionSns.push(item.sn)
            }
          })
        } else {
          // 已选中的权限中，不包含当前一级权限，则将该一级权限下所有的子权限移除
          this.permissionTree[a].children.map(item => {
            if (this.rolePermission.permissionSns.findIndex((n) => n == item.sn) > -1) {
              this.rolePermission.permissionSns.splice(this.rolePermission.permissionSns.findIndex((n) => n == item.sn), 1);
            }
          })
        }
      }
      // 获取已选中的长度
      let checkedCount = this.rolePermission.permissionSns.length;
      // 如果已选中的长度==所有权限的长度，则checkAll=true，也就是全选框被选中
      this.checkAll = checkedCount === this.allPermissionSns.length;
      // 如果已选中的长度>0并且已选中的长度<所有权限的长度，则全选框是一个横杠
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.allPermissionSns.length;
    },
    handlePermission(index,row){
      // 弹出 设置权限 弹框
      this.setPermissionVisible = true;
      // 获取所有的一级权限和它的二级权限 ,赋值给permissionTree,用来渲染页面
      this.getPermissionTree(row.id);
      // 拿到当前行的id赋值给rolePermission对象的roleId
      this.rolePermission.roleId = row.id;
    },
    getPermissionSnsByRoleId(roleId){
      this.$http.get("/role/permissionSns/"+roleId)
          .then(result => {
            result = result.data;
            if(result.success){
              this.rolePermission.permissionSns = result.resultObject;
              // 获取已选中的长度
              let checkedCount = this.rolePermission.permissionSns.length;
              // 如果已选中的长度==所有权限的长度，则checkAll=true，也就是全选框被选中
              this.checkAll = checkedCount === this.allPermissionSns.length;
              // 如果已选中的长度>0并且已选中的长度<所有权限的长度，则全选框是一个横杠
              this.isIndeterminate = checkedCount > 0 && checkedCount < this.allPermissionSns.length;
            }
          })
    },
    getPermissionTree(roleId){
      this.$http.get("/permission/tree")
          .then(result => {
            result = result.data;
            if(result.success){
              this.permissionTree = result.resultObject;
              // 拿到所有权限的sn,交给this.allPermissionSns
              if(this.permissionTree && this.permissionTree.length>0){
                for(let i=0;i<this.permissionTree.length;i++){
                  this.allPermissionSns.push(this.permissionTree[i].sn);
                  let childs = this.permissionTree[i].children;
                  for(let j=0;j<childs.length;j++){
                    this.allPermissionSns.push(childs[j].sn);
                  }
                }
              }
              // 根据角色ID获取该角色绑定的权限sn
              this.getPermissionSnsByRoleId(roleId);
            }else{
              this.$message({
                message: result.message,
                type: 'error'
              });
            }
          })
    },
    // 处理选择事件  type 1:一级菜单2:二级菜单   a：一级菜单的索引位置
    handleMenuCheck(type, a = 0) {
      // 多选框
      // 在已选中的角色菜单中，查看是否存在一级菜单 >-1 存在  <0 不存在
      let indexOf = this.roleMenu.menuId.indexOf(this.menuTree[a].id);
      if (type == 2) { // 二级权限点击
        let index = 0;  //
        // 拿到当前a这个下标所对应的一级权限的儿子
        this.menuTree[a].children.map(item => {
          // 已选中的权限列表中，是否包含当前一级权限下的某个子权限
          if (this.roleMenu.menuId.indexOf(item.id) > -1) {
            index += 1;
          }
        })
        if (index > 0) {  // 已选中的权限列表中，包含当前一级权限下的某个子权限
          if (indexOf < 0) {  // 已选中的权限列表中，没有当前一级权限，则添加到已选中的权限列表中
            this.roleMenu.menuId.push(this.menuTree[a].id);
          }
        } else {  // // 已选中的权限列表中，不包含当前一级权限下的某个子权限
          if (indexOf > -1) {// 已选中的权限列表中，有当前一级权限，则删除
            this.roleMenu.menuId.splice(indexOf, 1);
          }
        }
      } else {  // 一级菜单点击
        if (indexOf > -1) { // 已选中的权限中，包含当前一级权限，则将该一级权限下所有的子权限选中
          this.menuTree[a].children.map(item => {
            if (this.roleMenu.menuId.findIndex((n) => n == item.id) < 0) {
              this.roleMenu.menuId.push(item.id)
            }
          })
        } else {
          // 已选中的权限中，不包含当前一级权限，则将该一级权限下所有的子权限移除
          this.menuTree[a].children.map(item => {
            if (this.roleMenu.menuId.findIndex((n) => n == item.id) > -1) {
              this.roleMenu.menuId.splice(this.roleMenu.menuId.findIndex((n) => n == item.id), 1);
            }
          })
        }
      }
      // 获取已选中的长度
      let checkedCount = this.roleMenu.menuId.length;
      // 如果已选中的长度==所有权限的长度，则checkAll=true，也就是全选框被选中
      this.checkAll = checkedCount === this.allMenu.length;
      // 如果已选中的长度>0并且已选中的长度<所有权限的长度，则全选框是一个横杠
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.allMenu.length;
    },
    handleMenu:function(index,row){
      // 弹出 设置权限 弹框
      this.setMenuVisible = true;
      // 获取所有的一级菜单和它的二级菜单 ,赋值给menuTree,用来渲染页面
      this.getMenuTree(row.id);
      // 拿到当前行的id赋值给roleMenu对象的roleId
      this.roleMenu.roleId = row.id;
    },
    getMenuByRoleId(roleId){
      this.$http.get("/role/menuId/"+roleId)
          .then(result => {
            result = result.data;
            if(result.success){
              this.roleMenu.menuId = result.resultObject;
              // 获取已选中的长度
              let checkedCount = this.roleMenu.menuId.length;
              // 如果已选中的长度==所有权限的长度，则checkAll=true，也就是全选框被选中
              this.checkAll = checkedCount === this.allMenu.length;
              // 如果已选中的长度>0并且已选中的长度<所有权限的长度，则全选框是一个横杠
              this.isIndeterminate = checkedCount > 0 && checkedCount < this.allMenu.length;
            }
          })
    },
    getMenuTree(roleId){
      this.$http.get("/menu/tree")
          .then(result => {
            result = result.data;
            if(result.success){
              this.menuTree = result.resultObject;
              // 拿到所有权限的sn,交给this.allPermissionSns
              if(this.menuTree && this.menuTree.length>0){
                for(let i=0;i<this.menuTree.length;i++){
                  this.allMenu.push(this.menuTree[i].id);
                  let childs = this.menuTree[i].children;
                  for(let j=0;j<childs.length;j++){
                    this.allMenu.push(childs[j].id);
                  }
                }
              }
              // 根据角色ID获取该角色绑定的权限sn
              this.getMenuByRoleId(roleId);
            }else{
              this.$message({
                message: result.message,
                type: 'error'
              });
            }
          })
    },
    saveRolePermission(){
      this.$confirm('确认提交吗？', '提示', {}).then(() => {
        this.saveLoading = true;
        this.$http.put("/role/permission",this.rolePermission)
            .then(result => {
              result = result.data;
              // success message
              // 关加载框
              this.saveLoading = false;

              if(result.success){
                this.$message({
                  message: "设置权限成功",
                  type: 'success'
                });
                // 关弹框
                this.setPermissionVisible = false;
              }else{
                this.$message({
                  message: result.message,
                  type: 'error'
                });
              }
            })
      });
    },
    saveRoleMenu:function(){
        this.$confirm('确定提交吗','提示',{}).then(()=>{
          this.saveLoading = true;
          this.$http.put("/role/menu",this.roleMenu).then(result=>{
            result = result.data;
            //关闭加载框
            this.saveLoading = false;
            if(result.success){
              this.$message({
                message:"设置菜单成功",
                type:'success'
              });
              this.setMenuVisible = false;
            }else{
              this.$message({
                message:result.message,
                type:'error',
              })
            }
          })
        })
    },
    save: function () {
      // 校验表单中的输入是否满足规则
      this.$refs.saveForm.validate((valid) => {
        // valid==true,就说明表单是满足校验规则的
        if (valid) {
          this.$confirm('确认提交吗？', '提示', {}).then(() => {
            this.addLoading = true;
            this.$http.put("/role",this.saveForm)
                .then(result => {
                  result = result.data;
                  // success message
                  // 关加载框
                  this.addLoading = false;

                  if(result.success){
                    this.$message({
                      message: "保存成功",
                      type: 'success'
                    });
                    // 刷新表格数据
                    this.getRoles();

                    // 关弹框
                    this.saveFormVisible = false;
                  }else{
                    this.$message({
                      message: result.message,
                      type: 'error'
                    });
                  }
                })
          });
        }
      });
    },
    handleAdd(){
      // 弹出 新增弹框
      this.saveFormVisible = true;
      // 赋初始值=空值,为了防止将上一次填写的内容带到下一次
      this.saveForm = {
        id:null,
        name:"",
        sn:""
      }
    },
    handleEdit(index,row){
      // 弹出 修改弹框
      this.saveFormVisible = true;
      // 回显  利用双向绑定原则做回显
      this.saveForm = Object.assign({},row);
    },
    batchRemove: function () {
      this.$confirm('确认删除该记录吗?', '提示', {
        type: 'warning'
      }).then(() => {
        this.listLoading = true;  // 加载框
        // 获取要删除数据的id
        let ids = this.sels.map(x => x.id);

        this.$http.patch("/role",ids)
            .then(result => {
              result = result.data;
              // result {success:true,message:""}
              if(result.success){
                // 删除成功
                this.$message({
                  message: "删除成功",
                  type: 'success'
                });
                // 关闭加载框
                this.listLoading = false;
                // 跳转到第一页重新查询
                this.search();
              }
            })
      }).catch(() => {

      });
    },
    selsChange(val){
      // val 就是当前被选中的所有的行的对象
      this.sels = val;
    },
    handleDel: function (index, row) {
      this.$confirm('确认删除该记录吗?', '提示', {
        type: 'warning'
      }).then(() => {
        this.listLoading = true;  // 加载框
        this.$http.delete("/role/"+row.id)
            .then(result => {
              result = result.data;
              // result {success:true,message:""}
              if(result.success){
                // 删除成功
                this.$message({
                  message: "删除成功",
                  type: 'success'
                });
                // 关闭加载框
                this.listLoading = false;
                // 跳转到第一页重新查询
                this.search();
              }
            })
      }).catch(() => {

      });
    },
    search(){
      // 将当前页的值改为1
      this.query.currentPage = 1;
      this.getRoles();
    },
    handleCurrentChange(val) {
      // val 当前要跳转的页码
      this.query.currentPage = val;
      this.getRoles();
    },
    //获取权限列表
    getRoles() {
      this.$http.post("/role",this.query)
          .then(result => {
            result = result.data;
            console.log(result);
            if(result.success){ // 判断后端操作是否成功
              this.pageInfo = result.resultObject;
            }else{
              this.$message({
                message: result.message,
                type: 'error'
              });
            }
          })
          .catch(result => {
            this.$message({
              message: '网络错误!',
              type: 'error'
            });
          })
    },
  },
  mounted() {
    // 页面一加载完毕,就要调用后端接口渲染列表
    this.getRoles();
  }
}

</script>

<style scoped>

</style>