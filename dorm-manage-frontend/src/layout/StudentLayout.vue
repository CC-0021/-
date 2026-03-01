<template>
  <el-container class="layout">
    <el-aside width="220px" class="aside">
      <div class="logo">
        <el-icon :size="28"><School /></el-icon>
        <span>公寓管理</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        router
        class="sidebar-menu"
        background-color="transparent"
        text-color="#94a3b8"
        active-text-color="#0ea5e9"
      >
        <el-menu-item index="/announcement">
          <el-icon><Notification /></el-icon>
          <span>公告</span>
        </el-menu-item>
        <el-menu-item index="/repair">
          <el-icon><Tools /></el-icon>
          <span>报修记录</span>
        </el-menu-item>
        <el-menu-item index="/repair/submit">
          <el-icon><Plus /></el-icon>
          <span>提交报修</span>
        </el-menu-item>
        <el-menu-item index="/checkin">
          <el-icon><Calendar /></el-icon>
          <span>入住登记</span>
        </el-menu-item>
        <el-menu-item index="/message">
          <el-icon><ChatDotRound /></el-icon>
          <span>留言</span>
        </el-menu-item>
        <el-menu-item index="/dorm-application">
          <el-icon><Document /></el-icon>
          <span>宿舍申请</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container class="main-wrap">
      <el-header class="header">
        <div class="header-left">
          <span class="page-name">{{ pageTitle }}</span>
        </div>
        <div class="header-right">
          <span class="user-name">{{ userStore.displayName }}</span>
          <el-dropdown trigger="click" @command="handleCommand">
            <el-button type="primary" link>
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
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { School, Notification, Tools, Plus, Calendar, ChatDotRound, Document, User, ArrowDown } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)
const pageTitle = computed(() => route.meta.title || '首页')

function handleCommand(cmd) {
  if (cmd === 'logout') {
    userStore.logout()
    router.push('/login')
  }
}
</script>

<style scoped>
.layout {
  min-height: 100vh;
  background: var(--app-bg);
}
.aside {
  background: linear-gradient(180deg, #0f172a 0%, #1e293b 100%);
  box-shadow: 2px 0 12px rgba(0, 0, 0, 0.08);
}
.logo {
  height: 56px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  color: #f8fafc;
  font-size: 18px;
  font-weight: 700;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
}
.sidebar-menu {
  border: none;
  padding: 12px 0;
}
.sidebar-menu .el-menu-item {
  margin: 4px 12px;
  border-radius: 8px;
  height: 44px;
  line-height: 44px;
}
.sidebar-menu .el-menu-item.is-active {
  background: rgba(14, 165, 233, 0.15) !important;
  color: #0ea5e9;
}
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
  padding: 0 24px;
  background: #fff;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.06);
}
.page-name {
  font-size: 16px;
  font-weight: 600;
  color: var(--el-text-color-primary);
}
.user-name {
  margin-right: 8px;
  font-size: 14px;
  color: var(--el-text-color-regular);
}
.main {
  padding: 24px;
  background: var(--app-bg);
  overflow-x: hidden;
}
.fade-enter-active, .fade-leave-active { transition: opacity 0.15s ease; }
.fade-enter-from, .fade-leave-to { opacity: 0; }
</style>
