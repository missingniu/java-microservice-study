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
        <el-form-item>
          <el-button type="primary" @click="handleImport">导入</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleExport">导出</el-button>
        </el-form-item>
      </el-form>
    </el-col>

    <!--列表-->
    <el-table :data="pageInfo.rows" highlight-current-row v-loading="listLoading"  style="width: 100%;">
      <el-table-column type="selection" width="55">
      </el-table-column>
      <el-table-column type="index" width="60">
      </el-table-column>
      <el-table-column prop="fullName" label="名称" width="200" sortable>
      </el-table-column>
      <el-table-column prop="appellation" label="称呼" width="200" sortable>
      </el-table-column>
      <el-table-column prop="job" label="职业" width="200" sortable>
      </el-table-column>
      <el-table-column prop="email" label="邮箱" width="200" sortable>
      </el-table-column>
      <el-table-column prop="phone" label="电话" width="200" sortable>
      </el-table-column>
      <el-table-column prop="state" label="状态" width="200" sortable>
        <tempalte slot-scope="scope">
          <span v-if="scope.row.state == 1" style="color: red">未分配</span>
          <span v-if="scope.row.state == 2" style="color: blue">跟进中</span>
          <span v-if="scope.row.state == 3" style="color: #13ce66">已转化为商机</span>
          <span v-if="scope.row.state == 0" style="color: #8613ce">已废弃</span>
        </tempalte>
      </el-table-column>
      <el-table-column prop="description" label="描述" min-width="220" sortable>
      </el-table-column>
      <el-table-column label="操作" width="500">
        <template slot-scope="scope">
          <el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
          <el-button v-if="scope.row.state == 2" size="small" @click="handleClueEmployee(scope.$index, scope.row)">变更</el-button>
          <el-button size="small" @click="handleJoinActivity(scope.$index, scope.row)">关联活动</el-button>
          <el-button size="small" @click="handleClue(scope.$index, scope.row)">跟进</el-button>
          <el-button v-if="scope.row.state != 3" size="small" @click="handleClueBusiness(scope.$index, scope.row)">转化为商机</el-button>
          <el-button v-if="scope.row.state == 1" size="small" @click="handleClueEmployee(scope.$index, scope.row)">分配</el-button>
          <el-button size="small" @click="handleAbandon(scope.$index, scope.row)">废弃</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!--工具条-->
    <el-col :span="24" class="toolbar">
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
        <el-form-item label="全称" prop="fullName">
          <el-input v-model="saveForm.fullName" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="称呼" prop="appellation">
          <el-input v-model="saveForm.appellation" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="saveForm.phone" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input type="textarea" v-model="saveForm.description" auto-complete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="saveFormVisible = false">取消</el-button>
        <el-button type="primary" @click.native="save" :loading="addLoading">提交</el-button>
      </div>
    </el-dialog>

    <el-dialog title="参与活动" :visible.sync="clueActivityVisible" :close-on-click-modal="false">
      <!-- 线索id，活动多个id-->
      <el-form :model="clueActivitys" label-width="80px">
        <!--<el-checkbox-group v-model="clueActivitys.activitys" >
          <el-checkbox v-for=" a in activityList" :label="a.name" :value="a" :key="a.id"></el-checkbox>
        </el-checkbox-group>-->

        <el-select v-model="clueActivitys.activityIds" multiple clearable placeholder="请选择">
          <el-option
              v-for="item in activityList"
              :key="item.id"
              :label="item.name"
              :value="item.id">
          </el-option>
        </el-select>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="clueActivityVisible = false">取消</el-button>
        <el-button type="primary" @click.native="clueActivitysSubmit">提交</el-button>
      </div>
    </el-dialog>

    <el-dialog title="变更营销人员" :visible.sync="clueEmployeeVisible" :close-on-click-modal="false">
      <!-- 线索id，活动多个id-->
      <el-form :model="saveForm" label-width="80px">
        <!--<el-checkbox-group v-model="clueActivitys.activitys" >
          <el-checkbox v-for=" a in activityList" :label="a.name" :value="a" :key="a.id"></el-checkbox>
        </el-checkbox-group>-->

        <el-select v-model="saveForm.owner" clearable placeholder="请选择">
          <el-option
              v-for="item in employeeList"
              :key="item.id"
              :label="item.username"
              :value="item.id">
          </el-option>
        </el-select>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="clueEmployeeVisible = false">取消</el-button>
        <el-button type="primary" @click.native="clueEmployeeSubmit">提交</el-button>
      </div>
    </el-dialog>

    <el-dialog title="跟进" :visible.sync="clueFormVisible" :close-on-click-modal="false">
      <el-form :model="clueForm" label-width="80px">
        <!--prop="name" 引用saveFormRules规则中的 name规则-->

        <el-form-item label="下一次跟进时间" prop="nextContactTime" label-width="120px">
          <el-date-picker
              v-model="clueForm.nextContactTime"
              type="date"
              placeholder="选择日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="概述" prop="contactSummary" label-width="120px">
          <el-input type="textarea" v-model="clueForm.contactSummary" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item  prop="id">
          <el-input type="hidden" v-model="clueForm.id" auto-complete="off"></el-input>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="clueFormVisible = false">取消</el-button>
        <el-button type="primary" @click.native="clueFollowSubmit" :loading="addLoading">提交</el-button>
      </div>
    </el-dialog>

    <el-dialog title="转化商机" :visible.sync="clueBusinessFormVisible" :close-on-click-modal="false">
      <el-form :model="clueBusinessForm" label-width="80px">
        <!--prop="name" 引用saveFormRules规则中的 name规则-->
        <el-form-item label="名称" prop="fullName" >
          <el-input  v-model="clueBusinessForm.fullName" :disabled="true" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="产品" >
          <el-select v-model="clueBusinessForm.product" value-key="id" clearable placeholder="请选择">
            <el-option
                v-for="item in productList"
                :key="item.id"
                :label="item.name"
                :value="item">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="clueBusinessFormVisible = false">取消</el-button>
        <el-button type="primary" @click.native="clueBusinessSubmit" :loading="addLoading">提交</el-button>
      </div>
    </el-dialog>

    <el-dialog title="导入" :visible.sync="importVisible" :close-on-click-modal="false">
      <el-upload
          class="upload-demo"
          action="https://jsonplaceholder.typicode.com/posts/"
          :on-preview="handlePreview"
          :on-remove="handleRemove"
          :on-success="handleSuccess"
          :before-remove="beforeRemove"
          multiple
          :limit="3"
          :on-exceed="handleExceed"
          :file-list="fileList">
        <el-button size="small" type="primary">点击上传</el-button>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="importVisible = false">取消</el-button>
        <el-button type="primary" @click.native="importSubmit" :loading="addLoading">提交</el-button>
      </div>
    </el-dialog>

  </section>
