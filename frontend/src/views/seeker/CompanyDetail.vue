<template>
  <div class="company-detail-page">
    <SeekerNavBar />

    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="10" animated />
    </div>

    <template v-else-if="company">
      <div class="page-container">
        <div class="breadcrumb">
          <router-link to="/">首页</router-link>
          <span class="sep">/</span>
          <router-link to="/companies">公司</router-link>
          <span class="sep">/</span>
          <span class="current">{{ company.name }}</span>
        </div>

        <div class="company-header">
          <div class="company-logo">{{ company.name?.charAt(0) }}</div>
          <div class="company-info">
            <div class="company-row">
              <div class="company-name-wrapper">
                <h1 class="company-name">{{ company.name }}</h1>
                <button
                  class="favorite-btn"
                  :class="{ favorited: isCompanyFavorited }"
                  @click="toggleCompanyFavorite"
                >
                  <el-icon>
                    <StarFilled v-if="isCompanyFavorited" />
                    <Star v-else />
                  </el-icon>
                  {{ isCompanyFavorited ? '取消收藏' : '收藏' }}
                </button>
              </div>
              <div class="company-stats">
                <span class="stat-item">
                  <span class="stat-number">{{ jobTotal }}</span>
                  <span class="stat-label">在招职位</span>
                </span>
                <span class="stat-divider">|</span>
                <span class="stat-item">
                  <span class="stat-number">{{ bossCount }}</span>
                  <span class="stat-label">位BOSS</span>
                </span>
              </div>
            </div>
            <div class="company-row">
              <div class="company-tags">
                <el-tag v-if="company.verified" type="success">已认证</el-tag>
                <el-tag>{{ company.scale }}</el-tag>
                <el-tag type="info">{{ company.industry }}</el-tag>
              </div>
              <div class="company-actions">
                <span class="action-link" @click="goToResume">完善在线简历</span>
                <span class="action-divider">|</span>
                <span class="action-link" @click="goToResume">新增附件简历</span>
              </div>
            </div>
          </div>
        </div>

        <div class="main-content">
          <div class="left-section">
            <div class="section-card">
              <div class="section-tabs">
                <div
                  class="tab-item"
                  :class="{ active: activeSection === 'jobs' }"
                  @click="activeSection = 'jobs'"
                >
                  招聘职位({{ jobTotal }})
                </div>
                <div
                  class="tab-item"
                  :class="{ active: activeSection === 'about' }"
                  @click="activeSection = 'about'"
                >
                  公司简介
                </div>
              </div>

              <template v-if="activeSection === 'jobs'">
                <div class="hot-jobs" v-if="hotJobs.length > 0">
                  <h3>热招职位</h3>
                  <div class="job-list">
                    <div
                      class="job-item"
                      v-for="job in hotJobs"
                      :key="job.id"
                      @click="goToJob(job.id)"
                    >
                      <div class="job-header-row">
                        <div class="job-title">{{ job.title }}</div>
                        <div class="job-salary">{{ job.salary }}</div>
                      </div>
                      <div class="job-meta">
                        <span>{{ job.experience }}</span>
                        <span class="sep">·</span>
                        <span>{{ job.education }}</span>
                        <span class="sep">·</span>
                        <span>{{ getLocation(job) }}</span>
                      </div>
                    </div>
                  </div>
                </div>

                <div class="all-jobs">
                  <div class="jobs-header">
                    <h3>全部职位</h3>
                    <span class="view-all-link" @click="showAllJobs = !showAllJobs">
                      {{ showAllJobs ? '收起' : `查看全部${jobTotal}个职位` }}
                    </span>
                  </div>
                  <div class="jobs-list">
                    <div
                      class="job-card"
                      v-for="job in displayedJobs"
                      :key="job.id"
                      @click="goToJob(job.id)"
                    >
                      <div class="job-main">
                        <div class="job-header-row">
                          <h4>{{ job.title }}</h4>
                          <span class="salary">{{ job.salary }}</span>
                        </div>
                        <div class="job-meta-row">
                          <span class="job-location">{{ getLocation(job) }}</span>
                          <span class="experience">{{ job.experience }}</span>
                          <span class="education">{{ job.education }}</span>
                        </div>
                        <div class="job-benefits" v-if="getJobBenefits(job).length">
                          <el-tag
                            v-for="benefit in getJobBenefits(job).slice(0, 3)"
                            :key="benefit"
                            type="info"
                          >
                            {{ benefit }}
                          </el-tag>
                        </div>
                      </div>
                      <div class="job-action">
                        <el-button type="primary" @click.stop="communicate(job)"
                          >立即沟通</el-button
                        >
                      </div>
                    </div>
                  </div>
                </div>
              </template>

              <template v-else>
                <div class="company-description">
                  <p>{{ company.description || '暂无公司简介' }}</p>
                </div>
              </template>
            </div>

            <div class="section-card">
              <h2>公司相册</h2>
              <div class="photo-grid">
                <div class="photo-item" v-for="i in 6" :key="i">
                  <el-image :src="`https://picsum.photos/200/150?random=${i}`" fit="cover" />
                </div>
              </div>
            </div>

            <div class="section-card">
              <h2>工商信息</h2>
              <div class="business-info">
                <div class="info-row">
                  <span class="label">企业名称：</span>
                  <span class="value">{{ company.name }}</span>
                </div>
                <div class="info-row">
                  <span class="label">成立时间：</span>
                  <span class="value">{{ formatDate(company.createdAt) }}</span>
                </div>
                <div class="info-row">
                  <span class="label">企业类型：</span>
                  <span class="value">有限责任公司</span>
                </div>
                <div class="info-row">
                  <span class="label">经营状态：</span>
                  <span class="value">存续</span>
                </div>
                <div class="info-row">
                  <span class="label">行业类型：</span>
                  <span class="value">{{ company.industry }}</span>
                </div>
              </div>
            </div>

            <div class="section-card">
              <h2>公司地址</h2>
              <div class="address-info">
                <div class="address-item">
                  <el-icon>
                    <Location />
                  </el-icon>
                  <span>{{ getCompanyAddress(company) }}</span>
                </div>
              </div>
            </div>
          </div>

          <div class="right-section">
            <div class="sidebar-card">
              <h3>工作时间及福利</h3>
              <div class="work-time">
                <p>{{ companyWorkTime }}</p>
              </div>
              <div class="benefits" v-if="companyBenefits.length > 0">
                <el-tag v-for="benefit in companyBenefits" :key="benefit">
                  {{ benefit }}
                </el-tag>
              </div>
              <el-empty v-else description="暂无福利信息" :image-size="60" />
            </div>

            <div class="sidebar-card">
              <h3>{{ company.name }}招聘负责人</h3>
              <div class="boss-list" v-if="bossList.length > 0">
                <div
                  class="boss-item"
                  v-for="boss in bossList"
                  :key="boss.id"
                  @click="communicateWithBoss(boss)"
                >
                  <el-avatar :size="48">{{ boss.name?.charAt(0) }}</el-avatar>
                  <div class="boss-info-detail">
                    <div class="boss-first-line">
                      <span class="boss-name-lg">{{ boss.name }}</span>
                      <span class="boss-position-lg">{{ boss.position }}</span>
                    </div>
                    <div class="boss-hiring">正在招聘 "{{ boss.hiring }}" 等职位</div>
                  </div>
                </div>
              </div>
              <el-empty v-else description="暂无招聘负责人信息" :image-size="80" />
            </div>
          </div>
        </div>
      </div>
    </template>

    <el-empty v-else description="公司不存在" />

    <AppFooter />
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Location, Star, StarFilled } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import SeekerNavBar from '@/components/SeekerNavBar.vue'
import AppFooter from '@/components/AppFooter.vue'
import { companyApi } from '@/api/company'
import { useUserStore } from '@/store/user'
import type { Company, Job, Boss } from '@/types'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const loading = ref(true)
const company = ref<Company | null>(null)
const jobs = ref([] as Job[])
const jobTotal = ref(0)
const activeSection = ref('jobs')
const showAllJobs = ref(false)
const isCompanyFavorited = ref(false)

