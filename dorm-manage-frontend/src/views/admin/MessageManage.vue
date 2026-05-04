<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">留言回复</h2>
    </div>

    <div class="stat-cards">
      <div class="stat-card" style="background: linear-gradient(135deg, #6366f1, #818cf8)">
        <div class="stat-value">{{ total }}</div>
        <div class="stat-label">留言总数</div>
        <el-icon class="stat-icon"><ChatDotRound /></el-icon>
      </div>
      <div class="stat-card" style="background: linear-gradient(135deg, #f59e0b, #fbbf24)">
        <div class="stat-value">{{ unrepliedCount }}</div>
        <div class="stat-label">未回复</div>
        <el-icon class="stat-icon"><Clock /></el-icon>
      </div>
      <div class="stat-card" style="background: linear-gradient(135deg, #10b981, #34d399)">
        <div class="stat-value">{{ repliedCount }}</div>
        <div class="stat-label">已回复</div>
        <el-icon class="stat-icon"><CircleCheck /></el-icon>
      </div>
    </div>

    <el-table :data="list" stripe>
      <el-table-column prop="title" label="标题" width="200" />
      <el-table-column prop="content" label="内容" show-overflow-tooltip />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <span class="status-badge" :class="row.status === 1 ? 'success' : 'warning'">
            <span class="dot" />{{ row.status === 1 ? '已回复' : '未回复' }}
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="replyContent" label="回复内容" show-overflow-tooltip />
      <el-table-column prop="createTime" label="提交时间" width="180" />
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-button v-if="row.status === 0" type="primary" link size="small" @click="openReply(row)">回复</el-button>
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
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { ChatDotRound, Clock, CircleCheck } from '@element-plus/icons-vue'
import { adminPage, adminReply } from '@/api/message'

const list = ref([])
const pageNum = ref(1)
const pageSize = 10
const total = ref(0)
const replyVisible = ref(false)
const replyId = ref(null)
const replyContent = ref('')

const unrepliedCount = computed(() => list.value.filter(r => r.status === 0).length)
const repliedCount = computed(() => list.value.filter(r => r.status === 1).length)

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
