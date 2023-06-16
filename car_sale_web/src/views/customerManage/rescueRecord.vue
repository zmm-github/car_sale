<template>
    <div id="rescueRecord-container" class="common">
        <!-- 顶部搜索框 -->
        <el-card class="search">
            <el-row>
                <el-col :span="18">
                    <el-input placeholder="姓名" v-model="searchModel.name" clearable></el-input>
                    <el-input placeholder="救援内容" v-model="searchModel.type" clearable></el-input>
                    <el-button type="primary" @click="getRescueRecordList" size="medium" round
                        icon="el-icon-search">查询</el-button>
                </el-col>
                <el-col :span="6" align="right">
                    <el-button-group>
                        <el-button type="success" icon="el-icon-plus" @click="exportExcelSelect">导出</el-button>
                        <el-button type="primary" @click="openDialogUI(null)" icon="el-icon-plus">新增</el-button>
                        <el-button type="danger" icon="el-icon-delete" @click="deleteRescueRecordByIds">批量删除</el-button>
                    </el-button-group>
                </el-col>
            </el-row>
        </el-card>

        <!-- 结果列表 -->
        <el-card>
            <el-table size="small" ref="multipleTable" :default-sort="{ prop: 'createTime', order: 'descending' }"
                :data="rescueRecordList" border :cell-style="rowStyle" :header-cell-style="headerCellStyle" stripe
                tooltip-effect="dark" style="width: 100%" @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55">
                </el-table-column>
                <el-table-column label="#" width="120">
                    <template slot-scope="scope">
                        {{
                            (searchModel.currentPage - 1) * searchModel.pageSize + scope.$index + 1
                        }}
                    </template>
                </el-table-column>
                <el-table-column prop="id" label="ID" width="120">
                </el-table-column>
                <el-table-column prop="name" label="姓名" width="220">
                </el-table-column>
                <el-table-column prop="content" label="救援内容">
                </el-table-column>
                <el-table-column prop="createTime" sortable label="创建时间" width="200">
                </el-table-column>
                <el-table-column fixed="right" label="操作" width="180">
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
        <el-dialog @close="clearData" :title="title" top="25vh" :visible.sync="dialogFormVisible"
            :close-on-click-modal="closeOnClickModel" :destroy-on-close="destroyOnClose" :before-close="handleClose">
            <el-form ref="rescueRecordFormRef" :rules="rules" :model="rescueRecordForm">
                <el-form-item label="姓名" prop="name" :label-width="formLabelWidth">
                    <el-input v-model="rescueRecordForm.name" placeholder="请输入姓名" clearable autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="救援内容" prop="content" :label-width="formLabelWidth">
                    <el-input v-model="rescueRecordForm.content" placeholder="请输入救援内容" clearable
                        autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item align="right">
                    <el-button @click="handleCancel">取消</el-button>
                    <el-button type="primary" @click="handleAddOrUpdate('rescueRecordFormRef')">{{ !rescueRecordForm.id ?
                        '新增'
                        :
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
                <el-table-column prop="id" label="ID" width="120">
                </el-table-column>
                <el-table-column prop="name" label="姓名" width="220">
                </el-table-column>
                <el-table-column prop="content" label="救援内容">
                </el-table-column>
                <el-table-column prop="createTime" sortable label="创建时间" width="200">
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
import rescueRecordApi from "@/api/rescueRecordManage"
import htmlToExcel from '@/utils/htmlToExcel';
export default {
    name: 'RescueRecord',
    data() {
        return {
            title: '',
            total: 0,
            excelDialogVisible: false,
            selectData: [],
            searchModel: {
                name: "",
                content: "",
                currentPage: 1,
                pageSize: 5
            },
            rescueRecordList: [],
            rescueRecordForm: {},
            dialogFormVisible: false,
            closeOnClickModel: false,
            destroyOnClose: true,
            formLabelWidth: '150px',
            ids: [],
            rules: {
                name: [
                    { required: true, message: "请输入姓名", trigger: "blur" }
                ],
                content: [
                    { required: true, message: "请输入救援内容", trigger: "blur" }
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
            htmlToExcel.getExcel('#selectTable', '救援记录表');
            this.excelDialogVisible = false;
        },

        // 打开对话框
        openDialogUI(id) {
            var that = this;
            if (id == null) { // 新增操作
                that.title = "新增救援记录";
            } else { // 修改操作
                that.title = "修改救援记录";
                rescueRecordApi.getRescueRecordById(id).then(res => {
                    that.rescueRecordForm = res.data;
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
            this.rescueRecordForm = {};
        },
        // 获取救援记录列表
        getRescueRecordList() {
            var that = this;
            rescueRecordApi.getRescueRecordList(that.searchModel).then(res => {
                that.rescueRecordList = res.data.rows;
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
        // 删除选中的多个救援记录
        deleteRescueRecordByIds() {
            var that = this;
            that.$confirm("你确定要删除编号 “" + that.ids + "” 的救援记录信息吗").then(() => {
                rescueRecordApi.deleteRescueRecordByIds(that.ids).then(res => {
                    // 重新获取所有救援记录列表进行展示
                    that.getRescueRecordList();
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
            this.getRescueRecordList();
        },

        // 处理当前页改变
        handleCurrentChange(val) {
            this.searchModel.currentPage = val;
            this.getRescueRecordList()
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

        // 删除救援记录
        handleDelete(id) {
            var that = this;
            that.$confirm("你确定要删除编号 “" + id + "” 的救援记录信息吗?").then(() => {
                // 调用删除指定救援记录的api进行删除
                rescueRecordApi.deleteRescueRecordById(id).then(res => {
                    // 调用查询救援记录列表，重新加载。
                    that.getRescueRecordList();
                    // 弹出成功消息提示
                    that.$message.success(res.message);
                });
            }).catch(() => {
                that.$message.error("删除失败！！！");
            })
        },

        // 处理新增或修改操作
        handleAddOrUpdate(rescueRecordFormRef) {
            var that = this;
            this.$refs[rescueRecordFormRef].validate((valid) => {
                if (valid) {
                    rescueRecordApi.saveRescueRecord(that.rescueRecordForm).then(res => {
                        // 清空对话框中的数据
                        that.rescueRecordForm = {};
                        // 关闭对话框
                        that.dialogFormVisible = false;
                        // 重新获取列表信息
                        that.getRescueRecordList();
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
        this.getRescueRecordList();
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