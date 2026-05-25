<template>
  <div class="campus-page">
    <NavBar />

    <div class="page-container">
      <div class="campus-tabs">
        <span
          v-for="tab in tabs"
          :key="tab.value"
          class="tab-item"
          :class="{ active: activeTab === tab.value }"
          @click="handleTabChange(tab.value)"
        >
          {{ tab.label }}
        </span>
      </div>

      <div class="search-filter-bar">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索职位、公司"
          class="search-input"
          @keyup.enter="handleSearch"
          clearable
        />
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <div class="filter-divider"></div>
        <div class="filter-group">
          <span class="filter-label">按专业筛选</span>
          <el-select
            v-model="filters.major"
            placeholder="不限"
            class="filter-select"
            @change="handleFilterChange"
          >
            <el-option label="不限" value="" />
            <el-option label="计算机" value="计算机" />
            <el-option label="电子信息" value="电子信息" />
            <el-option label="机械工程" value="机械工程" />
            <el-option label="工商管理" value="工商管理" />
          </el-select>
        </div>
        <div class="filter-group">
          <span class="filter-label">按职类筛选</span>
          <el-select
            v-model="filters.category"
            placeholder="不限"
            class="filter-select"
            @change="handleFilterChange"
          >
            <el-option label="不限" value="" />
            <el-option label="技术" value="技术" />
            <el-option label="产品" value="产品" />
            <el-option label="设计" value="设计" />
            <el-option label="运营" value="运营" />
            <el-option label="销售" value="销售" />
          </el-select>
        </div>
      </div>

      <div class="major-tags">
        <span
          v-for="major in majors"
          :key="major"
          class="major-tag"
          :class="{ active: selectedMajor === major }"
          @click="handleMajorClick(major)"
        >
          {{ major }}
        </span>
      </div>

      <div class="enterprise-section">
        <div class="section-header">
          <h2 class="section-title">企业招聘</h2>
          <router-link to="/companies" class="view-more">查看更多</router-link>
        </div>
        <div class="enterprise-grid" v-loading="enterpriseLoading">
          <div
            v-for="enterprise in enterprises"
            :key="enterprise.id"
            class="enterprise-card"
            @click="goToCompany(enterprise.id)"
          >
            <div class="enterprise-logo">{{ enterprise.name?.charAt(0) }}</div>
            <div class="enterprise-info">
              <div class="enterprise-name">{{ enterprise.name }}</div>
              <div class="enterprise-jobs">{{ enterprise.jobCount }}个职位</div>
              <div class="enterprise-location">{{ enterprise.location }}</div>
            </div>
          </div>
        </div>
        <el-empty
          v-if="!enterpriseLoading && enterprises.length === 0"
          description="暂无企业招聘"
        />
      </div>

      <div class="hot-jobs-section">
        <div class="section-header">
          <h2 class="section-title">热门校招</h2>
          <div class="job-type-tabs">
            <span
              v-for="type in jobTypes"
              :key="type"
              class="job-type-tab"
              :class="{ active: selectedJobType === type }"
              @click="handleJobTypeChange(type)"
            >
              {{ type }}
            </span>
          </div>
        </div>
        <div class="jobs-grid" v-loading="jobsLoading">
          <div v-for="job in jobs" :key="job.id" class="job-card" @click="goToJob(job.id)">
            <div class="job-header">
              <div class="job-title-row">
                <span class="job-title">{{ job.title }}</span>
                <span v-if="job.online" class="online-badge">在线</span>
              </div>
              <div class="salary">{{ job.salary }}</div>
            </div>
            <div class="job-meta">
              <span>{{ job.location }}</span>
              <span class="sep">·</span>
              <span>{{ job.experience }}</span>
              <span class="sep">·</span>
              <span>{{ job.education }}</span>
            </div>
            <div class="job-skills" v-if="job.skills && job.skills.length > 0">
              <span v-for="skill in job.skills" :key="skill" class="skill-tag">{{ skill }}</span>
            </div>
            <div class="company-row">
              <div class="company-logo">{{ getCompanyInitial(job) }}</div>
              <div class="company-info">
                <div class="company-name">{{ getCompanyName(job) }}</div>
                <div class="company-tags">
                  <span class="company-tag">{{ job.industry }}</span>
                  <span class="company-tag">{{ job.stage }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        <el-empty v-if="!jobsLoading && jobs.length === 0" description="暂无热门校招" />

        <div class="pagination-container" v-if="jobTotal > pageSize">
          <el-pagination
            v-model:current-page="currentPage"
            :page-size="pageSize"
            :total="jobTotal"
            layout="prev, pager, next"
            @current-change="handlePageChange"
          />
        </div>
      </div>
    </div>

    <AppFooter />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import NavBar from '@/components/NavBar.vue'
import AppFooter from '@/components/AppFooter.vue'
import { companyApi } from '@/api/company'
import { jobApi } from '@/api/job'
import type { Company, Job } from '@/types'

defineOptions({
  name: 'CampusPage',
})

const router = useRouter()

const searchKeyword = ref('')
const activeTab = ref('campus')
const selectedMajor = ref('')
const selectedJobType = ref('综合')
const currentPage = ref(1)
const pageSize = ref(12)
const jobTotal = ref(0)

const enterpriseLoading = ref(false)
const jobsLoading = ref(false)

const tabs = [
  { label: '校招', value: 'campus' },
  { label: '实习', value: 'intern' },
  { label: '社招', value: 'social' },
  { label: '兼职', value: 'parttime' },
]

const majors = [
  '农学',
  '医学',
  '教育学',
  '文学',
  '艺术学',
  '工学',
  '法学',
  '历史学',
  '哲学',
  '理学',
  '经济学',
  '管理学',
]

const jobTypes = ['综合', '管培生', '教师', '银行', '国企', '外企']

const filters = reactive({
  major: '',
  category: '',
})

const enterprises = ref<Company[]>([])
const jobs = ref<Job[]>([])

const getCompanyName = (job: Job): string => {
  if (typeof job.company === 'string') {
    return job.company
  }
  return ''
}

const getCompanyInitial = (job: Job): string => {
  const name = getCompanyName(job)
  return name ? name.charAt(0) : ''
}

const fetchEnterprises = async (): Promise<void> => {
  try {
    enterpriseLoading.value = true
    const result = await companyApi.getCompanyList({ page: 1, size: 4 })
    enterprises.value = result.content.map((company) => ({
      ...company,
      jobCount: company.jobCount || Math.floor(Math.random() * 50) + 1,
      location: company.location || '南昌',
    }))
  } catch (error) {
    console.error('Failed to fetch enterprises:', error)
  } finally {
    enterpriseLoading.value = false
  }
}

const fetchJobs = async (): Promise<void> => {
  try {
    jobsLoading.value = true
    const params = {
      page: currentPage.value - 1,
      size: pageSize.value,
      keyword: searchKeyword.value || undefined,
      jobType:
        activeTab.value === 'campus' ? '校招' : activeTab.value === 'intern' ? '实习' : undefined,
    }

    const result = await jobApi.getJobList(params)
    jobs.value = result.content.map((job) => ({
      ...job,
      online: Math.random() > 0.7,
      skills: job.skills || [],
      company: job.companyName || '',
      industry: job.industry || '',
      stage: job.stage || '',
    })) as Job[]
    jobTotal.value = result.totalElements
  } catch (error) {
    console.error('Failed to fetch jobs:', error)
    ElMessage.error('获取职位列表失败')
  } finally {
    jobsLoading.value = false
  }
}

const handleTabChange = (tab: string): void => {
  activeTab.value = tab
  currentPage.value = 1
  fetchJobs()
}

const handleSearch = (): void => {
  currentPage.value = 1
  fetchJobs()
}

const handleFilterChange = (): void => {
  currentPage.value = 1
  fetchJobs()
}

const handleMajorClick = (major: string): void => {
  if (selectedMajor.value === major) {
    selectedMajor.value = ''
  } else {
    selectedMajor.value = major
  }
  currentPage.value = 1
  fetchJobs()
}

const handleJobTypeChange = (type: string): void => {
  selectedJobType.value = type
  currentPage.value = 1
  fetchJobs()
}

const handlePageChange = (page: number): void => {
  currentPage.value = page
  fetchJobs()
}

const goToCompany = (companyId: number): void => {
  router.push(`/companies/${companyId}`)
}

const goToJob = (jobId: number): void => {
  router.push(`/jobs/${jobId}`)
}

onMounted(() => {
  fetchEnterprises()
  fetchJobs()
})
</script>

<style scoped lang="scss">
@use '@/assets/styles/variables.scss' as *;

.campus-page {
  min-height: 100vh;
  background: linear-gradient(180deg, rgba($primary-deep, 0.04) 0%, #ffffff 25%);
}

.page-container {
  max-width: 1246px;
  margin: 0 auto;
  padding: 16px 28px;
}

.campus-tabs {
  display: flex;
  gap: 0;
  background: white;
  border-radius: 8px;
  margin-bottom: 16px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.tab-item {
  padding: 14px 32px;
  font-size: 15px;
  color: #666;
  cursor: pointer;
  transition: all 0.2s;
  border-bottom: 2px solid transparent;

  &:hover {
    color: $primary;
    background: rgba($primary, 0.02);
  }

  &.active {
    color: $primary;
    font-weight: 500;
    border-bottom-color: $primary;
    background: rgba($primary, 0.05);
  }
}

.search-filter-bar {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px 20px;
  background: white;
  border-radius: 8px;
  margin-bottom: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.search-input {
  width: 300px;
}

.filter-divider {
  width: 1px;
  height: 24px;
  background: #e0e0e0;
  margin: 0 8px;
}

.filter-group {
  display: flex;
  align-items: center;
  gap: 8px;
}

.filter-label {
  font-size: 14px;
  color: #333;
  font-weight: 500;
  white-space: nowrap;
}

.filter-select {
  width: 120px;
}

.major-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 24px;
}

.major-tag {
  font-size: 14px;
  color: #666;
  padding: 6px 16px;
  background: white;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.2s;

  &:hover {
    color: $primary;
    background: rgba($primary, 0.05);
  }

  &.active {
    color: white;
    background: $primary;
  }
}

.enterprise-section {
  margin-bottom: 24px;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
}

.section-title {
  font-size: 18px;
  color: #222;
  font-weight: 500;
  margin: 0;
}

.view-more {
  font-size: 14px;
  color: $primary;
  text-decoration: none;

  &:hover {
    text-decoration: underline;
  }
}

.enterprise-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  min-height: 120px;
}

.enterprise-card {
  background: white;
  padding: 20px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;

  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }
}

.enterprise-logo {
  width: 56px;
  height: 56px;
  border-radius: 8px;
  background: linear-gradient(135deg, $primary-soft 0%, $primary 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  font-weight: 500;
  color: white;
  margin-bottom: 12px;
}

.enterprise-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.enterprise-name {
  font-size: 15px;
  color: #222;
  font-weight: 500;
}

.enterprise-jobs {
  font-size: 13px;
  color: #ff6b6b;
}

.enterprise-location {
  font-size: 12px;
  color: #999;
}

.hot-jobs-section {
  margin-bottom: 24px;
}

.job-type-tabs {
  display: flex;
  gap: 8px;
}

.job-type-tab {
  font-size: 14px;
  color: #666;
  padding: 4px 12px;
  cursor: pointer;
  border-radius: 4px;
  transition: all 0.2s;

  &:hover {
    color: $primary;
    background: rgba($primary, 0.05);
  }

  &.active {
    color: $primary;
    background: rgba($primary, 0.1);
    font-weight: 500;
  }
}

.jobs-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  min-height: 200px;
}

.job-card {
  background: white;
  padding: 16px 20px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;

  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }
}

