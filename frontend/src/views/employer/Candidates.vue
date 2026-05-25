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
            v-if="row.status === 'pending' || row.status === 'viewed'"
          >
            邀请面试
          </el-button>
          <el-button
            type="danger"
            text
            size="small"
            @click="updateStatus(row, 'rejected')"
            v-if="row.status !== 'rejected'"
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
  </div>
</template>

<script setup lang="ts">
defineOptions({
  name: 'EmployerCandidates',
})
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { applicationApi } from '@/api'
import type { Application } from '@/types'
import dayjs from 'dayjs'

const loading = ref(false)
const candidates = ref<Application[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const statusFilter = ref('')

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

const viewResume = (_app: Application): void => {
  ElMessage.info('简历详情功能开发中')
}

const updateStatus = async (app: Application, status: string): Promise<void> => {
  await applicationApi.updateApplicationStatus(app.id, status as Application['status'])
  ElMessage.success('状态更新成功')
  fetchCandidates()
}

type TagType = 'primary' | 'success' | 'warning' | 'info' | 'danger'

const getStatusType = (status: string): TagType => {
  const types: Record<string, TagType> = {
    pending: 'info',
    viewed: 'warning',
    interview: 'success',
    rejected: 'danger',
  }
  return types[status] || 'info'
}

const getStatusText = (status: string): string => {
  const texts: Record<string, string> = {
    pending: '待查看',
    viewed: '已查看',
    interview: '面试',
    rejected: '已拒绝',
  }
  return texts[status] || status
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
</style>
