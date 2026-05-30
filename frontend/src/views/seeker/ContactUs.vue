<template>
  <div class="static-page">
    <SeekerNavBar />
    <div class="page-container">
      <h1>联系我们</h1>

      <div class="contact-grid">
        <div class="contact-card">
          <h3>企业服务热线</h3>
          <p class="phone">400 065 5799</p>
          <p class="time">工作时间：8:00 - 22:00</p>
          <p class="time">休息时间：8:00 - 22:00</p>
        </div>

        <div class="contact-card">
          <h3>老年人直连热线</h3>
          <p class="phone">400 661 6030</p>
          <p class="time">工作时间：8:00 - 22:00</p>
          <p class="time">休息时间：8:00 - 22:00</p>
        </div>

        <div class="contact-card">
          <h3>客服邮箱</h3>
          <p class="email">service@leafgo.com</p>
          <p class="desc">24小时内回复</p>
        </div>

        <div class="contact-card">
          <h3>举报邮箱</h3>
          <p class="email">jubao@leafgo.com</p>
          <p class="desc">违法和不良信息举报</p>
        </div>
      </div>

      <div class="company-info">
        <h2>公司信息</h2>
        <p><strong>公司名称：</strong>LeafGO 职达招聘平台</p>
        <p><strong>公司地址：</strong>北京市朝阳区太阳宫中�?6号院1号楼18�?801�?9</p>
        <p><strong>统一社会信用代码：</strong>91110105MA01XXXXXX</p>
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
    margin-bottom: 32px;
  }
}

.contact-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 32px;
}

.contact-card {
  background: white;
  padding: 24px;
  border-radius: 8px;

  h3 {
    font-size: 16px;
    color: #222;
    margin-bottom: 16px;
  }

  .phone,
  .email {
    font-size: 20px;
    color: #00b85c;
    font-weight: 500;
    margin-bottom: 8px;
  }

  .time,
  .desc {
    font-size: 14px;
    color: #999;
    margin: 0;
    line-height: 1.6;
  }
}

.company-info {
  background: white;
  padding: 24px;
  border-radius: 8px;

  h2 {
    font-size: 20px;
    color: #222;
    margin-bottom: 16px;
  }

  p {
    font-size: 14px;
    color: #666;
    margin: 8px 0;
    line-height: 1.6;

    strong {
      color: #222;
    }
  }
}
</style>
