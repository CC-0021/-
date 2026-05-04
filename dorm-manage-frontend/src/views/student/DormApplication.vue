<template>
  <div class="page-container student-page">
    <div class="page-header">
      <h2 class="page-title">宿舍申请</h2>
      <el-button type="primary" @click="dialogVisible = true">
        <el-icon><Plus /></el-icon>提交申请
      </el-button>
    </div>
    <el-table :data="list" stripe>
      <el-table-column prop="applicationType" label="申请类型" width="100" />
      <el-table-column prop="reason" label="申请理由" show-overflow-tooltip />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <span class="status-badge" :class="['warning', 'success', 'danger'][row.status]">
            <span class="dot" />{{ ['待审核', '审核通过', '审核驳回'][row.status] }}
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="rejectReason" label="驳回理由" show-overflow-tooltip />
    </el-table>
    <el-dialog v-model="dialogVisible" title="宿舍申请" width="500px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="申请类型" prop="applicationType">
          <el-select v-model="form.applicationType" placeholder="请选择" style="width: 100%">
            <el-option label="换宿舍" value="换宿舍" />
            <el-option label="入住" value="入住" />
            <el-option label="退宿" value="退宿" />
          </el-select>
        </el-form-item>
        <el-form-item label="原宿舍号" prop="originalRoom">
          <el-input v-model="form.originalRoom" placeholder="选填" />
        </el-form-item>
        <el-form-item label="目标宿舍号" prop="targetRoom">
          <el-input v-model="form.targetRoom" placeholder="选填" />
        </el-form-item>
        <el-form-item label="申请理由" prop="reason">
          <el-input v-model="form.reason" type="textarea" :rows="3" placeholder="至少20字" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submit">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { myList, submit as submitApi } from '@/api/dormApplication'

const list = ref([])
const dialogVisible = ref(false)
const formRef = ref(null)
const form = reactive({ applicationType: '', originalRoom: '', targetRoom: '', reason: '' })
const rules = {
  applicationType: [{ required: true, message: '请选择类型', trigger: 'change' }],
  reason: [
    { required: true, message: '请输入理由', trigger: 'blur' },
    { min: 20, message: '申请理由至少20字', trigger: 'blur' }
  ]
}

async function load() {
  const res = await myList()
  list.value = res.data || []
}

async function submit() {
  await formRef.value?.validate().catch(() => {})
  if (!form.applicationType || !form.reason || form.reason.length < 20) {
    ElMessage.warning('申请理由至少20字')
    return
  }
  await submitApi(form)
  ElMessage.success('提交成功')
  dialogVisible.value = false
  load()
}

onMounted(load)
</script>
