<template>
  <div class="order-detail-page">
    <h2>订单详情</h2>
    <!-- 订单主信息 -->
    <el-card class="block">
      <h3>订单信息</h3>
      <p><b>订单号：</b>{{ detail.order?.orderNo }}</p>
      <p><b>金额：</b>{{ detail.order?.payAmount }}</p>
      <p><b>状态：</b>{{ statusText(detail.order?.status) }}</p>
    </el-card>
    <!-- 商品列表 -->
    <el-card class="block">
      <h3>商品列表</h3>
      <el-table :data="detail.items">
        <el-table-column prop="productTitle" label="商品" />
        <el-table-column prop="price" label="单价" width="100"/>
        <el-table-column prop="quantity" label="数量" width="80"/>
      </el-table>
    </el-card>
    <!-- 收货信息 -->
    <el-card class="block">
      <h3>收货信息</h3>
      <template v-if="address">
        <p><b>收货人：</b>{{ address.receiver }}　{{ address.phone }}</p>
        <p>
          <b>地址：</b>
          {{ address.province }}
          {{ address.city }}
          {{ address.district }}
          {{ address.detail }}
        </p>
      </template>
      <template v-else>
        <p>暂无收货地址</p>
      </template>
      <!-- 确认收货按钮 -->
      <div style="margin-top: 15px;">
        <el-button
          v-if="Number(detail.order?.status) === 2"
          type="primary"
          @click="finish(detail.order.id)">
          确认收货
       </el-button>
     </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getMyOrderDetail, finishOrder } from '@/api/userOrder'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()

// 原始 detail
const detail = ref({
  order: null,
  items: []
})

// 解析出来的地址
const address = ref(null)

onMounted(async () => {
  const res = await getMyOrderDetail(route.params.id)
  detail.value = res

  // 解析地址 JSON
  try {
    address.value = JSON.parse(res.order.addressSnapshot)
  } catch (err) {
    address.value = null
  }
})
 //确认收货
const finish = async (id) => {
  await finishOrder(id)
  ElMessage.success('确认收货成功')
  router.push('/profile/orders')
}

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
</script>

<style scoped>
.block {
  margin: 20px 0;
}
</style>
