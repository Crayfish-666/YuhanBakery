import request from './request'

export function listAvailableOrders(params) {
  return request({
    url: '/rider/order/available',
    method: 'get',
    params
  })
}

export function acceptOrder(orderId) {
  return request({
    url: `/rider/order/accept?orderId=${orderId}`,
    method: 'put'
  })
}

export function listMyOrders(params) {
  return request({
    url: '/rider/order/my',
    method: 'get',
    params
  })
}

export function completeDelivery(orderId) {
  return request({
    url: `/rider/order/complete?orderId=${orderId}`,
    method: 'put'
  })
}
