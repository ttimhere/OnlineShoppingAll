<template>
  <div class="event-log">
    <el-card>
       <!-- 查询条件区域 -->
      <div class="search-bar">
        <!-- 输入用户ID -->
        <el-input
          v-model="userId"
          placeholder="用户ID"
          style="width: 150px; margin-right: 12px"
        />
         <!-- 事件类型下拉框 -->
        <el-select
          v-model="eventType"
          placeholder="选择事件类型"
          clearable
          style="width: 180px; margin-right: 12px"
        >
          <el-option label="浏览商品 VIEW_PRODUCT" value="VIEW_PRODUCT" />
          <el-option label="购买商品 PURCHASE" value="PURCHASE" />
        </el-select>
         <!-- 查询按钮 -->
        <el-button type="primary" @click="loadData">查询</el-button>
      </div>
    </el-card>
     <!-- 日志表格 -->
    <el-card style="margin-top: 16px">
      <el-table :data="list" border stripe>
        <el-table-column prop="id" label="ID" width="80" /> <!-- 主键 -->
        <el-table-column prop="eventType" label="类型" width="160" />  <!-- 事件类型 -->
        <el-table-column label="内容">  <!-- 内容 -->
          <template #default="{ row }">
            <pre>{{ formatContent(row.content) }}</pre>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="时间" width="180" />  <!-- 创建时间 -->
      </el-table>
      <!-- 分页 -->
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
import { getUserEventLogs } from '../api/customer'
import { useRoute } from 'vue-router'

const route = useRoute()
const userId = ref(route.query.userId || '') // 查询参数
const eventType = ref('') // 事件类型
// 分页参数
const pageNum = ref(1)
const pageSize = ref(10)

const list = ref([]) // 接口返回的数据列表
const total = ref(0) // 数据总量，用于分页



// 分页切换事件
function handlePageChange(p) {
  pageNum.value = p
  loadData()
}

// 内容字段通常是 JSON，需要格式化成多行
function formatContent(content) {
  try {
    return JSON.stringify(content, null, 2)
  } catch (e) {
    return content
  }
}


// 加载事件日志数据
function loadData() {
  getUserEventLogs({
    userId: userId.value || null,
    eventType: eventType.value || null,
    pageNum: pageNum.value,
    pageSize: pageSize.value
  }).then(res => {
    if (res.code === 200) {
      list.value = res.data.list
      total.value = res.data.total
    }
  })
}
onMounted(() => {
  loadData()
})

</script>

<style scoped>
/* 顶部查询栏横向排列 */
.search-bar {
  display: flex;
  align-items: center;
}
/* 内容展示框 */
pre {
  background: #f7f7f7;
  padding: 6px;
  border-radius: 4px;
}
/* 分页居中 */
.pagination {
  margin-top: 16px;
  text-align: center;
}
</style>
