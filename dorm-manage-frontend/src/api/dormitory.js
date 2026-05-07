import request from './request'

// 宿舍管理API
export default {
  // 分页列表
  page: (params) => request.get('/dormitory/admin/page', { params }),
  // 详情
  detail: (id) => request.get(`/dormitory/admin/detail/${id}`),
  // 新增
  add: (data) => request.post('/dormitory/admin/add', data),
  // 更新
  update: (data) => request.put('/dormitory/admin/update', data),
  // 删除
  delete: (id) => request.delete(`/dormitory/admin/delete/${id}`),
  // 批量删除
  batchDelete: (ids) => request.delete('/dormitory/admin/batch-delete', { data: ids }),
  // 统计
  stats: () => request.get('/dormitory/admin/stats')
}