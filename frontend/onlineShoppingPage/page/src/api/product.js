import request from './index'
console.log("🔥 前台 product.js 被加载！");
// 获取类目树结构
export function fetchCategoryTree() {
    return request.get('/category/tree')
}
// 获取商品分页数据
export function fetchProducts(params) {
    return request.get('/products', { params })
}
// 为商品添加图片
export function addProductImages(productId, urls) {
    return request.post(`/products/${productId}/images`, urls)
}
// 查看商品详情
export function getProductDetail(id) {
    return request.get(`/products/${id}`)
}