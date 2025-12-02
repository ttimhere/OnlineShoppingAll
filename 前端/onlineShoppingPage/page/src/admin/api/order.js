import adminRequest from './adminRequest'
export function fetchOrderList(params) {// 订单分页查询
    return adminRequest({
        url: '/api/admin/order/list',
        method: 'get',
        params
    })
}
export function fetchOrderDetail(id) {// 订单详情
    return adminRequest({
        url: `/api/admin/order/${id}`,
        method: 'get'
    })
}
export function shipOrder(id) {// 发货
    return adminRequest({
        url: `/api/admin/order/${id}/ship`,
        method: 'post'
    })
}
export function cancelOrder(id) {// 取消
    return adminRequest({
        url: `/api/admin/order/${id}/cancel`,
        method: 'post'
    })
}
export function updateOrderAddress(id, address) {// 修改订单地址
    return adminRequest({
        url: `/api/admin/order/${id}/address`,
        method: 'put',
        data: address
    })
}
export default {
    fetchOrderList,
    fetchOrderDetail,
    shipOrder,
    cancelOrder,
    updateOrderAddress
}
