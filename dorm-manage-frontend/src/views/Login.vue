<template>
  <div class="login-wrap">
    <!-- Ambient floating shapes -->
    <div class="login-atmosphere">
      <div class="orb orb-1" />
      <div class="orb orb-2" />
      <div class="orb orb-3" />
      <div class="orb orb-4" />
    </div>

    <div class="login-layout">
      <!-- Left: brand illustration -->
      <div class="brand-side">
        <div class="brand-illustration">
          <svg viewBox="0 0 420 400" fill="none" xmlns="http://www.w3.org/2000/svg">
            <!-- Main building structure -->
            <rect x="60" y="100" width="300" height="260" rx="14" fill="rgba(255,255,255,0.06)" stroke="rgba(255,255,255,0.2)" stroke-width="2"/>
            <!-- Roof -->
            <path d="M60 140L210 50L360 140" stroke="rgba(255,255,255,0.35)" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"/>
            <!-- Roof accent -->
            <path d="M210 50L210 80" stroke="rgba(255,255,255,0.15)" stroke-width="2" stroke-linecap="round"/>
            <!-- Windows row 1 -->
            <rect x="90" y="165" width="52" height="42" rx="4" fill="rgba(16,185,129,0.25)" stroke="rgba(16,185,129,0.5)" stroke-width="1.5"/>
            <rect x="158" y="165" width="52" height="42" rx="4" fill="rgba(16,185,129,0.25)" stroke="rgba(16,185,129,0.5)" stroke-width="1.5"/>
            <rect x="226" y="165" width="52" height="42" rx="4" fill="rgba(16,185,129,0.25)" stroke="rgba(16,185,129,0.5)" stroke-width="1.5"/>
            <rect x="294" y="165" width="52" height="42" rx="4" fill="rgba(16,185,129,0.25)" stroke="rgba(16,185,129,0.5)" stroke-width="1.5"/>
            <!-- Windows row 2 -->
            <rect x="90" y="225" width="52" height="42" rx="4" fill="rgba(255,255,255,0.05)" stroke="rgba(255,255,255,0.15)" stroke-width="1.5"/>
            <rect x="158" y="225" width="52" height="42" rx="4" fill="rgba(255,255,255,0.05)" stroke="rgba(255,255,255,0.15)" stroke-width="1.5"/>
            <rect x="226" y="225" width="52" height="42" rx="4" fill="rgba(255,255,255,0.05)" stroke="rgba(255,255,255,0.15)" stroke-width="1.5"/>
            <rect x="294" y="225" width="52" height="42" rx="4" fill="rgba(255,255,255,0.05)" stroke="rgba(255,255,255,0.15)" stroke-width="1.5"/>
            <!-- Windows row 3 -->
            <rect x="90" y="285" width="52" height="42" rx="4" fill="rgba(96,165,250,0.2)" stroke="rgba(96,165,250,0.4)" stroke-width="1.5"/>
            <rect x="158" y="285" width="52" height="42" rx="4" fill="rgba(255,255,255,0.05)" stroke="rgba(255,255,255,0.15)" stroke-width="1.5"/>
            <rect x="226" y="285" width="52" height="42" rx="4" fill="rgba(255,255,255,0.05)" stroke="rgba(255,255,255,0.15)" stroke-width="1.5"/>
            <rect x="294" y="285" width="52" height="42" rx="4" fill="rgba(255,255,255,0.05)" stroke="rgba(255,255,255,0.15)" stroke-width="1.5"/>
            <!-- Door -->
            <rect x="176" y="310" width="68" height="50" rx="4" fill="rgba(16,185,129,0.35)" stroke="rgba(16,185,129,0.6)" stroke-width="1.5"/>
            <!-- Door handle -->
            <circle cx="228" cy="338" r="3" fill="#10b981"/>
            <!-- Building sign -->
            <rect x="140" y="108" width="140" height="28" rx="6" fill="rgba(255,255,255,0.08)" stroke="rgba(255,255,255,0.18)" stroke-width="1"/>
            <text x="210" y="127" text-anchor="middle" fill="rgba(255,255,255,0.4)" font-size="11" font-weight="500" font-family="sans-serif">DORM · 学生公寓</text>
            <!-- Trees left -->
            <circle cx="42" cy="330" r="20" fill="rgba(16,185,129,0.1)" stroke="rgba(16,185,129,0.3)" stroke-width="1.5"/>
            <circle cx="42" cy="330" r="8" fill="rgba(16,185,129,0.2)"/>
            <rect x="38" y="350" width="8" height="20" rx="2" fill="rgba(255,255,255,0.08)"/>
            <!-- Trees right -->
            <circle cx="380" cy="320" r="16" fill="rgba(96,165,250,0.1)" stroke="rgba(96,165,250,0.25)" stroke-width="1.5"/>
            <circle cx="380" cy="320" r="6" fill="rgba(96,165,250,0.18)"/>
            <rect x="377" y="336" width="6" height="16" rx="2" fill="rgba(255,255,255,0.08)"/>
            <!-- Moon/clock circle -->
            <circle cx="345" cy="70" r="18" fill="rgba(255,255,255,0.06)" stroke="rgba(255,255,255,0.2)" stroke-width="1.5"/>
            <circle cx="345" cy="70" r="4" fill="rgba(255,255,255,0.3)"/>
            <path d="M345 54 L345 58 M345 82 L345 86 M329 70 L333 70 M357 70 L361 70" stroke="rgba(255,255,255,0.2)" stroke-width="1.5" stroke-linecap="round"/>
            <!-- Ground line -->
            <line x1="30" y1="360" x2="390" y2="360" stroke="rgba(255,255,255,0.08)" stroke-width="1"/>
            <!-- Decorative dots -->
            <circle cx="100" cy="55" r="2" fill="rgba(16,185,129,0.3)"/>
            <circle cx="310" cy="45" r="2" fill="rgba(16,185,129,0.25)"/>
            <circle cx="200" cy="35" r="1.5" fill="rgba(255,255,255,0.25)"/>
            <circle cx="55" cy="80" r="1.5" fill="rgba(255,255,255,0.2)"/>
            <circle cx="380" cy="90" r="1.5" fill="rgba(255,255,255,0.2)"/>
          </svg>
        </div>
        <h1 class="brand-heading">大学生公寓<br/>管理系统</h1>
        <p class="brand-tagline">安全 · 便捷 · 智能 — 让每一间宿舍都成为温暖的家</p>
        <div class="brand-features">
          <div class="feature-item" v-for="(f, i) in features" :key="i">
            <span class="feature-dot" />
            <span>{{ f }}</span>
          </div>
        </div>
      </div>

      <!-- Right: login card -->
      <div class="login-side">
        <div class="login-card">
          <div class="card-head">
            <div class="card-head-icon">
              <svg viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg">
                <rect x="4" y="10" width="24" height="18" rx="3" fill="rgba(30,58,138,0.12)" stroke="#1e3a8a" stroke-width="1.5"/>
                <path d="M4 14L16 6L28 14" stroke="#1e3a8a" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
                <rect x="13" y="18" width="6" height="10" rx="1.5" fill="rgba(16,185,129,0.25)" stroke="#10b981" stroke-width="1.2"/>
              </svg>
            </div>
            <h2>欢迎回来</h2>
            <p>登录您的账号以继续</p>
          </div>
          <el-form ref="formRef" :model="form" :rules="rules" label-position="top" class="login-form" @submit.prevent="handleLogin">
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
                  maxlength="4"
                  @keyup.enter="handleLogin"
                />
                <div class="captcha-code" @click="loadCaptcha">
                  {{ captchaCode || '获取' }}
                </div>
              </div>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="large" :loading="loading" class="submit-btn" @click="handleLogin">
                登 录
              </el-button>
            </el-form-item>
            <div class="form-footer">
              <router-link to="/register">还没有账号？<span>立即注册</span></router-link>
            </div>
          </el-form>
        </div>
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

