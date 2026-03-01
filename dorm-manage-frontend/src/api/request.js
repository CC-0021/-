import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'

const service = axios.create({
  baseURL: import.meta.env.VITE_BASE_API || '/api',
  timeout: 10000
})

service.interceptors.request.use((config) => {
  const store = useUserStore()
  if (store.token) {
    config.headers['Authorization'] = 'Bearer ' + store.token
  }
  return config
}, (e) => Promise.reject(e))

service.interceptors.response.use(
  (res) => {
    const data = res.data
    if (data.code !== 200) {
      ElMessage({ message: data.msg || '请求失败', type: 'error' })
      if (data.code === 401) {
        const store = useUserStore()
        store.logout().then(() => { window.location.hash = '#/login' })
      }
      return Promise.reject(new Error(data.msg))
    }
    return data
  },
  (err) => {
    const status = err.response?.status
    const msg = err.response?.data?.msg || err.message || '网络错误'
    ElMessage({ message: msg, type: 'error' })
    if (status === 401) {
      const store = useUserStore()
      store.logout().then(() => { window.location.hash = '#/login' })
    }
    if (status === 403) {
      ElMessage({ message: '权限不足或请求被拒绝', type: 'warning' })
    }
    return Promise.reject(err)
  }
)

export default service
