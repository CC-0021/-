<template>
  <div class="page-container student-page">
    <div class="page-header">
      <h2 class="page-title">我的宿舍</h2>
    </div>

    <div v-if="!loading && !dormitory" class="empty-state">
      <el-icon class="empty-icon"><House /></el-icon>
      <p>暂无宿舍分配信息</p>
      <span>请联系管理员进行宿舍分配</span>
    </div>

    <template v-else-if="dormitory">
      <div class="dorm-card">
        <div class="dorm-card-header">
          <div class="dorm-title-wrap">
            <el-icon class="dorm-icon"><House /></el-icon>
            <span class="dorm-title">{{ dormitory.buildingNo }}号楼 {{ dormitory.roomNo }}</span>
          </div>
          <span class="status-badge success">
            <span class="dot" />{{ dormitory.dormType }}
          </span>
        </div>
        <div class="dorm-info-grid">
          <div class="info-item">
            <span class="info-label">楼层</span>
            <span class="info-value">{{ dormitory.floorNo }}层</span>
          </div>
          <div class="info-item">
            <span class="info-label">我的床位</span>
            <span class="info-value highlight">{{ myBed?.bedNo }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">床位总数</span>
            <span class="info-value">{{ dormitory.bedTotal }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">空闲床位</span>
            <span class="info-value">{{ dormitory.bedAvailable }}</span>
          </div>
        </div>
      </div>

      <div class="roommates-card">
        <div class="card-section-title">
          <el-icon><User /></el-icon>
          <span>宿舍成员</span>
        </div>
        <el-table :data="roommates" stripe>
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="bedNo" label="床位号" width="100" />
          <el-table-column prop="studentName" label="姓名" width="120">
            <template #default="{ row }">
              <span :class="{ 'self-tag': row.studentId === currentUserId }">
                {{ row.studentName || '暂无' }}
                <el-tag v-if="row.studentId === currentUserId" type="primary" size="small" effect="dark">我</el-tag>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="studentNo" label="学号" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <span class="status-badge" :class="row.status === 1 ? 'success' : 'info'">
                <span class="dot" />{{ row.status === 1 ? '已入住' : '空闲' }}
              </span>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { House, User } from '@element-plus/icons-vue'
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
.empty-state {
  text-align: center;
  padding: 80px 20px;
  color: #94a3b8;
}
.empty-icon {
  font-size: 48px;
  color: #e2e8f0;
  margin-bottom: 16px;
}
.empty-state p {
  font-size: 16px;
  color: #64748b;
  margin: 0 0 4px;
}
.empty-state span {
  font-size: 13px;
}
.dorm-card {
  background: #fff;
  border-radius: 16px;
  padding: 24px;
  border: 1px solid rgba(14, 165, 233, 0.1);
  box-shadow: 0 4px 24px rgba(14, 165, 233, 0.08);
  margin-bottom: 20px;
}
.dorm-card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f1f5f9;
}
.dorm-title-wrap {
  display: flex;
  align-items: center;
  gap: 10px;
}
.dorm-icon {
  font-size: 22px;
  color: #0ea5e9;
}
.dorm-title {
  font-size: 20px;
  font-weight: 700;
  color: #0f172a;
}
.dorm-info-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}
.info-item {
  text-align: center;
  padding: 12px;
  background: #f8fafc;
  border-radius: 12px;
}
.info-label {
  display: block;
  font-size: 12px;
  color: #94a3b8;
  margin-bottom: 6px;
}
.info-value {
  display: block;
  font-size: 18px;
  font-weight: 600;
  color: #334155;
}
.info-value.highlight {
  color: #0ea5e9;
}
.roommates-card {
  background: #fff;
  border-radius: 16px;
  padding: 24px;
  border: 1px solid rgba(14, 165, 233, 0.1);
  box-shadow: 0 4px 24px rgba(14, 165, 233, 0.08);
}
.card-section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  color: #0f172a;
  margin-bottom: 16px;
}
.card-section-title .el-icon {
  color: #0ea5e9;
}
.self-tag {
  font-weight: 600;
}
@media (max-width: 768px) {
  .dorm-info-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>
