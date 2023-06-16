<template>
    <div id="register-controller" class="common">
        <el-container>
            <el-header>
                <h1>汽车销售管理系统</h1>
            </el-header>
            <el-main>
                <el-row type="flex" justify="center" :gutter="20">
                    <el-col :md="12" :sm="16" :xs="22">
                        <el-form ref="registerData" class="register_form" status-icon :rules="rules"
                            :label-position="labelPosition" :model="registerData">
                            <div class="register_title">
                                <p>系统注册</p>
                                <el-button @click="toLogin">去登录</el-button>
                            </div>
                            <el-form-item prop="username">
                                <el-input type="text" v-model="registerData.username" placeholder="请输入用户名"
                                    autocomplete="off"></el-input>
                            </el-form-item>
                            <el-form-item prop="password">
                                <el-input type="password" v-model="registerData.password" placeholder="请输入密码"
                                    autocomplete="off"></el-input>
                            </el-form-item>
                            <el-form-item prop="checkPassword">
                                <el-input type="password" v-model="registerData.checkPassword" placeholder="请再次输入密码"
                                    autocomplete="off"></el-input>
                            </el-form-item>
                            <el-button type="primary" class="register_btn" @click="register('registerData')">注册</el-button>
                            <el-button @click="resetForm('registerData')">重置</el-button>
                        </el-form>
                    </el-col>
                </el-row>
            </el-main>
        </el-container>
    </div>
</template>
  
<script>


export default {
    name: 'register',
    data() { // 数据
        var validatePassword = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请输入密码'));
            } else {
                if (this.registerData.checkPassword !== '') {
                    this.$refs.registerData.validateField('checkPassword');
                }
                callback();
            }
        };
        var validateCheckPassword = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请再次输入密码'));
            } else if (value !== this.registerData.password) {
                callback(new Error('两次输入密码不一致!'));
            } else {
                callback();
            }
        };
        return {
            registerData: {
                username: '',
                password: '',
                checkPassword: ''
            },
            labelPosition: 'left',
            rules: {
                username: [
                    { required: true, message: '请输入用户名', trigger: 'blur' },
                    { min: 3, max: 16, message: '长度在 3 到 16 个字符', trigger: 'blur' }
                ],
                password: [
                    { validator: validatePassword, trigger: 'blur' },
                    { min: 3, max: 16, message: '长度在 3 到 16 个字符', trigger: 'blur' }
                ],
                checkPassword: [
                    { validator: validateCheckPassword, trigger: 'blur' },
                    { min: 3, max: 16, message: '长度在 3 到 16 个字符', trigger: 'blur' }
                ]
            }
        };
    },
    methods: {
        register(registerData) {
            this.$refs[registerData].validate((valid) => {
                if (valid) {
                    alert('submit!');
                } else {
                    console.log('error submit!!');
                    return false;
                }
            });
        },
        resetForm(registerData) {
            this.$refs[registerData].resetFields();
        },
        toLogin() {
            this.$router.push({ path: '/' })
        }
    }
}
</script>

<style lang="scss">
body {
    margin: 0;
    padding: 0;
}

#register-controller {
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

    .register_form {
        padding: 30px 40px 50px 40px;
        margin-top: 40px;
        border: 1px solid gainsboro;
        box-shadow: 0px 0px 4px 2px gainsboro;
        text-align: center;

        .register_title {
            display: flex;
            justify-content: center;
            padding: 10px 0;
            align-items: center;

            p {
                margin-right: 10px;
                font-size: 20px;
            }

        }

        .register_btn {
            background-color: darkcyan;
            border: none;
        }

        .register_btn:hover {
            opacity: 0.8;
        }
    }
}
</style>