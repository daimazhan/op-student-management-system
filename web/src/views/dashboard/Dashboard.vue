<template>
  <div class="dashboard">
    <!-- 顶部统计卡片 -->
    <div class="dashboard-header">
      <div class="header-left">
        <h2>仪表盘</h2>
        <span class="update-time">最后更新：{{ updateTime }}</span>
      </div>
      <el-button type="primary" :icon="Refresh" @click="handleRefresh">刷新数据</el-button>
    </div>

    <el-row :gutter="20" class="stats-row">
      <el-col :xs="24" :sm="12" :md="6" :lg="6">
        <el-card class="stat-card" @click="handleCardClick('student')">
          <div class="stat-content">
            <div class="stat-icon student">
              <el-icon :size="40"><User /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.studentCount }}</div>
              <div class="stat-label">学生总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6" :lg="6">
        <el-card class="stat-card" @click="handleCardClick('class')">
          <div class="stat-content">
            <div class="stat-icon class">
              <el-icon :size="40"><School /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.classCount }}</div>
              <div class="stat-label">班级总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6" :lg="6">
        <el-card class="stat-card" @click="handleCardClick('user')">
          <div class="stat-content">
            <div class="stat-icon user">
              <el-icon :size="40"><UserFilled /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.userCount }}</div>
              <div class="stat-label">用户总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6" :lg="6">
        <el-card class="stat-card" @click="handleCardClick('role')">
          <div class="stat-content">
            <div class="stat-icon role">
              <el-icon :size="40"><Lock /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.roleCount }}</div>
              <div class="stat-label">角色总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="charts-row">
      <!-- 左侧：统计图表 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>学生性别分布</span>
            </div>
          </template>
          <div ref="genderChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>操作类型分布</span>
            </div>
          </template>
          <div ref="operationTypeChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="charts-row">
      <!-- 班级人数分布 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>班级人数分布</span>
            </div>
          </template>
          <div ref="classChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
      <!-- 操作日志趋势 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>操作日志趋势（最近7天）</span>
            </div>
          </template>
          <div ref="operationTrendChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { getStudentList } from '@/api/student'
import { getClassList } from '@/api/class'
import { getUserList } from '@/api/user'
import { getRoleList } from '@/api/role'
import { getStatisticsByType, getStatisticsByDate } from '@/api/log'
import { User, School, UserFilled, Lock, Refresh } from '@element-plus/icons-vue'
import * as echarts from 'echarts'

const router = useRouter()

const stats = ref({
  studentCount: 0,
  classCount: 0,
  userCount: 0,
  roleCount: 0
})

const updateTime = ref('')

// 图表引用
const genderChartRef = ref(null)
const classChartRef = ref(null)
const operationTypeChartRef = ref(null)
const operationTrendChartRef = ref(null)

let genderChart = null
let classChart = null
let operationTypeChart = null
let operationTrendChart = null

// 格式化时间
const formatTime = (date) => {
  const d = date || new Date()
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hour = String(d.getHours()).padStart(2, '0')
  const minute = String(d.getMinutes()).padStart(2, '0')
  const second = String(d.getSeconds()).padStart(2, '0')
  return `${year}-${month}-${day} ${hour}:${minute}:${second}`
}

// 更新刷新时间
const updateRefreshTime = () => {
  updateTime.value = formatTime()
}

// 加载统计数据
const loadStats = async () => {
  try {
    const [studentRes, classRes, userRes, roleRes] = await Promise.all([
      getStudentList({ pageNum: 1, pageSize: 1 }),
      getClassList(),
      getUserList(),
      getRoleList()
    ])
    if (studentRes.code === 200) {
      stats.value.studentCount = studentRes.data.total || 0
    }
    if (classRes.code === 200) {
      stats.value.classCount = classRes.data?.length || 0
    }
    if (userRes.code === 200) {
      stats.value.userCount = userRes.data?.length || 0
    }
    if (roleRes.code === 200) {
      stats.value.roleCount = roleRes.data?.length || 0
    }
    updateRefreshTime()
  } catch (error) {
    console.error(error)
  }
}

