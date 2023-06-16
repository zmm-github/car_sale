<template>
  <div class="login-container">
    <!-- 登录表单 -->
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on"
      label-position="left">

      <div class="title-container">
        <h3 class="title">欢迎来到汽车销售管理系统</h3>
      </div>

      <el-form-item prop="username">
        <span class="svg-container">
          <svg-icon icon-class="user" />
        </span>
        <el-input ref="username" v-model="loginForm.username" placeholder="用户名" name="username" type="text" tabindex="1"
          auto-complete="on" />
      </el-form-item>

      <el-form-item prop="password">
        <span class="svg-container">
          <svg-icon icon-class="password" />
        </span>
        <el-input :key="passwordType" ref="password" v-model="loginForm.password" :type="passwordType" placeholder="密码"
          name="password" tabindex="2" auto-complete="on" @keyup.enter.native="handleLogin" />
        <span class="show-pwd" @click="showPwd">
          <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
        </span>
      </el-form-item>
      <div class="register">
        <a href="#" @click.prevent="dialogFormVisible = true">点我注册</a>
      </div>

      <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;"
        @click.native="handleLogin">登 录</el-button>
    </el-form>

    <!-- 注册对话框 -->
    <el-dialog @close="clearDialogData" title="用户注册" :close-on-click-modal="closeOnClickModel"
      :destroy-on-close="destroyOnClose" :before-close="handleClose" :visible.sync="dialogFormVisible">
      <el-form ref='registerFormRef' style="margin-right: 40px;" :rules="registerRules" :model="registerForm">
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户名" prop="username" :label-width="formLabelWidth">
              <el-input type="text" v-model="registerForm.username" placeholder="用户名" autocomplete="off"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="昵称" prop="nickName" :label-width="formLabelWidth">
              <el-input v-model="registerForm.nickName" placeholder="请输入昵称" clearable autocomplete="off"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="密 码" prop="password" :label-width="formLabelWidth">
              <el-input type="password" v-model="registerForm.password" placeholder="密码" autocomplete="off"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="确认密码" prop="checkPass" :label-width="formLabelWidth">
              <el-input type="password" v-model="registerForm.checkPass" placeholder="确认密码" autocomplete="off"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="电子邮件" prop="email" :label-width="formLabelWidth">
              <el-input v-model="registerForm.email" placeholder="请输入电子邮件" clearable autocomplete="off"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号" prop="phoneNumber" :label-width="formLabelWidth">
              <el-input v-model="registerForm.phoneNumber" show-word-limit maxlength="11" placeholder="请输入手机号" clearable
                autocomplete="off"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="性别" prop="sex" :label-width="formLabelWidth">
          <el-radio-group v-model="registerForm.sex">
            <el-radio :label="'0'">男</el-radio>
            <el-radio :label="'1'">女</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleRegister">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import userApi from '@/api/user'
export default {
  name: 'Login',
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
    const validatePass = (rule, value, callback) => {
      if (value === '' || value === undefined) {
        callback(new Error('请输入密码'));
      } else {
        if (this.registerForm.checkPass !== '') {
          this.$refs.registerFormRef.validateField('checkPass');
        }
        callback();
      }
    };
    const validateCheckPass = (rule, value, callback) => {
      if (value === '' || value === undefined) {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.registerForm.password) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    const validateUsername = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入用户名'))
      } else {
        callback()
      }
    };
    const validatePassword = (rule, value, callback) => {
      if (value.length < 3) {
        callback(new Error('密码的长度不能小于3位'))
      } else {
        callback()
      }
    };
    /**
     * 检查用户名是否已在数据库中存在
     */
    const checkUsername = (rule, value, callback) => {
      userApi.getUserByUsername(value).then(res => {
        if (res.data != null) {
          callback(new Error('用户名已存在！'));
        } else {
          callback();
        }
      })
    };
    return {
      dialogFormVisible: false,
      formLabelWidth: '120px',
      destroyOnClose: true,
      closeOnClickModel: false,
      loginForm: {
        username: '',
        password: ''
      },
      registerForm: {
        sex: '0'
      },
      loginRules: {
        username: [{ required: true, gger: 'blur', validator: validateUsername }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }]
      },
      registerRules: {
        username: [
          { required: true, trigger: 'blur', message: "请输入用户名" },
          { required: true, validator: checkUsername, trigger: 'blur' },
        ],
        nickName: [
          { required: true, message: "请输入昵称", trigger: "blur" }
        ],
        password: [
          { required: true, validator: validatePass, trigger: 'blur' },
          { min: 3, message: "密码的长度不能小于3位", trigger: 'blur' }
        ],
        checkPass: [
          { required: true, validator: validateCheckPass, trigger: 'blur' }
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
        sex: [{ required: true, message: "请选择性别", trigger: 'blur' }]
      },
      loading: false,
      passwordType: 'password',
      redirect: undefined
    }
  },
  watch: {
    $route: {
      handler: function (route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  methods: {
    clearDialogData() {
      this.registerForm = { sex: '0' }
    },
    handleClose(done) {
      this.$confirm('确认关闭？')
        .then(_ => {
          done();
        })
        .catch(_ => { });
    },
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.$store.dispatch('user/login', this.loginForm).then(() => {
            this.$router.push({ path: this.redirect || '/dashboard' })
            this.loading = false
          }).catch(() => {
            this.loading = false
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },

    handleRegister() {
      this.$refs.registerFormRef.validate(valid => {
        if (valid) {
          userApi.register(this.registerForm).then(res => {
            // 对返回的结果进行判断，如果res.data为空，说明注册失败，注册用户已存在
            if (res.data == null) {
              // 弹出失败消息提示
              this.$message({
                message: res.message,
                type: 'fail'
              });
            } else {
              // 关闭对话框
              this.dialogFormVisible = false;
              // 弹出成功消息提示
              this.$message({
                message: res.message,
                type: 'success'
              });
            }
          })
        } else {
          this.$message.error("请按要求填写相关信息后再进行提交！");
          return false
        }
      })
    }
  }
}
</script>

<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg: #283443;
$light_gray: #fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.login-container {
  .login-form {
    .el-input {
      display: inline-block;
      height: 47px;
      width: 85%;

      input {
        background: transparent;
        border: 0px;
        -webkit-appearance: none;
        border-radius: 0px;
        padding: 12px 5px 12px 15px;
        color: $light_gray;
        height: 47px;
        caret-color: $cursor;

        &:-webkit-autofill {
          box-shadow: 0 0 0px 1000px $bg inset !important;
          -webkit-text-fill-color: $cursor !important;
        }
      }
    }

    .el-form-item {
      border: 1px solid rgba(255, 255, 255, 0.1);
      background: rgba(0, 0, 0, 0.1);
      border-radius: 5px;
      color: #454545;
    }

  }


  .el-dialog {
    input {
      color: gray;
    }
  }
}
</style>

<style lang="scss" scoped>
$bg: #2d3a4b;
$dark_gray: #889aa4;
$light_gray: #eee;

.login-container {
  min-height: 100%;
  width: 100%;
  background-color: $bg;
  overflow: hidden;

  .login-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 160px 35px 0;
    margin: 0 auto;
    overflow: hidden;
  }

  .register {
    font-size: 14px;
    color: #ccc;
    margin-bottom: 15px;
    text-align: right;
    margin-right: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: $light_gray;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
      letter-spacing: 1px;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }
}
</style>
