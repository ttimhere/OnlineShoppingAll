import request from './index'
// 获取分类树
export function getCategoryTree() {
    return request.get('/category/tree')
}
