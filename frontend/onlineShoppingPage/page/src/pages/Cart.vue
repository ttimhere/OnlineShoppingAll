<template>
  <div
    class="cart-page"
    v-loading="paying"
    element-loading-text="正在支付中，请稍候..."
    element-loading-background="rgba(255,255,255,0.6)"
  >
    <h2>🛒 我的购物车</h2>
    <!-- 当购物车为空时显示空提示 -->
    <div v-if="cartItems.length === 0" class="empty">
      <el-empty description="购物车是空的哦~" />
    </div>
    <!-- 有商品时显示商品列表 -->
    <div v-else>
      <CartItem
        v-for="item in cartItems"
        :key="item.cartItemId"
        :item="item"
        @refresh="loadCart"
      />
      <!-- 全选行（勾选栏下面） -->
      <div class="cart-item select-all-row">
        <el-row align="middle" :gutter="20">
          <el-col :span="2">
           <el-checkbox v-model="allSelected" @change="toggleSelectAll" />
         </el-col>
         <!-- 空白列，用于保持对齐 -->
         <el-col :span="22">
            <span class="select-all-text">全选</span>
          </el-col>
       </el-row  l-row>
      </div>
       <!-- 结算信息区域 -->
      <div class="summary">
        <div class="total">
          已选中 {{ selectedCount }} 件商品，
          总计：<span class="money">￥{{ totalAmount.toFixed(2) }}</span>
        </div>
        <el-button
          type="primary"
          size="large"
          class="checkout-btn"
          :disabled="selectedCount === 0"
          :loading="paying"
          @click="openAddressDialog" >
          结算
        </el-button>
      </div>
    </div>
    <!-- 选择收货地址弹窗 -->
    <el-dialog
      v-model="addressDialog"
      title="选择收货地址"
     width="840px">
    <!-- 地址列表（可滚动） -->
    <div style="max-height: 380px; overflow-y: auto; padding-right: 10px;">
    <el-radio-group v-model="selectedAddressId" class="address-group">
      <el-radio v-for="a in addressList" :key="a.id" :label="a.id" class="address-item">
         <div class="address-line">
            <div class="name">{{ a.receiver }}（{{ a.phone }}）</div>
            <div class="detail">
              {{ a.province }}{{ a.city }}{{ a.district }}{{ a.detail }}
          </div>
         </div>
       </el-radio>
     </el-radio-group>
      </div>
     <!-- 按钮 -->
      <template #footer>
      <el-button @click="addressDialog = false">取消</el-button>
      <el-button type="primary" @click="confirmAddress">确认使用该地址</el-button>
      <el-button type="success" @click="openAddDialog">新增地址</el-button>
      </template>
    </el-dialog>
    <!--  新增地址弹窗 -->
    <el-dialog v-model="addDialog" title="新增地址" width="880px">
      <el-form label-width="90px">
        <el-form-item label="收货人">
          <el-input v-model="newAddr.receiver" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="newAddr.phone" />
        </el-form-item>
        <el-form-item label="省份">
          <el-input v-model="newAddr.province" />
        </el-form-item>
        <el-form-item label="城市">
          <el-input v-model="newAddr.city" />
        </el-form-item>
        <el-form-item label="区域">
          <el-input v-model="newAddr.district" />
        </el-form-item>
        <el-form-item label="详细地址">
          <el-input v-model="newAddr.detail" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addDialog = false">取消</el-button>
        <el-button type="primary" @click="saveAddress">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getCartList, checkoutCart, updateCartItem } from '@/api/cart'
import { getAddressList, addAddress } from '@/api/userAddress'
import CartItem from '@/components/CartItem.vue'
import { useCartStore } from '@/store/cartStore'
import { ElMessage, ElMessageBox } from 'element-plus'

const cartStore = useCartStore()
const cartItems = ref([])  // 当前购物车商品列表
const paying = ref(false)   // 是否正在支付（用于 Loading）

// 地址相关状态
const addressDialog = ref(false)  // 地址选择弹窗
const addDialog = ref(false)  // 新增地址弹窗
const addressList = ref([]) // 地址列表
const selectedAddressId = ref(null) // 当前选中的地址
// 用于新增地址的表单对象
const newAddr = ref({
  receiver: '',
  phone: '',
  province: '',
  city: '',
  district: '',
  detail: '',
})

// 打开地址选择弹窗
const openAddressDialog = async () => {
  addressList.value = await getAddressList()
  if (addressList.value.length > 0) {
    selectedAddressId.value = addressList.value[0].id
  }
  addressDialog.value = true
}

