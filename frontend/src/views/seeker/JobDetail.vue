<template>
  <div class="job-detail-page">
    <SeekerNavBar />

    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="10" animated />
    </div>

    <template v-else-if="job">
      <main class="main-content">
        <div class="container">
          <div class="breadcrumb">
            <router-link to="/">首页</router-link>
            <span class="sep">/</span>
            <router-link to="/jobs">职位</router-link>
            <span class="sep">/</span>
            <span class="current">{{ job.title }}</span>
          </div>

          <div class="content-layout">
            <div class="job-main">
              <div class="job-header">
                <div class="job-top-row">
                  <div class="job-info">
                    <h1 class="job-title">{{ job.title }}</h1>
                    <div class="salary">{{ job.salary }}</div>
                  </div>
                  <div class="job-actions">
                    <button class="btn-apply" @click="applyJob">立即投递</button>
                    <el-button class="btn-collect" @click="toggleFavorite">
                      <el-icon>
                        <StarFilled v-if="isFavorited" />
                        <Star v-else />
                      </el-icon>
                      {{ isFavorited ? '取消收藏' : '收藏' }}
                    </el-button>
                  </div>
                </div>
                <div class="job-meta">
                  <span>{{ getLocation(job) }}</span>
                  <span class="divider">·</span>
                  <span>{{ job.experience }}</span>
                  <span class="divider">·</span>
                  <span>{{ job.education }}</span>
                </div>
                <div class="job-tags" v-if="getJobBenefits(job).length">
                  <span class="tag" v-for="benefit in getJobBenefits(job)" :key="benefit">
                    {{ benefit }}
                  </span>
                </div>
              </div>

              <div class="job-section">
                <h2 class="section-title">职位描述</h2>
                <div class="job-description">
                  <p>{{ job.description || '暂无职位描述' }}</p>
                </div>
              </div>

              <div class="job-section">
                <h2 class="section-title">工作地点</h2>
                <div class="location-info">
                  <p>{{ getJobAddress(job) }}</p>
                </div>
              </div>
            </div>

            <aside class="job-sidebar">
              <div class="company-card" v-if="job.company">
                <div class="company-row">
                  <div class="company-logo">{{ getCompanyName(job)?.charAt(0) }}</div>
                  <div class="company-details">
                    <h3 class="company-name">{{ getCompanyName(job) }}</h3>
                    <p class="company-meta">
                      {{ getCompanyIndustry(job) }} · {{ getCompanyScale(job) }}
                    </p>
                  </div>
                  <button class="btn-detail" @click="goToCompany">详情</button>
                </div>
              </div>

              <div class="similar-jobs" v-if="similarJobs.length > 0">
                <h3 class="sidebar-title">相似职位</h3>
                <div class="similar-job-list">
                  <div
                    class="similar-job-item"
                    v-for="similarJob in similarJobs"
                    :key="similarJob.id"
                    @click="goToJob(similarJob.id)"
                  >
                    <div class="similar-job-header">
                      <h4>{{ similarJob.title }}</h4>
                      <span class="salary">{{ similarJob.salary }}</span>
                    </div>
                    <p class="similar-job-meta">
                      {{ getCompanyName(similarJob) }} · {{ getLocation(similarJob) }}
                    </p>
                  </div>
                </div>
              </div>
            </aside>
          </div>
        </div>
      </main>
    </template>

    <el-empty v-else description="职位不存在" />

    <AppFooter />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Star, StarFilled } from '@element-plus/icons-vue'
import SeekerNavBar from '@/components/SeekerNavBar.vue'
import AppFooter from '@/components/AppFooter.vue'
import { jobApi } from '@/api/job'
import { favoriteApi } from '@/api/favorite'
import { applicationApi } from '@/api/application'
import { useUserStore } from '@/store/user'
import type { Job } from '@/types'

defineOptions({
  name: 'JobDetail',
})

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const loading = ref(true)
const job = ref<Job | null>(null)
const isFavorited = ref(false)
const similarJobs = ref([] as Job[])

const getCompanyName = (job: Job): string => {
  if (typeof job.company === 'object' && job.company) {
    return job.company.name || '未知公司'
  }
  return '未知公司'
}

const getCompanyIndustry = (job: Job): string => {
  if (typeof job.company === 'object' && job.company) {
    return job.company.industry || '未知行业'
  }
  return '未知行业'
}

const getCompanyScale = (job: Job): string => {
  if (typeof job.company === 'object' && job.company) {
    return job.company.scale || '未知规模'
  }
  return '未知规模'
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
  return '未知地点'
}

const getJobAddress = (job: Job): string => {
  const parts: string[] = []
  if (job.province) parts.push(job.province)
  if (job.city) parts.push(job.city)
  if (job.district) parts.push(job.district)
  if (job.address) parts.push(job.address)
  return parts.length > 0 ? parts.join(' ') : '暂无地址信息'
}

const fetchJobDetail = async (): Promise<void> => {
  const jobId = Number(route.params.id)
  if (!jobId) {
    loading.value = false
    return
  }

  try {
    loading.value = true
    const jobData = await jobApi.getJobDetail(jobId)
    job.value = jobData

    const status = await favoriteApi.getFavoriteStatus(jobId)
    isFavorited.value = status.favorited

    const similarData = await jobApi.getJobList({ page: 1, size: 5 })
    similarJobs.value = similarData.content.filter((j) => j.id !== jobId).slice(0, 4)
  } catch (error) {
    console.error('Failed to fetch job detail:', error)
    ElMessage.error('获取职位信息失败')
  } finally {
    loading.value = false
  }
}

const goToCompany = (): void => {
  if (job.value && typeof job.value.company === 'object' && job.value.company) {
    router.push(`/companies/${job.value.company.id}`)
  }
}

