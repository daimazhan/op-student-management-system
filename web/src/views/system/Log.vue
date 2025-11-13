<template>
  <div class="log-container">
    <!-- 查询条件 -->
    <el-card style="margin-bottom: 20px">
      <el-form :model="queryForm" :inline="true" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="queryForm.username" placeholder="请输入用户名" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="操作类型">
          <el-select v-model="queryForm.operationType" placeholder="请选择操作类型" clearable style="width: 200px">
            <el-option label="新增" value="ADD" />
            <el-option label="修改" value="UPDATE" />
            <el-option label="删除" value="DELETE" />
            <el-option label="查询" value="QUERY" />
            <el-option label="登录" value="LOGIN" />
            <el-option label="登出" value="LOGOUT" />
          </el-select>
        </el-form-item>
        <el-form-item label="操作模块">
          <el-select v-model="queryForm.operationModule" placeholder="请选择操作模块" clearable style="width: 200px">
            <el-option label="学生管理" value="学生管理" />
            <el-option label="班级管理" value="班级管理" />
            <el-option label="用户管理" value="用户管理" />
            <el-option label="角色管理" value="角色管理" />
            <el-option label="菜单管理" value="菜单管理" />
            <el-option label="系统管理" value="系统管理" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择状态" clearable style="width: 200px">
            <el-option label="成功" :value="1" />
            <el-option label="失败" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="操作时间">
          <el-date-picker
            v-model="dateRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 350px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 统计图表 -->
    <el-row :gutter="20" style="margin-bottom: 20px">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>操作类型统计</span>
          </template>
          <div ref="typeChartRef" style="width: 100%; height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>操作模块统计</span>
          </template>
          <div ref="moduleChartRef" style="width: 100%; height: 300px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-bottom: 20px">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>用户操作统计（TOP 10）</span>
          </template>
          <div ref="userChartRef" style="width: 100%; height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>最近7天操作统计</span>
          </template>
          <div ref="dateChartRef" style="width: 100%; height: 300px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 日志列表 -->
    <el-card>
      <template #header>
        <div class="card-header">
          <span>操作日志列表</span>
        </div>
      </template>

      <el-table :data="tableData" stripe border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="operationType" label="操作类型" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.operationType === 'ADD'" type="success">新增</el-tag>
            <el-tag v-else-if="row.operationType === 'UPDATE'" type="warning">修改</el-tag>
            <el-tag v-else-if="row.operationType === 'DELETE'" type="danger">删除</el-tag>
            <el-tag v-else-if="row.operationType === 'QUERY'" type="info">查询</el-tag>
            <el-tag v-else-if="row.operationType === 'LOGIN'" type="success">登录</el-tag>
            <el-tag v-else-if="row.operationType === 'LOGOUT'" type="info">登出</el-tag>
            <span v-else>{{ row.operationType }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="operationModule" label="操作模块" width="120" />
        <el-table-column prop="operationContent" label="操作内容" min-width="200" show-overflow-tooltip />
        <el-table-column prop="requestMethod" label="请求方法" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.requestMethod === 'GET'" type="success">{{ row.requestMethod }}</el-tag>
            <el-tag v-else-if="row.requestMethod === 'POST'" type="warning">{{ row.requestMethod }}</el-tag>
            <el-tag v-else-if="row.requestMethod === 'PUT'" type="info">{{ row.requestMethod }}</el-tag>
            <el-tag v-else-if="row.requestMethod === 'DELETE'" type="danger">{{ row.requestMethod }}</el-tag>
            <span v-else>{{ row.requestMethod }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="requestUrl" label="请求URL" min-width="200" show-overflow-tooltip />
        <el-table-column prop="ipAddress" label="IP地址" width="150" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag v-if="row.status === 1" type="success">成功</el-tag>
            <el-tag v-else type="danger">失败</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operationTime" label="操作时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.operationTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleView(row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="pagination.pageNum"
        v-model:page-size="pagination.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        style="margin-top: 20px; justify-content: flex-end"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <!-- 详情对话框 -->
    <el-dialog v-model="detailDialogVisible" title="日志详情" width="800px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="ID">{{ detailData.id }}</el-descriptions-item>
        <el-descriptions-item label="用户ID">{{ detailData.userId }}</el-descriptions-item>
        <el-descriptions-item label="用户名">{{ detailData.username }}</el-descriptions-item>
        <el-descriptions-item label="操作类型">
          <el-tag v-if="detailData.operationType === 'ADD'" type="success">新增</el-tag>
          <el-tag v-else-if="detailData.operationType === 'UPDATE'" type="warning">修改</el-tag>
          <el-tag v-else-if="detailData.operationType === 'DELETE'" type="danger">删除</el-tag>
          <el-tag v-else-if="detailData.operationType === 'QUERY'" type="info">查询</el-tag>
          <el-tag v-else-if="detailData.operationType === 'LOGIN'" type="success">登录</el-tag>
          <el-tag v-else-if="detailData.operationType === 'LOGOUT'" type="info">登出</el-tag>
          <span v-else>{{ detailData.operationType }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="操作模块">{{ detailData.operationModule }}</el-descriptions-item>
        <el-descriptions-item label="操作内容">{{ detailData.operationContent }}</el-descriptions-item>
        <el-descriptions-item label="请求方法">{{ detailData.requestMethod }}</el-descriptions-item>
        <el-descriptions-item label="请求URL">{{ detailData.requestUrl }}</el-descriptions-item>
        <el-descriptions-item label="IP地址">{{ detailData.ipAddress }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag v-if="detailData.status === 1" type="success">成功</el-tag>
          <el-tag v-else type="danger">失败</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="操作时间">{{ formatDate(detailData.operationTime) }}</el-descriptions-item>
        <el-descriptions-item v-if="detailData.errorMessage" label="错误信息" :span="2">
          <el-text type="danger">{{ detailData.errorMessage }}</el-text>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { getLogList, getLogById, getStatisticsByType, getStatisticsByModule, getStatisticsByUser, getStatisticsByDate } from '@/api/log'
import * as echarts from 'echarts'

const tableData = ref([])
const queryForm = reactive({
  username: '',
  operationType: '',
  operationModule: '',
  status: null,
  startTime: null,
  endTime: null,
  pageNum: 1,
  pageSize: 10
})

const dateRange = ref(null)
const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const detailDialogVisible = ref(false)
const detailData = ref({})

// 图表引用
const typeChartRef = ref(null)
const moduleChartRef = ref(null)
const userChartRef = ref(null)
const dateChartRef = ref(null)

let typeChart = null
let moduleChart = null
let userChart = null
let dateChart = null

const loadData = async () => {
  try {
    const params = {
      ...queryForm,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    }
    if (dateRange.value && dateRange.value.length === 2) {
      params.startTime = dateRange.value[0]
      params.endTime = dateRange.value[1]
    }
    const res = await getLogList(params)
    if (res.code === 200) {
      tableData.value = res.data.list || []
      pagination.total = res.data.total || 0
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('加载数据失败')
  }
}

const loadStatistics = async () => {
  try {
    // 加载操作类型统计
    const typeRes = await getStatisticsByType()
    if (typeRes.code === 200 && typeChart) {
      const typeData = typeRes.data || []
      typeChart.setOption({
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '操作类型',
            type: 'pie',
            radius: '50%',
            data: typeData.map(item => ({
              value: item.value,
              name: getOperationTypeName(item.name)
            })),
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      })
    }

    // 加载操作模块统计
    const moduleRes = await getStatisticsByModule()
    if (moduleRes.code === 200 && moduleChart) {
      const moduleData = moduleRes.data || []
      moduleChart.setOption({
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '操作模块',
            type: 'pie',
            radius: '50%',
            data: moduleData.map(item => ({
              value: item.value,
              name: item.name
            })),
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      })
    }

    // 加载用户统计
    const userRes = await getStatisticsByUser()
    if (userRes.code === 200 && userChart) {
      const userData = userRes.data || []
      userChart.setOption({
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'value'
        },
        yAxis: {
          type: 'category',
          data: userData.map(item => item.name)
        },
        series: [
          {
            name: '操作次数',
            type: 'bar',
            data: userData.map(item => item.value)
          }
        ]
      })
    }

    // 加载日期统计
    const dateRes = await getStatisticsByDate()
    if (dateRes.code === 200 && dateChart) {
      const dateData = dateRes.data || []
      dateChart.setOption({
        tooltip: {
          trigger: 'axis'
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: dateData.map(item => item.name)
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '操作次数',
            type: 'line',
            data: dateData.map(item => item.value),
            smooth: true
          }
        ]
      })
    }
  } catch (error) {
    console.error(error)
  }
}

const getOperationTypeName = (type) => {
  const typeMap = {
    'ADD': '新增',
    'UPDATE': '修改',
    'DELETE': '删除',
    'QUERY': '查询',
    'LOGIN': '登录',
    'LOGOUT': '登出'
  }
  return typeMap[type] || type
}

const initCharts = () => {
  nextTick(() => {
    if (typeChartRef.value) {
      typeChart = echarts.init(typeChartRef.value)
    }
    if (moduleChartRef.value) {
      moduleChart = echarts.init(moduleChartRef.value)
    }
    if (userChartRef.value) {
      userChart = echarts.init(userChartRef.value)
    }
    if (dateChartRef.value) {
      dateChart = echarts.init(dateChartRef.value)
    }
    loadStatistics()
  })
}

const handleSearch = () => {
  pagination.pageNum = 1
  loadData()
}

const handleReset = () => {
  Object.assign(queryForm, {
    username: '',
    operationType: '',
    operationModule: '',
    status: null,
    startTime: null,
    endTime: null
  })
  dateRange.value = null
  pagination.pageNum = 1
  loadData()
}

const handleSizeChange = (size) => {
  pagination.pageSize = size
  pagination.pageNum = 1
  loadData()
}

const handleCurrentChange = (page) => {
  pagination.pageNum = page
  loadData()
}

const handleView = async (row) => {
  try {
    const res = await getLogById(row.id)
    if (res.code === 200) {
      detailData.value = res.data
      detailDialogVisible.value = true
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('加载详情失败')
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

onMounted(() => {
  loadData()
  initCharts()
  
  // 监听窗口大小变化，重新调整图表大小
  window.addEventListener('resize', () => {
    if (typeChart) typeChart.resize()
    if (moduleChart) moduleChart.resize()
    if (userChart) userChart.resize()
    if (dateChart) dateChart.resize()
  })
})
</script>

<style scoped>
.log-container {
  width: 100%;
}

.log-container :deep(.el-card) {
  width: 100%;
}

.log-container :deep(.el-card__body) {
  padding: 20px;
}

.log-container :deep(.el-table) {
  width: 100% !important;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>

