<template>
  <!-- 过滤掉按钮类型（type=2）的菜单，按钮不应该显示在菜单中 -->
  <template v-if="menu.type !== 2">
    <el-sub-menu v-if="filteredChildren.length > 0" :index="getMenuPath(menu)">
      <template #title>
        <el-icon v-if="menu.icon">
          <component :is="menu.icon" />
        </el-icon>
        <span>{{ menu.name }}</span>
      </template>
      <menu-item
        v-for="child in filteredChildren"
        :key="child.id"
        :menu="child"
      />
    </el-sub-menu>
    <el-menu-item v-else :index="getMenuPath(menu)">
      <el-icon v-if="menu.icon">
        <component :is="menu.icon" />
      </el-icon>
      <span>{{ menu.name }}</span>
    </el-menu-item>
  </template>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  menu: {
    type: Object,
    required: true
  }
})

// 过滤掉按钮类型（type=2）的子菜单
const filteredChildren = computed(() => {
  if (!props.menu.children || props.menu.children.length === 0) {
    return []
  }
  return props.menu.children.filter(child => child.type !== 2)
})

// 获取菜单路径，确保与路由匹配
const getMenuPath = (menu) => {
  if (!menu.path) {
    return menu.id?.toString() || ''
  }
  // Element Plus 的 router 模式需要完整的路径
  // 如果路径已经是完整路径（以 / 开头），直接返回
  // 如果不是，添加 / 前缀
  const path = menu.path.startsWith('/') ? menu.path : `/${menu.path}`
  console.log('MenuItem getMenuPath:', { name: menu.name, path: menu.path, result: path })
  return path
}
</script>
