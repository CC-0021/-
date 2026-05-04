<template>
  <div class="login-wrap">
    <div class="login-bg">
      <div class="shape shape-1" />
      <div class="shape shape-2" />
      <div class="shape shape-3" />
      <div class="shape shape-4" />
    </div>
    <div class="login-container">
      <div class="login-brand">
        <div class="brand-icon">
          <svg viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
            <rect x="4" y="16" width="40" height="28" rx="4" fill="white" fill-opacity="0.2" stroke="white" stroke-width="2"/>
            <path d="M4 20L24 8L44 20" stroke="white" stroke-width="2" stroke-linecap="round"/>
            <rect x="18" y="28" width="12" height="16" rx="2" fill="white" fill-opacity="0.3" stroke="white" stroke-width="1.5"/>
          </svg>
        </div>
        <h1 class="brand-title">大学生公寓管理系统</h1>
        <p class="brand-desc">安全 · 便捷 · 智能</p>
      </div>
      <div class="login-card">
        <div class="card-header">
          <h2 class="card-title">欢迎回来</h2>
          <p class="card-subtitle">请登录您的账号</p>
        </div>
        <el-form ref="formRef" :model="form" :rules="rules" label-position="top" class="form" @submit.prevent="handleLogin">
          <el-form-item label="学号 / 工号" prop="studentId">
            <el-input
              v-model="form.studentId"
              placeholder="请输入学号或工号"
              size="large"
              :prefix-icon="User"
              clearable
            />
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input
              v-model="form.password"
              type="password"
              placeholder="请输入密码"
              size="large"
              :prefix-icon="Lock"
              show-password
              clearable
              @keyup.enter="handleLogin"
            />
          </el-form-item>
          <el-form-item label="验证码" prop="captcha">
            <div class="captcha-row">
              <el-input
                v-model="form.captcha"
                placeholder="验证码"
                size="large"
                :prefix-icon="Picture"
                style="flex: 1; margin-right: 12px"
                maxlength="4"
                @keyup.enter="handleLogin"
              />
              <div class="captcha-code" @click="loadCaptcha">
                {{ captchaCode || '点击获取' }}
              </div>
            </div>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="large" :loading="loading" class="submit-btn" @click="handleLogin">
              登 录
            </el-button>
          </el-form-item>
          <div class="footer-link">
            <router-link to="/register">没有账号？<span>立即注册</span></router-link>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock, Picture } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const formRef = ref(null)
const loading = ref(false)
const captchaCode = ref('')

const form = reactive({
  studentId: '',
  password: '',
  captcha: '',
  captchaKey: ''
})

const rules = {
  studentId: [{ required: true, message: '请输入学号或工号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  captcha: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
}

async function loadCaptcha() {
  const res = await userStore.getCaptcha()
  form.captchaKey = res.data.captchaKey
  captchaCode.value = res.data.captchaCode
}

async function handleLogin() {
  await formRef.value?.validate().catch(() => {})
  if (!form.studentId || !form.password || !form.captcha) return
  loading.value = true
  try {
    await userStore.login({
      studentId: form.studentId,
      password: form.password,
      captcha: form.captcha,
      captchaKey: form.captchaKey
    })
    ElMessage.success('登录成功')
    const redirect = route.query.redirect && typeof route.query.redirect === 'string' ? route.query.redirect : ''
    const want = redirect && (redirect.startsWith('/') ? redirect : '/' + redirect)
    const isAdmin = userStore.userType === 2
    let path = want || (isAdmin ? '/admin/announcement' : '/announcement')
    if (isAdmin && path.indexOf('/admin') !== 0) path = '/admin/announcement'
    if (!isAdmin && path.indexOf('/admin') === 0) path = '/announcement'
    router.push(path)
  } catch (e) {
    loadCaptcha()
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadCaptcha()
})
</script>

<style scoped>
.login-wrap {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
  position: relative;
  overflow: hidden;
  background: linear-gradient(145deg, #0f172a 0%, #1e3a5f 50%, #0c4a6e 100%);
}

.login-bg .shape {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.35;
  animation: float 8s ease-in-out infinite;
}
.shape-1 { width: 400px; height: 400px; background: #38bdf8; top: -100px; left: -100px; animation-delay: 0s; }
.shape-2 { width: 300px; height: 300px; background: #818cf8; bottom: -50px; right: -50px; animation-delay: 2s; }
.shape-3 { width: 200px; height: 200px; background: #22d3ee; top: 40%; left: 30%; animation-delay: 4s; }
.shape-4 { width: 250px; height: 250px; background: #a78bfa; bottom: 30%; right: 20%; animation-delay: 6s; }

@keyframes float {
  0%, 100% { transform: translateY(0px); }
  50% { transform: translateY(-20px); }
}

.login-container {
  position: relative;
  z-index: 1;
  display: flex;
  align-items: center;
  gap: 60px;
  max-width: 900px;
  width: 100%;
}

.login-brand {
  flex: 1;
  color: #fff;
  text-align: center;
}
.brand-icon {
  margin-bottom: 24px;
}
.brand-icon svg {
  width: 80px;
  height: 80px;
}
.brand-title {
  margin: 0 0 12px;
  font-size: 32px;
  font-weight: 800;
  letter-spacing: -0.5px;
  background: linear-gradient(135deg, #fff 0%, #bae6fd 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}
.brand-desc {
  margin: 0;
  font-size: 16px;
  color: rgba(255, 255, 255, 0.6);
  letter-spacing: 8px;
}

.login-card {
  width: 420px;
  flex-shrink: 0;
  padding: 40px;
  background: rgba(255, 255, 255, 0.98);
  border-radius: 20px;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.35);
  backdrop-filter: blur(10px);
}

.card-header {
  text-align: center;
  margin-bottom: 32px;
}
.card-title {
  margin: 0 0 8px;
  font-size: 24px;
  font-weight: 700;
  color: #0f172a;
}
.card-subtitle {
  margin: 0;
  font-size: 14px;
  color: #64748b;
}

.form :deep(.el-form-item__label) {
  font-weight: 500;
  color: #334155;
}

.captcha-row {
  display: flex;
  align-items: center;
  width: 100%;
}
.captcha-code {
  flex-shrink: 0;
  width: 110px;
  height: 40px;
  line-height: 40px;
  text-align: center;
  background: linear-gradient(135deg, #f1f5f9 0%, #e2e8f0 100%);
  border-radius: 8px;
  font-weight: 700;
  letter-spacing: 4px;
  cursor: pointer;
  color: #475569;
  user-select: none;
  border: 1px solid var(--el-border-color);
  transition: all 0.2s;
}
.captcha-code:hover {
  background: linear-gradient(135deg, #e2e8f0 0%, #cbd5e1 100%);
  transform: scale(1.02);
}

.submit-btn {
  width: 100%;
  height: 44px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 10px;
  transition: all 0.3s;
}
.submit-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 6px 20px rgba(14, 165, 233, 0.4);
}

.footer-link {
  text-align: center;
  margin-top: 16px;
}
.footer-link a {
  color: #94a3b8;
  text-decoration: none;
  font-size: 14px;
  transition: color 0.2s;
}
.footer-link a:hover {
  color: #0ea5e9;
}
.footer-link a span {
  color: #0ea5e9;
  font-weight: 500;
}

@media (max-width: 768px) {
  .login-container {
    flex-direction: column;
    gap: 32px;
  }
  .login-brand {
    display: none;
  }
  .login-card {
    width: 100%;
    max-width: 420px;
  }
}
</style>
