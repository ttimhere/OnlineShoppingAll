import request from './index';
export function register(data) {
    // data = { email, passwordHash, nickname }
    return request.post('/user/register', data);
}
export function login(data) {
    // data = { email, passwordHash }
    return request.post('/user/login', data);
}
export function getProfile() {
    return request.get('/user/me');
}
