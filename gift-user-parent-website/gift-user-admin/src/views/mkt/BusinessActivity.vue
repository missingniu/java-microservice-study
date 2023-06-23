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
      <el-table-column prop="type" label="类型" min-width="120" sortable>
      </el-table-column>
      <el-table-column prop="preferentialMethodId" label="优惠方式" min-width="100" sortable>
      </el-table-column>
<!--      <el-table-column prop="overMoney" label="满减" min-width="80" sortable>-->
<!--      </el-table-column>-->
      <el-table-column prop="description" label="描述" min-width="100" sortable>
      </el-table-column>
<!--      <el-table-column prop="subMoney" label="抵扣" min-width="80" sortable>-->
<!--      </el-table-column>-->
<!--      <el-table-column prop="endDate" label="结束时间" min-width="100" sortable>-->
<!--      </el-table-column>-->

<!--      <el-table-column prop="startDate" label="开始时间" min-width="100" sortable>-->
<!--      </el-table-column>-->
<!--      <el-table-column prop="creatTime" label="创建时间" min-width="100" sortable>-->
<!--      </el-table-column>-->
<!--      <el-table-column prop="creatBy" label="创建者" min-width="100" sortable>-->
<!--      </el-table-column>-->
<!--      <el-table-column prop="editTime" label="修改时间" min-width="100" sortable>-->
<!--      </el-table-column>-->
<!--      <el-table-column prop="editBy" label="最后一次修改者" min-width="80" sortable>-->
<!--      </el-table-column>-->
      <el-table-column label="操作" width="250">
        <template slot-scope="scope">
          <el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDel(scope.$index, scope.row)">删除</el-button>
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
    <el-dialog title="新增/修改" :visible.sync="saveFormVisible" :close-on-click-modal="false">
      <el-form :model="saveForm" label-width="80px" :rules="saveFormRules" ref="saveForm">
        <el-form-item label="名称">
          <el-input v-model="saveForm.name" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="类型">
          <el-radio v-model="saveForm.type" label="1">市场活动</el-radio>
          <el-radio v-model="saveForm.type" label="2">营销活动</el-radio>
        </el-form-item>
        <el-form-item label="开始时间">
          <el-date-picker
              v-model="saveForm.startDate"
              type="date"
              placeholder="选择日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间">
          <el-date-picker
              v-model="saveForm.endDate"
              type="date"
              placeholder="选择日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="优惠方式">
          <el-select v-model="saveForm.preferentialMethodId" :disabled="this.saveForm.type==1" placeholder="请选择">
            <el-option
                v-for="item in preferentialMethods"
                :key="item.id"
                :label="item.title"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="优惠条件">
          <el-input v-model="saveForm.overMoney" :disabled="this.saveForm.type==1" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="折扣金额">
          <el-input v-model="saveForm.subMoney" :disabled="this.saveForm.type==1" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="saveForm.description" auto-complete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="saveFormVisible = false">取消</el-button>
        <el-button type="primary" @click.native="saveSubmit" :loading="saveLoading">提交</el-button>
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
      saveLoading: false,
      saveFormRules: {
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' }//blur当鼠标失去焦点的时候
        ]
      },
      //新增界面数据
      saveForm: {
        id:null,
        name:"",
        type:null,
        startDate:"",
        endDate:"",
        description:"",
        preferentialMethodId:null,
        overMoney:null,
        subMoney:null
      },
      preferentialMethods:[],
      allType:[],
      query:{
        // 查询对象,包括分页条件和高级查询条件
        currentPage:1,  // 当前页,默认是查询第一页
        pageSize:10,    // 每页显示条数,默认是10条,当你第一次访问这个页面时,需要查第几页,每页几条
        keyword:""
      },
      activity:[]
    }
  },
  methods: {
    search(){
      // 当 点击"查询"按钮时,要从第一页开始查询
      this.query.currentPage = 1;
      // 调用getActivity方法查询数据
      this.getActivity();
    },
    // 就是当 当前页发生改变时就触发该方法
    handleCurrentChange(val) {
      // val它就是当前要访问的那个页码,当它发生改变时,需要修改query对象中的currentPage,只有当它发生改变,调到后端时,后端的currentPage才会拿到新的页码
      this.query.currentPage = val;
      // 重新调用getActivity(),重新从后端获取数据渲染页面
      this.getActivity();
    },
    //获取部门列表
    getActivity() {
      console.log(this.query)
      this.$http.post("/activity",this.query)
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

    //删除  index:要删除的数据所在下标  row:要删除的数据,当前行数据对象
    handleDel: function (index, row) {
      this.$confirm('确认删除该记录吗?', '提示', {
        type: 'warning'
      }).then(() => {
        this.listLoading = true;  // 弹出加载框
        // 如果确认删除,要调用后端删除接口
        this.$http.delete("/activity/"+row.id)
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
                this.getActivity();
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
    getAllactivityType() {
      console.log(this.query)
      this.$http.get("/activityType")
          .then(result => {
            result = result.data;
            console.log(result);  // 后端返回的resultObj里面,包含rows(当前页数据) total 总条数
            this.allType = result.resultObject;
          })
          .catch(result => {
            console.log(result)
            this.$message({
              message: '网络错误!',
              type: 'error'
            });
          })
    },
    //显示编辑或者新增界面
    handleEdit: function (index, row) {
      this.saveFormVisible = true;
      this.saveForm = Object.assign({},row);
      this.getPreferentialMethods();

    },
    //显示新增页面
    handleAdd:function() {
      this.saveFormVisible = true;//显示弹框
      this.saveForm = {
        id:null,
        name:"",
        startDate:"",
        endDate: "",
        description: "",
        preferentialMethodId: null,
        overMoney: null,
        subMoney: null
      };

      this.getPreferentialMethods();


    },

    //保存
    saveSubmit: function () {
      this.$refs.saveForm.validate((valid) => {
        if (valid) {
          this.$confirm('确认提交吗？', '提示', {}).then(() => {
            this.saveLoading = true;
            this.$http.put("/activity",this.saveForm)
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
                    this.getActivity();
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
        this.$http.patch("/activity",ids)
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
                this.getActivity();
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
    getPreferentialMethods() {
        this.$http.get("/dictionaryItem/sn/activityMethod")
      .then(result=>{
        result = result.data;
        if(result.success){
          this.preferentialMethods = result.resultObject;
        }
      })
    }
  },
  mounted() {
    // 页面加载完毕,调用部门的分页接口,获取数据渲染页面
    this.getActivity();
  }
}

</script>

<style scoped>

</style>