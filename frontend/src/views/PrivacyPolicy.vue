<template>
  <div class="static-page">
    <NavBar />
    <div class="page-container">
      <h1>隐私政策</h1>
      <p>更新日期：2026年5月25日</p>

      <div class="content-section">
        <h2>引言</h2>
        <p>
          LeafGO
          职达（以下简称"我们"）深知个人信息对您的重要性，我们将按照法律法规要求，采取相应安全保护措施，尽力保护您的个人信息安全可控。
        </p>
      </div>

      <div class="content-section">
        <h2>信息收集</h2>
        <p>我们收集的信息包括：账户注册信息、简历信息、求职偏好、使用记录等。</p>
      </div>

      <div class="content-section">
        <h2>信息使用</h2>
        <p>我们使用收集的信息用于：提供招聘服务、改善用户体验、发送通知、安全保障等。</p>
      </div>

      <div class="content-section">
        <h2>信息保护</h2>
        <p>我们采用业界领先的技术保护措施，确保您的个人信息安全。</p>
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
    font-size: 14px;
    color: #999;
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
    line-height: 1.8;
  }
}
</style>
