<template>
  <div class="home-page">
    <div class="welcome-card">
      <div class="welcome-bg" />
      <div class="welcome-content">
        <div class="welcome-text">
          <h2>你好，{{ userStore.displayName }} 👋</h2>
          <p>欢迎使用学生公寓服务系统，祝你生活愉快！</p>
        </div>
        <div class="welcome-illustration">
          <el-icon :size="80" style="color: rgba(255,255,255,0.3)"><School /></el-icon>
        </div>
      </div>
    </div>

    <div class="quick-cards">
      <div class="quick-card" @click="router.push('/repair/submit')">
        <div class="quick-icon" style="background: linear-gradient(135deg, #0ea5e9, #0284c7)">
          <el-icon :size="28"><Tools /></el-icon>
        </div>
        <div class="quick-info">
          <h4>提交报修</h4>
          <p>宿舍设施报修</p>
        </div>
        <el-icon class="quick-arrow"><ArrowRight /></el-icon>
      </div>
      <div class="quick-card" @click="router.push('/checkin')">
        <div class="quick-icon" style="background: linear-gradient(135deg, #10b981, #059669)">
          <el-icon :size="28"><Calendar /></el-icon>
        </div>
        <div class="quick-info">
          <h4>入住登记</h4>
          <p>申请宿舍入住</p>
        </div>
        <el-icon class="quick-arrow"><ArrowRight /></el-icon>
      </div>
      <div class="quick-card" @click="router.push('/message')">
        <div class="quick-icon" style="background: linear-gradient(135deg, #8b5cf6, #7c3aed)">
          <el-icon :size="28"><ChatDotRound /></el-icon>
        </div>
        <div class="quick-info">
          <h4>留言反馈</h4>
          <p>意见与建议</p>
        </div>
        <el-icon class="quick-arrow"><ArrowRight /></el-icon>
      </div>
      <div class="quick-card" @click="router.push('/dorm-application')">
        <div class="quick-icon" style="background: linear-gradient(135deg, #f59e0b, #d97706)">
          <el-icon :size="28"><Document /></el-icon>
        </div>
        <div class="quick-info">
          <h4>宿舍申请</h4>
          <p>换宿/退宿申请</p>
        </div>
        <el-icon class="quick-arrow"><ArrowRight /></el-icon>
      </div>
    </div>

    <div class="info-grid">
      <div class="info-card">
        <div class="info-card-header">
          <h3>最新公告</h3>
          <el-button type="primary" link @click="router.push('/announcement')">更多</el-button>
        </div>
        <div class="announcement-list">
          <div v-for="item in announcements" :key="item.id" class="announcement-item" @click="router.push('/announcement/' + item.id)">
            <div class="ann-title">
              <el-tag v-if="item.isTop === 1" size="small" type="danger" class="top-tag">置顶</el-tag>
              {{ item.title }}
            </div>
            <div class="ann-time">{{ item.publishTime }}</div>
          </div>
          <div v-if="announcements.length === 0" class="empty-state">
            <div class="empty-text">暂无公告</div>
          </div>
        </div>
      </div>

      <div class="info-card">
        <div class="info-card-header">
          <h3>我的报修</h3>
          <el-button type="primary" link @click="router.push('/repair')">更多</el-button>
        </div>
        <div class="repair-list">
          <div v-for="item in repairs" :key="item.id" class="repair-item" @click="router.push('/repair/detail/' + item.id)">
            <div class="repair-left">
              <span class="status-badge" :class="repairStatusClass(item.status)">
                <span class="dot" />
                {{ repairStatusText(item.status) }}
              </span>
              <span class="repair-type">{{ item.repairType }}</span>
            </div>
            <div class="repair-room">{{ item.roomNo }}</div>
          </div>
          <div v-if="repairs.length === 0" class="empty-state">
            <div class="empty-text">暂无报修记录</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import {
  School, Tools, Calendar, ChatDotRound, Document, ArrowRight
} from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { getList } from '@/api/announcement'
import { myList } from '@/api/repair'

const router = useRouter()
const userStore = useUserStore()

const announcements = ref([])
const repairs = ref([])

