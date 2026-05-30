<template>
  <div class="profile-page">
    <SeekerNavBar />
    <main class="main-content">
      <div class="container">
        <div class="profile-header">
          <div class="user-info-card">
            <el-avatar :size="80" class="user-avatar">{{
              userStore.userInfo?.username?.charAt(0) || 'U'
            }}</el-avatar>
            <div class="user-details">
              <h1 class="username">{{ userStore.userInfo?.username || '用户' }}</h1>
              <p class="user-basic-info" v-if="resume.birthday">
                {{ calculateAge(resume.birthday) }}岁·{{ resume.jobStatus || '26年应届生本科' }}
              </p>
              <p class="user-expectation" v-if="resume.jobIntention">
                期望：{{ resume.jobIntention }}·{{ resume.expectedSalary || '6-10K' }}
              </p>
              <p class="user-education" v-if="resume.education && resume.education.length > 0">
                {{ resume.education[0].school }}·{{ resume.education[0].major
                }}{{ resume.education[0].startDate }}-{{ resume.education[0].endDate }}
              </p>
              <p class="user-work" v-if="resume.workExperience && resume.workExperience.length > 0">
                {{ resume.workExperience[0].company }}·{{ resume.workExperience[0].position
                }}{{ resume.workExperience[0].startDate }}-{{
                  resume.workExperience[0].endDate || '至今'
                }}
              </p>
            </div>
          </div>

          <div class="user-stats">
            <div class="stat-item">
              <span class="stat-label">在线简历</span>
            </div>
            <div class="stat-item">
              <span class="stat-value">{{ stats.communicated }}</span>
              <span class="stat-label">沟通过</span>
            </div>
            <div class="stat-item">
              <span class="stat-value">{{ stats.applied }}</span>
              <span class="stat-label">已投递</span>
            </div>
            <div class="stat-item">
              <span class="stat-value">{{ stats.interview }}</span>
              <span class="stat-label">面试</span>
            </div>
            <div class="stat-item">
              <span class="stat-value">{{ stats.interested }}</span>
              <span class="stat-label">感兴趣</span>
            </div>
            <div class="stat-item">
              <span class="stat-value">{{ stats.records }}</span>
              <span class="stat-label">网申记录</span>
            </div>
          </div>
        </div>

        <div class="profile-menu">
          <div
            class="menu-item"
            :class="{ active: activeMenu === 'favorites' }"
            @click="activeMenu = 'favorites'"
          >
            <el-icon><Star /></el-icon>
            <span>我的收藏</span>
          </div>
          <div
            class="menu-item"
            :class="{ active: activeMenu === 'applications' }"
            @click="activeMenu = 'applications'"
          >
            <el-icon><Document /></el-icon>
            <span>投递记录</span>
          </div>
        </div>

        <div class="profile-content">
          <div class="profile-section" v-if="activeMenu === 'favorites'">
            <h2>我的收藏</h2>
            <div class="favorites-list" v-if="favorites.length > 0">
              <div class="favorite-item" v-for="job in favorites" :key="job.id">
                <div class="job-info">
                  <h4>{{ job.title }}</h4>
                  <p>{{ job.company }} · {{ job.location }}</p>
                </div>
                <div class="job-salary">{{ job.salary }}</div>
              </div>
            </div>
            <el-empty v-else description="暂无收藏" />
          </div>

          <div class="profile-section" v-else-if="activeMenu === 'applications'">
            <h2>投递记录</h2>
            <div class="applications-list" v-if="applications.length > 0">
              <div class="application-item" v-for="app in applications" :key="app.id">
                <div class="job-info">
                  <h4>{{ app.jobTitle }}</h4>
                  <p>{{ app.company }} · {{ app.time }}</p>
                </div>
                <el-tag :type="getStatusType(app.status)">{{ app.status }}</el-tag>
              </div>
            </div>
            <el-empty v-else description="暂无投递记录" />
          </div>
        </div>
      </div>
    </main>
    <AppFooter />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Star, Document } from '@element-plus/icons-vue'
import SeekerNavBar from '@/components/SeekerNavBar.vue'
import AppFooter from '@/components/AppFooter.vue'
import { useUserStore } from '@/store/user'
import { favoriteApi } from '@/api/favorite'
import { applicationApi } from '@/api/application'
import { resumeApi } from '@/api/resume'

defineOptions({
  name: 'ProfilePage',
})

const userStore = useUserStore()

const activeMenu = ref('favorites')

const resume = ref({
  birthday: '',
  jobStatus: '',
  jobIntention: '',
  expectedSalary: '',
  education: [] as Array<{ school: string; major: string; startDate: string; endDate: string }>,
  workExperience: [] as Array<{
    company: string
    position: string
    startDate: string
    endDate?: string
  }>,
})

const stats = ref({
  communicated: 0,
  applied: 0,
  interview: 0,
  interested: 0,
  records: 0,
})

interface FavoriteJob {
  id: number
  title: string
  company: string
  location: string
  salary: string
}

interface Application {
  id: number
  jobTitle: string
  company: string
  time: string
  status: string
}

const favorites = ref([] as FavoriteJob[])
const applications = ref([] as Application[])

const fetchResume = async (): Promise<void> => {
  if (!userStore.userInfo?.id) return

  try {
    const data = await resumeApi.getResume(userStore.userInfo.id)
    resume.value = {
      birthday: (data as unknown as { birthday?: string }).birthday || '',
      jobStatus: '',
      jobIntention: data.jobIntention || '',
      expectedSalary: data.expectedSalary || '',
      education: parseJsonArray(data.education),
      workExperience: parseJsonArray(data.workExperience),
    }
  } catch (error) {
    console.error('Failed to fetch resume:', error)
  }
}

