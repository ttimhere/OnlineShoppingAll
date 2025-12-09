<template>
  <div class="cart-item">
    <el-row align="middle" :gutter="20">
     <!-- 勾选当前商品 -->
      <el-col :span="2">
        <el-checkbox v-model="item.selected" @change="toggleSelected" />
      </el-col>
      <!-- 商品图片 -->
      <el-col :span="4">
        <img :src="imgUrl(item.mainImg || item.img)" alt="商品图片" class="cart-img" />
      </el-col>
      <!-- 商品标题、副标题 -->
      <el-col :span="6">
        <div class="title">{{ item.title }}</div>
        <div class="subtitle">{{ item.subTitle }}</div>
      </el-col>
      <!-- 商品单价 -->
      <el-col :span="3">
        <div class="price">￥{{ item.price.toFixed(2) }}</div>
      </el-col>
      <!-- 数量增减 -->
      <el-col :span="4">
        <el-input-number
          v-model="item.quantity"
          :min="1"
          :max="item.stock"
          size="small"
          @change="changeQuantity"
        />
      </el-col>
      <!-- 小计（单价 × 数量） -->
      <el-col :span="3">
        <div class="subtotal">￥{{ (item.price * item.quantity).toFixed(2) }}</div>
      </el-col>
      <!-- 删除按钮 -->
      <el-col :span="2">
        <el-button type="danger" size="small" @click="removeItem">删除</el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { updateCartItem, deleteCartItem } from '@/api/cart'
import { ElMessage } from 'element-plus'

//接收父组件传来的单个购物车 item
const props = defineProps({
  item: { type: Object, required: true }
})
//refresh 事件：用于通知父组件刷新购物车列表
const emits = defineEmits(['refresh'])

//根据商品的图片路径，生成可访问的完整图片 URL
const BACKEND_ORIGIN = window.location.origin;
const imgUrl = (p) => {
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


//修改商品数量
const changeQuantity = async (value) => {
  await updateCartItem({
    productId: props.item.productId,
    quantity: value,
    selected: props.item.selected
  })
  ElMessage.success('数量已更新')
  // 通知父组件刷新数据
  emits('refresh')
}
//勾选/取消勾选当前商品
const toggleSelected = async (value) => {
  await updateCartItem({
    productId: props.item.productId,
    quantity: props.item.quantity,
    selected: value
  })
  
  emits('refresh')
}
//  删除该商品
const removeItem = async () => {
  await deleteCartItem(props.item.productId)
  ElMessage.success('已删除商品')
  emits('refresh')
}
</script>

<style scoped>
.cart-item {
  border-bottom: 1px solid #eee;
  padding: 10px 0;
}
.cart-img {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 8px;
}
.title {
  font-weight: 600;
}
.subtitle {
  color: #999;
  font-size: 13px;
}
.price, .subtotal {
  color: #e74c3c;
  font-weight: bold;
}

</style>
