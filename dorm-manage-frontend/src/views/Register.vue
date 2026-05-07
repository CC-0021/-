<template>
  <div class="register-wrap">
    <div class="register-atmosphere">
      <div class="orb orb-1" />
      <div class="orb orb-2" />
      <div class="orb orb-3" />
    </div>
    <div class="register-layout">
      <div class="brand-side">
        <div class="brand-mark">
          <svg viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
            <rect x="4" y="16" width="40" height="28" rx="4" fill="white" fill-opacity="0.2" stroke="white" stroke-width="2"/>
            <path d="M4 20L24 8L44 20" stroke="white" stroke-width="2" stroke-linecap="round"/>
            <rect x="18" y="28" width="12" height="16" rx="2" fill="white" fill-opacity="0.3" stroke="white" stroke-width="1.5"/>
          </svg>
        </div>
        <h1 class="brand-heading">大学生公寓<br/>管理系统</h1>
        <p class="brand-tagline">加入我们，开启温馨的宿舍生活</p>
      </div>
      <div class="register-side">
        <div class="register-card">
          <div class="card-head">
            <h2>学生注册</h2>
            <p>创建您的账号</p>
          </div>
          <el-form ref="formRef" :model="form" :rules="rules" label-position="top" class="form" @submit.prevent="handleRegister">
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
                <el-input v-model="form.captcha" placeholder="验证码" size="large" :prefix-icon="Picture" maxlength="4" />
                <div class="captcha-code" @click="loadCaptcha">{{ captchaCode || '获取' }}</div>
              </div>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="large" :loading="loading" class="submit-btn" @click="handleRegister">注 册</el-button>
            </el-form-item>
            <div class="form-footer">
              <router-link to="/login">已有账号？<span>去登录</span></router-link>
            </div>
          </el-form>
        </div>
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
  position: relative;
  overflow: hidden;
  background: linear-gradient(160deg, #0f172a 0%, #1e3a5f 40%, #0c4a6e 70%, #0f172a 100%);
}
.register-atmosphere {
  position: absolute;
  inset: 0;
  overflow: hidden;
  pointer-events: none;
}
.orb {
  position: absolute;
  border-radius: 50%;
  filter: blur(100px);
  opacity: 0.25;
}
.orb-1 { width: 420px; height: 420px; background: radial-gradient(circle, #38bdf8, transparent 70%); top: -120px; right: -80px; animation: drift 16s ease-in-out infinite; }
.orb-2 { width: 340px; height: 340px; background: radial-gradient(circle, #818cf8, transparent 70%); bottom: -100px; left: -60px; animation: drift 20s ease-in-out 3s infinite; }
.orb-3 { width: 260px; height: 260px; background: radial-gradient(circle, #22d3ee, transparent 70%); top: 50%; left: 35%; animation: drift 18s ease-in-out 6s infinite; }
@keyframes drift {
  0%, 100% { transform: translate(0, 0) scale(1); }
  25% { transform: translate(20px, -25px) scale(1.06); }
  50% { transform: translate(-15px, 15px) scale(0.96); }
  75% { transform: translate(-25px, -10px) scale(1.03); }
}
.register-layout {
  position: relative;
  z-index: 1;
  display: flex;
  align-items: center;
  gap: 60px;
  max-width: 960px;
  width: 100%;
  padding: 40px;
}
.brand-side {
  flex: 1;
  color: #fff;
  text-align: center;
}
.brand-mark svg {
  width: 80px;
  height: 80px;
  margin-bottom: 24px;
  filter: drop-shadow(0 4px 16px rgba(56, 189, 248, 0.2));
}
.brand-heading {
  margin: 0 0 12px;
  font-family: 'Noto Serif SC', serif;
  font-size: 36px;
  font-weight: 900;
  line-height: 1.3;
  background: linear-gradient(180deg, #ffffff 0%, #bae6fd 60%, #38bdf8 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}
.brand-tagline {
  margin: 0;
  font-size: 14px;
  color: rgba(255, 255, 255, 0.45);
  letter-spacing: 0.05em;
}
.register-side {
  flex-shrink: 0;
  width: 420px;
}
.register-card {
  padding: 40px;
  background: rgba(255, 255, 255, 0.97);
  border-radius: var(--radius-2xl);
  box-shadow: 0 25px 60px rgba(0, 0, 0, 0.3);
}
.card-head {
  text-align: center;
  margin-bottom: 28px;
}
.card-head h2 {
  margin: 0 0 6px;
  font-family: 'Noto Serif SC', serif;
  font-size: 24px;
  font-weight: 700;
  color: var(--neutral-900);
}
.card-head p {
  margin: 0;
  font-size: 14px;
  color: var(--neutral-400);
}
.form :deep(.el-form-item__label) {
  font-weight: 500;
  color: var(--neutral-600);
}
.captcha-row {
  display: flex;
  align-items: center;
  gap: 12px;
}
.captcha-code {
  flex-shrink: 0;
  width: 100px;
  height: 40px;
  line-height: 40px;
  text-align: center;
  background: var(--neutral-100);
  border-radius: 8px;
  font-weight: 700;
  letter-spacing: 3px;
  cursor: pointer;
  color: var(--neutral-700);
  user-select: none;
  border: 1px solid var(--neutral-200);
  transition: all 0.2s;
}
.captcha-code:hover {
  background: var(--neutral-200);
  transform: scale(1.03);
}
.submit-btn {
  width: 100%;
  height: 46px;
  font-size: 16px;
  font-weight: 700;
  border-radius: 12px;
  letter-spacing: 2px;
  transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);
}
.submit-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(14, 165, 233, 0.35);
}
.form-footer {
  text-align: center;
  margin-top: 4px;
}
.form-footer a {
  color: var(--neutral-400);
  text-decoration: none;
  font-size: 14px;
  transition: color 0.2s;
}
.form-footer a:hover { color: #0ea5e9; }
.form-footer a span { color: #0ea5e9; font-weight: 600; }

@media (max-width: 768px) {
  .register-layout { flex-direction: column; gap: 32px; padding: 24px; }
  .brand-side { display: none; }
  .register-side { width: 100%; max-width: 420px; }
  .register-card { padding: 32px 24px; }
}
</style>
