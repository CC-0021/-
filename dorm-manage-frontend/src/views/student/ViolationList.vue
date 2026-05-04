<template>
  <div class="page-container student-page">
    <div class="page-header">
      <h2 class="page-title">我的违规记录</h2>
    </div>
    <el-table :data="list" stripe v-loading="loading">
      <el-table-column prop="violationType" label="违规类型" width="140" />
      <el-table-column prop="description" label="违规描述" show-overflow-tooltip />
      <el-table-column prop="roomNo" label="宿舍号" width="100" />
      <el-table-column prop="violationTime" label="违规时间" width="180" />
      <el-table-column prop="handleResult" label="处理结果" width="120">
        <template #default="{ row }">
          <span v-if="row.handleResult" class="status-badge danger">
            <span class="dot" />{{ row.handleResult }}
          </span>
          <span v-else class="status-badge info">
            <span class="dot" />待处理
          </span>
        </template>
      </el-table-column>
    </el-table>
    <div v-if="list.length === 0 && !loading" class="empty-state">
      <el-icon class="empty-icon"><CircleCheck /></el-icon>
      <p>暂无违规记录</p>
      <span>继续保持，做一个文明住宿生</span>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { CircleCheck } from '@element-plus/icons-vue'
import violationApi from '@/api/violation'

const loading = ref(false)
const list = ref([])

async function load() {
  loading.value = true
  try {
    const res = await violationApi.myList()
    list.value = res.data || []
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>

<style scoped>
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
