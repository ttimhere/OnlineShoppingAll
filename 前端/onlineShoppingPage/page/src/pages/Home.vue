<template>
  <div class="home">
    <div class="container">
      <!-- 左侧：分类栏 -->
      <aside class="sidebar">
        <h3>商品分类</h3>
        <ul class="category-tree">
          <!-- 首页 -->
          <li
            :class="{ active: selectedCategory === null }"
            @click="selectHome">
            首页
          </li>
          <!-- 一级分类 -->
          <li
            v-for="cat in topCategories"
            :key="cat.id">
            <div
              class="category-item"
              :class="{ active: selectedCategory === cat.id }"
              @click="selectCategory(cat)">
              <!-- 展开/折叠标记（仅在有子分类时显示） -->
              <span class="toggle" v-if="cat.children && cat.children.length"
                @click.stop="toggleExpand(cat)">
                {{ cat.expanded ? '▼' : '▶' }}
              </span>
              <span class="cat-name">{{ cat.name }}</span>
            </div>
            <!-- 二级分类 -->
            <ul
              v-if="cat.children && cat.children.length && cat.expanded"
              class="subcategory">
              <li
                v-for="sub in cat.children"
                :key="sub.id"
                :class="{ active: selectedCategory === sub.id }"
                @click="selectCategory(sub)">
                {{ sub.name }}
              </li>
            </ul>
          </li>
        </ul>
      </aside>

      <!-- 右侧：商品网格 -->
      <section class="content">
        <div class="grid">
          <template v-if="loading">
            <el-empty description="正在加载商品..." />
          </template>
          <!-- 商品为空（无数据） -->
          <template v-else-if="items.length === 0">
            <el-empty :description="emptyText" />
          </template>
           <!-- 有数据时：网格渲染商品卡片 -->
          <template v-else>
            <div
              v-for="item in items"
              :key="item.id"
              class="card-wrap"
              @click="openDetail(item)">
              <el-card shadow="hover" body-class="card-body">
                <!-- 商品图片 -->
                <div class="thumb">
                  <img :src="resolveImg(item.mainImg)" :alt="item.title" />
                </div>
                    <div class="info">
                      <!-- 商品标题 -->
                      <div class="title" :title="item.title">{{ item.title }}</div>
                       <!-- 底部：价格 + 加入购物车按钮 -->
                      <div class="bottom-row">
                        <span class="price">￥{{ formatPrice(item.price) }}</span>
                        <el-button
                          class="add-btn"
                          size="small"
                          @click.stop="addFromList(item)">
                          加入购物车
                        </el-button>
                      </div>
                   </div>
              </el-card>
            </div>
          </template>
        </div>
        <!-- 分页组件（仅当商品总数 > 页面容量时显示） -->
        <div class="pager" v-if="!loading && total > pageSize">
          <el-pagination
            background
            layout="prev, pager, next, jumper"
            :total="total"
            :page-size="pageSize"
            :current-page="pageNum"
            @current-change="handlePageChange"
          />
        </div>
      </section>
    </div>
    <!-- 商品详情弹窗 -->
    <el-dialog
      v-model="showDialog"
      width="500px"
      :title="currentItem && currentItem.title">
      <div v-if="currentItem" class="detail-dialog">
         <!-- 图片 -->
        <div class="detail-image">
          <img
            :src="resolveImg(currentItem.mainImg)"
            :alt="currentItem.title"
          />
        </div>
        <p class="detail-subtitle">
          {{ currentItem.subTitle }}
        </p>
        <!-- 弹窗里的加入购物车按钮 -->
        <el-button
          size="large"
          style="margin-top: 12px"
          class="add-btn"
          @click="addFromDialog"
          >
          加入购物车
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { fetchProducts, fetchCategoryTree, getProductDetail } from '@/api/product'
import { ElMessage } from 'element-plus'
import { addToCart as addToCartApi } from '@/api/cart'
import { useCartStore } from '@/store/cartStore'

