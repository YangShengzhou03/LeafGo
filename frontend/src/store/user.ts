import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { User } from '@/types'
import { authApi, userApi } from '@/api'
import router from '@/router'

export const useUserStore = defineStore('user', () => {
  const token = ref<string>(localStorage.getItem('token') || '')
  const userInfo = ref<User | null>(null)

  const isLoggedIn = computed(() => !!token.value)
  const isJobSeeker = computed(() => userInfo.value?.userType === 'job_seeker')
  const isEmployer = computed(() => userInfo.value?.userType === 'employer')

  const login = async (
    email: string,
    password: string
  ): Promise<{ userId: number; token: string; userType: string }> => {
    const data = await authApi.login({ email, password })
    token.value = data.token
    localStorage.setItem('token', data.token)
    await fetchUserInfo(data.userId)
    return data
  }

  const register = async (data: {
    username: string
    email: string
    password: string
    userType: 'job_seeker' | 'employer'
  }): Promise<{ userId: number; token: string }> => {
    const res = await authApi.register(data)
    token.value = res.token
    localStorage.setItem('token', res.token)
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
    login,
    register,
    fetchUserInfo,
    logout,
    updateUserInfo,
  }
})
