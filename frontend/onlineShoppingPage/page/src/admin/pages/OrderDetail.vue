<template>
  <div class="order-detail">
    <!-- 返回按钮 -->
    <el-button type="text" @click="goBack">
      ← 返回订单列表
    </el-button>
    <!-- 订单信息 -->
    <el-card class="block-card" v-if="order">
      <template #header>
        <div class="card-header">
          <span>订单信息</span>
          <div>
             <!-- 订单状态标签 -->
            <el-tag :type="statusTagType(order.status)" class="mr-8">
              {{ statusText(order.status) }}
            </el-tag>
            <!-- 发货按钮：仅 “已支付” 状态可点击 -->
            <el-button
              type="success"
              size="small"
              :disabled="order.status !== 1"
              @click="handleShip">
              发货
            </el-button>
             <!-- 取消订单按钮 -->
            <el-button
              type="danger"
              size="small"
              :disabled="![0, 1].includes(order.status)"
              @click="handleCancel">
              取消订单
            </el-button>
          </div>
        </div>
      </template>
      <!-- 订单基本信息 -->
      <el-descriptions :column="2" border>
        <el-descriptions-item label="订单ID">
          {{ order.id }}
        </el-descriptions-item>
        <el-descriptions-item label="订单号">
          {{ order.orderNo }}
        </el-descriptions-item>
        <el-descriptions-item label="用户ID">
          {{ order.userId }}
        </el-descriptions-item>
        <el-descriptions-item label="订单金额">
          ￥{{ order.totalAmount }}
        </el-descriptions-item>
        <el-descriptions-item label="实付金额">
          ￥{{ order.payAmount }}
        </el-descriptions-item>
        <el-descriptions-item label="支付方式">
          {{ order.payType || '-' }}
        </el-descriptions-item>

        <el-descriptions-item label="创建时间">
          {{ order.createTime || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="支付时间">
          {{ order.payTime || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="发货时间">
          {{ order.shipTime || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="完成时间">
          {{ order.finishTime || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="取消时间">
          {{ order.cancelTime || '-' }}
        </el-descriptions-item>
      </el-descriptions>
    </el-card>

    <!-- 收货地址 -->
    <el-card class="block-card" v-if="address">
      <template #header>
        <span>收货地址</span>
      </template>
      <el-descriptions :column="1" border>
        <el-descriptions-item label="收货人">
          {{ address.receiver || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="手机号">
          {{ address.phone || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="地区">
          {{ fullRegion }}
        </el-descriptions-item>
        <el-descriptions-item label="详细地址">
          {{ address.detail || '-' }}
        </el-descriptions-item>
      </el-descriptions>
    </el-card>

    <!-- 商品明细 -->
    <el-card class="block-card">
      <template #header>
        <span>商品明细</span>
      </template>
      <el-table :data="items" border stripe style="width: 100%">
        <!-- 商品图片 -->
        <el-table-column label="图片" width="100" align="center">
          <template #default="{ row }">
            <el-image
              v-if="row.img"
              :src="formatImg(row.img)"
              fit="cover"
              style="width: 60px; height: 60px"
              :preview-src-list="[formatImg(row.img)]"/>
          </template>
        </el-table-column>
        <el-table-column prop="productTitle" label="商品名称" min-width="180" />
        <el-table-column
          prop="price"
          label="单价"
          width="120"
          align="right">
          <template #default="{ row }">
            ￥{{ row.price }}
          </template>
        </el-table-column>
        <el-table-column
          prop="quantity"
          label="数量"
          width="80"
          align="center"/>
        <el-table-column
          label="小计"
          width="120"
          align="right">
          <template #default="{ row }">
            ￥{{ row.price * row.quantity }}
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <!-- 支付信息 -->
    <el-card class="block-card" v-if="payment">
      <template #header>
        <span>支付信息</span>
      </template>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="支付渠道">
          {{ payment.payChannel || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="支付请求号">
          {{ payment.requestNo || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="支付金额">
          ￥{{ payment.amount }}
        </el-descriptions-item>
        <el-descriptions-item label="支付状态">
          {{ paymentStatusText(payment.status) }}
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">
          {{ payment.createdAt || '-' }}
        </el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  fetchOrderDetail,
  shipOrder,
  cancelOrder
} from '../api/order'

const route = useRoute()
const router = useRouter()
const loading = ref(false)

const detail = ref(null) //订单总数据
const order = computed(() => detail.value?.order || null)
const items = computed(() => detail.value?.items || [])
const payment = computed(() => detail.value?.payment || null)

// 本地图片地址处理
// 统一使用当前站点域名/IP 作为图片前缀
const IMG_BASE = window.location.origin;
const formatImg = (url) => {
  if (!url) return '';
  const normalized = url.replace(/\\/g, '/');
  if (normalized.startsWith('/uploads/')) {
    return IMG_BASE + normalized.replace('/uploads', '');
  }
  if (normalized.startsWith('/product/')) {
    return IMG_BASE + normalized;
  }
  return IMG_BASE + '/product/' + normalized.replace(/^\/+/, '');
};



// 收货地址：解析 JSON
const address = computed(() => {
  if (!order.value || !order.value.addressSnapshot) return null
  try {
    return JSON.parse(order.value.addressSnapshot)
  } catch (e) {
    console.warn('地址 JSON 解析失败', e)
    return null
  }
})
//拼接省市区字符串
const fullRegion = computed(() => {
  if (!address.value) return '-'
  const { province, city, district } = address.value
  return [province, city, district].filter(Boolean).join(' ')
})

//订单状态文本
const statusText = (status) => {
  switch (status) {
    case 0:
      return '未支付'
    case 1:
      return '已支付'
    case 2:
      return '已发货'
    case 3:
      return '已完成'
    case 4:
      return '已取消'
    default:
      return '未知'
  }
}

//状态对应的 tag 颜色
const statusTagType = (status) => {
  switch (status) {
    case 0:
      return 'info'
    case 1:
      return 'warning'
    case 2:
      return 'success'
    case 3:
      return 'success'
    case 4:
      return 'danger'
    default:
      return 'info'
  }
}

//支付状态文本
const paymentStatusText = (status) => {
  if (status === 1) return '支付成功'
  if (status === 0) return '未支付'
  return '未知'
}

// 发货
const handleShip = () => {
  if (!order.value) return
  ElMessageBox.confirm(
    `确认发货订单【${order.value.orderNo}】吗？`,
    '提示',
    { type: 'warning' }
  ).then(async () => {
    const res = await shipOrder(order.value.id)
    const { code, msg } = res
    if (code !== 200) {
      ElMessage.error(msg || '发货失败')
      return
    }
    ElMessage.success('发货成功')
    loadDetail() // 刷新订单
  })
}

// 取消订单
const handleCancel = () => {
  if (!order.value) return
  ElMessageBox.confirm(
    `确认取消订单【${order.value.orderNo}】吗？`,
    '警告',
    { type: 'warning' }
  ).then(async () => {
    const res = await cancelOrder(order.value.id)
    const { code, msg } = res
    if (code !== 200) {
      ElMessage.error(msg || '取消失败')
      return
    }
    ElMessage.success('取消成功')
    loadDetail()
  })
}
// 返回上一页
const goBack = () => router.back()

//加载订单详情
const loadDetail = async () => {
  const id = route.params.id
  if (!id) {
    ElMessage.error('缺少订单ID')
    return
  }
  loading.value = true
  try {
    const res = await fetchOrderDetail(id)
    const { code, msg, data } = res
    if (code !== 200) {
      ElMessage.error(msg || '获取订单详情失败')
      return
    }
    detail.value = data
  } finally {
    loading.value = false
  }
}
// 页面加载时获取订单
onMounted(() => loadDetail())
</script>


<style scoped>
.order-detail {
  word-break: break-all;     
  overflow-x: hidden;      
  width: 100%;
  max-width: none;
  margin: 0;
  padding: 0 20px;
  box-sizing: border-box;
}

.block-card {
  width: 100%;
  margin-bottom: 16px;
}

.order-detail .el-descriptions {
  width: 100%;
}

.order-detail .el-table {
  width: 100% !important;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.mr-8 {
  margin-right: 8px;
}

</style>
