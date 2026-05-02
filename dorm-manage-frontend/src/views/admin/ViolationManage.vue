<template>
  <div class="violation-manage">
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <span>宿舍违规管理</span>
          <el-button type="primary" @click="openAddDialog">新增违规记录</el-button>
        </div>
      </template>
      
      <!-- 搜索表单 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="学生ID">
          <el-input v-model="searchForm.studentId" placeholder="请输入学生ID" />
        </el-form-item>
        <el-form-item label="宿舍号">
          <el-input v-model="searchForm.roomNo" placeholder="请输入宿舍号" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getList">查询</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
      
      <!-- 数据表格 -->
      <el-table :data="tableData" style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="studentId" label="学生ID" />
        <el-table-column prop="roomNo" label="宿舍号" />
        <el-table-column prop="violationType" label="违规类型" />
        <el-table-column prop="violationTime" label="违规时间" />
        <el-table-column prop="description" label="违规描述" />
        <el-table-column prop="evidence" label="凭证" />
        <el-table-column prop="handleResult" label="处理结果" />
        <el-table-column prop="handlerId" label="处理人ID" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="openEditDialog(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination">
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
      
      <!-- 批量操作 -->
      <div class="batch-operation" v-if="selectedIds.length > 0">
        <el-button type="danger" @click="handleBatchDelete">批量删除</el-button>
      </div>
    </el-card>
    
    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogType === 'add' ? '新增违规记录' : '编辑违规记录'" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="学生ID" required>
          <el-input v-model.number="form.studentId" type="number" placeholder="请输入学生ID" />
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
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import violationApi from '@/api/violation'

// 表格数据
const tableData = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)

// 搜索表单
const searchForm = reactive({
  studentId: '',
  roomNo: ''
})

// 选中的ID
const selectedIds = ref([])

// 对话框
const dialogVisible = ref(false)
const dialogType = ref('add')
const form = reactive({
  id: '',
  studentId: '',
  roomNo: '',
  violationType: '',
  violationTime: '',
  description: '',
  evidence: '',
  handleResult: '',
  handlerId: ''
})

// 获取列表
const getList = () => {
  violationApi.page({
    pageNum: pageNum.value,
    pageSize: pageSize.value,
    studentId: searchForm.studentId,
    roomNo: searchForm.roomNo
  }).then(res => {
    tableData.value = res.data.list
    total.value = res.data.total
  })
}

// 重置表单
const resetForm = () => {
  searchForm.studentId = ''
  searchForm.roomNo = ''
  getList()
}

// 分页处理
const handleSizeChange = (size) => {
  pageSize.value = size
  getList()
}

const handleCurrentChange = (current) => {
  pageNum.value = current
  getList()
}

// 选择处理
const handleSelectionChange = (val) => {
  selectedIds.value = val.map(item => item.id)
}

// 打开新增对话框
const openAddDialog = () => {
  dialogType.value = 'add'
  Object.assign(form, {
    id: '',
    studentId: '',
    roomNo: '',
    violationType: '',
    violationTime: '',
    description: '',
    evidence: '',
    handleResult: '',
    handlerId: ''
  })
  dialogVisible.value = true
}

// 打开编辑对话框
const openEditDialog = (row) => {
  dialogType.value = 'edit'
  Object.assign(form, row)
  dialogVisible.value = true
}

// 提交表单
const handleSubmit = () => {
  if (dialogType.value === 'add') {
    violationApi.add(form).then(() => {
      dialogVisible.value = false
      getList()
      ElMessage.success('新增成功')
    })
  } else {
    violationApi.update(form).then(() => {
      dialogVisible.value = false
      getList()
      ElMessage.success('编辑成功')
    })
  }
}

// 删除
const handleDelete = (id) => {
  ElMessageBox.confirm('确定要删除吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    violationApi.delete(id).then(() => {
      getList()
      ElMessage.success('删除成功')
    })
  })
}

// 批量删除
const handleBatchDelete = () => {
  ElMessageBox.confirm('确定要批量删除吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    violationApi.batchDelete(selectedIds.value).then(() => {
      getList()
      selectedIds.value = []
      ElMessage.success('批量删除成功')
    })
  })
}

// 页面挂载时获取数据
onMounted(() => {
  getList()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.batch-operation {
  margin-top: 10px;
}
</style>