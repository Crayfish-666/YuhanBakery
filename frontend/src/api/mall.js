import request from './request'

export function listCategories() {
  return request({
    url: '/customer/category/list',
    method: 'get'
  })
}

export function listProducts(params) {
  return request({
    url: '/customer/product/list',
    method: 'get',
    params
  })
}

export function getProductDetail(id) {
  return request({
    url: `/customer/product/${id}`,
    method: 'get'
  })
}

export function getProductReviews(id) {
  return request({
    url: `/customer/product/reviews/${id}`,
    method: 'get'
  })
}

export function listCombos() {
  return request({
    url: '/customer/combo/list',
    method: 'get'
  })
}

// ==================== 购物车 API ====================
export function addCart(skuId, quantity) {
  return request({
    url: `/cart/add?skuId=${skuId}&quantity=${quantity}`,
    method: 'post'
  })
}

export function listCart() {
  return request({
    url: '/cart/list',
    method: 'get'
  })
}

export function updateCartQuantity(skuId, quantity) {
  return request({
    url: `/cart/quantity?skuId=${skuId}&quantity=${quantity}`,
    method: 'put'
  })
}

export function deleteCartItem(skuId) {
  return request({
    url: `/cart/${skuId}`,
    method: 'delete'
  })
}

export function clearCart() {
  return request({
    url: '/cart/clear',
    method: 'delete'
  })
}

export function claimCoupon(couponId) {
  return request({
    url: `/customer/coupon/claim/${couponId}`,
    method: 'post'
  })
}

export function getMyCoupons() {
  return request({
    url: '/customer/coupon/my',
    method: 'get'
  })
}
