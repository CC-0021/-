<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">提交报修</h2>
      <el-button @click="router.push('/repair')">返回</el-button>
    </div>
    <el-card shadow="never" class="form-card">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" style="max-width: 520px">
        <el-form-item label="报修类型" prop="repairType">
          <el-select v-model="form.repairType" placeholder="请选择" style="width: 100%">
            <el-option label="水电故障" value="水电故障" />
            <el-option label="家具损坏" value="家具损坏" />
            <el-option label="网络问题" value="网络问题" />
            <el-option label="卫生问题" value="卫生问题" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="宿舍号" prop="roomNo">
          <el-input v-model="form.roomNo" placeholder="如 A栋-101" />
        </el-form-item>
        <el-form-item label="问题描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="4" placeholder="请详细描述问题" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="手机号" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="submit">提交</el-button>
          <el-button @click="router.push('/repair')">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { submit as submitRepair } from '@/api/repair'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)
const form = reactive({ repairType: '', roomNo: '', description: '', phone: '', images: '' })

const rules = {
  repairType: [{ required: true, message: '请选择报修类型', trigger: 'change' }],
  roomNo: [{ required: true, message: '请输入宿舍号', trigger: 'blur' }],
  description: [{ required: true, message: '请输入问题描述', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }]
}

async function submit() {
  await formRef.value?.validate().catch(() => {})
  if (!form.repairType || !form.roomNo || !form.description || !form.phone) return
  loading.value = true
  try {
    await submitRepair(form)
    ElMessage.success('提交成功')
    router.push('/repair')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.form-card {
  max-width: 600px;
  border-radius: var(--card-radius);
}
</style>
