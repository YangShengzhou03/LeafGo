import { request } from '@/utils/request'
import type { User } from '@/types'

export const authApi = {
  login(data: { phone: string; code: string }): Promise<{ userId: number; token: string; userType: string }> {
    return request.post('/auth/login', data)
  },

  register(data: { username: string; phone: string; code: string; userType: string }): Promise<{ userId: number; token: string }> {
    return request.post('/auth/register', data)
  },

  logout(): Promise<void> {
    return request.post('/auth/logout')
  },
}

export const userApi = {
  getUserInfo(userId: number): Promise<User> {
    return request.get(`/users/${userId}`)
  },

  updateUserInfo(userId: number, data: Partial<User>): Promise<User> {
    return request.put(`/users/${userId}`, data)
  },

  uploadAvatar(userId: number, file: File): Promise<string> {
    const formData = new FormData()
    formData.append('file', file)
    return request.post(`/users/${userId}/avatar`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    })
  },
}
