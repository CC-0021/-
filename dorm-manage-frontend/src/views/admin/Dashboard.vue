<template>
  <div class="page-container dashboard">
    <div class="page-header">
      <h2 class="page-title">数据概览</h2>
      <span class="page-subtitle">欢迎回来，{{ userStore.displayName }}</span>
    </div>

    <div class="stat-cards">
      <div class="stat-card" style="background: linear-gradient(135deg, #6366f1, #818cf8)">
        <div class="stat-value">{{ stats.dormitoryCount }}</div>
        <div class="stat-label">宿舍总数</div>
        <el-icon class="stat-icon"><OfficeBuilding /></el-icon>
      </div>
      <div class="stat-card" style="background: linear-gradient(135deg, #0ea5e9, #38bdf8)">
        <div class="stat-value">{{ stats.bedAvailable }}</div>
        <div class="stat-label">空闲床位</div>
        <el-icon class="stat-icon"><FirstAidKit /></el-icon>
      </div>
      <div class="stat-card" style="background: linear-gradient(135deg, #f59e0b, #fbbf24)">
        <div class="stat-value">{{ stats.pendingRepair }}</div>
        <div class="stat-label">待处理报修</div>
        <el-icon class="stat-icon"><Tools /></el-icon>
      </div>
      <div class="stat-card" style="background: linear-gradient(135deg, #10b981, #34d399)">
        <div class="stat-value">{{ stats.pendingCheckIn }}</div>
        <div class="stat-label">待审核入住</div>
        <el-icon class="stat-icon"><Calendar /></el-icon>
      </div>
    </div>

    <div class="dashboard-grid">
      <div class="dash-card todo-card">
        <div class="dash-card-header">
          <h3>待办事项</h3>
          <el-tag size="small" type="danger">{{ todoList.length }}项待处理</el-tag>
        </div>
        <div class="todo-list">
          <div v-for="item in todoList" :key="item.id" class="todo-item" @click="router.push(item.path)">
            <div class="todo-left">
              <span class="todo-dot" :class="item.type" />
              <span class="todo-text">{{ item.text }}</span>
            </div>
            <div class="todo-right">
              <span class="todo-time">{{ item.time }}</span>
              <el-icon><ArrowRight /></el-icon>
            </div>
          </div>
          <div v-if="todoList.length === 0" class="empty-state">
            <el-icon class="empty-icon" :size="48"><CircleCheck /></el-icon>
            <div class="empty-text">暂无待办事项</div>
          </div>
        </div>
      </div>

      <div class="dash-card quick-card">
        <div class="dash-card-header">
          <h3>快捷操作</h3>
        </div>
        <div class="quick-grid">
          <div class="quick-item" @click="router.push('/admin/announcement')">
            <div class="quick-icon" style="background: linear-gradient(135deg, #6366f1, #818cf8)">
              <el-icon :size="22"><Notification /></el-icon>
            </div>
            <span>发布公告</span>
          </div>
          <div class="quick-item" @click="router.push('/admin/repair')">
            <div class="quick-icon" style="background: linear-gradient(135deg, #f59e0b, #fbbf24)">
              <el-icon :size="22"><Tools /></el-icon>
            </div>
            <span>处理报修</span>
          </div>
          <div class="quick-item" @click="router.push('/admin/checkin')">
            <div class="quick-icon" style="background: linear-gradient(135deg, #10b981, #34d399)">
              <el-icon :size="22"><Calendar /></el-icon>
            </div>
            <span>入住审核</span>
          </div>
          <div class="quick-item" @click="router.push('/admin/message')">
            <div class="quick-icon" style="background: linear-gradient(135deg, #0ea5e9, #38bdf8)">
              <el-icon :size="22"><ChatDotRound /></el-icon>
            </div>
            <span>回复留言</span>
          </div>
          <div class="quick-item" @click="router.push('/admin/dormitory')">
            <div class="quick-icon" style="background: linear-gradient(135deg, #8b5cf6, #a78bfa)">
              <el-icon :size="22"><OfficeBuilding /></el-icon>
            </div>
            <span>宿舍管理</span>
          </div>
          <div class="quick-item" @click="router.push('/admin/employee')">
            <div class="quick-icon" style="background: linear-gradient(135deg, #ec4899, #f472b6)">
              <el-icon :size="22"><UserFilled /></el-icon>
            </div>
            <span>员工管理</span>
          </div>
        </div>
      </div>
    </div>

    <div class="dash-card recent-card">
      <div class="dash-card-header">
        <h3>最近报修</h3>
        <el-button type="primary" link @click="router.push('/admin/repair')">查看全部</el-button>
      </div>
      <el-table :data="recentRepairs" stripe>
        <el-table-column prop="repairNo" label="报修单号" width="180" />
        <el-table-column prop="repairType" label="类型" width="100" />
        <el-table-column prop="roomNo" label="宿舍号" width="100" />
        <el-table-column prop="description" label="描述" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100">
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
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import {
  OfficeBuilding, FirstAidKit, Tools, Calendar, Notification,
  ChatDotRound, UserFilled, ArrowRight, CircleCheck
} from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { adminPage as repairPage } from '@/api/repair'
import dormitoryApi from '@/api/dormitory'
import { adminPage as checkinPage } from '@/api/checkin'

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

