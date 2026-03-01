<template>
  <div class="page-container">
    <el-button type="default" @click="router.push('/repair')">返回</el-button>
    <el-card v-loading="loading" class="detail-card" shadow="never">
      <template #header>
        <span>报修详情</span>
      </template>
      <el-descriptions :column="1" border>
        <el-descriptions-item label="报修单号">{{ detailData.repairNo }}</el-descriptions-item>
        <el-descriptions-item label="报修类型">{{ detailData.repairType }}</el-descriptions-item>
        <el-descriptions-item label="宿舍号">{{ detailData.roomNo }}</el-descriptions-item>
        <el-descriptions-item label="问题描述">{{ detailData.description }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="statusTagType(detailData.status)" size="small">{{ statusText(detailData.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="处理备注">{{ detailData.handleRemark || '-' }}</el-descriptions-item>
        <el-descriptions-item label="完成时间">{{ detailData.completeTime || '-' }}</el-descriptions-item>
      </el-descriptions>
      <div v-if="detailData.status === 2 && !hasEval" class="eval-section">
        <h4>评价</h4>
        <el-rate v-model="evalScore" />
        <el-input v-model="evalContent" type="textarea" placeholder="评价内容（选填）" :rows="3" class="eval-input" />
        <el-button type="primary" @click="submitEval">提交评价</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { detail as getRepairDetail, evaluate } from '@/api/repair'

const route = useRoute()
const router = useRouter()
const loading = ref(true)
const detailData = ref({})
const evalScore = ref(5)
const evalContent = ref('')
const hasEval = ref(false)

const statusText = (s) => ['待处理', '处理中', '已完成', '已撤销'][s] || ''
const statusTagType = (s) => ({ 0: 'warning', 1: 'primary', 2: 'success', 3: 'info' })[s] || 'info'

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
.detail-card { margin-top: 20px; border-radius: var(--card-radius); }
.eval-section { margin-top: 24px; padding-top: 20px; border-top: 1px solid var(--el-border-color-lighter); }
.eval-section h4 { margin: 0 0 12px; font-size: 14px; color: var(--el-text-color-regular); }
.eval-input { margin: 12px 0; display: block; }
</style>
