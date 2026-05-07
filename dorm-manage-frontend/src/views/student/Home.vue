<template>
  <div class="home-page">
    <!-- Welcome banner -->
    <div class="welcome-card">
      <div class="welcome-shapes">
        <div class="wshape wshape-1" />
        <div class="wshape wshape-2" />
        <div class="wshape wshape-3" />
      </div>
      <div class="welcome-content">
        <div class="welcome-text">
          <h2>你好，{{ userStore.displayName }}<span class="wave">👋</span></h2>
          <p>欢迎使用学生公寓服务系统，愿你在宿舍感受到家的温暖</p>
        </div>
        <div class="welcome-date">
          <span class="date-day">{{ day }}</span>
          <span class="date-weekday">{{ weekday }}</span>
        </div>
      </div>
    </div>

    <!-- Quick actions -->
    <div class="quick-cards">
      <div
        v-for="(card, i) in quickCards"
        :key="i"
        class="quick-card"
        @click="router.push(card.path)"
        :style="{ animationDelay: `${0.1 + i * 0.07}s` }"
      >
        <div class="quick-icon" :style="{ background: card.gradient }">
          <el-icon :size="24"><component :is="card.icon" /></el-icon>
        </div>
        <div class="quick-info">
          <h4>{{ card.title }}</h4>
          <p>{{ card.desc }}</p>
        </div>
        <el-icon class="quick-arrow"><ArrowRight /></el-icon>
      </div>
    </div>

    <!-- Info grid: Announcements + Repairs -->
    <div class="info-grid">
      <!-- Latest announcements -->
      <div class="info-card">
        <div class="info-card-header">
          <h3>
            <el-icon class="header-icon"><Notification /></el-icon>
            最新公告
          </h3>
          <el-button type="primary" link @click="router.push('/announcement')">更多 →</el-button>
        </div>
        <div class="announcement-list">
          <div
            v-for="(item, i) in announcements"
            :key="item.id"
            class="announcement-item"
            @click="router.push('/announcement/' + item.id)"
            :style="{ animationDelay: `${0.1 * i}s` }"
          >
            <div class="ann-left">
              <el-tag v-if="item.isTop === 1" size="small" type="danger" class="top-tag">置顶</el-tag>
              <span class="ann-title">{{ item.title }}</span>
            </div>
            <span class="ann-time">{{ item.publishTime }}</span>
          </div>
          <div v-if="announcements.length === 0" class="empty-hint">暂无公告</div>
        </div>
      </div>

      <!-- My repairs -->
      <div class="info-card">
        <div class="info-card-header">
          <h3>
            <el-icon class="header-icon"><Tools /></el-icon>
            我的报修
          </h3>
          <el-button type="primary" link @click="router.push('/repair')">更多 →</el-button>
        </div>
        <div class="repair-list">
          <div
            v-for="(item, i) in repairs"
            :key="item.id"
            class="repair-item"
            @click="router.push('/repair/detail/' + item.id)"
            :style="{ animationDelay: `${0.1 * i}s` }"
          >
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

const quickCards = [
  { title: '提交报修', desc: '宿舍设施报修', icon: Tools, path: '/repair/submit', gradient: 'linear-gradient(135deg, #f97316, #ea580c)' },
  { title: '入住登记', desc: '申请宿舍入住', icon: Calendar, path: '/checkin', gradient: 'linear-gradient(135deg, #10b981, #059669)' },
  { title: '留言反馈', desc: '意见与建议', icon: ChatDotRound, path: '/message', gradient: 'linear-gradient(135deg, #8b5cf6, #7c3aed)' },
  { title: '宿舍申请', desc: '换宿/退宿申请', icon: Document, path: '/dorm-application', gradient: 'linear-gradient(135deg, #f59e0b, #d97706)' }
]

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
  gap: 28px;
}

