<template>
  <div class="order-list-page">
    <h2>我的订单</h2>
    <!-- 有订单时显示表格 -->
    <el-table :data="orders" v-if="orders.length">
      <el-table-column prop="orderNo" label="订单号" />
      <el-table-column prop="payAmount" label="金额" width="100" />
      <el-table-column label="状态">
        <template #default="{ row }">
          {{ statusText(Number(row.status)) }}
        </template>
      </el-table-column>
      <!-- 操作按钮：查看和确认收货 -->
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <!-- 跳转到订单详情 -->
          <el-button size="small" @click="view(row.id)">查看</el-button>
           <!-- 状态为 2（已发货）时展示“确认收货”按钮 -->
          <el-button
            v-if="Number(row.status) === 2"
            type="primary"
            size="small"
            @click="finish(row.id)"
            style="margin-left: 10px"
          >
            确认收货
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 没订单时显示空状态 -->
    <el-empty v-else description="暂无订单" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyOrders } from '@/api/userOrder'
import { useRouter } from 'vue-router'

import { finishOrder } from '@/api/userOrder'
import { ElMessage } from 'element-plus'

//确认收货
const finish = async (id) => {
  await finishOrder(id)
  ElMessage.success('确认收货成功')

  // 刷新订单列表
  orders.value = await getMyOrders()
}
// 路由实例
const router = useRouter()
// 订单数据
const orders = ref([])

//状态数字转中文文本
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

onMounted(async () => {
  orders.value = await getMyOrders()
  console.log("订单 API 返回：", res)
  orders.value = res
})
//跳转到订单详情
const view = (id) => router.push(`/profile/orders/${id}`)
</script>
