<template>
  <div class="product-manage">
    <!-- 查询条件 -->
    <el-card class="search-card">
      <el-form :inline="true" size="large">
        <el-form-item label="标题">
          <el-input
            v-model="query.title"
            placeholder="输入关键词"
            clearable
            @keyup.enter="loadData"
          />
        </el-form-item>
        <el-form-item label="分类">
          <el-tree-select
            v-model="query.categoryId"
            :data="categoryTree"
            :props="{ value: 'id', label: 'name', children: 'children' }"
            placeholder="选择分类"
            check-strictly
            clearable
            style="width: 220px"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.status" clearable placeholder="全部"  style="width: 80px">
            <el-option label="上架" :value="1" />
            <el-option label="下架" :value="0" />
          </el-select>
        </el-form-item>
        <el-button type="primary" @click="loadData">搜索</el-button>
        <el-button type="success" @click="openCreate">新增商品</el-button>
      </el-form>
    </el-card>

    <!-- 商品表格 -->
    <el-card class="table-card">
      <el-table :data="list" border style="width: 100%">
        <el-table-column label="ID" prop="id" width="80" />
         <!-- 商品主图 -->
        <el-table-column label="主图" width="120">
          <template #default="{ row }">
            <img :src="fullImageUrl(row.mainImg)" class="thumb" />
          </template>
        </el-table-column>
        <el-table-column label="标题" prop="title" min-width="160" />
        <el-table-column label="分类ID" prop="categoryId" width="120" />
        <el-table-column label="价格" prop="price" width="100" />
        <el-table-column label="库存" prop="stock" width="100" />
        <!-- 状态（上架/下架） -->
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '上架' : '下架' }}
            </el-tag>
          </template>
        </el-table-column>
         <!-- 操作按钮：编辑 / 上架 / 下架 -->
        <el-table-column label="操作" width="260">
          <template #default="{ row }">
            <el-button size="small" @click="openEdit(row)">编辑</el-button>
            <el-button
              size="small"
              type="warning"
              @click="toggleStatus(row)">
              {{ row.status === 1 ? '下架' : '上架' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页 -->
      <el-pagination
        class="pager"
        background
        layout="prev, pager, next"
        :total="total"
        :page-size="query.pageSize"
        :current-page="query.pageNum"
        @current-change="pageChange"
      />
    </el-card>

    <!-- 商品编辑弹窗 -->
    <ProductEdit 
      v-model="showEdit"
      @update:visible="val => showEdit = val"
      :product="editingProduct"
      @success="onEditSuccess"
    />

  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { fetchProductPage, changeProductStatus } from '../api/product'
import { getCategoryTree } from '../../api/category'
import ProductEdit from './ProductEdit.vue'
import { ElMessageBox, ElMessage } from 'element-plus'

// 表格数据：商品列表和总条数
const list = ref([])
const total = ref(0)
// 拼接完整的图片 URL
function fullImageUrl(url) {
  if (!url) return '';
  const BASE = window.location.origin;
  const normalized = url.replace(/\\/g, '/');
  if (normalized.startsWith('/uploads/')) {
    return BASE + normalized.replace('/uploads', '');
  }
  if (normalized.startsWith('/product/')) {
    return BASE + normalized;
  }
  if (normalized.startsWith('/images/product')) {
    return BASE + normalized.replace('/images', '');
  }
  return BASE + '/product/' + normalized.replace(/^\/+/, '');
}


// 查询条件（双向绑定）
const query = reactive({
  pageNum: 1,
  pageSize: 10,
  title: '',
  categoryId: null,
  status: null
})



const categoryTree = ref([])

// 加载分类树
async function loadCategory() {
  const res = await getCategoryTree()
  categoryTree.value = res
}
// 根据 id 找分类节点
function findNodeById(tree, id) {
  for (const node of tree) {
    if (node.id === id) return node;
    if (node.children && node.children.length > 0) {
      const found = findNodeById(node.children, id);
      if (found) return found;
    }
  }
  return null;
}

// 工具函数：收集所有子类 ID
function collectIds(node, arr) {
  if (!node) return;
  arr.push(node.id);
  if (node.children && node.children.length > 0) {
    node.children.forEach(child => collectIds(child, arr));
  }
}

// 加载商品分页列表
async function loadData() {
  if (!query.categoryId) {
    const res = await fetchProductPage(query)
    list.value = res.data.list
    total.value = res.data.total
    return
  }
  // 选中了父分类
  const selectedNode = findNodeById(categoryTree.value, query.categoryId)
  const ids = []
  collectIds(selectedNode, ids)  // 收集父、子分类
  let allProducts = []
  // 遍历每个分类，逐一拉取商品
  for (const cid of ids) {
    const res = await fetchProductPage({
      ...query,
      categoryId: cid   // 使用每个子类查询一次
    })
    allProducts = allProducts.concat(res.data.list)
  }
  // 去重
  const map = new Map()
  allProducts.forEach(p => map.set(p.id, p))
  list.value = Array.from(map.values())
  total.value = map.size
}

//  分页切换
function pageChange(p) {
  query.pageNum = p
  loadData()
}

// 上架 / 下架
async function toggleStatus(row) {
  const newStatus = row.status === 1 ? 0 : 1

  await changeProductStatus(row.id, newStatus)
  ElMessage.success('状态更新成功')
  loadData()
}

// 弹窗相关
const showEdit = ref(false)
const editingProduct = ref(null)
// 新建商品
function openCreate() {
  console.log("openCreate 被触发了！！！！！")
  editingProduct.value = null
  showEdit.value = true
}
// 编辑商品
function openEdit(row) {
  editingProduct.value = { ...row }
  showEdit.value = true
}
// 编辑成功后关闭弹窗并刷新列表
function onEditSuccess() {
  showEdit.value = false
  loadData()
}

// 初始化加载
onMounted(() => {
  loadCategory()
  loadData()
})
</script>

<style scoped>
/* 搜索区域 */
.search-card {
  margin-bottom: 20px;
}
/* 表格中的图片显示样式 */
.table-card img {
  border: 1px solid #eee;
}

.pager {
  margin-top: 20px;
  text-align: center;
}
/* 商品主图缩略图样式 */
.thumb {
  width: 100%;
  height: 100%;
  object-fit: contain; /* 等比缩放，不裁剪 */
  padding: 4px;
  box-sizing: border-box;
}
.el-table .cell {
  height: 120px;
  line-height: 120px;
}
.el-table .cell img {
  display: block;
  margin: auto;   
  height: 100%;
}

</style>
