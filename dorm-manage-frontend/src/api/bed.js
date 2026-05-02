import request from './request'

// 床位管理API
export default {
  // 分页列表
  page: (params) => request.get('/bed/admin/page', { params }),
  // 根据宿舍ID获取床位列表
  listByDorm: (dormitoryId) => request.get(`/bed/admin/list-by-dorm/${dormitoryId}`),
  // 详情
  detail: (id) => request.get(`/bed/admin/detail/${id}`),
  // 新增
  add: (data) => request.post('/bed/admin/add', data),
  // 更新
  update: (data) => request.put('/bed/admin/update', data),
  // 更新状态
  updateStatus: (params) => request.put('/bed/admin/update-status', { params }),
  // 删除
  delete: (id) => request.delete(`/bed/admin/delete/${id}`),
  // 批量删除
  batchDelete: (ids) => request.delete('/bed/admin/batch-delete', { data: ids })
}