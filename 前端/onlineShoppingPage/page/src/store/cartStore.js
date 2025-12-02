import { defineStore } from 'pinia'
import { getCartList } from '@/api/cart'
/**
 * 将后端返回的各种可能格式，统一解析成数组
 * 避免后端字段结构不一致导致失败
 */
function toList(res) {
    if (Array.isArray(res)) return res
    if (Array.isArray(res?.data?.data)) return res.data.data
    if (Array.isArray(res?.data)) return res.data
    return []
}
// 购物车状态管理
export const useCartStore = defineStore('cart', {
    state: () => ({ count: 0 }), //只存一个 count，用于显示购物车数量
    actions: {
        async loadCount() {
            try {
                const res = await getCartList() //调用 API 获取购物车列表
                this.count = toList(res).length //使用 toList 统一解析 将 count 设置为列表长度
            } catch {
                this.count = 0
            }
        },
        increase() { this.count++ }, //添加商品后购物车数量+1
        clear() { this.count = 0 } //清空购物车数量
    }
})
