<template>
  <div class="page-container student-page">
    <div class="page-header">
      <h2 class="page-title">我的违规记录</h2>
      <div class="filter-bar">
        <el-radio-group v-model="filterStatus" size="default" @change="onFilterChange">
          <el-radio-button value="all">全部</el-radio-button>
          <el-radio-button value="pending">待处理</el-radio-button>
          <el-radio-button value="handled">已处理</el-radio-button>
        </el-radio-group>
      </div>
    </div>
    <el-table :data="filteredList" stripe v-loading="loading">
      <el-table-column prop="violationType" label="违规类型" width="140" />
      <el-table-column prop="description" label="违规描述" show-overflow-tooltip />
      <el-table-column prop="roomNo" label="宿舍号" width="100" />
      <el-table-column prop="violationTime" label="违规时间" width="180" />
      <el-table-column prop="handleResult" label="处理状态" width="120">
        <template #default="{ row }">
          <span v-if="row.handleResult" class="status-badge handled">
            <span class="dot" />已处理
          </span>
          <span v-else class="status-badge pending">
            <span class="dot" />待处理
          </span>
        </template>
      </el-table-column>
      <el-table-column label="处理结果" min-width="140">
        <template #default="{ row }">
          <span v-if="row.handleResult" class="handle-result">{{ row.handleResult }}</span>
          <span v-else class="no-result">暂无</span>
        </template>
      </el-table-column>
    </el-table>
    <div v-if="filteredList.length === 0 && !loading" class="empty-state">
      <el-icon class="empty-icon"><CircleCheck /></el-icon>
      <p>暂无违规记录</p>
      <span>继续保持，做一个文明住宿生</span>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { CircleCheck } from '@element-plus/icons-vue'
import violationApi from '@/api/violation'

const loading = ref(false)
const list = ref([])
const filterStatus = ref('all')

const filteredList = computed(() => list.value)

function onFilterChange() {
  load()
}

async function load() {
  loading.value = true
  try {
    const res = await violationApi.myList(filterStatus.value)
    list.value = res.data || []
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>

<style scoped>
.filter-bar {
  display: flex;
  align-items: center;
  gap: 12px;
}
.status-badge {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 2px 10px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
}
.status-badge .dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
}
.status-badge.pending {
  background: rgba(245, 158, 11, 0.1);
  color: #d97706;
}
.status-badge.pending .dot {
  background: #f59e0b;
  animation: pulse 2s ease-in-out infinite;
}
.status-badge.handled {
  background: rgba(16, 185, 129, 0.1);
  color: #059669;
}
.status-badge.handled .dot {
  background: #10b981;
}
@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.4; }
}
.handle-result {
  color: #334155;
  font-size: 13px;
}
.no-result {
  color: #94a3b8;
  font-size: 13px;
}
.empty-state {
  text-align: center;
  padding: 80px 20px;
  color: #94a3b8;
}
.empty-icon {
  font-size: 48px;
  color: #10b981;
  margin-bottom: 16px;
}
.empty-state p {
  font-size: 16px;
  color: #64748b;
  margin: 0 0 4px;
}
.empty-state span {
  font-size: 13px;
}
</style>