const parseJsonArray = <T,>(data: T[] | string | undefined): T[] => {
  if (!data) return []
  if (Array.isArray(data)) return data
  try {
    const parsed = JSON.parse(data)
    return Array.isArray(parsed) ? parsed : []
  } catch {
    return []
  }
}

const fetchStats = async (): Promise<void> => {
  try {
    const [favoritesData, applicationsData] = await Promise.all([
      favoriteApi.getMyFavorites(),
      applicationApi.getMyApplications(),
    ])

    stats.value = {
      communicated: 0,
      applied: applicationsData.totalElements || 0,
      interview: 0,
      interested: favoritesData.length || 0,
      records: applicationsData.totalElements || 0,
    }
  } catch (error) {
    console.error('Failed to fetch stats:', error)
  }
}

const fetchFavorites = async (): Promise<void> => {
  try {
    const data = await favoriteApi.getMyFavorites()
    favorites.value = data.map((item) => ({
      id: item.job?.id || item.jobId,
      title: item.job?.title || '未知职位',
      company:
        typeof item.job?.company === 'object' && item.job?.company
          ? item.job.company.name
          : '未知公司',
      location: item.job?.city || '未知地点',
      salary: item.job?.salary || '面议',
    }))
  } catch (error) {
    console.error('Failed to fetch favorites:', error)
  }
}

const fetchApplications = async (): Promise<void> => {
  try {
    const data = await applicationApi.getMyApplications()
    applications.value = (data.content || []).map((item) => ({
      id: item.id,
      jobTitle: item.job?.title || '未知职位',
      company:
        typeof item.job?.company === 'object' && item.job?.company
          ? item.job.company.name
          : '未知公司',
      time: new Date(item.createdAt).toLocaleDateString(),
      status: getStatusText(item.status),
    }))
  } catch (error) {
    console.error('Failed to fetch applications:', error)
  }
}

const getStatusText = (status: string): string => {
  switch (status) {
    case 'PENDING':
      return '待处理'
    case 'VIEWED':
      return '已查看'
    case 'ACCEPTED':
      return '已通过'
    case 'REJECTED':
      return '已拒绝'
    default:
      return status
  }
}

const calculateAge = (birthday: string): number => {
  const birth = new Date(birthday)
  const now = new Date()
  let age = now.getFullYear() - birth.getFullYear()
  const monthDiff = now.getMonth() - birth.getMonth()
  if (monthDiff < 0 || (monthDiff === 0 && now.getDate() < birth.getDate())) {
    age--
  }
  return age
}

onMounted(() => {
  fetchResume()
  fetchStats()
  fetchFavorites()
  fetchApplications()
})

const getStatusType = (status: string): 'success' | 'warning' | 'danger' | 'info' => {
  switch (status) {
    case '已查看':
      return 'success'
    case '待处理':
      return 'warning'
    case '已拒绝':
      return 'danger'
    default:
      return 'info'
  }
}
</script>

<style scoped lang="scss">
@use '@/assets/styles/variables.scss' as *;

.profile-page {
  min-height: 100vh;
  background: linear-gradient(180deg, rgba($primary-deep, 0.04) 0%, #f5f5f5 25%);
}

.main-content {
  padding: 20px 0;
}

.container {
  max-width: 1246px;
  margin: 0 auto;
  padding: 0 24px;
}

.profile-header {
  background: white;
  border-radius: 8px;
  padding: 24px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);

  .user-info-card {
    display: flex;
    gap: 20px;
    margin-bottom: 20px;
    padding-bottom: 20px;
    border-bottom: 1px solid #f0f0f0;

    .user-avatar {
      background: linear-gradient(135deg, $primary-soft 0%, $primary 100%);
      color: white;
      flex-shrink: 0;
    }

    .user-details {
      flex: 1;

      .username {
        font-size: 24px;
        font-weight: 600;
        color: #222;
        margin: 0 0 8px 0;
      }

      p {
        font-size: 14px;
        color: #666;
        margin: 4px 0;
        line-height: 1.6;
      }

      .user-basic-info,
      .user-expectation {
        color: #999;
      }
    }
  }

  .user-stats {
    display: flex;
    gap: 32px;

    .stat-item {
      display: flex;
      align-items: center;
      gap: 4px;

      .stat-value {
        font-size: 18px;
        font-weight: 600;
        color: $primary;
      }

      .stat-label {
        font-size: 14px;
        color: #666;
      }
    }
  }
}

.profile-menu {
  background: white;
  border-radius: 8px;
  padding: 16px 24px;
  margin-bottom: 20px;
  display: flex;
  gap: 32px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);

  .menu-item {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 8px 16px;
    border-radius: 8px;
    cursor: pointer;
    transition: all 0.2s;
    font-size: 14px;
    color: #666;

    &:hover {
      background: #f5f5f5;
      color: #222;
    }

    &.active {
      background: rgba($primary, 0.1);
      color: $primary;
      font-weight: 500;
    }
  }
}

.profile-content {
  background: white;
  border-radius: 8px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.profile-section {
  h2 {
    font-size: 18px;
    font-weight: 600;
    color: #222;
    margin: 0 0 24px 0;
    padding-bottom: 12px;
    border-bottom: 1px solid #f0f0f0;
  }
}

.favorites-list,
.applications-list {
  display: flex;
  flex-direction: column;
  gap: 0;
}

.favorite-item,
.application-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #f0f0f0;

  &:last-child {
    border-bottom: none;
  }
}

.job-info {
  flex: 1;

  h4 {
    font-size: 16px;
    font-weight: 500;
    color: #222;
    margin: 0 0 4px 0;
  }

  p {
    font-size: 14px;
    color: #999;
    margin: 0;
  }
}

.job-salary {
  font-size: 16px;
  font-weight: 600;
  color: $primary;
}
</style>
