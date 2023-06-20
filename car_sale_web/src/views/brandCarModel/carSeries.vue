<template>
  <div id="carSeries-container" class="common">
    <!-- 搜索栏 -->
    <el-card class="search">
      <el-row>
        <el-col :span="18">
          <el-input placeholder="车系" v-model="searchModel.carSeries" clearable></el-input>
          <el-input placeholder="创建人" v-model="searchModel.createPeople" clearable></el-input>
          <el-button type="primary" @click="getCarSeriesList" size="medium" round icon="el-icon-search">查询</el-button>
        </el-col>
        <el-col :span="6" align="right">
          <el-button-group>
            <el-button type="success" icon="el-icon-plus" @click="exportExcelSelect">导出</el-button>
            <el-button type="primary" @click="openDialogUI(null)" icon="el-icon-plus">新增</el-button>
            <el-button type="danger" icon="el-icon-delete" @click="deleteCarSeriesByIds">批量删除</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 结果列表 -->
    <el-card>
      <el-table size="small" border stripe ref="multipleTable" :data="carSeriesList" :cell-style="rowStyle"
        :header-cell-style="headerCellStyle" :default-sort="{ prop: 'createTime', order: 'descending' }"
        tooltip-effect="dark" style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55">
        </el-table-column>
        <el-table-column label="#" width="100">
          <template slot-scope="scope">
            {{
              (searchModel.currentPage - 1) * searchModel.pageSize + scope.$index + 1
            }}
          </template>
        </el-table-column>
        <el-table-column prop="id" label="编号" width="120">
        </el-table-column>
        <el-table-column prop="carSeries" label="车系名称" width="200">
        </el-table-column>
        <el-table-column prop="money" label="报价（万）" width="180">
        </el-table-column>
        <el-table-column prop="createTime" sortable label="创建时间">
        </el-table-column>
        <el-table-column prop="createPeople" label="创建人" width="180">
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="openDialogUI(scope.row.id)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 分页 -->
    <el-pagination v-if="total > 0" align="right" @size-change="handleSizeChange" @current-change="handleCurrentChange"
      :current-page="searchModel.currentPage" :page-sizes="[5, 10, 15, 20]" :page-size="searchModel.pageSize"
      layout="total, sizes, prev, pager, next, jumper" :total="total">
    </el-pagination>

    <!-- 对话框 -->
    <el-dialog @close="clearData" :title="title" top="20vh" :visible.sync="dialogFormVisible"
      :close-on-click-modal="closeOnClickModel" :destroy-on-close="destroyOnClose" :before-close="handleClose">
      <el-form ref="carSeriesFormRef" :rules="rules" :model="carSeriesForm">
        <el-form-item label="车系名称" prop="carSeries" :label-width="formLabelWidth">
          <el-input v-model="carSeriesForm.carSeries" placeholder="车系名称" clearable autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="报价（万）" prop="money" :label-width="formLabelWidth">
          <el-input v-model="carSeriesForm.money" placeholder="金额范围：例10-20" clearable autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item v-if="carSeriesForm.createTime != undefined" label="创建时间" prop="createTime"
          :label-width="formLabelWidth">
          <span>{{ carSeriesForm.createTime }}</span>
        </el-form-item>
        <el-form-item label="创建人" prop="createPeople" :label-width="formLabelWidth">
          <el-input v-model="carSeriesForm.createPeople" placeholder="创建人名称" clearable autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item align="right">
          <el-button @click="handleCancel">取消</el-button>
          <el-button type="primary" @click="handleAddAndUpdate('carSeriesFormRef')">{{ !carSeriesForm.id ? '新增' : '修改'
          }}</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <!-- 导出表格预览对话框 -->
    <el-dialog title="表格导出预览" top="8vh" width="70%" :visible.sync="excelDialogVisible"
      :close-on-click-modal="closeOnClickModel" :before-close="handleClose">
      <el-table size="small" stripe ref="multipleTable" align="center" :cell-style="rowStyle"
        :header-cell-style="headerCellStyle" :data="selectData" id="selectTable" border tooltip-effect="dark"
        @selection-change="handleSelectionChange" style="width: 100%">
        <el-table-column prop="id" label="编号" width="120">
        </el-table-column>
        <el-table-column prop="carSeries" label="车系名称" width="200">
        </el-table-column>
        <el-table-column prop="money" label="报价（万）" width="180">
        </el-table-column>
        <el-table-column prop="createTime" sortable label="创建时间">
        </el-table-column>
        <el-table-column prop="createPeople" label="创建人" width="180">
        </el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleCancel">取消</el-button>
        <el-button type="primary" @click="exportExcel">确定导出</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import carSeriesApi from '@/api/carSeriesManage'
