<template>
  <div class="student-portal">
    <!-- Top bar -->
    <header class="portal-topbar">
      <div class="topbar-inner">
        <div class="brand">
          <div class="brand-mark">
            <svg viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg">
              <rect x="4" y="8" width="24" height="20" rx="4" fill="rgba(255,255,255,0.15)" stroke="rgba(255,255,255,0.6)" stroke-width="1.5"/>
              <path d="M4 13L16 5L28 13" stroke="rgba(255,255,255,0.6)" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
              <rect x="12" y="18" width="8" height="10" rx="2" fill="rgba(255,255,255,0.3)" stroke="rgba(255,255,255,0.7)" stroke-width="1"/>
            </svg>
          </div>
          <span class="brand-text">学生公寓服务</span>
        </div>
        <div class="topbar-right">
          <el-badge :value="unreadCount" :max="99" :hidden="unreadCount === 0">
            <div class="notify-btn">
              <el-icon :size="20"><Bell /></el-icon>
            </div>
          </el-badge>
          <span class="welcome-text">你好，{{ userStore.displayName }}</span>
          <el-dropdown trigger="click" @command="handleCommand">
            <div class="avatar-btn">
              <el-icon :size="20"><User /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout">
                  <el-icon><SwitchButton /></el-icon>
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </header>

    <!-- Body: sidebar + main -->
    <div class="portal-body">
      <aside class="portal-sidebar">
        <nav class="nav-menu">
          <router-link
            v-for="item in menuItems"
            :key="item.path"
            :to="item.path"
            class="nav-item"
            :class="{ active: activeMenu === item.path }"
          >
            <span class="nav-icon-wrap">
              <el-icon class="nav-icon"><component :is="item.icon" /></el-icon>
            </span>
            <span class="nav-label">{{ item.label }}</span>
          </router-link>
        </nav>
      </aside>

      <main class="portal-main">
        <div class="main-head">
          <h1 class="page-name">{{ pageTitle }}</h1>
          <el-breadcrumb separator="/" class="page-breadcrumb">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ pageTitle }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="main-content">
          <router-view v-slot="{ Component }">
            <transition name="page-fade" mode="out-in">
              <component :is="Component" />
            </transition>
          </router-view>
        </div>
      </main>
    </div>

    <transition name="back-top-fade">
      <div class="back-top" v-show="showBackTop" @click="scrollToTop">
        <el-icon :size="18"><Top /></el-icon>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import {
  School, Notification, Tools, Plus, Calendar, ChatDotRound,
  Document, User, SwitchButton, Warning, House, Bell, Top, HomeFilled
} from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import violationApi from '@/api/violation'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const showBackTop = ref(false)
const unreadCount = ref(0)

const activeMenu = computed(() => route.path)
const pageTitle = computed(() => route.meta.title || '首页')

const menuItems = [
  { path: '/home', label: '首页', icon: HomeFilled },
  { path: '/announcement', label: '公告通知', icon: Notification },
  { path: '/repair', label: '报修记录', icon: Tools },
  { path: '/repair/submit', label: '提交报修', icon: Plus },
  { path: '/checkin', label: '入住登记', icon: Calendar },
  { path: '/my-dormitory', label: '我的宿舍', icon: House },
  { path: '/message', label: '留言反馈', icon: ChatDotRound },
  { path: '/dorm-application', label: '宿舍申请', icon: Document },
  { path: '/violation', label: '违规记录', icon: Warning }
]

function handleCommand(cmd) {
  if (cmd === 'logout') {
    userStore.logout()
    router.push('/login')
  }
}

function handleScroll() {
  showBackTop.value = window.scrollY > 300
}

function scrollToTop() {
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

async function checkPendingViolations() {
  try {
    const res = await violationApi.myList('pending')
    const pendingList = res.data || []
    if (pendingList.length > 0) {
      const detail = pendingList.slice(0, 3).map(v =>
        `<p style="margin:6px 0;color:var(--neutral-500);">· ${v.violationType} — ${v.violationTime || ''}</p>`
      ).join('')
      const more = pendingList.length > 3 ? `<p style="margin:6px 0;color:var(--neutral-400);">...还有 ${pendingList.length - 3} 条</p>` : ''
      ElMessageBox.alert(
        `<div style="text-align:left;"><p style="font-size:16px;color:var(--neutral-900);margin:0 0 12px;">您有 <b style="color:var(--student-600);">${pendingList.length}</b> 条待处理的违规记录！</p>${detail}${more}</div>`,
        '违规提醒',
        {
          dangerouslyUseHTMLString: true,
          confirmButtonText: '查看详情',
          type: 'warning',
          center: true
        }
      ).then(() => {
        router.push('/violation')
      }).catch(() => {})
    }
  } catch (e) {}
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
  checkPendingViolations()
})
onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>

<style scoped>
.student-portal {
  min-height: 100vh;
  background: #faf8f5;
  font-family: 'DM Sans', 'Noto Sans SC', system-ui, sans-serif;
}

