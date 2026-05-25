import { request } from '@/utils/request'
import type { Job, JobQuery, PageData } from '@/types'

export const jobApi = {
  getJobList(params: JobQuery): Promise<PageData<Job>> {
    return request.get('/jobs', { params })
  },

  getJobDetail(jobId: number): Promise<Job> {
    return request.get(`/jobs/${jobId}`)
  },

  createJob(data: Partial<Job>): Promise<Job> {
    return request.post('/jobs', data)
  },

  updateJob(jobId: number, data: Partial<Job>): Promise<Job> {
    return request.put(`/jobs/${jobId}`, data)
  },

  deleteJob(jobId: number): Promise<void> {
    return request.delete(`/jobs/${jobId}`)
  },

  getMyJobs(params: { page?: number; size?: number; status?: string }): Promise<PageData<Job>> {
    return request.get('/jobs/my', { params })
  },

  searchJobs(keyword: string, params: JobQuery): Promise<PageData<Job>> {
    return request.get('/jobs/search', { params: { keyword, ...params } })
  },
}
