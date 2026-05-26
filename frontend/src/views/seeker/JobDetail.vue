<template>
  <div class="seeker-job-detail">
    <div class="container">
      <div class="job-main" v-loading="loading">
        <div class="job-header">
          <div class="job-info">
            <h1>{{ job.title }}</h1>
            <div class="salary">{{ job.salary }}</div>
            <div class="job-tags">
              <el-tag v-if="job.jobType">{{ job.jobType }}</el-tag>
              <el-tag v-if="job.experience" type="info">{{ job.experience }}</el-tag>
              <el-tag v-if="job.education" type="info">{{ job.education }}</el-tag>
              <span class="location">
                <el-icon><Location /></el-icon>
                {{ getLocation(job) }}
              </span>
            </div>
          </div>
          <div class="job-actions">
            <el-button type="primary" size="large" @click="handleApply"> 立即投递 </el-button>
            <el-button size="large" @click="handleCollect">
              <el-icon><Star /></el-icon>
              收藏
            </el-button>
          </div>
        </div>

        <div class="job-content">
          <div class="content-section">
            <h3>职位描述</h3>
            <div class="content-text">{{ job.description }}</div>
          </div>

          <div class="content-section" v-if="job.benefits && job.benefits.length > 0">
            <h3>福利待遇</h3>
            <div class="benefits-tags">
              <el-tag v-for="benefit in job.benefits" :key="benefit" type="success">
                {{ benefit }}
              </el-tag>
            </div>
          </div>
        </div>

        <div class="company-section">
          <h3>公司信息</h3>
          <div class="company-card" @click="handleCompanyClick">
            <img v-if="companyInfo?.logo" :src="companyInfo.logo" class="company-logo" />
            <div v-else class="company-logo-placeholder">
              {{ companyInfo?.name?.charAt(0) || '?' }}
            </div>
            <div class="company-info">
              <div class="company-name">{{ companyInfo?.name || job.companyName || '未知公司' }}</div>
              <div class="company-meta">
                <span v-if="companyInfo?.industry">{{ companyInfo.industry }}</span>
                <span v-if="companyInfo?.scale">· {{ companyInfo.scale }}</span>
              </div>
            </div>
            <el-icon class="arrow-icon"><ArrowRight /></el-icon>
          </div>
        </div>

        <div class="work-address">
          <h3>工作地址</h3>
          <div class="address-text">
            <el-icon><Location /></el-icon>
            {{ getFullAddress(job) }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useJobStore } from '@/store/job'
import { useUserStore } from '@/store/user'
import type { Job, Company } from '@/types'

const route = useRoute()
const router = useRouter()
const jobStore = useJobStore()
const userStore = useUserStore()

const loading = ref(false)
const job = ref<Job>({
  id: 0,
  companyId: 0,
  title: '',
  salary: '',
  jobType: '',
  experience: '',
  education: '',
  description: '',
  benefits: [],
  status: 'ACTIVE',
  viewCount: 0,
  createdAt: '',
  updatedAt: '',
})

const companyInfo = computed<Company | null>(() => {
  const c = job.value.company
  if (typeof c === 'object' && c !== null) {
    return c
  }
  return null
})

const fetchJobDetail = async (): Promise<void> => {
  loading.value = true
  try {
    const jobId = Number(route.params.id)
    const data = await jobStore.fetchJobDetail(jobId)
    job.value = data
  } finally {
    loading.value = false
  }
}

const handleApply = (): void => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  router.push({
    path: '/seeker/resume',
    query: { jobId: job.value.id },
  })
}

const handleCollect = (): void => {
  ElMessage.success('收藏成功')
}

const handleCompanyClick = (): void => {
  if (companyInfo.value?.id) {
    router.push(`/seeker/companies/${companyInfo.value.id}`)
  }
}

const getLocation = (job: Job): string => {
  if (job.city && job.district) {
    return `${job.city}·${job.district}`
  }
  if (job.city) {
    return job.city
  }
  if (companyInfo.value) {
    const c = companyInfo.value
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
  if (companyInfo.value) {
    const c = companyInfo.value
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

onMounted(() => {
  fetchJobDetail()
})
</script>

<style scoped lang="scss">
.seeker-job-detail {
  .job-main {
    background: #fff;
    border-radius: 12px;
    padding: 30px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);

    .job-header {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      margin-bottom: 30px;
      padding-bottom: 20px;
      border-bottom: 1px solid #f0f2f5;

      .job-info {
        h1 {
          font-size: 24px;
          font-weight: 600;
          color: #303133;
          margin: 0 0 12px 0;
        }

        .salary {
          font-size: 24px;
          font-weight: 600;
          color: #00bebd;
          margin-bottom: 12px;
        }

        .job-tags {
          display: flex;
          align-items: center;
          gap: 12px;

          .location {
            display: flex;
            align-items: center;
            gap: 4px;
            color: #909399;
            font-size: 14px;
          }
        }
      }

      .job-actions {
        display: flex;
        gap: 12px;
      }
    }

    .job-content {
      margin-bottom: 30px;

      .content-section {
        margin-bottom: 24px;

        h3 {
          font-size: 16px;
          font-weight: 600;
          color: #303133;
          margin: 0 0 12px 0;
        }

        .content-text {
          font-size: 14px;
          color: #606266;
          line-height: 1.8;
          white-space: pre-wrap;
        }

        .benefits-tags {
          display: flex;
          flex-wrap: wrap;
          gap: 10px;
        }
      }
    }

    .company-section {
      margin-bottom: 30px;

      h3 {
        font-size: 16px;
        font-weight: 600;
        color: #303133;
        margin: 0 0 12px 0;
      }

      .company-card {
        display: flex;
        align-items: center;
        padding: 16px;
        background: #f5f7fa;
        border-radius: 8px;
        cursor: pointer;
        transition: all 0.3s;

        &:hover {
          background: #ecf5ff;
        }

        .company-logo,
        .company-logo-placeholder {
          width: 48px;
          height: 48px;
          border-radius: 8px;
          margin-right: 12px;
        }

        .company-logo-placeholder {
          background: linear-gradient(135deg, #00bebd 0%, #00a8a7 100%);
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 20px;
          font-weight: 600;
          color: #fff;
        }

        .company-info {
          flex: 1;

          .company-name {
            font-size: 16px;
            font-weight: 600;
            color: #303133;
            margin-bottom: 4px;
          }

          .company-meta {
            font-size: 13px;
            color: #909399;
          }
        }

        .arrow-icon {
          color: #c0c4cc;
        }
      }
    }

    .work-address {
      h3 {
        font-size: 16px;
        font-weight: 600;
        color: #303133;
        margin: 0 0 12px 0;
      }

      .address-text {
        display: flex;
        align-items: center;
        gap: 8px;
        color: #606266;
        font-size: 14px;
      }
    }
  }
}
</style>
