<template>
  <div class="login-container">
    <div class="login-box">
      <h2 class="title">后台管理登录</h2>
      <!-- 表单容器 -->
      <el-form :model="form" class="login-form">
        <!-- 用户名输入框 -->
        <el-form-item>
          <el-input v-model="form.username" placeholder="请输入账号" />
        </el-form-item>
        <!-- 密码输入框 -->
        <el-form-item>
          <el-input type="password" v-model="form.password" placeholder="请输入密码" />
        </el-form-item>
         <!-- 登录按钮-->
        <el-form-item>
          <el-button type="primary" class="login-btn" @click="handleLogin" :loading="loading">
            登录
          </el-button>
        </el-form-item>
         <!-- 登录失败提示信息 -->
        <div class="error" v-if="errorMsg">{{ errorMsg }}</div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { adminLogin } from '../api/login'   // 登录接口
import { useAdminStore } from '../store/adminStore'
import { useRouter } from 'vue-router'
// 表单数据
const form = reactive({
  username: '',
  password: ''
})

const loading = ref(false)
const errorMsg = ref('') // 显示错误信息
const router = useRouter() // 页面跳转
const adminStore = useAdminStore() // 管理员登录状态
// 点击登录按钮触发
async function handleLogin() {
  loading.value = true
  errorMsg.value = ''
  console.log("点击登录，开始 handleLogin");
  try {
     // 调用后台登录接口
    const res = await adminLogin(form)
    console.log("登录返回结果：", res);
    if (res.code !== 200) {  // 登录成功：存入 Pinia 
      errorMsg.value = res.message
      loading.value = false
      return
    }
    adminStore.setLoginInfo(res.data)
    router.push('/admin') // 跳转后台首页
  } catch (e) {
    errorMsg.value = '登录失败，请检查网络'
  }
  loading.value = false
}
</script>

<style scoped>
.login-container { /* 整个页面居中布局 */
  width: 100%;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f6f8;
}
/* 登录窗口的白色卡片 */
.login-box {
  width: 360px;
  padding: 30px;
  background: white;
  border-radius: 10px;
  box-shadow: 0 2px 10px #ddd;
}
/* 登录标题 */
.title {
  text-align: center;
  margin-bottom: 20px;
}
/* 登录按钮 */
.login-btn {
  width: 100%;
}
/* 错误提示文字 */
.error {
  color: red;
  text-align: center;
}
</style>
