<template>
  <div class="seeker-company-detail">
    <el-card>
      <div class="company-header">
        <h1>{{ company.name }}</h1>
        <div class="company-meta">{{ company.industry }} · {{ company.scale }}</div>
      </div>

      <el-divider />

      <div class="company-info">
        <h3>公司简介</h3>
        <p>{{ company.description }}</p>
      </div>

      <el-divider />

      <div class="company-jobs">
        <h3>在招职位</h3>

        <el-row :gutter="20">
          <el-col :span="8" v-for="job in jobList" :key="job.id">
            <el-card
              shadow="hover"
              class="job-card"
              @click="$router.push(`/seeker/jobs/${job.id}`)"
            >
              <div class="job-title">{{ job.title }}</div>
              <div class="job-salary">{{ job.salary }}</div>
              <div class="job-meta">{{ job.location }} · {{ job.experience }}</div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const company = ref({
  id: 1,
  name: '科技公司',
  industry: '互联网',
  scale: '100-500人',
  description: '这是一家专注于互联网技术开发的科技公司，致力于为客户提供优质的技术解决方案。',
})

const jobList = ref([
  { id: 1, title: '前端开发工程师', salary: '15-25K', location: '南昌', experience: '3-5年' },
  { id: 2, title: 'Java开发工程师', salary: '18-30K', location: '南昌', experience: '3-5年' },
  { id: 3, title: '产品经理', salary: '20-35K', location: '南昌', experience: '5-10年' },
])
</script>

<style scoped lang="scss">
.seeker-company-detail {
  min-height: 100vh;

  background-color: #f5f5f5;
}

.company-header {
  h1 {
    font-size: 24px;

    margin-bottom: 10px;
  }

  .company-meta {
    color: #666;

    font-size: 14px;
  }
}

.company-info {
  h3 {
    font-size: 18px;

    margin-bottom: 15px;
  }

  p {
    color: #666;

    line-height: 1.6;
  }
}

.company-jobs {
  h3 {
    font-size: 18px;

    margin-bottom: 15px;
  }
}

.job-card {
  margin-bottom: 20px;

  cursor: pointer;

  .job-title {
    font-size: 16px;

    font-weight: bold;

    margin-bottom: 8px;
  }

  .job-salary {
    color: #1a7f64;

    font-size: 14px;

    margin-bottom: 8px;
  }

  .job-meta {
    color: #666;

    font-size: 12px;
  }
}
</style>
