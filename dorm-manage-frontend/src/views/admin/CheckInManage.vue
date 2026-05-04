<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">入住审核</h2>
    </div>

    <div class="stat-cards">
      <div class="stat-card" style="background: linear-gradient(135deg, #6366f1, #818cf8)">
        <div class="stat-value">{{ total }}</div>
        <div class="stat-label">申请总数</div>
        <el-icon class="stat-icon"><Document /></el-icon>
      </div>
      <div class="stat-card" style="background: linear-gradient(135deg, #f59e0b, #fbbf24)">
        <div class="stat-value">{{ pendingCount }}</div>
        <div class="stat-label">待审核</div>
        <el-icon class="stat-icon"><Clock /></el-icon>
      </div>
      <div class="stat-card" style="background: linear-gradient(135deg, #10b981, #34d399)">
        <div class="stat-value">{{ approvedCount }}</div>
        <div class="stat-label">已通过</div>
        <el-icon class="stat-icon"><CircleCheck /></el-icon>
      </div>
      <div class="stat-card" style="background: linear-gradient(135deg, #ef4444, #f87171)">
        <div class="stat-value">{{ rejectedCount }}</div>
        <div class="stat-label">已驳回</div>
        <el-icon class="stat-icon"><CircleClose /></el-icon>
      </div>
    </div>

    <el-table :data="list" stripe>
      <el-table-column prop="studentNo" label="学号" width="120" />
      <el-table-column prop="realName" label="姓名" width="100" />
      <el-table-column prop="roomDisplay" label="拟入住房间" />
      <el-table-column prop="checkInTime" label="拟入住时间" width="180" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <span class="status-badge" :class="['warning', 'success', 'danger'][row.status]">
            <span class="dot" />{{ ['待审核', '审核通过', '审核驳回'][row.status] }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120">
        <template #default="{ row }">
          <el-button v-if="row.status === 0" type="primary" link size="small" @click="openAuditDialog(row)">审核</el-button>
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

    <el-dialog v-model="auditDialogVisible" title="入住审核" width="500px">
      <el-form :model="auditForm" label-width="80px">
        <el-form-item label="审核结果">
          <el-radio-group v-model="auditForm.pass">
            <el-radio :label="true">通过</el-radio>
            <el-radio :label="false">驳回</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="!auditForm.pass" label="驳回理由">
          <el-input v-model="auditForm.rejectReason" type="textarea" :rows="3" placeholder="请输入驳回理由" />
        </el-form-item>
        <el-form-item label="分配床位ID">
          <el-input v-model="auditForm.bedId" type="number" placeholder="请输入床位ID（通过时可选）" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="auditDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAudit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { adminPage, audit } from '@/api/checkin'
import { ElMessage } from 'element-plus'
import { Document, Clock, CircleCheck, CircleClose } from '@element-plus/icons-vue'

const list = ref([])
const pageNum = ref(1)
const pageSize = 10
const total = ref(0)
const auditDialogVisible = ref(false)
const auditForm = ref({ id: null, pass: true, rejectReason: '', bedId: null })

const pendingCount = computed(() => list.value.filter(r => r.status === 0).length)
const approvedCount = computed(() => list.value.filter(r => r.status === 1).length)
const rejectedCount = computed(() => list.value.filter(r => r.status === 2).length)

async function load() {
  const res = await adminPage({ pageNum: pageNum.value, pageSize })
  list.value = res.data?.list || []
  total.value = res.data?.total || 0
}

function openAuditDialog(row) {
  auditForm.value = { id: row.id, pass: true, rejectReason: '', bedId: null }
  auditDialogVisible.value = true
}

async function handleAudit() {
  try {
    if (!auditForm.value.pass && !auditForm.value.rejectReason) {
      ElMessage.warning('请输入驳回理由')
      return
    }
    await audit(auditForm.value)
    ElMessage.success('审核成功')
    auditDialogVisible.value = false
    load()
  } catch (error) {
    ElMessage.error('审核失败：' + (error.message || '未知错误'))
  }
}

onMounted(load)
</script>
