import request from './request'

export function getList(params) {
  return request({ url: '/announcement/list', method: 'get', params })
}

export function getDetail(id) {
  return request({ url: `/announcement/detail/${id}`, method: 'get' })
}

export function adminPage(params) {
  return request({ url: '/announcement/admin/page', method: 'get', params })
}

export function add(data) {
  return request({ url: '/announcement/admin/add', method: 'post', data })
}

export function update(data) {
  return request({ url: '/announcement/admin/update', method: 'put', data })
}

export function remove(id) {
  return request({ url: `/announcement/admin/delete/${id}`, method: 'delete' })
}
