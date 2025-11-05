<template>
  <div class="menu-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>菜单管理</span>
          <el-button type="primary" @click="handleAdd">新增菜单</el-button>
        </div>
      </template>

      <el-table
        :data="menuList"
        row-key="id"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        :default-expand-all="false"
        border
        style="width: 100%"
      >
        <el-table-column prop="name" label="菜单名称" min-width="200" />
        <el-table-column prop="path" label="路由路径" min-width="200" />
        <el-table-column prop="component" label="组件路径" min-width="250" />
        <el-table-column prop="icon" label="图标" width="120">
          <template #default="{ row }">
            <el-icon v-if="row.icon">
              <component :is="row.icon" />
            </el-icon>
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column prop="type" label="类型" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.type === 0" type="info">目录</el-tag>
            <el-tag v-else-if="row.type === 1" type="success">菜单</el-tag>
            <el-tag v-else type="warning">按钮</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="permission" label="权限标识" min-width="150" />
        <el-table-column label="操作" width="200" fixed="right">
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
      width="600px"
      @close="resetForm"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
      >
        <el-form-item label="菜单名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入菜单名称" />
        </el-form-item>
        <el-form-item label="路由路径" prop="path">
          <el-input v-model="formData.path" placeholder="请输入路由路径，如：/student" />
        </el-form-item>
        <el-form-item label="组件路径" prop="component">
          <el-input v-model="formData.component" placeholder="请输入组件路径，如：views/student/Student" />
        </el-form-item>
        <el-form-item label="图标" prop="icon">
          <el-input v-model="formData.icon" placeholder="请输入图标名称，如：User" />
        </el-form-item>
        <el-form-item label="父菜单" prop="parentId">
          <el-tree-select
            v-model="formData.parentId"
            :data="allMenuTree"
            :props="{ label: 'name', value: 'id', children: 'children' }"
            placeholder="请选择父菜单"
            check-strictly
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="formData.sort" :min="0" />
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-radio-group v-model="formData.type">
            <el-radio :label="0">目录</el-radio>
            <el-radio :label="1">菜单</el-radio>
            <el-radio :label="2">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="权限标识" prop="permission">
          <el-input v-model="formData.permission" placeholder="请输入权限标识，如：student:view" />
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
import { getAllMenuTree, saveMenu, updateMenu, deleteMenu } from '@/api/menu'

const menuList = ref([])
const allMenuTree = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增菜单')
const formRef = ref(null)

const formData = reactive({
  id: null,
  name: '',
  path: '',
  component: '',
  icon: '',
  parentId: 0,
  sort: 0,
  type: 1,
  permission: ''
})

const formRules = {
  name: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }],
  type: [{ required: true, message: '请选择类型', trigger: 'change' }]
}

const loadData = async () => {
  try {
    // 使用 getAllMenuTree 获取树形结构的菜单数据，用于层级表格展示
    const res = await getAllMenuTree()
    if (res.code === 200) {
      menuList.value = res.data || []
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



const handleAdd = () => {
  dialogTitle.value = '新增菜单'
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑菜单'
  Object.assign(formData, {
    id: row.id,
    name: row.name,
    path: row.path,
    component: row.component,
    icon: row.icon,
    parentId: row.parentId || 0,
    sort: row.sort || 0,
    type: row.type,
    permission: row.permission
  })
  dialogVisible.value = true
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该菜单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const res = await deleteMenu(row.id)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      loadData()
      // 重新加载菜单树（用于新增/编辑对话框的父菜单选择）
      loadMenuTree()
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
          ? await updateMenu(formData)
          : await saveMenu(formData)
        if (res.code === 200) {
          ElMessage.success(formData.id ? '更新成功' : '保存成功')
          dialogVisible.value = false
          loadData()
          loadMenuTree()
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
    name: '',
    path: '',
    component: '',
    icon: '',
    parentId: 0,
    sort: 0,
    type: 1,
    permission: ''
  })
}

onMounted(() => {
  loadData()
  loadMenuTree()
})
</script>

<style scoped>
.menu-container {
  width: 100%;
}

.menu-container :deep(.el-card) {
  width: 100%;
}

.menu-container :deep(.el-card__body) {
  padding: 20px;
}

.menu-container :deep(.el-table) {
  width: 100% !important;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}


</style>
