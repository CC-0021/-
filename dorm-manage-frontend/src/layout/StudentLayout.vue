<template>
  <div class="student-portal">
    <header class="portal-topbar">
      <div class="topbar-inner">
        <div class="brand">
          <span class="brand-icon">
            <el-icon :size="24"><School /></el-icon>
          </span>
          <span class="brand-text">学生公寓服务</span>
        </div>
        <div class="topbar-right">
          <el-badge :value="unreadCount" :max="99" :hidden="unreadCount === 0" class="notify-badge">
            <el-icon :size="20" class="notify-icon"><Bell /></el-icon>
          </el-badge>
          <span class="welcome-text">你好，{{ userStore.displayName }}</span>
          <el-dropdown trigger="click" @command="handleCommand" class="user-dropdown">
            <div class="avatar-btn">
              <el-icon :size="18"><User /></el-icon>
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
            <div class="nav-icon-wrap">
              <el-icon class="nav-icon"><component :is="item.icon" /></el-icon>
            </div>
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
            <transition name="slide-left" mode="out-in">
              <component :is="Component" />
            </transition>
          </router-view>
        </div>
      </main>
    </div>

    <div class="back-top" v-show="showBackTop" @click="scrollToTop">
      <el-icon :size="18"><Top /></el-icon>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  School, Notification, Tools, Plus, Calendar, ChatDotRound,
  Document, User, SwitchButton, Warning, House, Bell, Top, HomeFilled
} from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const showBackTop = ref(false)
const unreadCount = ref(0)

const activeMenu = computed(() => route.path)
const pageTitle = computed(() => route.meta.title || '首页')

const menuItems = [
  { path: '/home', label: '首页', icon: HomeFilled },
  { path: '/announcement', label: '公告', icon: Notification },
  { path: '/repair', label: '报修记录', icon: Tools },
  { path: '/repair/submit', label: '提交报修', icon: Plus },
  { path: '/checkin', label: '入住登记', icon: Calendar },
  { path: '/my-dormitory', label: '我的宿舍', icon: House },
  { path: '/message', label: '留言', icon: ChatDotRound },
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
  showBackTop.value = window.scrollY > 200
}

function scrollToTop() {
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
})
onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>

<style scoped>
.student-portal {
  min-height: 100vh;
  background: linear-gradient(160deg, #f0f9ff 0%, #e0f2fe 35%, #f0fdfa 100%);
  font-family: 'Noto Sans SC', system-ui, sans-serif;
}

.portal-topbar {
  background: linear-gradient(135deg, #0ea5e9 0%, #0284c7 100%);
  color: #fff;
  padding: 0 28px;
  height: 60px;
  box-shadow: 0 4px 20px rgba(14, 165, 233, 0.25);
  position: sticky;
  top: 0;
  z-index: 100;
}
.topbar-inner {
  max-width: 1400px;
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
.brand-icon {
  width: 40px;
  height: 40px;
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.2);
  display: inline-flex;
  align-items: center;
  justify-content: center;
}
.brand-text {
  font-size: 20px;
  font-weight: 700;
  letter-spacing: 0.5px;
}
.topbar-right {
  display: flex;
  align-items: center;
  gap: 16px;
}
.notify-badge {
  cursor: pointer;
}
.notify-icon {
  color: rgba(255, 255, 255, 0.85);
  transition: color 0.2s;
}
.notify-icon:hover {
  color: #fff;
}
.welcome-text {
  font-size: 14px;
  opacity: 0.95;
}
.avatar-btn {
  width: 36px;
  height: 36px;
  border-radius: 10px;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  cursor: pointer;
  transition: background 0.2s;
}
.avatar-btn:hover {
  background: rgba(255, 255, 255, 0.35);
}

.portal-body {
  display: flex;
  max-width: 1400px;
  margin: 0 auto;
  padding: 24px;
  gap: 24px;
  min-height: calc(100vh - 60px);
}

.portal-sidebar {
  width: 200px;
  flex-shrink: 0;
}
.nav-menu {
  background: #fff;
  border-radius: 16px;
  padding: 12px;
  box-shadow: 0 4px 24px rgba(14, 165, 233, 0.08);
  border: 1px solid rgba(14, 165, 233, 0.12);
  display: flex;
  flex-direction: column;
  gap: 4px;
  position: sticky;
  top: 84px;
}
.nav-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  border-radius: 12px;
  color: #64748b;
  text-decoration: none;
  font-size: 15px;
  transition: all 0.25s ease;
}
.nav-item:hover {
  background: #f0f9ff;
  color: #0ea5e9;
  transform: translateX(4px);
}
.nav-item.active {
  background: linear-gradient(135deg, rgba(14, 165, 233, 0.15) 0%, rgba(14, 165, 233, 0.08) 100%);
  color: #0284c7;
  font-weight: 600;
}
.nav-icon-wrap {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f1f5f9;
  transition: all 0.25s ease;
}
.nav-item:hover .nav-icon-wrap {
  background: rgba(14, 165, 233, 0.1);
}
.nav-item.active .nav-icon-wrap {
  background: rgba(14, 165, 233, 0.15);
}
.nav-icon {
  font-size: 18px;
}

.portal-main {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 20px;
}
.main-head {
  padding: 0 4px;
  display: flex;
  align-items: baseline;
  justify-content: space-between;
}
.page-name {
  margin: 0;
  font-size: 22px;
  font-weight: 700;
  color: #0f172a;
  letter-spacing: -0.02em;
}
.page-breadcrumb {
  font-size: 13px;
}
.page-breadcrumb :deep(.el-breadcrumb__inner) {
  color: #94a3b8;
}
.page-breadcrumb :deep(.el-breadcrumb__item:last-child .el-breadcrumb__inner) {
  color: #0284c7;
}
.main-content {
  flex: 1;
  overflow-x: hidden;
}

.slide-left-enter-active,
.slide-left-leave-active {
  transition: all 0.25s ease;
}
.slide-left-enter-from {
  opacity: 0;
  transform: translateX(20px);
}
.slide-left-leave-to {
  opacity: 0;
  transform: translateX(-20px);
}

.back-top {
  position: fixed;
  bottom: 40px;
  right: 40px;
  width: 44px;
  height: 44px;
  border-radius: 12px;
  background: linear-gradient(135deg, #0ea5e9, #0284c7);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 4px 16px rgba(14, 165, 233, 0.4);
  transition: all 0.3s ease;
  z-index: 100;
}
.back-top:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 20px rgba(14, 165, 233, 0.5);
}

@media (max-width: 768px) {
  .portal-body {
    flex-direction: column;
    padding: 16px;
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
  }
  .nav-item {
    flex-direction: column;
    padding: 8px 12px;
    font-size: 12px;
    gap: 4px;
  }
  .nav-icon-wrap {
    width: 28px;
    height: 28px;
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
}
</style>
