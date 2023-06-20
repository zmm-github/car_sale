<template>
    <div id="customer-container" class="common">
        <!-- 顶部搜索框 -->
        <el-card class="search">
            <el-row>
                <el-col :span="18">
                    <el-input placeholder="姓名" v-model="searchModel.name" clearable></el-input>
                    <el-input placeholder="状态" v-model="searchModel.status" clearable></el-input>
                    <el-button type="primary" @click="getCustomerList" size="medium" round
                        icon="el-icon-search">查询</el-button>
                </el-col>
                <el-col :span="6" align="right">
                    <el-button-group>
                        <el-button type="success" icon="el-icon-plus" @click="exportExcelSelect">导出</el-button>
                        <el-button type="primary" @click="openDialogUI(null)" icon="el-icon-plus">新增</el-button>
                        <el-button type="danger" icon="el-icon-delete" @click="deleteCustomerByIds">批量删除</el-button>
                    </el-button-group>

                </el-col>
            </el-row>
        </el-card>

        <!-- 结果列表 -->
        <el-card>
            <el-table size="small" border stripe ref="multipleTable" :data="customerList" :cell-style="rowStyle"
                :header-cell-style="headerCellStyle" :default-sort="{ prop: 'applicationTime', order: 'descending' }"
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
                <el-table-column prop="name" label="姓名" width="130">
                </el-table-column>
                <el-table-column prop="information" label="申请贷款信息" width="180">
                </el-table-column>
                <el-table-column prop="location" label="省/市" width="120">
                </el-table-column>
                <el-table-column prop="phoneNumber" label="手机号" width="130">
                </el-table-column>
                <el-table-column prop="status" label="状态" width="130">
                    <template slot-scope="scope">
                        <span style="color:red;" v-if="scope.row.status == '未处理'">{{ scope.row.status }}</span>
                        <span v-else>{{ scope.row.status }}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="applicationTime" sortable label="申请时间">
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
            <el-form ref="customerFormRef" :rules="rules" :model="customerForm">
                <el-form-item label="姓名" prop="name" :label-width="formLabelWidth">
                    <el-input v-model="customerForm.name" placeholder="请输入姓名" clearable autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="申请贷款信息" prop="information" :label-width="formLabelWidth">
                    <el-input v-model="customerForm.information" placeholder="请输入申请贷款信息" clearable
                        autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="省/市" prop="location" :label-width="formLabelWidth">
                    <el-input v-model="customerForm.location" placeholder="请输入所在的省/市" clearable
                        autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="手机号" prop="phoneNumber" :label-width="formLabelWidth">
                    <el-input v-model="customerForm.phoneNumber" show-word-limit maxlength="11" placeholder="请输入手机号" clearable
                        autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="状态" prop="status" :label-width="formLabelWidth">
                    <el-select v-model="customerForm.status" placeholder="请选择当前的状态">
                        <el-option label="已处理" value="已处理"></el-option>
                        <el-option label="已申请" value="已申请"></el-option>
                        <el-option label="未处理" value="未处理"></el-option>
                        <el-option label="审核中" value="审核中"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item v-if="customerForm.applicationTime != undefined" label="申请时间" prop="applicationTime"
                    :label-width="formLabelWidth">
                    <span>{{ customerForm.applicationTime }}</span>
                </el-form-item>
                <el-form-item align="right">
                    <el-button @click="handleCancel">取消</el-button>
                    <el-button type="primary" @click="handleAddOrUpdate('customerFormRef')">{{ !customerForm.id ? '新增' :
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
                <el-table-column prop="name" label="姓名" width="130">
                </el-table-column>
                <el-table-column prop="information" label="申请贷款信息" width="180">
                </el-table-column>
                <el-table-column prop="location" label="省/市" width="120">
                </el-table-column>
                <el-table-column prop="phoneNumber" label="手机号" width="130">
                </el-table-column>
                <el-table-column prop="status" label="状态" width="130">
                    <template slot-scope="scope">
                        <span style="color:red;" v-if="scope.row.status == '未处理'">{{ scope.row.status }}</span>
                        <span v-else>{{ scope.row.status }}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="applicationTime" sortable label="申请时间">
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
import customerApi from "@/api/customerManage"
import htmlToExcel from '@/utils/htmlToExcel';
export default {
    name: 'Customer',
    data() {
        /**
         * 校验手机号
         */
        const validatePhone = (rule, value, callback) => {
            const regExp = /^(0|86|17951)?(13[0-9]|15[012356789]|166|17[3678]|18[0-9]|14[57])[0-9]{8}$/;
            if (!regExp.test(value)) {
                callback(new Error('请输入正确的手机号'));
            } else {
                callback();
            }
        };
        return {
            title: '',
            total: 0,
            searchModel: {
                name: "",
                status: "",
                currentPage: 1,
                pageSize: 5
            },
            excelDialogVisible: false,
            selectData: [],
            customerList: [],
            customerForm: {},
            dialogFormVisible: false,
            closeOnClickModel: false,
            destroyOnClose: true,
            formLabelWidth: '150px',
            ids: [],
            rules: {
                name: [
                    { required: true, message: "请输入姓名", trigger: "blur" }
                ],
                information: [
                    { required: true, message: "请输入申请贷款信息", trigger: "blur" }
                ],
                location: [
                    { required: true, message: "请输入省/市", trigger: "blur" }
                ],
                phoneNumber: [
                    { required: true, message: "请输入手机号", trigger: "blur" },
                    {
                        validator: validatePhone,
                        trigger: ['change', 'blur']
                    }
                ],
                status: [
                    { required: true, message: '请选择当前状态', trigger: 'change' }
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
            htmlToExcel.getExcel('#selectTable', '客户表');
            this.excelDialogVisible = false;
        },

        // 打开对话框
        openDialogUI(id) {
            var that = this;
            if (id == null) { // 新增操作
                that.title = "新增顾客";
            } else { // 修改操作
                that.title = "修改顾客";
                customerApi.getCustomerById(id).then(res => {
                    that.customerForm = res.data;
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
            this.customerForm = {};
        },
        // 获取顾客列表
        getCustomerList() {
            var that = this;
            customerApi.getCustomerList(that.searchModel).then(res => {
                that.customerList = res.data.rows;
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
        // 删除选中的多个顾客
        deleteCustomerByIds() {
            var that = this;
            that.$confirm("你确定要删除编号 “" + that.ids + "” 的顾客信息吗").then(() => {
                customerApi.deleteCustomerByIds(that.ids).then(res => {
                    // 重新获取所有顾客列表进行展示
                    that.getCustomerList();
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
            this.getCustomerList();
        },

        // 处理当前页改变
        handleCurrentChange(val) {
            this.searchModel.currentPage = val;
            this.getCustomerList()
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

        // 删除顾客
        handleDelete(id) {
            var that = this;
            that.$confirm("你确定要删除编号 “" + id + "” 的顾客信息吗?").then(() => {
                // 调用删除指定顾客的api进行删除
                customerApi.deleteCustomerById(id).then(res => {
                    // 调用查询顾客列表，重新加载。
                    that.getCustomerList();
                    // 弹出成功消息提示
                    that.$message.success(res.message);
                });
            }).catch(() => {
                that.$message.error("删除失败！！！");
            })
        },

        // 处理新增或修改操作
        handleAddOrUpdate(customerFormRef) {
            var that = this;
            this.$refs[customerFormRef].validate((valid) => {
                if (valid) {
                    customerApi.saveCustomer(that.customerForm).then(res => {
                        // 清空对话框中的数据
                        that.customerForm = {};
                        // 关闭对话框
                        that.dialogFormVisible = false;
                        // 重新获取列表信息
                        that.getCustomerList();
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
        this.getCustomerList();
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