<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">入住审核</h2>
    </div>
    <el-table :data="list" stripe>
      <el-table-column prop="studentNo" label="学号" width="120" />
      <el-table-column prop="realName" label="姓名" width="100" />
      <el-table-column prop="roomDisplay" label="拟入住房间" />
      <el-table-column prop="checkInTime" label="拟入住时间" width="180" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : row.status === 2 ? 'danger' : 'warning'" size="small">
            {{ ['待审核', '审核通过', '审核驳回'][row.status] }}
          </el-tag>
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
import { ref, onMounted } from 'vue'
import { adminPage } from '@/api/checkin'

const list = ref([])
const pageNum = ref(1)
const pageSize = 10
const total = ref(0)

async function load() {
  const res = await adminPage({ pageNum: pageNum.value, pageSize })
  list.value = res.data?.list || []
  total.value = res.data?.total || 0
}

onMounted(load)
</script>

<style scoped>
.pagination-wrap { margin-top: 20px; display: flex; justify-content: flex-end; }
</style>
