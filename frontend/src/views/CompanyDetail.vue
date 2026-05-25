<template>
  <div class="company-detail-page">
    <NavBar />

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
            <h1 class="company-name">{{ company.name }}</h1>
            <div class="company-tags">
              <el-tag v-if="company.verified" type="success" size="small">已认证</el-tag>
              <el-tag size="small">{{ company.scale }}</el-tag>
              <el-tag type="info" size="small">{{ company.industry }}</el-tag>
            </div>
          </div>
          <el-button :type="isFavorited ? 'primary' : 'default'" @click="toggleFavorite">
            <el-icon><Star /></el-icon>
            {{ isFavorited ? '已收藏' : '收藏' }}
          </el-button>
        </div>

        <div class="stats-bar">
          <div class="stat-item">
            <span class="stat-number">{{ jobTotal }}</span>
            <span class="stat-label">在招职位</span>
          </div>
          <div class="stat-item">
            <span class="stat-number">{{ bossCount }}</span>
            <span class="stat-label">位BOSS</span>
          </div>
          <div class="stat-actions">
            <el-button @click="goToResume">完善在线简历</el-button>
            <el-button @click="goToResume">新增附件简历</el-button>
          </div>
        </div>

        <div class="main-content">
          <div class="left-section">
            <div class="section-card">
              <div class="section-tabs">
                <el-radio-group v-model="activeSection" size="small">
                  <el-radio-button label="jobs">招聘职位({{ jobTotal }})</el-radio-button>
                  <el-radio-button label="about">公司简介</el-radio-button>
                </el-radio-group>
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
                      <div class="job-salary">{{ job.salary }}</div>
                      <div class="job-title">{{ job.title }}</div>
                      <div class="job-meta">
                        <span>{{ job.experience }}</span>
                        <span class="sep">·</span>
                        <span>{{ job.education }}</span>
                        <span class="sep">·</span>
                        <span>{{ job.location }}</span>
                      </div>
                    </div>
                  </div>
                </div>

                <div class="all-jobs">
                  <div class="jobs-header">
                    <h3>全部职位</h3>
                    <el-button text @click="showAllJobs = !showAllJobs">
                      {{ showAllJobs ? '收起' : `查看全部${jobTotal}个职位` }}
                    </el-button>
                  </div>
                  <div class="jobs-list">
                    <div
                      class="job-card"
                      v-for="job in displayedJobs"
                      :key="job.id"
                      @click="goToJob(job.id)"
                    >
                      <div class="job-main">
                        <div class="job-header">
                          <h4>{{ job.title }}</h4>
                          <span class="job-location">[{{ job.location }}]</span>
                        </div>
                        <div class="job-info">
                          <span class="salary">{{ job.salary }}</span>
                          <span class="experience">{{ job.experience }}</span>
                          <span class="education">{{ job.education }}</span>
                        </div>
                        <div class="job-benefits" v-if="job.benefits?.length">
                          <el-tag
                            v-for="benefit in job.benefits.slice(0, 3)"
                            :key="benefit"
                            size="small"
                            type="info"
                          >
                            {{ benefit }}
                          </el-tag>
                        </div>
                      </div>
                      <div class="job-action">
                        <el-button type="primary" size="small" @click.stop="communicate(job)"
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

            <div class="section-card" v-if="company.description">
              <h2>公司简介</h2>
              <div class="description-content">
                <p :class="{ expanded: descExpanded }">{{ company.description }}</p>
                <el-button
                  v-if="company.description.length > 200"
                  text
                  @click="descExpanded = !descExpanded"
                >
                  {{ descExpanded ? '收起' : '展开' }}
                </el-button>
              </div>
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
              <el-button text @click="showBusinessInfo = !showBusinessInfo">
                {{ showBusinessInfo ? '收起' : '查看更多信息' }}
              </el-button>
              <div class="business-info" v-show="showBusinessInfo">
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
                  <el-icon><Location /></el-icon>
                  <span>{{ company.location || '暂无地址信息' }}</span>
                </div>
              </div>
            </div>
          </div>

          <div class="right-section">
            <div class="sidebar-card">
              <h3>工作时间及福利</h3>
              <div class="work-time">
                <p>上午08:30 - 下午06:00</p>
                <p>偶尔加班</p>
              </div>
              <div class="benefits">
                <el-tag v-for="benefit in defaultBenefits" :key="benefit" size="small">
                  {{ benefit }}
                </el-tag>
              </div>
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
                    <div class="boss-name-lg">{{ boss.name }}</div>
                    <div class="boss-position-lg">{{ boss.position }}</div>
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
import { Star, Location } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import NavBar from '@/components/NavBar.vue'
import AppFooter from '@/components/AppFooter.vue'
import { companyApi } from '@/api/company'
import type { Company, Job, Boss } from '@/types'

