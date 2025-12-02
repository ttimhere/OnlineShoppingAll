<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-50">
    <el-card class="w-[420px]">
      <template #header>
        <div class="text-lg font-semibold text-center">用户登录 / 注册</div>
      </template>

      <el-tabs v-model="tab" stretch>
        <el-tab-pane label="登录" name="login">
          <el-form ref="loginRef" :model="loginForm" :rules="rules" label-width="80px">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="loginForm.email" placeholder="请输入邮箱"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="passwordHash">
              <el-input v-model="loginForm.passwordHash" type="password" show-password placeholder="请输入密码"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button :loading="loading" type="primary" class="w-full" @click="onLogin">登录</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="注册" name="register">
          <el-form ref="regRef" :model="regForm" :rules="rules" label-width="80px">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="regForm.email" placeholder="请输入邮箱"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="passwordHash">
              <el-input v-model="regForm.passwordHash" type="password" show-password placeholder="请输入密码"></el-input>
            </el-form-item>
            <el-form-item label="昵称" prop="nickname">
              <el-input v-model="regForm.nickname" placeholder="可选昵称"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button :loading="loading" type="success" class="w-full" @click="onRegister">注册</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>

      <div v-if="errorMsg" class="mt-2 text-red-500 text-sm text-center">{{ errorMsg }}</div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { ElMessage } from 'element-plus';
import { useRouter, useRoute } from 'vue-router';
import { useUserStore } from '@/store/userStore';
import { login, register } from '@/api/user';

const router = useRouter();
const route = useRoute();
const store = useUserStore();

const tab = ref('login');
const loading = ref(false);
const errorMsg = ref('');

const loginForm = ref({ email: '', passwordHash: '' });
const regForm = ref({ email: '', passwordHash: '', nickname: '' });

const rules = {
  email: [{ required: true, message: '请输入邮箱', trigger: 'blur' }],
  passwordHash: [{ required: true, message: '请输入密码', trigger: 'blur' }],
};

const loginRef = ref();
const regRef = ref();

async function onLogin() {
  errorMsg.value = '';
  await loginRef.value.validate();  // 进行表单校验
  loading.value = true;
  try {
    const data = await login(loginForm.value); // 调用后端登录接口
    const token = data?.token;
    if (!token) throw new Error('登录失败，未获取到token');
    store.setLogin(data);  // 调用 Pinia 方法写入 token 和用户信息
    ElMessage.success('登录成功');
    router.replace(route.query.redirect || '/'); // 跳转回页面
  } catch (err) {
    errorMsg.value = String(err);  // 显示错误
  } finally {
    loading.value = false;
  }
}

async function onRegister() {
  errorMsg.value = '';
  await regRef.value.validate(); // 校验表单
  loading.value = true;
  try {
    await register(regForm.value); // 调用接口发送注册信息
    ElMessage.success('注册成功，请登录');
    tab.value = 'login';   // 自动切换到登录 tab
    loginForm.value.email = regForm.value.email; // 自动填入邮箱
  } catch (err) {
    errorMsg.value = String(err);
  } finally {
    loading.value = false;
  }
}
</script>

<style scoped>
.w-full { width: 100%; }
.min-h-screen { min-height: 100vh; }
.bg-gray-50 { background: #f9fafb; }
</style>
