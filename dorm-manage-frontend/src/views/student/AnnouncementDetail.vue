<template>
  <div class="page-container student-page">
    <el-button @click="router.back()" style="margin-bottom: 16px">返回</el-button>
    <div class="detail-card" v-loading="loading">
      <h1 class="detail-title">{{ detail.title }}</h1>
      <div class="meta">
        <span>发布人：{{ detail.publisherName }}</span>
        <span class="meta-divider">·</span>
        <span>发布时间：{{ detail.publishTime }}</span>
      </div>
      <div class="content" v-html="detail.content" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getDetail } from '@/api/announcement'

const route = useRoute()
const router = useRouter()
const loading = ref(true)
const detail = ref({})

async function load() {
  loading.value = true
  try {
    const res = await getDetail(route.params.id)
    detail.value = res.data || {}
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>

<style scoped>
.detail-card {
  padding: 28px;
  background: #fff;
  border-radius: 16px;
  border: 1px solid rgba(14, 165, 233, 0.1);
  box-shadow: 0 4px 24px rgba(14, 165, 233, 0.08);
}
.detail-title {
  margin: 0 0 12px;
  font-size: 22px;
  font-weight: 700;
  color: #0f172a;
}
.meta {
  color: #94a3b8;
  font-size: 14px;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f1f5f9;
  display: flex;
  align-items: center;
  gap: 8px;
}
.meta-divider {
  color: #e2e8f0;
}
.content {
  line-height: 1.8;
  color: #334155;
}
</style>
