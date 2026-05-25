<template>
  <div class="employer-layout">
    <el-container>
      <el-aside width="200px" class="sidebar">
        <div class="logo">
          <svg width="32" height="32" viewBox="0 0 60 60" fill="none">
            <rect width="60" height="60" rx="12" fill="#1a7f64" />

            <path d="M30 15L42 27L30 39L18 27L30 15Z" fill="#101010" />

            <path d="M30 21L36 27L30 33L24 27L30 21Z" fill="#1a7f64" />
          </svg>

          <span>招聘者后台</span>
        </div>

        <el-menu :default-active="activeMenu" :router="true" class="sidebar-menu">
          <el-menu-item index="/employer">
            <el-icon><House /></el-icon>

            <span>首页概览</span>
          </el-menu-item>

          <el-menu-item index="/employer/jobs">
            <el-icon><Briefcase /></el-icon>

            <span>职位管理</span>
          </el-menu-item>

          <el-menu-item index="/employer/candidates">
            <el-icon><User /></el-icon>

            <span>候选人管理</span>
          </el-menu-item>

          <el-menu-item index="/employer/company">
            <el-icon><OfficeBuilding /></el-icon>

            <span>企业信息</span>
          </el-menu-item>

          <el-menu-item index="/employer/messages">
            <el-icon><ChatDotRound /></el-icon>

            <span>消息中心</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <el-container>
        <el-header class="header">
          <div class="header-content">
            <div class="breadcrumb">
              <el-breadcrumb separator="/">
                <el-breadcrumb-item :to="{ path: '/employer' }">首页</el-breadcrumb-item>

                <el-breadcrumb-item>{{ currentPageTitle }}</el-breadcrumb-item>
              </el-breadcrumb>
            </div>

            <div class="user-info">
              <el-dropdown @command="handleCommand">
                <div class="user-avatar">
                  <el-avatar :size="36" :src="userStore.userInfo?.avatar">
                    {{ userStore.userInfo?.username?.charAt(0) }}
                  </el-avatar>

                  <span>{{ userStore.userInfo?.username }}</span>
                </div>

                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </div>
        </el-header>

        <el-main class="main">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

import { useRoute } from 'vue-router'

import { useUserStore } from '@/store/user'

const route = useRoute()

const userStore = useUserStore()

const activeMenu = computed(() => route.path)

const currentPageTitle = computed(() => {
  const titleMap: Record<string, string> = {
    '/employer': '首页概览',

    '/employer/jobs': '职位管理',

    '/employer/candidates': '候选人管理',

    '/employer/company': '企业信息',

    '/employer/messages': '消息中心',
  }

  return titleMap[route.path] || '首页'
})

const handleCommand = (command: string): void => {
  if (command === 'logout') {
    userStore.logout()
  }
}
</script>

<style scoped lang="scss">
.employer-layout {
  height: 100vh;

  .el-container {
    height: 100%;
  }
}

.sidebar {
  background: #001529;

  overflow: hidden;

  .logo {
    height: 60px;

    display: flex;

    align-items: center;

    justify-content: center;

    padding: 0 16px;

    border-bottom: 1px solid rgba(255, 255, 255, 0.1);

    img {
      height: 32px;

      margin-right: 8px;
    }

    span {
      color: #fff;

      font-size: 16px;

      font-weight: 500;
    }
  }

  .sidebar-menu {
    border: none;

    background: transparent;

    :deep(.el-menu-item) {
      color: rgba(255, 255, 255, 0.65);

      &:hover {
        background: rgba(255, 255, 255, 0.08);

        color: #fff;
      }

      &.is-active {
        background: #00bebd;

        color: #fff;
      }
    }
  }
}

.header {
  background: #fff;

  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);

  padding: 0 24px;

  display: flex;

  align-items: center;

  .header-content {
    width: 100%;

    display: flex;

    justify-content: space-between;

    align-items: center;
  }

  .user-info {
    .user-avatar {
      display: flex;

      align-items: center;

      cursor: pointer;

      span {
        margin-left: 8px;

        font-size: 14px;

        color: #606266;
      }
    }
  }
}

.main {
  background: #f0f2f5;

  padding: 24px;

  overflow-y: auto;
}
</style>
