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
        <el-form-item>
          <el-button type="primary" @click="handleBatchAssign" :disabled="this.sels.length == 0">批量分配</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="importClue" >导入线索</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="exportClue" >导出线索</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-table :data="pageInfo.rows" highlight-current-row v-loading="listLoading" @selection-change="selsChange" style="width: 100%;">
      <el-table-column type="selection" width="55">
      </el-table-column>
      <el-table-column type="index" width="60">
      </el-table-column>
      <el-table-column prop="fullName" label="全名" width="100" sortable>
      </el-table-column>
      <el-table-column prop="appellation" label="昵称" width="100" sortable>
      </el-table-column>
      <el-table-column prop="company" label="公司" width="100" sortable>
      </el-table-column>
      <el-table-column prop="email" label="邮箱" width="100" sortable>
      </el-table-column>
      <el-table-column prop="phone" label="电话" width="100" sortable>
      </el-table-column>
      <el-table-column prop="state" label="状态"  width="100" sortable>
        <template slot-scope="scope">
          <span v-if="scope.row.state==0" style="color:grey">放入线索池</span>
          <span v-if="scope.row.state==1" style="color:skyblue">未分配</span>
          <span v-if="scope.row.state==3" style="color:lawngreen">转化商机</span>
          <span v-if="scope.row.state==2" style="color:lawngreen">跟进中</span>

        </template>
      </el-table-column>
      <el-table-column prop="description" label="描述" width="100" sortable>
      </el-table-column>
      <el-table-column label="操作" >
        <template slot-scope="scope">
          <el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleClueActivity(scope.$index, scope.row)">关联活动</el-button>
          <el-button type="danger" size="small" v-if="scope.row.state == 2" @click="handleDel(scope.$index, scope.row)">跟进</el-button>
          <el-button type="danger" size="small" v-if="scope.row.state == 2 " @click="handleBusiness(scope.$index, scope.row)">转化商机</el-button>
          <el-button type="danger" size="small" v-if ="scope.row.state==1" @click="handleSingleAssign(scope.$index, scope.row)" >分配</el-button>
          <el-button type="danger" v-if="scope.row.state == 2 || scope.row.state == 3" size="small" @click="handleClueActivity(scope.$index, scope.row)">变更</el-button>
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
        <el-form-item label="全名" prop="fullName">
          <el-input v-model="saveForm.fullName" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="昵称" prop="appellation">
          <el-input v-model="saveForm.appellation" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="公司" prop="company">
          <el-input v-model="saveForm.company" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="saveForm.email" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="saveForm.phone" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="saveForm.description" auto-complete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="saveFormVisible = false">取消</el-button>
        <el-button type="primary" @click.native="saveSubmit" :loading="saveLoading">提交</el-button>
      </div>
    </el-dialog>
<!--    关联活动-->
    <el-dialog title="关联活动" :visible.sync="clueActivityVisible" :close-on-click-modal="false">
      <el-form :model="clueActivity" label-width="80px" :rules="saveFormRules" ref="saveForm">
        <el-form-item label="称呼">
          <el-input v-model="clueActivity.clueName" disabled auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="市场活动">
          <el-select v-model="clueActivity.activityIds" multiple placeholder="请选择">
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
        <el-button @click.native="cancelActivity">取消</el-button>
        <el-button type="primary" @click.native="saveClueActivity" :loading="addLoading">提交</el-button>
      </div>
    </el-dialog>
<!--    转化商机-->
    <el-dialog title="转化商机" :visible.sync="businessVisible" :close-on-click-modal="false">
      <el-form :model="business" label-width="80px" ref="businessForm">
        <el-form-item label="名称">
          <el-input v-model="business.fullName" disabled auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="business.phone" disabled auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="产品信息">
          <el-select v-model="business.productId" placeholder="请选择">
            <el-option
                v-for="item in products"
                :key="item.id"
                :label="item.name"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="businessVisible = false">取消</el-button>
        <el-button type="primary" @click.native="saveBusiness" :loading="addLoading">提交</el-button>
      </div>
    </el-dialog>
<!--    批量分配-->
    <el-dialog title="批量分配" :visible.sync="batchAssignVisible" :close-on-click-modal="false">
      <el-form :model="batchAssign" label-width="80px" ref="batchAssignForm">
        <el-form-item label="营销人员">
          <el-select v-model="batchAssign.owner" placeholder="请选择">
            <el-option
                v-for="item in employees"
                :key="item.id"
                :label="item.username"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="batchAssignVisible = false">取消</el-button>
        <el-button type="primary" @click.native="saveBatchAssign" :loading="addLoading">提交</el-button>
      </div>
    </el-dialog>
    <!--    单独分配-->
    <el-dialog title="分配" :visible.sync="singleAssignVisible" :close-on-click-modal="false">
      <el-form :model="singleAssign" label-width="80px" >
        <el-form-item label="营销人员">
          <el-select v-model="singleAssign.owner" placeholder="请选择">
            <el-option
                v-for="item in employees"
                :key="item.id"
                :label="item.username"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="cancel">取消</el-button>
        <el-button type="primary" @click.native="saveSingleAssign" :loading="addLoading">提交</el-button>
      </div>
    </el-dialog>
