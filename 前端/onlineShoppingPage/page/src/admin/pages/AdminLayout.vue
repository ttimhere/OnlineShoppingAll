<template>
  <div class="admin-layout">
    <!-- 左侧侧边栏 -->
    <aside class="sidebar">
      <div class="logo-area">
        <el-icon style="margin-right: 8px; vertical-align: middle"><Shop /></el-icon>
        <span>OnlineShop商城</span>
      </div>
      <!-- 后台菜单 -->
      <el-menu
        router
        class="menu"
        :default-active="activePath" 
        background-color="#2d3a4b"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        :unique-opened="true">
       <!-- 遍历渲染过滤后的后台菜单 -->
        <el-menu-item
          v-for="item in filteredMenus"
          :key="item.id"
          :index="item.path">
          <span>{{ item.name }}</span>
        </el-menu-item>
      </el-menu>
    </aside>
    <!-- 右侧主体 -->
    <section class="main">
       <!-- 顶部栏 -->
      <header class="topbar">
        <div class="left-panel"></div> 
        <div class="admin-info">
          <span class="nickname">
            {{ adminStore.admin?.nickname || '管理员' }}
          </span>
          <el-button type="primary" link class="logout-btn" @click="logout">
            退出
          </el-button>
        </div>
      </header>
       <!-- 主内容区 -->
      <main class="content-wrapper">
        <div class="content-inner">
           <router-view />
        </div>
      </main>
    </section>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAdminStore } from '../store/adminStore'
import { Shop } from '@element-plus/icons-vue' // 如果你安装了 element-plus 图标库
// 路由、状态管理
const route = useRoute()
const router = useRouter()
const adminStore = useAdminStore()
// 当前路由路径
const activePath = computed(() => route.path)
// 筛选后台菜单
const filteredMenus = computed(() => {
  const roles = adminStore.roles || []
  const allMenus = adminStore.menus.filter(m =>
    !["category:manage", "admin:manage"].includes(m.permission)
  )
   // 超级管理员：显示所有菜单
  if (roles.includes("SUPER_ADMIN")) return allMenus
   // 客服：仅显示订单与客户管理
  if (roles.includes("CUSTOMER_SERVICE")) {
    return allMenus.filter(m =>
      ["order:manage", "customer:manage"].includes(m.permission)
    )
  }
  return []
})

// 退出登录
const logout = () => {
  adminStore.logout()
  router.push('/admin/login')
}
</script>

<style scoped>
/* --- 整体后台布局 --- */
.admin-layout {
  display: flex;
  width: 100%;
  height: 100vh;
  overflow: hidden; 
}

/* 左侧侧边栏 */
.sidebar {
  width: 210px;
  background: #2d3a4b;
  color: #fff;
  display: flex;
  flex-direction: column;
  height: 100%; 
  flex-shrink: 0;
  transition: width 0.3s; 
}

.logo-area {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 18px;
  color: #fff;
  background-color: #263445;
  border-bottom: 1px solid #1f2d3d;
}
/* 后台菜单区域 */
.menu {
  border-right: none;
  flex: 1;
  overflow-y: auto; 
}

/* 隐藏滚动条 */
.menu::-webkit-scrollbar {
  width: 0;
}

/* --- 右侧主体布局 --- */
.main {
  flex: 1;
  display: flex;
  flex-direction: column;
  height: 100%;
  overflow: hidden; 
  background-color: #f0f2f5; 
}

/* 顶部栏 */
.topbar {
  height: 60px;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08); 
  padding: 0 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-shrink: 0; 
  z-index: 9; 
}

.nickname {
  font-size: 14px;
  color: #606266;
  margin-right: 16px;
}

/* 内容包装区 */
.content-wrapper {
  flex: 1;
  overflow-y: overlay; 
  overflow-x: hidden;
  padding: 20px; 
  box-sizing: border-box;
  scroll-behavior: smooth;
  -webkit-overflow-scrolling: touch;
}
/* 内容内部容器 */
.content-inner {
  width: 100%;
  height: auto; 
  min-height: 100%;
  max-width: 100%;
  padding-bottom: 40px; 
}
</style>