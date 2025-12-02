import adminRequest from './adminRequest'
// 统一前缀
const prefix = '/api/admin/report'
// 今日 / 本月销售摘要
export function fetchReportSummary() {
    return adminRequest.get(`${prefix}/summary`)
}
// 热销商品 Top5
export function fetchHotTop5() {
    return adminRequest.get(`${prefix}/hot-top5`)
}
// 按商品统计
export function fetchSalesByProduct() {
    return adminRequest.get(`${prefix}/by-product`)
}
// 按分类统计
export function fetchSalesByCategory() {
    return adminRequest.get(`${prefix}/by-category`)
}