const selectedCategory = ref(null) // 当前选中的分类 ID
const cartStore = useCartStore()  // 顶部购物车数量同步

// 通用加入购物车逻辑
const doAddToCart = async (productId) => {
  try {
    await addToCartApi({ productId, quantity: 1 })
    ElMessage.success('已加入购物车')
    cartStore.increase()
  } catch (e) {
    if (e.response?.status === 401) {
      ElMessage.warning('请先登录后再添加购物车')
    } else {
      ElMessage.error(e.message || '加入购物车失败')
    }
  }
}

//  商品数据
const items = ref([])
const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(12)
const total = ref(0)

const route = useRoute() // 用于同步 keyword
// 搜索词来自 URL 的 query
const keyword = computed(() => (route.query.keyword || '').toString().trim())
const emptyText = computed(() =>
  keyword.value ? `没有找到与 “${keyword.value}” 相关的商品` : '暂无商品'
)

//  获取分类树
const loadCategories = async () => {
  try {
    const res = await fetchCategoryTree()
    if (Array.isArray(res)) {
      // 添加 expanded 状态
      const markExpanded = (nodes) => {
        nodes.forEach((n) => {
          n.expanded = false
          if (n.children && n.children.length) markExpanded(n.children)
        })
      }
      markExpanded(res)
      topCategories.value = res
    }
  } catch (e) {
    console.error('❌ 加载分类失败：', e)
  }
}

// 图片路径拼接
const BACKEND_ORIGIN = window.location.origin;
const resolveImg = (p) => {
  if (!p) return '';
  const normalized = p.replace(/\\/g, '/');
  if (normalized.startsWith('/uploads/')) {
    return BACKEND_ORIGIN + normalized.replace('/uploads', '');
  }
  if (normalized.startsWith('/product/')) {
    return BACKEND_ORIGIN + normalized;
  }
  return BACKEND_ORIGIN + '/product/' + normalized.replace(/^\/+/, '');
};



//  格式化价格
const formatPrice = (p) => {
  try {
    return Number(p).toFixed(2)
  } catch {
    return p
  }
}

// 获取商品列表
const fetchList = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      orderBy: 'time_desc',
    }
    // 如果当前 URL 中带有 ?keyword=xxx，则加入搜索参数
    if (keyword.value) params.keyword = keyword.value
    // 如果用户选中了某个分类，则加入分类过滤
    if (selectedCategory.value) params.categoryId = selectedCategory.value
    // 调用后端 API
    const res = await fetchProducts(params)
    if (res && Array.isArray(res.list)) {
      items.value = res.list  // 商品数据
      total.value = res.total || 0  // 商品总数量
      pageNum.value = res.pageNum || 1
      pageSize.value = res.pageSize || 12
    } else {
      items.value = []
    }
  } catch (err) {
    console.error('❌ 请求商品失败：', err)
    items.value = []
  } finally {
    loading.value = false
  }
}
// 首页卡片上的“加入购物车”
const addFromList = (item) => {
  doAddToCart(item.id)
}

//  分类交互
const topCategories = ref([]) // 存储完整分类树
// 点击分类
const selectCategory = (cat) => {
  selectedCategory.value = cat.id
  pageNum.value = 1
  fetchList()
}
// 展开/折叠一级分类
const toggleExpand = (cat) => {
  cat.expanded = !cat.expanded
}

// 点击“首页”
const selectHome = () => {
  selectedCategory.value = null
  pageNum.value = 1
  fetchList()
}
// 生命周期和监听
onMounted(async () => {
  await loadCategories()
  await fetchList()
})

watch(() => keyword.value, () => {
  selectedCategory.value = null
  pageNum.value = 1
  fetchList()
})

// 分页
const handlePageChange = (p) => {
  pageNum.value = p
  fetchList()
}

// 商品详情弹窗相关状态
const showDialog = ref(false)
const currentItem = ref(null)

