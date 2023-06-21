<template>
  <section>
    <!--高级查询表单-->
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
      <el-table-column prop="username" label="名称" width="100" sortable>
      </el-table-column>
      <el-table-column prop="password"  label="密码" width="150" sortable>
        <template slot-scope="scope">
          <el-input v-model="scope.row.password" auto-complete="off" show-password class="borderNone"></el-input>
        </template>
      </el-table-column>
      <el-table-column prop="email" label="邮件" width="100" sortable>
      </el-table-column>
      <el-table-column prop="headImage" label="头像" min-width="50" sortable>
<!--        <el-button type="primary" @click="submitUpload">修改</el-button>-->
        <template #default="scope">
          <img :src="scope.row.headImage" width="40" height="40" />
        </template>
      </el-table-column>
      <el-table-column prop="age" label="年龄" min-width="50" sortable>
      </el-table-column>
      <el-table-column prop="department.name" label="部门" min-width="80" sortable>
      </el-table-column>
      <el-table-column label="操作" >
        <template slot-scope="scope">
          <el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDel(scope.$index, scope.row)">删除</el-button>
          <el-button type="danger" size="small" @click="setRole(scope.$index, scope.row)">设置角色</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-col :span="24" class="toolbar">
      <!--分页条
      total 就是当前满足条件的总条数
      @current-change="handleCurrentChange"  当当前页发生改变时会触发handleCurrentChange()
      -->
      <el-col :span="24" class="toolbar">
        <el-button type="danger" @click="batchRemove" :disabled="this.sels.length===0">批量删除</el-button>
        <el-pagination layout="sizes,prev, pager, next" @current-change="handleCurrentChange" @size-change="handleSizeChange"
                       :page-size="query.pageSize"
                       :page-sizes="[10, 20, 30, 40]"
                       :total="pageInfo.total"
                       :current-page="query.currentPage"
                       style="float:right;">
        </el-pagination>
      </el-col>
    </el-col>

    <!--新增界面-->
    <el-dialog :title="title" :visible.sync="saveFormVisible" :close-on-click-modal="false">
      <el-form :model="saveForm" label-width="80px" :rules="saveFormRules" ref="saveForm">
        <el-form-item label="名字" prop="username">
          <el-input v-model="saveForm.username" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="saveForm.password" auto-complete="off" show-password></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="saveForm.email" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="头像" prop="headImage">
          <el-input v-model="saveForm.headImage" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input v-model="saveForm.age" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="部门" prop="department">
          <el-select v-model="saveForm.department.id" clearable value-key="id" placeholder="请选择">
            <!--下拉选项
              :key=""  //唯一标识
              :label 选择之后展示到选择框中的值
              :value 选中之后绑定给模型层的值  如果要绑定对象给模型层 有一个大坑
                 必须要写  value-key="id"
            -->
            <el-option
                v-for="item in department"
                :key="item.id"
                :label="item.name"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="saveFormVisible = false">取消</el-button>
        <el-button type="primary" @click.native="saveSubmit" :loading="saveLoading">提交</el-button>
      </div>
    </el-dialog>
    <!--    给用户设置角色-->
    <el-dialog title="设置角色" :visible.sync="setRoleVisible" :close-on-click-modal="false">
      <el-form :model="empRole" label-width="80px" ref="setRoleForm">
        <!--indeterminate 属性用以表示 checkbox 的不确定状态，一般用于实现全选的效果  indeterminate的值如果为true,就表示当前子项目下有值被选中了会显示出一个横杠-->
        <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleRoleCheckAllChange">全选</el-checkbox>
        <!--循环后端返回的一级权限-->
        <div class="checkbox-table" v-for="(p,indexkey) in role" :key="p.id">
          <template>
            <!--显示一级菜单-->
            <el-checkbox class="check1" style="font-weight: 600;margin-bottom: 15px "
                         v-model='empRole.roleId' :label="p.id"
                         @change='handleRoleCheck(1,indexkey)'>
              {{p.name}}
            </el-checkbox>

          </template>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="setRoleVisible = false">取消</el-button>
        <el-button type="primary" @click.native="saveRoleMenu" :loading="saveLoading">提交</el-button>
      </div>
    </el-dialog>
  </section>
