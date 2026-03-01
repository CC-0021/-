<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">报修管理</h2>
    </div>
    <el-table :data="list" stripe>
      <el-table-column prop="repairNo" label="报修单号" width="180" />
      <el-table-column prop="repairType" label="类型" width="100" />
      <el-table-column prop="roomNo" label="宿舍号" width="100" />
      <el-table-column prop="description" label="描述" show-overflow-tooltip />
      <el-table-column prop="status" label="状态" width="90">
        <template #default="{ row }">
          <el-tag :type="statusTagType(row.status)" size="small">{{ statusText(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="handlerName" label="处理人" width="100" />
      <el-table-column label="操作" width="100" align="center">
        <template #default="{ row }">
          <el-button v-if="row.status !== 2 && row.status !== 3" type="primary" link @click="openHandle(row)">处理</el-button>
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
    <el-dialog v-model="handleVisible" title="处理报修" width="500px" destroy-on-close>
      <el-form :model="handleForm" label-width="80px">
        <el-form-item label="处理备注">
          <el-input v-model="handleForm.handleRemark" type="textarea" :rows="3" placeholder="填写处理说明" />
        </el-form-item>
        <el-form-item label="标记完成">
          <el-switch v-model="handleForm.complete" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="handleVisible = false">取消</el-button>
        <el-button type="primary" @click="submitHandle">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { adminPage, adminHandle } from '@/api/repair'

const list = ref([])
const pageNum = ref(1)
const pageSize = 10
const total = ref(0)
const handleVisible = ref(false)
const handleForm = reactive({ id: null, handleRemark: '', complete: false })

const statusText = (s) => ['待处理', '处理中', '已完成', '已撤销'][s] || ''
const statusTagType = (s) => ({ 0: 'warning', 1: 'primary', 2: 'success', 3: 'info' })[s] || 'info'

async function load() {
  const res = await adminPage({ pageNum: pageNum.value, pageSize })
  list.value = res.data?.list || []
  total.value = res.data?.total || 0
}

function openHandle(row) {
  handleForm.id = row.id
  handleForm.handleRemark = row.handleRemark || ''
  handleForm.complete = false
  handleVisible.value = true
}

async function submitHandle() {
  await adminHandle(handleForm)
  ElMessage.success('操作成功')
  handleVisible.value = false
  load()
}

onMounted(load)
</script>

<style scoped>
.pagination-wrap { margin-top: 20px; display: flex; justify-content: flex-end; }
</style>
