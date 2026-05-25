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
          <el-tag :type="row.status === 'active' ? 'success' : 'danger'">
            {{ row.status === 'active' ? '正常' : '禁用' }}
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
            :type="row.status === 'active' ? 'danger' : 'success'"
            text
            size="small"
            @click="toggleStatus(row)"
          >
            {{ row.status === 'active' ? '禁用' : '启用' }}
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
  name: 'AdminUsers',
})
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import type { User } from '@/types'
import dayjs from 'dayjs'

const loading = ref(false)
const searchKeyword = ref('')
const users = ref<User[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)

const fetchUsers = async (): Promise<void> => {
  loading.value = true
  try {
    await new Promise((resolve) => setTimeout(resolve, 500))
    users.value = [
      {
        id: 1,
        username: '张三',
        email: 'zhangsan@example.com',
        phone: '13800138001',
        userType: 'job_seeker',
        status: 'active',
        createdAt: '2024-01-01 10:00:00',
        updatedAt: '2024-01-01 10:00:00',
      },
    ]
    total.value = 1
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

const toggleStatus = (_user: User): void => {
  ElMessage.success('状态更新成功')
  fetchUsers()
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
