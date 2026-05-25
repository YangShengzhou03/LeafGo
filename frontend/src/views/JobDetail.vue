<template>
  <div class="job-detail-page">
    <NavBar />

    <main class="main-content">
      <div class="container">
        <div class="content-layout">
          <div class="job-main">
            <div class="job-header">
              <div class="job-info">
                <h1 class="job-title">高级前端工程师</h1>
                <div class="salary">20-35K</div>
              </div>
              <div class="job-meta">
                <span>南昌</span>
                <span class="divider">·</span>
                <span>3-5年</span>
                <span class="divider">·</span>
                <span>本科</span>
              </div>
              <div class="job-tags">
                <span class="tag">五险一金</span>
                <span class="tag">年终奖</span>
                <span class="tag">股票期权</span>
                <span class="tag">带薪年假</span>
              </div>
            </div>

            <div class="job-section">
              <h2 class="section-title">职位描述</h2>
              <div class="job-description">
                <p>
                  我们正在寻找一位经验丰富的前端工程师加入我们的团队。您将负责开发和维护我们的Web应用程序，确保高质量的用户体验。
                </p>

                <h3>岗位职责：</h3>
                <ul>
                  <li>负责公司产品的前端开发工作</li>
                  <li>与产品经理、设计师和后端工程师紧密合作</li>
                  <li>优化前端性能，提升用户体验</li>
                  <li>参与技术方案设计和代码评审</li>
                </ul>

                <h3>任职要求：</h3>
                <ul>
                  <li>3-5年前端开发经验</li>
                  <li>精通HTML5、CSS3、JavaScript</li>
                  <li>熟练使用Vue.js或React框架</li>
                  <li>熟悉前端工程化和模块化开发</li>
                  <li>良好的沟通能力和团队协作精神</li>
                </ul>
              </div>
            </div>

            <div class="job-section">
              <h2 class="section-title">工作地点</h2>
              <div class="location-info">
                <p>江西省南昌市青山湖区京东大道1188号</p>
              </div>
            </div>
          </div>

          <aside class="job-sidebar">
            <div class="company-card">
              <div class="company-header">
                <div class="company-logo">华</div>
                <div class="company-info">
                  <h3 class="company-name">华勤技术股份有限公司</h3>
                  <div class="company-meta">智能硬件 · 已上市 · 10000人以上</div>
                </div>
              </div>
              <button class="btn-view-company">查看公司主页</button>
            </div>

            <div class="apply-card">
              <button class="btn-apply">立即投递</button>
              <button class="btn-collect">收藏职位</button>
            </div>

            <div class="similar-jobs">
              <h3 class="sidebar-title">相似职位</h3>
              <div class="similar-job-list">
                <div class="similar-job-item">
                  <div class="job-info">
                    <h4>前端开发工程师</h4>
                    <p>盒马鲜生 · 南昌</p>
                  </div>
                  <span class="salary">15-25K</span>
                </div>
                <div class="similar-job-item">
                  <div class="job-info">
                    <h4>Web前端工程师</h4>
                    <p>泰盈科技 · 南昌</p>
                  </div>
                  <span class="salary">12-20K</span>
                </div>
              </div>
            </div>
          </aside>
        </div>
      </div>
    </main>

    <AppFooter />
  </div>
</template>

<script setup lang="ts">
import NavBar from '@/components/NavBar.vue'
import AppFooter from '@/components/AppFooter.vue'
</script>

<style scoped lang="scss">
@use '@/assets/styles/variables.scss' as *;

.job-detail-page {
  min-height: 100vh;
}

.main-content {
  padding: $spacing-2xl 0;
}

.content-layout {
  display: grid;
  grid-template-columns: 1fr 360px;
  gap: $spacing-3xl;
}

.job-main {
  background-color: $canvas;
  border: 1px solid $hairline;
  border-radius: $rounded-md;
  padding: $spacing-3xl;
}

.job-header {
  margin-bottom: $spacing-3xl;
  padding-bottom: $spacing-3xl;
  border-bottom: 1px solid $hairline;
}

.job-info {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  margin-bottom: $spacing-lg;
}

.job-title {
  font-size: $display-md;
  font-weight: 700;
  color: $ink;
  margin: 0;
}

.salary {
  font-size: $display-md;
  font-weight: 700;
  color: $primary;
}

