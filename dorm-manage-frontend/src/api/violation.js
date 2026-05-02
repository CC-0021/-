import request from './request'

// 宿舍违规管理API
export default {
  // 分页列表
  page: (params) => request.get('/violation/admin/page', { params }),
  // 详情
  detail: (id) => request.get(`/violation/admin/detail/${id}`),
  // 新增
  add: (data) => request.post('/violation/admin/add', data),
  // 更新
  update: (data) => request.put('/violation/admin/update', data),
  // 删除
  delete: (id) => request.delete(`/violation/admin/delete/${id}`),
  // 批量删除
  batchDelete: (ids) => request.delete('/violation/admin/batch-delete', { data: ids }),
  // 学生端：我的违规记录
  myList: () => request.get('/violation/my/list')
}