</template>
<script>
export default {
  data() {
    return {
      title:"新增或修改",
      filters: {
        name: ''
      },
      pageInfo:{
        rows: [],
        total: 0,
      },
      page: 1,
      listLoading: false,
      sels: [],//列表选中列

      editFormVisible: false,//编辑界面是否显示
      editLoading: false,
      editFormRules: {
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' }
        ]
      },
      saveFormVisible: false,//新增界面是否显示
      setRoleVisible:false,//设置角色弹框是否显示
      saveLoading: false,
      isIndeterminate:false,
      checkAll:[],
      saveFormRules: {
        username: [
          { required: true, message: '请输入姓名', trigger: 'blur' }//blur当鼠标失去焦点的时候
        ]
      },
      //新增界面数据
     saveForm: {
          username: '',
          password:"",
          email:"",
          headImage:"",
          age:"",
          department:{
            id:null,
            name:""
       },

      },
      empRole:{
        employeeId:null,
        roleId:[]
      },
      allRole:[],//装所有角色的
      role:[],
      department:[],
      employees:[],
      query:{
        // 查询对象,包括分页条件和高级查询条件
        currentPage:1,  // 当前页,默认是查询第一页
        pageSize:10,    // 每页显示条数,默认是10条,当你第一次访问这个页面时,需要查第几页,每页几条
        keyword:""
      },
      departments:[]
    }
  },
  methods: {
    search(){
      // 当 点击"查询"按钮时,要从第一页开始查询
      this.query.currentPage = 1;
      // 调用getDepartment方法查询数据
      this.getEmployee();
    },
    handleRoleCheckAllChange(val){
      //val是当前全选框选中状态true 表明全选框选中了 ，false全选框取消勾选
      this.empRole.roleId = val ? this.allRole:[];
      this.isIndeterminate = false;
    },
    // 就是当 当前页发生改变时就触发该方法
    handleCurrentChange(val) {
      // val它就是当前要访问的那个页码,当它发生改变时,需要修改query对象中的currentPage,只有当它发生改变,调到后端时,后端的currentPage才会拿到新的页码
      this.query.currentPage = val;
      // 重新调用getDepartment(),重新从后端获取数据渲染页面
      this.getEmployee();
    },
    //获取部门列表
    getEmployee() {
      console.log(this.query)
      this.$http.post("/employee",this.query)
          .then(result => {
            result = result.data;
            console.log(result);  // 后端返回的resultObj里面,包含rows(当前页数据) total 总条数
            this.pageInfo = result.resultObject;
            console.log(this.pageInfo)
          })
          .catch(result => {
            console.log(result)
            this.$message({
              message: '网络错误!',
              type: 'error'
            });
          })
    },
    handleSizeChange(val){
      this.query.pageSize = val;
      this.search();
    },

    //获取所有部门
    getDepartments(){
      this.$http.get("/department")
          .then(result=>{
            result = result.data;
            console.log(result);
            this.department = result.resultObject;
            console.log(this.department);
      }).catch(result=>{
        console.log("fdf",result);
        this.$message({
          message:'网络错误!',
          type:'error'
        })
      })
    },

    //删除  index:要删除的数据所在下标  row:要删除的数据,当前行数据对象
    handleDel: function (index, row) {
      this.$confirm('确认删除该记录吗?', '提示', {
        type: 'warning'
      }).then(() => {
        this.listLoading = true;  // 弹出加载框
        // 如果确认删除,要调用后端删除接口
        this.$http.delete("/employee/"+row.id)
            .then(result => {
              // 关闭加载框
              this.listLoading = false;
              // 进入then说明当前调用成功
              result = result.data; // 删除接口,后端会返回{success:true/false,message:""}
              if(result.success){
                this.$message({
                  message: '删除成功!',
                  type: 'success'
                });
                // 删除成功以后,需要重新调用查询接口渲染页面,注意:要从第一页开始重新查询数据,会出问题
                this.query.currentPage = 1;
                this.getEmployee();
              }else{
                this.$message({
                  message: '删除失败!',
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
      }).catch(() => {

      });
    },
    //显示编辑或者新增界面
    handleEdit: function (index, row) {
      this.saveFormVisible = true;
      this.saveForm = Object.assign({},row);

      this.getEmployee() ;
      this.getDepartments();
      // this.getDeptTree();

    },
    //显示新增页面
    handleAdd:function() {
      this.saveFormVisible = true;//显示弹框
      this.saveForm = {
        username: '',
        password:"",
        email:"",
        headImage:"",
        age:"",
        department:{
          id:null,
          name:""
        },
      };
      this.getEmployee();
      this.getDeptTree();
    },
    setRole:function(index,row){
        //弹出设置角色的弹框
      this.setRoleVisible = true;
      //获取所有角色
      this.getAllRole(row.id);
      //拿到当前行的id赋值给empRole对象的roleId
      this.empRole.employeeId = row.id;
    },
    //处理选择事件
    handleRoleCheck(type,a = 0){
      // 获取已选中的长度
      let checkedCount = this.empRole.roleId.length;
      // 如果已选中的长度==所有权限的长度，则checkAll=true，也就是全选框被选中
      this.checkAll = checkedCount === this.allRole.length;
      // 如果已选中的长度>0并且已选中的长度<所有权限的长度，则全选框是一个横杠
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.allRole.length;
    },
    getRoleByRoleId(roleId){
      this.$http.get("/employee/emp/"+roleId)
          .then(result => {
            console.log(result)
            result = result.data;
            if(result.success){
              this.empRole.roleId = result.resultObject;
              console.log(this.empRole.roleId);
              // 获取已选中的长度
              let checkedCount = this.empRole.roleId.length;
              // 如果已选中的长度==所有权限的长度，则checkAll=true，也就是全选框被选中
              this.checkAll = checkedCount === this.allRole.length;
              // 如果已选中的长度>0并且已选中的长度<所有权限的长度，则全选框是一个横杠
              this.isIndeterminate = checkedCount > 0 && checkedCount < this.allRole.length;
            }else{
              this.$message({
                message:result.message,
                type:'error'
              });
            }
          })
      .catch(error=>{
        this.$message({
          message:result.message,
          type:'error'
        });
      })
    },
    //提交角色
    saveRoleMenu(){
      this.$confirm('确认提交吗','提示',{}).then(()=>{
        this.saveLoading = true;
        this.$http.put("/employee/role",this.empRole).then(result=>{
          result = result.data;
          //关闭加载框
          this.saveLoading = false;
          if(result.success){
            this.$message({
              message:"设置角色成功",
              type:'success'
            });
            //关闭弹框
            this.setRoleVisible = false;
          }else{
            this.$message({
              message:result.message,
              type:'error'
            })
          }
        })
      })
    },
    //保存
    saveSubmit: function () {
      this.$refs.saveForm.validate((valid) => {
        if (valid) {//弹框判断是否显示
          this.$confirm('确认提交吗？', '提示', {}).then(() => {
            this.saveLoading = true;

            this.$http.put("/employee",this.saveForm)
            .then(result=>{
              result = result.data;
              this.saveLoading = false;
              if(result.success){
                this.$message({
                  message:'保存成功',
                  type:'success'
                });
                //关闭弹框
                this.saveFormVisible = false;
                //重新加载数据
                this.getEmployee();
              }else{
                this.$message({message:result.message,type:'error'})
              }
            }).catch(result=>{
              this.$message({
                message:'网络错误!',
                type:'error'
              })
            })

          });
        }
      });
    },
    selsChange: function (sels) {
      this.sels = sels;
    },
    //批量删除
    batchRemove: function () {
      // this.sels 当前被勾选中的所有的数据
      var ids = this.sels.map(item => item.id);  // 获取所有被勾选中的行的id
      this.$confirm('确认删除选中记录吗？', '提示', {
        type: 'warning'
      }).then(() => {
        this.listLoading = true;
        // 如果确认要删除,调用后端的批量删除接口
        this.$http.patch("/employee",ids)
            .then(result => {
              // 关闭加载框
              this.listLoading = false; // 因为就算是删除失败也要关闭加载框
              console.log("批量删除结果打印",result);
              result = result.data; // 后端返回的结果是封装到result.data属性中的
              console.log("批量删除后端返回的结果",result)
              if(result.success){
                this.$message({
                  message: '删除成功!',
                  type: 'success'
                });
                // 删除成功,从第一页开始重新查询数据
                this.query.currentPage = 1;
                this.getEmployee();
              }else{
                this.$message({
                  message: '删除失败!',
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
      }).catch(() => {

      });
    },
    getAllRole(roleId) {
      this.$http.get("/role")
      .then(result=>{
        result = result.data;
        if(result.success){
          this.role = result.resultObject;
          if(this.role && this.role.length>0){
            for(let i=0;i<this.role.length;i++){
              this.allRole.push(this.role[i].id);
            }
          }
          this.getRoleByRoleId(roleId);
        }else{
          this.$message({
            message:result.message,
            type:'error'
          })
        }
      })
    }
  },
  mounted() {
    // 页面加载完毕,调用部门的分页接口,获取数据渲染页面
    this.getEmployee();
  }
}
</script>

<style scoped>

</style>