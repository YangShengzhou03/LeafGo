<template>
  <header class="nav-bar">
    <div class="nav-container">
      <div class="logo" @click="$router.push('/')">
        <span class="logo-text">LeafGO 职达</span>
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
        <template v-if="userStore.isLoggedIn">
          <el-badge :value="unreadCount" :hidden="unreadCount === 0" class="badge-item">
            <router-link to="/seeker/messages" class="icon-link">
              <el-icon><ChatDotRound /></el-icon>
              消息
            </router-link>
          </el-badge>
          <router-link to="/seeker/resume" class="icon-link">
            <el-icon><Document /></el-icon>
            简历
          </router-link>
          <el-dropdown @command="handleCommand">
            <span class="user-dropdown">
              <el-avatar :size="28">{{ userStore.userInfo?.username?.charAt(0) }}</el-avatar>
              <span class="username">{{ userStore.userInfo?.username }}</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
        <template v-else>
          <router-link to="/login" class="text-link">登录</router-link>
          <router-link to="/login" class="text-link register-link">注册</router-link>
        </template>
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ChatDotRound, Document } from '@element-plus/icons-vue'
import { useUserStore } from '@/store/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const menuItems = [
  { path: '/', label: '首页' },
  { path: '/jobs', label: '职位' },
  { path: '/companies', label: '公司' },
  { path: '/campus', label: '校园' },
  { path: '/app', label: 'APP' },
]

const unreadCount = ref(0)

const isActive = (path: string): boolean => {
  if (path === '/') return route.path === '/'
  return route.path.startsWith(path)
}

const handleCommand = (command: string): void => {
  switch (command) {
    case 'profile':
      router.push('/seeker')
      break
    case 'logout':
      userStore.logout()
      router.push('/')
      break
  }
}
</script>

<style scoped lang="scss">
@use '@/assets/styles/variables.scss' as *;

$nav-bg: #0a1f15;
$nav-active: #00b85c;
$nav-text: #00b85c;

.nav-bar {
  position: sticky;
  top: 0;
  z-index: 1000;
  background: linear-gradient(180deg, #1a3a2e 0%, #153028 100%);
  padding: $spacing-sm 0;
}

.nav-container {
  display: flex;
  align-items: center;
  max-width: 1246px;
  margin: 0 auto;
  padding: 0 $spacing-3xl;
}

.logo {
  cursor: pointer;
  margin-right: $spacing-2xl;

  .logo-text {
    font-size: $body-lg;
    color: #00b85c;
    letter-spacing: 3px;
  }
}

.nav-menu {
  display: flex;
  align-items: center;
  gap: $spacing-xs;
  margin-right: auto;
}

.nav-link {
  font-size: $body-sm;
  color: $nav-text;
  text-decoration: none;
  padding: $spacing-xs $spacing-md;
  border-radius: $rounded-sm;
  transition: all $transition-fast;

  &:hover {
    color: #6eeaa0;
    background-color: rgba(0, 184, 92, 0.1);
  }

  &.active {
    color: #00ff7f;
    background-color: rgba(0, 184, 92, 0.15);
    font-weight: 500;
  }
}

.user-actions {
  display: flex;
  align-items: center;
  gap: $spacing-lg;
}

.text-link {
  font-size: $body-sm;
  color: $nav-text;
  text-decoration: none;
  transition: color $transition-fast;

  &:hover {
    color: #4ae08a;
  }
}

.register-link {
  color: #4ae08a;

  &:hover {
    color: #00b85c;
  }
}

.icon-link {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: $body-sm;
  color: $nav-text;
  text-decoration: none;
  transition: color $transition-fast;

  &:hover {
    color: #4ae08a;
  }
}

.badge-item {
  :deep(.el-badge__content) {
    background-color: #f56c6c;
  }
}

.user-dropdown {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;

  .username {
    font-size: $body-sm;
    color: $nav-text;
  }
}
</style>
