<template>
    <div id="newsList-container" class="common">
        <!-- 顶部搜索框 -->
        <el-card class="search">
            <el-row>
                <el-col :span="18">
                    <el-input placeholder="标题" v-model="searchModel.title" clearable></el-input>
                    <el-input placeholder="作者" v-model="searchModel.author" clearable></el-input>
                    <el-button type="primary" @click="getNewsList" size="medium" round icon="el-icon-search">查询</el-button>
                </el-col>
                <el-col :span="6" align="right">
                    <el-button-group>
                        <el-button type="success" icon="el-icon-plus" @click="exportExcelSelect">导出</el-button>
                        <el-button type="primary" @click="openDialogUI(null)" icon="el-icon-plus">新增</el-button>
                        <el-button type="danger" @click="deleteNewsByIds" icon="el-icon-delete">批量删除</el-button>
                    </el-button-group>
                </el-col>
            </el-row>
        </el-card>

        <!-- 结果列表 -->
        <el-card>
            <el-table size="small" :default-sort="{ prop: 'createTime', order: 'descending' }" stripe border
                ref="multipleTable" :cell-style="rowStyle" :header-cell-style="headerCellStyle" :data="newsList"
                tooltip-effect="dark" style="width: 100%" @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55">
                </el-table-column>
                <el-table-column label="#" width="80">
                    <template slot-scope="scope">
                        {{
                            (searchModel.currentPage - 1) * searchModel.pageSize + scope.$index + 1
                        }}
                    </template>
                </el-table-column>
                <el-table-column prop="id" label="编号" width="120">
                </el-table-column>
                <el-table-column prop="title" label="标题">
                </el-table-column>
                <el-table-column prop="author" label="作者" width="180">
                </el-table-column>
                <el-table-column prop="createTime" sortable label="创建时间" width="180">
                </el-table-column>
                <el-table-column label="操作" width="220">
                    <template slot-scope="scope">
                        <el-button size="mini" type="success" @click="getNewsDetail(scope.row.id)">详情</el-button>
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
        <el-dialog @close="clearData" :title="title" top="2vh" width="60%" :visible.sync="dialogFormVisible"
            :close-on-click-modal="closeOnClickModel" :destroy-on-close="destroyOnClose" :before-close="handleClose">
            <el-form ref="newsForm" :rules="rules" :model="newsForm">
                <el-form-item label="标题" prop="title" :label-width="formLabelWidth">
                    <el-input v-model="newsForm.title" placeholder="请输入新闻标题..." clearable autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="关键字" prop="keyword" :label-width="formLabelWidth">
                    <el-input v-model="newsForm.keyword" placeholder="请输入新闻关键字..." clearable autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="作者" prop="author" :label-width="formLabelWidth">
                    <el-input v-model="newsForm.author" placeholder="请输入新闻作者..." clearable autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="内容" prop="content" :label-width="formLabelWidth">
                    <TinyEditor class="setTinymce" v-model="newsForm.content"></TinyEditor>
                </el-form-item>
                <el-form-item align="right">
                    <el-button @click="handleCancel">取消</el-button>
                    <el-button type="primary" @click="handleAddAndUpdate('newsForm')">{{ !newsForm.id ? '新增' : '修改'
                    }}</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>

        <!-- 新闻详情对话框 -->
        <el-dialog title="新闻详情" center top="5vh" :visible.sync="newsDetailVisible" width="70%"
            :before-close="handleNewsClose">
            <h1 align="center">{{ newsDetailData.title }}</h1>
            <span style="color: gray; font-size: 13px;">{{ newsDetailData.createTime + " " + "来源：" +
                newsDetailData.author }}</span>
            <div v-html="newsDetailData.content"></div>
        </el-dialog>

        <!-- 导出表格预览对话框 -->
        <el-dialog title="表格导出预览" top="8vh" width="70%" :visible.sync="excelDialogVisible"
            :close-on-click-modal="closeOnClickModel" :before-close="handleClose">
            <el-table size="small" stripe ref="multipleTable" align="center" :cell-style="rowStyle"
                :header-cell-style="headerCellStyle" :data="selectData" id="selectTable" border tooltip-effect="dark"
                @selection-change="handleSelectionChange" style="width: 100%">
                <el-table-column prop="id" label="编号" width="120">
                </el-table-column>
                <el-table-column prop="title" label="标题">
                </el-table-column>
                <el-table-column prop="author" label="作者" width="180">
                </el-table-column>
                <el-table-column prop="createTime" sortable label="创建时间" width="180">
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
import newsListApi from "@/api/newsListManage"
import TinyEditor from "@/components/TinyEditor.vue"
import htmlToExcel from '@/utils/htmlToExcel';
export default {
    name: 'NewsList',
    components: {
        TinyEditor
    },
    mounted() {
        this.getNewsList();
    },
    data() {
        return {
            newsDetailData: {},
            total: 0,
            excelDialogVisible: false,
            selectData: [],
            title: '',
            searchModel: {
                title: "",
                author: "",
                currentPage: 1,
                pageSize: 5
            },
            newsList: [],
            newsForm: {},
            dialogFormVisible: false,
            closeOnClickModel: false,
            destroyOnClose: true,
            formLabelWidth: '100px',
            newsDetailVisible: false,
            ids: [],
            rules: {
                title: [
                    { required: true, message: "请输入新闻标题", trigger: "blur" }
                ],
                keyword: [
                    { required: true, message: "请输入新闻关键字", trigger: "blur" }
                ],
                author: [
                    { required: true, message: "请输入新闻作者", trigger: "blur" }
                ],
                content: [
                    { required: true, message: '请输入新闻内容', trigger: 'blur' }
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
            htmlToExcel.getExcel('#selectTable', '新闻表');
            this.excelDialogVisible = false;
        },

        handleNewsClose() {
            this.newsDetailVisible = false;
        },
        // 获取新闻列表
        getNewsList() {
            var that = this;
            newsListApi.getNewsList(that.searchModel).then(res => {
                that.newsList = res.data.rows;
                that.total = res.data.total;
            })
        },

        // 清除对话框中表单的数据
        clearData() {
            this.carModelData = {}; //清除数据
            this.imageUrl = ""; // 清除图像的src
        },

        // 设置表格样式
        rowStyle() {
            return "text-align:center";
        },
        headerCellStyle() {
            return "text-align: center; background: #e9e2f1;";
        },

        // 选择的记录改变
        handleSelectionChange(val) {
            this.selectData = val;
            val.forEach((ele, index) => {
                this.ids[index] = ele.id;
            });
        },

        // 打开对话框
        openDialogUI(id) {
            var that = this;
            if (id == null) { // 新增操作
                that.title = "新增新闻";
            } else { // 修改操作
                that.title = "修改新闻";
                newsListApi.getNewsById(id).then(res => {
                    that.newsForm = res.data;
                })
            }
            // 显示对话框
            this.dialogFormVisible = true;
        },

        // 删除选中的多个新闻
        deleteNewsByIds() {
            var that = this;
            that.$confirm("你确定要删除编号 “" + that.ids + "” 的新闻信息吗").then(() => {
                newsListApi.deleteNewsByIds(that.ids).then(res => {
                    // 重新获取所有新闻列表进行展示
                    that.getNewsList();
                    // 提示删除成功信息
                    that.$message.success(res.message);
                })
            }).catch(() => {
                // 提示删除失败信息
                that.$message.error("已取消删除");
            })
        },
        // 处理每页大小改变
        handleSizeChange(val) {
            this.searchModel.pageSize = val;
            this.getNewsList();
        },

        // 处理当前页改变
        handleCurrentChange(val) {
            this.searchModel.currentPage = val;
            this.getNewsList()
        },
        // 关闭对话框
        handleClose() {
            this.$confirm('确认关闭？').then(res => {
                this.dialogFormVisible = false;//关闭对话框
                this.excelDialogVisible = false; //关闭表格对话框
            }).catch(() => { })
        },

        // 取消对话框
        handleCancel() {
            this.dialogFormVisible = false; //关闭对话框
            this.excelDialogVisible = false; //关闭表格对话框
        },

        // 根据id删除指定新闻
        handleDelete(id) {
            var that = this;
            that.$confirm("你确定要删除编号 “" + id + "” 的新闻信息吗?").then(() => {
                // 调用删除指定新闻的api进行删除
                newsListApi.deleteNewsById(id).then(res => {
                    // 调用查询新闻列表，重新加载。
                    that.getNewsList();
                    // 显示成功消息提示
                    that.$message.success(res.message);
                });
            }).catch(() => {
                that.$message.error("删除失败！！！");
            })
        },

        // 处理新增或修改操作
        handleAddAndUpdate(newsForm) {
            var that = this;
            this.$refs[newsForm].validate((valid) => {
                if (valid) {
                    newsListApi.saveNews(that.newsForm).then(res => {
                        // 清空对话框中的数据
                        that.newsForm = {};
                        // 关闭对话框
                        that.dialogFormVisible = false;
                        // 重新获取列表信息
                        that.getNewsList();
                        // 显示成功消息提示
                        that.$message.success(res.message);
                    })
                } else {
                    that.$message.error("请按要求填写相关信息后再进行提交！");
                    return false;
                }
            })
        },
        getNewsDetail(id) {
            var that = this;
            newsListApi.getNewsById(id).then(res => {
                that.newsDetailVisible = true;
                that.newsDetailData = res.data;
            })
        }
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