<!--    导入线索页面-->

    <el-dialog title="导入线索" :visible.sync="clueFormVisible" :close-on-click-modal="false">
      <el-form :model="clueForm" label-width="80px" ref="clueForm" enctype="multipart/form-data">
        <el-form-item label="标题">
          <el-input  auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="线索源">
          <el-upload drag
                     :limit=limitNum
                     :auto-upload="false"
                     accept=".xlsx"
                     :action="UploadUrl()"
                     :before-upload="beforeUploadFile"
                     :on-change="fileChange"
                     :on-exceed="exceedFile"
                     :on-success="handleSuccess"
                     :on-error="handleError"
                     :file-list="fileList">
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <div class="el-upload__tip" slot="tip">只能上传xlsx文件，且不超过10M</div>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="clueFormVisible = false">取消</el-button>
        <el-button type="primary" @click.native="uploadFile" :loading="addLoading">提交</el-button>
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
      clueFormVisible:false,
      editFormRules: {
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' }
        ]
      },
      saveFormVisible: false,//新增界面是否显示
      saveLoading: false,
      singleAssignVisible:false,//控制单独分配显示
      saveFormRules: {
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' }//blur当鼠标失去焦点的时候
        ]
      },
      //新增界面数据
      saveForm: {
        fullName: '',
        appellation:"",
        company:"",
        email:"",
        phone:"",

      },
      fileList:[],
      clueForm:{

      },
      addLoading:false,    // 保存按钮,加载框
      clueTypes:[],
      allType:[],
      query:{
        // 查询对象,包括分页条件和高级查询条件
        currentPage:1,  // 当前页,默认是查询第一页
        pageSize:10,    // 每页显示条数,默认是10条,当你第一次访问这个页面时,需要查第几页,每页几条
        keyword:""
      },
      clue:[],
     batchAssignVisible:false,
         employees:[],
        batchAssign:{
          owner:null,
          state:null,// 所属营销人员
    },
      singleAssign:{
        owner:null,
        state:null,
    },
      clueActivityVisible:false,
      clueActivity:{
        clueId:null,
        activityIds:[],
        clueName:""
      },
      activities:[],
    businessVisible:false,
        products:[],
        business:{
         clueId:null,
          fullName:"",
          phone:"",
          productId:null
    }
    }

  },
  methods: {
    search(){
      // 当 点击"查询"按钮时,要从第一页开始查询
      this.query.currentPage = 1;
      // 调用getProduct方法查询数据
      this.getClue();
    },
    // 文件超出个数限制时的钩子
    exceedFile(files, fileList) {
      this.$message.warning(`只能选择 ${this.limitNum} 个文件，当前共选择了 ${files.length + fileList.length} 个`);
    },
    // 文件状态改变时的钩子
    fileChange(file, fileList) {
      console.log(file.raw);
      this.fileList.push(file.raw) ;
      console.log(this.fileList);
    },
    // 上传文件之前的钩子, 参数为上传的文件,若返回 false 或者返回 Promise 且被 reject，则停止上传
    beforeUploadFile(file) {
      console.log('before upload');
      console.log(file);
      let extension = file.name.substring(file.name.lastIndexOf('.')+1);
      let size = file.size / 1024 / 1024;
      if(extension !== 'xlsx') {
        this.$message.warning('只能上传后缀是.xlsx的文件');
      }
      if(size > 10) {
        this.$message.warning('文件大小不得超过10M');
      }
    },
    // 文件上传成功时的钩子
    handleSuccess(res, file, fileList) {
      this.$message.success('文件上传成功');
    },
    // 文件上传失败时的钩子
    handleError(err, file, fileList) {
      this.$message.error('文件上传失败');
    },
    UploadUrl:function(){
      // 因为action参数是必填项，我们使用二次确认进行文件上传时，直接填上传文件的url会因为没有参数导致api报404，所以这里将action设置为一个返回为空的方法就行，避免抛错
      return ""
    },
    uploadFile() {
      console.log(this.fileList)
      if (this.fileList.length === 0){
        this.$message.warning('请上传文件');
      } else {
        let file = new FormData();
        file.append('file',this.fileList[0]);
        this.$http.post("/clue/upload",file)
            .then(result=>{
              result = result.data
              if(result.success){
                this.$message.success(result.message)
              }
            })
      }
      this.clueFormVisible=false
    },
    importClue(){

        this.clueFormVisible=true;

    },
    exportClue(){
      if(this.query.keyword!=null && this.query.keyword!=''){
        window.location.href="http://localhost:8080/clue/export/"+this.query.keyword;
      }else {
        window.location.href="http://localhost:8080/clue/export/null"
      }

    },
    saveBusiness(){
      this.$http.post("/clue/business",this.business)
      .then(result=>{
        result = result.data;
        if(result.success){
          this.$message({
            message:result.message,
            type:'success',
          });
          //关闭弹框
          this.businessVisible = false;
          //刷新页面
          this.getClue();
        }else{
          this.$message({
            message:result.message,
            type:'error'
          });
        }
      })
    },
    // 就是当 当前页发生改变时就触发该方法
    handleCurrentChange(val) {
      // val它就是当前要访问的那个页码,当它发生改变时,需要修改query对象中的currentPage,只有当它发生改变,调到后端时,后端的currentPage才会拿到新的页码
      this.query.currentPage = val;
      // 重新调用getProduct(),重新从后端获取数据渲染页面
      this.getClue();
    },
    //获取线索分页
    getClue() {
      console.log(this.query)
      this.$http.post("/clue",this.query)
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


    handleBatchAssign(){
      //弹框显示
        this.batchAssignVisible = true;
        this.getAllEmployee();
    },
    handleSingleAssign(index,row){
      //弹框显示
      //获取被选中的线索的id
      this.singleAssign.clueId = row.id;
      this.singleAssignVisible = true;
      this.getAllEmployee();
    },
    saveBatchAssign(){
      // 获取所有被选中的线索的id
      let ids = this.sels.map(x => x.id);
      this.batchAssign.clueIds = ids;
      // 将线索id和营销人员一块传到后端
      this.$http.put("/clue/batch/assign",this.batchAssign)
          .then(result => {
            result = result.data;
            if(result.success){
              this.$message({
                message: result.message,
                type: 'success'
              });
              // 关闭弹框
              this.batchAssignVisible = false;
              // 重新刷新页面
              this.getClue();
            }else{
              this.$message({
                message: result.message,
                type: 'error'
              });
            }
          })
    },
    saveSingleAssign(){
      console.log(this.singleAssign)
      //把线索id和营销人员一起传到后端
      this.$http.put("/clue/single/assign",this.singleAssign)
      .then(result=>{
        result = result.data;
        if(result.success){
          this.$message({
            message:result.message,
            type:'success'
          });
          //关闭弹框
          this.singleAssignVisible = false;
          //重新刷新页面
          this.getClue();
        }else{
          this.$message({
            message:result.message,
            type:'error'
          })
        }
      })

    },
    handleSizeChange(val){
      this.query.pageSize = val;
      this.search();
    },
    handleClueActivity(index,row){
      // 弹出关联活动的弹框
      this.clueActivityVisible = true;
      this.clueActivity.clueName = row.fullName;
      this.clueActivity.clueId = row.id;
      // 查询出所有的市场活动渲染下拉框
      this.$http.get("/activity/type/1")
          .then(result => {
            result = result.data;
            if(result.success){
              this.activities = result.resultObject;
            }
          })
    },

    handleBusiness(index,row){
      // 弹出弹框
      this.businessVisible = true;
      // 查询出所有的产品信息,给products赋值
      this.getAllProducts();
      // 给business对象赋值,做回显
      this.business.clueId = row.id;
      this.business.fullName = row.fullName;
      this.business.phone = row.phone;
    },
    getAllProducts(){
      this.$http.get("/product")
          .then(result => {
            result = result.data;
            console.log(result)
            // 将获取到的所有的员工信息
            this.products = result.resultObject;
          })
    },
    //删除  index:要删除的数据所在下标  row:要删除的数据,当前行数据对象
    handleDel: function (index, row) {
      this.$confirm('确认删除该记录吗?', '提示', {
        type: 'warning'
      }).then(() => {
        this.listLoading = true;  // 弹出加载框
        // 如果确认删除,要调用后端删除接口
        this.$http.delete("/clue/"+row.id)
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
                this.getClue();
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
      this.getClue();
      this.getAllProductType();

    },
    //显示新增页面
    handleAdd:function() {
      this.saveFormVisible = true;//显示弹框
      this.saveForm = {
        fullName: '',
        appellation:"",
        company:"",
        email:"",
        phone:"",
        description:""
      };
      this.getClue();


    },
    cancelActivity(){
      this.clueActivityVisible = false;
      this.clueActivity.activityIds = null;
    },

    saveClueActivity(){
      this.$http.put("/clue/activity",this.clueActivity)
          .then(result => {
            result = result.data;
            if(result.success){
              // 保存线索活动成功以后，关闭弹框
              this.clueActivityVisible = false;
              // 展示操作成功提示
              this.$message({
                message: "操作成功",
                type: 'success'
              });
            }else{
              this.$message({
                message: result.message,
                type: 'error'
              });
            }
          })
    },
    cancel(){
      this.singleAssignVisible = false;
      this.singleAssign.owner = "";
    },
    //保存
    saveSubmit: function () {
      this.$refs.saveForm.validate((valid) => {
        if (valid) {
          this.$confirm('确认提交吗？', '提示', {}).then(() => {
            this.saveLoading = true;
            this.$http.put("/clue",this.saveForm)
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
                    this.getClue();
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
        this.$http.patch("/clue",ids)
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
                this.getClue();
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
    getAllEmployee() {
      this.$http.get("/employee")
          .then(result => {
            result = result.data;
            console.log(result)
            // 将获取到的所有的员工信息
            this.employees = result.resultObject;
          })
    }
  },
  mounted() {
    // 页面加载完毕,调用部门的分页接口,获取数据渲染页面
    this.getClue();
  }
}

</script>

<style scoped>

</style>