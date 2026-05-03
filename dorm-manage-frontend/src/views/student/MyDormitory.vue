<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">我的宿舍</h2>
    </div>

    <!-- 未分配宿舍提示 -->
    <el-empty v-if="!loading && !dormitory" description="暂无宿舍分配信息" />

    <!-- 宿舍信息卡片 -->
    <template v-else-if="dormitory">
      <el-card class="dorm-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <span class="dorm-title">{{ dormitory.buildingNo }}号楼 {{ dormitory.roomNo }}</span>
            <el-tag type="success" size="small">{{ dormitory.dormType }}</el-tag>
          </div>
        </template>
        <div class="dorm-info">
          <div class="info-item">
            <span class="label">楼层：</span>
            <span class="value">{{ dormitory.floorNo }}层</span>
          </div>
          <div class="info-item">
            <span class="label">我的床位：</span>
            <span class="value highlight">{{ myBed?.bedNo }}</span>
          </div>
          <div class="info-item">
            <span class="label">床位总数：</span>
            <span class="value">{{ dormitory.bedTotal }}</span>
          </div>
          <div class="info-item">
            <span class="label">空闲床位：</span>
            <span class="value">{{ dormitory.bedAvailable }}</span>
          </div>
        </div>
      </el-card>

      <!-- 宿舍成员列表 -->
      <el-card class="roommates-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <span>宿舍成员</span>
          </div>
        </template>
        <el-table :data="roommates" stripe>
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="bedNo" label="床位号" width="100" />
          <el-table-column prop="studentName" label="姓名" width="120">
            <template #default="{ row }">
              <span :class="{ 'self-tag': row.studentId === currentUserId }">
                {{ row.studentName || '暂无' }}
                <el-tag v-if="row.studentId === currentUserId" type="primary" size="small">我</el-tag>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="studentNo" label="学号" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">
                {{ row.status === 1 ? '已入住' : '空闲' }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import bedApi from '@/api/bed'

const loading = ref(false)
const dormitory = ref(null)
const myBed = ref(null)
const roommates = ref([])
const currentUserId = ref(null)

async function load() {
  loading.value = true
  try {
    const res = await bedApi.myDormitory()
    const data = res.data || {}
    dormitory.value = data.dormitory || null
    myBed.value = data.myBed || null
    roommates.value = data.roommates || []
    // 从localStorage获取当前用户ID
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    currentUserId.value = userInfo.id
  } catch (err) {
    ElMessage.error('获取宿舍信息失败')
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>

<style scoped>
.page-container {
  padding: 20px;
}
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
.page-title {
  margin: 0;
  font-size: 20px;
  font-weight: 500;
}
.dorm-card {
  margin-bottom: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.dorm-title {
  font-size: 18px;
  font-weight: bold;
}
.dorm-info {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
}
.info-item {
  display: flex;
  align-items: center;
}
.label {
  color: #666;
  margin-right: 8px;
}
.value {
  font-weight: 500;
}
.highlight {
  color: #409eff;
  font-size: 16px;
  font-weight: bold;
}
.self-tag {
  font-weight: bold;
}
.roommates-card {
  margin-top: 20px;
}
</style>
