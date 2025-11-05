import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  
  // 安全地解析 JSON，避免解析错误
  const getUserInfo = () => {
    try {
      const info = localStorage.getItem('userInfo')
      if (!info || info === 'undefined' || info === 'null') {
        return {}
      }
      return JSON.parse(info)
    } catch (e) {
      console.error('解析用户信息失败:', e)
      return {}
    }
  }
  
  const userInfo = ref(getUserInfo())

  function setToken(val) {
    token.value = val
    localStorage.setItem('token', val)
  }

  function setUserInfo(val) {
    userInfo.value = val
    localStorage.setItem('userInfo', JSON.stringify(val))
  }

  function logout() {
    token.value = ''
    userInfo.value = {}
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
  }

  return {
    token,
    userInfo,
    setToken,
    setUserInfo,
    logout
  }
})
