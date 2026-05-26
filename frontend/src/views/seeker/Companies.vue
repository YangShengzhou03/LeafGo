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
          <div class="company-header">
            <div class="company-logo">{{ company.name?.charAt(0) }}</div>
            <div class="company-info">
              <div class="company-name">
                {{ company.name }}
                <el-icon v-if="company.verified" class="verified-icon">
                  <CircleCheck />
                </el-icon>
              </div>
              <div class="company-meta">{{ company.industry }} · {{ company.scale }}</div>
            </div>
          </div>
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
import { CircleCheck } from '@element-plus/icons-vue'

const searchKeyword = ref('')

const companyList = ref([
  { id: 1, name: '科技公司', industry: '互联网', scale: '100-500人', verified: true },
  { id: 2, name: '互联网公司', industry: '电子商务', scale: '500-1000人', verified: true },
  { id: 3, name: '金融公司', industry: '金融', scale: '1000人以上', verified: false },
  { id: 4, name: '教育公司', industry: '教育', scale: '100-500人', verified: true },
  { id: 5, name: '医疗公司', industry: '医疗健康', scale: '500-1000人', verified: false },
  { id: 6, name: '制造业公司', industry: '制造业', scale: '1000人以上', verified: true },
  { id: 7, name: '咨询公司', industry: '咨询', scale: '100-500人', verified: false },
  { id: 8, name: '传媒公司', industry: '传媒', scale: '100-500人', verified: true },
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

  .company-header {
    display: flex;
    align-items: center;
    gap: 12px;
  }

  .company-logo {
    width: 48px;
    height: 48px;
    border-radius: 8px;
    background: linear-gradient(135deg, #e8f5e9 0%, #4caf50 100%);
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 20px;
    font-weight: 500;
    color: white;
    flex-shrink: 0;
  }

  .company-info {
    flex: 1;
    min-width: 0;
  }

  .company-name {
    font-size: 15px;
    font-weight: 500;
    color: #222;
    margin-bottom: 4px;
    display: flex;
    align-items: center;
    gap: 6px;

    .verified-icon {
      color: #4caf50;
      font-size: 14px;
    }
  }

  .company-meta {
    color: #666;
    font-size: 13px;
  }
}
</style>