</template>

<script>

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
      clueForm:{
        id:null,
        contactSummary:'',
        nextContactTime:''
      },

      clueBusinessForm:{
        id:null,
        fullName:"",
        product:{
          id:null,
          name:''
        }
      },

      saveForm:{
        id:null,
        fullName:'',
        appellation:'',
        phone:'',
        description:'',
        owner:null,
      },

      clueActivitys:{
        clueId:null,
        activityIds:[],
      },

      fileList:[],
      productList:[],
      activityList:[],
      employeeList:[],
      importVisible:false,
      clueBusinessFormVisible:false,
      clueFormVisible:false,
      clueActivityVisible:false,
      clueEmployeeVisible:false,
      addLoading:false,    // 保存按钮,加载框
      saveFormVisible:false,   // 用来弹窗是否显示的
      saveLoading:false   // 设置权限那个加载框
    }
  },
  methods: {


    handleSuccess(file,fileList){
      console.log(fileList)
      this.fileList = fileList;
    },

    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file,fileList) {
      console.log("文件是",file)
    },
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
    },
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${ file.name }？`);
    },

    handleExport(){
      console.log(this.query)
      // this.$http.get("/homework/exportExcel",this.query).then(result => {
      //   console.log("导出返回值",result)
      //
      // })

      window.location.href = "http://localhost:8080/homework/exportExcel/"+this.query.keyword

    },

    importSubmit(){
      console.log("提交时",this.fileList)
      let formData = new FormData();
      formData.append("file",this.fileList.raw);
      // headers:{"Content-Type": "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW"
      this.$http.post("/homework/importExcel",formData,{headers:{"Content-Type":false}})
          .then(result => {
            result = result.data;
            if (result.resultObj.error){
              this.$message({
                type:'error',
                message:result.resultObj.error.fullName + result.resultObj.error.errorMsg
              })
            }else {
              this.$message({
                type:'success',
                message:"导入成功"
              })
            }
            this.getClue();
      })
    },
    handleImport(index,row){
      this.importVisible = true;
    },

    handleAbandon(index,row){
      this.$confirm('确认废弃该线索么?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.put("/clue/abandon",row).then(result => {
          result = result.data;
          if (result.success){
            this.$message({
              type: 'success',
              message: '废弃成功!'
            });
          } else {
            this.$message({
              type: 'error',
              message: '废弃失败!'
            });
          }
          this.getClue();
        }).catch(() => {
          this.$message({
            type: 'error',
            message: '网络错误!'
          });
        })

      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消废弃'
        });
      });
    },

    clueBusinessSubmit(){
      console.log(this.clueBusinessForm)
      this.$http.put("/clue/business",this.clueBusinessForm).then(result => {
        result = result.data;
        if (result.success){
          this.$message({
            type:'success',
            message:'转化为商机成功'
          })
        } else {
          this.$message({
            type:'error',
            message:'转化为商机失败'
          })
        }
        this.clueBusinessFormVisible = false;
        this.getClue();
      }).catch(result => {
        this.$message({
          type:'error',
          message:'网络错误!'
        })
      })
    },

    getProductList(){
      this.$http.get("/product").then(result => {
        result = result.data;
        if (result.success){
          this.productList = result.resultObj;
        }
      })
    },

    handleClueBusiness(index,row){
      this.getProductList();
      this.clueBusinessFormVisible = true;
      this.clueBusinessForm = Object.assign({},row);
    },

    clueFollowSubmit(){
      this.$http.put("/clue/follow",this.clueForm).then(result => {
        result = result.data;
        if (result.success){
          this.$message({
            type:'success',
            message:'跟进成功'
          })
        } else {
          this.$message({
            type:'error',
            message:'跟进失败'
          })
        }
        this.clueFormVisible = false;
      }).catch(result => {
        this.$message({
          type:'error',
          message:'网络错误!'
        })
      })
    },

    handleClue(index,row){

      this.clueFormVisible = true;
      this.clueForm = Object.assign({},row);

    },

    clueEmployeeSubmit(){
      console.log(this.saveForm)
      this.$http.put("/clue",this.saveForm).then(result => {
        result = result.data;
        if (result.success){
          this.$message({
            type:'success',
            message:'变更成功'
          })
        }else {
          this.$message({
            type:'error',
            message:'变更失败'
          })
        }
        this.clueEmployeeVisible = false;
        this.getClue();
      }).catch(result => {
        this.$message({
          type:'error',
          message:'变更失败'
        })
      })
    },

    getEmployees() {
      this.$http.get("/employee").then(result => {
        result = result.data;
        if (result.success){
          this.employeeList = result.resultObj;
        }
      })
    },

    handleClueEmployee(index,row){
      this.saveForm = Object.assign({},row);
      this.clueEmployeeVisible = true;
      this.getEmployees();
    },

    clueActivitysSubmit(){
      console.log(this.clueActivitys)
      this.$http.put("/clue/activitys",this.clueActivitys).then(result => {
        result = result.data;
        if (result.success){
          this.$message({
            type:'success',
            message:'关联成功'
          })
        } else {
          this.$message({
            type:'error',
            message:'关联失败'
          })
        }
        this.clueActivityVisible = false;
      }).catch(result => {
        this.$message({
          type:'error',
          message:'网络错误！'
        })
      })
    },

    handleJoinActivity(index,row){
      this.clueActivitys.clueId = row.id;
      this.clueActivityVisible = true;
      this.getActivitys();
    },

    getActivitys() {
      this.$http.get("/activity").then(result => {
        result = result.data;
        if (result.success){
          this.activityList = result.resultObj;
        }
      })
    },

    save: function () {
      // 校验表单中的输入是否满足规则
      this.$refs.saveForm.validate((valid) => {
        // valid==true,就说明表单是满足校验规则的
        if (valid) {
          this.$confirm('确认提交吗？', '提示', {}).then(() => {
            this.addLoading = true;
            this.$http.put("/clue",this.saveForm)
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
                    this.getClue();

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
        fullName:'',
        appellation:'',
        phone:'',
        description:''
      }
    },
    handleEdit(index,row){
      // 弹出 修改弹框
      this.saveFormVisible = true;
      // 回显  利用双向绑定原则做回显
      this.saveForm = Object.assign({},row);
    },


    search(){
      // 将当前页的值改为1
      this.query.currentPage = 1;
      this.getClue();
    },
    handleCurrentChange(val) {
      // val 当前要跳转的页码
      this.query.currentPage = val;
      this.getClue();
    },
    //获取权限列表
    getClue() {
      this.$http.post("/clue",this.query)
          .then(result => {
            result = result.data;
            console.log(result);
            if(result.success){ // 判断后端操作是否成功
              this.pageInfo = result.resultObj;
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
    this.getClue();
  }
}

</script>

<style scoped>

</style>
