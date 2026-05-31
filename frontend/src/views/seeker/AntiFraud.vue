<template>
  <div class="static-page">
    <SeekerNavBar />
    <div class="page-container">
      <h1>防骗指南</h1>
      <p>保护您的求职安全，警惕各类招聘诈骗</p>

      <div class="content-section">
        <h2>
          <el-icon><CircleCheck /></el-icon> 防骗技巧
        </h2>
        <div class="tips-grid">
          <div class="tip-card">
            <h3>核实企业资质</h3>
            <p>面试前通过工商局网站查询企业注册信息，确认企业真实性。</p>
          </div>
          <div class="tip-card">
            <h3>拒绝缴纳费用</h3>
            <p>正规企业不会收取任何费用，遇到收费要求请立即拒绝并举报。</p>
          </div>
          <div class="tip-card">
            <h3>保护个人信息</h3>
            <p>不要轻易提供身份证原件、银行卡密码等敏感信息。</p>
          </div>
          <div class="tip-card">
            <h3>选择正规渠道</h3>
            <p>通过LeafGO等正规招聘平台求职，避免私下交易。</p>
          </div>
        </div>
      </div>

      <div class="content-section">
        <h2>
          <el-icon><Phone /></el-icon> 举报渠道
        </h2>
        <p>如发现可疑招聘信息，请立即举报：</p>
        <div class="contact-info">
          <div class="contact-item">
            <span class="label">举报邮箱：</span>
            <span class="value">jubao@leafgo.com</span>
          </div>
          <div class="contact-item">
            <span class="label">客服热线：</span>
            <span class="value">400-065-5799</span>
          </div>
          <div class="contact-item">
            <span class="label">报警电话：</span>
            <span class="value">110</span>
          </div>
        </div>
      </div>
    </div>
    <AppFooter />
  </div>
</template>

<script setup lang="ts">
import { CircleCheck, Phone } from '@element-plus/icons-vue'
import SeekerNavBar from '@/components/SeekerNavBar.vue'
import AppFooter from '@/components/AppFooter.vue'
</script>

<style scoped lang="scss">
@use '@/assets/styles/variables.scss' as *;

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
    margin-bottom: 20px;
    display: flex;
    align-items: center;
    gap: 8px;

    .el-icon {
      color: $primary;
    }
  }

  > p {
    font-size: 14px;
    color: #666;
    margin-bottom: 16px;
  }
}

.warning-list {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.warning-item {
  padding: 16px;
  background: #fff7e6;
  border-radius: 8px;
  border-left: 3px solid #faad14;

  h3 {
    font-size: 16px;
    color: #d48806;
    margin-bottom: 8px;
  }

  p {
    font-size: 14px;
    color: #666;
    margin: 0;
    line-height: 1.6;
  }
}

.tips-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.tip-card {
  padding: 20px;
  background: #f6ffed;
  border-radius: 8px;
  border: 1px solid #b7eb8f;

  h3 {
    font-size: 16px;
    color: #389e0d;
    margin-bottom: 8px;
  }

  p {
    font-size: 14px;
    color: #666;
    margin: 0;
    line-height: 1.6;
  }
}

.contact-info {
  display: flex;
  gap: 32px;
}

.contact-item {
  .label {
    font-size: 14px;
    color: #999;
  }

  .value {
    font-size: 16px;
    color: $primary;
    font-weight: 500;
    margin-left: 8px;
  }
}
</style>
