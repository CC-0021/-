<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">宿舍申请审核</h2>
    </div>
    <el-table :data="list" stripe>
      <el-table-column prop="applicationType" label="申请类型" width="100" />
      <el-table-column prop="reason" label="申请理由" show-overflow-tooltip />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : row.status === 2 ? 'danger' : 'warning'" size="small">
            {{ ['待审核', '通过', '驳回'][row.status] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" align="center">
        <template #default="{ row }">
          <template v-if="row.status === 0">
            <el-button type="primary" link @click="audit(row.id, true)">通过</el-button>
            <el-button type="danger" link @click="audit(row.id, false)">驳回</el-button>
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
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { adminPage, adminAudit } from '@/api/dormApplication'

const list = ref([])
const pageNum = ref(1)
const pageSize = 10
const total = ref(0)

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

<style scoped>
.pagination-wrap { margin-top: 20px; display: flex; justify-content: flex-end; }
</style>
