<template>
  <div class="admin-home">
    <el-row :gutter="20">
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon" style="background: #409eff">
            <el-icon size="24"><User /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.userCount }}</div>
            <div class="stat-label">注册用户</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon" style="background: #00b85c">
            <el-icon size="24"><OfficeBuilding /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.companyCount }}</div>
            <div class="stat-label">入驻企业</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon" style="background: #e6a23c">
            <el-icon size="24"><Briefcase /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.jobCount }}</div>
            <div class="stat-label">发布职位</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon" style="background: #f56c6c">
            <el-icon size="24"><Document /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.applicationCount }}</div>
            <div class="stat-label">投递记录</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mt-20">
      <el-col :span="12">
        <div class="chart-card">
          <h3>用户增长趋势</h3>
          <div class="chart-placeholder">
            <el-icon size="48" color="#dcdfe6"><TrendCharts /></el-icon>
            <p>图表区域</p>
          </div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="chart-card">
          <h3>热门行业分布</h3>
          <div class="chart-placeholder">
            <el-icon size="48" color="#dcdfe6"><PieChart /></el-icon>
            <p>图表区域</p>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { reactive, onMounted } from 'vue'
import {
  User,
  OfficeBuilding,
  Briefcase,
  Document,
  TrendCharts,
  PieChart,
} from '@element-plus/icons-vue'
import { adminApi } from '@/api/admin'

defineOptions({
  name: 'AdminHome',
})

const stats = reactive({
  userCount: 0,
  companyCount: 0,
  jobCount: 0,
  applicationCount: 0,
})

const fetchStats = async (): Promise<void> => {
  try {
    const data = await adminApi.getStats()
    Object.assign(stats, data)
  } catch {
    // 加载失败时使用默认值
  }
}

onMounted(() => {
  fetchStats()
})
</script>

<style scoped lang="scss">
.admin-home {
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

  .chart-card {
    background: #fff;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);

    h3 {
      margin: 0 0 16px 0;
      font-size: 16px;
      font-weight: 600;
    }

    .chart-placeholder {
      height: 300px;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      background: #f5f7fa;
      border-radius: 8px;

      p {
        margin-top: 12px;
        color: #909399;
        font-size: 14px;
      }
    }
  }
}
</style>
