<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">提交报修</h2>
      <el-button @click="router.push('/repair')">返回</el-button>
    </div>
    <el-card shadow="never" class="form-card">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" style="max-width: 520px">
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
            class="upload-demo"
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
              <el-icon class="avatar-uploader-icon"><Plus /></el-icon>
            </template>
            <template #file="{ file }">
              <div class="upload-file-item">
                <img :src="file.url" class="upload-file-image" />
                <div class="upload-file-actions">
                  <span class="upload-file-name">{{ file.name }}</span>
                  <el-button type="text" size="small" @click.stop="handleRemove(file)">
                    <el-icon><Delete /></el-icon>
                  </el-button>
                </div>
              </div>
            </template>
            <template #tip>
              <div class="el-upload__tip">
                最多上传3张图片，支持jpg、jpeg、png格式
              </div>
            </template>
          </el-upload>
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
import { Plus, Delete } from '@element-plus/icons-vue'
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

function handleUploadSuccess(response, file, fileList) {
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

function handleRemove(file) {
  const index = fileList.value.findIndex(item => item.uid === file.uid)
  if (index > -1) {
    fileList.value.splice(index, 1)
    // 从imageUrls中移除对应的URL
    const urlIndex = imageUrls.value.indexOf(file.url)
    if (urlIndex > -1) {
      imageUrls.value.splice(urlIndex, 1)
      form.images = imageUrls.value.join(',')
    }
  }
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
  transition: all var(--transition-normal);
}

.form-card:hover {
  box-shadow: var(--student-card-shadow-hover);
}

.avatar-uploader-icon {
  width: 100px;
  height: 100px;
  font-size: 24px;
  color: #909399;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px dashed #d9d9d9;
  border-radius: 8px;
  transition: all var(--transition-fast);
}

.avatar-uploader-icon:hover {
  border-color: var(--student-primary);
  color: var(--student-primary);
}

.upload-file-item {
  position: relative;
  border-radius: 8px;
  overflow: hidden;
  transition: all var(--transition-fast);
}

.upload-file-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.upload-file-image {
  width: 100%;
  height: 100px;
  object-fit: cover;
}

.upload-file-actions {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: rgba(0, 0, 0, 0.6);
  color: #fff;
  padding: 8px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  opacity: 0;
  transition: all var(--transition-fast);
}

.upload-file-item:hover .upload-file-actions {
  opacity: 1;
}

.upload-file-name {
  font-size: 12px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  flex: 1;
  margin-right: 8px;
}

.el-upload__tip {
  font-size: 12px;
  color: #64748b;
  margin-top: 8px;
}
</style>