const route = useRoute()
const router = useRouter()

const loading = ref(true)
const company = ref<Company | null>(null)
const jobs = ref<Job[]>([])
const jobTotal = ref(0)
const isFavorited = ref(false)
const activeSection = ref('jobs')
const showAllJobs = ref(false)
const descExpanded = ref(false)
const showBusinessInfo = ref(false)

const defaultBenefits = ['餐补', '全勤奖', '夜班补助', '加班补助', '定期体检', '五险一金']

const bossList = ref<Boss[]>([
  { id: 1, name: '陈加辉', position: '物流经理', hiring: '物流工程师' },
  { id: 2, name: '吕小伟', position: '生产主管', hiring: '生产组长/拉长' },
  { id: 3, name: '赖先生', position: '招聘经理', hiring: '人力资源专员/助理' },
  { id: 4, name: '徐女士', position: '招聘主管', hiring: '华勤直招小时工/暑假工/短期工' },
])

const bossCount = computed(() => bossList.value.length)

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

const toggleFavorite = (): void => {
  isFavorited.value = !isFavorited.value
  ElMessage.success(isFavorited.value ? '收藏成功' : '已取消收藏')
}

const goToJob = (jobId: number): void => {
  router.push(`/jobs/${jobId}`)
}

const goToResume = (): void => {
  router.push('/seeker/resume')
}

const communicate = (_job: Job): void => {
  ElMessage.info('请登录后与HR沟通')
  router.push('/login')
}

const communicateWithBoss = (_boss: Boss): void => {
  ElMessage.info('请登录后与招聘负责人沟通')
  router.push('/login')
}

const formatDate = (dateStr: string): string => {
  if (!dateStr) return '-'
  return new Date(dateStr).toLocaleDateString('zh-CN')
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
}

.company-name {
  font-size: 24px;
  font-weight: 600;
  color: #222;
  margin: 0 0 12px 0;
}

.company-tags {
  display: flex;
  gap: 8px;
}

.stats-bar {
  display: flex;
  align-items: center;
  gap: 32px;
  background: white;
  padding: 16px 24px;
  border-radius: 8px;
  margin-bottom: 16px;
}

.stat-item {
  display: flex;
  align-items: baseline;
  gap: 8px;
}

.stat-number {
  font-size: 24px;
  font-weight: 600;
  color: $primary;
}

.stat-label {
  font-size: 14px;
  color: #999;
}

.stat-actions {
  margin-left: auto;
  display: flex;
  gap: 12px;
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
  margin-bottom: 20px;
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

  .job-salary {
    font-size: 16px;
    font-weight: 600;
    color: $primary;
    margin-bottom: 8px;
  }

  .job-title {
    font-size: 14px;
    color: #222;
    margin-bottom: 8px;
  }

  .job-meta {
    font-size: 12px;
    color: #999;

    .sep {
      margin: 0 4px;
    }
  }
}

.all-jobs {
  .jobs-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
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

  .job-header {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 8px;

    h4 {
      font-size: 16px;
      font-weight: 500;
      color: #222;
      margin: 0;
    }

    .job-location {
      font-size: 14px;
      color: #999;
    }
  }

  .job-info {
    display: flex;
    align-items: center;
    gap: 16px;
    margin-bottom: 8px;

    .salary {
      font-size: 16px;
      font-weight: 600;
      color: $primary;
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

.description-content {
  p {
    font-size: 14px;
    color: #666;
    line-height: 1.8;
    margin: 0;

    &.expanded {
      display: -webkit-box;
      -webkit-line-clamp: unset;
    }

    &:not(.expanded) {
      display: -webkit-box;
      -webkit-line-clamp: 4;
      line-clamp: 4;
      -webkit-box-orient: vertical;
      overflow: hidden;
    }
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
}

.info-row {
  display: flex;
  padding: 8px 0;
  border-bottom: 1px solid #f0f0f0;

  &:last-child {
    border-bottom: none;
  }

  .label {
    width: 120px;
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

.boss-name-lg {
  font-size: 14px;
  font-weight: 500;
  color: #222;
  margin-bottom: 4px;
}

.boss-position-lg {
  font-size: 12px;
  color: #666;
  margin-bottom: 4px;
}

.boss-hiring {
  font-size: 12px;
  color: #999;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
