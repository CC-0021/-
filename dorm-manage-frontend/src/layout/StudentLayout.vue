<template>
  <div class="student-portal">
    <!-- 顶部欢迎条：学生端专属 -->
    <header class="portal-topbar">
      <div class="topbar-inner">
        <div class="brand">
          <span class="brand-icon">
            <el-icon :size="24"><School /></el-icon>
          </span>
          <span class="brand-text">学生公寓服务</span>
        </div>
        <div class="topbar-right">
          <span class="welcome-text">你好，{{ userStore.displayName }}</span>
          <el-dropdown trigger="click" @command="handleCommand" class="user-dropdown">
            <el-button class="avatar-btn" circle>
              <el-icon><User /></el-icon>
            </el-button>
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
      <!-- 左侧导航：卡片式、轻量 -->
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

      <!-- 主内容区 -->
      <main class="portal-main">
        <div class="main-head">
          <h1 class="page-name">{{ pageTitle }}</h1>
        </div>
        <div class="main-content">
          <router-view v-slot="{ Component }">
            <transition name="fade" mode="out-in">
              <component :is="Component" />
            </transition>
          </router-view>
        </div>
      </main>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  School,
  Notification,
  Tools,
  Plus,
  Calendar,
  ChatDotRound,
  Document,
  User,
  SwitchButton,
  Warning,
  House
} from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)
const pageTitle = computed(() => route.meta.title || '首页')

const menuItems = [
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
</script>

<style scoped>
.student-portal {
  min-height: 100vh;
  background: linear-gradient(160deg, #f0f9ff 0%, #e0f2fe 35%, #f0fdfa 100%);
  font-family: 'Noto Sans SC', system-ui, sans-serif;
}

/* 顶部欢迎条 */
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
.welcome-text {
  font-size: 14px;
  opacity: 0.95;
}
.avatar-btn {
  background: rgba(255, 255, 255, 0.2) !important;
  border: none !important;
  color: #fff !important;
}
.avatar-btn:hover {
  background: rgba(255, 255, 255, 0.35) !important;
}

/* 主体：侧栏 + 主区 */
.portal-body {
  display: flex;
  max-width: 1400px;
  margin: 0 auto;
  padding: 24px;
  gap: 24px;
  min-height: calc(100vh - 60px);
}

/* 左侧导航 - 卡片式 */
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
  transition: all 0.2s ease;
}
.nav-item:hover {
  background: #f0f9ff;
  color: #0ea5e9;
}
.nav-item.active {
  background: linear-gradient(135deg, rgba(14, 165, 233, 0.15) 0%, rgba(14, 165, 233, 0.08) 100%);
  color: #0284c7;
  font-weight: 600;
}
.nav-icon {
  font-size: 20px;
}

/* 主内容 */
.portal-main {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 20px;
}
.main-head {
  padding: 0 4px;
}
.page-name {
  margin: 0;
  font-size: 22px;
  font-weight: 700;
  color: #0f172a;
  letter-spacing: -0.02em;
}
.main-content {
  flex: 1;
  overflow-x: hidden;
}
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
