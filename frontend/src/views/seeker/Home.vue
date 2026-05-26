<template>
  <div class="seeker-home">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索职位、公司"
          size="large"
          @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
          </template>
        </el-input>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="section">
      <el-col :span="24">
        <h2>精选职位</h2>
      </el-col>

      <el-col :span="8" v-for="job in jobList" :key="job.id">
        <el-card shadow="hover" class="job-card" @click="$router.push(`/seeker/jobs/${job.id}`)">
          <div class="job-title">{{ job.title }}</div>
          <div class="job-salary">{{ job.salary }}</div>
          <div class="job-meta">{{ getLocation(job) }} · {{ job.experience }}</div>
          <div class="company-name">{{ getCompanyName(job) }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="section">
      <el-col :span="24">
        <h2>热门企业</h2>
      </el-col>

      <el-col :span="6" v-for="company in companyList" :key="company.id">
        <el-card
          shadow="hover"
          class="company-card"
          @click="$router.push(`/seeker/companies/${company.id}`)"
        >
          <div class="company-name">{{ company.name }}</div>
          <div class="company-meta">{{ company.industry }} · {{ company.scale }}</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { jobApi } from '@/api/job'
import { companyApi } from '@/api/company'
import type { Job, Company } from '@/types'

defineOptions({
  name: 'SeekerHome',
})

const router = useRouter()

const searchKeyword = ref('')
const jobList = ref<Job[]>([])
const companyList = ref<Company[]>([])

const getCompanyName = (job: Job): string => {
  if (typeof job.company === 'string') {
    return job.company
  }
  if (job.company && typeof job.company === 'object') {
    return job.company.name
  }
  return ''
}

const getLocation = (job: Job): string => {
  if (job.city && job.district) {
    return `${job.city}·${job.district}`
  }
  if (job.city) {
    return job.city
  }
  if (typeof job.company === 'object' && job.company) {
    const c = job.company
    if (c.city && c.district) {
      return `${c.city}·${c.district}`
    }
    if (c.city) {
      return c.city
    }
  }
  return ''
}

const fetchJobs = async (): Promise<void> => {
  try {
    const result = await jobApi.getJobList({ page: 1, size: 3 })
    jobList.value = result.content
  } catch {
    jobList.value = []
  }
}

const fetchCompanies = async (): Promise<void> => {
  try {
    const result = await companyApi.getCompanyList({ page: 1, size: 4 })
    companyList.value = result.content
  } catch {
    companyList.value = []
  }
}

const handleSearch = (): void => {
  if (searchKeyword.value.trim()) {
    router.push({
      path: '/seeker/jobs',
      query: { keyword: searchKeyword.value },
    })
  }
}

onMounted(() => {
  fetchJobs()
  fetchCompanies()
})
</script>

<style scoped lang="scss">
.seeker-home {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.section {
  margin-top: 20px;
}
</style>
