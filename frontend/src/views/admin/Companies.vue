<template>
  <div class="admin-companies">
    <div class="page-header">
      <h2>企业管理</h2>
    </div>

    <el-table :data="companies" style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="企业名称" width="200" />
      <el-table-column prop="industry" label="所属行业" width="150" />
      <el-table-column prop="scale" label="规模" width="120" />
      <el-table-column prop="location" label="地址" width="200" />
      <el-table-column prop="verified" label="认证状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.verified ? 'success' : 'info'">
            {{ row.verified ? '已认证' : '未认证' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="注册时间" width="180">
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
import type { Company } from '@/types'
import dayjs from 'dayjs'

defineOptions({
  name: 'AdminCompanies',
})

const loading = ref(false)
const companies = ref<Company[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)

const fetchCompanies = async (): Promise<void> => {
  loading.value = true
  try {
    const data = await adminApi.getCompanies({
      page: currentPage.value,
      size: pageSize.value,
    })
    companies.value = data.content
    total.value = data.totalElements
  } finally {
    loading.value = false
  }
}

const handlePageChange = (page: number): void => {
  currentPage.value = page
  fetchCompanies()
}

const formatTime = (time: string): string => {
  return dayjs(time).format('YYYY-MM-DD HH:mm')
}

onMounted(() => {
  fetchCompanies()
})
</script>

<style scoped lang="scss">
.admin-companies {
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
