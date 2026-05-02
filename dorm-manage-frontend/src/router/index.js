import { createRouter, createWebHashHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录', noAuth: true }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue'),
    meta: { title: '注册', noAuth: true }
  },
  {
    path: '/',
    component: () => import('@/layout/StudentLayout.vue'),
    redirect: '/announcement',
    meta: { requireAuth: true, role: 1 },
    children: [
      { path: 'announcement', name: 'Announcement', component: () => import('@/views/student/AnnouncementList.vue'), meta: { title: '公告' } },
      { path: 'announcement/:id', name: 'AnnouncementDetail', component: () => import('@/views/student/AnnouncementDetail.vue'), meta: { title: '公告详情' } },
      { path: 'repair', name: 'Repair', component: () => import('@/views/student/RepairList.vue'), meta: { title: '报修记录' } },
      { path: 'repair/submit', name: 'RepairSubmit', component: () => import('@/views/student/RepairSubmit.vue'), meta: { title: '提交报修' } },
      { path: 'repair/detail/:id', name: 'RepairDetail', component: () => import('@/views/student/RepairDetail.vue'), meta: { title: '报修详情' } },
      { path: 'checkin', name: 'CheckIn', component: () => import('@/views/student/CheckIn.vue'), meta: { title: '入住登记' } },
      { path: 'message', name: 'Message', component: () => import('@/views/student/Message.vue'), meta: { title: '留言' } },
      { path: 'dorm-application', name: 'DormApplication', component: () => import('@/views/student/DormApplication.vue'), meta: { title: '宿舍申请' } },
      { path: 'violation', name: 'Violation', component: () => import('@/views/student/ViolationList.vue'), meta: { title: '违规记录' } }
    ]
  },
  {
    path: '/admin',
    component: () => import('@/layout/AdminLayout.vue'),
    redirect: '/admin/announcement',
    meta: { requireAuth: true, role: 2 },
    children: [
      { path: 'announcement', name: 'AdminAnnouncement', component: () => import('@/views/admin/AnnouncementManage.vue'), meta: { title: '公告管理' } },
      { path: 'repair', name: 'AdminRepair', component: () => import('@/views/admin/RepairManage.vue'), meta: { title: '报修管理' } },
      { path: 'checkin', name: 'AdminCheckIn', component: () => import('@/views/admin/CheckInManage.vue'), meta: { title: '入住审核' } },
      { path: 'message', name: 'AdminMessage', component: () => import('@/views/admin/MessageManage.vue'), meta: { title: '留言回复' } },
      { path: 'dorm-application', name: 'AdminDormApp', component: () => import('@/views/admin/DormApplicationManage.vue'), meta: { title: '宿舍申请审核' } },
      { path: 'dormitory', name: 'AdminDormitory', component: () => import('@/views/admin/DormitoryManage.vue'), meta: { title: '宿舍管理' } },
      { path: 'bed', name: 'AdminBed', component: () => import('@/views/admin/BedManage.vue'), meta: { title: '床位管理' } },
      { path: 'employee', name: 'AdminEmployee', component: () => import('@/views/admin/EmployeeManage.vue'), meta: { title: '员工信息管理' } },
      { path: 'violation', name: 'AdminViolation', component: () => import('@/views/admin/ViolationManage.vue'), meta: { title: '宿舍违规管理' } },
      { path: 'visitor', name: 'AdminVisitor', component: () => import('@/views/admin/VisitorManage.vue'), meta: { title: '来访登记' } }
    ]
  }
]

const router = createRouter({
  history: createWebHashHistory(import.meta.env.BASE_URL),
  routes
})

router.beforeEach((to, from, next) => {
  document.title = to.meta.title ? `${to.meta.title} - 大学生公寓管理系统` : '大学生公寓管理系统'
  if (to.meta.noAuth) {
    next()
    return
  }
  const store = useUserStore()
  if (!store.token) {
    next({ path: '/login', query: { redirect: to.fullPath } })
    return
  }
  const userType = Number(store.userType)
  const isAdminPath = to.path.indexOf('/admin') === 0
  if (userType === 2 && !isAdminPath) {
    next({ path: '/admin/announcement' })
    return
  }
  if (userType === 1 && isAdminPath) {
    next({ path: '/announcement' })
    return
  }
  next()
})

export default router
