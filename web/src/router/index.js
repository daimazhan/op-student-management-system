import { createRouter, createWebHistory } from 'vue-router'

// 基础路由（不需要权限控制）
const constantRoutes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/Login.vue'),
    meta: { title: '登录' }
  }
]

// 动态路由
const dynamicRoutes = [
  {
    path: '/',
    component: () => import('@/views/layout/Layout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/Dashboard.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/profile/Profile.vue'),
        meta: { title: '个人中心' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes: [...constantRoutes, ...dynamicRoutes]
})

// 组件映射表，用于动态导入组件
// 为了避免 Vite 的动态导入限制，我们需要预先定义所有可能的组件路径
const componentMap = {
  'dashboard/Dashboard': () => import('@/views/dashboard/Dashboard.vue'),
  'student/Student': () => import('@/views/student/Student.vue'),
  'class/Class': () => import('@/views/class/Class.vue'),
  'system/Menu': () => import('@/views/system/Menu.vue'),
  'system/User': () => import('@/views/system/User.vue'),
  'system/Role': () => import('@/views/system/Role.vue'),
  // 也可以支持小写开头的格式
  'dashboard/dashboard': () => import('@/views/dashboard/Dashboard.vue'),
  'student/student': () => import('@/views/student/Student.vue'),
  'class/class': () => import('@/views/class/Class.vue'),
  'system/menu': () => import('@/views/system/Menu.vue'),
  'system/user': () => import('@/views/system/User.vue'),
  'system/role': () => import('@/views/system/Role.vue'),
  // 可以根据需要添加更多组件映射
}

// 获取组件加载函数
function getComponentLoader(componentPath) {
  console.log('获取组件加载函数，原始路径:', componentPath)
  
  // 去掉可能的 views/ 前缀和 .vue 后缀
  let normalizedPath = componentPath
  if (normalizedPath.startsWith('/views/')) {
    normalizedPath = normalizedPath.substring(7)
  } else if (normalizedPath.startsWith('views/')) {
    normalizedPath = normalizedPath.substring(6)
  } else if (normalizedPath.startsWith('/')) {
    normalizedPath = normalizedPath.substring(1)
  }
  if (normalizedPath.endsWith('.vue')) {
    normalizedPath = normalizedPath.substring(0, normalizedPath.length - 4)
  }
  
  console.log('标准化后的路径:', normalizedPath)
  console.log('组件映射表中的键:', Object.keys(componentMap))
  
  // 从映射表中获取组件加载函数（尝试不同的大小写格式）
  if (componentMap[normalizedPath]) {
    console.log('从组件映射表找到组件:', normalizedPath)
    return componentMap[normalizedPath]
  }
  
  // 尝试首字母大写格式
  const capitalizedPath = normalizedPath.split('/').map(part => 
    part.charAt(0).toUpperCase() + part.slice(1).toLowerCase()
  ).join('/')
  
  if (componentMap[capitalizedPath]) {
    console.log('从组件映射表找到组件（大写格式）:', capitalizedPath)
    return componentMap[capitalizedPath]
  }
  
  // 如果映射表中没有，尝试使用动态导入（可能在某些情况下不工作）
  console.warn(`组件 ${normalizedPath} 未在 componentMap 中定义，尝试动态导入`)
  console.warn('尝试的路径格式:', [normalizedPath, capitalizedPath])
  return () => import(`@/views/${normalizedPath}.vue`).catch((err) => {
    console.error(`加载组件失败: @/views/${normalizedPath}.vue`, err)
    return import('@/views/dashboard/Dashboard.vue')
  })
}

// 动态添加路由
export function addRoutes(routes) {
  // 获取现有的路由
  const existingRoutes = router.getRoutes()
  const parentRoute = existingRoutes.find(r => r.path === '/')
  
  if (!parentRoute) {
    console.error('父路由 "/" 不存在，无法添加子路由')
    return
  }
  
  // 获取现有的子路由
  const existingChildren = parentRoute.children || []
  
  // 处理新路由，确保路径格式正确
  const normalizedRoutes = routes.map(route => {
    // 确保路径是相对路径（不以 / 开头）
    let normalizedPath = route.path
    if (normalizedPath.startsWith('/')) {
      normalizedPath = normalizedPath.substring(1)
    }
    
    const normalizedRoute = {
      ...route,
      path: normalizedPath
    }
    
    // 如果路由有子路由，也需要处理子路由的路径
    if (normalizedRoute.children && normalizedRoute.children.length > 0) {
      normalizedRoute.children = normalizedRoute.children.map(child => {
        let childPath = child.path
        if (childPath.startsWith('/')) {
          childPath = childPath.substring(1)
        }
        return {
          ...child,
          path: childPath
        }
      })
    }
    
    return normalizedRoute
  })
  
  // 过滤出尚未存在的路由
  const newRoutes = normalizedRoutes.filter(route => {
    const exists = existingChildren.some(existing => {
      // 检查路径是否已存在
      if (existing.path === route.path) {
        return true
      }
      // 检查是否有相同的 name
      if (existing.name && route.name && existing.name === route.name) {
        return true
      }
      return false
    })
    return !exists
  })
  
  if (newRoutes.length === 0) {
    console.log('所有路由已存在，无需添加')
    return
  }
  
  // 合并现有子路由和新路由
  const allChildren = [...existingChildren, ...newRoutes]
  
  // 移除并重新添加 '/' 路由，包含所有子路由
  router.removeRoute('/')
  
  router.addRoute({
    path: '/',
    component: () => import('@/views/layout/Layout.vue'),
    redirect: '/dashboard',
    children: allChildren
  })
  
  console.log('路由添加成功，新增路由数量:', newRoutes.length)
  console.log('当前所有路由:', router.getRoutes())
  
  // 打印路由详情，特别是嵌套路由
  const allRoutes = router.getRoutes()
  const rootRoute = allRoutes.find(r => r.path === '/')
  if (rootRoute && rootRoute.children) {
    console.log('根路由的所有子路由:')
    rootRoute.children.forEach(child => {
      console.log(`  - 路径: ${child.path}, 名称: ${child.name}, 是否有子路由: ${child.children ? '是' : '否'}`)
      if (child.children) {
        child.children.forEach(grandchild => {
          console.log(`    - 子路径: ${grandchild.path}, 名称: ${grandchild.name}`)
        })
      }
    })
  }
}

// 重置路由（退出登录时使用）
export function resetRouter() {
  isDynamicRouteLoaded = false
  isRouteLoading = false
  const newRouter = createRouter({
    history: createWebHistory(),
    routes: [...constantRoutes, ...dynamicRoutes]
  })
  router.matcher = newRouter.matcher
}

// 标记是否已加载动态路由
let isDynamicRouteLoaded = false
// 标记是否正在加载路由（防止重复请求）
let isRouteLoading = false

// 路由守卫
router.beforeEach(async (to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path === '/login') {
    if (token) {
      // 登录后重置路由状态，确保重新加载
      isDynamicRouteLoaded = false
      isRouteLoading = false
      next('/')
    } else {
      next()
    }
  } else {
    if (token) {
      // 如果已经加载过动态路由，直接放行
      if (isDynamicRouteLoaded) {
        next()
      } else if (isRouteLoading) {
        // 如果正在加载中，等待加载完成
        // 这里可以使用一个简单的轮询或者直接放行（因为加载是异步的，下次路由跳转时应该已经加载完成）
        next()
      } else {
        // 开始加载动态路由
        isRouteLoading = true
        try {
          const { getMenuTree } = await import('@/api/menu')
          const res = await getMenuTree()
          if (res.code === 200 && res.data && res.data.length > 0) {
            const routes = formatRoutes(res.data)
            console.log('格式化后的路由列表:', routes)
            addRoutes(routes)
            isDynamicRouteLoaded = true
            isRouteLoading = false
            // 确保添加路由后再跳转
            next({ ...to, replace: true })
          } else {
            // 如果没有菜单数据，标记为已加载（避免重复请求），然后放行
            isDynamicRouteLoaded = true
            isRouteLoading = false
            next()
          }
        } catch (error) {
          console.error('加载菜单失败:', error)
          // 加载失败时，标记为已加载（避免重复请求和无限循环），然后放行
          isDynamicRouteLoaded = true
          isRouteLoading = false
          next()
        }
      }
    } else {
      next('/login')
    }
  }
})

