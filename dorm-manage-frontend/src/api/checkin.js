import request from './request'

export function myList() {
  return request({ url: '/checkin/my/list', method: 'get' })
}

export function submit(data) {
  return request({ url: '/checkin/submit', method: 'post', data })
}

export function adminPage(params) {
  return request({ url: '/checkin/admin/page', method: 'get', params })
}

export function audit(data) {
  return request({ url: '/checkin/admin/audit', method: 'put', data })
}