// 加载学生性别分布图表
const loadGenderChart = async () => {
  try {
    // 获取所有学生数据
    const studentRes = await getStudentList({ pageNum: 1, pageSize: 10000 })
    if (studentRes.code === 200 && genderChart) {
      const students = studentRes.data.list || []
      
      // 如果没有数据，显示空图表提示
      if (students.length === 0) {
        genderChart.setOption({
          title: {
            text: '暂无数据',
            left: 'center',
            top: 'middle',
            textStyle: {
              color: '#999',
              fontSize: 14
            }
          }
        })
        return
      }
      
      // 统计性别分布
      const genderMap = {}
      students.forEach(student => {
        const gender = student.gender || '未知'
        genderMap[gender] = (genderMap[gender] || 0) + 1
      })
      
      const genderData = Object.keys(genderMap).map(key => ({
        name: key,
        value: genderMap[key]
      }))

      genderChart.setOption({
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '学生性别',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: true,
              formatter: '{b}: {c}\n({d}%)'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 16,
                fontWeight: 'bold'
              }
            },
            data: genderData
          }
        ]
      })
    }
  } catch (error) {
    console.error('加载性别分布图表失败:', error)
  }
}

// 加载班级人数分布图表
const loadClassChart = async () => {
  try {
    // 获取所有学生数据
    const studentRes = await getStudentList({ pageNum: 1, pageSize: 10000 })
    if (studentRes.code === 200 && classChart) {
      const students = studentRes.data.list || []
      
      // 如果没有数据，显示空图表提示
      if (students.length === 0) {
        classChart.setOption({
          title: {
            text: '暂无数据',
            left: 'center',
            top: 'middle',
            textStyle: {
              color: '#999',
              fontSize: 14
            }
          }
        })
        return
      }
      
      // 统计班级人数
      const classMap = {}
      students.forEach(student => {
        const className = student.className || '未分配'
        classMap[className] = (classMap[className] || 0) + 1
      })
      
      // 转换为数组并按人数排序
      const classData = Object.keys(classMap)
        .map(key => ({
          name: key,
          value: classMap[key]
        }))
        .sort((a, b) => b.value - a.value)
        .slice(0, 10) // 只显示前10个班级

      // 如果没有班级数据，显示空图表提示
      if (classData.length === 0) {
        classChart.setOption({
          title: {
            text: '暂无数据',
            left: 'center',
            top: 'middle',
            textStyle: {
              color: '#999',
              fontSize: 14
            }
          }
        })
        return
      }

      classChart.setOption({
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
          type: 'category',
          data: classData.map(item => item.name),
          axisLabel: {
            rotate: 45,
            interval: 0
          }
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '学生人数',
            type: 'bar',
            data: classData.map(item => item.value),
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: '#83bff6' },
                { offset: 0.5, color: '#188df0' },
                { offset: 1, color: '#188df0' }
              ])
            },
            emphasis: {
              itemStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: '#2378f7' },
                  { offset: 0.7, color: '#2378f7' },
                  { offset: 1, color: '#83bff6' }
                ])
              }
            }
          }
        ]
      })
    }
  } catch (error) {
    console.error('加载班级人数分布图表失败:', error)
  }
}

