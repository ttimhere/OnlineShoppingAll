import request from './index'

// 获取当前用户订单列表
export function getMyOrders() {
    return request.get('/user/orders')
}

// 获取某订单详情
export function getMyOrderDetail(orderId) {
    return request.get(`/user/orders/${orderId}`)
}

// 确认收货
export function finishOrder(orderId) {
    return request.post(`/user/orders/${orderId}/finish`)
}
