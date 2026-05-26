<template>
  <div class="jobs-page">
    <NavBar />

    <div class="page-container">
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
          <span class="filter-label">薪资待遇</span>
          <el-select
            v-model="filters.salary"
            placeholder="不限"
            class="filter-select"
            @change="handleFilterChange"
          >
            <el-option label="不限" value="" />
            <el-option label="3-5K" value="3-5K" />
            <el-option label="5-10K" value="5-10K" />
            <el-option label="10-20K" value="10-20K" />
            <el-option label="20K以上" value="20K+" />
          </el-select>
        </div>
        <div class="filter-group">
          <span class="filter-label">工作经验</span>
          <el-select
            v-model="filters.experience"
            placeholder="不限"
            class="filter-select"
            @change="handleFilterChange"
          >
            <el-option label="不限" value="" />
            <el-option label="在校/应届" value="在校/应届" />
            <el-option label="1-3年" value="1-3年" />
            <el-option label="3-5年" value="3-5年" />
            <el-option label="5-10年" value="5-10年" />
          </el-select>
        </div>
        <div class="filter-group">
          <span class="filter-label">学历要求</span>
          <el-select
            v-model="filters.education"
            placeholder="不限"
            class="filter-select"
            @change="handleFilterChange"
          >
            <el-option label="不限" value="" />
            <el-option label="大专" value="大专" />
            <el-option label="本科" value="本科" />
            <el-option label="硕士" value="硕士" />
            <el-option label="博士" value="博士" />
          </el-select>
        </div>
        <el-button text @click="clearFilters">清空筛选</el-button>
      </div>

      <div class="main-content" v-loading="loading">
        <div class="jobs-list">
          <div v-if="jobs.length === 0 && !loading" class="empty-state">
            <el-empty description="暂无符合条件的职位" />
          </div>
          <template v-else>
            <template v-if="loading">
              <el-skeleton v-for="i in 6" :key="i" :rows="3" animated />
            </template>
            <template v-else>
              <div
                v-for="job in jobs"
                :key="job.id"
                class="job-card"
                :class="{ active: selectedJob?.id === job.id }"
                @click="selectJob(job)"
              >
                <div class="job-title-row">
                  <span class="job-title">{{ job.title }}</span>
                  <span class="salary">{{ job.salary }}</span>
                </div>
                <div class="job-meta-row">
                  <span class="experience">{{ job.experience }}</span>
                  <span class="education">{{ job.education }}</span>
                </div>
                <div class="company-row">
                  <span class="company-name">{{ getCompanyName(job) }}</span>
                  <span class="industry">{{ getCompanyIndustry(job) }}</span>
                  <span class="location">{{ getLocation(job) }}</span>
                </div>
              </div>
            </template>
          </template>

          <div class="pagination-container" v-if="total > pageSize">
            <el-pagination
              v-model:current-page="currentPage"
              :page-size="pageSize"
              :total="total"
              layout="prev, pager, next"
              @current-change="handlePageChange"
            />
          </div>
        </div>

        <div class="job-detail">
          <div v-if="selectedJob" class="detail-content">
            <div class="detail-header">
              <div class="detail-title-row">
                <div class="title-left">
                  <span class="detail-job-title">{{ selectedJob.title }}</span>
                  <span class="detail-salary">{{ selectedJob.salary }}</span>
                </div>
                <div class="title-right">
                  <el-button type="primary" class="communicate-btn" @click="communicate">
                    立即沟通
                  </el-button>
                  <el-button class="favorite-btn" @click="toggleFavorite">
                    {{ isFavorited ? '已收藏' : '收藏' }}
                  </el-button>
                </div>
              </div>
              <div class="detail-meta-row">
                <span>{{ getLocation(selectedJob) }}</span>
                <span class="sep">|</span>
                <span>{{ selectedJob.experience }}</span>
                <span class="sep">|</span>
                <span>{{ selectedJob.education }}</span>
              </div>
            </div>

            <div class="detail-scroll">
              <div class="detail-section" v-if="selectedJob.description">
                <h2 class="section-title">职位描述</h2>
                <div class="job-description">{{ selectedJob.description }}</div>
              </div>

              <div class="hr-card" v-if="selectedJob.company">
                <div class="hr-avatar">{{ getCompanyInitial(selectedJob) }}</div>
                <div class="hr-info">
                  <span class="hr-name">龚先生</span>
                  <span class="hr-status">在线</span>
                  <div class="hr-company">{{ getCompanyName(selectedJob) }} · 招聘专员</div>
                </div>
              </div>

              <div class="detail-section">
                <h2 class="section-title">工作地点</h2>
                <div class="address-text">{{ getFullAddress(selectedJob) }}</div>
              </div>
            </div>

            <div class="detail-footer">
              <el-button class="view-more-btn" @click="goToJobDetail(selectedJob.id)">
                查看更多信息
              </el-button>
            </div>
          </div>
          <el-empty v-else description="请选择职位查看详情" />
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
import { jobApi } from '@/api/job'
import type { Job } from '@/types'

