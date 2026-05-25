<template>
  <div class="home-page">
    <NavBar />

    <section class="search-section">
      <div class="container">
        <div class="search-group">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索职位、公司或关键词"
            size="large"
            class="search-input"
            @keyup.enter="handleSearch"
          />
          <el-button type="primary" size="large" @click="handleSearch"> 搜索 </el-button>
        </div>

        <div class="hot-keywords">
          <span class="keywords-label">热门搜索:</span>
          <el-tag
            v-for="keyword in hotKeywords"
            :key="keyword"
            class="keyword-tag"
            @click="searchKeyword = keyword"
          >
            {{ keyword }}
          </el-tag>
        </div>
      </div>
    </section>

    <section class="section">
      <div class="container">
        <el-carousel
          v-if="banners.length > 0"
          height="280px"
          :interval="5000"
          arrow="hover"
          indicator-position="outside"
        >
          <el-carousel-item v-for="banner in banners" :key="banner.id">
            <div class="banner-card" @click="handleBannerClick(banner)">
              <img
                :src="banner.imageUrl"
                :alt="banner.title"
                class="banner-image"
                @error="handleBannerImgError($event)"
              />
              <div class="banner-overlay">
                <h3>{{ banner.title }}</h3>
                <p>{{ banner.description }}</p>
              </div>
            </div>
          </el-carousel-item>
        </el-carousel>
        <el-skeleton v-else :rows="3" animated />
      </div>
    </section>

    <section class="section">
      <div class="container">
        <h2 class="section-title">精选职位</h2>

        <div class="jobs-grid" v-loading="loading">
          <template v-if="loading">
            <el-skeleton v-for="i in 6" :key="i" :rows="4" animated />
          </template>
          <template v-else>
            <div
              v-for="job in featuredJobs"
              :key="job.id"
              class="job-card"
              @click="goToJob(job.id)"
            >
              <div class="job-header">
                <h3 class="job-title">{{ job.title }}</h3>
                <div class="salary">{{ job.salary }}</div>
              </div>
              <div class="job-tags">
                <span v-for="tag in job.tags" :key="tag" class="tag">{{ tag }}</span>
              </div>
              <div class="company-info">
                <div class="company-name">
                  {{ typeof job.company === 'object' ? job.company.name : job.companyName || '' }}
                </div>
                <div class="company-detail">
                  {{ typeof job.company === 'object' ? job.company.industry : job.industry || '' }}
                  ·
                  {{ typeof job.company === 'object' ? job.company.scale : job.stage || '' }}
                </div>
              </div>
            </div>
          </template>
        </div>
        <el-empty v-if="!loading && featuredJobs.length === 0" description="暂无精选职位" />
      </div>
    </section>

    <AppFooter />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import NavBar from '@/components/NavBar.vue'
import AppFooter from '@/components/AppFooter.vue'
import { bannerApi, type Banner } from '@/api/banner'
import { jobApi } from '@/api/job'
import { configApi } from '@/api/config'
import type { Job } from '@/types'

defineOptions({
  name: 'HomePage',
})

const router = useRouter()
const searchKeyword = ref('')
const loading = ref(false)

const hotKeywords = ref<string[]>([])

const banners = ref<Banner[]>([])
const featuredJobs = ref<Job[]>([])

const fetchHotKeywords = async (): Promise<void> => {
  try {
    const data = await configApi.getHotKeywords()
    hotKeywords.value = data
  } catch (error) {
    console.error('Failed to fetch hot keywords:', error)
  }
}

const fetchBanners = async (): Promise<void> => {
  try {
    const data = await bannerApi.getBanners()
    banners.value = data
  } catch (error) {
    console.error('Failed to fetch banners:', error)
  }
}

const fetchFeaturedJobs = async (): Promise<void> => {
  try {
    loading.value = true
    const result = await jobApi.getJobList({ page: 1, size: 6 })
    featuredJobs.value = result.content.map((job) => ({
      ...job,
      tags: typeof job.tags === 'string' ? (job.tags ? job.tags.split(',') : []) : (job.tags || []),
    }))
  } catch (error) {
    console.error('获取精选职位失败:', error)
  } finally {
    loading.value = false
  }
}

const handleSearch = (): void => {
  if (searchKeyword.value.trim()) {
    router.push(`/jobs?keyword=${encodeURIComponent(searchKeyword.value)}`)
  }
}

const handleBannerImgError = (e: Event): void => {
  const img = e.target as HTMLImageElement
  img.style.display = 'none'
  const parent = img.parentElement
  if (parent) {
    parent.classList.add('banner-img-error')
  }
}

const handleBannerClick = (banner: Banner): void => {
  if (banner.linkUrl) {
    if (banner.linkUrl.startsWith('http')) {
      window.open(banner.linkUrl, '_blank')
    } else {
      router.push(banner.linkUrl)
    }
  }
}

