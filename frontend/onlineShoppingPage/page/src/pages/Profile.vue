<template>
  <div class="profile-page">
    <h2>个人中心</h2>
    <!-- 基本信息卡片 -->
    <el-card class="block">
      <h3>基本信息</h3>
      <p><b>昵称：</b>{{ user.nickname }}</p>
      <p><b>邮箱：</b>{{ user.email }}</p>
    </el-card>
    <!-- 快捷入口卡片 -->
    <el-card class="block">
      <h3>快捷入口</h3>
      <div class="links">
        <el-button type="primary" @click="goOrders">我的订单</el-button>
        <el-button type="primary" @click="goAddress">地址管理</el-button>
        <el-button type="danger" @click="logout">退出登录</el-button>
      </div>
    </el-card>
    <el-card class="block">
      <h3>最近订单</h3>
      <el-table :data="orders" v-if="orders.length">
        <el-table-column prop="orderNo" label="订单号" />
        <el-table-column prop="payAmount" label="金额" width="100" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            {{ statusText(Number(row.status)) }}
          </template>
        </el-table-column>
        <!-- 操作列 -->
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button size="small" @click="view(row.id)">查看详情</el-button>
            <el-button 
              v-if="Number(row.status) === 2"
              type="primary"
              size="small"
              @click="finish(row.id)"
              style="margin-left: 10px">
              确认收货
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-else description="暂无订单" />
    </el-card>

  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { getProfile } from '@/api/user'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/userStore'
import { finishOrder } from '@/api/userOrder'
import { ElMessage } from 'element-plus'
//实例初始化
const router = useRouter()
const userStore = useUserStore()
//用户基本信息与最近订单
const user = ref({})
const orders = ref([])
// 将订单状态数字转换为中文文案
const statusText = (status) => {
  const s = Number(status)
  switch (s) {
    case 1: return '已支付'
    case 2: return '已发货'
    case 3: return '已完成'
    case 4: return '已取消'
    default: return '未知状态'
  }
}
// 确认收货
const finish = async (id) => {
  await finishOrder(id)
  ElMessage.success('确认收货成功')
  // 刷新最近订单列表
  const profile = await getProfile()
  orders.value = profile.orders || []
}
//页面加载时获取用户资料和最近订单
onMounted(async () => {
  const profile = await getProfile()
  user.value = profile
  orders.value = profile.orders || []
})

// 路由跳转方法
const goOrders = () => router.push('/profile/orders')
const goAddress = () => router.push('/profile/address')
const view = (id) => router.push(`/profile/orders/${id}`)
//除用户信息并跳转到登录页
const logout = () => {
  userStore.clear()
  router.push('/login')
}
</script>

<style scoped>
.block {
  margin: 20px 0;
}
.links {
  display: flex;
  gap: 15px;
}
</style>
