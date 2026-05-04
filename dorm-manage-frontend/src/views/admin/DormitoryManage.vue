<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">宿舍管理</h2>
    </div>

    <div class="stat-cards">
      <div class="stat-card" style="background: linear-gradient(135deg, #6366f1, #818cf8)">
        <div class="stat-value">{{ total }}</div>
        <div class="stat-label">宿舍总数</div>
        <el-icon class="stat-icon"><OfficeBuilding /></el-icon>
      </div>
      <div class="stat-card" style="background: linear-gradient(135deg, #10b981, #34d399)">
        <div class="stat-value">{{ normalCount }}</div>
        <div class="stat-label">正常宿舍</div>
        <el-icon class="stat-icon"><CircleCheck /></el-icon>
      </div>
      <div class="stat-card" style="background: linear-gradient(135deg, #f59e0b, #fbbf24)">
        <div class="stat-value">{{ repairCount }}</div>
        <div class="stat-label">维修中</div>
        <el-icon class="stat-icon"><Tools /></el-icon>
      </div>
      <div class="stat-card" style="background: linear-gradient(135deg, #0ea5e9, #38bdf8)">
        <div class="stat-value">{{ totalBeds }}</div>
        <div class="stat-label">总床位</div>
        <el-icon class="stat-icon"><FirstAidKit /></el-icon>
      </div>
    </div>

    <div class="search-bar">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="楼栋号">
          <el-input v-model="searchForm.buildingNo" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item label="房间号">
          <el-input v-model="searchForm.roomNo" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部" clearable>
            <el-option label="正常" value="1" />
            <el-option label="维修中" value="0" />
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
          <el-icon><Plus /></el-icon>新增宿舍
        </el-button>
        <el-button v-if="selectedIds.length > 0" type="danger" @click="handleBatchDelete">
          <el-icon><Delete /></el-icon>批量删除
        </el-button>
      </div>
      <div class="action-bar-right">
        <el-button @click="getList">
          <el-icon><Refresh /></el-icon>
        </el-button>
      </div>
    </div>

    <el-table :data="tableData" stripe @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="buildingNo" label="楼栋号" />
      <el-table-column prop="floorNo" label="楼层" />
      <el-table-column prop="roomNo" label="房间号" />
      <el-table-column prop="dormType" label="宿舍类型" />
      <el-table-column prop="bedTotal" label="床位总数" />
      <el-table-column prop="bedAvailable" label="空闲床位" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <span class="status-badge" :class="row.status === 1 ? 'success' : 'warning'">
            <span class="dot" />{{ row.status === 1 ? '正常' : '维修中' }}
          </span>
        </template>
      </el-table-column>
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

    <el-dialog v-model="dialogVisible" :title="dialogType === 'add' ? '新增宿舍' : '编辑宿舍'" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="楼栋号" required>
          <el-input v-model="form.buildingNo" placeholder="请输入楼栋号" />
        </el-form-item>
        <el-form-item label="楼层" required>
          <el-input v-model.number="form.floorNo" type="number" placeholder="请输入楼层" />
        </el-form-item>
        <el-form-item label="房间号" required>
          <el-input v-model="form.roomNo" placeholder="请输入房间号" />
        </el-form-item>
        <el-form-item label="宿舍类型">
          <el-select v-model="form.dormType" placeholder="请选择" @change="handleDormTypeChange">
            <el-option label="4人间" value="4人间" />
            <el-option label="6人间" value="6人间" />
          </el-select>
        </el-form-item>
        <el-form-item label="床位总数" required>
          <el-input v-model.number="form.bedTotal" type="number" disabled />
        </el-form-item>
        <el-form-item label="空闲床位" required>
          <el-input v-model.number="form.bedAvailable" type="number" disabled />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" placeholder="请选择">
            <el-option label="正常" value="1" />
            <el-option label="维修中" value="0" />
          </el-select>
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
import { Plus, Delete, Refresh, OfficeBuilding, CircleCheck, Tools, FirstAidKit } from '@element-plus/icons-vue'
import dormitoryApi from '@/api/dormitory'

const tableData = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const selectedIds = ref([])

const searchForm = reactive({ buildingNo: '', roomNo: '', status: '' })

const dialogVisible = ref(false)
const dialogType = ref('add')
const form = reactive({
  id: '', buildingNo: '', floorNo: '', roomNo: '',
  dormType: '4人间', bedTotal: 4, bedAvailable: 4, status: 1
})

const normalCount = computed(() => tableData.value.filter(r => r.status === 1).length)
const repairCount = computed(() => tableData.value.filter(r => r.status === 0).length)
const totalBeds = computed(() => tableData.value.reduce((s, r) => s + (r.bedTotal || 0), 0))

const getList = () => {
  dormitoryApi.page({
    pageNum: pageNum.value, pageSize: pageSize.value,
    buildingNo: searchForm.buildingNo, roomNo: searchForm.roomNo, status: searchForm.status
  }).then(res => {
    tableData.value = res.data.list
    total.value = res.data.total
  })
}

const resetForm = () => {
  searchForm.buildingNo = ''
  searchForm.roomNo = ''
  searchForm.status = ''
  getList()
}

const handleSizeChange = (size) => { pageSize.value = size; getList() }
const handleCurrentChange = (current) => { pageNum.value = current; getList() }
const handleSelectionChange = (val) => { selectedIds.value = val.map(item => item.id) }

const openAddDialog = () => {
  dialogType.value = 'add'
  Object.assign(form, { id: '', buildingNo: '', floorNo: '', roomNo: '', dormType: '4人间', bedTotal: 4, bedAvailable: 4, status: 1 })
  dialogVisible.value = true
}

const handleDormTypeChange = (val) => {
  const n = val === '6人间' ? 6 : 4
  form.bedTotal = n
  form.bedAvailable = n
}

const openEditDialog = (row) => {
  dialogType.value = 'edit'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleSubmit = () => {
  const action = dialogType.value === 'add' ? dormitoryApi.add(form) : dormitoryApi.update(form)
  action.then(() => {
    dialogVisible.value = false
    getList()
    ElMessage.success(dialogType.value === 'add' ? '新增成功' : '编辑成功')
  })
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定要删除吗？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
    .then(() => { dormitoryApi.delete(id).then(() => { getList(); ElMessage.success('删除成功') }) })
}

const handleBatchDelete = () => {
  ElMessageBox.confirm('确定要批量删除吗？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
    .then(() => { dormitoryApi.batchDelete(selectedIds.value).then(() => { getList(); selectedIds.value = []; ElMessage.success('批量删除成功') }) })
}

onMounted(() => { getList() })
</script>
