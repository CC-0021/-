<template>
  <div class="admin-dashboard">
    <el-container class="layout">
      <!-- Sidebar -->
      <el-aside :width="isCollapsed ? '68px' : '248px'" class="aside">
        <!-- Logo -->
        <div class="logo" :class="{ collapsed: isCollapsed }">
          <div class="logo-mark">
            <svg viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg">
              <rect x="4" y="8" width="24" height="20" rx="4" fill="rgba(255,255,255,0.08)" stroke="rgba(255,255,255,0.35)" stroke-width="1.5"/>
              <path d="M4 13L16 5L28 13" stroke="rgba(255,255,255,0.35)" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
              <rect x="12" y="18" width="8" height="10" rx="2" fill="rgba(129,140,248,0.3)" stroke="rgba(129,140,248,0.5)" stroke-width="1"/>
            </svg>
          </div>
          <span v-show="!isCollapsed" class="logo-text">管理后台</span>
        </div>

        <div class="aside-divider" />

        <!-- Menu -->
        <el-menu
          :default-active="activeMenu"
          :collapse="isCollapsed"
          router
          class="sidebar-menu"
          background-color="transparent"
          text-color="rgba(255,255,255,0.55)"
          active-text-color="#ffffff"
        >
          <el-menu-item v-for="item in menuItems" :key="item.path" :index="item.path">
            <el-icon><component :is="item.icon" /></el-icon>
            <span>{{ item.label }}</span>
          </el-menu-item>
        </el-menu>

        <!-- Collapse toggle -->
        <div class="collapse-btn" @click="isCollapsed = !isCollapsed">
          <el-icon :size="18">
            <Fold v-if="!isCollapsed" />
            <Expand v-else />
          </el-icon>
        </div>
      </el-aside>

      <!-- Main area -->
      <el-container class="main-wrap">
        <!-- Header -->
        <el-header class="header">
          <div class="header-left">
            <el-breadcrumb separator="/" class="breadcrumb">
              <el-breadcrumb-item :to="{ path: '/admin/dashboard' }">首页</el-breadcrumb-item>
              <el-breadcrumb-item>{{ pageTitle }}</el-breadcrumb-item>
            </el-breadcrumb>
          </div>
          <div class="header-right">
            <!-- Notifications -->
            <el-popover trigger="click" :width="340" placement="bottom-end" popper-class="notify-popover">
              <template #reference>
                <el-badge :value="notifyCount" :max="99" :hidden="notifyCount === 0" class="notify-badge">
                  <div class="notify-btn">
                    <el-icon :size="19"><Bell /></el-icon>
                  </div>
                </el-badge>
              </template>
              <div class="notify-panel">
                <div class="notify-title">待办事宜</div>
                <div v-if="notifyCount === 0" class="notify-empty">暂无待办事项</div>
                <div v-else class="notify-list">
                  <div
                    v-for="item in pendingItems"
                    :key="item.key"
                    class="notify-item"
                    @click="router.push(item.path)"
                  >
                    <div class="notify-item-left">
                      <el-icon :size="15"><component :is="item.icon" /></el-icon>
                      <span>{{ item.label }}</span>
                    </div>
                    <el-tag size="small" type="danger" round>{{ item.count }}</el-tag>
                  </div>
                </div>
              </div>
            </el-popover>

            <!-- User -->
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

        <!-- Content -->
        <el-main class="main">
          <div class="main-inner">
            <router-view v-slot="{ Component }">
              <transition name="page-fade" mode="out-in">
                <component :is="Component" />
              </transition>
            </router-view>
          </div>
          <transition name="back-top-fade">
            <div class="back-top" v-show="showBackTop" @click="scrollToTop">
              <el-icon :size="20"><Top /></el-icon>
            </div>
          </transition>
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
import { adminPage as repairPage } from '@/api/repair'
import { adminPage as checkinPage } from '@/api/checkin'
import { adminPage as dormAppPage } from '@/api/dormApplication'
import { adminPage as messagePage } from '@/api/message'
import violationApi from '@/api/violation'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const isCollapsed = ref(false)
const showBackTop = ref(false)
const notifyCount = ref(0)
const pendingItems = ref([])

const menuItems = [
  { path: '/admin/dashboard', label: '数据概览', icon: DataAnalysis },
  { path: '/admin/announcement', label: '公告管理', icon: Notification },
  { path: '/admin/repair', label: '报修管理', icon: Tools },
  { path: '/admin/checkin', label: '入住审核', icon: Calendar },
  { path: '/admin/message', label: '留言回复', icon: ChatDotRound },
  { path: '/admin/dorm-application', label: '宿舍申请', icon: Document },
  { path: '/admin/dormitory', label: '宿舍管理', icon: OfficeBuilding },
  { path: '/admin/bed', label: '床位管理', icon: FirstAidKit },
  { path: '/admin/employee', label: '员工管理', icon: UserFilled },
  { path: '/admin/violation', label: '违规管理', icon: Warning },
  { path: '/admin/visitor', label: '来访登记', icon: Tickets }
]

