<template>
  <div class="admin-dashboard">
    <el-container class="layout">
      <el-aside :width="isCollapsed ? '64px' : '240px'" class="aside">
        <div class="logo" :class="{ collapsed: isCollapsed }">
          <span class="logo-icon">
            <el-icon :size="22"><Setting /></el-icon>
          </span>
          <span v-show="!isCollapsed" class="logo-text">管理后台</span>
        </div>
        <div class="aside-divider" />
        <el-menu
          :default-active="activeMenu"
          :collapse="isCollapsed"
          router
          class="sidebar-menu"
          background-color="transparent"
          text-color="#a5b4fc"
          active-text-color="#e0e7ff"
        >
          <el-menu-item index="/admin/dashboard">
            <el-icon><DataAnalysis /></el-icon>
            <span>数据概览</span>
          </el-menu-item>
          <el-menu-item index="/admin/announcement">
            <el-icon><Notification /></el-icon>
            <span>公告管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/repair">
            <el-icon><Tools /></el-icon>
            <span>报修管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/checkin">
            <el-icon><Calendar /></el-icon>
            <span>入住审核</span>
          </el-menu-item>
          <el-menu-item index="/admin/message">
            <el-icon><ChatDotRound /></el-icon>
            <span>留言回复</span>
          </el-menu-item>
          <el-menu-item index="/admin/dorm-application">
            <el-icon><Document /></el-icon>
            <span>宿舍申请</span>
          </el-menu-item>
          <el-menu-item index="/admin/dormitory">
            <el-icon><OfficeBuilding /></el-icon>
            <span>宿舍管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/bed">
            <el-icon><FirstAidKit /></el-icon>
            <span>床位管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/employee">
            <el-icon><UserFilled /></el-icon>
            <span>员工管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/violation">
            <el-icon><Warning /></el-icon>
            <span>违规管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/visitor">
            <el-icon><Tickets /></el-icon>
            <span>来访登记</span>
          </el-menu-item>
        </el-menu>
        <div class="collapse-btn" @click="isCollapsed = !isCollapsed">
          <el-icon :size="18">
            <Fold v-if="!isCollapsed" />
            <Expand v-else />
          </el-icon>
        </div>
      </el-aside>

      <el-container class="main-wrap">
        <el-header class="header">
          <div class="header-left">
            <el-breadcrumb separator="/" class="breadcrumb">
              <el-breadcrumb-item :to="{ path: '/admin/dashboard' }">首页</el-breadcrumb-item>
              <el-breadcrumb-item>{{ pageTitle }}</el-breadcrumb-item>
            </el-breadcrumb>
          </div>
          <div class="header-right">
            <el-badge :value="3" :max="99" class="notify-badge">
              <el-icon :size="20" class="notify-icon"><Bell /></el-icon>
            </el-badge>
            <el-dropdown trigger="click" @command="handleCommand">
              <div class="user-info">
                <div class="user-avatar">
                  <el-icon><User /></el-icon>
                </div>
                <span class="user-name">{{ userStore.displayName }}</span>
                <el-icon class="arrow"><ArrowDown /></el-icon>
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
        </el-header>

        <el-main class="main">
          <div class="main-inner">
            <router-view v-slot="{ Component }">
              <transition name="slide-left" mode="out-in">
                <component :is="Component" />
              </transition>
            </router-view>
          </div>
          <div class="back-top" v-show="showBackTop" @click="scrollToTop">
            <el-icon :size="20"><Top /></el-icon>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  Setting, Notification, Tools, Calendar, ChatDotRound, Document,
  User, ArrowDown, OfficeBuilding, FirstAidKit, UserFilled, Warning,
  Tickets, Fold, Expand, Bell, SwitchButton, DataAnalysis, Top
} from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const isCollapsed = ref(false)
const showBackTop = ref(false)

const activeMenu = computed(() => route.path)
const pageTitle = computed(() => route.meta.title || '管理')

function handleCommand(cmd) {
  if (cmd === 'logout') {
    userStore.logout()
    router.push('/login')
  }
}

function handleScroll() {
  showBackTop.value = document.querySelector('.main')?.scrollTop > 200
}

function scrollToTop() {
  document.querySelector('.main')?.scrollTo({ top: 0, behavior: 'smooth' })
}

