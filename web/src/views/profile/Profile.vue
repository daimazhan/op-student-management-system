<template>
  <div class="profile-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>个人中心</span>
        </div>
      </template>

      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="120px"
        style="max-width: 600px"
      >
        <el-form-item label="用户名">
          <el-input v-model="formData.username" disabled />
        </el-form-item>
        <el-form-item label="角色">
          <el-input v-model="formData.roleName" disabled />
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="formData.realName" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="formData.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="formData.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSubmit">保存</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="warning" @click="showChangePasswordDialog">修改密码</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 修改密码对话框 -->
    <el-dialog v-model="changePasswordDialogVisible" title="修改密码" width="450px" @close="resetPasswordForm">
      <el-form
        ref="passwordFormRef"
        :model="passwordForm"
        :rules="passwordRules"
        label-width="100px"
      >
        <el-form-item label="旧密码" prop="oldPassword">
          <el-input
            v-model="passwordForm.oldPassword"
            type="password"
            placeholder="请输入旧密码"
            show-password
          />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input
            v-model="passwordForm.newPassword"
            type="password"
            placeholder="请输入新密码"
            show-password
          />
        </el-form-item>
        <el-form-item label="确认新密码" prop="confirmPassword">
          <el-input
            v-model="passwordForm.confirmPassword"
            type="password"
            placeholder="请再次输入新密码"
            show-password
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="changePasswordDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleChangePassword">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getUserInfo, updateProfile, changePassword } from '@/api/auth'
import { useUserStore } from '@/store/user'

const formRef = ref(null)
const passwordFormRef = ref(null)
const userStore = useUserStore()

const changePasswordDialogVisible = ref(false)

const formData = reactive({
  id: null,
  username: '',
  role: '',
  roleName: '',
  realName: '',
  email: '',
  phone: ''
})

const originalData = reactive({
  realName: '',
  email: '',
  phone: ''
})

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const formRules = {
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ]
}

// 自定义验证规则：确认新密码必须与新密码一致
const validateConfirmPassword = (rule, value, callback) => {
  if (value !== passwordForm.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const passwordRules = {
  oldPassword: [
    { required: true, message: '请输入旧密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

const loadUserInfo = async () => {
  try {
    const res = await getUserInfo()
    if (res.code === 200) {
      formData.id = res.data.id
      formData.username = res.data.username || ''
      formData.role = res.data.role || ''
      formData.roleName = res.data.role === 'ADMIN' ? '管理员' : '普通用户'
      formData.realName = res.data.realName || ''
      formData.email = res.data.email || ''
      formData.phone = res.data.phone || ''
      
      // 保存原始数据用于重置
      originalData.realName = formData.realName
      originalData.email = formData.email
      originalData.phone = formData.phone
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('加载用户信息失败')
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const res = await updateProfile({
          id: formData.id,
          realName: formData.realName,
          email: formData.email,
          phone: formData.phone
        })
        if (res.code === 200) {
          ElMessage.success('更新成功')
          // 更新用户store中的信息
          userStore.setUserInfo({
            ...userStore.userInfo,
            realName: formData.realName,
            email: formData.email,
            phone: formData.phone
          })
          // 重新加载用户信息
          await loadUserInfo()
        } else {
          ElMessage.error(res.msg || '更新失败')
        }
      } catch (error) {
        console.error(error)
        ElMessage.error(error.response?.data?.msg || '更新失败')
      }
    }
  })
}

const handleReset = () => {
  formData.realName = originalData.realName
  formData.email = originalData.email
  formData.phone = originalData.phone
  if (formRef.value) {
    formRef.value.clearValidate()
  }
}

const showChangePasswordDialog = () => {
  changePasswordDialogVisible.value = true
  resetPasswordForm()
}

const resetPasswordForm = () => {
  if (passwordFormRef.value) {
    passwordFormRef.value.resetFields()
  }
  passwordForm.oldPassword = ''
  passwordForm.newPassword = ''
  passwordForm.confirmPassword = ''
}

const handleChangePassword = async () => {
  if (!passwordFormRef.value) return
  await passwordFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const res = await changePassword({
          oldPassword: passwordForm.oldPassword,
          newPassword: passwordForm.newPassword
        })
        if (res.code === 200) {
          ElMessage.success('密码修改成功，请重新登录')
          changePasswordDialogVisible.value = false
          resetPasswordForm()
          // 密码修改成功后，可以选择自动登出或保持登录
          // 这里我们提示用户重新登录，但不强制登出
        } else {
          ElMessage.error(res.msg || '密码修改失败')
        }
      } catch (error) {
        console.error(error)
        ElMessage.error(error.response?.data?.msg || '密码修改失败')
      }
    }
  })
}

onMounted(() => {
  loadUserInfo()
})
</script>

<style scoped>
.profile-container {
  width: 100%;
}

.profile-container :deep(.el-card) {
  width: 100%;
}

.profile-container :deep(.el-card__body) {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