// 加载操作类型分布图表
const loadOperationTypeChart = async () => {
  try {
    const typeRes = await getStatisticsByType()
    if (typeRes.code === 200 && operationTypeChart) {
      const typeData = typeRes.data || []
      const typeMap = {
        'ADD': '新增',
        'UPDATE': '修改',
        'DELETE': '删除',
        'QUERY': '查询',
        'LOGIN': '登录',
        'LOGOUT': '登出'
      }

      // 如果没有数据，显示空图表提示
      if (typeData.length === 0) {
        operationTypeChart.setOption({
          title: {
            text: '暂无数据',
            left: 'center',
            top: 'middle',
            textStyle: {
              color: '#999',
              fontSize: 14
            }
          }
        })
        return
      }

      operationTypeChart.setOption({
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
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
              name: typeMap[item.name] || item.name
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
  } catch (error) {
    console.error('加载操作类型分布图表失败:', error)
  }
}

// 加载操作日志趋势图表
const loadOperationTrendChart = async () => {
  try {
    const dateRes = await getStatisticsByDate()
    if (dateRes.code === 200 && operationTrendChart) {
      const dateData = dateRes.data || []
      
      // 格式化日期 - 处理 YYYY-MM-DD 格式
      const formatDate = (dateStr) => {
        if (!dateStr) return ''
        // 如果是 YYYY-MM-DD 格式，直接处理
        if (dateStr.includes('-')) {
          const parts = dateStr.split('-')
          if (parts.length >= 2) {
            return `${parseInt(parts[1])}-${parseInt(parts[2])}`
          }
        }
        // 否则尝试 Date 对象解析
        const date = new Date(dateStr)
        if (!isNaN(date.getTime())) {
          const month = date.getMonth() + 1
          const day = date.getDate()
          return `${month}-${day}`
        }
        return dateStr
      }

      // 如果没有数据，显示空图表提示
      if (dateData.length === 0) {
        operationTrendChart.setOption({
          title: {
            text: '暂无数据',
            left: 'center',
            top: 'middle',
            textStyle: {
              color: '#999',
              fontSize: 14
            }
          }
        })
        return
      }

      operationTrendChart.setOption({
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
          data: dateData.map(item => formatDate(item.name))
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '操作次数',
            type: 'line',
            smooth: true,
            data: dateData.map(item => item.value),
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: 'rgba(128, 255, 165, 0.5)' },
                { offset: 1, color: 'rgba(1, 191, 236, 0.5)' }
              ])
            },
            itemStyle: {
              color: '#01BFEC'
            }
          }
        ]
      })
    }
  } catch (error) {
    console.error('加载操作日志趋势图表失败:', error)
  }
}

// 窗口 resize 处理函数
const handleResize = () => {
  genderChart?.resize()
  classChart?.resize()
  operationTypeChart?.resize()
  operationTrendChart?.resize()
}

// 初始化图表
const initCharts = () => {
  nextTick(() => {
    if (genderChartRef.value) {
      genderChart = echarts.init(genderChartRef.value)
    }
    if (classChartRef.value) {
      classChart = echarts.init(classChartRef.value)
    }
    if (operationTypeChartRef.value) {
      operationTypeChart = echarts.init(operationTypeChartRef.value)
    }
    if (operationTrendChartRef.value) {
      operationTrendChart = echarts.init(operationTrendChartRef.value)
    }
    window.addEventListener('resize', handleResize)
    loadAllCharts()
  })
}

// 加载所有图表数据
const loadAllCharts = async () => {
  await Promise.all([
    loadStats(),
    loadGenderChart(),
    loadClassChart(),
    loadOperationTypeChart(),
    loadOperationTrendChart()
  ])
}

// 卡片点击跳转
const handleCardClick = (type) => {
  if (type === 'student') {
    router.push('/student')
  } else if (type === 'class') {
    router.push('/class')
  } else if (type === 'user') {
    router.push('/system/user')
  } else if (type === 'role') {
    router.push('/system/role')
  }
}

// 刷新数据
const handleRefresh = async () => {
  await loadAllCharts()
}

// 组件挂载
onMounted(() => {
  initCharts()
})

// 组件卸载前清理
onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  if (genderChart) {
    genderChart.dispose()
    genderChart = null
  }
  if (classChart) {
    classChart.dispose()
    classChart = null
  }
  if (operationTypeChart) {
    operationTypeChart.dispose()
    operationTypeChart = null
  }
  if (operationTrendChart) {
    operationTrendChart.dispose()
    operationTrendChart = null
  }
})
</script>

<style scoped>
.dashboard {
  width: 100%;
  padding: 20px;
}

.dashboard-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header-left h2 {
  margin: 0;
  color: #333;
  font-size: 24px;
}

.update-time {
  color: #999;
  font-size: 12px;
  margin-left: 10px;
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
  margin-bottom: 20px;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.stat-content {
  display: flex;
  align-items: center;
}

.stat-icon {
  width: 80px;
  height: 80px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  margin-right: 20px;
  flex-shrink: 0;
}

.stat-icon.student {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-icon.class {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-icon.user {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-icon.role {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #333;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #999;
}

.charts-row {
  margin-bottom: 20px;
}

.chart-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
  color: #333;
}

.chart-container {
  width: 100%;
  height: 300px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .dashboard {
    padding: 10px;
  }

  .dashboard-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .stat-card {
    margin-bottom: 10px;
  }

  .chart-container {
    height: 250px;
  }
}
</style>