/* ========== Welcome card ========== */
.welcome-card {
  border-radius: var(--radius-2xl);
  overflow: hidden;
  position: relative;
  background: linear-gradient(135deg, #f97316 0%, #ea580c 35%, #9a3412 100%);
  color: #fff;
  box-shadow: 0 12px 40px rgba(249, 115, 22, 0.25);
}
.welcome-shapes {
  position: absolute;
  inset: 0;
  pointer-events: none;
}
.wshape {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.05);
}
.wshape-1 {
  width: 380px; height: 380px;
  top: -140px; right: -60px;
}
.wshape-2 {
  width: 200px; height: 200px;
  bottom: -60px; right: 220px;
}
.wshape-3 {
  width: 140px; height: 140px;
  top: 30%; left: -40px;
}
.welcome-content {
  position: relative;
  padding: 44px 52px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.welcome-text h2 {
  margin: 0 0 10px;
  font-family: 'Noto Serif SC', serif;
  font-size: 34px;
  font-weight: 800;
  letter-spacing: -0.01em;
}
.welcome-text h2 .wave {
  display: inline-block;
  animation: wave 1.5s ease infinite;
  transform-origin: 70% 70%;
}
@keyframes wave {
  0%, 100% { transform: rotate(0deg); }
  25% { transform: rotate(18deg); }
  75% { transform: rotate(-8deg); }
}
.welcome-text p {
  margin: 0;
  font-size: 16px;
  opacity: 0.8;
  font-weight: 400;
  max-width: 380px;
  line-height: 1.5;
}
.welcome-date {
  text-align: center;
  background: rgba(255, 255, 255, 0.12);
  border-radius: var(--radius-lg);
  padding: 18px 32px;
  backdrop-filter: blur(8px);
  border: 1px solid rgba(255, 255, 255, 0.1);
}
.date-day {
  display: block;
  font-family: 'DM Sans', sans-serif;
  font-size: 48px;
  font-weight: 800;
  line-height: 1;
  letter-spacing: -0.02em;
}
.date-weekday {
  display: block;
  font-size: 14px;
  opacity: 0.8;
  margin-top: 6px;
  font-weight: 500;
}

/* ========== Quick cards ========== */
.quick-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}
.quick-card {
  background: #fff;
  border-radius: var(--radius-xl);
  padding: 26px 22px;
  display: flex;
  align-items: center;
  gap: 16px;
  cursor: pointer;
  border: 1px solid var(--neutral-200);
  box-shadow: var(--shadow-xs);
  transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);
  animation: card-reveal 0.5s cubic-bezier(0.16, 1, 0.3, 1) backwards;
}
@keyframes card-reveal {
  from { opacity: 0; transform: translateY(16px); }
  to { opacity: 1; transform: translateY(0); }
}
.quick-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 16px 40px rgba(28, 25, 23, 0.1);
  border-color: var(--student-300);
}
.quick-icon {
  width: 56px;
  height: 56px;
  border-radius: var(--radius-lg);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  flex-shrink: 0;
  transition: transform 0.25s ease;
}
.quick-card:hover .quick-icon {
  transform: scale(1.1);
}
.quick-info h4 {
  margin: 0 0 5px;
  font-size: 16px;
  font-weight: 700;
  color: var(--neutral-900);
}
.quick-info p {
  margin: 0;
  font-size: 13px;
  color: var(--neutral-400);
}
.quick-arrow {
  margin-left: auto;
  color: var(--neutral-300);
  font-size: 20px;
  transition: all 0.25s ease;
}
.quick-card:hover .quick-arrow {
  color: var(--student-500);
  transform: translateX(6px);
}

/* ========== Info grid ========== */
.info-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
}
.info-card {
  background: #fff;
  border-radius: var(--radius-xl);
  padding: 32px;
  border: 1px solid var(--neutral-200);
  box-shadow: var(--shadow-xs);
}
.info-card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}
.info-card-header h3 {
  margin: 0;
  font-family: 'Noto Serif SC', serif;
  font-size: 18px;
  font-weight: 700;
  color: var(--neutral-900);
  display: flex;
  align-items: center;
  gap: 10px;
}
.header-icon {
  color: var(--student-500);
  font-size: 20px;
}

/* Announcements */
.announcement-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px 0;
  border-bottom: 1px solid var(--neutral-100);
  cursor: pointer;
  transition: all 0.2s ease;
  animation: item-reveal 0.3s ease backwards;
}
@keyframes item-reveal {
  from { opacity: 0; }
  to { opacity: 1; }
}
.announcement-item:last-child {
  border-bottom: none;
}
.announcement-item:hover .ann-title {
  color: var(--student-500);
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
  color: var(--neutral-700);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  transition: color 0.2s;
  font-weight: 500;
}
.top-tag {
  flex-shrink: 0;
}
.ann-time {
  font-size: 13px;
  color: var(--neutral-400);
  white-space: nowrap;
  margin-left: 16px;
}

/* Repairs */
.repair-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px 0;
  border-bottom: 1px solid var(--neutral-100);
  cursor: pointer;
  transition: all 0.2s ease;
  animation: item-reveal 0.3s ease backwards;
}
.repair-item:last-child {
  border-bottom: none;
}
.repair-item:hover {
  background: var(--student-50);
  margin: 0 -12px;
  padding-left: 12px;
  padding-right: 12px;
  border-radius: 8px;
}
.repair-left {
  display: flex;
  align-items: center;
  gap: 12px;
}
.repair-type {
  font-size: 15px;
  color: var(--neutral-700);
  font-weight: 500;
}
.repair-room {
  font-size: 14px;
  color: var(--neutral-500);
}

.empty-hint {
  text-align: center;
  padding: 32px 0;
  color: var(--neutral-400);
  font-size: 14px;
}

/* ========== Responsive ========== */
@media (max-width: 768px) {
  .quick-cards {
    grid-template-columns: repeat(2, 1fr);
    gap: 14px;
  }
  .info-grid {
    grid-template-columns: 1fr;
    gap: 20px;
  }
  .welcome-content {
    flex-direction: column;
    align-items: flex-start;
    gap: 20px;
    padding: 32px 24px;
  }
  .welcome-date {
    align-self: flex-end;
    padding: 14px 24px;
  }
  .welcome-text h2 {
    font-size: 26px;
  }
  .date-day {
    font-size: 36px;
  }
  .info-card {
    padding: 24px;
  }
  .quick-card {
    padding: 18px;
  }
  .quick-icon {
    width: 48px;
    height: 48px;
  }
  .quick-info h4 {
    font-size: 14px;
  }
}
</style>
