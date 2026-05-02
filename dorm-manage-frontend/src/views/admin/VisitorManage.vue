<template>
  <div class="visitor-manage">
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <span>来访登记</span>
          <el-button type="primary" @click="openAddDialog">新增来访记录</el-button>
        </div>
      </template>
      
      <!-- 搜索表单 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="宿舍号">
          <el-input v-model="searchForm.roomNo" placeholder="请输入宿舍号" />
        </el-form-item>
        <el-form-item label="开始时间">
          <el-date-picker v-model="searchForm.startTime" type="datetime" placeholder="请选择开始时间" />
        </el-form-item>
        <el-form-item label="结束时间">
          <el-date-picker v-model="searchForm.endTime" type="datetime" placeholder="请选择结束时间" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getList">查询</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
      
      <!-- 数据表格 -->
      <el-table :data="tableData" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="visitorName" label="访客姓名" />
        <el-table-column prop="idCard" label="身份证号" />
        <el-table-column prop="phone" label="联系电话" />
        <el-table-column prop="studentId" label="被访学生ID" />
        <el-table-column prop="roomNo" label="来访宿舍号" />
        <el-table-column prop="enterTime" label="进入时间" />
        <el-table-column prop="leaveTime" label="离开时间" />
        <el-table-column prop="registrarId" label="登记人ID" />
        <el-table-column prop="remark" label="备注" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="openEditDialog(scope.row)">编辑</el-button>
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
    </el-card>
    
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <span>按天统计</span>
          </template>
          <el-chart v-if="dayStats.length > 0" type="bar" :data="dayStats" :x-field="'date'" :y-field="'count'" />
          <div v-else class="no-data">暂无数据</div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <span>按楼栋统计</span>
          </template>
          <el-chart v-if="buildingStats.length > 0" type="pie" :data="buildingStats" :x-field="'building'" :y-field="'count'" />
          <div v-else class="no-data">暂无数据</div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogType === 'add' ? '新增来访记录' : '编辑来访记录'" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="访客姓名" required>
          <el-input v-model="form.visitorName" placeholder="请输入访客姓名" />
        </el-form-item>
        <el-form-item label="身份证号">
          <el-input v-model="form.idCard" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="被访学生ID">
          <el-input v-model.number="form.studentId" type="number" placeholder="请输入被访学生ID" />
        </el-form-item>
        <el-form-item label="来访宿舍号" required>
          <el-input v-model="form.roomNo" placeholder="请输入来访宿舍号" />
        </el-form-item>
        <el-form-item label="进入时间" required>
          <el-date-picker v-model="form.enterTime" type="datetime" placeholder="请选择进入时间" />
        </el-form-item>
        <el-form-item label="离开时间">
          <el-date-picker v-model="form.leaveTime" type="datetime" placeholder="请选择离开时间" />
        </el-form-item>
        <el-form-item label="登记人ID">
          <el-input v-model.number="form.registrarId" type="number" placeholder="请输入登记人ID" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
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
import visitorApi from '@/api/visitor'

// 表格数据
const tableData = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)

// 搜索表单
const searchForm = reactive({
  roomNo: '',
  startTime: '',
  endTime: ''
})

// 统计数据
const dayStats = ref([])
const buildingStats = ref([])

// 对话框
const dialogVisible = ref(false)
const dialogType = ref('add')
const form = reactive({
  id: '',
  visitorName: '',
  idCard: '',
  phone: '',
  studentId: '',
  roomNo: '',
  enterTime: '',
  leaveTime: '',
  registrarId: '',
  remark: ''
})

// 获取列表
const getList = () => {
  const params = {
    pageNum: pageNum.value,
    pageSize: pageSize.value,
    roomNo: searchForm.roomNo || undefined
  }
  if (searchForm.startTime) {
    params.startTime = searchForm.startTime.toISOString().slice(0, 19).replace('T', ' ')
  }
  if (searchForm.endTime) {
    params.endTime = searchForm.endTime.toISOString().slice(0, 19).replace('T', ' ')
  }
  visitorApi.page(params).then(res => {
    tableData.value = res.data.list
    total.value = res.data.total
  })
}

// 获取统计数据
const getStats = () => {
  // 按天统计
  visitorApi.statsByDay({}).then(res => {
    dayStats.value = res.data
  })
  // 按楼栋统计
  visitorApi.statsByBuilding({}).then(res => {
    buildingStats.value = res.data
  })
}

// 重置表单
const resetForm = () => {
  searchForm.roomNo = ''
  searchForm.startTime = ''
  searchForm.endTime = ''
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

// 打开新增对话框
const openAddDialog = () => {
  dialogType.value = 'add'
  Object.assign(form, {
    id: '',
    visitorName: '',
    idCard: '',
    phone: '',
    studentId: '',
    roomNo: '',
    enterTime: '',
    leaveTime: '',
    registrarId: '',
    remark: ''
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
    visitorApi.add(form).then(() => {
      dialogVisible.value = false
      getList()
      getStats()
      ElMessage.success('新增成功')
    })
  } else {
    visitorApi.update(form).then(() => {
      dialogVisible.value = false
      getList()
      getStats()
      ElMessage.success('编辑成功')
    })
  }
}

// 页面挂载时获取数据
onMounted(() => {
  getList()
  getStats()
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

.stats-row {
  margin-top: 20px;
}

.no-data {
  text-align: center;
  padding: 40px 0;
  color: #999;
}
</style>