import { createRouter, createWebHistory } from 'vue-router'
import Home from '../pages/Home.vue'
import Login from '../pages/Login.vue'
import Cart from '../pages/Cart.vue'
import adminRoutes from '@/admin/router/adminRoutes'
import { useAdminStore } from '@/admin/store/adminStore'
import Profile from '@/pages/Profile.vue'
import ProfileOrders from '@/pages/ProfileOrders.vue'
import ProfileOrderDetail from '@/pages/ProfileOrderDetail.vue'
import ProfileAddress from '@/pages/ProfileAddress.vue'
// 所有路由表
const routes = [
  { path: '/', component: Home }, // 前台首页
  { path: '/login', component: Login }, //用户登录路由
  { path: '/cart', component: Cart }, //购物车路由
  // 用户中心相关路由
  { path: '/profile', component: Profile },
  { path: '/profile/orders', component: ProfileOrders },
  { path: '/profile/orders/:id', component: ProfileOrderDetail },
  { path: '/profile/address', component: ProfileAddress },
  // 后台管理所有子路由
  ...adminRoutes
]
const router = createRouter({
  history: createWebHistory(),
  routes
})
// 每次路由跳转前都会执行这里的逻辑
// 用于后台管理的权限验证
router.beforeEach((to, from, next) => {
  if (to.path.startsWith('/admin') && to.path !== '/admin/login') {
    const adminStore = useAdminStore()  // 读取后台管理员登录状态
    if (!adminStore.token) {
      return next('/admin/login')
    }
  }
  next()
})
export default router
