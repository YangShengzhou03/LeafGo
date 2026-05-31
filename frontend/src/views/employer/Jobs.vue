<template>
  <div class="employer-jobs">
    <EmployerNavBar />
    <main class="main-content">
      <div class="container">
        <div class="filter-bar">
          <div class="filter-left">
            <el-select v-model="statusFilter" placeholder="状态筛选" clearable @change="handleFilter">
              <el-option label="全部" value="" />
              <el-option label="招聘中" value="ACTIVE" />
              <el-option label="已关闭" value="CLOSED" />
              <el-option label="草稿" value="DRAFT" />
            </el-select>
          </div>
          <div class="filter-right">
            <el-input
              v-model="searchKeyword"
              placeholder="搜索职位名称"
              prefix-icon="Search"
              clearable
              class="search-input"
              @input="handleSearch"
            />
          </div>
        </div>

        <div class="table-wrapper">
          <el-table
            :data="filteredJobs"
            style="width: 100%"
            v-loading="loading"
          >
            <el-table-column prop="title" label="职位名称" min-width="180" />
            <el-table-column prop="city" label="地点" width="120">
              <template #default="{ row }">
                {{ row.city || row.province || '未设置' }}
              </template>
            </el-table-column>
            <el-table-column prop="salary" label="薪资范围" width="120">
              <template #default="{ row }">
                {{ row.salary || '面议' }}
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="100" align="center">
              <template #default="{ row }">
                <el-tag :type="getStatusType(row.status)" size="small">
                  {{ getStatusText(row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="viewCount" label="浏览量" width="100" align="center" />
            <el-table-column prop="createdAt" label="发布时间" width="120" align="center">
              <template #default="{ row }">
                {{ formatTime(row.createdAt) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" fixed="right" width="120" align="center">
              <template #default="{ row }">
                <span class="link-text" @click="handleEdit(row)">编辑</span>
                <span class="link-text" :class="row.status === 'ACTIVE' ? 'warning' : 'success'" @click="toggleStatus(row)">
                  {{ row.status === 'ACTIVE' ? '关闭' : '开启' }}
                </span>
                <span class="link-text danger" @click="handleDelete(row)">删除</span>
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
      </div>
    </main>
    <AppFooter />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Briefcase, Lock, Edit, Location, View, Delete, Unlock } from '@element-plus/icons-vue'
import EmployerNavBar from '@/components/EmployerNavBar.vue'
import AppFooter from '@/components/AppFooter.vue'
import { jobApi } from '@/api'
import type { Job } from '@/types'
import dayjs from 'dayjs'

defineOptions({
  name: 'EmployerJobs',
})

const router = useRouter()
const loading = ref(false)
const jobList = ref<Job[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const statusFilter = ref('')
const searchKeyword = ref('')

const stats = reactive({
  active: 0,
  closed: 0,
  draft: 0,
})

const filteredJobs = computed<Job[]>(() => {
  if (!searchKeyword.value) return jobList.value
  const keyword = searchKeyword.value.toLowerCase()
  return jobList.value.filter((job) => {
    return job.title.toLowerCase().includes(keyword)
  })
})

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
    stats.active = jobList.value.filter((j) => j.status === 'ACTIVE').length
    stats.closed = jobList.value.filter((j) => j.status === 'CLOSED').length
    stats.draft = jobList.value.filter((j) => j.status === 'DRAFT').length
  } finally {
    loading.value = false
  }
}

const handleSearch = (): void => {
  // Search is handled by computed property
}

const handleFilter = (): void => {
  currentPage.value = 1
  fetchJobs()
}

const handlePageChange = (page: number): void => {
  currentPage.value = page
  fetchJobs()
}

const handleSizeChange = (size: number): void => {
  pageSize.value = size
  currentPage.value = 1
  fetchJobs()
}

const handleEdit = (job: Job): void => {
  router.push(`/employer/jobs/${job.id}/edit`)
}

const toggleStatus = async (job: Job): Promise<void> => {
  const newStatus = job.status === 'ACTIVE' ? 'CLOSED' : 'ACTIVE'
  try {
    await jobApi.updateJob(job.id, { ...job, status: newStatus as Job['status'] })
    ElMessage.success(newStatus === 'ACTIVE' ? '职位已开启' : '职位已关闭')
    fetchJobs()
  } catch {
    ElMessage.error('操作失败')
  }
}

const handleDelete = async (job: Job): Promise<void> => {
  try {
    await ElMessageBox.confirm('确定要删除该职位吗？删除后无法恢复。', '删除确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    await jobApi.deleteJob(job.id)
    ElMessage.success('删除成功')
    fetchJobs()
  } catch {
    // User cancelled or error occurred
  }
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
  return dayjs(time).format('YYYY-MM-DD')
}

onMounted(() => {
  fetchJobs()
})
</script>

<style scoped lang="scss">
.employer-jobs {
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
  cursor: pointer;
  transition: all 0.3s;

  &:hover {
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
    transform: translateY(-2px);
  }

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

  &.active .stat-icon {
    background: #00b85c;
  }

  &.closed .stat-icon {
    background: #909399;
  }

  &.draft .stat-icon {
    background: #e6a23c;
  }

  &.publish-card {
    cursor: pointer;

    .stat-icon {
      background: #409eff;
    }

    .stat-label {
      font-size: 16px;
      font-weight: 500;
      color: #303133;
    }
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

  .job-title-cell {
    .job-title {
      font-size: 14px;
      font-weight: 500;
      color: #303133;
      margin-bottom: 4px;
    }

    .job-location {
      display: flex;
      align-items: center;
      gap: 4px;
      font-size: 12px;
      color: #909399;
    }
  }

  .salary-text {
    font-size: 14px;
    font-weight: 500;
    color: #f56c6c;
  }

  .view-count {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 4px;
    font-size: 13px;
    color: #606266;
  }

  .time-text {
    font-size: 13px;
    color: #909399;
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

.link-text {
  color: #00b85c;
  cursor: pointer;
  font-size: 14px;
  margin-right: 12px;

  &:hover {
    color: #85ce61;
    text-decoration: underline;
  }

  &.warning {
    color: #e6a23c;

    &:hover {
      color: #ebb563;
    }
  }

  &.success {
    color: #00b85c;

    &:hover {
      color: #85ce61;
    }
  }

  &.danger {
    color: #f56c6c;

    &:hover {
      color: #f78989;
    }
  }
}
</style>