// 打开商品详情
const openDetail = async (item) => {
  showDialog.value = true  // 先打开弹窗，提高响应速度

  try {
    const detail = await getProductDetail(item.id)
    currentItem.value = detail
  } catch (e) {
    console.error("❌ 加载商品详情失败", e)
  }
}
// 弹窗里的“加入购物车”
const addFromDialog = () => {
  if (!currentItem.value) return
  doAddToCart(currentItem.value.id)
}



</script>

<style scoped>
/* 主容器布局，左侧分类 + 右侧商品 */
.container {
  display: flex;
  gap: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

/* 左侧分类栏 */
.sidebar {
  width: 220px;
  background: #fff;
  border-radius: 12px;
  padding: 10px 0;
  border: 1px solid #eee;
  flex-shrink: 0;
}
/* 分类标题 “商品分类” */
.sidebar h3 {
  font-size: 16px;
  font-weight: 600;
  color: #e1251b;
  padding: 0 16px 8px;
  border-bottom: 1px solid #f5f5f5;
  margin-bottom: 8px;
}
/* 分类树整体 */
.category-tree {
  list-style: none;
  margin: 0;
  padding: 0;
}
/* 一级分类样式 */
.category-tree li {
  padding: 6px 16px;
  font-size: 14px;
  cursor: pointer;
  color: #333;
  border-left: 4px solid transparent;
  transition: all 0.2s;
}

.category-tree li:hover,
.category-tree li.active {
  background: #fff5f5;
  color: #e1251b;
  border-left-color: #e1251b;
}
/* 分类项中的图标和分类名 */
.category-item {
  display: flex;
  align-items: center;
  gap: 6px;
}
/* 展开/折叠 ▶ ▼ 图标 */
.toggle {
  font-size: 12px;
  color: #999;
  user-select: none;
}

.toggle:hover {
  color: #e1251b;
}

/* 二级分类缩进 */
.subcategory {
  list-style: none;
  margin: 4px 0 4px 18px;
  padding: 0;
}
/* 二级分类样式 */
.subcategory li {
  padding: 6px 12px;
  font-size: 13px;
  border-left: 3px solid transparent;
  transition: all 0.2s;
}

.subcategory li:hover,
.subcategory li.active {
  background: #fff5f5;
  color: #e1251b;
  border-left-color: #e1251b;
}

/* 右侧商品区 */
.content {
  flex: 1;
}
/* 商品网格布局：自适应列数量 */
.grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 16px;
}

.card-body {
  padding: 10px;
}
/* 商品封面图容器 */
.thumb {
  width: 100%;
  aspect-ratio: 1 / 1;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fff;
  overflow: hidden;
  border-radius: 12px;
}
/* 商品图像 */
.thumb img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.25s ease;
}

.card-wrap:hover .thumb img {
  transform: scale(1.03);
}
/* 商品信息区 */
.info {
  padding: 8px 2px 2px 2px;
}

.title {
  font-size: 14px;
  line-height: 20px;
  height: 40px;
  overflow: hidden;
  color: #333;
}

.price {
  margin-top: 6px;
  font-weight: 700;
  color: #e1251b;
  font-size: 16px;
}
/* 分页区域 */
.pager {
  display: flex;
  justify-content: center;
  padding: 16px 0 32px;
}
/* 弹窗商品详情整体 */
.detail-dialog {
  text-align: center;
}
/* 弹窗图片容器 */
.detail-image {
  display: flex;
  justify-content: center;
  margin-top: 10px;
}
/* 弹窗图片样式 */
.detail-image img {
  width: 80%;
  border-radius: 10px;
}

.detail-subtitle {
  margin-top: 15px;
  font-size: 14px;
  color: #666;
  text-align: left;
}

.bottom-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 6px;
}

</style>

<style>

html {
  scrollbar-gutter: stable;
}


.add-btn {
  background-color: #e1251b;
  border-color: #e1251b;
  color: #fff;
}

.add-btn:hover {
  background-color: #c91f1a;
  border-color: #c91f1a;
}
</style>
