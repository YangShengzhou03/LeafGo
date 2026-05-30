import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { User } from '@/types'
import { authApi, userApi } from '@/api'
import router from '@/router'

export const useUserStore = defineStore('user', () => {
  const token = ref<string>(localStorage.getItem('token') || '')
  const userInfo = ref<User | null>(null)

  const isLoggedIn = computed(() => !!token.value)
  const isJobSeeker = computed(() => userInfo.value?.userType === 'JOB_SEEKER')
  const isEmployer = computed(() => userInfo.value?.userType === 'EMPLOYER')

  const initUser = async (): Promise<void> => {
    const savedUserId = localStorage.getItem('userId')
    const savedUserType = localStorage.getItem('userType') as 'JOB_SEEKER' | 'EMPLOYER' | null

    if (token.value && savedUserId) {
      try {
        const user = await userApi.getUserInfo(Number(savedUserId))
        userInfo.value = user
        if (user.userType !== savedUserType) {
          localStorage.setItem('userType', user.userType)
        }
      } catch (error) {
        console.error('Failed to fetch user info:', error)
        logout()
      }
    }
  }

  const login = async (
    phone: string,
    code: string
  ): Promise<{ userId: number; token: string; userType: string }> => {
    const data = await authApi.login({ phone, code })
    token.value = data.token
    localStorage.setItem('token', data.token)
    localStorage.setItem('userId', String(data.userId))
    localStorage.setItem('userType', data.userType)
    await fetchUserInfo(data.userId)
    return data
  }

  const register = async (data: {
    username: string
    phone: string
    code: string
    userType: 'JOB_SEEKER' | 'EMPLOYER'
  }): Promise<{ userId: number; token: string; userType: string }> => {
    const res = await authApi.register(data)
    token.value = res.token
    localStorage.setItem('token', res.token)
    localStorage.setItem('userId', String(res.userId))
    localStorage.setItem('userType', res.userType)
    await fetchUserInfo(res.userId)
    return res
  }

  const fetchUserInfo = async (userId: number): Promise<User> => {
    const user = await userApi.getUserInfo(userId)
    userInfo.value = user
    return user
  }

  const logout = (): void => {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('userId')
    localStorage.removeItem('userType')
    router.push('/login')
  }

  const updateUserInfo = async (data: Partial<User>): Promise<User | undefined> => {
    if (!userInfo.value) return
    const user = await userApi.updateUserInfo(userInfo.value.id, data)
    userInfo.value = user
    return user
  }

  return {
    token,
    userInfo,
    isLoggedIn,
    isJobSeeker,
    isEmployer,
    initUser,
    login,
    register,
    fetchUserInfo,
    logout,
    updateUserInfo,
  }
})
