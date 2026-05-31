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
            <div class="user-info-right">
              <h1 class="username">{{ userStore.userInfo?.username || '用户' }}</h1>
              <div class="user-stats">
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
          </div>
        </div>

        <div class="profile-content">
          <el-tabs v-model="activeTab" type="card">
            <el-tab-pane label="收藏岗位" name="favoriteJobs">
              <el-table
                :data="favoriteJobs"
                style="width: 100%"
                :header-cell-style="{ background: '#fafafa', color: '#606266' }"
              >
                <el-table-column prop="title" label="职位名称" min-width="180" />
                <el-table-column prop="company" label="公司" width="180" />
                <el-table-column prop="location" label="地点" width="120" />
                <el-table-column prop="salary" label="薪资" width="120" />
                <el-table-column label="操作" width="60">
                  <template #default="{ row }">
                    <span class="link-text" @click="viewJob(row.id)">查看</span>
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>

            <el-tab-pane label="收藏公司" name="favoriteCompanies">
              <el-table
                :data="favoriteCompanies"
                style="width: 100%"
                :header-cell-style="{ background: '#fafafa', color: '#606266' }"
              >
                <el-table-column prop="name" label="公司名称" min-width="200" />
                <el-table-column prop="industry" label="行业" width="150" />
                <el-table-column prop="scale" label="规模" width="120" />
                <el-table-column label="操作" width="60">
                  <template #default="{ row }">
                    <span class="link-text" @click="viewCompany(row.id)">查看</span>
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>

            <el-tab-pane label="投递记录" name="applications">
              <el-table
                :data="applications"
                style="width: 100%"
                :header-cell-style="{ background: '#fafafa', color: '#606266' }"
              >
                <el-table-column prop="jobTitle" label="职位名称" min-width="180" />
                <el-table-column prop="company" label="公司" width="180" />
                <el-table-column prop="time" label="投递时间" width="120" />
                <el-table-column prop="status" label="状态" width="100">
                  <template #default="{ row }">
                    <el-tag :type="getStatusType(row.status)">{{ row.status }}</el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="60">
                  <template #default="{ row }">
                    <span class="link-text" @click="viewJob(row.jobId)">查看</span>
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>

            <el-tab-pane label="沟通过" name="communicated">
              <el-table
                :data="communicatedJobs"
                style="width: 100%"
                :header-cell-style="{ background: '#fafafa', color: '#606266' }"
              >
                <el-table-column prop="jobTitle" label="职位名称" min-width="180" />
                <el-table-column prop="company" label="公司" width="180" />
                <el-table-column prop="lastMessage" label="最新消息" min-width="200" />
                <el-table-column prop="time" label="时间" width="120" />
              </el-table>
            </el-tab-pane>

            <el-tab-pane label="已投递" name="submitted">
              <el-table
                :data="submittedJobs"
                style="width: 100%"
                :header-cell-style="{ background: '#fafafa', color: '#606266' }"
              >
                <el-table-column prop="jobTitle" label="职位名称" min-width="180" />
                <el-table-column prop="company" label="公司" width="180" />
                <el-table-column prop="time" label="投递时间" width="120" />
                <el-table-column label="操作" width="80">
                  <template #default="{ row }">
                    <span class="link-text" @click="viewJob(row.jobId)">查看</span>
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>

            <el-tab-pane label="面试" name="interview">
              <el-table
                :data="interviewJobs"
                style="width: 100%"
                :header-cell-style="{ background: '#fafafa', color: '#606266' }"
              >
                <el-table-column prop="jobTitle" label="职位名称" min-width="180" />
                <el-table-column prop="company" label="公司" width="180" />
                <el-table-column prop="time" label="面试时间" width="120" />
                <el-table-column label="操作" width="80">
                  <template #default="{ row }">
                    <span class="link-text" @click="viewJob(row.jobId)">查看</span>
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>

            <el-tab-pane label="网申记录" name="onlineRecords">
              <el-table
                :data="onlineRecords"
                style="width: 100%"
                :header-cell-style="{ background: '#fafafa', color: '#606266' }"
              >
                <el-table-column prop="jobTitle" label="职位名称" min-width="180" />
                <el-table-column prop="company" label="公司" width="180" />
                <el-table-column prop="platform" label="平台" width="120" />
                <el-table-column prop="time" label="申请时间" width="120" />
                <el-table-column prop="status" label="状态" width="100">
                  <template #default="{ row }">
                    <el-tag :type="getStatusType(row.status)">{{ row.status }}</el-tag>
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>
    </main>
    <AppFooter />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import SeekerNavBar from '@/components/SeekerNavBar.vue'