// 格式化路由
function formatRoutes(menus) {
  const routes = []
  menus.forEach(menu => {
    // 只处理类型为1的菜单（实际菜单项）
    if (menu.type === 1 && menu.component && menu.path) {
      // 将路径转换为相对路径（去掉开头的 '/'）
      let routePath = menu.path
      if (routePath.startsWith('/')) {
        routePath = routePath.substring(1)
      }
      
      // 检查是否是嵌套路径（包含 /）
      if (routePath.includes('/')) {
        // 对于嵌套路径，我们需要创建嵌套路由结构
        // 例如 system/menu 需要拆分为：
        // - system (父路由)
        //   - menu (子路由)
        const pathParts = routePath.split('/')
        const parentPath = pathParts[0]
        const childPath = pathParts.slice(1).join('/')
        
        // 查找是否已经存在该父路由
        let parentRoute = routes.find(r => r.path === parentPath)
        
        if (!parentRoute) {
          // 创建父路由（作为容器路由）
          // 父路由需要一个简单的组件来显示子路由
          // 使用 RouterView 组件
          parentRoute = {
            path: parentPath,
            name: parentPath,
            component: () => import('@/components/RouterView.vue'),
            children: []
          }
          routes.push(parentRoute)
        }
        
        // 添加子路由到父路由
        if (!parentRoute.children) {
          parentRoute.children = []
        }
        
        // 使用组件映射获取组件加载函数
        console.log('处理嵌套路由，菜单信息:', { path: menu.path, component: menu.component, name: menu.name })
        const componentLoader = getComponentLoader(menu.component)
        
        const childRoute = {
          path: childPath,
          name: routePath.replace(/\//g, '-'),
          component: componentLoader,
          meta: {
            title: menu.name,
            icon: menu.icon,
            permission: menu.permission,
            originalPath: menu.path
          }
        }
        
        parentRoute.children.push(childRoute)
        console.log('创建嵌套路由:', menu.path, '->', parentPath, '/', childPath, '完整路由:', childRoute)
      } else {
        // 单层路径，直接添加
        // 使用组件映射获取组件加载函数
        const componentLoader = getComponentLoader(menu.component)
        
        const route = {
          path: routePath,
          name: routePath.replace(/-/g, '-').replace(/-+/g, '-').replace(/^-|-$/g, '') || 'index',
          component: componentLoader,
          meta: {
            title: menu.name,
            icon: menu.icon,
            permission: menu.permission,
            originalPath: menu.path
          }
        }
        console.log('格式化路由:', menu.path, '->', routePath, route)
        routes.push(route)
      }
    }
    // 递归处理子菜单
    if (menu.children && menu.children.length > 0) {
      routes.push(...formatRoutes(menu.children))
    }
  })
  return routes
}

export default router