const companyBenefits = computed(() => {
  if (!company.value?.benefits) return []
  try {
    return JSON.parse(company.value.benefits)
  } catch {
    return []
  }
})

const companyWorkTime = computed(() => {
  return company.value?.workTime || '暂无工作时间信息'
})

const bossList = computed(() => {
  if (!company.value?.bossInfo) return []
  try {
    return JSON.parse(company.value.bossInfo)
  } catch {
    return []
  }
})

const bossCount = computed(() => bossList.value.length)

const getCompanyAddress = (company: Company): string => {
  const parts: string[] = []
  if (company.province) parts.push(company.province)
  if (company.city) parts.push(company.city)
  if (company.district) parts.push(company.district)
  if (company.address) parts.push(company.address)
  return parts.length > 0 ? parts.join(' ') : '暂无地址信息'
}

const hotJobs = computed(() => jobs.value.slice(0, 3))

const displayedJobs = computed(() => {
  return showAllJobs.value ? jobs.value : jobs.value.slice(0, 10)
})

const fetchCompanyDetail = async (): Promise<void> => {
  const companyId = Number(route.params.id)
  if (!companyId) {
    loading.value = false
    return
  }

  try {
    loading.value = true
    const [companyData, jobsData] = await Promise.all([
      companyApi.getCompanyDetail(companyId),
      companyApi.getCompanyJobs(companyId, { page: 1, size: 100 }),
    ])

    company.value = companyData
    jobs.value = jobsData.content
    jobTotal.value = jobsData.totalElements
  } catch (error) {
    console.error('Failed to fetch company detail:', error)
    ElMessage.error('获取公司信息失败')
  } finally {
    loading.value = false
  }
}

