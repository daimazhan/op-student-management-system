<template>
  <div class="class-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>班级管理</span>
          <el-button type="primary" @click="handleAdd">新增班级</el-button>
        </div>
      </template>

      <el-table :data="tableData" stripe border style="width: 100%">
        <el-table-column prop="className" label="班级名称" min-width="150" />
        <el-table-column prop="grade" label="年级" min-width="100" />
        <el-table-column prop="major" label="专业" min-width="150" />
        <el-table-column prop="teacherName" label="班主任" min-width="120" />
        <el-table-column prop="studentCount" label="学生人数" min-width="100" />
        <el-table-column prop="createTime" label="创建时间" min-width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      @close="resetForm"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
      >
        <el-form-item label="班级名称" prop="className">
          <el-input v-model="formData.className" placeholder="请输入班级名称" />
        </el-form-item>
        <el-form-item label="年级" prop="grade">
          <el-input v-model="formData.grade" placeholder="请输入年级，如：2023级" />
        </el-form-item>
        <el-form-item label="专业" prop="major">
          <el-input v-model="formData.major" placeholder="请输入专业" />
        </el-form-item>
        <el-form-item label="班主任" prop="teacherName">
          <el-input v-model="formData.teacherName" placeholder="请输入班主任姓名" />
        </el-form-item>
        <el-form-item label="学生人数" prop="studentCount">
          <el-input-number v-model="formData.studentCount" :min="0" />
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
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getClassList, saveClass, updateClass, deleteClass } from '@/api/class'

const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增班级')
const formRef = ref(null)

const formData = reactive({
  id: null,
  className: '',
  grade: '',
  major: '',
  teacherName: '',
  studentCount: 0
})

const formRules = {
  className: [{ required: true, message: '请输入班级名称', trigger: 'blur' }]
}

const loadData = async () => {
  try {
    const res = await getClassList()
    if (res.code === 200) {
      tableData.value = res.data || []
    }
  } catch (error) {
    console.error(error)
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN')
}

const handleAdd = () => {
  dialogTitle.value = '新增班级'
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑班级'
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该班级吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const res = await deleteClass(row.id)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      loadData()
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error(error)
    }
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const res = formData.id
          ? await updateClass(formData)
          : await saveClass(formData)
        if (res.code === 200) {
          ElMessage.success(formData.id ? '更新成功' : '保存成功')
          dialogVisible.value = false
          loadData()
        }
      } catch (error) {
        console.error(error)
      }
    }
  })
}

const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  Object.assign(formData, {
    id: null,
    className: '',
    grade: '',
    major: '',
    teacherName: '',
    studentCount: 0
  })
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.class-container {
  width: 100%;
}

.class-container :deep(.el-card) {
  width: 100%;
}

.class-container :deep(.el-card__body) {
  padding: 20px;
}

.class-container :deep(.el-table) {
  width: 100% !important;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