const features = ['智能分配宿舍', '在线报修服务', '实时公告通知', '来访登记管理']

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
    let path = want || (isAdmin ? '/admin/dashboard' : '/home')
    if (isAdmin && path.indexOf('/admin') !== 0) path = '/admin/dashboard'
    if (!isAdmin && path.indexOf('/admin') === 0) path = '/home'
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
  position: relative;
  overflow: hidden;
  background: linear-gradient(160deg, #0f172a 0%, #172554 25%, #1e3a8a 55%, #1e40af 80%, #172554 100%);
}

/* ---------- Ambient orbs ---------- */
.login-atmosphere {
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
.orb-1 {
  width: 560px; height: 560px;
  background: radial-gradient(circle, #10b981 0%, transparent 70%);
  top: -200px; left: -180px;
  animation: drift 18s ease-in-out infinite;
}
.orb-2 {
  width: 420px; height: 420px;
  background: radial-gradient(circle, #1e3a8a 0%, transparent 70%);
  bottom: -160px; right: -120px;
  animation: drift 22s ease-in-out 3s infinite;
}
.orb-3 {
  width: 320px; height: 320px;
  background: radial-gradient(circle, #3b82f6 0%, transparent 70%);
  top: 45%; left: 55%;
  animation: drift 20s ease-in-out 6s infinite;
}
.orb-4 {
  width: 380px; height: 380px;
  background: radial-gradient(circle, #10b981 0%, transparent 70%);
  bottom: 15%; left: 8%;
  animation: drift 24s ease-in-out 9s infinite;
}

@keyframes drift {
  0%, 100% { transform: translate(0, 0) scale(1); }
  25% { transform: translate(30px, -40px) scale(1.08); }
  50% { transform: translate(-20px, 20px) scale(0.95); }
  75% { transform: translate(-35px, -15px) scale(1.04); }
}

/* ---------- Layout ---------- */
.login-layout {
  position: relative;
  z-index: 1;
  display: flex;
  align-items: center;
  gap: 60px;
  max-width: 1060px;
  width: 100%;
  padding: 40px;
}

/* ---------- Brand side ---------- */
.brand-side {
  flex: 1;
  color: #fff;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}
.brand-illustration {
  margin-bottom: 28px;
}
.brand-illustration svg {
  width: 300px;
  height: auto;
  filter: drop-shadow(0 8px 32px rgba(16, 185, 129, 0.18));
  animation: float-illustration 6s ease-in-out infinite;
}
@keyframes float-illustration {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-12px); }
}
.brand-heading {
  margin: 0 0 14px;
  font-family: 'Noto Serif SC', serif;
  font-size: 38px;
  font-weight: 900;
  line-height: 1.25;
  letter-spacing: 0.02em;
  background: linear-gradient(180deg, #ffffff 0%, #a7f3d0 45%, #34d399 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}
.brand-tagline {
  margin: 0 0 32px;
  font-size: 14px;
  color: rgba(255, 255, 255, 0.4);
  letter-spacing: 0.05em;
  max-width: 320px;
}
.brand-features {
  display: flex;
  flex-direction: column;
  gap: 12px;
  align-items: center;
}
.feature-item {
  display: flex;
  align-items: center;
  gap: 10px;
  color: rgba(255, 255, 255, 0.45);
  font-size: 14px;
  transition: color 0.3s ease;
}
.feature-item:hover {
  color: rgba(255, 255, 255, 0.7);
}
.feature-dot {
  width: 5px;
  height: 5px;
  border-radius: 50%;
  background: #10b981;
  box-shadow: 0 0 10px rgba(16, 185, 129, 0.6), 0 0 20px rgba(16, 185, 129, 0.3);
  animation: dot-pulse 2s ease-in-out infinite;
}
.feature-item:nth-child(2) .feature-dot { animation-delay: 0.4s; }
.feature-item:nth-child(3) .feature-dot { animation-delay: 0.8s; }
.feature-item:nth-child(4) .feature-dot { animation-delay: 1.2s; }
@keyframes dot-pulse {
  0%, 100% { opacity: 0.5; }
  50% { opacity: 1; }
}

/* ---------- Login card ---------- */
.login-side {
  flex-shrink: 0;
  width: 420px;
}
.login-card {
  padding: 44px 40px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 24px;
  border: 1px solid rgba(255, 255, 255, 0.15);
  box-shadow: 0 8px 40px rgba(0, 0, 0, 0.3), inset 0 1px 0 rgba(255, 255, 255, 0.08);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
}
.card-head {
  text-align: center;
  margin-bottom: 36px;
}
.card-head-icon {
  width: 52px;
  height: 52px;
  margin: 0 auto 18px;
  background: rgba(255, 255, 255, 0.08);
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid rgba(255, 255, 255, 0.12);
}
.card-head-icon svg {
  width: 28px;
  height: 28px;
}
.card-head h2 {
  margin: 0 0 6px;
  font-family: 'Noto Serif SC', serif;
  font-size: 26px;
  font-weight: 700;
  color: #ffffff;
}
.card-head p {
  margin: 0;
  font-size: 14px;
  color: rgba(255, 255, 255, 0.4);
}

/* ---------- Form ---------- */
.login-form :deep(.el-form-item__label) {
  font-weight: 500;
  color: rgba(255, 255, 255, 0.7);
  font-size: 13px;
  letter-spacing: 0.02em;
}
.login-form :deep(.el-input__wrapper) {
  background: rgba(255, 255, 255, 0.07);
  border: 1px solid rgba(255, 255, 255, 0.12);
  border-radius: 10px !important;
  box-shadow: none;
  transition: all 0.3s ease;
}
.login-form :deep(.el-input__wrapper:hover) {
  border-color: rgba(16, 185, 129, 0.35);
  background: rgba(255, 255, 255, 0.1);
}
.login-form :deep(.el-input__wrapper.is-focus) {
  border-color: #10b981;
  background: rgba(255, 255, 255, 0.12);
  box-shadow: 0 0 0 3px rgba(16, 185, 129, 0.12);
}
.login-form :deep(.el-input__inner) {
  color: #ffffff;
  font-size: 15px;
}
.login-form :deep(.el-input__inner::placeholder) {
  color: rgba(255, 255, 255, 0.28);
}
.login-form :deep(.el-input__prefix .el-icon) {
  color: rgba(255, 255, 255, 0.4);
}
.login-form :deep(.el-input__suffix .el-icon) {
  color: rgba(255, 255, 255, 0.4);
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
  background: rgba(16, 185, 129, 0.15);
  border: 1px solid rgba(16, 185, 129, 0.25);
  border-radius: 10px;
  font-weight: 700;
  letter-spacing: 3px;
  cursor: pointer;
  color: #34d399;
  user-select: none;
  transition: all 0.3s ease;
  font-size: 16px;
}
.captcha-code:hover {
  background: rgba(16, 185, 129, 0.25);
  border-color: rgba(16, 185, 129, 0.45);
  transform: scale(1.04);
}

.submit-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 700;
  border-radius: 12px;
  border: none;
  background: linear-gradient(135deg, #1e3a8a 0%, #059669 100%);
  transition: all 0.35s cubic-bezier(0.16, 1, 0.3, 1);
  letter-spacing: 3px;
  margin-top: 4px;
}
.submit-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 28px rgba(16, 185, 129, 0.4);
  background: linear-gradient(135deg, #1e40af 0%, #10b981 100%);
}
.submit-btn:active {
  transform: translateY(0);
}

.form-footer {
  text-align: center;
  margin-top: 4px;
}
.form-footer a {
  color: rgba(255, 255, 255, 0.35);
  text-decoration: none;
  font-size: 14px;
  transition: color 0.2s;
}
.form-footer a:hover {
  color: rgba(255, 255, 255, 0.6);
}
.form-footer a span {
  color: #34d399;
  font-weight: 600;
}

@media (max-width: 768px) {
  .login-layout {
    flex-direction: column;
    gap: 36px;
    padding: 24px;
  }
  .brand-side {
    display: none;
  }
  .login-side {
    width: 100%;
    max-width: 420px;
  }
  .login-card {
    padding: 36px 28px;
  }
}
</style>
