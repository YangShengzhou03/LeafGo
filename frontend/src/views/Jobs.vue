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
                  <span class="location">{{ job.location }}</span>
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
                <span class="detail-job-title">{{ selectedJob.title }}</span>
                <span class="detail-salary">{{ selectedJob.salary }}</span>
                <div class="detail-actions">
                  <el-button type="primary" @click="communicate">立即沟通</el-button>
                  <el-button @click="toggleFavorite">
                    <el-icon><Star /></el-icon>
                    {{ isFavorited ? '已收藏' : '收藏' }}
                  </el-button>
                </div>
              </div>
              <div class="detail-meta-row">
                <span>{{ selectedJob.location }}</span>
                <span class="sep">|</span>
                <span>{{ selectedJob.experience }}</span>
                <span class="sep">|</span>
                <span>{{ selectedJob.education }}</span>
              </div>
            </div>

            <div class="detail-scroll">
              <div class="detail-section">
                <h2 class="section-title">职位描述</h2>
                <div class="job-description">{{ selectedJob.description }}</div>
              </div>

              <div class="detail-section" v-if="selectedJob.requirements">
                <h2 class="section-title">任职要求</h2>
                <div class="job-requirements">{{ selectedJob.requirements }}</div>
              </div>

              <div class="detail-section" v-if="selectedJob.benefits">
                <h2 class="section-title">福利待遇</h2>
                <div class="benefits-list">
                  <el-tag
                    v-for="benefit in getBenefitsList(selectedJob.benefits)"
                    :key="benefit"
                    size="small"
                    >{{ benefit }}</el-tag
                  >
                </div>
              </div>

              <div class="hr-card" v-if="selectedJob.company">
                <div class="hr-avatar">{{ getCompanyInitial(selectedJob) }}</div>
                <div class="hr-info">
                  <div class="hr-row">
                    <span class="hr-name">{{ getCompanyName(selectedJob) }}</span>
                    <span class="hr-status">{{
                      getCompanyVerified(selectedJob) ? '已认证' : ''
                    }}</span>
                  </div>
                  <div class="hr-company">
                    {{ getCompanyIndustry(selectedJob) }} · {{ getCompanyScale(selectedJob) }}
                  </div>
                </div>
                <el-button text @click="goToCompany(getCompanyId(selectedJob))">查看公司</el-button>
              </div>

              <div class="detail-section">
                <h2 class="section-title">工作地点</h2>
                <div class="address-text">{{ selectedJob.location }}</div>
              </div>
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
import { Star } from '@element-plus/icons-vue'
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

const getCompanyScale = (job: Job): string => {
  if (typeof job.company === 'string') {
    return ''
  }
  if (job.company && typeof job.company === 'object') {
    return job.company.scale
  }
  return ''
}

const getCompanyVerified = (job: Job): boolean => {
  if (typeof job.company === 'string') {
    return false
  }
  if (job.company && typeof job.company === 'object') {
    return job.company.verified
  }
  return false
}

const getCompanyId = (job: Job): number => {
  if (typeof job.company === 'string') {
    return 0
  }
  if (job.company && typeof job.company === 'object') {
    return job.company.id
  }
  return 0
}

const getCompanyInitial = (job: Job): string => {
  const name = getCompanyName(job)
  return name ? name.charAt(0) : ''
}

const getBenefitsList = (benefits: string | string[]): string[] => {
  if (Array.isArray(benefits)) return benefits
  if (typeof benefits === 'string' && benefits) return benefits.split(',').map((b) => b.trim())
  return []
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

const goToCompany = (companyId: number): void => {
  if (companyId > 0) {
    router.push(`/companies/${companyId}`)
  }
}

onMounted(() => {
  fetchJobs()
})
</script>

<style scoped lang="scss">
@use '@/assets/styles/variables.scss' as *;

.jobs-page {
  min-height: 100vh;
  background: linear-gradient(180deg, rgba($primary-deep, 0.04) 0%, #ffffff 25%);
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
  gap: 16px;
  margin-bottom: 8px;
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
  margin-right: auto;
}

.detail-actions {
  display: flex;
  gap: 8px;
  flex-shrink: 0;

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
  margin-bottom: 24px;

  .section-title {
    font-size: 16px;
    color: $ink;
    font-weight: 500;
    margin: 0 0 12px 0;
  }

  .job-description,
  .job-requirements,
  .address-text {
    font-size: 14px;
    color: $body;
    line-height: 1.8;
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
  margin-bottom: 24px;
}

.hr-avatar {
  width: 48px;
  height: 48px;
  border-radius: 8px;
  background-color: $primary-soft;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  color: $on-primary;
}

.hr-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
  flex: 1;
}

.hr-row {
  display: flex;
  align-items: center;
  gap: 12px;
}

.hr-name {
  font-size: 14px;
  color: $ink;
}

.hr-status {
  font-size: 12px;
  color: $primary;
}

.hr-company {
  font-size: 13px;
  color: $body;
}
</style>
