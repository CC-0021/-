<template>
  <div class="register-wrap">
    <div class="register-bg">
      <div class="shape shape-1" />
      <div class="shape shape-2" />
    </div>
    <div class="register-card">
      <div class="card-header">
        <h1 class="title">学生注册</h1>
        <p class="subtitle">创建您的账号</p>
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
          <router-link to="/login">已有账号？去登录</router-link>
        </div>
      </el-form>
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
  opacity: 0.4;
}
.shape-1 { width: 400px; height: 400px; background: #38bdf8; top: -100px; right: -100px; }
.shape-2 { width: 300px; height: 300px; background: #818cf8; bottom: -50px; left: -50px; }

.register-card {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 420px;
  padding: 40px;
  background: rgba(255, 255, 255, 0.98);
  border-radius: 20px;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.35);
  backdrop-filter: blur(10px);
}
.card-header { text-align: center; margin-bottom: 28px; }
.title { margin: 0 0 8px; font-size: 24px; font-weight: 700; color: #0f172a; }
.subtitle { margin: 0; font-size: 14px; color: #64748b; }
.form :deep(.el-form-item__label) { font-weight: 500; color: #334155; }
.captcha-row { display: flex; align-items: center; width: 100%; }
.captcha-code {
  flex-shrink: 0; width: 110px; height: 40px; line-height: 40px; text-align: center;
  background: linear-gradient(135deg, #f1f5f9 0%, #e2e8f0 100%); border-radius: 8px;
  font-weight: 700; letter-spacing: 4px; cursor: pointer; color: #475569; user-select: none;
  border: 1px solid var(--el-border-color);
}
.captcha-code:hover { background: linear-gradient(135deg, #e2e8f0 0%, #cbd5e1 100%); }
.submit-btn { width: 100%; height: 44px; font-size: 16px; font-weight: 600; }
.footer-link { text-align: center; margin-top: 16px; }
.footer-link a { color: #0ea5e9; text-decoration: none; font-size: 14px; }
.footer-link a:hover { text-decoration: underline; }
</style>
