<template>
  <div class="seeker-jobs">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-input v-model="searchKeyword" placeholder="搜索职位" size="large">
          <template #append>
            <el-button type="primary">搜索</el-button>
          </template>
        </el-input>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="section">
      <el-col :span="8" v-for="job in jobList" :key="job.id">
        <el-card shadow="hover" class="job-card" @click="$router.push(`/seeker/jobs/${job.id}`)">
          <div class="job-title">{{ job.title }}</div>
          <div class="job-salary">{{ job.salary }}</div>
          <div class="job-meta">{{ job.location }} · {{ job.experience }}</div>
          <div class="company-name">{{ job.company }}</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
defineOptions({
  name: 'SeekerJobs',
})
import { ref } from 'vue'

const searchKeyword = ref('')

const jobList = ref([
  {
    id: 1,
    title: '前端开发工程师',
    salary: '15-25K',
    location: '南昌',
    experience: '3-5年',
    company: '科技公司',
  },
  {
    id: 2,
    title: 'Java开发工程师',
    salary: '18-30K',
    location: '南昌',
    experience: '3-5年',
    company: '互联网公司',
  },
  {
    id: 3,
    title: '产品经理',
    salary: '20-35K',
    location: '南昌',
    experience: '5-10年',
    company: '科技公司',
  },
  {
    id: 4,
    title: 'UI设计师',
    salary: '12-20K',
    location: '南昌',
    experience: '1-3年',
    company: '设计公司',
  },
  {
    id: 5,
    title: '测试工程师',
    salary: '10-18K',
    location: '南昌',
    experience: '1-3年',
    company: '科技公司',
  },
  {
    id: 6,
    title: '运维工程师',
    salary: '12-20K',
    location: '南昌',
    experience: '3-5年',
    company: '互联网公司',
  },
])
</script>

<style scoped lang="scss">
.seeker-jobs {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.section {
  margin-top: 20px;
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
    margin-bottom: 8px;
  }

  .company-name {
    color: #999;
    font-size: 12px;
  }
}
</style>
