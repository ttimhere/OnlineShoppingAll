<template>
  <div class="customer-list">
    <!-- 搜索栏 -->
    <el-card>
      <div class="search-bar">
         <!-- 输入搜索关键字 -->
        <el-input
          v-model="keyword"
          placeholder="搜索邮箱 / 昵称"
          clearable
          @keyup.enter="loadData"
          style="width: 260px; margin-right: 12px"/>
         <!-- 点击按钮进行搜索 -->
        <el-button type="primary" @click="loadData">搜索</el-button>
      </div>
    </el-card>
     <!-- 客户列表表格 -->
    <el-card style="margin-top: 16px">
      <el-table :data="list" stripe border>
         <!-- 用户基本信息列 -->
        <el-table-column prop="userId" label="用户ID" width="100" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="nickname" label="昵称" width="120" />
        <!-- 用户消费信息 -->
        <el-table-column prop="orderCount" label="订单数" width="100" />
        <el-table-column prop="totalAmount" label="总消费金额" width="120" />
         <!-- 注册时间 -->
        <el-table-column prop="createdAt" label="注册时间" width="180" />
         <!-- 操作按钮区 -->
        <el-table-column label="操作" width="140">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="openUserLog(row)">
              查看日志
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页组件 -->
      <div class="pagination">
        <el-pagination
          background
          layout="prev, pager, next"
          :page-size="pageSize"
          :current-page="pageNum"
          :total="total"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getCustomerList } from '../api/customer'
import { useRouter } from 'vue-router'

const router = useRouter()

const keyword = ref('') // 搜索关键词
// 分页参数
const pageNum = ref(1)
const pageSize = ref(10)

const list = ref([]) // 数据列表
const total = ref(0)


// 分页：切换页码后重新请求接口
function handlePageChange(p) {
  pageNum.value = p
  loadData()
}

// 查看用户行为日志
function openUserLog(row) {
  router.push({
    path: '/admin/customer/events',
    query: {
      userId: row.userId
    }
  })
}


// 加载客户列表
function loadData() {
  getCustomerList({
    keyword: keyword.value, // 模糊搜索字符串
    pageNum: pageNum.value,  // 当前页
    pageSize: pageSize.value,  // 每页数量
  }).then(res => {
    if (res.code === 200) {
      list.value = res.data.list // 表格数据
      total.value = res.data.total // 总条数
    }
  })
}
onMounted(() => {
  loadData()
})
</script>

<style scoped>
/* 顶部搜索栏横向排列 */
.search-bar {
  display: flex;
  align-items: center;
}
/* 分页居中 */
.pagination {
  margin-top: 16px;
  text-align: center;
}
</style>