const goToJob = (jobId: number): void => {
  router.push(`/jobs/${jobId}`)
}

onMounted(() => {
  fetchHotKeywords()
  fetchBanners()
  fetchFeaturedJobs()
})
</script>

<style scoped lang="scss">
@use '@/assets/styles/variables.scss' as *;

.home-page {
  min-height: 100vh;
  background: linear-gradient(180deg, rgba($primary-deep, 0.04) 0%, #ffffff 25%);
}

.search-section {
  padding: 28px 0 $spacing-xl;
}

.search-group {
  display: flex;
  max-width: 840px;
  margin: 0 auto;
  border-radius: 48px;
  overflow: hidden;
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.1);

  .search-input {
    flex: 1;

    :deep(.el-input__wrapper) {
      background-color: $canvas;
      border: none;
      border-radius: 48px 0 0 48px;
      box-shadow: none;
      padding: 0 $spacing-2xl;
      height: 52px;
    }

    :deep(.el-input__inner) {
      font-size: $body-sm;
    }
  }

  .el-button--primary {
    border-radius: 0 48px 48px 0;
    padding: 0 $spacing-4xl;
    font-size: $body-sm;
    height: 52px;
    background-color: $primary;
    border-color: $primary;

    &:hover {
      background-color: $primary-soft;
      border-color: $primary-soft;
    }
  }
}

.hot-keywords {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: $spacing-md;
  margin-top: $spacing-xl;
  flex-wrap: wrap;

  .keywords-label {
    font-size: $body-sm;
    color: $mute;
    margin-right: $spacing-sm;
    min-width: 70px;
  }

  .keyword-tag {
    background-color: #ffffff;
    border: none;
    color: $body;
    cursor: pointer;
    font-size: $body-sm;
    padding: 6px $spacing-xl;
    border-radius: 20px;
    transition: all 0.2s;

    &:hover {
      background-color: $primary;
      color: #fff;
    }
  }
}

.section {
  padding: $spacing-3xl 0;

  &:first-of-type {
    padding-top: $spacing-lg;
  }

  :deep(.el-carousel) {
    border-radius: $rounded-xl;
    overflow: hidden;
  }

  :deep(.el-carousel__indicators) {
    margin-top: 12px;
  }
}

.section-title {
  font-size: $body-lg;
  color: $ink;
  margin: 0 0 $spacing-lg 0;
  letter-spacing: 0.3px;
  font-weight: 400;
}

.banner-card {
  position: relative;
  height: 100%;
  border-radius: $rounded-xl;
  overflow: hidden;
  cursor: pointer;

  &.banner-img-error {
    background: #000;
  }
}

.banner-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.banner-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 16px 24px;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.4), transparent);

  h3 {
    font-size: 20px;
    color: white;
    margin: 0 0 4px 0;
    font-weight: 500;
  }

  p {
    font-size: 13px;
    color: rgba(255, 255, 255, 0.85);
    margin: 0;
  }
}

.jobs-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: $spacing-sm;
  min-height: 200px;
}

.job-card {
  background-color: $canvas;
  border-radius: $rounded-xl;
  padding: $spacing-xl;
  cursor: pointer;
  transition: box-shadow $transition-base;
  border: 2px solid #ececec;
  min-width: 0;

  &:hover {
    box-shadow: 0 12px 32px rgba(0, 0, 0, 0.1);
  }
}

.job-header {
  display: flex;
  align-items: flex-start;
  gap: $spacing-sm;
  margin-bottom: $spacing-sm;
  min-width: 0;
}

.job-title {
  flex: 1;
  min-width: 0;
  font-size: $body-md;
  color: $ink;
  margin: 0;
  line-height: 1.5;
  font-weight: 400;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.salary {
  flex-shrink: 0;
  font-size: $body-md;
  color: #ff6b6b;
  white-space: nowrap;
}

.job-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-bottom: $spacing-lg;
}

.tag {
  padding: 4px 10px;
  background-color: #f0f7f4;
  border-radius: $rounded-sm;
  font-size: $caption;
  color: $body;
  border: none;
}

.company-info {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: $spacing-xs;
  padding-top: $spacing-md;
  border-top: 1px solid #f0f0f0;
}

.company-name {
  font-size: $caption;
  color: $ink;
}

.company-detail {
  font-size: 11px;
  color: $mute;
}

.load-more {
  text-align: center;
  margin-top: $spacing-2xl;

  .el-button {
    padding: $spacing-md $spacing-5xl;
    font-size: $body-sm;
    border-radius: $rounded-pill;
    border-color: $hairline-soft;
    color: $body;
    transition: all $transition-fast;

    &:hover {
      border-color: $primary;
      color: $primary;
      background-color: rgba($primary, 0.04);
    }
  }
}
</style>
