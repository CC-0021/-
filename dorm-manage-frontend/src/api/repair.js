import request from './request'

export function submit(data) {
  return request({ url: '/repair/submit', method: 'post', data })
}

export function cancel(id) {
  return request({ url: `/repair/cancel/${id}`, method: 'put' })
}

export function myList(params) {
  return request({ url: '/repair/my/list', method: 'get', params })
}

export function detail(id) {
  return request({ url: `/repair/detail/${id}`, method: 'get' })
}

export function evaluate(data) {
  return request({ url: '/repair/evaluate', method: 'post', data })
}

export function adminPage(params) {
  return request({ url: '/repair/admin/page', method: 'get', params })
}

export function adminAssign(data) {
  return request({ url: '/repair/admin/assign', method: 'put', data })
}

export function adminHandle(data) {
  return request({ url: '/repair/admin/handle', method: 'put', data })
}

export function statsByType(params) {
  return request({ url: '/repair/admin/stats/type', method: 'get', params })
}

export function statsByBuilding(params) {
  return request({ url: '/repair/admin/stats/building', method: 'get', params })
}
