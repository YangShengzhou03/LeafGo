<template>
  <div class="campus-page">
    <SeekerNavBar />

    <div class="page-container">
      <div class="campus-tabs">
        <span
          v-for="tab in tabs"
          :key="tab.value"
          class="tab-item"
          :class="{ active: activeTab === tab.value }"
          @click="handleTabChange(tab.value)"
        >
          {{ tab.label }}
        </span>
      </div>

      <div class="search-filter-bar">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索职位、公司"
          class="search-input"
          @keyup.enter="handleSearch"
          clearable
        />
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <div class="filter-divider"></div>
        <div class="filter-group">
          <span class="filter-label">按专业筛选</span>
          <el-select
            v-model="filters.major"
            placeholder="不限"
            class="filter-select"
            @change="handleFilterChange"
          >
            <el-option label="不限" value="" />
            <el-option label="计算机" value="计算机" />
            <el-option label="电子信息" value="电子信息" />
            <el-option label="机械工程" value="机械工程" />
            <el-option label="工商管理" value="工商管理" />
          </el-select>
        </div>
        <div class="filter-group">
          <span class="filter-label">按职类筛选</span>
          <el-select
            v-model="filters.category"
            placeholder="不限"
            class="filter-select"
            @change="handleFilterChange"
          >
            <el-option label="不限" value="" />
            <el-option label="技术" value="技术" />
            <el-option label="产品" value="产品" />
            <el-option label="设计" value="设计" />
            <el-option label="运营" value="运营" />
            <el-option label="销售" value="销售" />
          </el-select>
        </div>
      </div>

      <div class="major-tags">
        <span
          v-for="major in majors"
          :key="major"
          class="major-tag"
          :class="{ active: selectedMajor === major }"
          @click="handleMajorClick(major)"
        >
          {{ major }}
        </span>
      </div>

      <div class="enterprise-section">
        <div class="section-header">
          <h2 class="section-title">企业招聘</h2>
          <router-link to="/companies" class="view-more">查看更多</router-link>
        </div>
        <div class="enterprise-grid" v-loading="enterpriseLoading">
          <div
            v-for="enterprise in enterprises"
            :key="enterprise.id"
            class="enterprise-card"
            @click="goToCompany(enterprise.id)"
          >
            <div class="enterprise-header">
              <div class="enterprise-logo">{{ enterprise.name?.charAt(0) }}</div>
              <div class="enterprise-info">
                <div class="enterprise-name">
                  {{ enterprise.name }}
                  <el-icon v-if="enterprise.verified" class="verified-icon">
                    <CircleCheck />
                  </el-icon>
                </div>
                <div class="enterprise-meta">
                  <span>{{ enterprise.scale }}</span>
                  <span class="sep">·</span>
                  <span>{{ enterprise.industry }}</span>
                </div>
              </div>
            </div>
            <div class="enterprise-location" v-if="getLocation(enterprise)">
              <el-icon><Location /></el-icon>
              {{ getLocation(enterprise) }}
            </div>
          </div>
        </div>
        <el-empty
          v-if="!enterpriseLoading && enterprises.length === 0"
          description="暂无企业招聘"
        />
      </div>
    </div>

    <AppFooter />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { CircleCheck, Location } from '@element-plus/icons-vue'
import SeekerNavBar from '@/components/SeekerNavBar.vue'
import AppFooter from '@/components/AppFooter.vue'
import { companyApi } from '@/api/company'
import type { Company } from '@/types'

defineOptions({
  name: 'CampusPage',
})

const router = useRouter()

const searchKeyword = ref('')
const activeTab = ref('campus')
const selectedMajor = ref('')

const enterpriseLoading = ref(false)

const tabs = [
  { label: '校招', value: 'campus' },
  { label: '实习', value: 'intern' },
  { label: '社招', value: 'social' },
  { label: '兼职', value: 'parttime' },
]

const majors = [
  '农学',
  '医学',
  '教育学',
  '文学',
  '艺术学',
  '工学',
  '法学',
  '历史学',
  '哲学',
  '理学',
  '经济学',
  '管理学',
]

const filters = reactive({
  major: '',
  category: '',
})

const enterprises = ref<Company[]>([])

