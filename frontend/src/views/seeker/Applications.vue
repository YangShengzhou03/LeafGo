<template>
  <div class="seeker-applications">
    <div class="container">
      <div class="applications-header">
        <h2>投递记录</h2>
        <el-radio-group v-model="statusFilter" @change="handleFilter">
          <el-radio-button label="">全部</el-radio-button>
          <el-radio-button label="PENDING">待查看</el-radio-button>
          <el-radio-button label="VIEWED">已查看</el-radio-button>
          <el-radio-button label="INTERVIEW">面试邀请</el-radio-button>
          <el-radio-button label="REJECTED">已拒绝</el-radio-button>
        </el-radio-group>
      </div>

      <div class="applications-list" v-loading="loading">
        <div v-for="app in applications" :key="app.id" class="application-item">
          <div class="job-info">
            <h3>{{ app.job?.title }}</h3>
            <div class="company">{{ getCompanyName(app) }}</div>
            <div class="salary">{{ app.job?.salary }}</div>
          </div>
          <div class="status-info">
            <el-tag :type="getStatusType(app.status)">
              {{ getStatusText(app.status) }}
            </el-tag>
            <div class="time">{{ formatTime(app.createdAt) }}</div>
          </div>
        </div>

        <el-empty v-if="!loading && applications.length === 0" description="暂无投递记录" />
      </div>

      <div class="pagination" v-if="total > 0">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          layout="prev, pager, next"
          @current-change="handlePageChange"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
defineOptions({
  name: 'SeekerApplications',
})
import { ref, onMounted } from 'vue'
import { applicationApi } from '@/api'
import type { Application } from '@/types'
import dayjs from 'dayjs'

type TagType = 'primary' | 'success' | 'warning' | 'info' | 'danger'

const loading = ref(false)
const applications = ref<Application[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const statusFilter = ref('')

const getCompanyName = (app: Application): string => {
  if (!app.job?.company) return ''
  if (typeof app.job.company === 'string') {
    return app.job.company
  }
  return app.job.company.name
}

const fetchApplications = async (): Promise<void> => {
  loading.value = true
  try {
    const data = await applicationApi.getMyApplications({
      page: currentPage.value,
      size: pageSize.value,
      status: statusFilter.value,
    })
    applications.value = data.content
    total.value = data.totalElements
  } finally {
    loading.value = false
  }
}

const handleFilter = (): void => {
  currentPage.value = 1
  fetchApplications()
}

const handlePageChange = (page: number): void => {
  currentPage.value = page
  fetchApplications()
}

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
  return dayjs(time).format('YYYY-MM-DD HH:mm')
}

onMounted(() => {
  fetchApplications()
})
</script>

<style scoped lang="scss">
.seeker-applications {
  .applications-header {
    background: #fff;
    border-radius: 12px;
    padding: 20px;
    margin-bottom: 20px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
    display: flex;
    justify-content: space-between;
    align-items: center;

    h2 {
      margin: 0;
      font-size: 20px;
      font-weight: 600;
      color: #303133;
    }
  }

  .applications-list {
    background: #fff;
    border-radius: 12px;
    padding: 20px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
    min-height: 400px;

    .application-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 16px;
      border-bottom: 1px solid #f0f2f5;
      cursor: pointer;
      transition: background 0.3s;

      &:hover {
        background: #f5f7fa;
      }

      &:last-child {
        border-bottom: none;
      }

      .job-info {
        h3 {
          font-size: 16px;
          font-weight: 600;
          color: #303133;
          margin: 0 0 8px 0;
        }

        .company {
          font-size: 14px;
          color: #606266;
          margin-bottom: 4px;
        }

        .salary {
          font-size: 14px;
          color: #00bebd;
          font-weight: 500;
        }
      }

      .status-info {
        text-align: right;

        .time {
          font-size: 12px;
          color: #909399;
          margin-top: 8px;
        }
      }
    }
  }

  .pagination {
    background: #fff;
    border-radius: 12px;
    padding: 20px;
    margin-top: 20px;
    display: flex;
    justify-content: center;
  }
}
</style>