defineOptions({
  name: 'JobsList',
})

const router = useRouter()

const loading = ref(false)
const searchKeyword = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const filters = reactive({
  salary: '',
  experience: '',
  education: '',
})

const selectedJob = ref<Job | null>(null)
const isFavorited = ref(false)
const jobs = ref<Job[]>([])

const getCompanyName = (job: Job): string => {
  if (typeof job.company === 'string') {
    return job.company
  }
  if (job.company && typeof job.company === 'object') {
    return job.company.name
  }
  return '未知公司'
}

const getCompanyIndustry = (job: Job): string => {
  if (typeof job.company === 'string') {
    return ''
  }
  if (job.company && typeof job.company === 'object') {
    return job.company.industry
  }
  return ''
}

const getCompanyInitial = (job: Job): string => {
  const name = getCompanyName(job)
  return name ? name.charAt(0) : ''
}

const fetchJobs = async (): Promise<void> => {
  try {
    loading.value = true
    const params = {
      page: currentPage.value,
      size: pageSize.value,
      keyword: searchKeyword.value || undefined,
      salary: filters.salary || undefined,
      experience: filters.experience || undefined,
      education: filters.education || undefined,
    }

    const result = await jobApi.getJobList(params)
    jobs.value = result.content
    total.value = result.totalElements

    if (jobs.value.length > 0 && !selectedJob.value) {
      selectedJob.value = jobs.value[0]
    }
  } catch (error) {
    console.error('Failed to fetch jobs:', error)
    ElMessage.error('获取职位列表失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = (): void => {
  currentPage.value = 1
  fetchJobs()
}

const handleFilterChange = (): void => {
  currentPage.value = 1
  fetchJobs()
}

const handlePageChange = (page: number): void => {
  currentPage.value = page
  fetchJobs()
}

const clearFilters = (): void => {
  filters.salary = ''
  filters.experience = ''
  filters.education = ''
  searchKeyword.value = ''
  currentPage.value = 1
  fetchJobs()
}

const selectJob = (job: Job): void => {
  selectedJob.value = job
  isFavorited.value = false
}

const communicate = (): void => {
  ElMessage.info('请登录后与HR沟通')
  router.push('/login')
}

const toggleFavorite = (): void => {
  isFavorited.value = !isFavorited.value
  ElMessage.success(isFavorited.value ? '收藏成功' : '已取消收藏')
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

const getFullAddress = (job: Job): string => {
  const parts: string[] = []
  if (job.province) parts.push(job.province)
  if (job.city && job.city !== job.province) parts.push(job.city)
  if (job.district) parts.push(job.district)
  if (job.address) parts.push(job.address)
  if (parts.length > 0) {
    return parts.join(' ')
  }
  if (typeof job.company === 'object' && job.company) {
    const c = job.company
    const companyParts: string[] = []
    if (c.province) companyParts.push(c.province)
    if (c.city && c.city !== c.province) companyParts.push(c.city)
    if (c.district) companyParts.push(c.district)
    if (c.address) companyParts.push(c.address)
    if (companyParts.length > 0) {
      return companyParts.join(' ')
    }
  }
  return ''
}

const goToJobDetail = (jobId: number): void => {
  router.push(`/jobs/${jobId}`)
}

onMounted(() => {
  fetchJobs()
})
</script>

<style scoped lang="scss">
@use '@/assets/styles/variables.scss' as *;

.jobs-page {
  min-height: 100vh;
  background: linear-gradient(180deg, rgba($primary-deep, 0.04) 0%, #f7f7f7 25%);
}

.page-container {
  max-width: 1246px;
  margin: 0 auto;
  padding: 16px 28px;
}

.search-filter-bar {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background-color: $canvas;
  border-radius: $rounded-lg;
  margin-bottom: 16px;

  .search-input {
    width: 300px;
  }

  .el-button--primary {
    background-color: $primary;
    border-color: $primary;

    &:hover {
      background-color: $primary-soft;
      border-color: $primary-soft;
    }

    &:active {
      background-color: $primary-deep;
      border-color: $primary-deep;
    }
  }

  .filter-divider {
    width: 1px;
    height: 24px;
    background-color: $hairline;
    margin: 0 8px;
  }
}

.filter-group {
  display: flex;
  align-items: center;
  gap: 8px;
}

.filter-label {
  font-size: 14px;
  color: $body;
}

.filter-select {
  width: 100px;
}

.main-content {
  display: flex;
  gap: 16px;
  min-height: 600px;
}

.jobs-list {
  width: 340px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.empty-state {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

.job-card {
  background-color: $canvas;
  border: 2px solid $hairline;
  border-radius: $rounded-lg;
  padding: 14px 16px;
  cursor: pointer;
  transition: all $transition-fast;

  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  }

  &.active {
    border-color: $primary;

    .job-title {
      color: $primary;
    }
  }
}

.job-title-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 8px;
}

.job-title {
  font-size: 16px;
  color: $ink;
  font-weight: 400;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  flex: 1;
}

.salary {
  font-size: 16px;
  color: #ff6b6b;
  font-weight: 500;
}

.job-meta-row {
  display: flex;
  gap: 8px;
  margin-bottom: 8px;

  span {
    font-size: 12px;
    color: $body;
    padding: 2px 8px;
    background-color: #f5f5f5;
    border-radius: $rounded-sm;
  }
}

.company-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 13px;
  color: $mute;
  gap: 8px;

  .company-name,
  .industry {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    flex-shrink: 1;
  }

  .location {
    margin-left: auto;
    flex-shrink: 0;
  }
}

.pagination-container {
  padding: 16px 0;
  display: flex;
  justify-content: center;
}

.job-detail {
  flex: 1;
  border-radius: $rounded-lg;
  padding: 16px;
  display: flex;
  flex-direction: column;
  position: sticky;
  top: 80px;
  height: calc(100vh - 150px);
  max-height: 800px;
}

.detail-content {
  display: flex;
  flex-direction: column;
  flex: 1;
  min-height: 0;
}

.detail-header {
  flex-shrink: 0;
  padding-bottom: 16px;
  border-bottom: 1px solid $hairline;
}

.detail-title-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 8px;
}

.title-left {
  display: flex;
  align-items: baseline;
  gap: 16px;
}

.title-right {
  display: flex;
  gap: 8px;
}

.detail-job-title {
  font-size: 20px;
  color: $ink;
  font-weight: 500;
}

.detail-salary {
  font-size: 20px;
  color: #ff6b6b;
  font-weight: 500;
}

.detail-meta-row {
  font-size: 14px;
  color: $body;

  .sep {
    margin: 0 8px;
    color: $hairline;
  }
}

.detail-scroll {
  flex: 1;
  overflow-y: auto;
  padding-top: 16px;
}

.detail-section {
  margin-bottom: 20px;

  .section-title {
    font-size: 16px;
    color: $ink;
    font-weight: 500;
    margin: 0 0 8px 0;
  }

  .address-text {
    font-size: 14px;
    color: $body;
    line-height: 1.8;
  }

  .job-description {
    font-size: 14px;
    color: $body;
    line-height: 1.8;
    white-space: pre-wrap;
  }
}

.benefits-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.hr-card {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background-color: #f8f8f8;
  border-radius: $rounded-lg;
  margin-bottom: 20px;
}

.hr-avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background-color: $primary-soft;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  color: $on-primary;
  flex-shrink: 0;
}

.hr-info {
  display: flex;
  align-items: baseline;
  gap: 8px;
  flex: 1;
  flex-wrap: wrap;
}

.hr-name {
  font-size: 14px;
  color: $ink;
  font-weight: 500;
}

.hr-status {
  font-size: 12px;
  color: $primary;
  padding: 2px 8px;
  background-color: rgba($primary, 0.1);
  border-radius: 12px;
}

.hr-company {
  font-size: 13px;
  color: $body;
  width: 100%;
}

.detail-footer {
  display: flex;
  justify-content: center;
  padding-top: 16px;
  border-top: 1px solid $hairline;
  margin-top: auto;

  .view-more-btn {
    padding: 10px 28px;
    font-size: 14px;
    font-weight: 500;
    border-radius: 4px;
    background: white;
    border: 1px solid $primary;
    color: $primary;
    transition: all 0.2s ease;

    &:hover {
      background: rgba($primary, 0.05);
    }
  }
}
</style>
