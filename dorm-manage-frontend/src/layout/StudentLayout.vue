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
            <el-icon :size="22" class="notify-icon"><Bell /></el-icon>
          </el-badge>
          <span class="welcome-text">你好，{{ userStore.displayName }}</span>
          <el-dropdown trigger="click" @command="handleCommand" class="user-dropdown">
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
            <el-icon class="nav-icon"><component :is="item.icon" /></el-icon>
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
  background: #fafaf9;
  font-family: 'Noto Sans SC', system-ui, sans-serif;
}

.portal-topbar {
  background: #fff;
  border-bottom: 1px solid #f0efed;
  padding: 0 48px;
  height: 68px;
  position: sticky;
  top: 0;
  z-index: 100;
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
.brand-icon {
  width: 42px;
  height: 42px;
  border-radius: 12px;
  background: linear-gradient(135deg, #f97316, #ea580c);
  color: #fff;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}
.brand-text {
  font-size: 22px;
  font-weight: 700;
  color: #1c1917;
  letter-spacing: 0.5px;
}
.topbar-right {
  display: flex;
  align-items: center;
  gap: 20px;
}
.notify-badge {
  cursor: pointer;
}
.notify-icon {
  color: #78716c;
  transition: color 0.2s;
}
.notify-icon:hover {
  color: #f97316;
}
.welcome-text {
  font-size: 15px;
  color: #78716c;
}
.avatar-btn {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: #fafaf9;
  border: 2px solid #f0efed;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #78716c;
  cursor: pointer;
  transition: all 0.2s;
}
.avatar-btn:hover {
  border-color: #f97316;
  color: #f97316;
}

.portal-body {
  display: flex;
  max-width: 100%;
  margin: 0 auto;
  padding: 32px 48px;
  gap: 36px;
  min-height: calc(100vh - 68px);
}

.portal-sidebar {
  width: 260px;
  flex-shrink: 0;
}
.nav-menu {
  background: #fff;
  border-radius: 18px;
  padding: 16px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.04);
  border: 1px solid #f0efed;
  display: flex;
  flex-direction: column;
  gap: 4px;
  position: sticky;
  top: 100px;
}
.nav-item {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 14px 18px;
  border-radius: 12px;
  color: #78716c;
  text-decoration: none;
  font-size: 16px;
  font-weight: 500;
  transition: all 0.2s ease;
  position: relative;
}
.nav-item::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 3px;
  height: 0;
  border-radius: 0 3px 3px 0;
  background: #f97316;
  transition: height 0.2s ease;
}
.nav-item:hover {
  background: #fff7ed;
  color: #f97316;
}
.nav-item.active {
  background: #fff7ed;
  color: #ea580c;
  font-weight: 600;
}
.nav-item.active::before {
  height: 20px;
}
.nav-icon {
  font-size: 22px;
  flex-shrink: 0;
}

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
  font-size: 28px;
  font-weight: 700;
  color: #1c1917;
  letter-spacing: -0.02em;
}
.page-breadcrumb {
  font-size: 14px;
}
.page-breadcrumb :deep(.el-breadcrumb__inner) {
  color: #a8a29e;
}
.page-breadcrumb :deep(.el-breadcrumb__item:last-child .el-breadcrumb__inner) {
  color: #ea580c;
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
  border-radius: 50%;
  background: linear-gradient(135deg, #f97316, #ea580c);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 4px 16px rgba(249, 115, 22, 0.35);
  transition: all 0.3s ease;
  z-index: 100;
}
.back-top:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 24px rgba(249, 115, 22, 0.45);
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
    padding: 10px;
    gap: 6px;
    border-radius: 14px;
  }
  .nav-item {
    flex-direction: column;
    padding: 10px 14px;
    font-size: 13px;
    gap: 5px;
  }
  .nav-item::before {
    display: none;
  }
  .nav-icon {
    font-size: 18px;
  }
  .nav-label {
    font-size: 12px;
  }
  .welcome-text {
    display: none;
  }
  .portal-topbar {
    padding: 0 20px;
  }
}
</style>
