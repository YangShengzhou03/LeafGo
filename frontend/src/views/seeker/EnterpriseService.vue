<template>
  <div class="static-page">
    <SeekerNavBar />
    <div class="page-container">
      <h1>企业服务</h1>
      <p>LeafGO 职达为企业提供全方位的招聘解决方案</p>

      <div class="service-grid">
        <div class="service-card">
          <h3>职位发布</h3>
          <p>快速发布职位，精准触达求职者</p>
        </div>
        <div class="service-card">
          <h3>人才搜索</h3>
          <p>海量简历库，智能匹配候选人</p>
        </div>
        <div class="service-card">
          <h3>招聘管理</h3>
          <p>一站式招聘管理，提升效率</p>
        </div>
        <div class="service-card">
          <h3>品牌推广</h3>
          <p>企业品牌展示，吸引优秀人才</p>
        </div>
      </div>
    </div>
    <AppFooter />
  </div>
</template>

<script setup lang="ts">
import SeekerNavBar from '@/components/SeekerNavBar.vue'
import AppFooter from '@/components/AppFooter.vue'
</script>

<style scoped lang="scss">
.static-page {
  min-height: 100vh;
  background: #f5f5f5;
}

.page-container {
  max-width: 1246px;
  margin: 0 auto;
  padding: 40px 24px;

  h1 {
    font-size: 28px;
    color: #222;
    margin-bottom: 16px;
  }

  p {
    font-size: 16px;
    color: #666;
    margin-bottom: 32px;
  }
}

.service-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.service-card {
  background: white;
  padding: 24px;
  border-radius: 8px;

  h3 {
    font-size: 18px;
    color: #222;
    margin-bottom: 12px;
  }

  p {
    font-size: 14px;
    color: #666;
    margin: 0;
  }
}
</style>
