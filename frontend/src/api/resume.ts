import { request } from '@/utils/request'
import type { Resume, Application, PageData } from '@/types'

export const resumeApi = {
  getResume(userId: number): Promise<Resume> {
    return request.get(`/resumes/${userId}`)
  },

  updateResume(userId: number, data: Partial<Resume>): Promise<Resume> {
    return request.put(`/resumes/${userId}`, data)
  },

  uploadResumeFile(userId: number, file: File): Promise<string> {
    const formData = new FormData()
    formData.append('file', file)
    return request.post(`/resumes/${userId}/upload`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    })
  },
}

export const applicationApi = {
  applyJob(data: { jobId: number; resumeId: number; coverLetter?: string }): Promise<Application> {
    return request.post('/applications', data)
  },

  getMyApplications(params: {
    page?: number
    size?: number
    status?: string
  }): Promise<PageData<Application>> {
    return request.get('/applications', { params })
  },

  getApplicationDetail(applicationId: number): Promise<Application> {
    return request.get(`/applications/${applicationId}`)
  },

  updateApplicationStatus(
    applicationId: number,
    status: Application['status']
  ): Promise<Application> {
    return request.put(`/applications/${applicationId}/status`, { status })
  },

  getReceivedApplications(params: {
    page?: number
    size?: number
    status?: string
  }): Promise<PageData<Application>> {
    return request.get('/applications/received', { params })
  },
}
