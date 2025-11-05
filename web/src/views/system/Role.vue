<template>
  <div class="role-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>角色管理</span>
          <el-button type="primary" @click="handleAdd">新增角色</el-button>
        </div>
      </template>

      <el-table :data="tableData" stripe border style="width: 100%">
        <el-table-column prop="id" label="ID" min-width="80" />
        <el-table-column prop="roleCode" label="角色编码" min-width="120" />
        <el-table-column prop="roleName" label="角色名称" min-width="150" />
        <el-table-column prop="description" label="描述" min-width="200" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.status === 1" type="success">启用</el-tag>
            <el-tag v-else type="danger">禁用</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="success" size="small" @click="handleAssignMenu(row)">分配权限</el-button>
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
        <el-form-item label="角色编码" prop="roleCode">
          <el-input v-model="formData.roleCode" placeholder="请输入角色编码，如：ADMIN" />
        </el-form-item>
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="formData.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="3"
            placeholder="请输入角色描述"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 权限分配弹窗 -->
    <el-dialog
      v-model="assignDialogVisible"
      title="分配权限"
      width="600px"
      @close="handleCloseAssignDialog"
    >
      <el-form label-width="100px">
        <el-form-item label="角色名称">
          <el-input v-model="currentRole.roleName" disabled />
        </el-form-item>
        <el-form-item label="菜单权限">
          <el-tree
            ref="menuTreeRef"
            :data="allMenuTree"
            :props="{ label: 'name', children: 'children' }"
            show-checkbox
            node-key="id"
            style="margin-top: 10px; max-height: 400px; overflow: auto"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="handleCloseAssignDialog">取消</el-button>
        <el-button type="primary" @click="handleAssignSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getRoleList, saveRole, updateRole, deleteRole, assignMenuToRole, getMenuIdsByRole } from '@/api/role'
import { getAllMenuTree } from '@/api/menu'

const tableData = ref([])
const dialogVisible = ref(false)
const assignDialogVisible = ref(false)
const dialogTitle = ref('新增角色')
const formRef = ref(null)
const menuTreeRef = ref(null)
const allMenuTree = ref([])
const currentRole = reactive({ id: null, roleName: '' })

const formData = reactive({
  id: null,
  roleCode: '',
  roleName: '',
  description: '',
  status: 1
})

const formRules = {
  roleCode: [{ required: true, message: '请输入角色编码', trigger: 'blur' }],
  roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }]
}

const loadData = async () => {
  try {
    const res = await getRoleList()
    if (res.code === 200) {
      tableData.value = res.data || []
    }
  } catch (error) {
    console.error(error)
  }
}

const loadMenuTree = async () => {
  try {
    const res = await getAllMenuTree()
    if (res.code === 200) {
      allMenuTree.value = res.data || []
    }
  } catch (error) {
    console.error(error)
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

const handleAdd = () => {
  dialogTitle.value = '新增角色'
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑角色'
  Object.assign(formData, {
    id: row.id,
    roleCode: row.roleCode,
    roleName: row.roleName,
    description: row.description || '',
    status: row.status
  })
  dialogVisible.value = true
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该角色吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const res = await deleteRole(row.id)
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

const handleAssignMenu = async (row) => {
  currentRole.id = row.id
  currentRole.roleName = row.roleName
  assignDialogVisible.value = true
  
  // 加载该角色的菜单权限
  try {
    const res = await getMenuIdsByRole(row.id)
    if (res.code === 200 && menuTreeRef.value) {
      // 等待树组件渲染完成
      await new Promise(resolve => setTimeout(resolve, 100))
      menuTreeRef.value.setCheckedKeys(res.data || [])
    }
  } catch (error) {
    console.error(error)
  }
}

const handleAssignSubmit = async () => {
  if (!menuTreeRef.value) return
  
  const checkedKeys = menuTreeRef.value.getCheckedKeys()
  try {
    const res = await assignMenuToRole({
      roleId: currentRole.id,
      menuIds: checkedKeys
    })
    if (res.code === 200) {
      ElMessage.success('分配成功')
      handleCloseAssignDialog()
    }
  } catch (error) {
    console.error(error)
  }
}

const handleCloseAssignDialog = () => {
  assignDialogVisible.value = false
  currentRole.id = null
  currentRole.roleName = ''
  if (menuTreeRef.value) {
    menuTreeRef.value.setCheckedKeys([])
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const res = formData.id
          ? await updateRole(formData)
          : await saveRole(formData)
        if (res.code === 200) {
          ElMessage.success(formData.id ? '更新成功' : '保存成功')
          dialogVisible.value = false
          loadData()
        } else {
          ElMessage.error(res.msg || '操作失败')
        }
      } catch (error) {
        console.error(error)
        ElMessage.error(error.response?.data?.msg || '操作失败')
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
    roleCode: '',
    roleName: '',
    description: '',
    status: 1
  })
}

onMounted(() => {
  loadData()
  loadMenuTree()
})
</script>

<style scoped>
.role-container {
  width: 100%;
}

.role-container :deep(.el-card) {
  width: 100%;
}

.role-container :deep(.el-card__body) {
  padding: 20px;
}

.role-container :deep(.el-table) {
  width: 100% !important;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
