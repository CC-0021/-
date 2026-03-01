<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">留言回复</h2>
    </div>
    <el-table :data="list" stripe>
      <el-table-column prop="title" label="标题" width="200" />
      <el-table-column prop="content" label="内容" show-overflow-tooltip />
      <el-table-column prop="status" label="状态" width="90">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">{{ row.status === 1 ? '已回复' : '未回复' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="replyContent" label="回复内容" show-overflow-tooltip />
      <el-table-column prop="createTime" label="提交时间" width="180" />
      <el-table-column label="操作" width="100" align="center">
        <template #default="{ row }">
          <el-button v-if="row.status === 0" type="primary" link @click="openReply(row)">回复</el-button>
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
    <el-dialog v-model="replyVisible" title="回复留言" width="500px" destroy-on-close>
      <el-input v-model="replyContent" type="textarea" :rows="4" placeholder="请输入回复内容" />
      <template #footer>
        <el-button @click="replyVisible = false">取消</el-button>
        <el-button type="primary" @click="submitReply">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { adminPage, adminReply } from '@/api/message'

const list = ref([])
const pageNum = ref(1)
const pageSize = 10
const total = ref(0)
const replyVisible = ref(false)
const replyId = ref(null)
const replyContent = ref('')

async function load() {
  const res = await adminPage({ pageNum: pageNum.value, pageSize })
  list.value = res.data?.list || []
  total.value = res.data?.total || 0
}

function openReply(row) {
  replyId.value = row.id
  replyContent.value = ''
  replyVisible.value = true
}

async function submitReply() {
  await adminReply({ id: replyId.value, replyContent: replyContent.value })
  ElMessage.success('回复成功')
  replyVisible.value = false
  load()
}

onMounted(load)
</script>

<style scoped>
.pagination-wrap { margin-top: 20px; display: flex; justify-content: flex-end; }
</style>
