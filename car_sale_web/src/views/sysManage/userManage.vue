<template>
    <div id="userManage-container" class="common">
        <!-- 顶部搜索框 -->
        <el-card class="search">
            <el-row>
                <el-col :span="18">
                    <el-input placeholder="请输入昵称" v-model="searchModel.nickName" clearable></el-input>
                    <el-select v-model="searchModel.status" clearable placeholder="请选择用户状态">
                        <el-option label="正常" :value="1"></el-option>
                        <el-option label="禁用" :value="0"></el-option>
                    </el-select>
                    <el-button type="primary" round @click="getUserList">查找</el-button>
                </el-col>
                <el-col :span="6" align="right">
                    <el-button-group>
                        <el-button type="success" icon="el-icon-plus" @click="exportExcelSelect">导出</el-button>
                        <el-button type="primary" icon="el-icon-plus" @click="openDialogUI(null)">新增</el-button>
                        <el-button type="danger" icon="el-icon-delete" @click="deleteUserByIds">批量删除</el-button>
                    </el-button-group>
                </el-col>
            </el-row>
        </el-card>

        <!-- 结果列表 -->
        <el-card>
            <el-table size="small" stripe ref="multipleTable" align="center" :cell-style="rowStyle"
                :header-cell-style="headerCellStyle" :data="userList" border tooltip-effect="dark"
                :default-sort="{ prop: 'id' }" @selection-change="handleSelectionChange" style="width: 100%">
                <el-table-column type="selection" width="55">
                </el-table-column>
                <el-table-column label="#" width="80">
                    <template slot-scope="scope">
                        {{
                            (searchModel.currentPage - 1) * searchModel.pageSize + scope.$index + 1
                        }}
                    </template>
                </el-table-column>
                <el-table-column prop="id" label="ID" width="100">
                </el-table-column>
                <el-table-column prop="nickName" label="昵称" width="140">
                </el-table-column>
                <el-table-column prop="avatar" label="头像" width="140">
                    <template slot-scope="scope">
                        <el-avatar :size="50" :src="scope.row.avatar" fit="cover" @error="errorHandler">
                            <img :src="scope.row.avatar" alt="" />
                        </el-avatar>
                    </template>
                </el-table-column>
                <el-table-column prop="email" label="电子邮件">
                </el-table-column>
                <el-table-column prop="phoneNumber" label="手机号" width="140">
                </el-table-column>
                <el-table-column prop="sex" label="性别" width="100">
                    <template slot-scope="scope">
                        <span v-if="scope.row.sex == 0">男</span>
                        <span v-else>女</span>
                    </template>
                </el-table-column>
                <el-table-column prop="status" label="状态" width="150">
                    <template slot-scope="scope">
                        <el-tag v-if="scope.row.status == 1" type="success">正常</el-tag>
                        <el-tag v-else type="danger">禁用</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="160">
                    <template slot-scope="scope">
                        <el-button size="mini" type="success" @click="openDialogUI(scope.row.id)">编辑</el-button>
                        <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-card>

        <!-- 分页 -->
        <el-pagination v-if="total > 0" @size-change="handleSizeChange" align="right" @current-change="handleCurrentChange"
            :current-page="searchModel.currentPage" :page-sizes="[5, 10, 15, 20]" :page-size="searchModel.pageSize"
            layout="total, sizes, prev, pager, next, jumper" :total="total">
        </el-pagination>

        <!-- 对话框 -->
        <el-dialog @close="clearData" :title="title" top="5vh" :visible.sync="dialogFormVisible"
            :close-on-click-modal="closeOnClickModel" :destroy-on-close="destroyOnClose" :before-close="handleClose">
            <el-form ref="userForm" style="margin-right: 40px;" :rules="rules" :model="userForm">
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="用户名" prop="username" :label-width="formLabelWidth">
                            <el-input v-model="userForm.username" placeholder="请输入用户名" clearable
                                autocomplete="off"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="昵称" prop="nickName" :label-width="formLabelWidth">
                            <el-input v-model="userForm.nickName" placeholder="请输入昵称" clearable
                                autocomplete="off"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="密码" v-if="userForm.id == null || userForm.id == undefined" prop="password"
                            :label-width="formLabelWidth">
                            <el-input type="password" v-model="userForm.password" placeholder="请输入密码" clearable
                                autocomplete="off"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="电子邮件" prop="email" :label-width="formLabelWidth">
                            <el-input v-model="userForm.email" placeholder="请输入电子邮件" clearable
                                autocomplete="off"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="手机号" prop="phoneNumber" :label-width="formLabelWidth">
                            <el-input v-model="userForm.phoneNumber" show-word-limit maxlength="11" placeholder="请输入手机号"
                                clearable autocomplete="off"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="性别" prop="sex" :label-width="formLabelWidth">
                            <el-radio-group v-model="userForm.sex">
                                <el-radio :label="'0'">男</el-radio>
                                <el-radio :label="'1'">女</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="用户角色" prop="roleIdList" :label-width="formLabelWidth">
                            <el-checkbox-group v-model="userForm.roleIdList" :min="0" :max="2">
                                <el-checkbox v-for="role in roleList" :label="role.roleId" :key="role.roleId">{{
                                    role.roleDesc
                                }}</el-checkbox>
                            </el-checkbox-group>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="用户状态" prop="status" :label-width="formLabelWidth">
                            <el-switch v-model="userForm.status" :active-value="'1'" :inactive-value="'0'"
                                active-color="#13ce66" inactive-color="#ff4949" active-text="正常" inactive-text="禁用">
                            </el-switch>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-form-item label="头像" prop="avatar" :label-width="formLabelWidth">
                    <el-upload class="avatar-uploader" action="http://localhost:9999/file/ossUpload" ref="upload"
                        :auto-upload="false" drag :on-change="handleChange" :on-success="handleUploadSuccess"
                        :show-file-list="false">
                        <img v-if="userForm.avatar || imageUrl" alt="图片上传失败"
                            :src="imageUrl == '' ? userForm.avatar : imageUrl" class="avatar">
                        <div v-else>
                            <i class="el-icon-upload"></i>
                            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                        </div>
                        <div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过500kb</div>
                    </el-upload>
                </el-form-item>
                <el-form-item align="right">
                    <el-button @click="handleCancel">取消</el-button>
                    <el-button type="primary" @click="handleAddOrUpdate('userForm')">{{ (userForm.id == null || userForm.id
                        == undefined) ? '新增' : '修改'
                    }}</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>

        <!-- 导出表格预览对话框 -->
        <el-dialog title="表格导出预览" top="8vh" width="60%" :visible.sync="excelDialogVisible"
            :close-on-click-modal="closeOnClickModel" :before-close="handleClose">
            <el-table size="small" stripe ref="multipleTable" align="center" :cell-style="rowStyle"
                :header-cell-style="headerCellStyle" :data="selectData" id="selectTable" border tooltip-effect="dark"
                @selection-change="handleSelectionChange" style="width: 100%">
                <el-table-column prop="id" label="ID" width="100">
                </el-table-column>
                <el-table-column prop="nickName" label="昵称" width="140">
                </el-table-column>
                <el-table-column prop="email" label="电子邮件">
                </el-table-column>
                <el-table-column prop="phoneNumber" label="手机号" width="140">
                </el-table-column>
                <el-table-column prop="sex" label="性别" width="100">
                    <template slot-scope="scope">
                        <span v-if="scope.row.sex == 0">男</span>
                        <span v-else>女</span>
                    </template>
                </el-table-column>
                <el-table-column prop="status" label="状态" width="150">
                    <template slot-scope="scope">
                        <el-tag v-if="scope.row.status == 1" type="success">正常</el-tag>
                        <el-tag v-else type="danger">禁用</el-tag>
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
import userApi from '@/api/userManage'
import roleApi from '@/api/roleManage'
import htmlToExcel from '@/utils/htmlToExcel';
export default {
    name: "UserManage",
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
        /**
         * 校验电子邮件
         */
        const checkEmail = (rule, value, callback) => {
            const regExp = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/;
            if (!regExp.test(value)) {
                return callback(new Error('请输入正确的电子邮件'));
            } else {
                callback();
            }
        };
        return {
            roleList: [],
            total: 0,
            title: "新增用户",
            excelDialogVisible: false,
            selectData: [],
            searchModel: {
                nickName: "",
                status: null,
                currentPage: 1,
                pageSize: 5
            },
            userList: [],
            userForm: { sex: '0', roleIdList: [], status: '1' },
            ids: [],
            formLabelWidth: '120px',
            destroyOnClose: true,
            closeOnClickModel: false,
            dialogFormVisible: false,
            rules: {
                username: [
                    { required: true, message: "请输入用户名", trigger: "blur" }
                ],
                nickName: [
                    { required: true, message: "请输入昵称", trigger: "blur" }
                ],
                password: [
                    { required: true, message: "请输入密码", trigger: "blur" }
                ],
                email: [
                    { required: true, message: "请输入电子邮件", trigger: "blur" },
                    { validator: checkEmail, trigger: ['blur', 'change'] }
                ],
                phoneNumber: [
                    { required: true, message: "请输入手机号", trigger: "blur" },
                    {
                        validator: validatePhone,
                        trigger: ['change', 'blur']
                    }
                ],
                roleIdList: [
                    { required: true, message: "请选择所属角色", trigger: "blur" }
                ]
            },
            imageUrl: "",
            file: null
        };
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
            htmlToExcel.getExcel('#selectTable', '用户表');
            this.excelDialogVisible = false;
        },

        // 获取所有角色列表
        getAllRoleList() {
            var that = this;
            roleApi.getAllRoleList().then(res => {
                console.log(res);
                that.roleList = res.data;
            })
        },
        // 清楚对话框中表单的数据
        clearData() {
            // 清理数据
            this.userForm = { sex: '0', roleIdList: [] };
            this.imageUrl = "";
            this.file = null;
        },
        rowStyle() {
            return "text-align:center";
        },
        headerCellStyle() {
            return "text-align: center; background: #e9e2f1;";
        },
        // 图片加载失败的 fallback 行为
        errorHandler() {
            return true
        },
        // 处理每页大小改变
        handleSizeChange(val) {
            this.searchModel.pageSize = val;
            this.getUserList();
        },
        // 处理当前页改变
        handleCurrentChange(val) {
            this.searchModel.currentPage = val;
            this.getUserList();
        },
        // 关闭对话框
        handleClose() {
            this.$confirm("确认关闭？").then(res => {
                this.dialogFormVisible = false; //关闭对话框
                this.excelDialogVisible = false; // 关闭表格对话框
            }).catch(() => { });
        },
        // 取消对话框
        handleCancel() {
            this.dialogFormVisible = false; //关闭对话框
            this.excelDialogVisible = false; // 关闭表格对话框
        },
        // 选择的记录改变
        handleSelectionChange(val) {
            this.selectData = val;
            val.forEach((ele, index) => {
                this.ids[index] = ele.id;
            });
        },

        // 获取用户列表
        getUserList() {
            var that = this;
            userApi.getUserList(that.searchModel).then(res => {
                that.total = res.data.total;
                that.userList = res.data.rows;
            });
        },

        // 文件上传方法
        handleUploadSuccess(res, file) {
            var that = this;
            that.userForm.avatar = res.data;
            userApi.saveUser(that.userForm).then(res => {
                // 显示成功提示信息
                that.$message.success(res.message);
                // 关闭对话框
                that.dialogFormVisible = false;
                // 重新获取列表信息
                that.getUserList();
            })
        },
        handleChange(file) {
            console.log(file);
            const isJPG = file.raw.type === 'image/jpeg';
            const isLt2M = file.raw.size / 1024 / 1024 < 2;

            if (!isJPG) {
                this.$message.error('上传头像图片只能是 JPG 格式!');
            } else if (!isLt2M) {
                this.$message.error('上传头像图片大小不能超过 2MB!');
            } else {
                this.file = file.raw;
                this.imageUrl = URL.createObjectURL(file.raw);
            }
            return isJPG && isLt2M;
        },

        // 处理新增或修改
        handleAddOrUpdate(userForm) {
            var that = this;
            this.$refs[userForm].validate((valid) => {
                if (valid) {
                    if (that.file != null) {
                        that.$refs.upload.submit()
                    } else {
                        userApi.saveUser(that.userForm).then(res => {
                            // 显示成功提示信息
                            that.$message.success(res.message);
                            // 关闭对话框
                            that.dialogFormVisible = false;
                            // 重新获取列表信息
                            that.getUserList();
                        })
                    }
                } else {
                    that.$message.error("请按要求填写相关信息后再进行提交！");
                    return false;
                }
            })
        },

        // 打开对话框
        openDialogUI(id) {
            var that = this;
            if (id == null) { // 新增操作
                that.title = "新增用户";
            } else { // 修改操作
                that.title = "修改用户";
                userApi.getUserById(id).then(res => {
                    that.userForm = res.data;
                })
            }
            // 显示对话框
            this.dialogFormVisible = true;
        },

        // 删除用户
        handleDelete(id) {
            var that = this;
            that.$confirm(`你确定要永久删除编号 “${id}” 的用户吗?`).then(() => {
                userApi.deleteUserById(id).then(res => {
                    // 刷新表格
                    that.getUserList();
                    // 显示成功提示消息
                    that.$message.success(res.message);
                })
            }).catch(() => {
                that.$message.error("已取消删除");
            })
        },

        // 批量删除用户
        deleteUserByIds() {
            var that = this;
            that.$confirm(`你确定要永久删除编号 “${that.ids}” 的用户吗?`).then(() => {
                userApi.deleteUserByIds(that.ids).then(res => {
                    // 刷新表格
                    that.getUserList();
                    // 提示成功消息
                    that.$message.success(res.message);
                })
            }).catch(() => {
                // 提示失败消息
                that.$message.error("已取消删除");
            })
        }
    },
    created() {
        this.getUserList();
        this.getAllRoleList();
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

// .avatar-uploader .el-upload {
//     border: 1px dashed #d9d9d9;
//     border-radius: 6px;
//     cursor: pointer;
//     position: relative;
//     overflow: hidden;
// }

// .avatar-uploader .el-upload:hover {
//     border-color: #409EFF;
// }

// .avatar-uploader-icon {
//     font-size: 28px;
//     color: #8c939d;
//     width: 178px;
//     height: 178px;
//     line-height: 178px;
//     text-align: center;
// }

// .avatar {
//     width: 178px;
//     height: 178px;
//     display: block;
// }
</style>