import htmlToExcel from '@/utils/htmlToExcel';
export default {
  name: 'CarSeries',
  data() {
    return {
      title: '',
      total: 0,
      excelDialogVisible: false,
      selectData: [],
      searchModel: {
        carSeries: '',
        createPeople: '',
        currentPage: 1,
        pageSize: 5,
      },
      carSeriesList: [],
      carSeriesForm: {},
      formLabelWidth: '120px',
      dialogFormVisible: false,
      closeOnClickModel: false,
      destroyOnClose: true,
      ids: [],
      rules: {
        carSeries: [
          { required: true, message: '请输入车系名称', trigger: 'blur' }
          // { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
        ],
        money: [
          { required: true, message: '请输入金额', trigger: 'blur' }
        ],
        createTime: [
          { required: true, message: '请输入创建时间', trigger: 'blur' }
        ],
        createPeople: [
          { required: true, message: '请输入创建人', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    // 显示要导出的数据
    exportExcelSelect() {
      if (this.selectData.length < 1) {
        this.$message.error('请选择要导出的内容！');
        return false;
      }

      // 显示表格对话框
      this.excelDialogVisible = true;
    },

    // 导出
    exportExcel() {
      htmlToExcel.getExcel('#selectTable', '车系表');
      this.excelDialogVisible = false;
    },

    // 打开对话框
    openDialogUI(id) {
      var that = this;
      if (id == null) { // 新增操作
        that.title = "新增车系";
      } else { // 修改操作
        that.title = "修改车系";
        carSeriesApi.getCarSeriesById(id).then(res => {
          that.carSeriesForm = res.data;
        })
      }
      // 显示对话框
      this.dialogFormVisible = true;
    },

    // 设置表格样式
    rowStyle() {
      return "text-align:center";
    },
    headerCellStyle() {
      return "text-align: center; background: #e9e2f1;";
    },

    clearData() {
      // 清理数据
      this.carSeriesForm = {};
    },

    handleSelectionChange(val) {
      this.selectData = val;
      val.forEach((ele, index) => {
        this.ids[index] = ele.id;
      });
    },
    // 删除选中的多个车系
    deleteCarSeriesByIds() {
      var that = this;
      that.$confirm("你确定要删除编号 “" + that.ids + "” 的车系信息吗").then(() => {
        carSeriesApi.deleteCarSeriesByIds(that.ids).then(res => {
          // 重新获取所有车系列表进行展示
          that.getCarSeriesList();
          // 提示删除成功信息
          that.$message.success(res.message);
        })
      }).catch(() => {
        that.$message.error("删除失败！！！");
      })
    },
    handleSizeChange(val) {
      this.searchModel.pageSize = val;
      this.getCarSeriesList();
    },
    handleCurrentChange(val) {
      this.searchModel.currentPage = val;
      this.getCarSeriesList()
    },
    // 获取车系列表
    getCarSeriesList() {
      var that = this;
      carSeriesApi.getCarSeriesList(that.searchModel).then(res => {
        that.total = res.data.total;
        that.carSeriesList = res.data.rows;
      });
    },

    // 删除车系
    handleDelete(id) {
      var that = this;
      that.$confirm("你确定要删除编号 “" + id + "” 的车系信息吗?").then(() => {
        carSeriesApi.deleteCarSeriesById(id).then(res => {
          // 关闭对话框
          that.dialogFormVisible = false;
          // 重新加载所有车系信息
          that.getCarSeriesList();
          // 显示删除成功信息
          that.$message.success(res.message);
        })
      }).catch(() => {
        // 显示删除失败信息
        that.$message.error("删除失败！！！");
      })
    },

    // 关闭对话框
    handleClose() {
      this.$confirm('确认关闭？').then(res => {
        this.dialogFormVisible = false;//关闭对话框
        this.excelDialogVisible = false; //关闭表格对话框
      }).catch(() => { })
    },

    // 取消新增或修改操作
    handleCancel() {
      this.dialogFormVisible = false;//关闭对话框
      this.excelDialogVisible = false; //关闭表格对话框
    },

    // 新增和修改车系
    handleAddAndUpdate(carSeriesFormRef) {
      var that = this;
      that.$refs[carSeriesFormRef].validate((valid) => {
        if (valid) {
          carSeriesApi.saveCarSeries(that.carSeriesForm).then(res => {
            // 清空对话框中的数据
            that.carSeriesForm = {};
            // 关闭对话框
            that.dialogFormVisible = false;
            // 重新获取列表信息
            that.getCarSeriesList();
            // 显示成功提示信息
            that.$message.success(res.message);
          })
        } else {
          that.$message.error("请按要求填写相关信息后再进行提交！");
          return false;
        }
      });
    }
  },
  mounted() {
    this.getCarSeriesList();
  }
}
</script>

<style lang="scss">
.search {
  .el-input {
    width: 220px;
    margin-right: 10px;
  }
}
</style>