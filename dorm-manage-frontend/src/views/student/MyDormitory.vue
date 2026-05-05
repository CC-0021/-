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
  padding: 100px 20px;
  color: #94a3b8;
}
.empty-icon {
  font-size: 64px;
  color: #e2e8f0;
  margin-bottom: 20px;
}
.empty-state p {
  font-size: 18px;
  color: #64748b;
  margin: 0 0 6px;
}
.empty-state span {
  font-size: 15px;
}
.dorm-card {
  background: #fff;
  border-radius: 18px;
  padding: 36px;
  border: 1px solid rgba(249, 115, 22, 0.1);
  box-shadow: 0 4px 24px rgba(249, 115, 22, 0.06);
  margin-bottom: 28px;
}
.dorm-card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 28px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f1f5f9;
}
.dorm-title-wrap {
  display: flex;
  align-items: center;
  gap: 14px;
}
.dorm-icon {
  font-size: 28px;
  color: #f97316;
}
.dorm-title {
  font-size: 24px;
  font-weight: 700;
  color: #0f172a;
}
.dorm-info-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
}
.info-item {
  text-align: center;
  padding: 20px 16px;
  background: #f8fafc;
  border-radius: 14px;
}
.info-label {
  display: block;
  font-size: 14px;
  color: #94a3b8;
  margin-bottom: 8px;
}
.info-value {
  display: block;
  font-size: 22px;
  font-weight: 600;
  color: #334155;
}
.info-value.highlight {
  color: #f97316;
}
.roommates-card {
  background: #fff;
  border-radius: 18px;
  padding: 36px;
  border: 1px solid rgba(249, 115, 22, 0.1);
  box-shadow: 0 4px 24px rgba(249, 115, 22, 0.06);
}
.card-section-title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 18px;
  font-weight: 600;
  color: #0f172a;
  margin-bottom: 20px;
}
.card-section-title .el-icon {
  color: #f97316;
  font-size: 20px;
}
.self-tag {
  font-weight: 600;
}
@media (max-width: 768px) {
  .dorm-info-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;
  }
  .dorm-card, .roommates-card {
    padding: 24px;
  }
  .dorm-title {
    font-size: 20px;
  }
  .info-value {
    font-size: 18px;
  }
}
</style>
