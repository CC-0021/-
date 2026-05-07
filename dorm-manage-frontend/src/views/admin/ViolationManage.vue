<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">宿舍违规管理</h2>
    </div>

    <div class="stat-cards">
      <div class="stat-card" style="background: linear-gradient(135deg, #6366f1, #818cf8)">
        <div class="stat-value">{{ total }}</div>
        <div class="stat-label">违规总数</div>
        <el-icon class="stat-icon"><Warning /></el-icon>
      </div>
      <div class="stat-card" style="background: linear-gradient(135deg, #f59e0b, #fbbf24)">
        <div class="stat-value">{{ pendingCount }}</div>
        <div class="stat-label">待处理</div>
        <el-icon class="stat-icon"><Clock /></el-icon>
      </div>
      <div class="stat-card" style="background: linear-gradient(135deg, #10b981, #34d399)">
        <div class="stat-value">{{ handledCount }}</div>
        <div class="stat-label">已处理</div>
        <el-icon class="stat-icon"><CircleCheck /></el-icon>
      </div>
    </div>

    <div class="search-bar">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="学号">
          <el-input v-model="searchForm.studentId" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item label="宿舍号">
          <el-input v-model="searchForm.roomNo" placeholder="请输入" clearable />
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
          <el-icon><Plus /></el-icon>新增违规记录
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
      <el-table-column prop="studentName" label="学生姓名" />
      <el-table-column prop="roomNo" label="宿舍号" />
      <el-table-column prop="violationType" label="违规类型" />
      <el-table-column prop="violationTime" label="违规时间" />
      <el-table-column prop="description" label="违规描述" show-overflow-tooltip />
      <el-table-column prop="handleResult" label="处理结果" show-overflow-tooltip />
      <el-table-column label="操作" width="220" fixed="right">
        <template #default="{ row }">
          <el-button v-if="!row.handleResult" type="success" link size="small" @click="openHandleDialog(row)">处理</el-button>
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

    <el-dialog v-model="dialogVisible" :title="dialogType === 'add' ? '新增违规记录' : '编辑违规记录'" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="学号" required>
          <el-input v-model="form.studentId" placeholder="请输入学号" />
        </el-form-item>
        <el-form-item label="宿舍号" required>
          <el-input v-model="form.roomNo" placeholder="请输入宿舍号" />
        </el-form-item>
        <el-form-item label="违规类型" required>
          <el-input v-model="form.violationType" placeholder="请输入违规类型" />
        </el-form-item>
        <el-form-item label="违规时间" required>
          <el-date-picker v-model="form.violationTime" type="datetime" placeholder="请选择违规时间" />
        </el-form-item>
        <el-form-item label="违规描述">
          <el-input v-model="form.description" type="textarea" placeholder="请输入违规描述" />
        </el-form-item>
        <el-form-item label="凭证">
          <el-input v-model="form.evidence" placeholder="请输入凭证URL" />
        </el-form-item>
        <el-form-item label="处理结果">
          <el-input v-model="form.handleResult" placeholder="请输入处理结果" />
        </el-form-item>
        <el-form-item label="处理人ID">
          <el-input v-model.number="form.handlerId" type="number" placeholder="请输入处理人ID" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="handleDialogVisible" title="处理违规记录" width="520px">
      <el-form :model="handleForm" label-width="100px">
        <el-form-item label="违规学生">
          <span class="handle-info">{{ handleForm.studentName }} ({{ handleForm.studentId }})</span>
        </el-form-item>
        <el-form-item label="违规类型">
          <span class="handle-info">{{ handleForm.violationType }}</span>
        </el-form-item>
        <el-form-item label="违规描述">
          <span class="handle-info">{{ handleForm.description || '无' }}</span>
        </el-form-item>
        <el-form-item label="处理结果" required>
          <el-input v-model="handleForm.handleResult" type="textarea" :rows="4" placeholder="请输入处理结果" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="handleDialogVisible = false">取消</el-button>
        <el-button type="primary" :disabled="!handleForm.handleResult" @click="submitHandle">确认处理</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Delete, Refresh, Warning, Clock, CircleCheck } from '@element-plus/icons-vue'
import violationApi from '@/api/violation'

const tableData = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const selectedIds = ref([])

const searchForm = reactive({ studentId: '', roomNo: '' })

const dialogVisible = ref(false)
const dialogType = ref('add')
const form = reactive({ id: '', studentId: '', roomNo: '', violationType: '', violationTime: '', description: '', evidence: '', handleResult: '', handlerId: '' })

const handleDialogVisible = ref(false)
const handleForm = reactive({ id: '', studentId: '', studentName: '', violationType: '', description: '', handleResult: '' })

const pendingCount = computed(() => tableData.value.filter(r => !r.handleResult).length)
const handledCount = computed(() => tableData.value.filter(r => r.handleResult).length)

const getList = () => {
  violationApi.page({
    pageNum: pageNum.value, pageSize: pageSize.value,
    studentId: searchForm.studentId, roomNo: searchForm.roomNo
  }).then(res => { tableData.value = res.data.list; total.value = res.data.total })
}

const resetForm = () => { searchForm.studentId = ''; searchForm.roomNo = ''; getList() }
const handleSizeChange = (size) => { pageSize.value = size; getList() }
const handleCurrentChange = (current) => { pageNum.value = current; getList() }
const handleSelectionChange = (val) => { selectedIds.value = val.map(item => item.id) }

const openAddDialog = () => {
  dialogType.value = 'add'
  Object.assign(form, { id: '', studentId: '', roomNo: '', violationType: '', violationTime: '', description: '', evidence: '', handleResult: '', handlerId: '' })
  dialogVisible.value = true
}

const openEditDialog = (row) => { dialogType.value = 'edit'; Object.assign(form, row); dialogVisible.value = true }

const openHandleDialog = (row) => {
  Object.assign(handleForm, {
    id: row.id,
    studentId: row.studentId,
    studentName: row.studentName,
    violationType: row.violationType,
    description: row.description,
    handleResult: ''
  })
  handleDialogVisible.value = true
}

const submitHandle = () => {
  if (!handleForm.handleResult) return
  violationApi.update({ id: handleForm.id, handleResult: handleForm.handleResult })
    .then(() => { handleDialogVisible.value = false; getList(); ElMessage.success('处理完成') })
}

const handleSubmit = () => {
  const action = dialogType.value === 'add' ? violationApi.add(form) : violationApi.update(form)
  action.then(() => { dialogVisible.value = false; getList(); ElMessage.success(dialogType.value === 'add' ? '新增成功' : '编辑成功') })
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定要删除吗？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
    .then(() => { violationApi.delete(id).then(() => { getList(); ElMessage.success('删除成功') }) })
}

const handleBatchDelete = () => {
  ElMessageBox.confirm('确定要批量删除吗？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
    .then(() => { violationApi.batchDelete(selectedIds.value).then(() => { getList(); selectedIds.value = []; ElMessage.success('批量删除成功') }) })
}

onMounted(() => { getList() })
</script>

<style scoped>
.handle-info {
  color: #334155;
  font-size: 14px;
}
</style>
