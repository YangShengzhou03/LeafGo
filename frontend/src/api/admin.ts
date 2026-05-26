import { request } from '@/utils/request'
import type { User, Company, Job, PageData } from '@/types'

interface AdminStats {
  userCount: number
  companyCount: number
  jobCount: number
  applicationCount: number
}

export const adminApi = {
  getStats: (): Promise<AdminStats> => {
    return request.get('/admin/stats')
  },

  getUsers: (params: {
    page?: number
    size?: number
    keyword?: string
  }): Promise<PageData<User>> => {
    return request.get('/admin/users', { params })
  },

  toggleUserStatus: (userId: number): Promise<User> => {
    return request.put(`/admin/users/${userId}/status`)
  },

  getCompanies: (params: { page?: number; size?: number }): Promise<PageData<Company>> => {
    return request.get('/admin/companies', { params })
  },

  getJobs: (params: { page?: number; size?: number }): Promise<PageData<Job>> => {
    return request.get('/admin/jobs', { params })
  },
}
