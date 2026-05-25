<template>
  <div class="seeker-companies">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-input v-model="searchKeyword" placeholder="搜索公司" size="large">
          <template #append>
            <el-button type="primary">搜索</el-button>
          </template>
        </el-input>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="section">
      <el-col :span="6" v-for="company in companyList" :key="company.id">
        <el-card
          shadow="hover"
          class="company-card"
          @click="$router.push(`/seeker/companies/${company.id}`)"
        >
          <div class="company-name">{{ company.name }}</div>
          <div class="company-meta">{{ company.industry }} · {{ company.scale }}</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
defineOptions({
  name: 'SeekerCompanies',
})
import { ref } from 'vue'

const searchKeyword = ref('')

const companyList = ref([
  { id: 1, name: '科技公司', industry: '互联网', scale: '100-500人' },
  { id: 2, name: '互联网公司', industry: '电子商务', scale: '500-1000人' },
  { id: 3, name: '金融公司', industry: '金融', scale: '1000人以上' },
  { id: 4, name: '教育公司', industry: '教育', scale: '100-500人' },
  { id: 5, name: '医疗公司', industry: '医疗健康', scale: '500-1000人' },
  { id: 6, name: '制造业公司', industry: '制造业', scale: '1000人以上' },
  { id: 7, name: '咨询公司', industry: '咨询', scale: '100-500人' },
  { id: 8, name: '传媒公司', industry: '传媒', scale: '100-500人' },
])
</script>

<style scoped lang="scss">
.seeker-companies {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.section {
  margin-top: 20px;
}

.company-card {
  margin-bottom: 20px;
  cursor: pointer;

  .company-name {
    font-size: 16px;
    font-weight: bold;
    margin-bottom: 8px;
  }

  .company-meta {
    color: #666;
    font-size: 12px;
  }
}
</style>