const goToJob = (jobId: number): void => {
  router.push(`/jobs/${jobId}`)
}

const goToResume = (): void => {
  router.push('/login')
}

const toggleCompanyFavorite = async (): Promise<void> => {
  if (!company.value) return

  try {
    if (isCompanyFavorited.value) {
      isCompanyFavorited.value = false
      ElMessage.success('已取消收藏')
    } else {
      isCompanyFavorited.value = true
      ElMessage.success('收藏成功')
    }
  } catch (error) {
    console.error('Failed to toggle company favorite:', error)
    ElMessage.error('操作失败')
  }
}

const communicate = (_job: Job): void => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  router.push('/messages')
}

const communicateWithBoss = (boss: Boss): void => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录后再与招聘负责人沟通')
    router.push('/login')
    return
  }
  ElMessage.success(`正在与�?${boss.name} 建立对话`)
  router.push('/messages')
}

const formatDate = (dateStr: string): string => {
  if (!dateStr) return '-'
  return new Date(dateStr).toLocaleDateString('zh-CN')
}

const getJobBenefits = (job: Job): string[] => {
  if (!job.benefits) return []
  if (Array.isArray(job.benefits)) return job.benefits
  if (typeof job.benefits === 'string') {
    return job.benefits.split(',').filter((b) => b.trim())
  }
  return []
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

onMounted(() => {
  fetchCompanyDetail()
})
</script>

<style scoped lang="scss">
@use '@/assets/styles/variables.scss' as *;

.company-detail-page {
  min-height: 100vh;
  background: #f5f5f5;
}

.loading-container {
  max-width: 1246px;
  margin: 0 auto;
  padding: 40px 24px;
}

.page-container {
  max-width: 1246px;
  margin: 0 auto;
  padding: 20px 24px;
}

.breadcrumb {
  font-size: 14px;
  color: #999;
  margin-bottom: 16px;

  a {
    color: #666;
    text-decoration: none;

    &:hover {
      color: $primary;
    }
  }

  .sep {
    margin: 0 8px;
  }

  .current {
    color: #222;
  }
}

.company-header {
  display: flex;
  align-items: center;
  gap: 16px;
  background: white;
  padding: 24px;
  border-radius: 8px;
  margin-bottom: 16px;
}

.company-logo {
  width: 80px;
  height: 80px;
  border-radius: 8px;
  background: linear-gradient(135deg, $primary-soft 0%, $primary 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32px;
  font-weight: 500;
  color: white;
  flex-shrink: 0;
}

.company-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.company-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

.company-name-wrapper {
  display: flex;
  align-items: center;
  gap: 12px;
}

.company-name {
  font-size: 24px;
  font-weight: 600;
  color: #222;
  margin: 0;
}

.favorite-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  padding: 6px 12px;
  border: 1px solid $primary;
  border-radius: 8px;
  background: white;
  color: $primary;
  cursor: pointer;
  transition: all 0.2s;
  font-size: 14px;

  &:hover {
    background: rgba($primary, 0.05);
  }

  &.favorited {
    background: $primary;
    color: white;

    &:hover {
      background: $primary-deep;
    }
  }

  .el-icon {
    font-size: 16px;
  }
}

.company-stats {
  display: flex;
  align-items: center;
  gap: 16px;
  font-size: 14px;
}

.company-tags {
  display: flex;
  gap: 8px;
}

.company-actions {
  display: flex;
  align-items: center;
  gap: 8px;
}

.action-link {
  font-size: 14px;
  color: $primary;
  cursor: pointer;
  transition: opacity 0.2s;

  &:hover {
    opacity: 0.8;
  }
}

.action-divider {
  color: #dcdfe6;
}

.stat-item {
  display: inline-flex;
  align-items: baseline;
  gap: 4px;
}

.stat-number {
  font-size: 24px;
  font-weight: 600;
  color: $primary;
}

.stat-label {
  font-size: 13px;
  color: #666;
}

.stat-divider {
  color: #dcdfe6;
  margin: 0 4px;
}

.main-content {
  display: flex;
  gap: 16px;
}

.left-section {
  flex: 1;
  min-width: 0;
}

.right-section {
  width: 320px;
  flex-shrink: 0;
}

.section-card {
  background: white;
  padding: 24px;
  border-radius: 8px;
  margin-bottom: 16px;

  h2 {
    font-size: 18px;
    font-weight: 600;
    color: #222;
    margin: 0 0 16px 0;
  }

  h3 {
    font-size: 16px;
    font-weight: 500;
    color: #222;
    margin: 0 0 12px 0;
  }
}

.section-tabs {
  display: flex;
  gap: 0;
  border-bottom: 2px solid #f0f0f0;
  margin-bottom: 20px;

  .tab-item {
    padding: 12px 24px;
    font-size: 15px;
    color: #666;
    cursor: pointer;
    position: relative;
    transition: all 0.2s;

    &:hover {
      color: $primary;
    }

    &.active {
      color: $primary;
      font-weight: 500;

      &::after {
        content: '';
        position: absolute;
        bottom: -2px;
        left: 0;
        right: 0;
        height: 2px;
        background: $primary;
      }
    }
  }
}

.hot-jobs {
  margin-bottom: 24px;

  h3 {
    font-size: 16px;
    color: #222;
    margin-bottom: 12px;
  }
}

.job-list {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}

.job-item {
  padding: 16px;
  background: #fafafa;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;

  &:hover {
    background: #f0f0f0;
  }

  .job-header-row {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    margin-bottom: 8px;
    gap: 16px;
  }

  .job-title {
    font-size: 14px;
    font-weight: 500;
    color: #222;
  }

  .job-salary {
    font-size: 16px;
    color: $primary;
  }

  .job-meta {
    font-size: 12px;
    color: #999;

    .sep {
      margin: 0 4px;
    }
  }
}

.company-description {
  p {
    font-size: 14px;
    color: #666;
    line-height: 1.8;
    margin: 0;
  }
}

.all-jobs {
  .jobs-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
  }

  .view-all-link {
    font-size: 14px;
    color: #999;
    cursor: pointer;
    transition: color 0.2s;

    &:hover {
      color: $primary;
    }
  }
}

