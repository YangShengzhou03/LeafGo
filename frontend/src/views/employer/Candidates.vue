<template>
  <div class="employer-candidates">
    <div class="page-header">
      <h2>候选人管理</h2>
    </div>

    <div class="filter-bar">
      <el-radio-group v-model="statusFilter" @change="handleFilter">
        <el-radio-button label="">全部</el-radio-button>
        <el-radio-button label="pending">待查看</el-radio-button>
        <el-radio-button label="viewed">已查看</el-radio-button>
        <el-radio-button label="interview">面试邀请</el-radio-button>
        <el-radio-button label="rejected">已拒绝</el-radio-button>
      </el-radio-group>
    </div>

    <el-table :data="candidates" style="width: 100%" v-loading="loading">
      <el-table-column prop="user.username" label="姓名" width="120" />
      <el-table-column prop="job.title" label="应聘职位" width="200" />
      <el-table-column prop="resume.phone" label="手机号" width="130" />
      <el-table-column prop="resume.email" label="邮箱" width="180" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">
            {{ getStatusText(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="投递时间" width="160">
        <template #default="{ row }">
          {{ formatTime(row.createdAt) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" fixed="right" width="240">
        <template #default="{ row }">
          <el-button type="primary" text size="small" @click="viewResume(row)">
            查看简历
          </el-button>
          <el-button
            type="success"
            text
            size="small"
            @click="updateStatus(row, 'interview')"
            v-if="row.status === 'PENDING' || row.status === 'VIEWED'"
          >
            邀请面试
          </el-button>
          <el-button
            type="danger"
            text
            size="small"
            @click="updateStatus(row, 'rejected')"
            v-if="row.status !== 'REJECTED'"
          >
            拒绝
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        layout="total, prev, pager, next"
        @current-change="handlePageChange"
      />
    </div>

    <el-dialog
      v-model="resumeDialogVisible"
      :title="`${resumeData?.name || ''} 的简历`"
      width="700px"
    >
      <div v-if="resumeLoading" v-loading="resumeLoading" style="height: 200px" />
      <div v-else-if="resumeData" class="resume-detail">
        <div class="resume-section">
          <h3>基本信息</h3>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="姓名">{{ resumeData.name }}</el-descriptions-item>
            <el-descriptions-item label="手机号">{{ resumeData.phone }}</el-descriptions-item>
            <el-descriptions-item label="邮箱">{{ resumeData.email }}</el-descriptions-item>
          </el-descriptions>
        </div>

        <div class="resume-section" v-if="resumeData.selfIntroduction">
          <h3>自我介绍</h3>
          <p class="resume-text">{{ resumeData.selfIntroduction }}</p>
        </div>

        <div class="resume-section" v-if="parsedEducation.length > 0">
          <h3>教育经历</h3>
          <div v-for="(edu, index) in parsedEducation" :key="index" class="resume-item">
            <div class="item-header">{{ edu.school }} · {{ edu.major }}</div>
            <div class="item-meta">{{ edu.degree }} · {{ edu.startDate }} - {{ edu.endDate }}</div>
          </div>
        </div>

        <div class="resume-section" v-if="parsedWorkExperience.length > 0">
          <h3>工作经历</h3>
          <div v-for="(work, index) in parsedWorkExperience" :key="index" class="resume-item">
            <div class="item-header">{{ work.company }} · {{ work.position }}</div>
            <div class="item-desc">{{ work.description }}</div>
          </div>
        </div>

        <div class="resume-section" v-if="parsedSkills.length > 0">
          <h3>技能特长</h3>
          <div class="skills-list">
            <el-tag
              v-for="(skill, index) in parsedSkills"
              :key="index"
              type="success"
              style="margin: 4px"
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
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
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

const resumeDialogVisible = ref(false)
const resumeLoading = ref(false)
const resumeData = ref<Resume | null>(null)

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
  } finally {
    loading.value = false
  }
}

const handleFilter = (): void => {
  currentPage.value = 1
  fetchCandidates()
}

const handlePageChange = (page: number): void => {
  currentPage.value = page
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
      await applicationApi.updateApplicationStatus(app.id, 'viewed')
      fetchCandidates()
    }
  } catch {
    ElMessage.error('获取简历失败')
  } finally {
    resumeLoading.value = false
  }
}

const updateStatus = async (app: Application, status: string): Promise<void> => {
  await applicationApi.updateApplicationStatus(app.id, status as Application['status'])
  ElMessage.success('状态更新成功')
  fetchCandidates()
}

type TagType = 'primary' | 'success' | 'warning' | 'info' | 'danger'

const getStatusType = (status: string): TagType => {
  const s = status.toLowerCase()
  const types: Record<string, TagType> = {
    pending: 'info',
    viewed: 'warning',
    interview: 'success',
    rejected: 'danger',
  }
  return types[s] || 'info'
}

const getStatusText = (status: string): string => {
  const s = status.toLowerCase()
  const texts: Record<string, string> = {
    pending: '待查看',
    viewed: '已查看',
    interview: '面试',
    rejected: '已拒绝',
  }
  return texts[s] || status
}

const formatTime = (time: string): string => {
  return dayjs(time).format('YYYY-MM-DD HH:mm')
}

onMounted(() => {
  fetchCandidates()
})
</script>

<style scoped lang="scss">
.employer-candidates {
  .page-header {
    margin-bottom: 20px;

    h2 {
      margin: 0;
      font-size: 20px;
      font-weight: 600;
    }
  }

  .filter-bar {
    background: #fff;
    padding: 16px;
    border-radius: 8px;
    margin-bottom: 16px;
  }

  :deep(.el-table) {
    background: #fff;
    border-radius: 8px;
  }

  .pagination {
    background: #fff;
    padding: 16px;
    border-radius: 8px;
    margin-top: 16px;
    display: flex;
    justify-content: center;
  }
}

.resume-detail {
  .resume-section {
    margin-bottom: 24px;

    h3 {
      font-size: 16px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 12px 0;
      padding-bottom: 8px;
      border-bottom: 2px solid #00bebd;
    }

    .resume-text {
      color: #606266;
      line-height: 1.8;
      white-space: pre-wrap;
    }

    .resume-item {
      padding: 12px;
      background: #f5f7fa;
      border-radius: 8px;
      margin-bottom: 8px;

      .item-header {
        font-size: 14px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }

      .item-meta {
        font-size: 13px;
        color: #909399;
      }

      .item-desc {
        font-size: 13px;
        color: #606266;
        margin-top: 4px;
        line-height: 1.6;
      }
    }

    .skills-list {
      display: flex;
      flex-wrap: wrap;
      gap: 4px;
    }
  }
}
</style>