const repairStatusText = (s) => ['待处理', '处理中', '已完成', '已撤销'][s] || ''
const repairStatusClass = (s) => ({ 0: 'warning', 1: 'primary', 2: 'success', 3: 'info' })[s] || 'info'

async function loadStats() {
  try {
    const dormRes = await dormitoryApi.page({ pageNum: 1, pageSize: 1 })
    stats.dormitoryCount = dormRes.data?.total || 0
  } catch (e) {}
  try {
    const repairRes = await repairPage({ pageNum: 1, pageSize: 1, status: 0 })
    stats.pendingRepair = repairRes.data?.total || 0
  } catch (e) {}
  try {
    const checkinRes = await checkinPage({ pageNum: 1, pageSize: 1 })
    stats.pendingCheckIn = checkinRes.data?.total || 0
  } catch (e) {}
}

async function loadRecentRepairs() {
  try {
    const res = await repairPage({ pageNum: 1, pageSize: 5 })
    recentRepairs.value = res.data?.list || []
  } catch (e) {}
}

async function loadTodoList() {
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
.dashboard {
  max-width: 1200px;
}
.page-subtitle {
  font-size: 14px;
  color: #64748b;
  font-weight: 400;
}
.dashboard-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  margin-bottom: 20px;
}
.dash-card {
  background: #fff;
  border-radius: 12px;
  border: 1px solid #e2e8f0;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
  padding: 20px;
  transition: box-shadow 0.2s ease;
}
.dash-card:hover {
  box-shadow: 0 4px 16px rgba(99, 102, 241, 0.1);
}
.dash-card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
}
.dash-card-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #1e1b4b;
}

.todo-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.todo-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 14px;
  border-radius: 10px;
  background: #f8fafc;
  cursor: pointer;
  transition: all 0.2s ease;
}
.todo-item:hover {
  background: #faf5ff;
  transform: translateX(4px);
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
.todo-dot.primary { background: #6366f1; }
.todo-dot.danger { background: #ef4444; }
.todo-text {
  font-size: 14px;
  color: #334155;
}
.todo-right {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #94a3b8;
  font-size: 13px;
}

.quick-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}
.quick-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  padding: 16px 8px;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s ease;
}
.quick-item:hover {
  background: #f8fafc;
  transform: translateY(-2px);
}
.quick-icon {
  width: 48px;
  height: 48px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}
.quick-item span {
  font-size: 13px;
  color: #475569;
  font-weight: 500;
}

.recent-card {
  margin-top: 0;
}

@media (max-width: 768px) {
  .dashboard-grid {
    grid-template-columns: 1fr;
  }
  .quick-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>
