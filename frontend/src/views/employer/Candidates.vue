<template>
  <div class="employer-candidates">
    <EmployerNavBar />
    <main class="main-content">
      <div class="container">
        <el-row :gutter="16" class="stats-row">
          <el-col :span="6">
            <div class="stat-card total">
              <div class="stat-icon">
                <el-icon size="28"><User /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ stats.total }}</div>
                <div class="stat-label">候选人总数</div>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card pending">
              <div class="stat-icon">
                <el-icon size="28"><Clock /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ stats.pending }}</div>
                <div class="stat-label">待查看</div>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card interview">
              <div class="stat-icon">
                <el-icon size="28"><Calendar /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ stats.interview }}</div>
                <div class="stat-label">面试邀请</div>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card rejected">
              <div class="stat-icon">
                <el-icon size="28"><Close /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ stats.rejected }}</div>
                <div class="stat-label">已拒绝</div>
              </div>
            </div>
          </el-col>
        </el-row>

        <div class="filter-bar">
          <div class="filter-left">
            <span class="filter-label">状态筛选：</span>
            <el-radio-group v-model="statusFilter" @change="handleFilter" size="default">
              <el-radio-button label="">全部</el-radio-button>
              <el-radio-button label="PENDING">待查看</el-radio-button>
              <el-radio-button label="VIEWED">已查看</el-radio-button>
              <el-radio-button label="INTERVIEW">面试邀请</el-radio-button>
              <el-radio-button label="REJECTED">已拒绝</el-radio-button>
            </el-radio-group>
          </div>
          <div class="filter-right">
            <el-input
              v-model="searchKeyword"
              placeholder="搜索候选人姓名或职位"
              prefix-icon="Search"
              clearable
              class="search-input"
              @input="handleSearch"
            />
          </div>
        </div>

        <div class="table-wrapper">
          <el-table
            :data="filteredCandidates"
            style="width: 100%"
            v-loading="loading"
            :header-cell-style="{ background: '#fafafa', color: '#606266' }"
          >
            <el-table-column prop="user.username" label="姓名" width="120">
              <template #default="{ row }">
                <div class="candidate-name">
                  <el-avatar :size="32" class="avatar">{{ row.user?.username?.charAt(0) || '用' }}</el-avatar>
                  <span>{{ row.user?.username || '未知' }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="job.title" label="应聘职位" min-width="180">
              <template #default="{ row }">
                <div class="job-info">
                  <div class="job-title">{{ row.job?.title || '未知职位' }}</div>
                  <div class="job-company">{{ row.job?.companyName || '' }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="resume.phone" label="联系方式" width="150">
              <template #default="{ row }">
                <div class="contact-info">
                  <div class="phone">{{ row.resume?.phone || '-' }}</div>
                  <div class="email">{{ row.resume?.email || '-' }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="110" align="center">
              <template #default="{ row }">
                <el-tag :type="getStatusType(row.status)" size="default" effect="light">
                  {{ getStatusText(row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createdAt" label="投递时间" width="160" align="center">
              <template #default="{ row }">
                <span class="time-text">{{ formatTime(row.createdAt) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" fixed="right" width="200" align="center">
              <template #default="{ row }">
                <div class="action-buttons">
                  <el-button type="primary" size="small" @click="viewResume(row)">
                    <el-icon><View /></el-icon>
                    查看简历
                  </el-button>
                  <el-dropdown @command="(cmd: string) => handleCommand(cmd, row)" trigger="click">
                    <el-button size="small">
                      更多<el-icon class="el-icon--right"><ArrowDown /></el-icon>
                    </el-button>
                    <template #dropdown>
                      <el-dropdown-menu>
                        <el-dropdown-item
                          command="interview"
                          v-if="row.status === 'PENDING' || row.status === 'VIEWED'"
                        >
                          <el-icon><Calendar /></el-icon>邀请面试
                        </el-dropdown-item>
                        <el-dropdown-item
                          command="reject"
                          v-if="row.status !== 'REJECTED'"
                        >
                          <el-icon><Close /></el-icon>拒绝
                        </el-dropdown-item>
                      </el-dropdown-menu>
                    </template>
                  </el-dropdown>
                </div>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div class="pagination-wrapper">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :total="total"
            :page-sizes="[10, 20, 50]"
            layout="total, sizes, prev, pager, next, jumper"
            @current-change="handlePageChange"
            @size-change="handleSizeChange"
          />
        </div>

        <el-dialog
          v-model="resumeDialogVisible"
          :title="`${resumeData?.name || '候选人'} 的简历`"
          width="800px"
          class="resume-dialog"
        >
          <div v-if="resumeLoading" v-loading="resumeLoading" style="height: 200px" />
          <div v-else-if="resumeData" class="resume-detail">
            <div class="resume-header">
              <el-avatar :size="64" class="resume-avatar">{{ resumeData.name?.charAt(0) || '用' }}</el-avatar>
              <div class="resume-basic">
                <h3>{{ resumeData.name }}</h3>
                <div class="basic-info">
                  <span><el-icon><Phone /></el-icon> {{ resumeData.phone }}</span>
                  <span><el-icon><Message /></el-icon> {{ resumeData.email }}</span>
                  <span v-if="resumeData.location"><el-icon><Location /></el-icon> {{ resumeData.location }}</span>
                </div>
              </div>
            </div>

            <div class="resume-section" v-if="resumeData.selfIntroduction">
              <h4><el-icon><User /></el-icon> 自我介绍</h4>
              <p class="resume-text">{{ resumeData.selfIntroduction }}</p>
            </div>

            <div class="resume-section" v-if="parsedEducation.length > 0">
              <h4><el-icon><Reading /></el-icon> 教育经历</h4>
              <el-timeline>
                <el-timeline-item
                  v-for="(edu, index) in parsedEducation"
                  :key="index"
                  :timestamp="`${edu.startDate} - ${edu.endDate}`"
                  placement="top"
                >
                  <div class="timeline-content">
                    <div class="timeline-title">{{ edu.school }}</div>
                    <div class="timeline-desc">{{ edu.major }} · {{ edu.degree }}</div>
                  </div>
                </el-timeline-item>
              </el-timeline>
            </div>

            <div class="resume-section" v-if="parsedWorkExperience.length > 0">
              <h4><el-icon><Briefcase /></el-icon> 工作经历</h4>
              <el-timeline>
                <el-timeline-item
                  v-for="(work, index) in parsedWorkExperience"
                  :key="index"
                  :timestamp="`${work.startDate} - ${work.endDate || '至今'}`"
                  placement="top"
                >
                  <div class="timeline-content">
                    <div class="timeline-title">{{ work.company }} · {{ work.position }}</div>
                    <div class="timeline-desc">{{ work.description }}</div>
                  </div>
                </el-timeline-item>
              </el-timeline>
            </div>

            <div class="resume-section" v-if="parsedSkills.length > 0">
              <h4><el-icon><Medal /></el-icon> 技能特长</h4>
              <div class="skills-list">
                <el-tag
                  v-for="(skill, index) in parsedSkills"
                  :key="index"
                  type="success"
                  size="large"
                  effect="light"
                >
                  {{ skill.name }} · {{ skill.level }}
                </el-tag>
              </div>
            </div>
          </div>
          <div v-else>
            <el-empty description="暂无简历信息" />
          </div>
        </el-dialog>
      </div>
    </main>
    <AppFooter />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import {
  User,
  Clock,
  Calendar,
  Close,
  View,
  ArrowDown,
  Phone,
  Message,
  Location,
  Reading,
  Briefcase,
  Medal,
} from '@element-plus/icons-vue'
import EmployerNavBar from '@/components/EmployerNavBar.vue'
import AppFooter from '@/components/AppFooter.vue'
import { applicationApi, resumeApi } from '@/api'
import type { Application, Resume, Education, WorkExperience, Skill } from '@/types'
import dayjs from 'dayjs'

defineOptions({
  name: 'EmployerCandidates',
})

const loading = ref(false)
const candidates = ref<Application[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const statusFilter = ref('')
const searchKeyword = ref('')

const stats = reactive({
  total: 0,
  pending: 0,
  interview: 0,
  rejected: 0,
})

const resumeDialogVisible = ref(false)
const resumeLoading = ref(false)
const resumeData = ref<Resume | null>(null)

const filteredCandidates = computed<Application[]>(() => {
  if (!searchKeyword.value) return candidates.value
  const keyword = searchKeyword.value.toLowerCase()
  return candidates.value.filter((c) => {
    const name = c.user?.username?.toLowerCase() || ''
    const title = c.job?.title?.toLowerCase() || ''
    return name.includes(keyword) || title.includes(keyword)
  })
})

const parsedEducation = computed<Education[]>(() => {
  if (!resumeData.value?.education) return []
  try {
    return typeof resumeData.value.education === 'string'
      ? JSON.parse(resumeData.value.education)
      : resumeData.value.education
  } catch {
    return []
  }
})

const parsedWorkExperience = computed<WorkExperience[]>(() => {
  if (!resumeData.value?.workExperience) return []
  try {
    return typeof resumeData.value.workExperience === 'string'
      ? JSON.parse(resumeData.value.workExperience)
      : resumeData.value.workExperience
  } catch {
    return []
  }
})

const parsedSkills = computed<Skill[]>(() => {
  if (!resumeData.value?.skills) return []
  try {
    return typeof resumeData.value.skills === 'string'
      ? JSON.parse(resumeData.value.skills)
      : resumeData.value.skills
  } catch {
    return []
  }
})

const fetchCandidates = async (): Promise<void> => {
  loading.value = true
  try {
    const data = await applicationApi.getReceivedApplications({
      page: currentPage.value,
      size: pageSize.value,
      status: statusFilter.value,
    })
    candidates.value = data.content
    total.value = data.totalElements
    stats.total = data.totalElements
    stats.pending = candidates.value.filter((c) => c.status === 'PENDING').length
    stats.interview = candidates.value.filter((c) => c.status === 'INTERVIEW').length
    stats.rejected = candidates.value.filter((c) => c.status === 'REJECTED').length
  } finally {
    loading.value = false
  }
}

const handleSearch = (): void => {
  // Search is handled by computed property
}

const handleFilter = (): void => {
  currentPage.value = 1
  fetchCandidates()
}

const handlePageChange = (page: number): void => {
  currentPage.value = page
  fetchCandidates()
}

const handleSizeChange = (size: number): void => {
  pageSize.value = size
  currentPage.value = 1
  fetchCandidates()
}

const viewResume = async (app: Application): Promise<void> => {
  resumeDialogVisible.value = true
  resumeLoading.value = true
  resumeData.value = null
  try {
    const resume = await resumeApi.getResume(app.userId)
    resumeData.value = resume
    if (app.status === 'PENDING') {
      await applicationApi.updateApplicationStatus(app.id, 'VIEWED')
      fetchCandidates()
    }
  } catch {
    ElMessage.error('获取简历失败')
  } finally {
    resumeLoading.value = false
  }
}

const handleCommand = (command: string, app: Application): void => {
  if (command === 'interview') {
    updateStatus(app, 'INTERVIEW')
  } else if (command === 'reject') {
    updateStatus(app, 'REJECTED')
  }
}

const updateStatus = async (app: Application, status: string): Promise<void> => {
  await applicationApi.updateApplicationStatus(app.id, status as Application['status'])
  ElMessage.success('状态更新成功')
  fetchCandidates()
}

type TagType = 'primary' | 'success' | 'warning' | 'info' | 'danger'

const getStatusType = (status: string): TagType => {
  const types: Record<string, TagType> = {
    PENDING: 'info',
    VIEWED: 'warning',
    INTERVIEW: 'success',
    REJECTED: 'danger',
    ACCEPTED: 'success',
  }
  return types[status] || 'info'
}

const getStatusText = (status: string): string => {
  const texts: Record<string, string> = {
    PENDING: '待查看',
    VIEWED: '已查看',
    INTERVIEW: '面试邀请',
    REJECTED: '已拒绝',
    ACCEPTED: '已录用',
  }
  return texts[status] || status
}

const formatTime = (time: string): string => {
  return dayjs(time).format('MM-DD HH:mm')
}

onMounted(() => {
  fetchCandidates()
})
</script>

<style scoped lang="scss">
.employer-candidates {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f5f7fa;
}

.main-content {
  flex: 1;
  padding: 24px 0;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  display: flex;
  align-items: center;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);

  .stat-icon {
    width: 56px;
    height: 56px;
    border-radius: 8px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 16px;
    color: #fff;
  }

  .stat-info {
    flex: 1;

    .stat-value {
      font-size: 28px;
      font-weight: 600;
      color: #303133;
      line-height: 1;
      margin-bottom: 8px;
    }

    .stat-label {
      font-size: 14px;
      color: #909399;
    }
  }

  &.total .stat-icon {
    background: #409eff;
  }

  &.pending .stat-icon {
    background: #e6a23c;
  }

  &.interview .stat-icon {
    background: #00b85c;
  }

  &.rejected .stat-icon {
    background: #f56c6c;
  }
}

.filter-bar {
  background: #fff;
  padding: 16px 20px;
  border-radius: 8px;
  margin-bottom: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  display: flex;
  justify-content: space-between;
  align-items: center;

  .filter-left {
    display: flex;
    align-items: center;
    gap: 12px;

    .filter-label {
      font-size: 14px;
      color: #606266;
      font-weight: 500;
    }
  }

  .filter-right {
    .search-input {
      width: 250px;
    }
  }
}

.table-wrapper {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  margin-bottom: 16px;

  .candidate-name {
    display: flex;
    align-items: center;
    gap: 8px;

    .avatar {
      background: #409eff;
      color: #fff;
    }
  }

  .job-info {
    .job-title {
      font-size: 14px;
      font-weight: 500;
      color: #303133;
      margin-bottom: 4px;
    }

    .job-company {
      font-size: 12px;
      color: #909399;
    }
  }

  .contact-info {
    .phone {
      font-size: 13px;
      color: #606266;
      margin-bottom: 4px;
    }

    .email {
      font-size: 12px;
      color: #909399;
    }
  }

  .time-text {
    font-size: 13px;
    color: #606266;
  }

  .action-buttons {
    display: flex;
    gap: 8px;
    justify-content: center;
  }
}

.pagination-wrapper {
  background: #fff;
  padding: 16px 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  display: flex;
  justify-content: center;
}

.resume-dialog {
  .resume-detail {
    .resume-header {
      display: flex;
      align-items: center;
      gap: 20px;
      padding: 20px;
      background: #f5f7fa;
      border-radius: 8px;
      margin-bottom: 24px;

      .resume-avatar {
        background: #409eff;
        color: #fff;
        font-size: 24px;
      }

      .resume-basic {
        h3 {
          margin: 0 0 12px;
          font-size: 20px;
          font-weight: 600;
          color: #303133;
        }

        .basic-info {
          display: flex;
          gap: 16px;
          font-size: 14px;
          color: #606266;

          span {
            display: flex;
            align-items: center;
            gap: 4px;
          }
        }
      }
    }

    .resume-section {
      margin-bottom: 24px;

      h4 {
        display: flex;
        align-items: center;
        gap: 8px;
        font-size: 16px;
        font-weight: 600;
        color: #303133;
        margin: 0 0 16px;
        padding-bottom: 12px;
        border-bottom: 2px solid #e4e7ed;
      }

      .resume-text {
        color: #606266;
        line-height: 1.8;
        white-space: pre-wrap;
        padding: 12px;
        background: #f5f7fa;
        border-radius: 8px;
      }

      .timeline-content {
        .timeline-title {
          font-size: 15px;
          font-weight: 500;
          color: #303133;
          margin-bottom: 4px;
        }

        .timeline-desc {
          font-size: 13px;
          color: #606266;
          line-height: 1.6;
        }
      }

      .skills-list {
        display: flex;
        flex-wrap: wrap;
        gap: 8px;
      }
    }
  }
}
</style>
