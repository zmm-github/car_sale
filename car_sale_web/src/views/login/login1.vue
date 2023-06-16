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
                <el-input type="text" v-model="loginForm.username" placeholder="请输入用户名"></el-input>
              </el-form-item>
              <el-form-item prop="password">
                <el-input type="password" v-model="loginForm.password" placeholder="请输入密码"></el-input>
              </el-form-item>
              <el-button class="login_btn" @click="login" :disabled="login_disabled" >登录</el-button>
            </el-form>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import Axios from 'axios';


export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      },
      login_disabled: false,
      labelPosition: 'left',
      rules: {
        username: [{
          required: true,
          message: '请输入用户名',
          trigger: 'blur'
        }],
        password: [{
          required: true,
          message: '请输入密码',
          trigger: 'blur'
        }]
      }
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
    login() {
      this.login_disabled = true;
      this.$refs.loginFormRef.validate((valid) => {
        if (valid) {
          // 去后台验证用户名密码
          Axios.post('http://localhost:9999/user/login', this.loginForm).then(res => res.data).then(res => {
            console.log(res);
            if(res.code == 20000) {
              // 存储
              sessionStorage.setItem("loginUser", JSON.stringify(res.data));
              // 跳转到主页
              // this.$router.replace('/');
              this.$router.push({ path: '/' })
            } else {
              this.login_disabled = false;
              this.$message.error("校验失败，用户名或密码错误！");
              return false;
            }
          })
        } else {
          this.login_disabled = false;
          this.$message.error("校验失败！");
          return false
        }
      });
    },
    toRegister() {
      this.$router.push({ path: '/register' })
    }
  }
  //   login: function () {
  //     this.$refs.loginFormRef.validate((valid) => {
  //       if (valid) {
  //         this.loading = true
  //         this.$store.dispatch('user/login', this.loginForm).then(() => {
  //           this.$router.push({ path: this.redirect || '/' })
  //           this.loading = false
  //         }).catch(() => {
  //           this.loading = false
  //         })
  //       } else {
  //         console.log('error submit!!')
  //         return false
  //       }
  //     });
  //   },
  //   toRegister() {
  //     this.$router.push({ path: '/register' })
  //   }
  // }
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