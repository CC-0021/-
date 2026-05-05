<template>
  <div class="page-container student-page">
    <div class="page-header">
      <h2 class="page-title">提交报修</h2>
      <el-button @click="router.push('/repair')">返回列表</el-button>
    </div>
    <div class="form-card">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" style="max-width: 640px">
        <el-form-item label="报修类型" prop="repairType">
          <el-select v-model="form.repairType" placeholder="请选择" style="width: 100%" size="large">
            <el-option label="水电故障" value="水电故障" />
            <el-option label="家具损坏" value="家具损坏" />
            <el-option label="网络问题" value="网络问题" />
            <el-option label="卫生问题" value="卫生问题" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="宿舍号" prop="roomNo">
          <el-input v-model="form.roomNo" placeholder="如 A栋-101" size="large" />
        </el-form-item>
        <el-form-item label="问题描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="4" placeholder="请详细描述问题" size="large" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="手机号" size="large" />
        </el-form-item>
        <el-form-item label="上传图片">
          <el-upload
            action="/api/upload/image"
            :headers="{ 'Authorization': localStorage && localStorage.getItem('token') ? 'Bearer ' + localStorage.getItem('token') : '' }"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :limit="3"
            :file-list="fileList"
            list-type="picture-card"
            :auto-upload="true"
            ref="uploadRef"
          >
            <template #default>
              <el-icon class="upload-icon"><Plus /></el-icon>
            </template>
            <template #tip>
              <div class="upload-tip">最多上传3张图片，支持jpg、jpeg、png格式</div>
            </template>
          </el-upload>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="submit">提交</el-button>
          <el-button @click="router.push('/repair')">取消</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { submit as submitRepair } from '@/api/repair'

const router = useRouter()
const formRef = ref(null)
const uploadRef = ref(null)
const loading = ref(false)
const form = reactive({ repairType: '', roomNo: '', description: '', phone: '', images: '' })
const fileList = ref([])
const imageUrls = ref([])

const rules = {
  repairType: [{ required: true, message: '请选择报修类型', trigger: 'change' }],
  roomNo: [{ required: true, message: '请输入宿舍号', trigger: 'blur' }],
  description: [{ required: true, message: '请输入问题描述', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }]
}

function handleUploadSuccess(response, file, uploadFileList) {
  if (response.code === 200) {
    imageUrls.value.push(response.data)
    form.images = imageUrls.value.join(',')
    ElMessage.success('图片上传成功')
  } else {
    ElMessage.error('图片上传失败：' + response.msg)
  }
}

function handleUploadError(error) {
  ElMessage.error('图片上传失败：' + error.message)
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
  background: #fff;
  border-radius: 18px;
  padding: 40px;
  border: 1px solid rgba(249, 115, 22, 0.1);
  box-shadow: 0 4px 24px rgba(249, 115, 22, 0.06);
}
.upload-icon {
  font-size: 28px;
  color: #94a3b8;
}
.upload-tip {
  font-size: 13px;
  color: #64748b;
  margin-top: 10px;
}
</style>