.job-meta {
  display: flex;
  align-items: center;
  gap: $spacing-xs;
  font-size: $body-md;
  color: $body;
  margin-bottom: $spacing-lg;

  .divider {
    color: $hairline;
  }
}

.job-tags {
  display: flex;
  gap: $spacing-sm;
}

.tag {
  font-size: $body-sm;
  color: $body;
  padding: $spacing-xs $spacing-md;
  background-color: $canvas-soft;
  border: 1px solid $hairline;
  border-radius: $rounded-pill;
}

.job-section {
  margin-bottom: $spacing-3xl;

  &:last-child {
    margin-bottom: 0;
  }
}

.section-title {
  font-size: $body-lg;
  font-weight: 600;
  color: $ink;
  margin: 0 0 $spacing-xl 0;
}

.job-description {
  font-size: $body-md;
  color: $body;
  line-height: 1.8;

  h3 {
    font-size: $body-md;
    font-weight: 600;
    color: $ink;
    margin: $spacing-xl 0 $spacing-md 0;
  }

  ul {
    margin: $spacing-md 0;
    padding-left: $spacing-xl;

    li {
      margin-bottom: $spacing-sm;
    }
  }
}

.location-info {
  font-size: $body-md;
  color: $body;
}

.job-sidebar {
  display: flex;
  flex-direction: column;
  gap: $spacing-xl;
}

.company-card {
  background-color: $canvas;
  border: 1px solid $hairline;
  border-radius: $rounded-md;
  padding: $spacing-xl;
}

.company-header {
  display: flex;
  align-items: center;
  gap: $spacing-md;
  margin-bottom: $spacing-xl;
}

.company-logo {
  width: 64px;
  height: 64px;
  border-radius: $rounded-md;
  background-color: $canvas-soft;
  border: 1px solid $hairline;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: $display-sm;
  font-weight: 700;
  color: $ink;
}

.company-info {
  flex: 1;
}

.company-name {
  font-size: $body-md;
  font-weight: 600;
  color: $ink;
  margin: 0 0 $spacing-xs 0;
}

.company-meta {
  font-size: $body-sm;
  color: $mute;
}

.btn-view-company {
  width: 100%;
  font-size: $body-sm;
  font-weight: 600;
  color: $primary;
  background: none;
  border: 1px solid $hairline;
  border-radius: $rounded-sm;
  padding: $spacing-md;
  cursor: pointer;
  transition: all $transition-fast;

  &:hover {
    border-color: $primary;
    background-color: rgba($primary, 0.05);
  }
}

.apply-card {
  background-color: $canvas;
  border: 1px solid $hairline;
  border-radius: $rounded-md;
  padding: $spacing-xl;
  display: flex;
  flex-direction: column;
  gap: $spacing-md;
}

.btn-apply {
  width: 100%;
  font-size: $body-md;
  font-weight: 600;
  color: $on-primary;
  background-color: $primary;
  border: none;
  border-radius: $rounded-sm;
  padding: $spacing-lg;
  cursor: pointer;
  transition: background-color $transition-fast;

  &:hover {
    background-color: $primary-soft;
  }
}

.btn-collect {
  width: 100%;
  font-size: $body-sm;
  font-weight: 600;
  color: $body;
  background: none;
  border: 1px solid $hairline;
  border-radius: $rounded-sm;
  padding: $spacing-md;
  cursor: pointer;
  transition: all $transition-fast;

  &:hover {
    border-color: $primary;
    color: $primary;
  }
}

.similar-jobs {
  background-color: $canvas;
  border: 1px solid $hairline;
  border-radius: $rounded-md;
  padding: $spacing-xl;
}

.sidebar-title {
  font-size: $body-md;
  font-weight: 600;
  color: $ink;
  margin: 0 0 $spacing-lg 0;
}

.similar-job-list {
  display: flex;
  flex-direction: column;
  gap: $spacing-md;
}

.similar-job-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: $spacing-md 0;
  border-bottom: 1px solid $hairline;
  cursor: pointer;

  &:last-child {
    border-bottom: none;
  }

  &:hover {
    opacity: 0.8;
  }
}

.job-info {
  h4 {
    font-size: $body-sm;
    font-weight: 600;
    color: $ink;
    margin: 0 0 $spacing-xs 0;
  }

  p {
    font-size: $caption;
    color: $mute;
    margin: 0;
  }
}

.salary {
  font-size: $body-sm;
  font-weight: 600;
  color: $primary;
}
</style>