const activeMenu = computed(() => route.path)
const pageTitle = computed(() => route.meta.title || '管理')

async function loadNotifyCount() {
  const results = await Promise.allSettled([
    repairPage({ pageNum: 1, pageSize: 1, status: 0 }),
    checkinPage({ pageNum: 1, pageSize: 1, status: 0 }),
    dormAppPage({ pageNum: 1, pageSize: 1, status: 0 }),
    messagePage({ pageNum: 1, pageSize: 1, status: 0 }),
    violationApi.page({ pageNum: 1, pageSize: 1, handleStatus: 'pending' })
  ])
  const counts = results.map(r => r.status === 'fulfilled' ? (r.value.data?.total || 0) : 0)
  notifyCount.value = counts.reduce((a, b) => a + b, 0)

  const defs = [
    { key: 'repair', label: '报修待处理', path: '/admin/repair', icon: 'Tools', count: counts[0] },
    { key: 'checkin', label: '入住待审核', path: '/admin/checkin', icon: 'Calendar', count: counts[1] },
    { key: 'dormApp', label: '宿舍申请待审核', path: '/admin/dorm-application', icon: 'Document', count: counts[2] },
    { key: 'message', label: '留言待回复', path: '/admin/message', icon: 'ChatDotRound', count: counts[3] },
    { key: 'violation', label: '违规待处理', path: '/admin/violation', icon: 'Warning', count: counts[4] }
  ]
  pendingItems.value = defs.filter(d => d.count > 0)
}

function handleCommand(cmd) {
  if (cmd === 'logout') {
    userStore.logout()
    router.push('/login')
  }
}

function handleScroll() {
  showBackTop.value = document.querySelector('.main')?.scrollTop > 300
}

function scrollToTop() {
  document.querySelector('.main')?.scrollTo({ top: 0, behavior: 'smooth' })
}

onMounted(() => {
  document.querySelector('.main')?.addEventListener('scroll', handleScroll)
  loadNotifyCount()
})
onUnmounted(() => {
  document.querySelector('.main')?.removeEventListener('scroll', handleScroll)
})
</script>

<style scoped>
.admin-dashboard {
  min-height: 100vh;
  background: var(--neutral-50);
  font-family: 'DM Sans', 'Noto Sans SC', system-ui, sans-serif;
}
.layout {
  min-height: 100vh;
}

/* ========== Sidebar ========== */
.aside {
  background: linear-gradient(180deg, #1a1744 0%, #1e1b4b 30%, #312e81 70%, #1e1b4b 100%);
  box-shadow: 6px 0 32px rgba(0, 0, 0, 0.2);
  position: relative;
  transition: width 0.3s cubic-bezier(0.16, 1, 0.3, 1);
  display: flex;
  flex-direction: column;
  overflow: hidden;
  border-right: 1px solid rgba(255, 255, 255, 0.06);
}

/* Sidebar subtle glow line on right edge */
.aside::after {
  content: '';
  position: absolute;
  right: 0;
  top: 0;
  bottom: 0;
  width: 1px;
  background: linear-gradient(180deg,
    transparent 0%,
    rgba(129, 140, 248, 0.3) 15%,
    rgba(129, 140, 248, 0.5) 50%,
    rgba(129, 140, 248, 0.3) 85%,
    transparent 100%);
  opacity: 0.6;
}

.logo {
  height: 68px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  padding: 0 20px;
  transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);
  flex-shrink: 0;
  cursor: default;
}
.logo.collapsed {
  padding: 0;
}
.logo-mark {
  width: 38px;
  height: 38px;
  border-radius: 12px;
  background: rgba(129, 140, 248, 0.25);
  border: 1px solid rgba(129, 140, 248, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  transition: all 0.3s ease;
}
.logo-mark svg {
  width: 22px;
  height: 22px;
}
.logo-text {
  color: #e8eaf6;
  font-family: 'Noto Serif SC', serif;
  font-size: 18px;
  font-weight: 700;
  letter-spacing: 0.03em;
  white-space: nowrap;
  overflow: hidden;
}
.aside-divider {
  height: 1px;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.1), transparent);
  margin: 0 18px;
  flex-shrink: 0;
}

