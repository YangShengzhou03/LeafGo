<template>
  <div class="employer-home">
    <el-row :gutter="20">
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon" style="background: #409eff">
            <el-icon size="24"><Briefcase /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.jobCount }}</div>
            <div class="stat-label">在招职位</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon" style="background: #67c23a">
            <el-icon size="24"><User /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.applicationCount }}</div>
            <div class="stat-label">收到简历</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon" style="background: #e6a23c">
            <el-icon size="24"><View /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.viewCount }}</div>
            <div class="stat-label">职位浏览</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon" style="background: #f56c6c">
            <el-icon size="24"><ChatDotRound /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.messageCount }}</div>
            <div class="stat-label">未读消息</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mt-20">
      <el-col :span="16">
        <div class="recent-applications">
          <div class="section-header">
            <h3>最新投递</h3>
            <el-button text @click="$router.push('/employer/candidates')"> 查看全部 </el-button>
          </div>
          <el-table :data="recentApplications" style="width: 100%">
            <el-table-column prop="user.username" label="求职者" width="120" />
            <el-table-column prop="job.title" label="应聘职位" />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="getStatusType(row.status)" size="small">
                  {{ getStatusText(row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createdAt" label="投递时间" width="180">
              <template #default="{ row: _row }">
                {{ formatTime(_row.createdAt) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="120">
              <template #default>
                <el-button type="primary" text size="small">查看</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="quick-actions">
          <h3>快捷操作</h3>
          <div class="action-buttons">
            <el-button type="primary" size="large" @click="$router.push('/employer/jobs/create')">
              <el-icon><Plus /></el-icon>
              发布新职位
            </el-button>
            <el-button size="large" @click="$router.push('/employer/company')">
              <el-icon><Edit /></el-icon>
              编辑企业信息
            </el-button>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
defineOptions({
  name: 'EmployerHome',
})
import { ref, reactive, onMounted } from 'vue'
import { applicationApi } from '@/api'
import type { Application } from '@/types'
import dayjs from 'dayjs'

const stats = reactive({
  jobCount: 12,
  applicationCount: 156,
  viewCount: 2340,
  messageCount: 8,
})

const recentApplications = ref<Application[]>([])

const fetchRecentApplications = async (): Promise<void> => {
  const data = await applicationApi.getReceivedApplications({ size: 5 })
  recentApplications.value = data.content
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
  fetchRecentApplications()
})
</script>

<style scoped lang="scss">
.employer-home {
  .stat-card {
    background: #fff;
    border-radius: 8px;
    padding: 20px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);

    .stat-icon {
      width: 48px;
      height: 48px;
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      color: #fff;
      margin-right: 16px;
    }

    .stat-info {
      .stat-value {
        font-size: 24px;
        font-weight: 600;
        color: #303133;
      }

      .stat-label {
        font-size: 14px;
        color: #909399;
        margin-top: 4px;
      }
    }
  }

  .recent-applications {
    background: #fff;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);

    .section-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;

      h3 {
        margin: 0;
        font-size: 16px;
        font-weight: 600;
      }
    }
  }

  .quick-actions {
    background: #fff;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);

    h3 {
      margin: 0 0 16px 0;
      font-size: 16px;
      font-weight: 600;
    }

    .action-buttons {
      display: flex;
      flex-direction: column;
      gap: 12px;

      .el-button {
        width: 100%;
      }
    }
  }
}
</style>
