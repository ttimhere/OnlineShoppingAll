import adminRequest from './adminRequest'

export function adminLogin(data) {
    return adminRequest.post('/api/admin/login', data)
}
