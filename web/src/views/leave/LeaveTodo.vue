<template>
  <div class="page-container">
    <el-card>
      <template #header>待我审批</template>
      <el-table :data="list" v-loading="loading" style="width: 100%">
        <el-table-column prop="requestNo" label="单号" />
        <el-table-column prop="applicantName" label="申请人" />
        <el-table-column prop="type" label="类型" />
        <el-table-column prop="startTime" label="开始时间" />
        <el-table-column prop="endTime" label="结束时间" />
        <el-table-column prop="days" label="天数" />
        <el-table-column label="操作" width="220">
          <template #default="{ row }">
            <el-button type="primary" link @click="openApprove(row)">审批</el-button>
            <el-button type="info" link @click="openDetail(row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="approveVisible" title="审批处理" width="520px">
      <el-form :model="approveForm" label-width="90px">
        <el-form-item label="意见">
          <el-input v-model="approveForm.comment" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="approveVisible = false">取消</el-button>
        <el-button type="danger" @click="handleApprove(false)" :loading="opLoading">驳回</el-button>
        <el-button type="primary" @click="handleApprove(true)" :loading="opLoading">通过</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailVisible" title="请假详情" width="600px">
      <div v-if="current">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="单号">{{ current.requestNo }}</el-descriptions-item>
          <el-descriptions-item label="状态">{{ current.status }}</el-descriptions-item>
          <el-descriptions-item label="类型">{{ current.type }}</el-descriptions-item>
          <el-descriptions-item label="天数">{{ current.days }}</el-descriptions-item>
          <el-descriptions-item label="开始时间">{{ current.startTime }}</el-descriptions-item>
          <el-descriptions-item label="结束时间">{{ current.endTime }}</el-descriptions-item>
          <el-descriptions-item label="事由" :span="2">{{ current.reason }}</el-descriptions-item>
        </el-descriptions>
        <el-divider>审批轨迹</el-divider>
        <el-timeline>
          <el-timeline-item v-for="c in comments" :key="c.id" :timestamp="c.createTime">
            {{ c.operatorName }} - {{ c.action }} - {{ c.message }}
          </el-timeline-item>
        </el-timeline>
      </div>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { todoLeaves, approveLeave, getLeaveDetail, getLeaveComments } from '@/api/leave'

const loading = ref(false)
const list = ref([])
const total = ref(0)

const approveVisible = ref(false)
const opLoading = ref(false)
const approveForm = ref({ requestId: null, comment: '' })

const detailVisible = ref(false)
const current = ref(null)
const comments = ref([])

onMounted(loadData)

async function loadData() {
  loading.value = true
  try {
    const res = await todoLeaves()
    if (res.code === 200) {
      list.value = res.data.list || []
      total.value = res.data.total || 0
    }
  } finally {
    loading.value = false
  }
}

function openApprove(row) {
  approveForm.value = { requestId: row.id, comment: '' }
  approveVisible.value = true
}

async function handleApprove(approve) {
  opLoading.value = true
  try {
    const res = await approveLeave({ requestId: approveForm.value.requestId, comment: approveForm.value.comment, approve })
    if (res.code === 200) {
      ElMessage.success('处理成功')
      approveVisible.value = false
      loadData()
    } else {
      ElMessage.error(res.msg || '处理失败')
    }
  } finally {
    opLoading.value = false
  }
}

async function openDetail(row) {
  const [d, cs] = await Promise.all([getLeaveDetail(row.id), getLeaveComments(row.id)])
  if (d.code === 200) current.value = d.data
  if (cs.code === 200) comments.value = cs.data || []
  detailVisible.value = true
}
</script>

<style scoped>
.page-container {
  padding: 16px;
}

.page-container {
  width: 100%;
}

.page-container :deep(.el-card) {
  width: 100%;
}

.page-container :deep(.el-card__body) {
  padding: 20px;
}

.page-container :deep(.el-table) {
  width: 100% !important;
}
</style>


