<template>
  <div class="admin-jobs">
    <div class="page-header">
      <h2>职位管理</h2>
    </div>

    <el-table :data="jobs" style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="职位名称" width="200" />
      <el-table-column prop="companyName" label="公司" width="150" />
      <el-table-column prop="location" label="工作地点" width="150" />
      <el-table-column prop="salary" label="薪资" width="120" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 'ACTIVE' ? 'success' : 'info'">
            {{ row.status === 'ACTIVE' ? '招聘中' : '已关闭' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="发布时间" width="180">
        <template #default="{ row }">
          {{ formatTime(row.createdAt) }}
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
import { ref, onMounted } from 'vue'
import { adminApi } from '@/api/admin'
import type { Job } from '@/types'
import dayjs from 'dayjs'

defineOptions({
  name: 'AdminJobs',
})

const loading = ref(false)
const jobs = ref<Job[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)

const fetchJobs = async (): Promise<void> => {
  loading.value = true
  try {
    const data = await adminApi.getJobs({
      page: currentPage.value,
      size: pageSize.value,
    })
    jobs.value = data.content
    total.value = data.totalElements
  } finally {
    loading.value = false
  }
}

const handlePageChange = (page: number): void => {
  currentPage.value = page
  fetchJobs()
}

const formatTime = (time: string): string => {
  return dayjs(time).format('YYYY-MM-DD HH:mm')
}

onMounted(() => {
  fetchJobs()
})
</script>

<style scoped lang="scss">
.admin-jobs {
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
