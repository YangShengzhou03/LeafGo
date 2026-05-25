<template>
  <div class="admin-users">
    <div class="page-header">
      <h2>用户管理</h2>
      <el-input
        v-model="searchKeyword"
        placeholder="搜索用户名或邮箱"
        style="width: 300px"
        @keyup.enter="handleSearch"
      >
        <template #append>
          <el-button @click="handleSearch">搜索</el-button>
        </template>
      </el-input>
    </div>

    <el-table :data="users" style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" width="150" />
      <el-table-column prop="email" label="邮箱" width="200" />
      <el-table-column prop="phone" label="手机号" width="130" />
      <el-table-column prop="userType" label="用户类型" width="120">
        <template #default="{ row }">
          <el-tag :type="row.userType === 'job_seeker' ? 'primary' : 'success'">
            {{ row.userType === 'job_seeker' ? '求职者' : '招聘者' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 'ACTIVE' ? 'success' : 'danger'">
            {{ row.status === 'ACTIVE' ? '正常' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="注册时间" width="180">
        <template #default="{ row }">
          {{ formatTime(row.createdAt) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" fixed="right" width="200">
        <template #default="{ row }">
          <el-button type="primary" text size="small">查看</el-button>
            <el-button
              :type="row.status === 'ACTIVE' ? 'danger' : 'success'"
              text
              size="small"
              @click="toggleStatus(row)"
            >
              {{ row.status === 'ACTIVE' ? '禁用' : '启用' }}
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
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { adminApi } from '@/api/admin'
import type { User } from '@/types'
import dayjs from 'dayjs'

defineOptions({
  name: 'AdminUsers',
})

const loading = ref(false)
const searchKeyword = ref('')
const users = ref<User[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)

const fetchUsers = async (): Promise<void> => {
  loading.value = true
  try {
    const data = await adminApi.getUsers({
      page: currentPage.value,
      size: pageSize.value,
      keyword: searchKeyword.value || undefined,
    })
    users.value = data.content
    total.value = data.totalElements
  } finally {
    loading.value = false
  }
}

const handleSearch = (): void => {
  currentPage.value = 1
  fetchUsers()
}

const handlePageChange = (page: number): void => {
  currentPage.value = page
  fetchUsers()
}

const toggleStatus = async (user: User): Promise<void> => {
  try {
    await adminApi.toggleUserStatus(user.id)
    ElMessage.success('状态更新成功')
    fetchUsers()
  } catch {
    ElMessage.error('操作失败')
  }
}

const formatTime = (time: string): string => {
  return dayjs(time).format('YYYY-MM-DD HH:mm')
}

onMounted(() => {
  fetchUsers()
})
</script>

<style scoped lang="scss">
.admin-users {
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
