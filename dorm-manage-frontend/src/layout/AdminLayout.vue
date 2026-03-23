<template>
  <div class="admin-dashboard">
    <el-container class="layout">
      <!-- 深色侧边栏：管理后台专属 -->
      <el-aside width="240px" class="aside">
        <div class="logo">
          <span class="logo-icon">
            <el-icon :size="22"><Setting /></el-icon>
          </span>
          <span class="logo-text">管理后台</span>
        </div>
        <div class="aside-divider" />
        <el-menu
          :default-active="activeMenu"
          router
          class="sidebar-menu"
          background-color="transparent"
          text-color="#a5b4fc"
          active-text-color="#e0e7ff"
        >
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
        </el-menu>
        <div class="aside-footer">
          <el-button type="primary" link class="back-student" @click="router.push('/announcement')">
            <el-icon><Back /></el-icon>
            返回学生端
          </el-button>
        </div>
      </el-aside>

      <el-container class="main-wrap">
        <!-- 顶栏：白底 + 面包屑 + 用户 -->
        <el-header class="header">
          <div class="header-left">
            <el-breadcrumb separator="/" class="breadcrumb">
              <el-breadcrumb-item :to="{ path: '/admin' }">首页</el-breadcrumb-item>
              <el-breadcrumb-item>{{ pageTitle }}</el-breadcrumb-item>
            </el-breadcrumb>
          </div>
          <div class="header-right">
            <span class="user-name">{{ userStore.displayName }}</span>
            <el-dropdown trigger="click" @command="handleCommand">
              <el-button class="header-btn" type="primary" link>
                <el-icon><User /></el-icon>
                <el-icon class="el-icon--right"><ArrowDown /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>

        <el-main class="main">
          <div class="main-inner">
            <router-view v-slot="{ Component }">
              <transition name="fade" mode="out-in">
                <component :is="Component" />
              </transition>
            </router-view>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  Setting,
  Notification,
  Tools,
  Calendar,
  ChatDotRound,
  Document,
  User,
  ArrowDown,
  Back
} from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)
const pageTitle = computed(() => route.meta.title || '管理')

function handleCommand(cmd) {
  if (cmd === 'logout') {
    userStore.logout()
    router.push('/login')
  }
}
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

/* 深色侧边栏 - 管理端专属 */
.aside {
  background: linear-gradient(180deg, #1e1b4b 0%, #312e81 50%, #1e1b4b 100%);
  box-shadow: 4px 0 24px rgba(0, 0, 0, 0.15);
  position: relative;
}
.logo {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  padding: 0 20px;
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
}
.logo-text {
  color: #e0e7ff;
  font-size: 17px;
  font-weight: 700;
  letter-spacing: 0.5px;
}
.aside-divider {
  height: 1px;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.12), transparent);
  margin: 0 16px;
}
.sidebar-menu {
  border: none;
  padding: 16px 0;
}
.sidebar-menu .el-menu-item {
  margin: 2px 12px;
  border-radius: 10px;
  height: 46px;
  line-height: 46px;
  font-size: 14px;
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
.aside-footer {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 16px;
  border-top: 1px solid rgba(255, 255, 255, 0.08);
}
.back-student {
  color: #a5b4fc !important;
  font-size: 13px;
}
.back-student:hover {
  color: #e0e7ff !important;
}

/* 顶栏 */
.main-wrap {
  display: flex;
  flex-direction: column;
  min-width: 0;
}
.header {
  height: 56px;
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
.user-name {
  margin-right: 10px;
  font-size: 14px;
  color: #475569;
}
.header-btn {
  color: #64748b !important;
}

/* 主内容区 */
.main {
  padding: 24px;
  overflow-x: hidden;
  background: #f1f5f9;
}
.main-inner {
  min-height: 0;
}
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.15s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
