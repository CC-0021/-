<template>
  <div class="page-container">
    <el-button type="default" @click="router.back()">返回</el-button>
    <div class="detail-card" v-loading="loading">
      <h1 class="detail-title">{{ detail.title }}</h1>
      <div class="meta">发布人：{{ detail.publisherName }} · 发布时间：{{ detail.publishTime }}</div>
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
  margin-top: 20px;
  padding: 24px;
  background: #fff;
  border-radius: var(--card-radius);
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.06);
}
.detail-title {
  margin: 0 0 12px;
  font-size: 22px;
  font-weight: 600;
  color: var(--el-text-color-primary);
}
.meta {
  color: var(--el-text-color-secondary);
  font-size: 14px;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid var(--el-border-color-lighter);
}
.content {
  line-height: 1.8;
  color: var(--el-text-color-regular);
}
</style>
