<template>
    <div id="roleManage-container" class="common">
        <!-- 顶部搜索框 -->
        <el-card class="search">
            <el-row>
                <el-col :span="18">
                    <el-select v-model="searchModel.roleName" clearable placeholder="请选择角色名称">
                        <el-option label="超级管理员" value="admin"></el-option>
                        <el-option label="普通用户" value="normal"></el-option>
                    </el-select>
                    <el-button type="primary" round @click="getRoleList">查找</el-button>
                </el-col>
                <el-col :span="6" align="right">
                    <el-button-group>
                        <el-button type="success" icon="el-icon-plus" @click="exportExcelSelect">导出</el-button>
                        <el-button type="primary" icon="el-icon-plus" @click="openDialogUI(null)">新增</el-button>
                        <el-button type="danger" icon="el-icon-delete" @click="deleteRoleByIds">批量删除</el-button>
                    </el-button-group>
                </el-col>
            </el-row>
        </el-card>

        <!-- 结果列表 -->
        <el-card>
            <el-table size="small" stripe ref="multipleTable" :cell-style="rowStyle" :header-cell-style="headerCellStyle"
                :data="roleList" border tooltip-effect="dark" @selection-change="handleSelectionChange" style="width: 100%">
                <el-table-column type="selection" width="55">
                </el-table-column>
                <el-table-column label="#" width="100">
                    <template slot-scope="scope">
                        {{
                            (searchModel.currentPage - 1) * searchModel.pageSize + scope.$index + 1
                        }}
                    </template>
                </el-table-column>
                <el-table-column prop="roleId" label="ID" width="200">
                </el-table-column>
                <el-table-column prop="roleName" label="角色名称" width="260">
                </el-table-column>
                <el-table-column prop="roleDesc" label="角色描述">
                </el-table-column>
                <el-table-column label="操作" width="180">
                    <template slot-scope="scope">
                        <el-button size="mini" type="success" @click="openDialogUI(scope.row.roleId)">编辑</el-button>
                        <el-button size="mini" type="danger" @click="deleteRole(scope.row.roleId)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-card>

        <!-- 分页组件 -->
        <el-pagination v-if="total > 0" @size-change="handleSizeChange" align="right" @current-change="handleCurrentChange"
            :current-page="searchModel.currentPage" :page-sizes="[5, 10, 15, 20]" :page-size="searchModel.pageSize"
            layout="total, sizes, prev, pager, next, jumper" :total="total">
        </el-pagination>

        <!-- 角色信息编辑对话框 -->
        <el-dialog @close="clearData" :title="title" top="2vh" :visible.sync="dialogFormVisible"
            :close-on-click-modal="closeOnClickModel" :destroy-on-close="destroyOnClose" :before-close="handleClose">
            <el-form ref="roleFormRef" :rules="rules" :model="roleForm">
                <el-form-item label="角色名称" prop="roleName" :label-width="formLabelWidth">
                    <el-input v-model="roleForm.roleName" placeholder="请输入角色名称" clearable autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="角色描述" prop="roleDesc" :label-width="formLabelWidth">
                    <el-input v-model="roleForm.roleDesc" placeholder="请输入角色描述" clearable autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="权限设置" prop="menuIdList" :label-width="formLabelWidth">
                    <el-tree :data="menuList" node-key="menuId" ref="menuRef" default-expand-all :props="menuProps"
                        show-checkbox></el-tree>
                </el-form-item>
                <el-form-item align="right">
                    <el-button @click="handleCancel">取消</el-button>
                    <el-button type="primary" @click="handleAddOrUpdate('roleFormRef')">{{ (roleForm.roleId == null ||
                        roleForm.roleId
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
                <el-table-column prop="roleId" label="ID" width="200">
                </el-table-column>
                <el-table-column prop="roleName" label="角色名称" width="260">
                </el-table-column>
                <el-table-column prop="roleDesc" label="角色描述">
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
import roleApi from '@/api/roleManage'
import menuApi from '@/api/menuManage'
import htmlToExcel from '@/utils/htmlToExcel';
export default {
    name: "RoleManage",
    data() {
        return {
            menuList: [],
            menuProps: {
                children: 'children',
                label: 'title'
            },
            total: 0,
            title: "新增角色",
            excelDialogVisible: false,
            selectData: [],
            searchModel: {
                roleName: "",
                currentPage: 1,
                pageSize: 5
            },
            roleList: [],
            roleForm: {},
            ids: [],
            formLabelWidth: '150px',
            destroyOnClose: true,
            closeOnClickModel: false,
            dialogFormVisible: false,
            rules: {
                roleName: [
                    { required: true, message: "请输入角色名称", trigger: "blur" },
                    {
                        min: 2,
                        max: 20,
                        message: "长度在 2 到 20 个字符",
                        trigger: "blur"
                    }
                ],
                roleDesc: [
                    { required: true, message: "请输入角色描述", trigger: "blur" },
                    {
                        min: 2,
                        max: 20,
                        message: "长度在 2 到 20 个字符",
                        trigger: "blur"
                    }
                ]
            },
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
            htmlToExcel.getExcel('#selectTable', '角色表');
            this.excelDialogVisible = false;
        },

        // 获取所有菜单列表
        getMenuList() {
            var that = this;
            menuApi.getMenuList().then(res => {
                that.menuList = res.data;
            })
        },

        // 关闭对话框时清除对话框中的数据
        clearData() {
            // 清理数据
            this.roleForm = {};
            this.$refs.menuRef.setCheckedKeys([]);
        },

        // 设置表格样式
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
            this.getRoleList();
        },
        // 处理当前页改变
        handleCurrentChange(val) {
            this.searchModel.currentPage = val;
            this.getRoleList();
        },
        // 关闭对话框
        handleClose() {
            this.$confirm("确认关闭？").then(res => {
                this.dialogFormVisible = false; //关闭对话框
                this.excelDialogVisible = false; //关闭表格对话框
            }).catch(() => { });
        },
        // 取消对话框
        handleCancel() {
            this.dialogFormVisible = false; //关闭对话框
            this.excelDialogVisible = false; //关闭表格对话框
        },
        // 选择的记录改变
        handleSelectionChange(val) {
            this.selectData = val;
            val.forEach((ele, index) => {
                this.ids[index] = ele.roleId;
            });
        },

        // 获取角色列表
        getRoleList() {
            var that = this;
            roleApi.getRoleList(that.searchModel).then(res => {
                that.total = res.data.total;
                that.roleList = res.data.rows;
            });
        },

        // 处理新增或修改
        handleAddOrUpdate(roleFormRef) {
            var that = this;
            that.$refs[roleFormRef].validate((valid) => {
                if (valid) {
                    var checkedKeys = that.$refs.menuRef.getCheckedKeys();
                    var halfCheckedKeys = that.$refs.menuRef.getHalfCheckedKeys();
                    that.roleForm.menuIdList = checkedKeys.concat(halfCheckedKeys);
                    roleApi.saveRole(that.roleForm).then(res => {
                        // 显示成功提示信息
                        that.$message.success(res.message);
                        // 关闭对话框
                        that.dialogFormVisible = false;
                        // 重新获取列表信息
                        that.getRoleList();
                    })
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
                that.title = "新增角色";
            } else { // 修改操作
                that.title = "修改角色";
                roleApi.getRoleById(id).then(res => {
                    that.roleForm = res.data;
                    that.$refs.menuRef.setCheckedKeys(res.data.menuIdList);
                })
            }
            // 显示对话框
            this.dialogFormVisible = true;
        },

        // 删除角色
        deleteRole(id) {
            var that = this;
            that.$confirm(`你确定要永久删除编号 “${id}” 的角色吗?`).then(() => {
                roleApi.deleteRoleById(id).then(res => {
                    // 刷新表格
                    that.getRoleList();
                    // 显示成功提示消息
                    that.$message.success(res.message);
                })
            }).catch(() => {
                that.$message.error("已取消删除");
            })
        },

        // 批量删除角色
        deleteRoleByIds() {
            var that = this;
            that.$confirm(`你确定要永久删除编号 “${that.ids}” 的角色吗?`).then(() => {
                roleApi.deleteRoleByIds(that.ids).then(res => {
                    // 刷新表格
                    that.getRoleList();
                    // 提示成功消息
                    that.$message.success(res.message);
                })
            }).catch(() => {
                // 提示失败消息
                that.$message.error("已取消删除");
            })
        }
    },
    mounted() {
        this.getRoleList();
        this.getMenuList();
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
}

.avatar-uploader .el-upload:hover {
    border-color: #409EFF;
}

.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
}

.avatar {
    width: 178px;
    height: 178px;
    display: block;
}
</style>