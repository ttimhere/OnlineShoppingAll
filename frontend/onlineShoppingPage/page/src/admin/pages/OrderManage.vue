<template>
  <div class="order-manage">
    <!-- 查询条件 -->
    <el-card class="search-card">
      <el-form :inline="true" :model="query" size="large">
        <!-- 按订单号搜索 -->
        <el-form-item label="订单号">
          <el-input
            v-model="query.orderNo"
            placeholder="输入订单号"
            clearable
            @keyup.enter="handleSearch"/>
        </el-form-item>
         <!-- 按用户ID搜索 -->
        <el-form-item label="用户ID">
          <el-input
            v-model="query.userId"
            placeholder="输入用户ID"
            clearable
            @keyup.enter="handleSearch"/>
        </el-form-item>
         <!-- 按订单状态筛选 -->
        <el-form-item label="状态">
          <el-select
            v-model="query.status"
            placeholder="全部"
            clearable
            style="width: 160px">
            <el-option label="未支付" :value="0" />
            <el-option label="已支付" :value="1" />
            <el-option label="已发货" :value="2" />
            <el-option label="已完成" :value="3" />
            <el-option label="已取消" :value="4" />
          </el-select>
        </el-form-item>
         <!-- 按商品标题查询 -->
        <el-form-item label="商品标题">
          <el-input
            v-model="query.productTitle"
            placeholder="按商品标题模糊搜索"
            clearable
            @keyup.enter="handleSearch"
          />
        </el-form-item>
         <!-- 搜索 / 重置 -->
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="handleReset">重置</el-button>
      </el-form>
    </el-card>

    <!-- 列表 -->
    <el-card class="table-card">
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        style="width: 100%"
        :table-layout="'auto'">
        <el-table-column prop="id" label="ID" width="60" align="center" />
        <el-table-column prop="orderNo" label="订单号" min-width="150" align="center" />
        <el-table-column prop="userId" label="用户ID" width="70" align="center" />
        <!-- 订单金额 -->
        <el-table-column
          prop="totalAmount"
          label="订单金额"
          width="100"
          align="center">
          <template #default="{ row }">
            ￥{{ row.totalAmount }}
          </template>
        </el-table-column>
        <!-- 实付金额 -->
        <el-table-column
          prop="payAmount"
          label="实付金额"
          width="100"
          align="center">
          <template #default="{ row }">
            ￥{{ row.payAmount }}
          </template>
        </el-table-column>
         <!-- 状态 -->
        <el-table-column label="状态" width="120" align="center">
          <template #default="{ row }">
            <el-tag :type="statusTagType(row.status)">
              {{ statusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
         <!-- 支付方式 -->
        <el-table-column prop="payType" label="支付方式" width="100" align="center" />
         <!-- 创建时间 -->
        <el-table-column
          prop="createTime"
          label="创建时间"
          min-width="80"
          align="center">
          <template #default="{ row }">
            {{ row.createTime || '-' }}
          </template>
        </el-table-column>
         <!-- 操作按钮：详情 / 发货 / 取消 -->
        <el-table-column label="操作" width="260" align="center">
          <template #default="{ row }">
            <el-button
              type="primary"
              size="small"
              @click="goDetail(row)">
              详情
            </el-button>

            <el-button
              type="success"
              size="small"
              :disabled="row.status !== 1"
              @click="handleShip(row)">
              发货
            </el-button>

            <el-button
              type="danger"
              size="small"
              :disabled="![0, 1].includes(row.status)"
              @click="handleCancel(row)">
              取消
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-bar">
        <el-pagination
          v-model:current-page="query.pageNum"
          v-model:page-size="query.pageSize"
          :total="total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          background
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  fetchOrderList,
  shipOrder,
  cancelOrder
} from '../api/order'

const router = useRouter()

// 查询参数
const query = reactive({
  status: null,  // 订单状态筛选
  userId: '',  // 用户ID
  orderNo: '',  // 订单号
  productTitle: '',  // 商品标题
  pageNum: 1,  // 当前页码
  pageSize: 10  // 每页数量
})

const loading = ref(false)
const tableData = ref([])  // 表格数据
const total = ref(0)

//  状态文本映射 
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

// 订单状态对应 tag 类型
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



// 搜索按钮点击
const handleSearch = () => {
  query.pageNum = 1
  loadData()
}
//  重置按钮点击
const handleReset = () => {
  query.status = null
  query.userId = ''
  query.orderNo = ''
  query.productTitle = ''
  query.pageNum = 1
  query.pageSize = 10
  loadData()
}

// 分页：页码改变、：每页条数改变
const handlePageChange = () => {
  loadData()
}
const handleSizeChange = () => {
  query.pageNum = 1
  loadData()
}

// 跳转订单详情页
const goDetail = (row) => {
  router.push({
    name: 'OrderDetail',
    params: { id: row.id }
  })
}

// 发货
const handleShip = (row) => {
  ElMessageBox.confirm(
    `确认对订单【${row.orderNo}】执行发货操作吗？`,
    '提示',
    { type: 'warning' }
  )
    .then(async () => {
      try {
        const res = await shipOrder(row.id)
        const { code, msg } = res
        if (code !== 200) {
          ElMessage.error(msg || '发货失败')
          return
        }
        ElMessage.success('发货成功')
        loadData()
      } catch (e) {
        console.error(e)
        ElMessage.error('发货请求失败')
      }
    })
    .catch(() => {})
}

// 取消订单
const handleCancel = (row) => {
  ElMessageBox.confirm(
    `确认取消订单【${row.orderNo}】吗？\n已发货/已完成/已取消的订单不可取消。`,
    '警告',
    { type: 'warning' }
  )
    .then(async () => {
      try {
        const res = await cancelOrder(row.id)
        const { code, msg } = res
        if (code !== 200) {
          ElMessage.error(msg || '取消订单失败')
          return
        }
        ElMessage.success('取消成功')
        loadData()
      } catch (e) {
        console.error(e)
        ElMessage.error('取消订单请求失败')
      }
    })
    .catch(() => {})
}

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    const params = {
      status: query.status,
      userId: query.userId || undefined,
      orderNo: query.orderNo || undefined,
      productTitle: query.productTitle || undefined,
      pageNum: query.pageNum,
      pageSize: query.pageSize
    }
    const res = await fetchOrderList(params)

    console.log('订单接口返回结果:', res)

    const { code, msg, data } = res
    if (code !== 200) {
      ElMessage.error(msg || '加载订单失败')
      return
    }
     // 表格赋值
    tableData.value = data.list || []
    total.value = data.total || 0
  } catch (e) {
    console.error(e)
    ElMessage.error('请求订单列表失败')
  } finally {
    loading.value = false
  }
}
onMounted(() => {
  loadData()
})
</script>

<style scoped>
.order-manage {
  padding: 16px;
  
}
.order-manage :deep(.el-table .cell) {
  white-space: normal !important; 
  word-break: break-all !important;
}


.search-card {
  margin-bottom: 16px;
}

.table-card {
  margin-top: 0;
}

.pagination-bar {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}
</style>