/* ========== Top bar ========== */
.portal-topbar {
  background: linear-gradient(135deg, #f97316, #ea580c, #c2410c);
  padding: 0 48px;
  height: 64px;
  position: sticky;
  top: 0;
  z-index: 100;
  box-shadow: 0 2px 16px rgba(249, 115, 22, 0.2);
}
.topbar-inner {
  max-width: 100%;
  margin: 0 auto;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.brand {
  display: flex;
  align-items: center;
  gap: 12px;
}
.brand-mark {
  width: 40px;
  height: 40px;
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.12);
  border: 1px solid rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
}
.brand-mark svg {
  width: 22px;
  height: 22px;
}
.brand-text {
  font-family: 'Noto Serif SC', serif;
  font-size: 20px;
  font-weight: 700;
  color: #ffffff;
  letter-spacing: 0.03em;
}
.topbar-right {
  display: flex;
  align-items: center;
  gap: 18px;
}
.notify-btn {
  width: 38px;
  height: 38px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: rgba(255, 255, 255, 0.75);
  cursor: pointer;
  transition: all 0.2s ease;
}
.notify-btn:hover {
  background: rgba(255, 255, 255, 0.12);
  color: #ffffff;
}
.welcome-text {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.75);
  font-weight: 500;
}
.avatar-btn {
  width: 38px;
  height: 38px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.12);
  border: 2px solid rgba(255, 255, 255, 0.25);
  display: flex;
  align-items: center;
  justify-content: center;
  color: rgba(255, 255, 255, 0.85);
  cursor: pointer;
  transition: all 0.2s ease;
}
.avatar-btn:hover {
  background: rgba(255, 255, 255, 0.2);
  border-color: rgba(255, 255, 255, 0.45);
  color: #fff;
}

/* ========== Body ========== */
.portal-body {
  display: flex;
  max-width: 100%;
  margin: 0 auto;
  padding: 36px 48px;
  gap: 36px;
  min-height: calc(100vh - 64px);
}

/* ========== Sidebar ========== */
.portal-sidebar {
  width: 240px;
  flex-shrink: 0;
}
.nav-menu {
  background: #ffffff;
  border-radius: 20px;
  padding: 12px;
  box-shadow: 0 1px 3px rgba(28, 25, 23, 0.04);
  border: 1px solid var(--neutral-200);
  display: flex;
  flex-direction: column;
  gap: 2px;
  position: sticky;
  top: 104px;
}
.nav-item {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 13px 18px;
  border-radius: 12px;
  color: var(--neutral-500);
  text-decoration: none;
  font-size: 15px;
  font-weight: 500;
  transition: all 0.25s ease;
  position: relative;
}
.nav-icon-wrap {
  width: 36px;
  height: 36px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: transparent;
  transition: all 0.25s ease;
}
.nav-icon {
  font-size: 20px;
  flex-shrink: 0;
  transition: transform 0.25s ease;
}
.nav-item:hover {
  background: var(--student-50);
  color: var(--student-600);
}
.nav-item:hover .nav-icon-wrap {
  background: rgba(249, 115, 22, 0.08);
}
.nav-item.active {
  background: linear-gradient(135deg, #fff7ed, #ffedd5);
  color: var(--student-700);
  font-weight: 600;
  box-shadow: inset 3px 0 0 var(--student-500);
}
.nav-item.active .nav-icon-wrap {
  background: rgba(249, 115, 22, 0.12);
}
.nav-item.active .nav-icon {
  color: var(--student-500);
}

/* ========== Main ========== */
.portal-main {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 24px;
}
.main-head {
  padding: 0 4px;
  display: flex;
  align-items: baseline;
  justify-content: space-between;
}
.page-name {
  margin: 0;
  font-family: 'Noto Serif SC', serif;
  font-size: 28px;
  font-weight: 700;
  color: var(--neutral-900);
  letter-spacing: -0.01em;
}
.page-breadcrumb {
  font-size: 14px;
}
.page-breadcrumb :deep(.el-breadcrumb__inner) {
  color: var(--neutral-400);
  font-weight: 500;
  transition: color 0.15s;
}
.page-breadcrumb :deep(.el-breadcrumb__inner:hover) {
  color: var(--student-500);
}
.page-breadcrumb :deep(.el-breadcrumb__item:last-child .el-breadcrumb__inner) {
  color: var(--student-600);
  font-weight: 700;
}
.main-content {
  flex: 1;
  overflow-x: hidden;
}

/* Page transition */
.page-fade-enter-active {
  transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);
}
.page-fade-leave-active {
  transition: all 0.15s ease;
}
.page-fade-enter-from {
  opacity: 0;
  transform: translateY(10px);
}
.page-fade-leave-to {
  opacity: 0;
  transform: translateY(-6px);
}

/* ========== Back to top ========== */
.back-top {
  position: fixed;
  bottom: 40px;
  right: 40px;
  width: 46px;
  height: 46px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--student-500), var(--student-600));
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 4px 20px rgba(249, 115, 22, 0.3);
  transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);
  z-index: 100;
}
.back-top:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 28px rgba(249, 115, 22, 0.4);
}
.back-top-fade-enter-active {
  transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);
}
.back-top-fade-leave-active {
  transition: all 0.2s ease;
}
.back-top-fade-enter-from,
.back-top-fade-leave-to {
  opacity: 0;
  transform: translateY(12px);
}

/* ========== Responsive ========== */
@media (max-width: 768px) {
  .portal-body {
    flex-direction: column;
    padding: 20px 16px;
  }
  .portal-sidebar {
    width: 100%;
  }
  .nav-menu {
    flex-direction: row;
    overflow-x: auto;
    position: static;
    padding: 8px;
    gap: 4px;
    border-radius: 16px;
  }
  .nav-item {
    flex-direction: column;
    padding: 10px 12px;
    font-size: 12px;
    gap: 4px;
    border-radius: 10px;
  }
  .nav-item.active {
    box-shadow: none;
    border-bottom: 2px solid var(--student-500);
  }
  .nav-icon-wrap {
    width: 28px;
    height: 28px;
    border-radius: 8px;
  }
  .nav-icon {
    font-size: 16px;
  }
  .nav-label {
    font-size: 11px;
  }
  .welcome-text {
    display: none;
  }
  .portal-topbar {
    padding: 0 20px;
    height: 56px;
  }
  .brand-text {
    font-size: 17px;
  }
}
</style>
