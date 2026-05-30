<template>
  <div class="companies-page">
    <SeekerNavBar />

    <div class="page-container">
      <div class="search-filter-bar">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索职职位、公司名称"
          class="search-input"
          @keyup.enter="handleSearch"
          clearable
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <div class="filter-divider"></div>
        <div class="filter-group">
          <span class="filter-label">公司地点:</span>
          <el-select
            v-model="filters.city"
            placeholder="南昌"
            class="filter-select"
            @change="handleFilterChange"
          >
            <el-option v-for="city in cities" :key="city" :label="city" :value="city" />
          </el-select>
        </div>
        <div class="filter-group">
          <span class="filter-label">行业类型:</span>
          <el-select
            v-model="filters.industry"
            placeholder="不限"
            class="filter-select"
            @change="handleFilterChange"
          >
            <el-option label="不限" value="" />
            <el-option label="电子商务" value="电子商务" />
            <el-option label="游戏" value="游戏" />
            <el-option label="社交网络与媒体" value="社交网络与媒体" />
            <el-option label="广告营销" value="广告营销" />
            <el-option label="大数据" value="大数据" />
            <el-option label="医疗健康" value="医疗健康" />
            <el-option label="生活服务(O2O)" value="生活服务(O2O)" />
            <el-option label="旅游" value="旅游" />
            <el-option label="在线教育" value="在线教育" />
            <el-option label="企业服务" value="企业服务" />
            <el-option label="信息安全" value="信息安全" />
            <el-option label="智能硬件" value="智能硬件" />
            <el-option label="移动互联网" value="移动互联网" />
            <el-option label="互联网" value="互联网" />
            <el-option label="计算机软件" value="计算机软件" />
            <el-option label="通信/网络设备" value="通信/网络设备" />
            <el-option label="其他行业" value="其他行业" />
          </el-select>
        </div>
        <el-button text @click="clearFilters">清空筛选</el-button>
      </div>

      <div class="main-content" v-loading="loading">
        <div v-if="companies.length === 0 && !loading" class="empty-state">
          <el-empty description="暂无符合条条件的公司" />
        </div>
        <div v-else class="companies-grid">
          <template v-if="loading">
            <el-skeleton v-for="(_, i) in skeletonItems" :key="i" :rows="4" animated />
          </template>
          <template v-else>
            <div
              v-for="company in companies"
              :key="company.id"
              class="company-card"
              @click="goToCompany(company.id)"
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
                  <div class="company-meta">
                    <span>{{ company.scale }}</span>
                    <span class="sep">·</span>
                    <span>{{ company.industry }}</span>
                  </div>
                </div>
              </div>
              <div class="company-location" v-if="getLocation(company)">
                <el-icon><Location /></el-icon>
                {{ getLocation(company) }}
              </div>
            </div>
          </template>
        </div>

        <div class="pagination-container" v-if="total > pageSize">
          <el-pagination
            v-model:current-page="currentPage"
            :page-size="pageSize"
            :total="total"
            layout="prev, pager, next"
            @current-change="handlePageChange"
          />
        </div>
      </div>
    </div>

    <AppFooter />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Search, CircleCheck, Location } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import SeekerNavBar from '@/components/SeekerNavBar.vue'
import AppFooter from '@/components/AppFooter.vue'
import { companyApi } from '@/api/company'
import type { Company } from '@/types'

defineOptions({
  name: 'CompaniesList',
})

const router = useRouter()

const loading = ref(false)
const searchKeyword = ref('')
const currentPage = ref(1)
const pageSize = ref(12)
const total = ref(0)

const cities = [
  '全国',
  '南昌',
  '北京',
  '上海',
  '广州',
  '深圳',
  '杭州',
  '天津',
  '西安',
  '苏州',
  '武汉',
  '厦门',
  '长沙',
  '成都',
  '郑州',
  '重庆',
]

const filters = reactive({
  city: '南昌',
  industry: '',
})

const companies = ref<Company[]>([])

const skeletonItems: number[] = [1, 2, 3, 4, 5, 6, 7, 8]

const fetchCompanies = async (): Promise<void> => {
  try {
    loading.value = true
    const params = {
      page: currentPage.value - 1,
      size: pageSize.value,
      keyword: searchKeyword.value || undefined,
      industry: filters.industry || undefined,
    }

    const result = await companyApi.getCompanyList(params)
    companies.value = result.content
    total.value = result.totalElements
  } catch (error) {
    console.error('Failed to fetch companies:', error)
    ElMessage.error('获取公司列表失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = (): void => {
  currentPage.value = 1
  fetchCompanies()
}

const handleFilterChange = (): void => {
  currentPage.value = 1
  fetchCompanies()
}

const handlePageChange = (page: number): void => {
  currentPage.value = page
  fetchCompanies()
}

const clearFilters = (): void => {
  filters.city = '南昌'
  filters.industry = ''
  searchKeyword.value = ''
  currentPage.value = 1
  fetchCompanies()
}

const goToCompany = (companyId: number): void => {
  router.push(`/companies/${companyId}`)
}

const getLocation = (company: Company): string => {
  if (company.city && company.district) {
    return `${company.city}·${company.district}`
  }
  if (company.city) {
    return company.city
  }
  return ''
}

onMounted(() => {
  fetchCompanies()
})
</script>

<style scoped lang="scss">
@use '@/assets/styles/variables.scss' as *;

.companies-page {
  min-height: 100vh;
  background: linear-gradient(180deg, rgba($primary-deep, 0.04) 0%, #f7f7f7 25%);
}

.page-container {
  max-width: 1246px;
  margin: 0 auto;
  padding: 16px 28px;
}

.search-filter-bar {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px 20px;
  background: white;
  margin-bottom: 16px;
  border-radius: 8px;
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

.main-content {
  min-height: 400px;
}

.empty-state {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 400px;
}

.companies-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.company-card {
  background: white;
  padding: 16px;
  cursor: pointer;
  border-radius: 6px;
  border: 1px solid #f0f0f0;
}

.company-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 8px;
}

.company-logo {
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

.company-info {
  flex: 1;
  min-width: 0;
}

.company-name {
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

.company-meta {
  font-size: 12px;
  color: #666;

  .sep {
    margin: 0 3px;
    color: #ccc;
  }
}

.company-location {
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

.pagination-container {
  display: flex;
  justify-content: center;
  padding: 32px 0;
}
</style>
