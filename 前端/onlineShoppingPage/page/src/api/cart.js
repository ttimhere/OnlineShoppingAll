import request from './index'
//添加商品
export const addToCart = (data) => {
    return request.post('/cart/add', data)
}
//获取购物车列表
export const getCartList = () => {
    return request.get('/cart/list')
}
//更新购物车商品
export const updateCartItem = (data) => {
    return request.post('/cart/update', data)
}
//删除购物车商品
export const deleteCartItem = (productId) => {
    return request.post('/cart/delete', null, {
        params: { productId }
    })
}
//结算购物车
export const checkoutCart = (email, address) => {
    return request.post('/cart/checkout', null, {
        params: { email, address }
    })
}

