<template>
  <div class="page-container student-page">
    <el-button @click="router.push('/repair')" style="margin-bottom: 16px">返回列表</el-button>
    <div class="detail-card" v-loading="loading">
      <div class="detail-header">
        <h2 class="detail-title">报修详情</h2>
        <span class="status-badge" :class="statusClass(detailData.status)">
          <span class="dot" />{{ statusText(detailData.status) }}
        </span>
      </div>
      <div class="detail-grid">
        <div class="detail-item">
          <span class="detail-label">报修单号</span>
          <span class="detail-value">{{ detailData.repairNo }}</span>
        </div>
        <div class="detail-item">
          <span class="detail-label">报修类型</span>
          <span class="detail-value">{{ detailData.repairType }}</span>
        </div>
        <div class="detail-item">
          <span class="detail-label">宿舍号</span>
          <span class="detail-value">{{ detailData.roomNo }}</span>
        </div>
        <div class="detail-item">
          <span class="detail-label">问题描述</span>
          <span class="detail-value">{{ detailData.description }}</span>
        </div>
        <div class="detail-item">
          <span class="detail-label">处理备注</span>
          <span class="detail-value">{{ detailData.handleRemark || '-' }}</span>
        </div>
        <div class="detail-item">
          <span class="detail-label">完成时间</span>
          <span class="detail-value">{{ detailData.completeTime || '-' }}</span>
        </div>
      </div>
      <div v-if="detailData.status === 2 && !hasEval" class="eval-section">
        <div class="eval-title">
          <el-icon><Star /></el-icon>
          <span>评价服务</span>
        </div>
        <el-rate v-model="evalScore" size="large" />
        <el-input v-model="evalContent" type="textarea" placeholder="评价内容（选填）" :rows="3" style="margin: 12px 0" />
        <el-button type="primary" @click="submitEval">提交评价</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Star } from '@element-plus/icons-vue'
import { detail as getRepairDetail, evaluate } from '@/api/repair'

const route = useRoute()
const router = useRouter()
const loading = ref(true)
const detailData = ref({})
const evalScore = ref(5)
const evalContent = ref('')
const hasEval = ref(false)

const statusText = (s) => ['待处理', '处理中', '已完成', '已撤销'][s] || ''
const statusClass = (s) => ({ 0: 'warning', 1: 'primary', 2: 'success', 3: 'info' })[s] || 'info'

async function load() {
  loading.value = true
  try {
    const res = await getRepairDetail(route.params.id)
    detailData.value = res.data || {}
  } finally {
    loading.value = false
  }
}

async function submitEval() {
  await evaluate({ repairId: detailData.value.id, score: evalScore.value, content: evalContent.value })
  ElMessage.success('评价成功')
  hasEval.value = true
}

onMounted(load)
</script>

<style scoped>
.detail-card {
  padding: 40px;
  background: #fff;
  border-radius: 18px;
  border: 1px solid rgba(249, 115, 22, 0.1);
  box-shadow: 0 4px 24px rgba(249, 115, 22, 0.06);
}
.detail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 28px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f1f5f9;
}
.detail-title {
  margin: 0;
  font-size: 24px;
  font-weight: 700;
  color: #0f172a;
}
.detail-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 24px;
}
.detail-item {
  padding: 18px 20px;
  background: #f8fafc;
  border-radius: 14px;
}
.detail-label {
  display: block;
  font-size: 13px;
  color: #94a3b8;
  margin-bottom: 6px;
}
.detail-value {
  display: block;
  font-size: 16px;
  color: #334155;
  font-weight: 500;
}
.eval-section {
  margin-top: 32px;
  padding-top: 24px;
  border-top: 1px solid #f1f5f9;
}
.eval-title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 18px;
  font-weight: 600;
  color: #0f172a;
  margin-bottom: 16px;
}
.eval-title .el-icon {
  color: #f59e0b;
  font-size: 20px;
}
</style>
