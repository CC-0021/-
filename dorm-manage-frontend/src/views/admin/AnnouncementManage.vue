<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">公告管理</h2>
      <el-button type="primary" @click="openEdit()">
        <el-icon><Plus /></el-icon>新增公告
      </el-button>
    </div>

    <div class="stat-cards">
      <div class="stat-card" style="background: linear-gradient(135deg, #6366f1, #818cf8)">
        <div class="stat-value">{{ total }}</div>
        <div class="stat-label">公告总数</div>
        <el-icon class="stat-icon"><Notification /></el-icon>
      </div>
      <div class="stat-card" style="background: linear-gradient(135deg, #10b981, #34d399)">
        <div class="stat-value">{{ publishedCount }}</div>
        <div class="stat-label">已发布</div>
        <el-icon class="stat-icon"><CircleCheck /></el-icon>
      </div>
      <div class="stat-card" style="background: linear-gradient(135deg, #f59e0b, #fbbf24)">
        <div class="stat-value">{{ draftCount }}</div>
        <div class="stat-label">草稿</div>
        <el-icon class="stat-icon"><EditPen /></el-icon>
      </div>
    </div>

    <el-table :data="list" stripe>
      <el-table-column prop="title" label="标题" min-width="200" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <span class="status-badge" :class="{ 0: 'warning', 1: 'success', 2: 'info' }[row.status]">
            <span class="dot" />{{ statusMap[row.status] }}
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="isTop" label="置顶" width="80">
        <template #default="{ row }">
          <el-tag v-if="row.isTop === 1" size="small" type="danger">置顶</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="publishTime" label="发布时间" width="180" />
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button type="primary" link size="small" @click="openEdit(row)">编辑</el-button>
          <el-button v-if="row.status === 0" type="danger" link size="small" @click="remove(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-wrap">
      <el-pagination
        v-model:current-page="pageNum"
        :page-size="pageSize"
        :total="total"
        layout="total, prev, pager, next"
        @current-change="load"
      />
    </div>

    <el-dialog v-model="editVisible" :title="editId ? '编辑公告' : '新增公告'" width="600px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="公告标题" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input v-model="form.content" type="textarea" :rows="5" placeholder="公告内容" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择" style="width: 120px">
            <el-option label="草稿" value="0" />
            <el-option label="已发布" value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="置顶">
          <el-switch v-model="form.isTop" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editVisible = false">取消</el-button>
        <el-button type="primary" :loading="saveLoading" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Notification, CircleCheck, EditPen } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { adminPage, add, update, remove as removeApi } from '@/api/announcement'

const userStore = useUserStore()
const list = ref([])
const pageNum = ref(1)
const pageSize = 10
const total = ref(0)
const editVisible = ref(false)
const editId = ref(null)
const formRef = ref(null)
const saveLoading = ref(false)
const form = reactive({ title: '', content: '', status: '1', isTop: 0, publisherId: '', publisherName: '', publishTime: '' })

const statusMap = { 0: '草稿', 1: '已发布', 2: '已撤回' }
const publishedCount = computed(() => list.value.filter(r => r.status === 1).length)
const draftCount = computed(() => list.value.filter(r => r.status === 0).length)

const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }]
}

async function load() {
  const res = await adminPage({ pageNum: pageNum.value, pageSize })
  list.value = res.data?.list || []
  total.value = res.data?.total || 0
}

function openEdit(row) {
  editId.value = row?.id || null
  if (row) {
    form.title = row.title
    form.content = row.content || ''
    form.status = row.status?.toString() || '1'
    form.isTop = row.isTop ?? 0
    form.publisherId = row.publisherId
    form.publisherName = row.publisherName
    form.publishTime = row.publishTime
    form.id = row.id
  } else {
    form.title = ''
    form.content = ''
    form.status = '1'
    form.isTop = 0
    form.publisherId = userStore.userId
    form.publisherName = userStore.displayName
    form.publishTime = new Date().toISOString().slice(0, 19).replace('T', ' ')
    delete form.id
  }
  editVisible.value = true
}

async function save() {
  await formRef.value?.validate().catch(() => {})
  saveLoading.value = true
  try {
    const formData = { ...form, status: parseInt(form.status), id: editId.value }
    if (editId.value) {
      await update(formData)
      ElMessage.success('更新成功')
    } else {
      await add(formData)
      ElMessage.success('新增成功')
    }
    editVisible.value = false
    load()
  } finally {
    saveLoading.value = false
  }
}

async function remove(id) {
  await ElMessageBox.confirm('确定删除该公告？', '提示', { type: 'warning' })
  await removeApi(id)
  ElMessage.success('已删除')
  load()
}

onMounted(load)
</script>
