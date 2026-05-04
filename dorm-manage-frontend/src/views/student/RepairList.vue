<template>
  <div class="page-container student-page">
    <div class="page-header">
      <h2 class="page-title">报修记录</h2>
      <div class="toolbar">
        <el-select v-model="status" placeholder="状态" clearable style="width: 120px">
          <el-option label="待处理" :value="0" />
          <el-option label="处理中" :value="1" />
          <el-option label="已完成" :value="2" />
          <el-option label="已撤销" :value="3" />
        </el-select>
        <el-button type="primary" @click="load">查询</el-button>
        <el-button type="primary" plain @click="router.push('/repair/submit')">
          <el-icon><Plus /></el-icon>提交报修
        </el-button>
      </div>
    </div>
    <el-table :data="list" stripe>
      <el-table-column prop="repairNo" label="报修单号" width="180" />
      <el-table-column prop="repairType" label="类型" width="100" />
      <el-table-column prop="roomNo" label="宿舍号" width="100" />
      <el-table-column prop="createTime" label="提交时间" width="180" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <span class="status-badge" :class="statusClass(row.status)">
            <span class="dot" />{{ statusText(row.status) }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <el-button type="primary" link size="small" @click="router.push('/repair/detail/' + row.id)">详情</el-button>
          <el-button v-if="row.status === 0" type="danger" link size="small" @click="cancel(row.id)">撤销</el-button>
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
import { useRouter } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { myList, cancel as cancelApi } from '@/api/repair'

const router = useRouter()
const status = ref(null)
const pageNum = ref(1)
const pageSize = 10
const total = ref(0)
const list = ref([])

const statusText = (s) => ['待处理', '处理中', '已完成', '已撤销'][s] || ''
const statusClass = (s) => ({ 0: 'warning', 1: 'primary', 2: 'success', 3: 'info' })[s] || 'info'

async function load() {
  const res = await myList({ pageNum: pageNum.value, pageSize, status: status.value })
  list.value = res.data?.list || []
  total.value = res.data?.total || 0
}

async function cancel(id) {
  await ElMessageBox.confirm('确定撤销该报修？', '提示', { type: 'warning' })
  await cancelApi(id)
  ElMessage.success('已撤销')
  load()
}

onMounted(load)
</script>
