<template>
  <div class="home-page">
    <div class="welcome-card">
      <div class="welcome-shape shape-1" />
      <div class="welcome-shape shape-2" />
      <div class="welcome-content">
        <div class="welcome-text">
          <h2>你好，{{ userStore.displayName }} <span class="wave">👋</span></h2>
          <p>欢迎使用学生公寓服务系统，祝你生活愉快！</p>
        </div>
        <div class="welcome-date">
          <span class="date-day">{{ day }}</span>
          <span class="date-weekday">{{ weekday }}</span>
        </div>
      </div>
    </div>

    <div class="quick-cards">
      <div class="quick-card" @click="router.push('/repair/submit')">
        <div class="quick-icon" style="background: #f97316">
          <el-icon :size="26"><Tools /></el-icon>
        </div>
        <div class="quick-info">
          <h4>提交报修</h4>
          <p>宿舍设施报修</p>
        </div>
        <el-icon class="quick-arrow"><ArrowRight /></el-icon>
      </div>
      <div class="quick-card" @click="router.push('/checkin')">
        <div class="quick-icon" style="background: #10b981">
          <el-icon :size="26"><Calendar /></el-icon>
        </div>
        <div class="quick-info">
          <h4>入住登记</h4>
          <p>申请宿舍入住</p>
        </div>
        <el-icon class="quick-arrow"><ArrowRight /></el-icon>
      </div>
      <div class="quick-card" @click="router.push('/message')">
        <div class="quick-icon" style="background: #8b5cf6">
          <el-icon :size="26"><ChatDotRound /></el-icon>
        </div>
        <div class="quick-info">
          <h4>留言反馈</h4>
          <p>意见与建议</p>
        </div>
        <el-icon class="quick-arrow"><ArrowRight /></el-icon>
      </div>
      <div class="quick-card" @click="router.push('/dorm-application')">
        <div class="quick-icon" style="background: #f59e0b">
          <el-icon :size="26"><Document /></el-icon>
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
          <h3>
            <el-icon class="header-dot"><Notification /></el-icon>
            最新公告
          </h3>
          <el-button type="primary" link @click="router.push('/announcement')">更多 →</el-button>
        </div>
        <div class="announcement-list">
          <div v-for="item in announcements" :key="item.id" class="announcement-item" @click="router.push('/announcement/' + item.id)">
            <div class="ann-left">
              <el-tag v-if="item.isTop === 1" size="small" type="danger" class="top-tag">置顶</el-tag>
              <span class="ann-title">{{ item.title }}</span>
            </div>
            <span class="ann-time">{{ item.publishTime }}</span>
          </div>
          <div v-if="announcements.length === 0" class="empty-hint">暂无公告</div>
        </div>
      </div>

      <div class="info-card">
        <div class="info-card-header">
          <h3>
            <el-icon class="header-dot"><Tools /></el-icon>
            我的报修
          </h3>
          <el-button type="primary" link @click="router.push('/repair')">更多 →</el-button>
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
            <span class="repair-room">{{ item.roomNo }}</span>
          </div>
          <div v-if="repairs.length === 0" class="empty-hint">暂无报修记录</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import {
  Tools, Calendar, ChatDotRound, Document, ArrowRight, Notification
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

const now = new Date()
const day = now.getDate()
const weekdays = ['日', '一', '二', '三', '四', '五', '六']
const weekday = '星期' + weekdays[now.getDay()]

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
  gap: 32px;
}

