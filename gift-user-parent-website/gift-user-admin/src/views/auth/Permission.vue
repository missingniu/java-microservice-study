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
      <el-table-column prop="name" label="名称" width="100" sortable>
      </el-table-column>
      <el-table-column prop="url" label="路径" min-width="100" sortable>
      </el-table-column>
      <el-table-column prop="descs" label="描述" min-width="120" sortable>
      </el-table-column>
      <el-table-column prop="sn" label="唯一标识" min-width="120" sortable>
      </el-table-column>
      <el-table-column prop="parent.name" label="父级权限" min-width="120" sortable>
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
      saveLoading: false,
      saveFormRules: {
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' }//blur当鼠标失去焦点的时候
        ]
      },
      //新增界面数据
     saveForm: {
        name: '',
        intro:"",
       manager:{
         id:null,
         username:""
       },
       parent:{
         id:null,
         name:""
       },
       state:0,
      },

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
      this.getPermissions();
    },
    handleAdd:function(){

    },
    getPermissions(){
      this.$http.post("/permission",this.query)
      .then(result=>{
        result = result.data;
        console.log(result);
        this.pageInfo = result.resultObject;
        console.log(this.pageInfo)
          }).catch(result=>{
            this.$message({
              message:'网络错误!',
              type:'error'
            })
      })

    },
    // 就是当 当前页发生改变时就触发该方法
    handleCurrentChange(val) {
      // val它就是当前要访问的那个页码,当它发生改变时,需要修改query对象中的currentPage,只有当它发生改变,调到后端时,后端的currentPage才会拿到新的页码
      this.query.currentPage = val;
      // 重新调用getDepartment(),重新从后端获取数据渲染页面
      this.getPermissions();
    },
    //获取部门列表
    getDepartments() {
      console.log(this.query)
      this.$http.post("/department",this.query)
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
    //获取所有员工
    getEmployee(){
      this.$http.get("/employee")
          .then(result=>{
            result = result.data;
            console.log(result);
            this.employees = result.resultObject;
            console.log(this.employees);
      }).catch(result=>{
        console.log("fdf",result);
        this.$message({
          message:'网络错误!',
          type:'error'
        })
      })
    },
    selsChange: function (sels) {
      this.sels = sels;
    },


  },
  mounted() {
    // 页面加载完毕,调用部门的分页接口,获取数据渲染页面
    this.getPermissions();
  }
}

</script>

<style scoped>

</style>