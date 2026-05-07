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
}
.empty-state .empty-icon {
  font-size: 56px;
  color: var(--neutral-200);
  margin-bottom: 20px;
  opacity: 0.6;
}
.empty-state p {
  font-size: 18px;
  color: var(--neutral-500);
  margin: 0 0 6px;
  font-weight: 600;
}
.empty-state span {
  font-size: 14px;
  color: var(--neutral-400);
}

.dorm-card {
  background: #fff;
  border-radius: var(--radius-2xl);
  padding: 36px;
  border: 1px solid var(--student-100);
  box-shadow: var(--shadow-sm);
  margin-bottom: 24px;
}
.dorm-card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 18px;
  border-bottom: 1px solid var(--neutral-100);
}
.dorm-title-wrap {
  display: flex;
  align-items: center;
  gap: 14px;
}
.dorm-icon {
  font-size: 28px;
  color: var(--student-500);
}
.dorm-title {
  font-family: 'Noto Serif SC', serif;
  font-size: 22px;
  font-weight: 700;
  color: var(--neutral-900);
}

.dorm-info-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}
.info-item {
  text-align: center;
  padding: 22px 16px;
  background: var(--student-50);
  border-radius: var(--radius-lg);
  transition: transform 0.2s ease;
}
.info-item:hover {
  transform: translateY(-2px);
}
.info-label {
  display: block;
  font-size: 13px;
  color: var(--neutral-400);
  margin-bottom: 8px;
  font-weight: 500;
}
.info-value {
  display: block;
  font-family: 'DM Sans', sans-serif;
  font-size: 24px;
  font-weight: 700;
  color: var(--neutral-800);
}
.info-value.highlight {
  color: var(--student-600);
}

.roommates-card {
  background: #fff;
  border-radius: var(--radius-2xl);
  padding: 36px;
  border: 1px solid var(--student-100);
  box-shadow: var(--shadow-sm);
}
.card-section-title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-family: 'Noto Serif SC', serif;
  font-size: 18px;
  font-weight: 700;
  color: var(--neutral-900);
  margin-bottom: 20px;
}
.card-section-title .el-icon {
  color: var(--student-500);
  font-size: 20px;
}
.self-tag {
  font-weight: 600;
}

@media (max-width: 768px) {
  .dorm-info-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 14px;
  }
  .dorm-card, .roommates-card {
    padding: 24px;
  }
  .dorm-title {
    font-size: 20px;
  }
  .info-value {
    font-size: 20px;
  }
}
</style>
