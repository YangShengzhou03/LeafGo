import { request } from '@/utils/request'
import type { Job } from '@/types'

export interface Application {
  id: number
  jobId: number
  userId: number
  resumeId: number
  status: string
  coverLetter?: string
  job?: Job
  createdAt: string
  updatedAt: string
}

export interface ApplicationListResponse {
  content: Application[]
  totalElements: number
  totalPages: number
  size: number
  number: number
}

export const applicationApi = {
  getMyApplications(params?: {
    page?: number
    size?: number
    status?: string
  }): Promise<ApplicationListResponse> {
    return request.get('/applications', { params })
  },

  applyJob(data: { jobId: number; resumeId: number; coverLetter?: string }): Promise<Application> {
    return request.post('/applications', data)
  },

  withdrawApplication(applicationId: number): Promise<void> {
    return request.delete(`/applications/${applicationId}`)
  },
}