const fetchEnterprises = async (): Promise<void> => {
  try {
    enterpriseLoading.value = true
    const result = await companyApi.getCompanyList({ page: 1, size: 4 })
    enterprises.value = result.content
  } catch (error) {
    console.error('Failed to fetch enterprises:', error)
  } finally {
    enterpriseLoading.value = false
  }
}

const getLocation = (company: { city?: string; district?: string }): string => {
  if (company.city && company.district) {
    return `${company.city}·${company.district}`
  }
  if (company.city) {
    return company.city
  }
  return '南昌'
}

const handleTabChange = (tab: string): void => {
  activeTab.value = tab
}

const handleSearch = (): void => {}

const handleFilterChange = (): void => {}

const handleMajorClick = (major: string): void => {
  if (selectedMajor.value === major) {
    selectedMajor.value = ''
  } else {
    selectedMajor.value = major
  }
}

const goToCompany = (companyId: number): void => {
  router.push(`/companies/${companyId}`)
}

onMounted(() => {
  fetchEnterprises()
})
</script>

<style scoped lang="scss">
@use '@/assets/styles/variables.scss' as *;

.campus-page {
  min-height: 100vh;
  background: linear-gradient(180deg, rgba($primary-deep, 0.04) 0%, #f7f7f7 25%);
}

.page-container {
  max-width: 1246px;
  margin: 0 auto;
  padding: 16px 28px;
}

.campus-tabs {
  display: flex;
  gap: 0;
  background: white;
  border-radius: 8px;
  margin-bottom: 16px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.tab-item {
  padding: 14px 32px;
  font-size: 15px;
  color: #666;
  cursor: pointer;
  transition: all 0.2s;
  border-bottom: 2px solid transparent;

  &:hover {
    color: $primary;
    background: rgba($primary, 0.02);
  }

  &.active {
    color: $primary;
    font-weight: 500;
    border-bottom-color: $primary;
    background: rgba($primary, 0.05);
  }
}

.search-filter-bar {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px 20px;
  background: white;
  border-radius: 8px;
  margin-bottom: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.search-input {
  width: 300px;
}

.filter-divider {
  width: 1px;
  height: 24px;
  background: #e0e0e0;
  margin: 0 8px;
}

.filter-group {
  display: flex;
  align-items: center;
  gap: 8px;
}

.filter-label {
  font-size: 14px;
  color: #333;
  font-weight: 500;
  white-space: nowrap;
}

.filter-select {
  width: 120px;
}

.major-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 24px;
}

.major-tag {
  font-size: 14px;
  color: #666;
  padding: 6px 16px;
  background: white;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.2s;

  &:hover {
    color: $primary;
    background: rgba($primary, 0.05);
  }

  &.active {
    color: white;
    background: $primary;
  }
}

.enterprise-section {
  margin-bottom: 24px;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
}

.section-title {
  font-size: 18px;
  color: #222;
  font-weight: 500;
  margin: 0;
}

.view-more {
  font-size: 14px;
  color: $primary;
  text-decoration: none;

  &:hover {
    text-decoration: underline;
  }
}

.enterprise-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  min-height: 120px;
}

.enterprise-card {
  background: white;
  padding: 16px;
  border-radius: 6px;
  cursor: pointer;
  border: 1px solid #f0f0f0;
}

.enterprise-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 8px;
}

.enterprise-logo {
  width: 40px;
  height: 40px;
  border-radius: 6px;
  background: linear-gradient(135deg, $primary-soft 0%, $primary 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  font-weight: 500;
  color: white;
  flex-shrink: 0;
}

.enterprise-info {
  flex: 1;
  min-width: 0;
}

.enterprise-name {
  font-size: 15px;
  color: #222;
  font-weight: 500;
  margin-bottom: 4px;
  display: flex;
  align-items: center;
  gap: 6px;

  .verified-icon {
    color: $primary;
    font-size: 14px;
  }
}

.enterprise-meta {
  font-size: 12px;
  color: #666;

  .sep {
    margin: 0 3px;
    color: #ccc;
  }
}

.enterprise-location {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: #999;

  .el-icon {
    color: $primary;
    font-size: 12px;
  }
}
</style>
