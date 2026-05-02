<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">我的违规记录</h2>
    </div>
    <el-table :data="list" stripe v-loading="loading">
      <el-table-column prop="violationType" label="违规类型" width="140" />
      <el-table-column prop="description" label="违规描述" show-overflow-tooltip />
      <el-table-column prop="roomNo" label="宿舍号" width="100" />
      <el-table-column prop="violationTime" label="违规时间" width="180" />
      <el-table-column prop="handleResult" label="处理结果" width="120" />
    </el-table>
    <div v-if="list.length === 0 && !loading" class="empty-wrap">
      <el-empty description="暂无违规记录" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
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
.empty-wrap { margin-top: 60px; }
</style>
