<template>
  <div class="sales-report">
    <!-- 标题 -->
    <el-card class="mb-16">
      <div class="header">
        <span class="title">销售统计报表</span>
        <span class="sub-title">实时掌握订单与销售表现</span>
      </div>
    </el-card>

    <!-- 概要统计卡片（今日、本月的销售额和订单数）  -->
    <el-row :gutter="16" class="mb-16">
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-label">今日销售额</div>
          <div class="stat-value">￥{{ todaySales }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-label">本月销售额</div>
          <div class="stat-value">￥{{ monthSales }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-label">今日订单数</div>
          <div class="stat-value">{{ todayOrderCount }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-label">本月订单数</div>
          <div class="stat-value">{{ monthOrderCount }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 中间：热销 Top5 + 分类统计 -->
    <el-row :gutter="16" class="mb-16">
      <el-col :span="12">
        <el-card shadow="hover" style="height: 360px">
          <div class="card-header">热销商品 TOP5（按销量）</div>
          <div ref="hotChartRef" class="chart"></div>
        </el-card>
      </el-col>
      <!-- 分类销售额饼图 -->
      <el-col :span="12">
        <el-card shadow="hover" style="height: 360px">
          <div class="card-header">按分类统计销售额</div>
          <div ref="categoryChartRef" class="chart"></div>
        </el-card>
      </el-col>
    </el-row>
    <!-- 下方：按商品销售额柱状图 -->
    <el-row :gutter="16">
      <el-col :span="24">
        <el-card shadow="hover" style="height: 380px">
          <div class="card-header">按商品统计销售额</div>
          <div ref="productChartRef" class="chart"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, nextTick, computed } from 'vue'
import * as echarts from 'echarts'
import {
  fetchReportSummary,
  fetchHotTop5,
  fetchSalesByProduct,
  fetchSalesByCategory
} from '../api/report'

// 基础数据
const summary = ref({
  '今日销售额': 0,
  '本月销售额': 0,
  '今日订单数': 0,
  '本月订单数': 0
})

// 概要数据
const loadSummary = async () => {
  const res = await fetchReportSummary()
  console.log('summary resp:', res)

  if (res.code === 200 && res.data && typeof res.data === 'object') {
    summary.value = res.data
  } else {
    console.warn('加载 summary 失败，保持默认值:', res)
  }
}
const todaySales = computed(() => formatMoney(summary.value?.['今日销售额'] ?? 0))
const monthSales = computed(() => formatMoney(summary.value?.['本月销售额'] ?? 0))
const todayOrderCount = computed(() => summary.value?.['今日订单数'] ?? 0)
const monthOrderCount = computed(() => summary.value?.['本月订单数'] ?? 0)

// 图表数据
const hotData = ref([])        // 热销 Top5
const byProductData = ref([])  // 按商品统计
const byCategoryData = ref([]) // 按分类统计

// DOM 引用对应
const hotChartRef = ref(null)
const productChartRef = ref(null)
const categoryChartRef = ref(null)
// ECharts 实例
let hotChart = null
let productChart = null
let categoryChart = null

// 金额格式化
const formatMoney = (val) => {
  if (val == null) return '0.00'
  const num = Number(val)
  if (Number.isNaN(num)) return '0.00'
  return num.toFixed(2)
}

// 初始化图表
const initCharts = () => {
  // 每个图表只初始化一次
  if (hotChartRef.value && !hotChart) {
    hotChart = echarts.init(hotChartRef.value)
  }
  if (productChartRef.value && !productChart) {
    productChart = echarts.init(productChartRef.value)
  }
  if (categoryChartRef.value && !categoryChart) {
    categoryChart = echarts.init(categoryChartRef.value)
  }
}

// 热销 TOP5 图表
const renderHotChart = () => {
  if (!hotChart) return
  const names = hotData.value.map((item) => item.title)
  const quantities = hotData.value.map((item) => item.totalQuantity)

  hotChart.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: {
      type: 'category',
      data: names,
      axisLabel: { rotate: 30 }
    },
    yAxis: {
      type: 'value',
      name: '销量'
    },
    series: [
      {
        name: '销量',
        type: 'bar',
        data: quantities
      }
    ]
  })
}
// 热销前 5
const loadHotTop5 = async () => {
  const res = await fetchHotTop5()
  if (res.code === 200) {
    hotData.value = res.data || []
    renderHotChart()
  }
}

// 按商品统计柱状图
const renderProductChart = () => {
  if (!productChart) return
  const names = byProductData.value.map((item) => item.title)
  const amounts = byProductData.value.map((item) => item.totalAmount)
  productChart.setOption({
    tooltip: {
      trigger: 'axis',
      formatter: (params) => {
        const p = params[0]
        return `${p.name}<br/>销售额：￥${formatMoney(p.value)}`}
    },
    xAxis: {
      type: 'category',
      data: names,
      axisLabel: { rotate: 30 }
    },
    yAxis: {
      type: 'value',
      name: '销售额'
    },
    series: [
      {
        name: '销售额',
        type: 'bar',
        data: amounts}
    ]
  })
}
// 按商品统计
const loadByProduct = async () => {
  const res = await fetchSalesByProduct()
  if (res.code === 200) {
    byProductData.value = res.data || []
    renderProductChart()}
}

// 分类销售额饼图
const renderCategoryChart = () => {
  if (!categoryChart) return
  const pieData = byCategoryData.value.map((item) => ({
    name: item.categoryName,
    value: item.totalAmount
  }))
  categoryChart.setOption({
    tooltip: {
      trigger: 'item',
      formatter: (p) => {
        return `${p.name}<br/>销售额：￥${formatMoney(p.value)}（${p.percent}%）`}
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '销售额',
        type: 'pie',
        radius: '60%',
        center: ['55%', '50%'],
        data: pieData}
    ]
  })
}
// 按分类统计
const loadByCategory = async () => {
  const res = await fetchSalesByCategory()
if (res.code === 200) {
  byCategoryData.value = res.data || []
  renderCategoryChart()}
}

