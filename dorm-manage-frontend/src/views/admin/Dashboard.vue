<template>
  <div class="page-container dashboard">
    <div class="page-header">
      <div>
        <h2 class="page-title">数据概览</h2>
        <span class="page-subtitle">欢迎回来，{{ userStore.displayName }}</span>
      </div>
    </div>

    <!-- Stat cards with staggered reveal -->
    <div class="stat-cards">
      <div
        v-for="(card, i) in statCards"
        :key="i"
        class="stat-card"
        :style="{ background: card.gradient }"
      >
        <div class="stat-value">{{ card.value }}</div>
        <div class="stat-label">{{ card.label }}</div>
        <el-icon class="stat-icon"><component :is="card.icon" /></el-icon>
      </div>
    </div>

    <div class="dashboard-grid">
      <!-- Todo list -->
      <div class="dash-card">
        <div class="dash-card-header">
          <h3>待办事项</h3>
          <el-tag size="small" type="danger" round>{{ todoList.length }}项待处理</el-tag>
        </div>
        <div class="todo-list">
          <div v-for="(item, i) in todoList" :key="item.id" class="todo-item" @click="router.push(item.path)" :style="{ animationDelay: `${0.08 * i}s` }">
            <div class="todo-left">
              <span class="todo-dot" :class="item.type" />
              <span class="todo-text">{{ item.text }}</span>
            </div>
            <div class="todo-right">
              <span class="todo-time">{{ item.time }}</span>
              <el-icon class="todo-arrow"><ArrowRight /></el-icon>
            </div>
          </div>
          <div v-if="todoList.length === 0" class="empty-state">
            <el-icon class="empty-icon" :size="44"><CircleCheck /></el-icon>
            <div class="empty-text">暂无待办事项</div>
          </div>
        </div>
      </div>

      <!-- Quick actions -->
      <div class="dash-card">
        <div class="dash-card-header">
          <h3>快捷操作</h3>
        </div>
        <div class="quick-grid">
          <div
            v-for="(item, i) in quickActions"
            :key="i"
            class="quick-item"
            @click="router.push(item.path)"
            :style="{ animationDelay: `${0.2 + 0.06 * i}s` }"
          >
            <div class="quick-icon" :style="{ background: item.gradient }">
              <el-icon :size="20"><component :is="item.icon" /></el-icon>
            </div>
            <span>{{ item.label }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Recent repairs -->
    <div class="dash-card recent-card">
      <div class="dash-card-header">
        <h3>最近报修</h3>
        <el-button type="primary" link @click="router.push('/admin/repair')">查看全部 →</el-button>
      </div>
      <el-table :data="recentRepairs" stripe>
        <el-table-column prop="repairNo" label="报修单号" width="180" />
        <el-table-column prop="repairType" label="类型" width="100" />
        <el-table-column prop="roomNo" label="宿舍号" width="100" />
        <el-table-column prop="description" label="描述" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="110">
          <template #default="{ row }">
            <span class="status-badge" :class="repairStatusClass(row.status)">
              <span class="dot" />
              {{ repairStatusText(row.status) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="提交时间" width="180" />
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import {
  OfficeBuilding, FirstAidKit, Tools, Calendar, Notification,
  ChatDotRound, UserFilled, ArrowRight, CircleCheck
} from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { adminPage as repairPage } from '@/api/repair'
import dormitoryApi from '@/api/dormitory'
import { adminPage as checkinPage } from '@/api/checkin'
import violationApi from '@/api/violation'
import { adminPage as messageApi } from '@/api/message'

const router = useRouter()
const userStore = useUserStore()

const stats = reactive({
  dormitoryCount: 0,
  bedAvailable: 0,
  pendingRepair: 0,
  pendingCheckIn: 0
})

const todoList = ref([])
const recentRepairs = ref([])

const statCards = computed(() => [
  { value: stats.dormitoryCount, label: '宿舍总数', icon: OfficeBuilding, gradient: 'linear-gradient(135deg, #6366f1 0%, #818cf8 100%)' },
  { value: stats.bedAvailable, label: '空闲床位', icon: FirstAidKit, gradient: 'linear-gradient(135deg, #0ea5e9 0%, #38bdf8 100%)' },
  { value: stats.pendingRepair, label: '待处理报修', icon: Tools, gradient: 'linear-gradient(135deg, #f59e0b 0%, #fbbf24 100%)' },
  { value: stats.pendingCheckIn, label: '待审核入住', icon: Calendar, gradient: 'linear-gradient(135deg, #10b981 0%, #34d399 100%)' }
])

const quickActions = [
  { label: '发布公告', icon: Notification, path: '/admin/announcement', gradient: 'linear-gradient(135deg, #6366f1, #818cf8)' },
  { label: '处理报修', icon: Tools, path: '/admin/repair', gradient: 'linear-gradient(135deg, #f59e0b, #fbbf24)' },
  { label: '入住审核', icon: Calendar, path: '/admin/checkin', gradient: 'linear-gradient(135deg, #10b981, #34d399)' },
  { label: '回复留言', icon: ChatDotRound, path: '/admin/message', gradient: 'linear-gradient(135deg, #0ea5e9, #38bdf8)' },
  { label: '宿舍管理', icon: OfficeBuilding, path: '/admin/dormitory', gradient: 'linear-gradient(135deg, #8b5cf6, #a78bfa)' },
  { label: '员工管理', icon: UserFilled, path: '/admin/employee', gradient: 'linear-gradient(135deg, #ec4899, #f472b6)' }
]

const repairStatusText = (s) => ['待处理', '处理中', '已完成', '已撤销'][s] || ''
const repairStatusClass = (s) => ({ 0: 'warning', 1: 'primary', 2: 'success', 3: 'info' })[s] || 'info'

async function loadStats() {
  try {
    const dormRes = await dormitoryApi.stats()
    stats.dormitoryCount = dormRes.data?.dormitoryCount || 0
    stats.bedAvailable = dormRes.data?.bedAvailable || 0
  } catch (e) {}
  try {
    const repairRes = await repairPage({ pageNum: 1, pageSize: 1, status: 0 })
    stats.pendingRepair = repairRes.data?.total || 0
  } catch (e) {}
  try {
    const checkinRes = await checkinPage({ pageNum: 1, pageSize: 1, status: 0 })
    stats.pendingCheckIn = checkinRes.data?.total || 0
  } catch (e) {}
}

async function loadRecentRepairs() {
  try {
    const res = await repairPage({ pageNum: 1, pageSize: 5 })
    recentRepairs.value = res.data?.list || []
  } catch (e) {}
}

function loadTodoList() {
  const todos = []
  if (stats.pendingRepair > 0) {
    todos.push({ id: 1, text: `${stats.pendingRepair}条报修待处理`, type: 'warning', time: '刚刚', path: '/admin/repair' })
  }
  if (stats.pendingCheckIn > 0) {
    todos.push({ id: 2, text: `${stats.pendingCheckIn}条入住待审核`, type: 'primary', time: '刚刚', path: '/admin/checkin' })
  }
  todoList.value = todos
}

onMounted(async () => {
  await loadStats()
  loadTodoList()
  loadRecentRepairs()
})
</script>

<style scoped>
.page-subtitle {
  display: block;
  font-size: 13px;
  color: var(--neutral-400);
  font-weight: 500;
  margin-top: 4px;
}

/* Dashboard grid */
.dashboard-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  margin-bottom: 20px;
}

/* Dash card */
.dash-card {
  background: #fff;
  border-radius: var(--radius-xl);
  border: 1px solid var(--neutral-100);
  box-shadow: var(--shadow-sm);
  padding: 24px;
  transition: box-shadow var(--duration-normal) ease;
}
.dash-card:hover {
  box-shadow: var(--shadow-md);
}
.dash-card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 18px;
}
.dash-card-header h3 {
  margin: 0;
  font-family: 'Noto Serif SC', serif;
  font-size: 16px;
  font-weight: 700;
  color: var(--neutral-900);
}

/* Todo list */
.todo-list {
  display: flex;
  flex-direction: column;
  gap: 6px;
}
.todo-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 14px;
  border-radius: 10px;
  background: var(--neutral-50);
  cursor: pointer;
  transition: all 0.25s ease;
  animation: todo-reveal 0.4s var(--ease-out-expo) backwards;
}
@keyframes todo-reveal {
  from { opacity: 0; transform: translateX(-10px); }
  to { opacity: 1; transform: translateX(0); }
}
.todo-item:hover {
  background: var(--admin-50);
  transform: translateX(6px);
}
.todo-left {
  display: flex;
  align-items: center;
  gap: 10px;
}
.todo-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  flex-shrink: 0;
}
.todo-dot.warning { background: #f59e0b; }
.todo-dot.primary { background: var(--admin-500); }
.todo-dot.danger { background: #ef4444; }
.todo-text {
  font-size: 14px;
  color: var(--neutral-700);
  font-weight: 500;
}
.todo-right {
  display: flex;
  align-items: center;
  gap: 8px;
  color: var(--neutral-400);
  font-size: 13px;
}
.todo-arrow {
  opacity: 0;
  transform: translateX(-4px);
  transition: all 0.2s ease;
}
.todo-item:hover .todo-arrow {
  opacity: 1;
  transform: translateX(0);
  color: var(--admin-500);
}

/* Quick actions */
.quick-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 14px;
}
.quick-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  padding: 18px 8px;
  border-radius: 14px;
  cursor: pointer;
  transition: all 0.25s ease;
  animation: quick-reveal 0.4s var(--ease-out-expo) backwards;
}
@keyframes quick-reveal {
  from { opacity: 0; transform: scale(0.9); }
  to { opacity: 1; transform: scale(1); }
}
.quick-item:hover {
  background: var(--neutral-50);
  transform: translateY(-3px);
}
.quick-icon {
  width: 46px;
  height: 46px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  transition: transform 0.2s ease;
}
.quick-item:hover .quick-icon {
  transform: scale(1.08);
}
.quick-item span {
  font-size: 13px;
  color: var(--neutral-600);
  font-weight: 600;
}

/* Recent repairs */
.recent-card {
  margin-top: 0;
}

/* Responsive */
@media (max-width: 768px) {
  .dashboard-grid {
    grid-template-columns: 1fr;
  }
  .quick-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  .dash-card {
    padding: 20px;
  }
}
</style>
