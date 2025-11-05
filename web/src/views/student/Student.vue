<template>
  <div class="student-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>学生管理</span>
          <el-button type="primary" @click="handleAdd">新增学生</el-button>
        </div>
      </template>

      <div class="search-bar">
        <el-input
          v-model="queryParams.keyword"
          placeholder="请输入学号、姓名、班级或联系方式"
          style="width: 300px"
          clearable
          @keyup.enter="loadData"
        />
        <el-select
          v-model="queryParams.gender"
          placeholder="性别"
          style="width: 120px; margin-left: 10px"
          clearable
        >
          <el-option label="男" value="男" />
          <el-option label="女" value="女" />
        </el-select>
        <el-select
          v-model="queryParams.className"
          placeholder="班级"
          style="width: 200px; margin-left: 10px"
          clearable
        >
          <el-option
            v-for="item in classList"
            :key="item.className"
            :label="item.className"
            :value="item.className"
          />
        </el-select>
        <el-button type="primary" style="margin-left: 10px" @click="loadData">搜索</el-button>
        <el-button @click="resetQuery">重置</el-button>
      </div>

      <el-table :data="tableData" stripe border style="width: 100%; margin-top: 20px">
        <el-table-column prop="studentNo" label="学号" width="120" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="gender" label="性别" width="80" />
        <el-table-column prop="age" label="年龄" width="80" />
        <el-table-column prop="className" label="班级" width="150" />
        <el-table-column prop="phone" label="联系电话" width="150" />
        <el-table-column prop="email" label="邮箱" width="200" />
        <el-table-column prop="address" label="地址" show-overflow-tooltip />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="queryParams.pageNum"
        v-model:page-size="queryParams.pageSize"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        style="margin-top: 20px; justify-content: flex-end"
        @size-change="loadData"
        @current-change="loadData"
      />
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="resetForm"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
      >
        <el-form-item label="学号" prop="studentNo">
          <el-input v-model="formData.studentNo" placeholder="请输入学号" />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="formData.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="formData.gender">
            <el-radio label="男">男</el-radio>
            <el-radio label="女">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input-number v-model="formData.age" :min="1" :max="100" />
        </el-form-item>
        <el-form-item label="班级" prop="className">
          <el-select v-model="formData.className" placeholder="请选择班级" style="width: 100%">
            <el-option
              v-for="item in classList"
              :key="item.className"
              :label="item.className"
              :value="item.className"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="formData.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="formData.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="formData.address" type="textarea" placeholder="请输入地址" />
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
import { getStudentList, saveStudent, updateStudent, deleteStudent } from '@/api/student'
import { getClassList } from '@/api/class'

const tableData = ref([])
const classList = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const dialogTitle = ref('新增学生')
const formRef = ref(null)

const queryParams = reactive({
  keyword: '',
  gender: '',
  className: '',
  pageNum: 1,
  pageSize: 10
})

const formData = reactive({
  id: null,
  studentNo: '',
  name: '',
  gender: '男',
  age: null,
  className: '',
  phone: '',
  email: '',
  address: ''
})

const formRules = {
  studentNo: [{ required: true, message: '请输入学号', trigger: 'blur' }],
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  className: [{ required: true, message: '请选择班级', trigger: 'change' }]
}

const loadData = async () => {
  try {
    const res = await getStudentList(queryParams)
    if (res.code === 200) {
      tableData.value = res.data.list || []
      total.value = res.data.total || 0
    }
  } catch (error) {
    console.error(error)
  }
}

const loadClassList = async () => {
  try {
    const res = await getClassList()
    if (res.code === 200) {
      classList.value = res.data || []
    }
  } catch (error) {
    console.error(error)
  }
}

const resetQuery = () => {
  queryParams.keyword = ''
  queryParams.gender = ''
  queryParams.className = ''
  queryParams.pageNum = 1
  loadData()
}

const handleAdd = () => {
  dialogTitle.value = '新增学生'
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑学生'
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该学生吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const res = await deleteStudent(row.id)
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
          ? await updateStudent(formData)
          : await saveStudent(formData)
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
    studentNo: '',
    name: '',
    gender: '男',
    age: null,
    className: '',
    phone: '',
    email: '',
    address: ''
  })
}

onMounted(() => {
  loadData()
  loadClassList()
})
</script>

<style scoped>
.student-container {
  width: 100%;
}

.student-container :deep(.el-card) {
  width: 100%;
}

.student-container :deep(.el-card__body) {
  padding: 20px;
}

.student-container :deep(.el-table) {
  width: 100% !important;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-bar {
  display: flex;
  align-items: center;
}
</style>
