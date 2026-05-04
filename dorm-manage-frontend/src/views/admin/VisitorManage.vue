<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">来访登记</h2>
    </div>

    <div class="stat-cards">
      <div class="stat-card" style="background: linear-gradient(135deg, #6366f1, #818cf8)">
        <div class="stat-value">{{ total }}</div>
        <div class="stat-label">来访总数</div>
        <el-icon class="stat-icon"><User /></el-icon>
      </div>
      <div class="stat-card" style="background: linear-gradient(135deg, #10b981, #34d399)">
        <div class="stat-value">{{ visitingCount }}</div>
        <div class="stat-label">访问中</div>
        <el-icon class="stat-icon"><Clock /></el-icon>
      </div>
      <div class="stat-card" style="background: linear-gradient(135deg, #0ea5e9, #38bdf8)">
        <div class="stat-value">{{ leftCount }}</div>
        <div class="stat-label">已离开</div>
        <el-icon class="stat-icon"><CircleCheck /></el-icon>
      </div>
    </div>

    <div class="search-bar">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="宿舍号">
          <el-input v-model="searchForm.roomNo" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item label="开始时间">
          <el-date-picker v-model="searchForm.startTime" type="datetime" placeholder="请选择" />
        </el-form-item>
        <el-form-item label="结束时间">
          <el-date-picker v-model="searchForm.endTime" type="datetime" placeholder="请选择" />
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
          <el-icon><Plus /></el-icon>新增来访记录
        </el-button>
      </div>
      <div class="action-bar-right">
        <el-button @click="getList"><el-icon><Refresh /></el-icon></el-button>
      </div>
    </div>

    <el-table :data="tableData" stripe>
      <el-table-column prop="visitorName" label="访客姓名" />
      <el-table-column prop="idCard" label="身份证号" />
      <el-table-column prop="phone" label="联系电话" />
      <el-table-column prop="studentId" label="被访学生ID" />
      <el-table-column prop="roomNo" label="来访宿舍号" />
      <el-table-column prop="enterTime" label="进入时间" />
      <el-table-column prop="leaveTime" label="离开时间" />
      <el-table-column prop="remark" label="备注" show-overflow-tooltip />
      <el-table-column label="操作" width="100" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link size="small" @click="openEditDialog(row)">编辑</el-button>
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
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus, Refresh, User, Clock, CircleCheck } from '@element-plus/icons-vue'
import visitorApi from '@/api/visitor'

const tableData = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)

const searchForm = reactive({ roomNo: '', startTime: '', endTime: '' })

const dialogVisible = ref(false)
const dialogType = ref('add')
const form = reactive({ id: '', visitorName: '', idCard: '', phone: '', studentId: '', roomNo: '', enterTime: '', leaveTime: '', registrarId: '', remark: '' })

const visitingCount = computed(() => tableData.value.filter(r => !r.leaveTime).length)
const leftCount = computed(() => tableData.value.filter(r => r.leaveTime).length)

const getList = () => {
  const params = { pageNum: pageNum.value, pageSize: pageSize.value, roomNo: searchForm.roomNo || undefined }
  if (searchForm.startTime) params.startTime = searchForm.startTime.toISOString().slice(0, 19).replace('T', ' ')
  if (searchForm.endTime) params.endTime = searchForm.endTime.toISOString().slice(0, 19).replace('T', ' ')
  visitorApi.page(params).then(res => { tableData.value = res.data.list; total.value = res.data.total })
}

const resetForm = () => { searchForm.roomNo = ''; searchForm.startTime = ''; searchForm.endTime = ''; getList() }
const handleSizeChange = (size) => { pageSize.value = size; getList() }
const handleCurrentChange = (current) => { pageNum.value = current; getList() }

const openAddDialog = () => {
  dialogType.value = 'add'
  Object.assign(form, { id: '', visitorName: '', idCard: '', phone: '', studentId: '', roomNo: '', enterTime: '', leaveTime: '', registrarId: '', remark: '' })
  dialogVisible.value = true
}

const openEditDialog = (row) => { dialogType.value = 'edit'; Object.assign(form, row); dialogVisible.value = true }

const handleSubmit = () => {
  const action = dialogType.value === 'add' ? visitorApi.add(form) : visitorApi.update(form)
  action.then(() => { dialogVisible.value = false; getList(); ElMessage.success(dialogType.value === 'add' ? '新增成功' : '编辑成功') })
}

onMounted(() => { getList() })
</script>
