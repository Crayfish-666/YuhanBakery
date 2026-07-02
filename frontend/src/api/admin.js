import request from './request'

// 1. 上传图片到 OSS/本地降级目录
export function uploadImage(formData) {
  return request({
    url: '/admin/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 2. 商品分类管理
export function saveCategory(data) {
  return request({
    url: '/admin/category',
    method: 'post',
    data
  })
}

export function deleteCategory(id) {
  return request({
    url: `/admin/category/${id}`,
    method: 'delete'
  })
}

export function listCategories() {
  return request({
    url: '/admin/category/list',
    method: 'get'
  })
}

// 3. 商品与多规格 SKU 管理
export function saveProduct(data) {
  return request({
    url: '/admin/product/save',
    method: 'post',
    data
  })
}

export function deleteProduct(id) {
  return request({
    url: `/admin/product/${id}`,
    method: 'delete'
  })
}

export function toggleProductStatus(id, status) {
  return request({
    url: `/admin/product/status/${id}?status=${status}`,
    method: 'put'
  })
}

export function listProducts(params) {
  return request({
    url: '/admin/product/list',
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

// 4. 套餐与配置分组管理
export function saveCombo(data) {
  return request({
    url: '/admin/combo/save',
    method: 'post',
    data
  })
}

export function deleteCombo(id) {
  return request({
    url: `/admin/combo/${id}`,
    method: 'delete'
  })
}

export function toggleComboStatus(id, status) {
  return request({
    url: `/admin/combo/status/${id}?status=${status}`,
    method: 'put'
  })
}

export function listCombos(params) {
  return request({
    url: '/admin/combo/list',
    method: 'get',
    params
  })
}

// 5. 数据大屏看板统计与全局订单
export function getDashboardStats() {
  return request({
    url: '/admin/dashboard/stats',
    method: 'get'
  })
}

export function listAllOrders(params) {
  return request({
    url: '/admin/order/list',
    method: 'get',
    params
  })
}
