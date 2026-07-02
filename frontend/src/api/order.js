import request from './request'

export function submitOrder(data) {
  return request({
    url: '/customer/order/submit',
    method: 'post',
    data
  })
}

export function payWithWallet(orderNo) {
  return request({
    url: `/customer/order/pay?orderNo=${orderNo}`,
    method: 'post'
  })
}

export function mockWeChatPay(orderNo) {
  return request({
    url: `/customer/order/mock-wechat-pay?orderNo=${orderNo}`,
    method: 'post'
  })
}

export function listOrders(params) {
  return request({
    url: '/customer/order/list',
    method: 'get',
    params
  })
}

export function getOrderDetail(id) {
  return request({
    url: `/customer/order/${id}`,
    method: 'get'
  })
}

export function getOrderStatus(orderNo) {
  return request({
    url: `/customer/order/status/${orderNo}`,
    method: 'get'
  })
}

export function submitReviewApi(data) {
  return request({
    url: '/customer/order/review',
    method: 'post',
    data
  })
}

export function cancelOrder(orderNo) {
  return request({
    url: `/customer/order/cancel?orderNo=${orderNo}`,
    method: 'put'
  })
}

export function getOrderDetailByNo(orderNo) {
  return request({
    url: `/customer/order/detailByNo/${orderNo}`,
    method: 'get'
  })
}
