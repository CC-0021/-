<template>
  <div class="register-wrap">
    <div class="register-bg">
      <div class="shape shape-1" />
      <div class="shape shape-2" />
      <div class="shape shape-3" />
    </div>
    <div class="register-container">
      <div class="register-brand">
        <div class="brand-icon">
          <svg viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
            <rect x="4" y="16" width="40" height="28" rx="4" fill="white" fill-opacity="0.2" stroke="white" stroke-width="2"/>
            <path d="M4 20L24 8L44 20" stroke="white" stroke-width="2" stroke-linecap="round"/>
            <rect x="18" y="28" width="12" height="16" rx="2" fill="white" fill-opacity="0.3" stroke="white" stroke-width="1.5"/>
          </svg>
        </div>
        <h1 class="brand-title">大学生公寓管理系统</h1>
        <p class="brand-desc">加入我们</p>
      </div>
      <div class="register-card">
        <div class="card-header">
          <h2 class="card-title">学生注册</h2>
          <p class="card-subtitle">创建您的账号</p>
        </div>
        <el-form ref="formRef" :model="form" :rules="rules" label-position="top" class="form">
          <el-form-item label="学号" prop="studentId">
            <el-input v-model="form.studentId" placeholder="4-32位" size="large" :prefix-icon="User" clearable />
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input v-model="form.password" type="password" placeholder="6-20位" size="large" :prefix-icon="Lock" show-password clearable />
          </el-form-item>
          <el-form-item label="确认密码" prop="confirmPassword">
            <el-input v-model="form.confirmPassword" type="password" placeholder="再次输入密码" size="large" :prefix-icon="Lock" show-password clearable />
          </el-form-item>
          <el-form-item label="姓名（选填）" prop="realName">
            <el-input v-model="form.realName" placeholder="真实姓名" size="large" :prefix-icon="UserFilled" clearable />
          </el-form-item>
          <el-form-item label="联系电话（选填）" prop="phone">
            <el-input v-model="form.phone" placeholder="手机号" size="large" :prefix-icon="Phone" clearable />
          </el-form-item>
          <el-form-item label="验证码" prop="captcha">
            <div class="captcha-row">
              <el-input v-model="form.captcha" placeholder="验证码" size="large" :prefix-icon="Picture" style="flex:1;margin-right:12px" maxlength="4" />
              <div class="captcha-code" @click="loadCaptcha">{{ captchaCode || '点击获取' }}</div>
            </div>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="large" :loading="loading" class="submit-btn" @click="handleRegister">注 册</el-button>
          </el-form-item>
          <div class="footer-link">
            <router-link to="/login">已有账号？<span>去登录</span></router-link>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock, UserFilled, Phone, Picture } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { register as registerApi } from '@/api/auth'

const router = useRouter()
const userStore = useUserStore()

const formRef = ref(null)
const loading = ref(false)
const captchaCode = ref('')

const form = reactive({
  studentId: '',
  password: '',
  confirmPassword: '',
  realName: '',
  phone: '',
  captcha: '',
  captchaKey: ''
})

const validateConfirm = (rule, value, callback) => {
  if (value !== form.password) callback(new Error('两次密码输入不一致'))
  else callback()
}

const rules = {
  studentId: [
    { required: true, message: '请输入学号', trigger: 'blur' },
    { min: 4, max: 32, message: '学号长度4-32位', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度6-20位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    { validator: validateConfirm, trigger: 'blur' }
  ],
  captcha: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
}

async function loadCaptcha() {
  const res = await userStore.getCaptcha()
  form.captchaKey = res.data.captchaKey
  captchaCode.value = res.data.captchaCode
}

async function handleRegister() {
  await formRef.value?.validate().catch(() => {})
  if (!form.studentId || !form.password || form.password !== form.confirmPassword || !form.captcha) return
  loading.value = true
  try {
    await registerApi(form)
    ElMessage.success('注册成功，请登录')
    router.push('/login')
  } catch (e) {
    loadCaptcha()
  } finally {
    loading.value = false
  }
}

onMounted(() => loadCaptcha())
</script>

<style scoped>
.register-wrap {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
  position: relative;
  overflow: hidden;
  background: linear-gradient(145deg, #0f172a 0%, #1e3a5f 50%, #0c4a6e 100%);
}
.register-bg .shape {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.35;
  animation: float 8s ease-in-out infinite;
}
.shape-1 { width: 400px; height: 400px; background: #38bdf8; top: -100px; right: -100px; animation-delay: 0s; }
.shape-2 { width: 300px; height: 300px; background: #818cf8; bottom: -50px; left: -50px; animation-delay: 2s; }
.shape-3 { width: 200px; height: 200px; background: #22d3ee; top: 50%; left: 40%; animation-delay: 4s; }

@keyframes float {
  0%, 100% { transform: translateY(0px); }
  50% { transform: translateY(-20px); }
}

.register-container {
  position: relative;
  z-index: 1;
  display: flex;
  align-items: center;
  gap: 60px;
  max-width: 900px;
  width: 100%;
}

.register-brand {
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

.register-card {
  width: 420px;
  flex-shrink: 0;
  padding: 40px;
  background: rgba(255, 255, 255, 0.98);
  border-radius: 20px;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.35);
  backdrop-filter: blur(10px);
}
.card-header { text-align: center; margin-bottom: 28px; }
.card-title { margin: 0 0 8px; font-size: 24px; font-weight: 700; color: #0f172a; }
.card-subtitle { margin: 0; font-size: 14px; color: #64748b; }
.form :deep(.el-form-item__label) { font-weight: 500; color: #334155; }
.captcha-row { display: flex; align-items: center; width: 100%; }
.captcha-code {
  flex-shrink: 0; width: 110px; height: 40px; line-height: 40px; text-align: center;
  background: linear-gradient(135deg, #f1f5f9 0%, #e2e8f0 100%); border-radius: 8px;
  font-weight: 700; letter-spacing: 4px; cursor: pointer; color: #475569; user-select: none;
  border: 1px solid var(--el-border-color); transition: all 0.2s;
}
.captcha-code:hover {
  background: linear-gradient(135deg, #e2e8f0 0%, #cbd5e1 100%);
  transform: scale(1.02);
}
.submit-btn {
  width: 100%; height: 44px; font-size: 16px; font-weight: 600;
  border-radius: 10px; transition: all 0.3s;
}
.submit-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 6px 20px rgba(14, 165, 233, 0.4);
}
.footer-link { text-align: center; margin-top: 16px; }
.footer-link a { color: #94a3b8; text-decoration: none; font-size: 14px; transition: color 0.2s; }
.footer-link a:hover { color: #0ea5e9; }
.footer-link a span { color: #0ea5e9; font-weight: 500; }

@media (max-width: 768px) {
  .register-container {
    flex-direction: column;
    gap: 32px;
  }
  .register-brand {
    display: none;
  }
  .register-card {
    width: 100%;
    max-width: 420px;
  }
}
</style>
