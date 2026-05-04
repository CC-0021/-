<template>
  <div class="page-container student-page">
    <div class="page-header">
      <h2 class="page-title">留言</h2>
      <el-button type="primary" @click="dialogVisible = true">
        <el-icon><Plus /></el-icon>发起留言
      </el-button>
    </div>
    <el-table :data="list" stripe>
      <el-table-column prop="title" label="标题" width="200" />
      <el-table-column prop="content" label="内容" show-overflow-tooltip />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <span class="status-badge" :class="row.status === 1 ? 'success' : 'info'">
            <span class="dot" />{{ row.status === 1 ? '已回复' : '未回复' }}
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="replyContent" label="回复内容" show-overflow-tooltip />
      <el-table-column prop="createTime" label="提交时间" width="180" />
    </el-table>
    <el-dialog v-model="dialogVisible" title="发起留言" width="500px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="留言标题" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input v-model="form.content" type="textarea" :rows="3" placeholder="留言内容" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="选填" />
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
import { myList, add } from '@/api/message'

const list = ref([])
const dialogVisible = ref(false)
const formRef = ref(null)
const form = reactive({ title: '', content: '', phone: '' })
const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }]
}

async function load() {
  const res = await myList()
  list.value = res.data || []
}

async function submit() {
  await formRef.value?.validate().catch(() => {})
  if (!form.title || !form.content) return
  await add(form)
  ElMessage.success('提交成功')
  dialogVisible.value = false
  load()
}

onMounted(load)
</script>