.job-header {
  margin-bottom: 8px;
}

.job-title-row {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 4px;
}

.job-title {
  font-size: 15px;
  color: #222;
  font-weight: 500;
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.online-badge {
  font-size: 12px;
  color: #52c41a;
  background: rgba(#52c41a, 0.1);
  padding: 2px 6px;
  border-radius: 2px;
  flex-shrink: 0;
}

.salary {
  font-size: 15px;
  color: #ff6b6b;
  font-weight: 500;
}

.job-meta {
  font-size: 13px;
  color: #666;
  margin-bottom: 8px;

  .sep {
    margin: 0 6px;
    color: #e0e0e0;
  }
}

.job-skills {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-bottom: 12px;
}

.skill-tag {
  font-size: 12px;
  color: #666;
  padding: 2px 8px;
  background: #f5f5f5;
  border-radius: 2px;
}

.company-row {
  display: flex;
  align-items: center;
  gap: 10px;
  padding-top: 12px;
  border-top: 1px solid #f5f5f5;
}

.company-logo {
  width: 36px;
  height: 36px;
  border-radius: 6px;
  background: linear-gradient(135deg, $primary-soft 0%, $primary 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 500;
  color: white;
  flex-shrink: 0;
}

.company-info {
  flex: 1;
  min-width: 0;
}

.company-name {
  font-size: 13px;
  color: #333;
  margin-bottom: 2px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.company-tags {
  display: flex;
  gap: 6px;
}

.company-tag {
  font-size: 12px;
  color: #999;
}

.pagination-container {
  display: flex;
  justify-content: center;
  padding: 24px 0;
}
</style>
