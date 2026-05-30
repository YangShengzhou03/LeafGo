<template>
  <div class="employer-layout">
    <EmployerNavBar />
    <el-container>
      <el-aside width="200px" class="sidebar">
        <div class="sidebar-title">招聘者后台</div>

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
import EmployerNavBar from '@/components/EmployerNavBar.vue'

const route = useRoute()

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
</script>

<style scoped lang="scss">
.employer-layout {
  height: 100vh;
  display: flex;
  flex-direction: column;

  .el-container {
    flex: 1;
    overflow: hidden;
  }
}

.sidebar {
  background: #001529;

  overflow: hidden;

  .sidebar-title {
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 0 16px;
    border-bottom: 1px solid rgba(255, 255, 255, 0.1);
    color: #fff;
    font-size: 16px;
    font-weight: 500;
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
    align-items: center;
  }
}

.main {
  background: #f0f2f5;

  padding: 24px;

  overflow-y: auto;
}
</style>
