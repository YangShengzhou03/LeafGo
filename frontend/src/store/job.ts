import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Job, JobQuery, PageData } from '@/types'
import { jobApi } from '@/api'

export const useJobStore = defineStore('job', () => {
  const jobList = ref<Job[]>([])
  const currentJob = ref<Job | null>(null)
  const total = ref(0)
  const loading = ref(false)
  const searchParams = ref<JobQuery>({
    page: 1,
    size: 20,
  })

  const fetchJobList = async (params?: JobQuery): Promise<PageData<Job>> => {
    loading.value = true
    try {
      const query = { ...searchParams.value, ...params }
      const data: PageData<Job> = await jobApi.getJobList(query)
      jobList.value = data.content
      total.value = data.totalElements
      searchParams.value = query
      return data
    } finally {
      loading.value = false
    }
  }

  const fetchJobDetail = async (jobId: number): Promise<Job> => {
    loading.value = true
    try {
      const job = await jobApi.getJobDetail(jobId)
      currentJob.value = job
      return job
    } finally {
      loading.value = false
    }
  }

  const searchJobs = async (keyword: string, params?: JobQuery): Promise<PageData<Job>> => {
    loading.value = true
    try {
      const query = { ...searchParams.value, keyword, ...params }
      const data: PageData<Job> = await jobApi.searchJobs(keyword, query)
      jobList.value = data.content
      total.value = data.totalElements
      return data
    } finally {
      loading.value = false
    }
  }

  return {
    jobList,
    currentJob,
    total,
    loading,
    searchParams,
    fetchJobList,
    fetchJobDetail,
    searchJobs,
  }
})
