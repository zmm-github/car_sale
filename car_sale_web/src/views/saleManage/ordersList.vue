<template>
    <div id="ordersList-container" class="common">
        <!-- 顶部搜索框 -->
        <el-card class="search">
            <el-row>
                <el-col :span="18">
                    <el-input placeholder="客户姓名" v-model="searchModel.name" clearable></el-input>
                    <el-input placeholder="销售员姓名" v-model="searchModel.saleName" clearable></el-input>
                    <el-select v-model="searchModel.status" clearable placeholder="请选择支付状态">
                        <el-option label="未支付" value="未支付"></el-option>
                        <el-option label="已支付" value="已支付"></el-option>
                    </el-select>
                    <el-button type="primary" @click="getOrdersList" size="medium" round
                        icon="el-icon-search">查询</el-button>
                </el-col>
                <el-col :span="6" align="right">
                    <el-button-group>
                        <el-button type="success" icon="el-icon-plus" @click="exportExcelSelect">导出</el-button>
                        <el-button type="primary" @click="openDialogUI(null)" icon="el-icon-plus">新增</el-button>
                        <el-button type="danger" icon="el-icon-delete" @click="deleteOrdersByIds">批量删除</el-button>
                    </el-button-group>
                </el-col>
            </el-row>
        </el-card>

        <!-- 结果列表 -->
        <el-card>
            <el-table size="small" border stripe ref="multipleTable" :data="ordersList" :cell-style="rowStyle"
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
                <el-table-column fixed="left" prop="name" label="客户姓名" width="110">
                    <template slot-scope="scope">
                        <el-tag>{{ scope.row.name }}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="phoneNumber" label="客户手机号" width="100">
                </el-table-column>
                <el-table-column prop="price" label="总价（元）" width="110">
                </el-table-column>
                <el-table-column prop="saleName" label="销售员" width="110">
                </el-table-column>
                <el-table-column prop="carSeries" label="车系" width="170">
                </el-table-column>
                <el-table-column prop="carModel" label="车型" width="170">
                </el-table-column>
                <el-table-column prop="color" label="颜色" width="100">
                </el-table-column>
                <el-table-column prop="amount" label="数量" width="60">
                </el-table-column>
                <el-table-column prop="createTime" sortable label="创建时间" width="140">
                </el-table-column>
                <el-table-column prop="updateTime" label="更新时间" width="140">
                </el-table-column>
                <el-table-column prop="status" label="状态" width="120">
                    <template slot-scope="scope">
                        <el-tag v-if="scope.row.status == '已支付'" type="success">{{ scope.row.status }}</el-tag>
                        <el-tag v-else type="danger">{{ scope.row.status }}</el-tag>
                    </template>
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
        <el-dialog @close="clearData" :title="title" :visible.sync="dialogFormVisible"
            :close-on-click-modal="closeOnClickModel" :destroy-on-close="destroyOnClose" :before-close="handleClose">
            <el-form ref="ordersFormRef" size="small" style="margin-right: 40px;" :rules="rules" :model="ordersForm">
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="客户身份证号" prop="card" :label-width="formLabelWidth">
                            <el-input v-model="ordersForm.card" show-word-limit maxlength="18" placeholder="请输入身份证号"
                                clearable autocomplete="off"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="客户姓名" prop="name" :label-width="formLabelWidth">
                            <el-input v-model="ordersForm.name" placeholder="请输入客户姓名" clearable
                                autocomplete="off"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="客户手机号" prop="phoneNumber" :label-width="formLabelWidth">
                            <el-input v-model="ordersForm.phoneNumber" show-word-limit maxlength="11" placeholder="请输入客户手机号"
                                clearable autocomplete="off"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="支付状态" prop="status" :label-width="formLabelWidth">
                            <el-select v-model="ordersForm.status" clearable placeholder="请选择支付状态">
                                <el-option label="未支付" value="未支付"></el-option>
                                <el-option label="已支付" value="已支付"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="车 系" prop="carSeries" :label-width="formLabelWidth">
                            <el-select v-model="ordersForm.carSeries" clearable placeholder="请选择车系">
                                <el-option v-for="(carSeries, index) in carSeriesList" :key="index"
                                    :label="carSeries.carSeries" :value="carSeries.carSeries"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="车 型" prop="carModel" :label-width="formLabelWidth">
                            <el-select v-model="ordersForm.carModel" clearable placeholder="请选择车型">
                                <el-option v-for="(carModel, index) in carModelList" :key="index" :label="carModel.carModel"
                                    :value="carModel.carModel"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="颜 色" prop="color" :label-width="formLabelWidth">
                            <el-select v-model="ordersForm.color" clearable placeholder="请选择汽车颜色">
                                <el-option label="红色" value="红色"></el-option>
                                <el-option label="蓝色" value="蓝色"></el-option>
                                <el-option label="绿色" value="绿色"></el-option>
                                <el-option label="黄色" value="黄色"></el-option>
                                <el-option label="黑色" value="黑色"></el-option>
                                <el-option label="白色" value="白色"></el-option>
                                <el-option label="紫色" value="紫色"></el-option>
                                <el-option label="银色" value="银色"></el-option>
                                <el-option label="灰色" value="灰色"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="数 量" prop="amount" :label-width="formLabelWidth">
                            <el-input-number v-model="ordersForm.amount" size="medium" :min="0"
                                placeholder="请输入数量"></el-input-number>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="售价（元）" prop="price" :label-width="formLabelWidth">
                            <el-input v-model.number="ordersForm.price" clearable placeholder="请输入售价"  autocomplete="off"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">

                        <el-form-item label="销售员" prop="saleName" :label-width="formLabelWidth">
                            <el-input v-model="ordersForm.saleName" placeholder="请输入销售员" clearable
                                autocomplete="off"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="handleCancel">取消</el-button>
                <el-button type="primary" @click="handleAddOrUpdate('ordersFormRef')">{{ !ordersForm.id ? '新增'
                    :
                    '修改'
                }}</el-button>
            </span>
        </el-dialog>

        <!-- 导出表格预览对话框 -->
        <el-dialog title="表格导出预览" top="8vh" width="70%" :visible.sync="excelDialogVisible"
            :close-on-click-modal="closeOnClickModel" :before-close="handleClose">
            <el-table size="small" stripe ref="multipleTable" align="center" :cell-style="rowStyle"
                :header-cell-style="headerCellStyle" :data="selectData" id="selectTable" border tooltip-effect="dark"
                @selection-change="handleSelectionChange" style="width: 100%">
                <el-table-column prop="id" label="ID" width="60">
                </el-table-column>
                <el-table-column prop="name" label="客户姓名" width="110">
                    <template slot-scope="scope">
                        <el-tag>{{ scope.row.name }}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="phoneNumber" label="客户手机号" width="100">
                </el-table-column>
                <el-table-column prop="price" label="总价（元）" width="110">
                </el-table-column>
                <el-table-column prop="saleName" label="销售员" width="110">
                </el-table-column>
                <el-table-column prop="carSeries" label="车系" width="170">
                </el-table-column>
                <el-table-column prop="carModel" label="车型" width="170">
                </el-table-column>
                <el-table-column prop="color" label="颜色" width="100">
                </el-table-column>
                <el-table-column prop="amount" label="数量" width="60">
                </el-table-column>
                <el-table-column prop="createTime" sortable label="创建时间" width="140">
                </el-table-column>
                <el-table-column prop="updateTime" label="更新时间" width="140">
                </el-table-column>
                <el-table-column prop="status" label="状态" width="120">
                    <template slot-scope="scope">
                        <el-tag v-if="scope.row.status == '已支付'" type="success">{{ scope.row.status }}</el-tag>
                        <el-tag v-else type="danger">{{ scope.row.status }}</el-tag>
                    </template>
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
import ordersApi from "@/api/ordersManage.js"
import carSeriesApi from "@/api/carSeriesManage"
import carModelApi from "@/api/carModelManage"
import htmlToExcel from '@/utils/htmlToExcel';
export default {
    name: 'OrdersList',
    data() {
        /**
         * 校验售价
         */
        const checkPrice = (rule, value, callback) => {
            if (!value) {
                return callback(new Error('请输入售价'));
            } else if (!Number.isInteger(value)) {
                callback(new Error('请输入数字值'));
            } else {
                if (value < 0) {
                    callback(new Error('售价必须大于0'));
                } else {
                    callback();
                }
            }
        };

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

        /**
         * 校验身份证号
         */
        const validateCard = (rule, value, callback) => {
            const regExp = /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/;
            if (!regExp.test(value)) {
                callback(new Error('请输入正确的身份证号'));
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
                saleName: "",
                status: "",
                currentPage: 1,
                pageSize: 5
            },
            ordersList: [],
            ordersForm: {},
            dialogFormVisible: false,
            closeOnClickModel: false,
            destroyOnClose: true,
            formLabelWidth: '120px',
            ids: [],
            rules: {
                card: [
                    { required: true, message: "请输入顾客身份证号", trigger: "blur" },
                    {
                        validator: validateCard,
                        trigger: ['change', 'blur']
                    }
                ],
                name: [
                    { required: true, message: "请输入顾客姓名", trigger: "blur" }
                ],
                phoneNumber: [
                    { required: true, message: "请输入顾客手机号", trigger: "blur" },
                    {
                        validator: validatePhone,
                        trigger: ['change', 'blur']
                    }
                ],
                status: [
                    { required: true, message: "请选择支付状态", trigger: "blur" }
                ],
                price: [
                    { required: true, validator: checkPrice, trigger: ['blur', 'change'] }
                ],
                carSeries: [
                    { required: true, message: "请选择车系", trigger: "blur" }
                ],
                carModel: [
                    { required: true, message: "请选择车型", trigger: "blur" }
                ],
                color: [
                    { required: true, message: "请输入颜色", trigger: "blur" }
                ],
                amount: [
                    { required: true, message: "请输入数量", trigger: ["blur", "change"] }
                ],
                saleName: [
                    { required: true, message: "请输入销售员", trigger: "blur" }
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
            htmlToExcel.getExcel('#selectTable', '订单表');
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
                that.title = "新增订单";
            } else { // 修改操作
                that.title = "修改订单";
                ordersApi.getOrdersById(id).then(res => {
                    that.ordersForm = res.data;
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
            this.ordersForm = {};
        },
        // 获取订单列表
        getOrdersList() {
            var that = this;
            ordersApi.getOrdersList(that.searchModel).then(res => {
                that.ordersList = res.data.rows;
                that.total = res.data.total;
            })
        },
        // 选择的记录改变
        handleSelectionChange(val) {
            console.log(val);
            this.selectData = val;
            val.forEach((ele, index) => {
                this.ids[index] = ele.id;
            });
        },
        // 删除选中的多个订单
        deleteOrdersByIds() {
            var that = this;
            that.$confirm("你确定要删除编号 “" + that.ids + "” 的订单信息吗").then(() => {
                ordersApi.deleteOrdersByIds(that.ids).then(res => {
                    // 重新获取所有订单列表进行展示
                    that.getOrdersList();
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
            this.getOrdersList();
        },

        // 处理当前页改变
        handleCurrentChange(val) {
            this.searchModel.currentPage = val;
            this.getOrdersList()
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

        // 删除订单
        handleDelete(id) {
            var that = this;
            that.$confirm("你确定要删除编号 “" + id + "” 的订单信息吗?").then(() => {
                // 调用删除指定订单的api进行删除
                ordersApi.deleteOrdersById(id).then(res => {
                    // 调用查询订单列表，重新加载。
                    that.getOrdersList();
                    // 弹出成功消息提示
                    that.$message.success(res.message);
                });
            }).catch(() => {
                that.$message.error("删除失败！！！");
            })
        },

        // 处理新增或修改操作
        handleAddOrUpdate(ordersFormRef) {
            var that = this;
            this.$refs[ordersFormRef].validate((valid) => {
                if (valid) {
                    ordersApi.saveOrders(that.ordersForm).then(res => {
                        // 清空对话框中的数据
                        that.ordersForm = {};
                        // 关闭对话框
                        that.dialogFormVisible = false;
                        // 重新获取列表信息
                        that.getOrdersList();
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
        this.getOrdersList();
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