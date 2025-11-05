<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
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
      <el-col :span="6">
        <el-card class="stat-card">
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
    </el-row>
    <el-card class="welcome-card" style="margin-top: 20px">
      <h2>欢迎使用学生管理系统</h2>
      <p>请使用左侧菜单进行功能操作</p>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getStudentList } from '@/api/student'
import { getClassList } from '@/api/class'
import { User, School } from '@element-plus/icons-vue'

const stats = ref({
  studentCount: 0,
  classCount: 0
})

const loadStats = async () => {
  try {
    const [studentRes, classRes] = await Promise.all([
      getStudentList({ pageNum: 1, pageSize: 1 }),
      getClassList()
    ])
    if (studentRes.code === 200) {
      stats.value.studentCount = studentRes.data.total || 0
    }
    if (classRes.code === 200) {
      stats.value.classCount = classRes.data?.length || 0
    }
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => {
  loadStats()
})
</script>

<style scoped>
.dashboard {
  width: 100%;
}

.stat-card {
  cursor: pointer;
  transition: transform 0.3s;
}

.stat-card:hover {
  transform: translateY(-5px);
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
}

.stat-icon.student {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-icon.class {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
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

.welcome-card {
  text-align: center;
  padding: 40px;
}

.welcome-card h2 {
  margin-bottom: 10px;
  color: #333;
}

.welcome-card p {
  color: #666;
  font-size: 16px;
}
</style>
