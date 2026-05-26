<template>
  <div class="employer-jobs">
    <div class="page-header">
      <h2>职位管理</h2>
      <el-button type="primary" @click="$router.push('/employer/jobs/create')">
        <el-icon><Plus /></el-icon>
        发布新职位
      </el-button>
    </div>

    <div class="filter-bar">
      <el-radio-group v-model="statusFilter" @change="handleFilter">
        <el-radio-button label="">全部</el-radio-button>
        <el-radio-button label="ACTIVE">招聘中</el-radio-button>
        <el-radio-button label="CLOSED">已关闭</el-radio-button>
        <el-radio-button label="DRAFT">草稿</el-radio-button>
      </el-radio-group>
    </div>

    <el-table :data="jobList" style="width: 100%" v-loading="loading">
      <el-table-column prop="title" label="职位名称" width="200" />
      <el-table-column prop="location" label="工作地点" width="150" />
      <el-table-column prop="salary" label="薪资范围" width="120" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">
            {{ getStatusText(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="viewCount" label="浏览量" width="100" />
      <el-table-column prop="createdAt" label="发布时间" width="180">
        <template #default="{ row }">
          {{ formatTime(row.createdAt) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" fixed="right" width="200">
        <template #default="{ row }">
          <el-button type="primary" text size="small" @click="handleEdit(row)"> 编辑 </el-button>
          <el-button
            :type="row.status === 'ACTIVE' ? 'warning' : 'success'"
            text
            size="small"
            @click="toggleStatus(row)"
          >
            {{ row.status === 'ACTIVE' ? '关闭' : '开启' }}
          </el-button>
          <el-button type="danger" text size="small" @click="handleDelete(row)"> 删除 </el-button>
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
  name: 'EmployerJobs',
})
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { jobApi } from '@/api'
import type { Job } from '@/types'
import dayjs from 'dayjs'

const router = useRouter()
const loading = ref(false)
const jobList = ref<Job[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const statusFilter = ref('')

const fetchJobs = async (): Promise<void> => {
  loading.value = true
  try {
    const data = await jobApi.getMyJobs({
      page: currentPage.value,
      size: pageSize.value,
      status: statusFilter.value,
    })
    jobList.value = data.content
    total.value = data.totalElements
  } finally {
    loading.value = false
  }
}

const handleFilter = (): void => {
  currentPage.value = 1
  fetchJobs()
}

const handlePageChange = (page: number): void => {
  currentPage.value = page
  fetchJobs()
}

const handleEdit = (job: Job): void => {
  router.push(`/employer/jobs/${job.id}/edit`)
}

const toggleStatus = async (job: Job): Promise<void> => {
  const newStatus = job.status === 'ACTIVE' ? 'CLOSED' : 'ACTIVE'
  await jobApi.updateJob(job.id, { status: newStatus } as Partial<Job>)
  ElMessage.success('状态更新成功')
  fetchJobs()
}

const handleDelete = async (job: Job): Promise<void> => {
  await ElMessageBox.confirm('确定要删除该职位吗？', '提示', {
    type: 'warning',
  })
  await jobApi.deleteJob(job.id)
  ElMessage.success('删除成功')
  fetchJobs()
}

type TagType = 'primary' | 'success' | 'warning' | 'info' | 'danger'

const getStatusType = (status: string): TagType => {
  const types: Record<string, TagType> = {
    ACTIVE: 'success',
    CLOSED: 'info',
    DRAFT: 'warning',
  }
  return types[status] || 'info'
}

const getStatusText = (status: string): string => {
  const texts: Record<string, string> = {
    ACTIVE: '招聘中',
    CLOSED: '已关闭',
    DRAFT: '草稿',
  }
  return texts[status] || status
}

const formatTime = (time: string): string => {
  return dayjs(time).format('YYYY-MM-DD HH:mm')
}

onMounted(() => {
  fetchJobs()
})
</script>

<style scoped lang="scss">
.employer-jobs {
  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
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
