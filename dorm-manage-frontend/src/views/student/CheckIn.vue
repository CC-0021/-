<template>
  <div class="page-container student-page">
    <div class="page-header">
      <h2 class="page-title">入住登记</h2>
      <el-button type="primary" @click="dialogVisible = true">
        <el-icon><Plus /></el-icon>提交登记
      </el-button>
    </div>
    <el-table :data="list" stripe>
      <el-table-column prop="roomDisplay" label="拟入住房间" />
      <el-table-column prop="checkInTime" label="拟入住时间" width="180" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <span class="status-badge" :class="['warning', 'success', 'danger'][row.status]">
            <span class="dot" />{{ ['待审核', '审核通过', '审核驳回'][row.status] }}
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="rejectReason" label="驳回理由" show-overflow-tooltip />
    </el-table>
    <el-dialog v-model="dialogVisible" title="入住登记" width="500px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="手机号" />
        </el-form-item>
        <el-form-item label="拟入住房间" prop="roomDisplay">
          <el-input v-model="form.roomDisplay" placeholder="由管理员分配后显示" />
        </el-form-item>
        <el-form-item label="拟入住时间" prop="checkInTime">
          <el-date-picker v-model="form.checkInTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" placeholder="选择日期时间" />
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
import { myList, submit as submitApi } from '@/api/checkin'

const list = ref([])
const dialogVisible = ref(false)
const formRef = ref(null)
const form = reactive({ phone: '', roomDisplay: '', checkInTime: '' })
const rules = { phone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }] }

async function load() {
  const res = await myList()
  list.value = res.data || []
}

async function submit() {
  await formRef.value?.validate().catch(() => {})
  if (!form.phone) return
  await submitApi(form)
  ElMessage.success('提交成功')
  dialogVisible.value = false
  load()
}

onMounted(load)
</script>
