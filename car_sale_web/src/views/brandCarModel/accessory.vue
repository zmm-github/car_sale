<template>
    <div id="accessory-container" class="common">
        <!-- 顶部搜索框 -->
        <el-card class="search">
            <el-row>
                <el-col :span="18">
                    <el-input placeholder="配件名称" v-model="searchModel.accessoryName" clearable></el-input>
                    <el-input placeholder="所属车系" v-model="searchModel.carSeries" clearable></el-input>
                    <el-button type="primary" @click="getAccessoryList" size="medium" round
                        icon="el-icon-search">查询</el-button>
                </el-col>
                <el-col :span="6" align="right">
                    <el-button-group>
                        <el-button type="success" icon="el-icon-plus" @click="exportExcelSelect">导出</el-button>
                        <el-button type="primary" @click="openDialogUI(null)" icon="el-icon-plus">新增</el-button>
                        <el-button type="danger" icon="el-icon-delete" @click="deleteAccessoryByIds">批量删除</el-button>
                    </el-button-group>
                </el-col>
            </el-row>
        </el-card>

        <!-- 结果列表 -->
        <el-card>
            <el-table size="small" border stripe ref="multipleTable" :data="accessoryList" :cell-style="rowStyle"
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
                <el-table-column prop="id" label="ID" width="100">
                </el-table-column>
                <el-table-column prop="accessoryName" label="配件名称" width="160">
                </el-table-column>
                <el-table-column prop="carSeries" label="所属车系">
                </el-table-column>
                <el-table-column prop="money" label="配件价格" width="160">
                </el-table-column>
                <el-table-column prop="createTime" sortable label="创建时间" width="180">
                </el-table-column>
                <el-table-column prop="createPeople" label="创建人" width="150">
                </el-table-column>
                <el-table-column label="操作" width="160">
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
        <el-dialog @close="clearData" :title="title" :visible.sync="dialogFormVisible"
            :close-on-click-modal="closeOnClickModel" :destroy-on-close="destroyOnClose" :before-close="handleClose">
            <el-form ref="accessoryFormRef" :rules="rules" :model="accessoryForm">
                <el-form-item label="配件名称" prop="accessoryName" :label-width="formLabelWidth">
                    <el-input v-model="accessoryForm.accessoryName" placeholder="请输入配件名称" clearable
                        autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="所属车系" prop="carSeries" :label-width="formLabelWidth">
                    <el-input v-model="accessoryForm.carSeries" placeholder="请输入所属车系" clearable
                        autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="配件价格" prop="money" :label-width="formLabelWidth">
                    <el-input v-model="accessoryForm.money" placeholder="请输入配件价格" clearable autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="创建人" prop="createPeople" :label-width="formLabelWidth">
                    <el-input v-model="accessoryForm.createPeople" placeholder="请输入创建人" clearable
                        autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item align="right">
                    <el-button @click="handleCancel">取消</el-button>
                    <el-button type="primary" @click="handleAddOrUpdate('accessoryFormRef')">{{ !accessoryForm.id ? '新增' :
                        '修改'
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
                <el-table-column prop="id" label="ID" width="100">
                </el-table-column>
                <el-table-column prop="accessoryName" label="配件名称" width="160">
                </el-table-column>
                <el-table-column prop="carSeries" label="所属车系">
                </el-table-column>
                <el-table-column prop="money" label="配件价格" width="160">
                </el-table-column>
                <el-table-column prop="createTime" sortable label="创建时间" width="180">
                </el-table-column>
                <el-table-column prop="createPeople" label="创建人" width="150">
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
import htmlToExcel from '@/utils/htmlToExcel';
import accessoryApi from "@/api/accessoryManage"
export default {
    name: 'Accessory',
    data() {
        return {
            title: '',
            total: 0,
            excelDialogVisible: false,
            selectData: [],
            searchModel: {
                accessoryName: "",
                carSeries: "",
                currentPage: 1,
                pageSize: 5
            },
            accessoryList: [],
            accessoryForm: {},
            dialogFormVisible: false,
            closeOnClickModel: false,
            destroyOnClose: true,
            formLabelWidth: '150px',
            ids: [],
            rules: {
                accessoryName: [
                    { required: true, message: "请输入配件名称", trigger: "blur" }
                ],
                carSeries: [
                    { required: true, message: "请输入所属车系", trigger: "blur" }
                ],
                money: [
                    { required: true, message: "请输入配件价格", trigger: "blur" }
                ],
                createPeople: [
                    { required: true, message: "请输入创建人", trigger: "blur" }
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
            htmlToExcel.getExcel('#selectTable', '配件表');
            this.excelDialogVisible = false;
        },

        // 打开对话框
        openDialogUI(id) {
            var that = this;
            if (id == null) { // 新增操作
                that.title = "新增配件";
            } else { // 修改操作
                that.title = "修改配件";
                accessoryApi.getAccessoryById(id).then(res => {
                    that.accessoryForm = res.data;
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
            this.accessoryForm = {};
        },
        // 获取配件列表
        getAccessoryList() {
            var that = this;
            accessoryApi.getAccessoryList(that.searchModel).then(res => {
                that.accessoryList = res.data.rows;
                that.total = res.data.total;
            })
        },
        // 选择的记录改变
        handleSelectionChange(val) {
            this.selectData = val;
            val.forEach((ele, index) => {
                this.ids[index] = ele.id;
            });
        },
        // 删除选中的多个配件
        deleteAccessoryByIds() {
            var that = this;
            that.$confirm("你确定要删除编号 “" + that.ids + "” 的配件信息吗").then(() => {
                accessoryApi.deleteAccessoryByIds(that.ids).then(res => {
                    // 重新获取所有配件列表进行展示
                    that.getAccessoryList();
                    // 提示删除成功信息
                    that.$message.success(res.message);
                })
            }).catch(() => {
                // 提示删除失败信息
                that.$message.error("删除失败！！！");
            })
        },
        // 处理每页大小改变
        handleSizeChange(val) {
            this.searchModel.pageSize = val;
            this.getAccessoryList();
        },

        // 处理当前页改变
        handleCurrentChange(val) {
            this.searchModel.currentPage = val;
            this.getAccessoryList()
        },
        // 关闭对话框
        handleClose() {
            this.$confirm('确认关闭？').then(res => {
                this.dialogFormVisible = false; //关闭对话框
                this.excelDialogVisible = false; //关闭表格对话框
            }).catch(() => { })
        },

        // 取消对话框
        handleCancel() {
            this.dialogFormVisible = false; //关闭对话框
            this.excelDialogVisible = false; //关闭表格对话框
        },

        // 删除配件
        handleDelete(id) {
            var that = this;
            that.$confirm("你确定要删除编号 “" + id + "” 的配件信息吗?").then(() => {
                // 调用删除指定配件的api进行删除
                accessoryApi.deleteAccessoryById(id).then(res => {
                    // 调用查询配件列表，重新加载。
                    that.getAccessoryList();
                    // 弹出成功消息提示
                    that.$message.success(res.message);
                });
            }).catch(() => {
                that.$message.error("删除失败！！！");
            })
        },

        // 处理新增或修改操作
        handleAddOrUpdate(accessoryFormRef) {
            var that = this;
            this.$refs[accessoryFormRef].validate((valid) => {
                if (valid) {
                    accessoryApi.saveAccessory(that.accessoryForm).then(res => {
                        // 清空对话框中的数据
                        that.accessoryForm = {};
                        // 关闭对话框
                        that.dialogFormVisible = false;
                        // 重新获取列表信息
                        that.getAccessoryList();
                        // 显示成功提示信息
                        that.$message.success(res.message);
                    })
                } else {
                    that.$message.error("请按要求填写相关信息后再进行提交！");
                    return false;
                }
            })
        }
    },
    mounted() {
        this.getAccessoryList();
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