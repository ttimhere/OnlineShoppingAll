<template>
  <el-dialog
    v-model="localVisible"
    :title="isEdit ? '编辑商品' : '新增商品'" 
    width="700px"
    @close="handleClose">
     <!-- 表单区域 -->
    <el-form :model="form" label-width="100px" size="large">
      <el-form-item label="标题">
        <el-input v-model="form.title" placeholder="请输入商品标题" />
      </el-form-item>
      <el-form-item label="副标题">
        <el-input v-model="form.subTitle" placeholder="请输入副标题" />
      </el-form-item>
       <!-- 分类选择：树形下拉选择器 -->
      <el-form-item label="分类">
        <el-tree-select
          v-model="form.categoryId"
          :data="categoryTree"
          :props="{ value: 'id', label: 'name', children: 'children' }"
          placeholder="请选择分类"
          check-strictly
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="价格">
        <el-input v-model="form.price" type="number" placeholder="价格 (元)" />
      </el-form-item>
      <el-form-item label="库存">
        <el-input v-model="form.stock" type="number" placeholder="库存数量" />
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="form.status" placeholder="请选择">
          <el-option label="上架" :value="1" />
          <el-option label="下架" :value="0" />
        </el-select>
      </el-form-item>
      <!-- 主图上传 -->
      <el-form-item label="主图">
        <el-upload
          class="main-img-uploader"
          action="/api/upload/image"
          :headers="uploadHeaders"
          :show-file-list="false"
          :on-success="handleMainImgSuccess">
          <img v-if="form.mainImg" :src="fullImageUrl(form.mainImg)" class="main-img" />
          <el-icon v-else class="upload-icon"><Plus /></el-icon>
        </el-upload>
      </el-form-item>
      <!-- 商品属性 JSON（可扩展） -->
      <el-form-item label="属性 JSON">
        <el-input
          v-model="form.attrs"
          type="textarea"
          placeholder="示例：{color:red}"
          :rows="3"/>
      </el-form-item>
    </el-form>
    <!-- 底部按钮 -->
    <template #footer>
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit">保存</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch, reactive, onMounted } from "vue"
import { createProduct, updateProduct } from "../api/product"
import { getCategoryTree } from "../../api/category"
import { useAdminStore } from "../store/adminStore"
import { ElMessage } from "element-plus"
import { Plus } from "@element-plus/icons-vue"

// 处理图片 URL
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
  return BASE + '/product/' + normalized.replace(/^\/+/, '');
}



// 接收父组件参数 props
const props = defineProps({
  visible: Boolean,
  product: Object
})

// 向父组件发送事件
const emit = defineEmits(["close", "success", "update:visible"])
//弹窗内部的可控 visible 状态
const localVisible = ref(false)

//  双向绑定 visible
watch(
  () => props.visible,
  (v) => {
    localVisible.value = v
  },
  { immediate: true }
)

watch(
  () => localVisible.value,
  (v) => {
    emit("update:visible", v)
  }
)

const adminStore = useAdminStore()

// 上传请求头（携带 Token）
const uploadHeaders = {
  Authorization: "Bearer " + adminStore.token
}

// 分类树
const categoryTree = ref([])

// 是否为“编辑模式”
const isEdit = ref(false)

// 表单
const form = reactive({
  id: null,
  title: "",
  subTitle: "",
  categoryId: null,
  price: 0,
  stock: 0,
  status: 1,
  mainImg: "",
  attrs: "",
  images: []   // 图片 URL 数组
})


async function loadCategory() {
  const res = await getCategoryTree()
  console.log('分类 API 原始返回：', res)
  categoryTree.value = res
}


// 监听 product，回显表单
watch(
  () => props.product,
  (p) => {
    if (p) {
      isEdit.value = true
      Object.assign(form, JSON.parse(JSON.stringify(p)))
    } else {
      isEdit.value = false
      resetForm()
    }
  },
  { immediate: true }
)

// 主图上传成功
function handleMainImgSuccess(res) {
  if (res.code === 200) {
    form.mainImg = res.data.url
  }
}

// 重置表单
function resetForm() {
  Object.assign(form, {
    id: null,
    title: "",
    subTitle: "",
    categoryId: null,
    price: 0,
    stock: 0,
    status: 1,
    mainImg: "",
    attrs: "",
    images: []
  })
}

// 提交保存
async function handleSubmit() {
  const payload = {
    ...form,
    price: Number(form.price),
    stock: Number(form.stock),
    status: Number(form.status),
    categoryId: Number(form.categoryId),
    attrs: form.attrs?.trim() ? form.attrs : null, 
  }
  let res
  // 新增或更新
  if (!isEdit.value) {
    res = await createProduct(payload)
  } else {
    res = await updateProduct(form.id, payload)
  }
  if (res.code !== 200) {
    ElMessage.error(res.message)
    return
  }
  ElMessage.success(isEdit.value ? "更新成功" : "新增成功")
  emit("success")
}


// 关闭弹窗
function handleClose() {
  emit("close")
  emit("update:visible", false)
}

onMounted(() => {
  loadCategory()
})
</script>

<style scoped>
.main-img-uploader {
  width: 120px;
  height: 120px;
  border: 1px dashed #ccc;
  border-radius: 6px;
  cursor: pointer;
}

.main-img {
  width: 120px;
  height: 120px;
  object-fit: cover;
  border-radius: 6px;
}

.upload-icon {
  font-size: 28px;
  color: #999;
  margin-top: 40px;
}

.img-list-uploader {
  width: 100%;
}
</style>
