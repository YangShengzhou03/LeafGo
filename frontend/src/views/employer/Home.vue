<template>
  <div class="employer-home">
    <EmployerNavBar />
    <main class="main-content">
      <div class="container">
        <div class="welcome-section">
          <div class="welcome-left">
            <div class="welcome-title">欢迎回来，{{ displayName }}</div>
            <p>今天是 {{ currentDate }}，祝您招聘顺利！</p>
          </div>
          <div class="welcome-right">
            <span class="link-text" @click="$router.push('/employer/jobs/create')">发布新职位</span>
          </div>
        </div>

        <el-row :gutter="16" class="stats-row">
          <el-col :span="6">
            <div class="stat-card" @click="$router.push('/employer/jobs')">
              <div class="stat-value">{{ stats.jobCount }}</div>
              <div class="stat-label">在招职位</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card" @click="$router.push('/employer/candidates')">
              <div class="stat-value">{{ stats.applicationCount }}</div>
              <div class="stat-label">收到简历</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-value">{{ stats.viewCount }}</div>
              <div class="stat-label">职位浏览</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card" @click="$router.push('/employer/messages')">
              <div class="stat-value">{{ stats.messageCount }}</div>
              <div class="stat-label">未读消息</div>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="16" class="content-row">
          <el-col :span="16">
            <div class="recent-section">
              <div class="section-header">
                <div class="header-left">
                  <h3><el-icon><Clock /></el-icon> 最新投递</h3>
                  <span class="subtitle">最近7天收到的简历</span>
                </div>
                <span class="view-all-link" @click="$router.push('/employer/candidates')">
                  查看全部 <el-icon><ArrowRight /></el-icon>
                </span>
              </div>
              <div class="table-wrapper">
                <el-table :data="recentApplications" style="width: 100%"
                  :header-cell-style="{ background: '#fafafa', color: '#606266' }">
                  <el-table-column prop="user.username" label="求职者" width="140">
                    <template #default="{ row }">
                      <div class="candidate-info">
                        <el-avatar :size="36" class="avatar">
                          {{ row.user?.username?.charAt(0) || '用' }}
                        </el-avatar>
                        <span class="name">{{ row.user?.username || '未知' }}</span>
                      </div>
                    </template>
                  </el-table-column>
                  <el-table-column prop="job.title" label="应聘职位" min-width="180">
                    <template #default="{ row }">
                      <div class="job-info">
                        <div class="job-title">{{ row.job?.title || '未知职位' }}</div>
                        <div class="job-salary">{{ row.job?.salary || '' }}</div>
                      </div>
                    </template>
                  </el-table-column>
                  <el-table-column prop="status" label="状态" width="120" align="center">
                    <template #default="{ row }">
                      <el-tag :type="getStatusType(row.status)" size="default" effect="light">
                        {{ getStatusText(row.status) }}
                      </el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column prop="createdAt" label="投递时间" width="160" align="center">
                    <template #default="{ row }">
                      <span class="time-text">{{ formatTime(row.createdAt) }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column label="操作" width="100" align="center">
                    <template #default="{ row }">
                      <el-button type="primary" size="small" @click="viewApplication(row)">
                        查看
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </div>
          </el-col>

          <el-col :span="8">
            <div class="message-section">
              <div class="tips-section">
                <h3><el-icon>
                    <Notification />
                  </el-icon> 招聘小贴士</h3>
                <div class="tips-list">
                  <div class="tip-item">
                    <el-icon class="tip-icon">
                      <SuccessFilled />
                    </el-icon>
                    <span>完善职位描述可以提高简历匹配度</span>
                  </div>
                  <div class="tip-item">
                    <el-icon class="tip-icon">
                      <SuccessFilled />
                    </el-icon>
                    <span>及时回复候选人提升企业形象</span>
                  </div>
                  <div class="tip-item">
                    <el-icon class="tip-icon">
                      <SuccessFilled />
                    </el-icon>
                    <span>定期更新职位保持活跃度</span>
                  </div>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </main>
    <AppFooter />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import {
  Briefcase,
  Document,
  View,
  ChatDotRound,
  Plus,
  User,
  Clock,
  ArrowRight,
  Operation,
  Setting,
  Notification,
  SuccessFilled,
  TrendCharts,
} from '@element-plus/icons-vue'
import EmployerNavBar from '@/components/EmployerNavBar.vue'
import AppFooter from '@/components/AppFooter.vue'
import { applicationApi } from '@/api'
import { useUserStore } from '@/store/user'
import type { Application } from '@/types'
import dayjs from 'dayjs'

defineOptions({
  name: 'EmployerHome',
})

const router = useRouter()
const userStore = useUserStore()

const stats = reactive({
  jobCount: 12,
  applicationCount: 156,
  viewCount: 2340,
  messageCount: 8,
})

const recentApplications = ref<Application[]>([])

const displayName = computed((): string => {
  return userStore.userInfo?.username || '招聘者'
})

const currentDate = computed((): string => {
  return dayjs().format('YYYY年MM月DD日 dddd')
})

const fetchRecentApplications = async (): Promise<void> => {
  try {
    const data = await applicationApi.getReceivedApplications({ size: 5 })
    recentApplications.value = data.content
  } catch {
    // Ignore error
  }
}

const viewApplication = (row: Application): void => {
  router.push(`/employer/candidates/${row.id}`)
}

type TagType = 'primary' | 'success' | 'warning' | 'info' | 'danger'

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
    INTERVIEW: '面试',
    REJECTED: '已拒绝',
    ACCEPTED: '已录用',
  }
  return texts[status] || status
}

