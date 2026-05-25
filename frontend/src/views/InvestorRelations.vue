<template>
  <div class="static-page">
    <NavBar />
    <div class="page-container">
      <h1>投资者关系</h1>
      <p>LeafGO 职达致力于为投资者提供透明、及时的信息披露</p>

      <div class="content-section">
        <h2>公司概况</h2>
        <p>LeafGO 职达是中国领先的智能招聘平台，致力于为求职者和企业提供高效、精准的招聘服务。</p>
      </div>

      <div class="content-section">
        <h2>财务报告</h2>
        <p>定期披露公司财务状况，确保信息透明公开。</p>
      </div>
    </div>
    <AppFooter />
  </div>
</template>

<script setup lang="ts">
import NavBar from '@/components/NavBar.vue'
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

  > p {
    font-size: 16px;
    color: #666;
    margin-bottom: 32px;
  }
}

.content-section {
  background: white;
  padding: 24px;
  border-radius: 8px;
  margin-bottom: 16px;

  h2 {
    font-size: 20px;
    color: #222;
    margin-bottom: 12px;
  }

  p {
    font-size: 14px;
    color: #666;
    margin: 0;
    line-height: 1.6;
  }
}
</style>
