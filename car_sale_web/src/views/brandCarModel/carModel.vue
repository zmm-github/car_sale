<template>
    <div id="carModel-container" class="common">
        <!-- 搜索栏 -->
        <el-card class="search">
            <el-row>
                <el-col :span="18">
                    <el-input placeholder="车型" v-model="searchModel.carModel" clearable></el-input>
                    <el-input placeholder="创建人" v-model="searchModel.createPeople" clearable></el-input>
                    <el-button type="primary" @click="getCarModelList" size="medium" round
                        icon="el-icon-search">查询</el-button>
                </el-col>
                <el-col :span="6" align="right">
                    <el-button-group>
                        <el-button type="success" icon="el-icon-plus" @click="exportExcelSelect">导出</el-button>
                        <el-button type="primary" @click="openDialogUI(null)" icon="el-icon-plus">新增</el-button>
                        <el-button type="danger" icon="el-icon-delete" @click="deleteCarModelByIds">批量删除</el-button>
                    </el-button-group>
                </el-col>
            </el-row>
        </el-card>

        <!-- 结果列表 -->
        <el-card>
            <el-table size="small" border stripe ref="multipleTable" :data="carModelList" :cell-style="rowStyle"
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
                <el-table-column prop="image" label="图片" align="center" width="180">
                    <template slot-scope="scope">
                        <el-image v-if="scope.row.image" style="width: 80px; height: 80px; border-radius: 50%;" alt="图片显示失败"
                            :src="scope.row.image" :preview-src-list="[scope.row.image]">
                        </el-image>
                        <el-image v-else style="width: 80px; height: 80px; border-radius: 50%;" alt="图片显示失败" src="">
                        </el-image>
                    </template>
                </el-table-column>
                <el-table-column prop="carModel" label="车型名称" width="200">
                </el-table-column>
                <el-table-column prop="money" label="官方指导价（元）" width="140">
                </el-table-column>
                <el-table-column prop="createTime" sortable label="创建时间">
                </el-table-column>
                <el-table-column prop="createPeople" label="创建人" width="140">
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
        <el-dialog @close="clearData" :title="title" :visible.sync="dialogFormVisible" top="5vh"
            :close-on-click-modal="closeOnClickModel" :destroy-on-close="destroyOnClose" :before-close="handleClose">
            <el-form ref="carModelFormRef" :rules="rules" :model="carModelForm">
                <el-form-item label="图片" prop="image" :label-width="formLabelWidth">
                    <el-upload class="avatar-uploader" action="http://localhost:9999/file/ossUpload" ref="upload"
                        :auto-upload="false" drag :on-change="handleChange" :on-success="handleUploadSuccess"
                        :show-file-list="false">
                        <img v-if="carModelForm.image || imageUrl" alt="图片上传失败"
                            :src="imageUrl == '' ? carModelForm.image : imageUrl" class="avatar">
                        <div v-else>
                            <i class="el-icon-upload"></i>
                            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                        </div>
                        <div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过500kb</div>
                    </el-upload>
                </el-form-item>
                <el-form-item label="车型名称" prop="carModel" :label-width="formLabelWidth">
                    <el-input v-model="carModelForm.carModel" placeholder="车型名称" clearable autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="官方指导价（元）" prop="money" :label-width="formLabelWidth">
                    <el-input v-model="carModelForm.money" placeholder="金额范围：例10-20" clearable
                        autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item v-if="carModelForm.createTime != undefined" label="创建时间" prop="createTime"
                    :label-width="formLabelWidth">
                    <span>{{ carModelForm.createTime }}</span>
                </el-form-item>
                <el-form-item label="创建人" prop="createPeople" :label-width="formLabelWidth">
                    <el-input v-model="carModelForm.createPeople" placeholder="创建人名称" clearable
                        autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item align="right">
                    <el-button @click="handleCancel">取消</el-button>
                    <el-button type="primary" @click.prevent="handleAddAndUpdate('carModelFormRef')">{{ !carModelForm.id ?
                        '新增' :
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
                <el-table-column prop="carModel" label="车型名称" width="200">
                </el-table-column>
                <el-table-column prop="money" label="官方指导价（元）" width="140">
                </el-table-column>
                <el-table-column prop="createTime" sortable label="创建时间">
                </el-table-column>
                <el-table-column prop="createPeople" label="创建人" width="140">
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
import carModelApi from '@/api/carModelManage'
export default {
    name: 'CarModel',
    data() {
        return {
            title: '',
            total: 0,
            file: {},
            imageUrl: "",
            excelDialogVisible: false,
            selectData: [],
            searchModel: {
                carModel: "",
                createPeople: "",
                currentPage: 1,
                pageSize: 5,
            },
            carModelList: [],
            carModelForm: {},
            formLabelWidth: '150px',
            dialogFormVisible: false,
            closeOnClickModel: false,
            destroyOnClose: true,
            ids: [],
            rules: {
                carModel: [
                    { required: true, message: '请输入车型名称', trigger: 'blur' }
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
            htmlToExcel.getExcel('#selectTable', '车型表');
            this.excelDialogVisible = false;
        },

        // 打开对话框
        openDialogUI(id) {
            var that = this;
            if (id == null) { // 新增操作
                that.title = "新增车型";
            } else { // 修改操作
                that.title = "修改车型";
                carModelApi.getCarModelById(id).then(res => {
                    that.carModelForm = res.data;
                })
            }
            // 显示对话框
            this.dialogFormVisible = true;
        },

        // 清楚对话框中表单的数据
        clearData() {
            // 清理数据
            this.carModelForm = {};
            this.imageUrl = "";
            this.file = {};
        },

        // 设置表格样式
        rowStyle() {
            return "text-align:center";
        },
        headerCellStyle() {
            return "text-align: center; background: #e9e2f1;";
        },

        handleSelectionChange(val) {
            this.selectData = val;
            val.forEach((ele, index) => {
                this.ids[index] = ele.id;
            });
        },

        // 删除选中的多个车型
        deleteCarModelByIds() {
            var that = this;
            that.$confirm("你确定要删除编号 “" + that.ids + "” 的车系信息吗").then(() => {
                carModelApi.deleteCarModelByIds(that.ids).then(res => {
                    // 重新获取所有车型列表进行展示
                    that.getCarModelList();
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
            this.getCarModelList();
        },

        // 处理当前页改变
        handleCurrentChange(val) {
            this.searchModel.currentPage = val;
            this.getCarModelList()
        },

        // 获取车型列表
        getCarModelList() {
            var that = this;
            carModelApi.getCarModelList(that.searchModel).then(res => {
                that.total = res.data.total;
                that.carModelList = res.data.rows;
            });
        },

        // 删除车型
        handleDelete(id) {
            var that = this;
            that.$confirm("你确定要删除编号 “" + id + "” 的车型信息吗?").then(() => {
                carModelApi.deleteCarModelById(id).then(res => {
                    // 关闭对话框
                    that.dialogFormVisible = false;
                    // 重新获取所有车型列表
                    that.getCarModelList();
                    // 弹出删除成功消息提示
                    that.$message.success(res.message);
                })
            }).catch(() => {
                that.$message.error("删除车型失败！！！");
            })
        },

        // 关闭对话框
        handleClose() {
            this.$confirm('确认关闭？').then(res => {
                this.dialogFormVisible = false; // 关闭对话框
                this.excelDialogVisible = false; // 关闭表格对话框
            }).catch(() => { })
        },

        // 取消对话框
        handleCancel() {
            this.dialogFormVisible = false;//关闭对话框
            this.excelDialogVisible = false; // 关闭表格对话框
        },

        // 处理上传成功的函数
        handleUploadSuccess(data) {
            var that = this;
            that.carModelForm.image = data.data;
            carModelApi.saveCarModel(that.carModelForm).then(res => {
                // 关闭对话框
                that.dialogFormVisible = false;
                // 重新获取列表信息
                that.getCarModelList();
                // 显示成功提示信息
                that.$message.success(res.message);
            })
        },

        // 图片改变时调用
        handleChange(file) {
            this.file = file.raw;
            const isJPG = file.raw.type === 'image/jpeg';
            const isLt2M = file.raw.size / 1024 / 1024 < 2;

            if (!isJPG) {
                this.$message.error('上传的图片只能是 JPG 格式!');
            } else if (!isLt2M) {
                this.$message.error('上传的图片大小不能超过 2MB!');
            } else {
                this.imageUrl = URL.createObjectURL(file.raw);
            }
            return isJPG && isLt2M;
        },

        // 新增和修改车系
        handleAddAndUpdate(carModelFormRef) {
            var that = this;
            this.$refs[carModelFormRef].validate((valid) => {
                if (valid) {
                    if (JSON.stringify(that.file) != '{}') {
                        that.$refs.upload.submit();
                    } else {
                        carModelApi.saveCarModel(that.carModelForm).then(res => {
                            // 关闭对话框
                            that.dialogFormVisible = false;
                            // 重新获取列表信息
                            that.getCarModelList();
                            // 显示成功提示信息
                            that.$message.success(res.message);
                        })
                    }
                } else {
                    that.$message.error("请按要求填写相关信息后再进行提交！");
                    return false;
                }
            });
        }
    },
    mounted() {
        this.getCarModelList();
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

.avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;

    .el-upload:hover {
        border-color: #409EFF;
    }

    .el-upload-dragger {
        .avatar {
            width: 100%;
            height: 178px;
            display: block;
        }
    }
}
</style>