import AppFooter from '@/components/AppFooter.vue'
import { useUserStore } from '@/store/user'
import { favoriteApi } from '@/api/favorite'
import { applicationApi } from '@/api/application'
import { resumeApi } from '@/api/resume'

defineOptions({
  name: 'ProfilePage',
})

const router = useRouter()
const userStore = useUserStore()

const activeTab = ref('favoriteJobs')

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

interface FavoriteCompany {
  id: number
  name: string
  industry: string
  scale: string
}

interface Application {
  id: number
  jobId: number
  jobTitle: string
  company: string
  time: string
  status: string
}

interface CommunicatedJob {
  id: number
  jobId: number
  jobTitle: string
  company: string
  lastMessage: string
  time: string
}

interface OnlineRecord {
  id: number
  jobTitle: string
  company: string
  platform: string
  time: string
  status: string
}

const favoriteJobs = ref([] as FavoriteJob[])
const favoriteCompanies = ref([] as FavoriteCompany[])
const applications = ref([] as Application[])
const communicatedJobs = ref([] as CommunicatedJob[])
const submittedJobs = ref([] as Application[])
const interviewJobs = ref([] as Application[])
const onlineRecords = ref([] as OnlineRecord[])

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
    favoriteJobs.value = data.map((item) => ({
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
    const allApplications = (data.content || []).map((item) => ({
      id: item.id,
      jobId: item.job?.id || 0,
      jobTitle: item.job?.title || '未知职位',
      company:
        typeof item.job?.company === 'object' && item.job?.company
          ? item.job.company.name
          : '未知公司',
      time: new Date(item.createdAt).toLocaleDateString(),
      status: getStatusText(item.status),
    }))

    applications.value = allApplications
    submittedJobs.value = allApplications.filter((app) => app.status === '待处理')
    interviewJobs.value = allApplications.filter((app) => app.status === '已通过')
    onlineRecords.value = allApplications.map((app) => ({
      ...app,
      platform: '本平台',
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

const viewJob = (jobId: number): void => {
  router.push(`/jobs/${jobId}`)
}

const viewCompany = (companyId: number): void => {
  router.push(`/companies/${companyId}`)
}

onMounted(() => {
  fetchResume()
  fetchStats()
  fetchFavorites()
  fetchApplications()
})
</script>

<style scoped lang="scss">
.profile-page {
  min-height: 100vh;
  background: #f5f7fa;
}

.main-content {
  padding: 24px 0;
}

.container {
  max-width: 1200px;
  min-height: 50vh;
  margin: 0 auto;
  padding: 0 24px;
}

.profile-header {
  background: white;
  border-radius: 8px;
  padding: 24px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.user-info-card {
  display: flex;
  gap: 20px;
  align-items: center;
}

.user-avatar {
  background: #00b85c;
  color: white;
  flex-shrink: 0;
}

.user-info-right {
  flex: 1;
}

.username {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 16px 0;
}

.user-stats {
  display: flex;
  gap: 32px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.stat-value {
  font-size: 20px;
  font-weight: 600;
  color: #00b85c;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.profile-content {
  background: white;
  border-radius: 8px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.link-text {
  color: #00b85c;
  cursor: pointer;
  font-size: 14px;

  &:hover {
    color: #85ce61;
    text-decoration: underline;
  }
}
</style>