//  用户点击“确认使用该地址”
const confirmAddress = async () => {
  if (!selectedAddressId.value) {
    return ElMessage.warning('请选择一个收货地址')
  }
  // 找到当前选中的地址对象
  const addr = addressList.value.find(a => a.id === selectedAddressId.value)
  const json = JSON.stringify(addr)
  addressDialog.value = false
  checkout(json)
}

//  新增地址弹窗
const openAddDialog = () => {
  // 重置表单
  newAddr.value = {
    receiver: '',
    phone: '',
    province: '',
    city: '',
    district: '',
    detail: '',
  }
  // 先关闭“选择地址”弹窗，再打开“新增地址”
  addressDialog.value = false
  addDialog.value = true
}
//保存新增地址
const saveAddress = async () => {
  await addAddress(newAddr.value)
  ElMessage.success('地址已新增')
  // 关闭“新增地址”弹窗
  addDialog.value = false
  // 重新拉取地址列表
  const list = await getAddressList()
  addressList.value = list
  // 自动选中新加的那条
  if (addressList.value.length > 0) {
    selectedAddressId.value = addressList.value[addressList.value.length - 1].id
  }
  // 回到“选择收货地址”弹窗
  addressDialog.value = true
}

// 把后端可能各种格式的返回值统一转成数组
function toList(res) {
  if (Array.isArray(res)) return res
  if (Array.isArray(res?.data?.data)) return res.data.data
  if (Array.isArray(res?.data)) return res.data
  return []
}
 //获取购物车列表
const loadCart = async () => {
  try {
    const res = await getCartList()
    const list = toList(res)
    cartItems.value = list
    cartStore.count = list.length
  } catch {
    cartItems.value = []
    cartStore.count = 0
  }
}
//选中的商品列表、数量、总价
const selectedItems = computed(() => cartItems.value.filter(i => i.selected))
const selectedCount = computed(() => selectedItems.value.length)
const totalAmount = computed(() =>
  selectedItems.value.reduce((sum, i) => sum + i.price * i.quantity, 0)
)

// 执行结算（传入地址 JSON）
const checkout = async (addressJson) => {
  try {
    paying.value = true
    // 支付接口
    const res = await checkoutCart(null, addressJson)
    const orderNo = res
    paying.value = false
     // 显示支付成功弹窗
    await ElMessageBox.alert(
      `支付成功！<br>订单号：<b>${orderNo}</b>`,
      '支付结果',
      {
        dangerouslyUseHTMLString: true,
        confirmButtonText: '好的',
        type: 'success'
      }
    )
    await loadCart()  // 刷新购物车
  } catch (e) {
    paying.value = false
    ElMessage.error('支付失败')
  }
}

//  是否全选
const allSelected = computed({
  get() {
    return cartItems.value.length > 0 && cartItems.value.every(i => i.selected)
  },
  set(val) {
  }
})

//  切换全选 / 取消全选
const toggleSelectAll = async (value) => {
  for (const item of cartItems.value) {
    if (item.selected !== value) {
      await updateCartItem({
        productId: item.productId,
        quantity: item.quantity,
        selected: value
      })
    }
  }
  await loadCart()
}

//页面加载时获取购物车
onMounted(loadCart)
</script>

<style scoped>
.address-radio {
  display: block;
  margin: 12px 0;
}

.addr-text {
  white-space: normal;   /*  允许换行 */
  line-height: 1.6;
}
.address-group {
  width: 100%;
}

.address-item {
  display: block !important;   /* 强制每个 radio 占满整行 */
  width: 100%;
  margin: 16px 0;          /* 地址之间稍微拉开一点 */
}

.address-line {
  display: flex;
  flex-direction: column;   /*  姓名+电话 与 地址 垂直排布 */
  line-height: 1.6;
}

.name {
  font-weight: 600;
  font-size: 14px;
}

.detail {
  font-size: 13px;
  color: #666;
  white-space: normal;     /*  地址自动换行 */
  word-break: break-all;
}

/* 结算区域 */
.summary {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 16px;
  font-size: 14px;
  color: #666;
}

.money {
  color: #e1251b;    /* 统一商城红 */
  font-weight: 700;
  font-size: 18px;
}

/* 只影响当前结算按钮 */
.checkout-btn {
  background-color: #e1251b;
  border-color: #e1251b;
  width: 130px;
}

.checkout-btn:hover,
.checkout-btn:focus {
  background-color: #ff4d4f;
  border-color: #ff4d4f;
}

.checkout-btn.is-disabled {
  background-color: #ccc;
  border-color: #ccc;
}

.select-all-row {
  padding: 10px 0;
  border-bottom: 1px solid #eee;
  margin-bottom: 10px;
}

</style>

