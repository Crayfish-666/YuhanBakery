import request from './request'

export function login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

export function register(data) {
  return request({
    url: '/user/register',
    method: 'post',
    data
  })
}

export function getUserInfo() {
  return request({
    url: '/user/info',
    method: 'get'
  })
}

export function topup(amount) {
  return request({
    url: `/user/topup?amount=${amount}`,
    method: 'post'
  })
}

export function listAddresses() {
  return request({
    url: '/user/address',
    method: 'get'
  })
}

export function saveAddress(data) {
  return request({
    url: '/user/address',
    method: 'post',
    data
  })
}

export function setDefaultAddress(addressId) {
  return request({
    url: `/user/address/default/${addressId}`,
    method: 'put'
  })
}

export function deleteAddress(addressId) {
  return request({
    url: `/user/address/${addressId}`,
    method: 'delete'
  })
}

export function listBalanceLogs(params) {
  return request({
    url: '/user/balance-log',
    method: 'get',
    params
  })
}
