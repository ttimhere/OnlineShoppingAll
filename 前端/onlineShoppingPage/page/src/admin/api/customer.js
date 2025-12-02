import adminRequest from './adminRequest'
//客户列表（分页 + 搜索）
export function getCustomerList(params) {
    return adminRequest({
        url: '/api/admin/customer/list',
        method: 'get',
        params
    })
}
//用户行为日志
export function getUserEventLogs(params) {
    return adminRequest({
        url: '/api/admin/customer/events',
        method: 'get',
        params
    })
}
