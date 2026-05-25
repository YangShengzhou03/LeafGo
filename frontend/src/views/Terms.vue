<template>
  <div class="static-page">
    <NavBar />
    <div class="page-container">
      <h1>协议与规则</h1>
      <p>更新日期：2026年5月25日</p>

      <div class="content-section">
        <h2>一、服务协议</h2>
        <div class="article">
          <h3>1.1 服务内容</h3>
          <p>
            LeafGO职达招聘平台（以下简称"本平台"）为用户提供职位发布、简历投递、在线沟通等招聘相关服务。用户注册使用本平台服务即表示同意本协议。
          </p>
        </div>
        <div class="article">
          <h3>1.2 用户账号</h3>
          <p>
            用户需提供真实、准确的信息完成注册，并对账号安全负责。禁止转让、出借账号给他人使用。
          </p>
        </div>
        <div class="article">
          <h3>1.3 用户行为规范</h3>
          <p>
            用户在使用本平台服务时，应遵守国家法律法规，不得发布虚假信息、骚扰他人、侵犯他人权益等违法违规行为。
          </p>
        </div>
      </div>

      <div class="content-section">
        <h2>二、隐私保护</h2>
        <div class="article">
          <p>
            本平台重视用户隐私保护，具体内容请参阅《<router-link to="/privacy-policy"
              >隐私政策</router-link
            >》。
          </p>
        </div>
      </div>

      <div class="content-section">
        <h2>三、信息发布规则</h2>
        <div class="article">
          <h3>3.1 职位发布规则</h3>
          <ul>
            <li>职位信息必须真实、准确、完整</li>
            <li>禁止发布虚假招聘、传销、诈骗等违法信息</li>
            <li>禁止收取求职者任何费用</li>
            <li>薪资范围应与实际相符</li>
          </ul>
        </div>
        <div class="article">
          <h3>3.2 简历发布规则</h3>
          <ul>
            <li>简历信息应真实可信</li>
            <li>禁止冒用他人身份</li>
            <li>禁止在简历中发布广告或其他无关内容</li>
          </ul>
        </div>
      </div>

      <div class="content-section">
        <h2>四、违规处理</h2>
        <div class="article">
          <p>对于违反本协议的用户，本平台有权采取以下措施：</p>
          <ul>
            <li>警告并要求整改</li>
            <li>删除违规内容</li>
            <li>限制账号功能</li>
            <li>封禁账号</li>
            <li>追究法律责任</li>
          </ul>
        </div>
      </div>

      <div class="content-section">
        <h2>五、免责声明</h2>
        <div class="article">
          <p>
            本平台仅提供信息发布及撮合服务，不对用户间的交易行为承担责任。用户应自行判断信息的真实性，谨防诈骗。
          </p>
        </div>
      </div>

      <div class="content-section">
        <h2>六、联系我们</h2>
        <div class="article">
          <p>如有任何疑问，请通过以下方式联系我们：</p>
          <ul>
            <li>客服热线：400-065-5799</li>
            <li>邮箱：service@leafgo.com</li>
            <li>工作时间：工作日 8:00 - 22:00</li>
          </ul>
        </div>
      </div>
    </div>
    <AppFooter />
  </div>
</template>

<script setup lang="ts">
import NavBar from '@/components/NavBar.vue'
import AppFooter from '@/components/AppFooter.vue'

defineOptions({
  name: 'TermsPage',
})
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
    margin-bottom: 20px;
    padding-bottom: 12px;
    border-bottom: 1px solid #eee;
  }
}

.article {
  margin-bottom: 20px;

  h3 {
    font-size: 16px;
    color: #333;
    margin-bottom: 12px;
  }

  p {
    font-size: 14px;
    color: #666;
    line-height: 1.8;
    margin: 0;

    a {
      color: $primary;
      text-decoration: none;

      &:hover {
        text-decoration: underline;
      }
    }
  }

  ul {
    margin: 12px 0;
    padding-left: 20px;

    li {
      font-size: 14px;
      color: #666;
      line-height: 2;
    }
  }
}
</style>