/* Menu */
.sidebar-menu {
  border: none;
  padding: 12px 0;
  flex: 1;
  overflow-y: auto;
}
.sidebar-menu .el-menu-item {
  margin: 2px 10px;
  border-radius: 10px;
  height: 44px;
  line-height: 44px;
  font-size: 14px;
  transition: all 0.25s ease;
  position: relative;
}
.sidebar-menu .el-menu-item:hover {
  background: rgba(129, 140, 248, 0.15) !important;
  color: rgba(255, 255, 255, 0.85) !important;
}
.sidebar-menu .el-menu-item.is-active {
  background: linear-gradient(90deg, rgba(129, 140, 248, 0.3), rgba(129, 140, 248, 0.1)) !important;
  color: #ffffff !important;
  font-weight: 600;
  box-shadow: inset 3px 0 0 rgba(165, 180, 252, 0.8);
}

/* Collapse button */
.collapse-btn {
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: rgba(255, 255, 255, 0.4);
  cursor: pointer;
  border-top: 1px solid rgba(255, 255, 255, 0.06);
  transition: all 0.2s ease;
  flex-shrink: 0;
}
.collapse-btn:hover {
  color: #c7d2fe;
  background: rgba(129, 140, 248, 0.12);
}

/* ========== Header ========== */
.main-wrap {
  display: flex;
  flex-direction: column;
  min-width: 0;
}
.header {
  height: 62px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 28px;
  background: #ffffff;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.04);
  border-bottom: 1px solid var(--neutral-100);
  flex-shrink: 0;
}
.breadcrumb {
  font-size: 14px;
}
.breadcrumb :deep(.el-breadcrumb__inner) {
  color: var(--neutral-400);
  font-weight: 500;
  transition: color 0.15s;
}
.breadcrumb :deep(.el-breadcrumb__inner:hover) {
  color: var(--admin-500);
}
.breadcrumb :deep(.el-breadcrumb__item:last-child .el-breadcrumb__inner) {
  color: var(--admin-700);
  font-weight: 700;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

/* Notification button */
.notify-btn {
  width: 38px;
  height: 38px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--neutral-400);
  cursor: pointer;
  transition: all 0.2s ease;
}
.notify-btn:hover {
  background: var(--admin-50);
  color: var(--admin-500);
}
.notify-badge {
  cursor: pointer;
}

/* User info */
.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  padding: 6px 14px 6px 6px;
  border-radius: 12px;
  transition: all 0.2s ease;
}
.user-info:hover {
  background: var(--neutral-50);
}
.user-avatar {
  width: 34px;
  height: 34px;
  border-radius: 10px;
  background: linear-gradient(135deg, var(--admin-500), var(--admin-600));
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
}
.user-name {
  font-size: 14px;
  color: var(--neutral-700);
  font-weight: 600;
}
.arrow {
  font-size: 12px;
  color: var(--neutral-400);
  transition: transform 0.2s;
}
.user-info:hover .arrow {
  transform: translateY(2px);
}

/* ========== Main content ========== */
.main {
  padding: 24px 28px;
  overflow-x: hidden;
  background: var(--neutral-50);
  position: relative;
}
.main-inner {
  min-height: 0;
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

/* Back to top */
.back-top {
  position: fixed;
  bottom: 40px;
  right: 40px;
  width: 46px;
  height: 46px;
  border-radius: 14px;
  background: linear-gradient(135deg, var(--admin-500), var(--admin-600));
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 4px 20px rgba(99, 102, 241, 0.35);
  transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);
  z-index: 100;
}
.back-top:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 28px rgba(99, 102, 241, 0.45);
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
  .aside {
    position: fixed;
    z-index: 999;
    height: 100vh;
  }
  .user-name {
    display: none;
  }
  .header {
    padding: 0 16px;
  }
  .main {
    padding: 16px;
  }
}
</style>

<style>
/* Notification popover global styles */
.notify-popover {
  padding: 0 !important;
  border-radius: 16px !important;
  overflow: hidden;
  border: 1px solid var(--neutral-100) !important;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.1) !important;
}
.notify-panel {
  padding: 0;
}
.notify-title {
  padding: 16px 20px;
  font-size: 15px;
  font-weight: 700;
  color: var(--neutral-900);
  border-bottom: 1px solid var(--neutral-100);
  font-family: 'Noto Serif SC', serif;
}
.notify-empty {
  padding: 36px 20px;
  text-align: center;
  color: var(--neutral-400);
  font-size: 14px;
}
.notify-list {
  padding: 6px;
}
.notify-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 14px;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.2s ease;
}
.notify-item:hover {
  background: var(--admin-50);
}
.notify-item-left {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 14px;
  color: var(--neutral-700);
  font-weight: 500;
}
.notify-item-left .el-icon {
  color: var(--admin-400);
}
</style>
