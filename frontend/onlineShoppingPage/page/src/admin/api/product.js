import adminRequest from './adminRequest'
// 分页查询商品
export function fetchProductPage(params) {
    return adminRequest.get('/api/admin/products', { params })
}
// 新增商品
export function createProduct(data) {
    return adminRequest.post('/api/admin/products', data)
}
// 编辑商品
export function updateProduct(id, data) {
    return adminRequest.put(`/api/admin/products/${id}`, data)
}
// 修改商品状态（上架/下架）
export function changeProductStatus(id, status) {
    return adminRequest.patch(`/api/admin/products/${id}/status`, { status })
}
// 上传商品图片
export function uploadImage(file) {
    const formData = new FormData()
    formData.append('file', file)
    return adminRequest.post('/api/upload/image', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
    })
}
