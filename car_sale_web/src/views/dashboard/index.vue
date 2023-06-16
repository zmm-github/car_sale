<template>
    <div class="dashboard-container">
        <el-card class="box-card common">
            <el-descriptions title="用户信息">
                <el-descriptions-item label="用户名">{{ loginUser.username }}</el-descriptions-item>
                <el-descriptions-item label="手机号">{{ loginUser.phoneNumber }}</el-descriptions-item>
                <el-descriptions-item label="性别">
                    <el-tag v-if="loginUser.sex == 0" size="small">男</el-tag>
                    <el-tag v-else size="small">女</el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="用户状态">
                    <el-tag size="small" v-if="loginUser.status == 1">正常</el-tag>
                    <el-tag v-else type="danger" size="small">禁用</el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="电子邮件">{{ loginUser.email }}</el-descriptions-item>
            </el-descriptions>
        </el-card>
        <el-card class="box-card">
            <h3>最新资讯<i class="header-icon el-icon-info"></i></h3>
            <el-collapse accordion>
                <el-collapse-item v-for="(news, index) in newsList">
                    <template slot="title">
                        <p>{{ news.title }} <span>{{ news.createTime + " 来源：" + news.author }}</span></p>
                    </template>
                    <div v-html="news.content"></div>
                </el-collapse-item>
            </el-collapse>
        </el-card>
    </div>
</template>
  
<script>
import newsListApi from "@/api/newsListManage"
import store from "@/store";
export default {
    name: 'Index',
    data() {
        return {
            newsList: [],
            loginUser: store.getters.loginUser
        }
    },
    mounted() {
        var that = this;
        newsListApi.getAllNews().then(res => {
            that.newsList = res.data;
        })
    }
}
</script>
  
<style lang="scss" scoped>
.box-card {
    h3 {
        margin-top: 0;
    }

    p {
        padding-right: 5px;
        width: 100%;
        display: flex;
        justify-content: space-between;
        margin: 0;

        span {
            color: gray;
        }
    }
}
</style>
  