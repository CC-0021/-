<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">床位管理</h2>
    </div>

    <div class="stat-cards">
      <div class="stat-card" style="background: linear-gradient(135deg, #6366f1, #818cf8)">
        <div class="stat-value">{{ total }}</div>
        <div class="stat-label">床位总数</div>
        <el-icon class="stat-icon"><FirstAidKit /></el-icon>
      </div>
      <div class="stat-card" style="background: linear-gradient(135deg, #10b981, #34d399)">
        <div class="stat-value">{{ freeCount }}</div>
        <div class="stat-label">空闲床位</div>
        <el-icon class="stat-icon"><CircleCheck /></el-icon>
      </div>
      <div class="stat-card" style="background: linear-gradient(135deg, #0ea5e9, #38bdf8)">
        <div class="stat-value">{{ occupiedCount }}</div>
        <div class="stat-label">已入住</div>
        <el-icon class="stat-icon"><User /></el-icon>
      </div>
      <div class="stat-card" style="background: linear-gradient(135deg, #f59e0b, #fbbf24)">
        <div class="stat-value">{{ maintenanceCount }}</div>
        <div class="stat-label">维修中</div>
        <el-icon class="stat-icon"><Tools /></el-icon>
      </div>
    </div>

    <div class="search-bar">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="宿舍ID">
          <el-input v-model="searchForm.dormitoryId" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item label="床位号">
          <el-input v-model="searchForm.bedNo" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部" clearable>
            <el-option label="空闲" value="0" />
            <el-option label="已入住" value="1" />
            <el-option label="维修中" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getList">查询</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="action-bar">
      <div class="action-bar-left">
        <el-button type="primary" @click="openAddDialog">
          <el-icon><Plus /></el-icon>新增床位
        </el-button>
        <el-button v-if="selectedIds.length > 0" type="danger" @click="handleBatchDelete">
          <el-icon><Delete /></el-icon>批量删除
        </el-button>
      </div>
      <div class="action-bar-right">
        <el-button @click="getList"><el-icon><Refresh /></el-icon></el-button>
      </div>
    </div>

    <el-table :data="tableData" stripe @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="dormitoryId" label="宿舍ID" />
      <el-table-column prop="bedNo" label="床位号" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <span class="status-badge" :class="bedStatusClass(row.status)">
            <span class="dot" />{{ bedStatusText(row.status) }}
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="studentId" label="学生ID" />
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link size="small" @click="openEditDialog(row)">编辑</el-button>
          <el-button type="danger" link size="small" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-wrap">
      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <el-dialog v-model="dialogVisible" :title="dialogType === 'add' ? '新增床位' : '编辑床位'" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="宿舍ID" required>
          <el-input v-model.number="form.dormitoryId" type="number" placeholder="请输入宿舍ID" />
        </el-form-item>
        <el-form-item label="床位号" required>
          <el-input v-model="form.bedNo" placeholder="请输入床位号" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option label="空闲" value="0" />
            <el-option label="已入住" value="1" />
            <el-option label="维修中" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="学生ID">
          <el-input v-model.number="form.studentId" type="number" placeholder="请输入学生ID" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Delete, Refresh, FirstAidKit, CircleCheck, User, Tools } from '@element-plus/icons-vue'
import bedApi from '@/api/bed'

const tableData = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const selectedIds = ref([])

const searchForm = reactive({ dormitoryId: '', bedNo: '', status: '' })

const dialogVisible = ref(false)
const dialogType = ref('add')
const form = reactive({ id: '', dormitoryId: '', bedNo: '', status: 0, studentId: '' })

const bedStatusText = (s) => ['空闲', '已入住', '维修中'][s] || ''
const bedStatusClass = (s) => ({ 0: 'success', 1: 'primary', 2: 'warning' })[s] || 'info'

const freeCount = computed(() => tableData.value.filter(r => r.status === 0).length)
const occupiedCount = computed(() => tableData.value.filter(r => r.status === 1).length)
const maintenanceCount = computed(() => tableData.value.filter(r => r.status === 2).length)

const getList = () => {
  bedApi.page({
    pageNum: pageNum.value, pageSize: pageSize.value,
    dormitoryId: searchForm.dormitoryId, bedNo: searchForm.bedNo, status: searchForm.status
  }).then(res => { tableData.value = res.data.list; total.value = res.data.total })
}

const resetForm = () => { searchForm.dormitoryId = ''; searchForm.bedNo = ''; searchForm.status = ''; getList() }
const handleSizeChange = (size) => { pageSize.value = size; getList() }
const handleCurrentChange = (current) => { pageNum.value = current; getList() }
const handleSelectionChange = (val) => { selectedIds.value = val.map(item => item.id) }

const openAddDialog = () => {
  dialogType.value = 'add'
  Object.assign(form, { id: '', dormitoryId: '', bedNo: '', status: 0, studentId: '' })
  dialogVisible.value = true
}

const openEditDialog = (row) => { dialogType.value = 'edit'; Object.assign(form, row); dialogVisible.value = true }

const handleSubmit = () => {
  const action = dialogType.value === 'add' ? bedApi.add(form) : bedApi.update(form)
  action.then(() => { dialogVisible.value = false; getList(); ElMessage.success(dialogType.value === 'add' ? '新增成功' : '编辑成功') })
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定要删除吗？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
    .then(() => { bedApi.delete(id).then(() => { getList(); ElMessage.success('删除成功') }) })
}

const handleBatchDelete = () => {
  ElMessageBox.confirm('确定要批量删除吗？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
    .then(() => { bedApi.batchDelete(selectedIds.value).then(() => { getList(); selectedIds.value = []; ElMessage.success('批量删除成功') }) })
}

onMounted(() => { getList() })
</script>
