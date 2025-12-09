import request from './index'
// 获取地址列表
export function getAddressList() {
    return request.get('/user/address/list')
}
// 新增地址
export function addAddress(data) {
    return request.post('/user/address/add', data)
}
// 修改地址
export function updateAddress(data) {
    return request.post('/user/address/update', data)
}
// 删除地址
export function deleteAddress(id) {
    return request.delete(`/user/address/delete/${id}`)
}
// 设置默认地址
export function setDefaultAddress(id) {
    return request.post(`/user/address/setDefault/${id}`)
}
