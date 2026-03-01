import request from './request'

export function myList() {
  return request({ url: '/message/my/list', method: 'get' })
}

export function add(data) {
  return request({ url: '/message/add', method: 'post', data })
}

export function adminPage(params) {
  return request({ url: '/message/admin/page', method: 'get', params })
}

export function adminReply(data) {
  return request({ url: '/message/admin/reply', method: 'put', data })
}
