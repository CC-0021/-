import request from './request'

export function myList() {
  return request({ url: '/dorm-application/my/list', method: 'get' })
}

export function submit(data) {
  return request({ url: '/dorm-application/submit', method: 'post', data })
}

export function adminPage(params) {
  return request({ url: '/dorm-application/admin/page', method: 'get', params })
}

export function adminAudit(data) {
  return request({ url: '/dorm-application/admin/audit', method: 'put', data })
}
