<template>
  <header class="navbar">
    <div class="navbar-inner">
      <!-- 左侧：Logo（图片 + 文本）（点击回首页） -->
      <div class="left" @click="goHome">
        <img class="logo" src="@/logo.png" alt="logo" />
        <span class="brand">OnlineShop商城</span>
      </div>
      <!-- 中间：大搜索框 -->
      <div class="center">
        <el-input
          v-model="keyword"
          placeholder="搜一搜好物：手机 / 笔记本 / 连衣裙 ..."
          size="large"
          clearable
          @keyup.enter="doSearch"
        >
          <template #append>
            <el-button type="danger" size="large" @click="doSearch">
              搜索
            </el-button>
          </template>
        </el-input>
      </div>

      <!-- 右侧：用户中心 + 购物车 + 管理后台 -->
      <div class="right">
        <!-- 若已登录 显示用户中心 -->
        <template v-if="isAuthed">
          <!-- 用户下拉菜单 -->
          <el-dropdown trigger="hover">
            <span class="user entry">
              <el-icon><User /></el-icon>
              <!-- 用户昵称 -->
              <span class="username">
                {{ userStore.profile?.nickname || '用户' }}
              </span>
              <el-icon class="caret"><ArrowDown /></el-icon>
            </span>
            <!-- 下拉内容 -->
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="goProfile">个人中心</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
        <!-- 未登录状态：显示登录入口 -->
        <template v-else>
          <span class="entry" @click="goLogin">
            <el-icon><User /></el-icon>
            登录 / 注册
          </span>
        </template>

        <!-- 购物车 -->
        <span class="entry" @click="goCart">
          <el-icon><ShoppingCart /></el-icon>
          购物车
        </span>
        
        <span class="entry" @click="goAdminEntry">
                  管理后台
        </span>
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref, watch, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/store/userStore'
import { User, ShoppingCart, ArrowDown } from '@element-plus/icons-vue'
import { useAdminStore } from '@/admin/store/adminStore'

const router = useRouter()
const route = useRoute()
// 用户登录状态
const userStore = useUserStore()
const isAuthed = computed(() => userStore.isAuthed)
const goProfile = () => router.push('/profile')
// 搜索框绑定的关键字
const keyword = ref('')
// 初始化时把地址栏的 ?keyword= 同步到输入框

// 导航跳转函数
const goHome = () => router.push({ path: '/' })
const goCart = () => router.push({ path: '/cart' })
const goLogin = () => router.push({ path: '/login' })

// 管理员登录状态
const adminStore = useAdminStore()
const isAdminAuthed = computed(() => !!adminStore.token)
// 管理后台入口：已登录管理员 → /admin，未登录 → /admin/login
const goAdminEntry = () => {
  if (isAdminAuthed.value) {
    router.push('/admin')
  } else {
    router.push('/admin/login')
  }
}

onMounted(() => {
  keyword.value = (route.query.keyword || '').toString()
})
// 当路由的 keyword 变化时，同步输入框
watch(
  () => route.query.keyword,
  (v) => {
    keyword.value = (v || '').toString()
  }
)
// 触发搜索：跳到首页并更新 ?keyword=xxx，由 Home.vue 监听并拉取数据
const doSearch = () => {
  const k = keyword.value.trim()
  router.push({
    path: '/',
    query: k ? { keyword: k } : {}
  })
}
</script>

<style scoped>
/* 顶部导航栏样式 */
.navbar {
  position: fixed;
  top: 0; left: 0; right: 0;
  z-index: 1000;
  background: #fff;
  border-bottom: 1px solid #f2f2f2;
  box-shadow: 0 2px 10px rgba(0,0,0,0.04);
}
/* 容器布局 */
.navbar-inner {
  max-width: 1200px;
  margin: 0 auto;
  height: 72px;
  display: grid;
  grid-template-columns: 280px 1fr 320px;
  align-items: center;
  gap: 16px;
  padding: 0 16px;
}
/* 左侧 Logo */
.left {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.logo {
  width: 40px; height: 40px;
  border-radius: 8px;
  object-fit: cover;
  margin-right: 10px;
}

.brand {
  font-size: 20px;
  font-weight: 700;
  color: #e1251b; /* 天猫系红 */
  letter-spacing: 0.5px;
}
/* 中间搜索框 */
.center :deep(.el-input) {
  --el-input-border-color: #ff4d4f20;
}
/* 右侧入口（用户中心 / 购物车 / 管理后台） */
.right {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 18px;
}

.entry {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  color: #333;
  cursor: pointer;
  padding: 8px 10px;
  border-radius: 10px;
  transition: all .2s ease;
}

.entry:hover {
  background: #fdf2f2;
  color: #e1251b;
}

.username {
  max-width: 120px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.caret {
  margin-left: 2px;
}
</style>
