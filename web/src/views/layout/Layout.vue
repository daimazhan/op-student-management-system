<template>
  <el-container class="layout-container">
    <el-header class="layout-header">
      <div class="header-left">
        <h2>学生管理系统</h2>
      </div>
      <div class="header-right">
        <el-dropdown @command="handleCommand">
          <span class="user-dropdown">
            <el-icon style="margin-right: 5px"><User /></el-icon>
            <span class="username">{{ userInfo.username || '用户' }}</span>
            <el-icon class="el-icon--right"><arrow-down /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">
                <el-icon><UserFilled /></el-icon>
                个人中心
              </el-dropdown-item>
              <el-dropdown-item divided command="logout">
                <el-icon><SwitchButton /></el-icon>
                退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </el-header>
    <el-container>
      <el-aside width="200px" class="layout-aside">
        <el-menu
          :default-active="activeMenu"
          router
          class="layout-menu"
        >
          <menu-item
            v-for="menu in menuList"
            :key="menu.id"
            :menu="menu"
          />
        </el-menu>
      </el-aside>
      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed, ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { User, ArrowDown, UserFilled, SwitchButton } from '@element-plus/icons-vue'
import { logout } from '@/api/auth'
import { getMenuTree } from '@/api/menu'
import { useUserStore } from '@/store/user'
import MenuItem from '@/components/MenuItem.vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)
const userInfo = computed(() => userStore.userInfo)
const menuList = ref([])
const isLoadingMenus = ref(false)

const loadMenus = async () => {
  // 如果正在加载，直接返回，避免重复请求
  if (isLoadingMenus.value) {
    return
  }
  
  isLoadingMenus.value = true
  try {
    const res = await getMenuTree()
    if (res.code === 200) {
      menuList.value = res.data || []
    }
  } catch (error) {
    console.error('加载菜单失败:', error)
  } finally {
    isLoadingMenus.value = false
  }
}

onMounted(() => {
  loadMenus()
})

const handleCommand = async (command) => {
  if (command === 'profile') {
    router.push('/profile')
  } else if (command === 'logout') {
    handleLogout()
  }
}

const handleLogout = async () => {
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await logout()
    userStore.logout()
    router.push('/login')
  } catch (error) {
    // 用户取消
  }
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.layout-header {
  background: #409eff;
  color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.header-left h2 {
  margin: 0;
  font-size: 20px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

.user-dropdown {
  display: flex;
  align-items: center;
  color: white;
  cursor: pointer;
  padding: 5px 10px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.user-dropdown:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.username {
  font-size: 14px;
  margin: 0 5px;
}

.layout-aside {
  background: #f5f5f5;
}

.layout-menu {
  height: 100%;
  border-right: none;
}

.layout-main {
  background: #f0f2f5;
  padding: 20px;
}
</style>