.jobs-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.job-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  background: #fafafa;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;

  &:hover {
    background: #f0f0f0;
  }

  .job-main {
    flex: 1;
  }

  .job-header-row {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    margin-bottom: 8px;
    gap: 16px;

    h4 {
      font-size: 16px;
      font-weight: 500;
      color: #222;
      margin: 0;
    }

    .salary {
      font-size: 16px;
      color: $primary;
    }
  }

  .job-meta-row {
    display: flex;
    align-items: center;
    gap: 16px;
    margin-bottom: 8px;

    .job-location {
      font-size: 14px;
      color: #999;
    }

    .experience,
    .education {
      font-size: 14px;
      color: #666;
    }
  }

  .job-benefits {
    display: flex;
    gap: 8px;
  }
}

.photo-grid {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 8px;
}

.photo-item {
  aspect-ratio: 4/3;
  border-radius: 4px;
  overflow: hidden;

  .el-image {
    width: 100%;
    height: 100%;
  }
}

.business-info {
  margin-top: 16px;
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 0;
}

.info-row {
  display: flex;
  padding: 8px 0;
  border-bottom: 1px solid #f0f0f0;

  &:nth-last-child(-n + 2) {
    border-bottom: none;
  }

  .label {
    width: 80px;
    flex-shrink: 0;
    font-size: 14px;
    color: #999;
  }

  .value {
    flex: 1;
    font-size: 14px;
    color: #222;
  }
}

.address-info {
  .address-item {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 14px;
    color: #666;

    .el-icon {
      color: $primary;
    }
  }
}

.sidebar-card {
  background: white;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 16px;

  h3 {
    font-size: 16px;
    font-weight: 500;
    color: #222;
    margin: 0 0 16px 0;
  }
}

.work-time {
  margin-bottom: 16px;

  p {
    font-size: 14px;
    color: #666;
    margin: 0 0 4px;
  }
}

.benefits {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.boss-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.boss-item {
  display: flex;
  gap: 12px;
  padding: 12px;
  background: #fafafa;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;

  &:hover {
    background: #f0f0f0;
  }
}

.boss-info-detail {
  flex: 1;
  min-width: 0;
}

.boss-first-line {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 4px;
}

.boss-name-lg {
  font-size: 14px;
  font-weight: 500;
  color: #222;
}

.boss-position-lg {
  font-size: 12px;
  color: #666;
}

.boss-hiring {
  font-size: 12px;
  color: #999;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
