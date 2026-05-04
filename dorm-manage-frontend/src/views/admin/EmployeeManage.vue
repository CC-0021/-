<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">员工信息管理</h2>
    </div>

    <div class="stat-cards">
      <div class="stat-card" style="background: linear-gradient(135deg, #6366f1, #818cf8)">
        <div class="stat-value">{{ total }}</div>
        <div class="stat-label">员工总数</div>
        <el-icon class="stat-icon"><UserFilled /></el-icon>
      </div>
      <div class="stat-card" style="background: linear-gradient(135deg, #10b981, #34d399)">
        <div class="stat-value">{{ activeCount }}</div>
        <div class="stat-label">在职员工</div>
        <el-icon class="stat-icon"><CircleCheck /></el-icon>
      </div>
      <div class="stat-card" style="background: linear-gradient(135deg, #ef4444, #f87171)">
        <div class="stat-value">{{ leaveCount }}</div>
        <div class="stat-label">离职员工</div>
        <el-icon class="stat-icon"><CircleClose /></el-icon>
      </div>
    </div>

    <div class="search-bar">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="姓名">
          <el-input v-model="searchForm.name" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item label="部门">
          <el-input v-model="searchForm.department" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部" clearable>
            <el-option label="在职" value="1" />
            <el-option label="离职" value="0" />
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
          <el-icon><Plus /></el-icon>新增员工
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
      <el-table-column prop="name" label="姓名" />
      <el-table-column prop="gender" label="性别" width="80">
        <template #default="{ row }">{{ row.gender === 1 ? '男' : row.gender === 2 ? '女' : '未知' }}</template>
      </el-table-column>
      <el-table-column prop="phone" label="联系电话" />
      <el-table-column prop="department" label="所属部门" />
      <el-table-column prop="position" label="职位" />
      <el-table-column prop="hireDate" label="入职时间" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <span class="status-badge" :class="row.status === 1 ? 'success' : 'danger'">
            <span class="dot" />{{ row.status === 1 ? '在职' : '离职' }}
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

    <el-dialog v-model="dialogVisible" :title="dialogType === 'add' ? '新增员工' : '编辑员工'" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="姓名" required>
          <el-input v-model="form.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="form.gender" placeholder="请选择性别">
            <el-option label="男" value="1" />
            <el-option label="女" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="所属部门">
          <el-input v-model="form.department" placeholder="请输入所属部门" />
        </el-form-item>
        <el-form-item label="职位">
          <el-input v-model="form.position" placeholder="请输入职位" />
        </el-form-item>
        <el-form-item label="入职时间">
          <el-date-picker v-model="form.hireDate" type="date" placeholder="请选择入职时间" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option label="在职" value="1" />
            <el-option label="离职" value="0" />
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
import { Plus, Delete, Refresh, UserFilled, CircleCheck, CircleClose } from '@element-plus/icons-vue'
import employeeApi from '@/api/employee'

const tableData = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const selectedIds = ref([])

const searchForm = reactive({ name: '', department: '', status: '' })

const dialogVisible = ref(false)
const dialogType = ref('add')
const form = reactive({ id: '', name: '', gender: '', phone: '', department: '', position: '', hireDate: '', status: 1 })

const activeCount = computed(() => tableData.value.filter(r => r.status === 1).length)
const leaveCount = computed(() => tableData.value.filter(r => r.status === 0).length)

const getList = () => {
  employeeApi.page({
    pageNum: pageNum.value, pageSize: pageSize.value,
    name: searchForm.name, department: searchForm.department, status: searchForm.status
  }).then(res => { tableData.value = res.data.list; total.value = res.data.total })
}

const resetForm = () => { searchForm.name = ''; searchForm.department = ''; searchForm.status = ''; getList() }
const handleSizeChange = (size) => { pageSize.value = size; getList() }
const handleCurrentChange = (current) => { pageNum.value = current; getList() }
const handleSelectionChange = (val) => { selectedIds.value = val.map(item => item.id) }

const openAddDialog = () => {
  dialogType.value = 'add'
  Object.assign(form, { id: '', name: '', gender: '', phone: '', department: '', position: '', hireDate: '', status: 1 })
  dialogVisible.value = true
}

const openEditDialog = (row) => { dialogType.value = 'edit'; Object.assign(form, row); dialogVisible.value = true }

const handleSubmit = () => {
  const action = dialogType.value === 'add' ? employeeApi.add(form) : employeeApi.update(form)
  action.then(() => { dialogVisible.value = false; getList(); ElMessage.success(dialogType.value === 'add' ? '新增成功' : '编辑成功') })
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定要删除吗？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
    .then(() => { employeeApi.delete(id).then(() => { getList(); ElMessage.success('删除成功') }) })
}

const handleBatchDelete = () => {
  ElMessageBox.confirm('确定要批量删除吗？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
    .then(() => { employeeApi.batchDelete(selectedIds.value).then(() => { getList(); selectedIds.value = []; ElMessage.success('批量删除成功') }) })
}

onMounted(() => { getList() })
</script>
