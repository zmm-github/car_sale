<template>
  <div class="navbar">
    <hamburger :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />

    <breadcrumb class="breadcrumb-container" />

    <div class="right-menu">
      <el-dropdown class="avatar-container" trigger="click">
        <div class="avatar-wrapper">
          <img :src="loginUser.avatar" class="user-avatar" alt="图片显示失败">
          <span class="user-info">{{ loginUser.nickName }}<i class="el-icon-caret-bottom" /></span>
        </div>
        <el-dropdown-menu slot="dropdown" class="user-dropdown">
          <router-link to="/">
            <el-dropdown-item>
              主页
            </el-dropdown-item>
          </router-link>
          <el-dropdown-item divided @click.native="avatarDialogVisible = true">
            <span style="display:block;">修改头像</span>
          </el-dropdown-item>
          <el-dropdown-item divided @click.native="passDialogVisible = true">
            <span style="display:block;">修改密码</span>
          </el-dropdown-item>
          <el-dropdown-item divided @click.native="logout">
            <span style="display:block;">退出</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>

    <!-- 修改头像对话框 -->
    <el-dialog :append-to-body="true" @close="clearAvatarData" title="修改头像" :destroy-on-close="true"
      :close-on-click-modal="closeOnClickModel" :visible.sync="avatarDialogVisible" width="35%"
      :before-close="handleClose">
      <el-form :model="loginUser" status-icon :rules="rules" label-width="100px" class="demo-ruleForm">
        <el-form-item label="头 像" prop="avatar" :label-width="formLabelWidth">
          <el-upload class="avatar-uploader" action="http://localhost:9999/file/ossUpload" ref="upload"
            :auto-upload="false" drag :on-change="handleChange" :on-success="handleUploadSuccess" :show-file-list="false">
            <img v-if="loginUser.avatar || imageUrl" alt="图片上传失败" :src="imageUrl == '' ? loginUser.avatar : imageUrl"
              class="avatar">
            <div v-else>
              <i class="el-icon-upload"></i>
              <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            </div>
            <div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过500kb</div>
          </el-upload>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="avatarDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateUserAvatarById('loginUserRef')">修 改</el-button>
      </span>
    </el-dialog>

    <!-- 修改密码对话框 -->
    <el-dialog :append-to-body="true" @close="clearPassData" title="修改密码" :destroy-on-close="true"
      :close-on-click-modal="closeOnClickModel" :visible.sync="passDialogVisible" width="35%" :before-close="handleClose">
      <el-form :model="loginUser" style="margin-right: 20px;" status-icon :rules="rules" ref="loginUserRef"
        label-width="100px" class="demo-ruleForm">
        <el-form-item label="密码" prop="password" :label-width="formLabelWidth">
          <el-input placeholder="请输入密码" v-model="loginUser.password" show-password></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="checkPass" :label-width="formLabelWidth">
          <el-input placeholder="请输入密码" v-model="loginUser.checkPass" show-password></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="passDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateUserPassById('loginUserRef')">修 改</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'
import store from '@/store';
import userApi from '@/api/user'

export default {
  data() {
    const validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else {
        if (this.loginUser.checkPass !== '') {
          this.$refs.loginUserRef.validateField('checkPass');
        }
        callback();
      }
    };
    const validateCheckPass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.loginUser.password) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      avatarDialogVisible: false,
      passDialogVisible: false,
      formLabelWidth: "100px",
      imageUrl: "",
      file: null,
      closeOnClickModel: false,
      loginUser: store.getters.loginUser,
      rules: {
        password: [
          { required: true, validator: validatePass, trigger: 'blur' },
          { min: 3, message: "密码的长度不能小于3位", trigger: 'blur' }
        ],
        checkPass: [
          { required: true, validator: validateCheckPass, trigger: 'blur' }
        ]
      }
    };
  },
  components: {
    Breadcrumb,
    Hamburger
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar'
    ])
  },
  methods: {
    // 清除相关数据
    clearAvatarData() {
      this.file = null;
    },
    clearPassData() {
      this.loginUser.password = "";
      this.loginUser.checkPass = "";
    },

    // 文件上传方法
    handleUploadSuccess(res, file) {
      var that = this;
      that.loginUser.avatar = res.data;
      userApi.updateUserAvatarById(that.loginUser).then(res => {
        // 显示成功提示信息
        that.$message.success(res.message);
        // 关闭对话框
        that.avatarDialogVisible = false;
        // 退出重新登录
        that.logout();
      })
    },
    handleChange(file) {
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
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async logout() {
      await this.$store.dispatch('user/logout')
      // 注销时删除所有的tagsView
      await this.$store.dispatch('tagsView/delAllViews')
      sessionStorage.removeItem('tabViews')
      this.$router.push(`/login`)
    },
    handleClose(done) {
      this.$confirm('确认关闭？')
        .then(_ => {
          done();
        })
        .catch(_ => { });
    },

    // 根据用户ID修改用户头像
    updateUserAvatarById() {
      if (this.file != null) {
        this.$refs.upload.submit();
      } else {
        // 显示成功提示信息
        this.$message.success("修改头像成功，请重新登录！");
        // 关闭对话框
        this.avatarDialogVisible = false;
        // 退出重新登录
        this.logout();
      }
    },

    // 根据用户ID修改用户密码
    updateUserPassById(loginUserRef) {
      var that = this;
      this.$refs[loginUserRef].validate((valid) => {
        if (valid) {
          userApi.updateUserPassById(that.loginUser).then(res => {
            // 显示成功提示信息
            that.$message.success(res.message);
            // 关闭对话框
            that.passDialogVisible = false;
            // 退出重新登录
            this.logout();
          })
        } else {
          that.$message.error("请按要求填写相关信息后再进行提交！");
          return false;
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, .08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        // margin-top: 5px;
        position: relative;
        display: flex;
        align-items: center;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
          margin-right: 5px;
        }

        .user-info {
          cursor: pointer;
        }

        .user-info:hover {
          color: cornflowerblue;
        }
      }
    }
  }
}
</style>
