<template>
  <div id="loginContainer">
    <el-container>
      <el-header>
        <h1>汽车销售管理系统</h1>
      </el-header>
      <el-main>
        <el-row type="flex" justify="center" :gutter="20">
          <el-col :md="12" :sm="16" :xs="22">
            <el-form ref="loginFormRef" class="login_form" status-icon :rules="rules" :label-position="labelPosition"
              :model="loginForm">
              <div class="login_title">
                <p>系统登录</p>
                <el-button @click="toRegister">点我注册</el-button>
              </div>
              <el-form-item prop="username">
                <span class="svg-container">
                  <svg-icon icon-class="user" />
                </span>
                <el-input type="text" v-model="loginForm.username" placeholder="请输入用户名"></el-input>
              </el-form-item>
              <el-form-item prop="password">
                <el-input type="password" v-model="loginForm.password" placeholder="请输入密码"></el-input>
              </el-form-item>
              <el-button class="login_btn" @click="login" :disabled="login_disabled">登录</el-button>
            </el-form>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import { validUsername } from '@/utils/validate'

export default {
  name: 'Login',
  data() {
    const validateUsername = (rule, value, callback) => {
      if (!validUsername(value)) {
        callback(new Error('Please enter the correct user name'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('The password can not be less than 6 digits'))
      } else {
        callback()
      }
    }
    return {
      loginForm: {
        username: 'admin',
        password: '111111'
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', validator: validateUsername }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }]
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
    }
  }
}
</script>

<style lang="scss">
body {
  margin: 0;
  padding: 0;
}

#loginContainer {
  .el-header {
    display: flex;
    width: 100%;
    height: 200px !important;
    background-color: darkcyan;
    justify-content: center;
    align-items: center;

    h1 {
      margin: 0;
      color: white;
      letter-spacing: 2px;
      font-size: 50px;
      text-shadow: 2px 2px 6px lightseagreen;
    }
  }

  .login_form {
    padding: 30px 40px 50px 40px;
    margin-top: 60px;
    border: 1px solid gainsboro;
    box-shadow: 0px 0px 4px 2px gainsboro;

    .login_title {
      display: flex;
      justify-content: center;
      padding: 10px 0;
      align-items: center;

      p {
        margin-right: 10px;
        font-size: 20px;
      }
    }

    .login_btn {
      background-color: darkcyan;
      color: white;
      width: 100%;
      font-size: 18px;
      letter-spacing: 2px;
    }

    .login_btn:hover {
      opacity: 0.8;
    }
  }
}
</style>