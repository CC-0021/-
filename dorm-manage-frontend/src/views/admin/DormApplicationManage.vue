<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">宿舍申请审核</h2>
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
      <el-table-column prop="applicationType" label="申请类型" width="100" />
      <el-table-column prop="reason" label="申请理由" show-overflow-tooltip />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <span class="status-badge" :class="['warning', 'success', 'danger'][row.status]">
            <span class="dot" />{{ ['待审核', '通过', '驳回'][row.status] }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <template v-if="row.status === 0">
            <el-button type="primary" link size="small" @click="audit(row.id, true)">通过</el-button>
            <el-button type="danger" link size="small" @click="audit(row.id, false)">驳回</el-button>
          </template>
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
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Document, Clock, CircleCheck, CircleClose } from '@element-plus/icons-vue'
import { adminPage, adminAudit } from '@/api/dormApplication'

const list = ref([])
const pageNum = ref(1)
const pageSize = 10
const total = ref(0)

const pendingCount = computed(() => list.value.filter(r => r.status === 0).length)
const approvedCount = computed(() => list.value.filter(r => r.status === 1).length)
const rejectedCount = computed(() => list.value.filter(r => r.status === 2).length)

async function load() {
  const res = await adminPage({ pageNum: pageNum.value, pageSize })
  list.value = res.data?.list || []
  total.value = res.data?.total || 0
}

async function audit(id, pass) {
  await adminAudit({ id, pass, rejectReason: pass ? '' : '不符合条件' })
  ElMessage.success(pass ? '已通过' : '已驳回')
  load()
}

onMounted(load)
</script>
