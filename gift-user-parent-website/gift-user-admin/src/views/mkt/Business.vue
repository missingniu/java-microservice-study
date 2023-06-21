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
      <el-table-column prop="productName" label="产品名称" min-width="120" sortable>
      </el-table-column>
      <el-table-column prop="productPrice" label="产品价格" min-width="100" sortable>
      </el-table-column>
      <el-table-column prop="state" label="状态" min-width="100" sortable>
        <template slot-scope="scope">
          <span v-if="scope.row.state == 0" style="color: #8c939d">跟进中</span>
          <span v-if="scope.row.state == 1" style="color: #48ace6">缴纳定金</span>
          <span v-if="scope.row.state == 2" style="color: #11b95c">成单</span>
          <span v-if="scope.row.state == -1" style="color: red">商机池</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="250">
        <template slot-scope="scope">
          <el-button size="small" @click="handleEdit(scope.$index, scope.row)">跟进</el-button>
          <el-button type="danger" size="small" v-if="scope.row.state == 0" @click="handleDeposit(scope.$index, scope.row)">缴纳定金</el-button>
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
        <el-form-item label="名称" prop="name">
          <el-input v-model="saveForm.name" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input v-model="saveForm.price" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="saveForm.description" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="上级产品" prop="type">
          <el-select v-model="saveForm.type.id" clearable value-key="id" placeholder="请选择">
            <!--下拉选项
              :key=""  //唯一标识
              :label 选择之后展示到选择框中的值
              :value 选中之后绑定给模型层的值  如果要绑定对象给模型层 有一个大坑
                 必须要写  value-key="id"
            -->
            <el-option
                v-for="item in allType"
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
<!--    定金-->
    <el-dialog title="缴纳定金" :visible.sync="depositVisible" :close-on-click-modal="false">
      <el-form :model="depositDTO" label-width="80px" ref="depositForm">
        <el-form-item label="产品名称" >
          <el-input v-model="depositDTO.productName" disabled auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="产品价格">
          <el-input v-model="depositDTO.productPrice" disabled auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="定金金额">
          <el-input v-model="depositDTO.deposit" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="营销活动">
          <el-select v-model="depositDTO.businessIds" multiple placeholder="请选择">
            <el-option
                v-for="item in activities"
                :key="item.id"
                :label="item.name"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="depositVisible = false">取消</el-button>
        <el-button type="primary" @click.native="payDeposit" :loading="addLoading">提交</el-button>
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
      depositVisible:false,
      depositDTO:{
        businessId:null,
        productName:"",
        productPrice:"",
        deposit:null,
        businessIds:[]
      },
      activities:[],
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
        price:null,
        description:"",
        resource:"",
        type:{
          id:null,
          name:""
        },
      },

      allType:[],
      query:{
        // 查询对象,包括分页条件和高级查询条件
        currentPage:1,  // 当前页,默认是查询第一页
        pageSize:10,    // 每页显示条数,默认是10条,当你第一次访问这个页面时,需要查第几页,每页几条
        keyword:""
      },
      business:[]
    }
  },
  methods: {
    search(){
      // 当 点击"查询"按钮时,要从第一页开始查询
      this.query.currentPage = 1;
      // 调用getBusiness方法查询数据
      this.getBusiness();
    },
    handleDeposit(index,row){
      //支付定金弹框
      this.depositVisible = true;
      //弹框回显
      this.depositDTO.productName = row.productName;
      this.depositDTO.productPrice = row.productPrice;
      this.depositDTO.businessId = row.id;
      //查询当前在时间范围内的营销活动，渲染下拉框
      this.getActivities();
    },
    getActivities(){
      this.$http.get("activity/type/2")
      .then(result=>{
        result = result.data;
        if(result.success){
          this.activities = result.resultObject;
        }
      })
    },
    payDeposit(){
        this.$http.post("/business/pay/deposit",this.depositDTO)
      .then(result=>{
        result = result.data;
        if(result.success){
          this.$message({
            message:"操作成功",
            type:'success'
          })
          //关闭弹框
          this.depositVisible = false;
          this.getBusiness();
        }else{
          this.$message({
            message:result.message,
            type:'error'
          })
        }
      })
    },
    // 就是当 当前页发生改变时就触发该方法
    handleCurrentChange(val) {
      // val它就是当前要访问的那个页码,当它发生改变时,需要修改query对象中的currentPage,只有当它发生改变,调到后端时,后端的currentPage才会拿到新的页码
      this.query.currentPage = val;
      // 重新调用getBusiness(),重新从后端获取数据渲染页面
      this.getBusiness();
    },
    //获取部门列表
    getBusiness() {
      console.log(this.query)
      this.$http.post("/business",this.query)
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
        this.$http.delete("/business/"+row.id)
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
                this.getBusiness();
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
      if(!this.saveForm.type){//需要个初始值，防止回显得时候出错
        this.saveForm={
          id:null
        }
      }
      this.getBusiness();


    },
    //显示新增页面
    handleAdd:function() {
      this.saveFormVisible = true;//显示弹框
      this.saveForm = {
        name: '',
        price:null,
        description:"",
        resource:"",
        type:{
          id:null,
          name:""
        },
      };
      this.getBusiness();


    },

    //保存
    saveSubmit: function () {
      this.$refs.saveForm.validate((valid) => {
        if (valid) {
          this.$confirm('确认提交吗？', '提示', {}).then(() => {
            this.saveLoading = true;
            //级联下拉框被选中的是一个数组，包括所有层级的id
            //拿的是saveForm，parent是一个数组，和后端的不匹配
            var parent = this.saveForm.parent;
            if(parent && parent.length>0){//当前修改的是一个数组，不为空
              this.saveForm.parent = {id:parent[[parent.length-1]]};
            }else{
              this.saveForm.parent = {id:null};
            }
            console.log(this.saveForm);
            this.$http.put("/business",this.saveForm)
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
                    this.getBusiness();
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
        this.$http.patch("/business",ids)
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
                this.getBusiness();
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
    }
  },
  mounted() {
    // 页面加载完毕,调用部门的分页接口,获取数据渲染页面
    this.getBusiness();
  }
}

</script>

<style scoped>

</style>