.welcome-card {
  border-radius: 24px;
  overflow: hidden;
  position: relative;
  background: linear-gradient(135deg, #f97316 0%, #ea580c 40%, #c2410c 100%);
  color: #fff;
  box-shadow: 0 12px 40px rgba(249, 115, 22, 0.3);
}
.welcome-shape {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.06);
}
.shape-1 {
  width: 360px;
  height: 360px;
  top: -120px;
  right: -80px;
}
.shape-2 {
  width: 180px;
  height: 180px;
  bottom: -40px;
  right: 200px;
}
.welcome-content {
  position: relative;
  padding: 48px 56px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.welcome-text h2 {
  margin: 0 0 10px;
  font-size: 34px;
  font-weight: 700;
}
.welcome-text h2 .wave {
  display: inline-block;
  animation: wave 1.5s ease infinite;
  transform-origin: 70% 70%;
}
@keyframes wave {
  0%, 100% { transform: rotate(0deg); }
  25% { transform: rotate(20deg); }
  75% { transform: rotate(-10deg); }
}
.welcome-text p {
  margin: 0;
  font-size: 17px;
  opacity: 0.85;
}
.welcome-date {
  text-align: center;
  background: rgba(255, 255, 255, 0.15);
  border-radius: 16px;
  padding: 20px 36px;
  backdrop-filter: blur(8px);
}
.date-day {
  display: block;
  font-size: 48px;
  font-weight: 800;
  line-height: 1;
}
.date-weekday {
  display: block;
  font-size: 15px;
  opacity: 0.85;
  margin-top: 6px;
}

.quick-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
}
.quick-card {
  background: #fff;
  border-radius: 18px;
  padding: 28px 24px;
  display: flex;
  align-items: center;
  gap: 18px;
  cursor: pointer;
  border: 1px solid #f0efed;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.04);
  transition: all 0.25s ease;
}
.quick-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 16px 36px rgba(0, 0, 0, 0.1);
  border-color: #e7e5e4;
}
.quick-icon {
  width: 60px;
  height: 60px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  flex-shrink: 0;
}
.quick-info h4 {
  margin: 0 0 6px;
  font-size: 17px;
  font-weight: 600;
  color: #1c1917;
}
.quick-info p {
  margin: 0;
  font-size: 14px;
  color: #a8a29e;
}
.quick-arrow {
  margin-left: auto;
  color: #d6d3d1;
  font-size: 20px;
  transition: all 0.2s;
}
.quick-card:hover .quick-arrow {
  color: #f97316;
  transform: translateX(5px);
}

.info-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 28px;
}
.info-card {
  background: #fff;
  border-radius: 18px;
  padding: 36px;
  border: 1px solid #f0efed;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.04);
}
.info-card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 22px;
}
.info-card-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #1c1917;
  display: flex;
  align-items: center;
  gap: 10px;
}
.header-dot {
  color: #f97316;
  font-size: 20px;
}

.announcement-list {
  display: flex;
  flex-direction: column;
}
.announcement-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px 0;
  border-bottom: 1px solid #f5f5f4;
  cursor: pointer;
  transition: all 0.2s;
}
.announcement-item:last-child {
  border-bottom: none;
}
.announcement-item:hover .ann-title {
  color: #f97316;
}
.ann-left {
  display: flex;
  align-items: center;
  gap: 10px;
  min-width: 0;
  flex: 1;
}
.ann-title {
  font-size: 15px;
  color: #44403c;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  transition: color 0.2s;
}
.top-tag {
  flex-shrink: 0;
}
.ann-time {
  font-size: 13px;
  color: #a8a29e;
  white-space: nowrap;
  margin-left: 18px;
}

.repair-list {
  display: flex;
  flex-direction: column;
}
.repair-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px 0;
  border-bottom: 1px solid #f5f5f4;
  cursor: pointer;
  transition: all 0.2s;
}
.repair-item:last-child {
  border-bottom: none;
}
.repair-item:hover {
  background: #fafaf9;
  margin: 0 -10px;
  padding-left: 10px;
  padding-right: 10px;
  border-radius: 8px;
}
.repair-left {
  display: flex;
  align-items: center;
  gap: 12px;
}
.repair-type {
  font-size: 15px;
  color: #44403c;
}
.repair-room {
  font-size: 14px;
  color: #78716c;
}

.empty-hint {
  text-align: center;
  padding: 24px 0;
  color: #a8a29e;
  font-size: 13px;
}

@media (max-width: 768px) {
  .quick-cards {
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;
  }
  .info-grid {
    grid-template-columns: 1fr;
    gap: 20px;
  }
  .welcome-content {
    flex-direction: column;
    align-items: flex-start;
    gap: 20px;
    padding: 32px 28px;
  }
  .welcome-date {
    align-self: flex-end;
  }
  .welcome-text h2 {
    font-size: 26px;
  }
  .welcome-date {
    padding: 14px 24px;
  }
  .date-day {
    font-size: 36px;
  }
  .info-card {
    padding: 24px;
  }
  .quick-card {
    padding: 20px;
  }
  .quick-icon {
    width: 48px;
    height: 48px;
  }
}
</style>