onMounted(() => {
  document.querySelector('.main')?.addEventListener('scroll', handleScroll)
})
onUnmounted(() => {
  document.querySelector('.main')?.removeEventListener('scroll', handleScroll)
})
</script>

<style scoped>
.admin-dashboard {
  min-height: 100vh;
  background: #f1f5f9;
  font-family: 'Noto Sans SC', system-ui, sans-serif;
}
.layout {
  min-height: 100vh;
}

.aside {
  background: linear-gradient(180deg, #1e1b4b 0%, #312e81 50%, #1e1b4b 100%);
  box-shadow: 4px 0 24px rgba(0, 0, 0, 0.15);
  position: relative;
  transition: width 0.3s ease;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}
.logo {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  padding: 0 20px;
  transition: all 0.3s ease;
  flex-shrink: 0;
}
.logo.collapsed {
  padding: 0;
}
.logo-icon {
  width: 36px;
  height: 36px;
  border-radius: 10px;
  background: rgba(139, 92, 246, 0.4);
  color: #e0e7ff;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}
.logo-text {
  color: #e0e7ff;
  font-size: 17px;
  font-weight: 700;
  letter-spacing: 0.5px;
  white-space: nowrap;
  overflow: hidden;
}
.aside-divider {
  height: 1px;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.12), transparent);
  margin: 0 16px;
  flex-shrink: 0;
}
.sidebar-menu {
  border: none;
  padding: 16px 0;
  flex: 1;
  overflow-y: auto;
}
.sidebar-menu .el-menu-item {
  margin: 2px 12px;
  border-radius: 10px;
  height: 46px;
  line-height: 46px;
  font-size: 14px;
  transition: all 0.2s ease;
}
.sidebar-menu .el-menu-item:hover {
  background: rgba(139, 92, 246, 0.2) !important;
  color: #c4b5fd !important;
}
.sidebar-menu .el-menu-item.is-active {
  background: linear-gradient(90deg, rgba(139, 92, 246, 0.35), rgba(139, 92, 246, 0.15)) !important;
  color: #e0e7ff !important;
  font-weight: 600;
}
.collapse-btn {
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #a5b4fc;
  cursor: pointer;
  border-top: 1px solid rgba(255, 255, 255, 0.08);
  transition: all 0.2s ease;
  flex-shrink: 0;
}
.collapse-btn:hover {
  color: #e0e7ff;
  background: rgba(139, 92, 246, 0.2);
}

.main-wrap {
  display: flex;
  flex-direction: column;
  min-width: 0;
}
.header {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 28px;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
  border-bottom: 1px solid #e2e8f0;
}
.breadcrumb {
  font-size: 14px;
}
.breadcrumb :deep(.el-breadcrumb__inner) {
  color: #64748b;
  font-weight: 500;
}
.breadcrumb :deep(.el-breadcrumb__item:last-child .el-breadcrumb__inner) {
  color: #312e81;
  font-weight: 600;
}
.header-right {
  display: flex;
  align-items: center;
  gap: 24px;
}
.notify-badge {
  cursor: pointer;
}
.notify-icon {
  color: #64748b;
  transition: color 0.2s;
}
.notify-icon:hover {
  color: #6366f1;
}
.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  padding: 6px 12px;
  border-radius: 10px;
  transition: background 0.2s;
}
.user-info:hover {
  background: #f8fafc;
}
.user-avatar {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  background: linear-gradient(135deg, #6366f1, #818cf8);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
}
.user-name {
  font-size: 14px;
  color: #334155;
  font-weight: 500;
}
.arrow {
  font-size: 12px;
  color: #94a3b8;
}

.main {
  padding: 24px;
  overflow-x: hidden;
  background: #f1f5f9;
  position: relative;
}
.main-inner {
  min-height: 0;
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
  background: linear-gradient(135deg, #6366f1, #4f46e5);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 4px 16px rgba(99, 102, 241, 0.4);
  transition: all 0.3s ease;
  z-index: 100;
}
.back-top:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 20px rgba(99, 102, 241, 0.5);
}

@media (max-width: 768px) {
  .aside {
    position: fixed;
    z-index: 999;
    height: 100vh;
  }
  .user-name {
    display: none;
  }
}
</style>