const repairStatusText = (s) => ['待处理', '处理中', '已完成', '已撤销'][s] || ''
const repairStatusClass = (s) => ({ 0: 'warning', 1: 'primary', 2: 'success', 3: 'info' })[s] || 'info'

async function loadAnnouncements() {
  try {
    const res = await getList({ pageNum: 1, pageSize: 5 })
    announcements.value = res.data?.list || []
  } catch (e) {}
}

async function loadRepairs() {
  try {
    const res = await myList({ pageNum: 1, pageSize: 5 })
    repairs.value = res.data?.list || []
  } catch (e) {}
}

onMounted(() => {
  loadAnnouncements()
  loadRepairs()
})
</script>

<style scoped>
.home-page {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.welcome-card {
  border-radius: 16px;
  overflow: hidden;
  position: relative;
  background: linear-gradient(135deg, #0ea5e9 0%, #0284c7 50%, #0369a1 100%);
  color: #fff;
  box-shadow: 0 8px 32px rgba(14, 165, 233, 0.3);
}
.welcome-bg {
  position: absolute;
  top: -40px;
  right: -40px;
  width: 200px;
  height: 200px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.08);
}
.welcome-content {
  position: relative;
  padding: 32px 36px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.welcome-text h2 {
  margin: 0 0 8px;
  font-size: 24px;
  font-weight: 700;
}
.welcome-text p {
  margin: 0;
  font-size: 15px;
  opacity: 0.9;
}
.welcome-illustration {
  opacity: 0.6;
}

.quick-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}
.quick-card {
  background: #fff;
  border-radius: 14px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 14px;
  cursor: pointer;
  border: 1px solid rgba(14, 165, 233, 0.1);
  box-shadow: 0 2px 12px rgba(14, 165, 233, 0.06);
  transition: all 0.25s ease;
}
.quick-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(14, 165, 233, 0.15);
}
.quick-icon {
  width: 52px;
  height: 52px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  flex-shrink: 0;
}
.quick-info h4 {
  margin: 0 0 4px;
  font-size: 15px;
  font-weight: 600;
  color: #0f172a;
}
.quick-info p {
  margin: 0;
  font-size: 12px;
  color: #94a3b8;
}
.quick-arrow {
  margin-left: auto;
  color: #cbd5e1;
  transition: all 0.2s;
}
.quick-card:hover .quick-arrow {
  color: #0ea5e9;
  transform: translateX(4px);
}

.info-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}
.info-card {
  background: #fff;
  border-radius: 16px;
  padding: 24px;
  border: 1px solid rgba(14, 165, 233, 0.1);
  box-shadow: 0 4px 24px rgba(14, 165, 233, 0.08);
}
.info-card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
}
.info-card-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #0f172a;
}

.announcement-list {
  display: flex;
  flex-direction: column;
}
.announcement-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 0;
  border-bottom: 1px solid #f1f5f9;
  cursor: pointer;
  transition: all 0.2s;
}
.announcement-item:last-child {
  border-bottom: none;
}
.announcement-item:hover .ann-title {
  color: #0ea5e9;
}
.ann-title {
  font-size: 14px;
  color: #334155;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: color 0.2s;
}
.top-tag {
  flex-shrink: 0;
}
.ann-time {
  font-size: 12px;
  color: #94a3b8;
  white-space: nowrap;
  margin-left: 16px;
}

.repair-list {
  display: flex;
  flex-direction: column;
}
.repair-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 0;
  border-bottom: 1px solid #f1f5f9;
  cursor: pointer;
  transition: all 0.2s;
}
.repair-item:last-child {
  border-bottom: none;
}
.repair-item:hover {
  background: #f8fafc;
  padding-left: 8px;
  padding-right: 8px;
  border-radius: 8px;
}
.repair-left {
  display: flex;
  align-items: center;
  gap: 10px;
}
.repair-type {
  font-size: 14px;
  color: #334155;
}
.repair-room {
  font-size: 13px;
  color: #64748b;
}

@media (max-width: 768px) {
  .quick-cards {
    grid-template-columns: repeat(2, 1fr);
  }
  .info-grid {
    grid-template-columns: 1fr;
  }
  .welcome-content {
    padding: 24px;
  }
  .welcome-illustration {
    display: none;
  }
}
</style>