onMounted(async () => {
  // 等 DOM 渲染完再初始化图表
  await nextTick()
  // 初始化图表实例
  initCharts()
   // 并行加载后端数据
  await Promise.all([
    loadSummary(),
    loadHotTop5(),
    loadByProduct(),
    loadByCategory()
  ])
  // 监听窗口变化，图表自动适配
  window.addEventListener('resize', handleResize)
})
// 图表自动 resize
const handleResize = () => {
  hotChart && hotChart.resize()
  productChart && productChart.resize()
  categoryChart && categoryChart.resize()
}

// 页面销毁时释放图表实例，防止内存泄漏
onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)

  if (hotChart) {
    hotChart.dispose()
    hotChart = null
  }
  if (productChart) {
    productChart.dispose()
    productChart = null
  }
  if (categoryChart) {
    categoryChart.dispose()
    categoryChart = null
  }
})
</script>

<style scoped>
.sales-report {
  padding: 16px;
}

.mb-16 {
  margin-bottom: 16px;
}
/* 标题区域 */
.header {
  display: flex;
  flex-direction: column;
}

.title {
  font-size: 20px;
  font-weight: 600;
}

.sub-title {
  font-size: 13px;
  color: #888;
  margin-top: 4px;
}
/* 统计卡片 */
.stat-card {
  text-align: center;
}

.stat-label {
  font-size: 14px;
  color: #888;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 22px;
  font-weight: 600;
}
/* 区块标题 */
.card-header {
  font-size: 15px;
  font-weight: 500;
  margin-bottom: 8px;
}

.chart {
  width: 100%;
  height: 300px;
}

.sales-report {
  overflow-x: hidden;
}

.el-row {
  margin-left: 0 !important;
  margin-right: 0 !important;
}

</style>