const formatTime = (time: string): string => {
  return dayjs(time).format('MM-DD HH:mm')
}

onMounted(() => {
  fetchRecentApplications()
})
</script>

<style scoped lang="scss">
.employer-home {
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

.welcome-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding: 24px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);

  .welcome-left {
    h1 {
      margin: 0 0 8px;
      font-size: 24px;
      font-weight: 600;
      color: #303133;
    }

    p {
      margin: 0;
      font-size: 14px;
      color: #909399;
    }
  }
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  background: #fff;
  border-radius: 8px;
  padding: 24px;
  text-align: center;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  cursor: pointer;
  transition: all 0.3s;

  &:hover {
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  }

  .stat-value {
    font-size: 32px;
    color: #303133;
    line-height: 1;
    margin-bottom: 12px;
  }

  .stat-label {
    font-size: 14px;
    color: #909399;
  }
}

.content-row {
  margin-bottom: 20px;
}

.recent-section {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);

  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    .header-left {
      h3 {
        display: flex;
        align-items: center;
        gap: 8px;
        margin: 0 0 4px;
        font-size: 16px;
        font-weight: 600;
        color: #303133;
      }

      .subtitle {
        font-size: 13px;
        color: #909399;
      }
    }
  }

  .view-all-link {
    display: flex;
    align-items: center;
    gap: 4px;
    font-size: 14px;
    color: #00b85c;
    cursor: pointer;

    &:hover {
      color: #85ce61;
    }
  }

  .table-wrapper {
    .candidate-info {
      display: flex;
      align-items: center;
      gap: 10px;

      .avatar {
        background: #409eff;
        color: #fff;
      }

      .name {
        font-weight: 500;
        color: #303133;
      }
    }

    .job-info {
      .job-title {
        font-size: 14px;
        font-weight: 500;
        color: #303133;
        margin-bottom: 4px;
      }

      .job-salary {
        font-size: 12px;
        color: #f56c6c;
      }
    }

    .time-text {
      font-size: 13px;
      color: #909399;
    }
  }

  .empty-state {
    padding: 40px 0;
  }
}

.sidebar {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.quick-actions {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);

  h3 {
    display: flex;
    align-items: center;
    gap: 8px;
    margin: 0 0 20px;
    font-size: 16px;
    font-weight: 600;
    color: #303133;
  }

  .action-list {
    display: flex;
    flex-direction: column;
    gap: 12px;

    .action-item {
      display: flex;
      align-items: center;
      gap: 16px;
      padding: 16px;
      background: #f5f7fa;
      border-radius: 8px;
      cursor: pointer;

      .action-icon {
        width: 48px;
        height: 48px;
        border-radius: 8px;
        display: flex;
        align-items: center;
        justify-content: center;
        color: #fff;
        font-size: 20px;
      }

      .action-text {
        flex: 1;

        .action-title {
          font-size: 15px;
          font-weight: 500;
          color: #303133;
          margin-bottom: 4px;
        }

        .action-desc {
          font-size: 12px;
          color: #909399;
        }
      }
    }
  }
}

.tips-section {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);

  h3 {
    display: flex;
    align-items: center;
    gap: 8px;
    margin: 0 0 20px;
    font-size: 16px;
    font-weight: 600;
    color: #303133;
  }

  .tips-list {
    display: flex;
    flex-direction: column;
    gap: 12px;

    .tip-item {
      display: flex;
      align-items: center;
      gap: 12px;
      font-size: 13px;
      color: #606266;

      .tip-icon {
        color: #00b85c;
        font-size: 16px;
      }
    }
  }
}

.welcome-title {
  font-size: 24px;
  color: #303133;
  margin: 0 0 8px 0;
}

.link-text {
  color: #00b85c;
  cursor: pointer;
  font-size: 14px;

  &:hover {
    color: #85ce61;
    text-decoration: underline;
  }
}
</style>
