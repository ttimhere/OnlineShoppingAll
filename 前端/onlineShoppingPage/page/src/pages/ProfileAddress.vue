<template>
  <div class="address-page">
    <h2>地址管理</h2>

    <el-button type="primary" @click="openAdd">新增地址</el-button>
    <!-- 地址列表 -->
    <el-table :data="list" class="block" style="margin-top: 20px">
      <el-table-column prop="receiver" label="收货人" width="120" />
      <el-table-column prop="phone" label="手机号" width="140" />

      <el-table-column label="详细地址">
        <template #default="{ row }">
          {{ row.province }}{{ row.city }}{{ row.district }}{{ row.detail }}
        </template>
      </el-table-column>

      <el-table-column label="操作" width="260">
        <template #default="{ row }">
          <el-button size="small" @click="openEdit(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="remove(row)">删除</el-button>
          <el-button
            size="small"
            type="primary"
            v-if="row.isDefault !== true"
            @click="setDefault(row)"
          >
            设为默认
          </el-button>
          <span v-else style="color: #409EFF; margin-left: 10px">默认地址</span>
        </template>
      </el-table-column>
    </el-table>

    <!-- 编辑弹窗 -->
    <el-dialog v-model="visible" :title="form.id ? '编辑地址' : '新增地址'" width="450px">
      <el-form label-width="90px">
        <el-form-item label="收货人">
          <el-input v-model="form.receiver" />
        </el-form-item>

        <el-form-item label="手机号">
          <el-input v-model="form.phone" />
        </el-form-item>

        <el-form-item label="省/市/区">
          <el-input v-model="form.province" placeholder="省份（如：广东省）" />
          <el-input v-model="form.city" placeholder="城市（如：深圳市）" style="margin-top: 8px;" />
          <el-input v-model="form.district" placeholder="区域（如：南山区）" style="margin-top: 8px;" />
        </el-form-item>

        <el-form-item label="详细地址">
          <el-input type="textarea" rows="3" v-model="form.detail" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import {
  getAddressList,
  addAddress,
  updateAddress,
  deleteAddress,
  setDefaultAddress
} from '@/api/userAddress'
import { ElMessage } from 'element-plus'

const list = ref([])
const visible = ref(false)
const form = ref({})

// 加载地址列表
onMounted(async () => {
  list.value = await getAddressList()
})

// 新增
const openAdd = () => {
  form.value = {}
  visible.value = true
}

// 编辑
const openEdit = (row) => {
  form.value = { ...row }
  visible.value = true
}

// 保存
const save = async () => {
  if (form.value.id) {
    await updateAddress(form.value)
    ElMessage.success('地址已更新')
  } else {
    await addAddress(form.value)
    ElMessage.success('地址已新增')
  }

  list.value = await getAddressList()
  visible.value = false
}

// 删除
const remove = async (row) => {
  await deleteAddress(row.id)
  list.value = await getAddressList()
  ElMessage.success('删除成功')
}

// 设置默认
const setDefault = async (row) => {
  await setDefaultAddress(row.id)
  list.value = await getAddressList()
  ElMessage.success('已设为默认地址')
}

</script>

<style scoped>
.address-page {
  max-width: 800px;
  margin: 20px auto;
}

.block {
  margin-top: 20px;
}
</style>
