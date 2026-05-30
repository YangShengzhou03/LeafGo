<template>
  <footer class="footer">
    <div class="container">
      <div class="footer-links">
        <div class="link-group">
          <h4>企业服务</h4>
          <router-link to="/jobs">职位搜索</router-link>
          <router-link to="/app">LeafGO APP</router-link>
          <router-link to="/investor-relations">投资者关系</router-link>
        </div>
        <div class="link-group">
          <h4>使用与帮助</h4>
          <router-link to="/terms">用户协议</router-link>
          <router-link to="/privacy-policy">隐私政策</router-link>
          <router-link to="/anti-fraud">防骗指南</router-link>
        </div>
        <div class="link-group">
          <h4>联系我们</h4>
          <p>客服热线: <span class="phone" @click="copyPhone('4000655799')">400-065-5799</span></p>
          <p>工作时间: 8:00 - 22:00</p>
        </div>
      </div>

      <div class="footer-divider"></div>

      <div class="footer-bottom">
        <p>
          Copyright © {{ currentYear }} LeafGO 职达招聘平台 版权所有
          <a href="https://beian.miit.gov.cn/" target="_blank">京ICP备14013441号-5</a>
          <a href="http://www.beian.gov.cn/" target="_blank">京公网安备11010502032801</a>
          <a href="#" @click.prevent="showHRLicense">人力资源服务许可证</a>
        </p>
      </div>
    </div>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="400px">
      <p>{{ dialogContent }}</p>
    </el-dialog>
  </footer>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const currentYear = ref(new Date().getFullYear())

const dialogVisible = ref(false)
const dialogTitle = ref('')
const dialogContent = ref('')

const copyPhone = (phone: string): void => {
  navigator.clipboard.writeText(phone).then(() => {
    ElMessage.success('电话号码已复制')
  })
}

const showHRLicense = (): void => {
  dialogTitle.value = '人力资源服务许可证'
  dialogContent.value = '许可证编号：RC2026001\n发证机关：北京市人力资源和社会保障局'
  dialogVisible.value = true
}
</script>

<style scoped lang="scss">
@use '@/assets/styles/variables.scss' as *;

.footer {
  background-color: #1a1a2e;
  padding: $spacing-5xl 0 0;
  margin-top: $spacing-5xl;
}

.footer-links {
  display: flex;
  gap: 100px;
  flex-wrap: wrap;
}

.link-group {
  h4 {
    font-size: $body-sm;
    color: $on-primary;
    margin: 0 0 $spacing-lg 0;
  }

  a,
  .router-link {
    display: block;
    font-size: $body-sm;
    color: rgba(255, 255, 255, 0.5);
    text-decoration: none;
    margin-bottom: $spacing-md;
    transition: color $transition-fast;
    cursor: pointer;

    &:hover {
      color: $on-primary;
    }
  }

  p {
    font-size: $body-sm;
    color: rgba(255, 255, 255, 0.5);
    margin: 0 0 $spacing-md;
    line-height: 1.6;
  }

  .phone {
    cursor: pointer;
    transition: color $transition-fast;

    &:hover {
      color: $primary;
    }
  }

  .email {
    color: rgba(255, 255, 255, 0.5);
    transition: color $transition-fast;

    &:hover {
      color: $primary;
    }
  }
}

.footer-divider {
  height: 1px;
  background-color: rgba(255, 255, 255, 0.08);
  margin: $spacing-xl 0;
}

.footer-bottom {
  padding-bottom: $spacing-4xl;

  p {
    font-size: $caption;
    color: rgba(255, 255, 255, 0.35);
    margin: 0;
    line-height: 2.2;

    a {
      color: rgba(255, 255, 255, 0.35);
      text-decoration: none;
      margin: 0 $spacing-xs;
      white-space: nowrap;
      cursor: pointer;
      transition: color $transition-fast;

      &:hover {
        color: $on-primary;
      }
    }
  }
}
</style>
