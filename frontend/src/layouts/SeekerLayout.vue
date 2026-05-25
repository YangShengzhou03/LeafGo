<template>
  <div class="seeker-layout">
    <header class="nav-bar">
      <div class="container nav-content">
        <div class="logo" @click="$router.push('/seeker')">
          <svg width="32" height="32" viewBox="0 0 60 60" fill="none">
            <rect width="60" height="60" rx="12" fill="#1a7f64" />

            <path d="M30 15L42 27L30 39L18 27L30 15Z" fill="#101010" />

            <path d="M30 21L36 27L30 33L24 27L30 21Z" fill="#1a7f64" />
          </svg>

          <span class="logo-text">LeafGO</span>
        </div>

        <nav class="nav-menu">
          <router-link
            v-for="item in menuItems"
            :key="item.path"
            :to="item.path"
            class="nav-link"
            :class="{ active: isActive(item.path) }"
          >
            {{ item.label }}
          </router-link>
        </nav>

        <div class="user-actions">
          <el-dropdown @command="handleCommand" trigger="click">
            <button class="user-button">
              <div class="avatar">
                {{ userStore.userInfo?.username?.charAt(0) || 'U' }}
              </div>

              <span class="username">{{ userStore.userInfo?.username || '用户' }}</span>
            </button>

            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="resume">
                  <el-icon><Document /></el-icon>

                  我的简历
                </el-dropdown-item>
                <el-dropdown-item command="applications">
                  <el-icon><List /></el-icon>
                  投递记录
                </el-dropdown-item>
                <el-dropdown-item command="profile">
                  <el-icon><Setting /></el-icon>
                  个人设置
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">
                  <el-icon><SwitchButton /></el-icon>
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </header>

    <main class="main-content">
      <router-view />
    </main>

    <footer class="footer">
      <div class="container footer-content">
        <div class="footer-links">
          <a href="#" class="footer-link">关于我们</a>

          <a href="#" class="footer-link">联系我们</a>

          <a href="#" class="footer-link">隐私政策</a>

          <a href="#" class="footer-link">用户协议</a>
        </div>

        <div class="copyright">© {{ currentYear }} LeafGO. All rights reserved.</div>
      </div>
    </footer>
  </div>
</template>

<script setup lang="ts">
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/store/user'
import { Document, List, Setting, SwitchButton } from '@element-plus/icons-vue'

const currentYear = new Date().getFullYear()

const router = useRouter()

const route = useRoute()

const userStore = useUserStore()

const menuItems = [
  { path: '/seeker', label: '首页' },

  { path: '/seeker/jobs', label: '职位' },

  { path: '/seeker/companies', label: '公司' },

  { path: '/seeker/messages', label: '消息' },
]

const isActive = (path: string): boolean => {
  return route.path === path || route.path.startsWith(path + '/')
}

const handleCommand = (command: string): void => {
  switch (command) {
    case 'resume':
      router.push('/seeker/resume')

      break

    case 'applications':
      router.push('/seeker/applications')

      break

    case 'profile':
      router.push('/seeker/profile')

      break

    case 'logout':
      userStore.logout()

      router.push('/login')

      break
  }
}
</script>

<style scoped lang="scss">
@use '@/assets/styles/variables.scss' as *;

.seeker-layout {
  display: flex;

  flex-direction: column;

  min-height: 100vh;

  background-color: $canvas;
}

.nav-bar {
  position: sticky;

  top: 0;

  z-index: 100;

  background-color: $canvas;

  border-bottom: 1px solid $hairline;

  padding: $spacing-md 0;
}

.nav-content {
  display: flex;

  align-items: center;

  justify-content: space-between;

  gap: $spacing-2xl;
}

.logo {
  display: flex;

  align-items: center;

  gap: $spacing-md;

  cursor: pointer;

  transition: opacity $transition-fast;

  &:hover {
    opacity: 0.8;
  }

  .logo-text {
    font-size: $display-sm;

    font-weight: 700;

    color: $ink;

    letter-spacing: -0.5px;
  }
}

.nav-menu {
  display: flex;

  align-items: center;

  gap: $spacing-lg;

  flex: 1;

  justify-content: center;
}

.nav-link {
  font-size: $body-sm;

  font-weight: 600;

  color: $body;

  padding: $spacing-sm $spacing-md;

  border-radius: $rounded-sm;

  transition: all $transition-fast;

  &:hover {
    color: $ink;

    background-color: $canvas-soft;
  }

  &.active {
    color: $primary;

    background-color: rgba($primary, 0.1);
  }
}

.user-actions {
  display: flex;

  align-items: center;
}

.user-button {
  display: flex;

  align-items: center;

  gap: $spacing-md;

  padding: $spacing-sm $spacing-md;

  border-radius: $rounded-sm;

  transition: background-color $transition-fast;

  &:hover {
    background-color: $canvas-soft;
  }
}

.avatar {
  width: 36px;

  height: 36px;

  border-radius: $rounded-pill;

  background-color: $primary;

  color: $on-primary;

  display: flex;

  align-items: center;

  justify-content: center;

  font-size: $body-sm;

  font-weight: 600;
}

.username {
  font-size: $body-sm;

  font-weight: 600;

  color: $ink;
}

.main-content {
  flex: 1;

  padding: $spacing-4xl 0;
}

.footer {
  border-top: 1px solid $hairline;

  padding: $spacing-4xl 0;
}

.footer-content {
  display: flex;

  flex-direction: column;

  align-items: center;

  gap: $spacing-xl;
}

.footer-links {
  display: flex;

  align-items: center;

  gap: $spacing-2xl;
}

.footer-link {
  font-size: $body-sm;

  color: $body;

  transition: color $transition-fast;

  &:hover {
    color: $primary;
  }
}

.copyright {
  font-size: $caption;

  color: $mute;
}
</style>
