<template>
    <div id="appointmentRecord-container" class="common">
        <!-- 顶部搜索框 -->
        <el-card class="search">
            <el-row>
                <el-col :span="18">
                    <el-input placeholder="姓名" v-model="searchModel.name" clearable></el-input>
                    <el-input placeholder="类型" v-model="searchModel.type" clearable></el-input>
                    <el-button type="primary" @click="getAppointmentRecordList" size="medium" round
                        icon="el-icon-search">查询</el-button>
                </el-col>
                <el-col :span="6" align="right">
                    <el-button-group>
                        <el-button type="success" icon="el-icon-plus" @click="exportExcelSelect">导出</el-button>
                        <el-button type="primary" @click="openDialogUI(null)" icon="el-icon-plus">新增</el-button>
                        <el-button type="danger" icon="el-icon-delete" @click="deleteAppointmentRecordByIds">批量删除</el-button>
                    </el-button-group>
                </el-col>
            </el-row>
        </el-card>

        <!-- 结果列表 -->
        <el-card>
            <el-table size="small" border stripe ref="multipleTable" :data="appointmentRecordList" :cell-style="rowStyle"
                :header-cell-style="headerCellStyle" tooltip-effect="dark" style="width: 100%"
                :default-sort="{ prop: 'createTime', order: 'descending' }" @selection-change="handleSelectionChange">
                <el-table-column fixed="left" type="selection" width="55">
                </el-table-column>
                <el-table-column fixed="left" label="#" width="60">
                    <template slot-scope="scope">
                        {{
                            (searchModel.currentPage - 1) * searchModel.pageSize + scope.$index + 1
                        }}
                    </template>
                </el-table-column>
                <el-table-column fixed="left" prop="id" label="ID" width="60">
                </el-table-column>
                <el-table-column prop="name" label="姓名" width="130">
                </el-table-column>
                <el-table-column prop="phoneNumber" label="手机号" width="110">
                </el-table-column>
                <el-table-column prop="carSeries" label="车系" width="160">
                </el-table-column>
                <el-table-column prop="carModel" label="车型" width="160">
                </el-table-column>
                <el-table-column prop="arriveTime" label="到店时间" width="120">
                    <template slot-scope="scope">
                        {{ scope.row.arriveTime | dataFormat }}
                    </template>
                </el-table-column>
                <el-table-column prop="message" label="留言" width="120">
                </el-table-column>
                <el-table-column prop="type" label="预约类型" width="120">
                </el-table-column>
                <el-table-column prop="createTime" sortable label="创建时间" width="160">
                </el-table-column>
                <el-table-column fixed="right" label="操作" width="160">
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
        <el-dialog @close="clearData" :title="title" top="10vh" :visible.sync="dialogFormVisible"
            :close-on-click-modal="closeOnClickModel" :destroy-on-close="destroyOnClose" :before-close="handleClose">
            <el-form ref="appointmentRecordFormRef" :rules="rules" :model="appointmentRecordForm">
                <el-form-item label="姓名" prop="name" :label-width="formLabelWidth">
                    <el-input v-model="appointmentRecordForm.name" placeholder="请输入姓名" clearable autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="手机号" prop="phoneNumber" :label-width="formLabelWidth">
                    <el-input v-model="appointmentRecordForm.phoneNumber" show-word-limit maxlength="11" placeholder="请输入手机号" clearable
                        autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="车系" prop="carSeries" :label-width="formLabelWidth">
                    <el-select v-model="appointmentRecordForm.carSeries" placeholder="请选择车系">
                        <el-option v-for="(carSeries, index) in carSeriesList" :key="index" :label="carSeries.carSeries"
                            :value="carSeries.carSeries"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="车型" prop="carModel" :label-width="formLabelWidth">
                    <el-select v-model="appointmentRecordForm.carModel" placeholder="请选择车型">
                        <el-option v-for="(carModel, index) in carModelList" :key="index" :label="carModel.carModel"
                            :value="carModel.carModel"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="留言" prop="message" :label-width="formLabelWidth">
                    <el-input v-model="appointmentRecordForm.message" placeholder="请输入留言" clearable autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="预约类型" prop="type" :label-width="formLabelWidth">
                    <el-input v-model="appointmentRecordForm.type" placeholder="请输入车型" clearable autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item align="right">
                    <el-button @click="handleCancel">取消</el-button>
                    <el-button type="primary" @click="handleAddOrUpdate('appointmentRecordFormRef')">{{ !appointmentRecordForm.id ? '新增'
                        :
                        '修改'
                    }}</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>

        <!-- 导出表格预览对话框 -->
        <el-dialog title="表格导出预览" top="8vh" width="77%" :visible.sync="excelDialogVisible"
            :close-on-click-modal="closeOnClickModel" :before-close="handleClose">
            <el-table size="small" stripe ref="multipleTable" align="center" :cell-style="rowStyle"
                :header-cell-style="headerCellStyle" :data="selectData" id="selectTable" border tooltip-effect="dark"
                @selection-change="handleSelectionChange" style="width: 100%">
                <el-table-column prop="id" label="ID" width="60">
                </el-table-column>
                <el-table-column prop="name" label="姓名" width="130">
                </el-table-column>
                <el-table-column prop="phoneNumber" label="手机号" width="110">
                </el-table-column>
                <el-table-column prop="carSeries" label="车系" width="160">
                </el-table-column>
                <el-table-column prop="carModel" label="车型" width="160">
                </el-table-column>
                <el-table-column prop="arriveTime" label="到店时间" width="120">
                    <template slot-scope="scope">
                        {{ scope.row.arriveTime | dataFormat }}
                    </template>
                </el-table-column>
                <el-table-column prop="message" label="留言" width="120">
                </el-table-column>
                <el-table-column prop="type" label="预约类型" width="120">
                </el-table-column>
                <el-table-column prop="createTime" sortable label="创建时间" width="160">
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
import appointmentRecordApi from "@/api/appointmentRecordManage"
import carSeriesApi from "@/api/carSeriesManage"
import carModelApi from "@/api/carModelManage"
import htmlToExcel from '@/utils/htmlToExcel';
export default {
    name: 'AppointmentRecord',
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
            carSeriesList: [],
            carModelList: [],
            total: 0,
            excelDialogVisible: false,
            selectData: [],
            searchModel: {
                name: "",
                type: "",
                currentPage: 1,
                pageSize: 5
            },
            appointmentRecordList: [],
            appointmentRecordForm: {},
            dialogFormVisible: false,
            closeOnClickModel: false,
            destroyOnClose: true,
            formLabelWidth: '150px',
            ids: [],
            rules: {
                name: [
                    { required: true, message: "请输入姓名", trigger: "blur" }
                ],
                phoneNumber: [
                    { required: true, message: "请输入手机号", trigger: "blur" },
                    {
                        validator: validatePhone,
                        trigger: ['change', 'blur']
                    }
                ],
                carSeries: [
                    { required: true, message: "请输入车系", trigger: "blur" }
                ],
                carModel: [
                    { required: true, message: "请输入车型", trigger: "blur" }
                ],
                message: [
                    { required: true, message: "请输入留言", trigger: "blur" }
                ],
                type: [
                    { required: true, message: "请输入预约类型", trigger: "blur" }
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
            htmlToExcel.getExcel('#selectTable', '预约记录表');
            this.excelDialogVisible = false;
        },

        // 打开对话框
        openDialogUI(id) {
            var that = this;
            // 获取所有车系信息
            carSeriesApi.getAllCarSeries().then(res => {
                that.carSeriesList = res.data;
            })
            // 获取所有车型信息
            carModelApi.getAllCarModel().then(res => {
                that.carModelList = res.data;
            })
            if (id == null) { // 新增操作
                that.title = "新增预约记录";
            } else { // 修改操作
                that.title = "修改预约记录";
                appointmentRecordApi.getAppointmentRecordById(id).then(res => {
                    that.appointmentRecordForm = res.data;
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
            this.appointmentRecordForm = {};
        },
        // 获取预约记录列表
        getAppointmentRecordList() {
            var that = this;
            appointmentRecordApi.getAppointmentRecordList(that.searchModel).then(res => {
                that.appointmentRecordList = res.data.rows;
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
        // 删除选中的多个预约记录
        deleteAppointmentRecordByIds() {
            var that = this;
            that.$confirm("你确定要删除编号 “" + that.ids + "” 的预约记录信息吗").then(() => {
                appointmentRecordApi.deleteAppointmentRecordByIds(that.ids).then(res => {
                    // 重新获取所有预约记录列表进行展示
                    that.getAppointmentRecordList();
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
            this.getAppointmentRecordList();
        },

        // 处理当前页改变
        handleCurrentChange(val) {
            this.searchModel.currentPage = val;
            this.getAppointmentRecordList()
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

        // 删除预约记录
        handleDelete(id) {
            var that = this;
            that.$confirm("你确定要删除编号 “" + id + "” 的预约记录信息吗?").then(() => {
                // 调用删除指定预约记录的api进行删除
                appointmentRecordApi.deleteAppointmentRecordById(id).then(res => {
                    // 调用查询预约记录列表，重新加载。
                    that.getAppointmentRecordList();
                    // 弹出成功消息提示
                    that.$message.success(res.message);
                });
            }).catch(() => {
                that.$message.error("删除失败！！！");
            })
        },

        // 处理新增或修改操作
        handleAddOrUpdate(appointmentRecordFormRef) {
            var that = this;
            this.$refs[appointmentRecordFormRef].validate((valid) => {
                if (valid) {
                    appointmentRecordApi.saveAppointmentRecord(that.appointmentRecordForm).then(res => {
                        // 清空对话框中的数据
                        that.appointmentRecordForm = {};
                        // 关闭对话框
                        that.dialogFormVisible = false;
                        // 重新获取列表信息
                        that.getAppointmentRecordList();
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
    filters: { // 私有局部过滤器，只能在 当前 VM 对象所控制的 View 区域进行使用

        dataFormat(input, pattern = "") { // 在参数列表中 通过 pattern="" 来指定形参默认值，防止报错

            var dt = new Date(input);

            // 获取年月日

            var y = dt.getFullYear();

            var m = (dt.getMonth() + 1).toString().padStart(2, '0');

            var d = dt.getDate().toString().padStart(2, '0');

            return `${y}-${m}-${d}`;
        }

    },
    mounted() {
        this.getAppointmentRecordList();
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