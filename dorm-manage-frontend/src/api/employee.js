import request from './request'

// 员工信息管理API
export default {
  // 分页列表
  page: (params) => request.get('/employee/admin/page', { params }),
  // 详情
  detail: (id) => request.get(`/employee/admin/detail/${id}`),
  // 新增
  add: (data) => request.post('/employee/admin/add', data),
  // 更新
  update: (data) => request.put('/employee/admin/update', data),
  // 删除
  delete: (id) => request.delete(`/employee/admin/delete/${id}`),
  // 批量删除
  batchDelete: (ids) => request.delete('/employee/admin/batch-delete', { data: ids })
}