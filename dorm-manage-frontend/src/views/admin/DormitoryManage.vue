<template>
  <div class="dormitory-manage">
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <span>宿舍管理</span>
          <el-button type="primary" @click="openAddDialog">新增宿舍</el-button>
        </div>
      </template>
      
      <!-- 搜索表单 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="楼栋号">
          <el-input v-model="searchForm.buildingNo" placeholder="请输入楼栋号" />
        </el-form-item>
        <el-form-item label="房间号">
          <el-input v-model="searchForm.roomNo" placeholder="请输入房间号" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态">
            <el-option label="正常" value="1" />
            <el-option label="维修中" value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getList">查询</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
      
      <!-- 数据表格 -->
      <el-table :data="tableData" style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
<!--        <el-table-column prop="id" label="ID" width="80" />-->
        <el-table-column prop="buildingNo" label="楼栋号" />
        <el-table-column prop="floorNo" label="楼层" />
        <el-table-column prop="roomNo" label="房间号" />
        <el-table-column prop="dormType" label="宿舍类型" />
        <el-table-column prop="bedTotal" label="床位总数" />
        <el-table-column prop="bedAvailable" label="空闲床位" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'warning'">
              {{ scope.row.status === 1 ? '正常' : '维修中' }}
            </el-tag>
          </template>
        </el-table-column>
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
          <el-select v-model="form.dormType" placeholder="请选择宿舍类型">
            <el-option label="4人间" value="4人间" />
            <el-option label="6人间" value="6人间" />
          </el-select>
        </el-form-item>
        <el-form-item label="床位总数" required>
          <el-input v-model.number="form.bedTotal" type="number" placeholder="请输入床位总数" />
        </el-form-item>
        <el-form-item label="空闲床位" required>
          <el-input v-model.number="form.bedAvailable" type="number" placeholder="请输入空闲床位数" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option label="正常" value="1" />
            <el-option label="维修中" value="0" />
          </el-select>
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
import { ElMessage, ElMessageBox } from 'element-plus'
import dormitoryApi from '@/api/dormitory'

// 表格数据
const tableData = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)

// 搜索表单
const searchForm = reactive({
  buildingNo: '',
  roomNo: '',
  status: ''
})

// 选中的ID
const selectedIds = ref([])

// 对话框
const dialogVisible = ref(false)
const dialogType = ref('add')
const form = reactive({
  id: '',
  buildingNo: '',
  floorNo: '',
  roomNo: '',
  dormType: '4人间',
  bedTotal: 4,
  bedAvailable: 4,
  status: 1
})

// 获取列表
const getList = () => {
  dormitoryApi.page({
    pageNum: pageNum.value,
    pageSize: pageSize.value,
    buildingNo: searchForm.buildingNo,
    roomNo: searchForm.roomNo,
    status: searchForm.status
  }).then(res => {
    tableData.value = res.data.list
    total.value = res.data.total
  })
}

// 重置表单
const resetForm = () => {
  searchForm.buildingNo = ''
  searchForm.roomNo = ''
  searchForm.status = ''
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
    buildingNo: '',
    floorNo: '',
    roomNo: '',
    dormType: '4人间',
    bedTotal: 4,
    bedAvailable: 4,
    status: 1
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
    dormitoryApi.add(form).then(() => {
      dialogVisible.value = false
      getList()
      ElMessage.success('新增成功')
    })
  } else {
    dormitoryApi.update(form).then(() => {
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
    dormitoryApi.delete(id).then(() => {
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
    dormitoryApi.batchDelete(selectedIds.value).then(() => {
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