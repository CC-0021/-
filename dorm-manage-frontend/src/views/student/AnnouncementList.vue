<template>
  <div class="page-container student-page">
    <div class="page-header">
      <h2 class="page-title">系统公告</h2>
      <div class="toolbar">
        <el-input v-model="title" placeholder="标题搜索" clearable style="width: 200px" @clear="load" />
        <el-button type="primary" @click="load">搜索</el-button>
      </div>
    </div>
    <el-table :data="list" stripe>
      <el-table-column prop="title" label="标题" min-width="200">
        <template #default="{ row }">
          <div style="display: flex; align-items: center; gap: 8px;">
            <el-tag v-if="row.isTop === 1" size="small" type="danger">置顶</el-tag>
            <span>{{ row.title }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="publisherName" label="发布人" width="120" />
      <el-table-column prop="publishTime" label="发布时间" width="180" />
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-button type="primary" link size="small" @click="router.push('/announcement/' + row.id)">查看</el-button>
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
import { getList } from '@/api/announcement'

const router = useRouter()
const title = ref('')
const pageNum = ref(1)
const pageSize = 10
const total = ref(0)
const list = ref([])

async function load() {
  const res = await getList({ pageNum: pageNum.value, pageSize, title: title.value })
  list.value = res.data?.list || []
  total.value = res.data?.total || 0
}

onMounted(load)
</script>
