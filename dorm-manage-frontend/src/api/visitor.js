import request from './request'

// 来访登记API
export default {
  // 分页列表
  page: (params) => request.get('/visitor/admin/page', { params }),
  // 详情
  detail: (id) => request.get(`/visitor/admin/detail/${id}`),
  // 新增
  add: (data) => request.post('/visitor/admin/add', data),
  // 更新
  update: (data) => request.put('/visitor/admin/update', data),
  // 按天统计
  statsByDay: (params) => request.get('/visitor/admin/stats/day', { params }),
  // 按楼栋统计
  statsByBuilding: (params) => request.get('/visitor/admin/stats/building', { params })
}