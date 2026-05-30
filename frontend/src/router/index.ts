import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import { useUserStore } from '@/store/user'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/seeker/Home.vue'),
  },
  {
    path: '/jobs',
    name: 'Jobs',
    component: () => import('@/views/seeker/Jobs.vue'),
  },
  {
    path: '/jobs/:id',
    name: 'JobDetail',
    component: () => import('@/views/seeker/JobDetail.vue'),
  },
  {
    path: '/companies',
    name: 'Companies',
    component: () => import('@/views/seeker/Companies.vue'),
  },
  {
    path: '/companies/:id',
    name: 'CompanyDetail',
    component: () => import('@/views/seeker/CompanyDetail.vue'),
  },
  {
    path: '/campus',
    name: 'Campus',
    component: () => import('@/views/seeker/Campus.vue'),
  },
  {
    path: '/enterprise-service',
    name: 'EnterpriseService',
    component: () => import('@/views/seeker/EnterpriseService.vue'),
  },
  {
    path: '/investor-relations',
    name: 'InvestorRelations',
    component: () => import('@/views/seeker/InvestorRelations.vue'),
  },
  {
    path: '/privacy-policy',
    name: 'PrivacyPolicy',
    component: () => import('@/views/seeker/PrivacyPolicy.vue'),
  },
  {
    path: '/contact-us',
    name: 'ContactUs',
    component: () => import('@/views/seeker/ContactUs.vue'),
  },
  {
    path: '/anti-fraud',
    name: 'AntiFraud',
    component: () => import('@/views/seeker/AntiFraud.vue'),
  },
  {
    path: '/terms',
    name: 'Terms',
    component: () => import('@/views/seeker/Terms.vue'),
  },
  {
    path: '/app',
    name: 'AppDownload',
    component: () => import('@/views/seeker/AppDownload.vue'),
  },
  {
    path: '/messages',
    name: 'Messages',
    component: () => import('@/views/seeker/Messages.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/resume',
    name: 'Resume',
    component: () => import('@/views/seeker/Resume.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('@/views/seeker/Profile.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/seeker/Login.vue'),
    meta: { guest: true },
  },
  {
    path: '/employer',
    component: () => import('@/layouts/EmployerLayout.vue'),
    meta: { requiresAuth: true, role: 'EMPLOYER' },
    children: [
      {
        path: '',
        name: 'EmployerHome',
        component: () => import('@/views/employer/Home.vue'),
      },
      {
        path: 'jobs',
        name: 'EmployerJobs',
        component: () => import('@/views/employer/Jobs.vue'),
      },
      {
        path: 'jobs/create',
        name: 'EmployerJobCreate',
        component: () => import('@/views/employer/JobForm.vue'),
      },
      {
        path: 'jobs/:id/edit',
        name: 'EmployerJobEdit',
        component: () => import('@/views/employer/JobForm.vue'),
      },
      {
        path: 'candidates',
        name: 'EmployerCandidates',
        component: () => import('@/views/employer/Candidates.vue'),
      },
      {
        path: 'company',
        name: 'EmployerCompany',
        component: () => import('@/views/employer/Company.vue'),
      },
      {
        path: 'messages',
        name: 'EmployerMessages',
        component: () => import('@/views/employer/Messages.vue'),
      },
    ],
  },
  {
    path: '/admin',
    component: () => import('@/layouts/AdminLayout.vue'),
    meta: { requiresAuth: true, role: 'ADMIN' },
    children: [
      {
        path: '',
        name: 'AdminHome',
        component: () => import('@/views/admin/Home.vue'),
      },
      {
        path: 'users',
        name: 'AdminUsers',
        component: () => import('@/views/admin/Users.vue'),
      },
      {
        path: 'companies',
        name: 'AdminCompanies',
        component: () => import('@/views/admin/Companies.vue'),
      },
      {
        path: 'jobs',
        name: 'AdminJobs',
        component: () => import('@/views/admin/Jobs.vue'),
      },
      {
        path: 'statistics',
        name: 'AdminStatistics',
        component: () => import('@/views/admin/Statistics.vue'),
      },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to, _from, next) => {
  const userStore = useUserStore()

  if (to.meta.requiresAuth && !userStore.isLoggedIn) {
    next('/login')
  } else if (to.meta.guest && userStore.isLoggedIn) {
    const role = userStore.userInfo?.userType
    if (role === 'EMPLOYER') {
      next('/employer')
    } else {
      next('/')
    }
  } else if (to.meta.role) {
    const role = userStore.userInfo?.userType
    if (role !== to.meta.role) {
      if (role === 'EMPLOYER') {
        next('/employer')
      } else {
        next('/')
      }
    } else {
      next()
    }
  } else {
    next()
  }
})

export default router