const goToJob = (jobId: number): void => {
  router.push(`/jobs/${jobId}`)
}

const applyJob = async (): Promise<void> => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  if (!job.value) return

  try {
    await applicationApi.applyJob({
      jobId: job.value.id,
      resumeId: userStore.userInfo?.id || 0,
    })
    ElMessage.success('投递成功')
  } catch (error) {
    console.error('Failed to apply job:', error)
    ElMessage.error('投递失败，请重试')
  }
}

const toggleFavorite = async (): Promise<void> => {
  if (!job.value) return

  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  try {
    if (isFavorited.value) {
      await favoriteApi.removeFavorite(job.value.id)
      isFavorited.value = false
      ElMessage.success('已取消收藏')
    } else {
      await favoriteApi.addFavorite(job.value.id)
      isFavorited.value = true
      ElMessage.success('收藏成功')
    }
  } catch (error) {
    console.error('Failed to toggle favorite:', error)
    ElMessage.error('操作失败，请重试')
  }
}

onMounted(() => {
  fetchJobDetail()
})
</script>

<style scoped lang="scss">
@use '@/assets/styles/variables.scss' as *;

.job-detail-page {
  min-height: 100vh;
  background: linear-gradient(180deg, rgba($primary-deep, 0.04) 0%, #f7f7f7 25%);
}

.loading-container {
  max-width: 1246px;
  margin: 0 auto;
  padding: 40px 24px;
}

.main-content {
  padding: 20px 0;
}

.container {
  max-width: 1246px;
  margin: 0 auto;
  padding: 0 24px;
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

.content-layout {
  display: grid;
  grid-template-columns: 1fr 360px;
  gap: 24px;
}

.job-main {
  background: white;
  border-radius: 8px;
  padding: 24px;
}

.job-header {
  margin-bottom: 24px;
  padding-bottom: 24px;
  border-bottom: 1px solid #f0f0f0;
}

.job-top-row {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
}

.job-info {
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 16px;
}

.job-title {
  font-size: 24px;
  font-weight: 400;
  color: #222;
  margin: 0;
}

.salary {
  font-size: 20px;
  font-weight: 400;
  color: $primary;
}

.job-actions {
  display: flex;
  gap: 12px;
}

.btn-apply {
  font-size: 14px;
  font-weight: 600;
  color: white;
  background: $primary;
  border: none;
  border-radius: 4px;
  padding: 10px 24px;
  cursor: pointer;
  transition: background-color 0.2s;
  height: 36px;
  display: inline-flex;
  align-items: center;
  justify-content: center;

  &:hover {
    background: $primary-deep;
  }
}

.btn-collect {
  background-color: white;
  border: 1px solid $primary;
  color: $primary;
  padding: 10px 24px;
  height: 36px;
  font-size: 14px;
  font-weight: 500;
  border-radius: 4px;
  display: inline-flex;
  align-items: center;
  justify-content: center;

  &:hover {
    background-color: rgba($primary, 0.05);
    border-color: $primary;
    color: $primary;
  }

  &:active {
    background-color: rgba($primary, 0.1);
    border-color: $primary;
    color: $primary;
  }

  .el-icon {
    margin-right: 4px;
  }
}

.job-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #666;
  margin-bottom: 16px;

  .divider {
    color: #dcdfe6;
  }
}

.job-tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.tag {
  font-size: 12px;
  color: #666;
  padding: 4px 12px;
  background: #f5f5f5;
  border-radius: 4px;
}

.job-section {
  margin-bottom: 24px;

  &:last-child {
    margin-bottom: 0;
  }
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #222;
  margin: 0 0 16px 0;
}

.job-description {
  font-size: 14px;
  color: #666;
  line-height: 1.8;

  p {
    margin: 0;
  }
}

.location-info {
  font-size: 14px;
  color: #666;

  p {
    margin: 0;
  }
}

.job-sidebar {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.company-card {
  background: white;
  border-radius: 8px;
  padding: 16px;
}

.company-row {
  display: flex;
  align-items: center;
  gap: 16px;
}

.company-logo {
  width: 48px;
  height: 48px;
  border-radius: 24px;
  background: linear-gradient(135deg, $primary-soft 0%, $primary 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  font-weight: 600;
  color: white;
  flex-shrink: 0;
}

.company-details {
  flex: 1;
}

.company-name {
  font-size: 16px;
  font-weight: 600;
  color: #222;
  margin: 0 0 4px 0;
}

.company-meta {
  font-size: 14px;
  color: #999;
  margin: 0;
}

.btn-detail {
  font-size: 14px;
  font-weight: 500;
  color: $primary;
  background: white;
  border: 1px solid $primary;
  border-radius: 4px;
  padding: 6px 16px;
  cursor: pointer;
  transition: all 0.2s;
  flex-shrink: 0;

  &:hover {
    background: rgba($primary, 0.05);
  }
}

.similar-jobs {
  background: white;
  border-radius: 8px;
  padding: 16px;
}

.sidebar-title {
  font-size: 14px;
  font-weight: 600;
  color: #222;
  margin: 0 0 12px 0;
}

.similar-job-list {
  display: flex;
  flex-direction: column;
  gap: 0;
}

.similar-job-item {
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;

  &:last-child {
    border-bottom: none;
  }

  &:hover {
    opacity: 0.8;
  }
}

.similar-job-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;

  h4 {
    font-size: 14px;
    font-weight: 400;
    color: #222;
    margin: 0;
  }

  .salary {
    font-size: 14px;
    font-weight: 400;
    color: $primary;
  }
}

.similar-job-meta {
  font-size: 12px;
  color: #999;
  margin: 0;
}
</style>
