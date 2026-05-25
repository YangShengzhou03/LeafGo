import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import { useUserStore } from '@/store/user'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/Home.vue'),
  },
  {
    path: '/jobs',
    name: 'Jobs',
    component: () => import('@/views/Jobs.vue'),
  },
  {
    path: '/jobs/:id',
    name: 'JobDetail',
    component: () => import('@/views/JobDetail.vue'),
  },
  {
    path: '/companies',
    name: 'Companies',
    component: () => import('@/views/Companies.vue'),
  },
  {
    path: '/companies/:id',
    name: 'CompanyDetail',
    component: () => import('@/views/CompanyDetail.vue'),
  },
  {
    path: '/campus',
    name: 'Campus',
    component: () => import('@/views/Campus.vue'),
  },
  {
    path: '/enterprise-service',
    name: 'EnterpriseService',
    component: () => import('@/views/EnterpriseService.vue'),
  },
  {
    path: '/investor-relations',
    name: 'InvestorRelations',
    component: () => import('@/views/InvestorRelations.vue'),
  },
  {
    path: '/privacy-policy',
    name: 'PrivacyPolicy',
    component: () => import('@/views/PrivacyPolicy.vue'),
  },
  {
    path: '/contact-us',
    name: 'ContactUs',
    component: () => import('@/views/ContactUs.vue'),
  },
  {
    path: '/anti-fraud',
    name: 'AntiFraud',
    component: () => import('@/views/AntiFraud.vue'),
  },
  {
    path: '/help',
    name: 'Help',
    component: () => import('@/views/Help.vue'),
  },
  {
    path: '/terms',
    name: 'Terms',
    component: () => import('@/views/Terms.vue'),
  },
  {
    path: '/app',
    name: 'AppDownload',
    component: () => import('@/views/AppDownload.vue'),
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { guest: true },
  },
  {
    path: '/seeker',
    component: () => import('@/layouts/SeekerLayout.vue'),
    meta: { requiresAuth: true, role: 'job_seeker' },
    children: [
      {
        path: '',
        name: 'SeekerHome',
        component: () => import('@/views/seeker/Home.vue'),
      },
      {
        path: 'jobs',
        name: 'SeekerJobs',
        component: () => import('@/views/seeker/Jobs.vue'),
      },
      {
        path: 'jobs/:id',
        name: 'SeekerJobDetail',
        component: () => import('@/views/seeker/JobDetail.vue'),
      },
      {
        path: 'companies',
        name: 'SeekerCompanies',
        component: () => import('@/views/seeker/Companies.vue'),
      },
      {
        path: 'companies/:id',
        name: 'SeekerCompanyDetail',
        component: () => import('@/views/seeker/CompanyDetail.vue'),
      },
      {
        path: 'messages',
        name: 'SeekerMessages',
        component: () => import('@/views/seeker/Messages.vue'),
      },
      {
        path: 'resume',
        name: 'SeekerResume',
        component: () => import('@/views/seeker/Resume.vue'),
      },
      {
        path: 'applications',
        name: 'SeekerApplications',
        component: () => import('@/views/seeker/Applications.vue'),
      },
    ],
  },
  {
    path: '/employer',
    component: () => import('@/layouts/EmployerLayout.vue'),
    meta: { requiresAuth: true, role: 'employer' },
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
    meta: { requiresAuth: true, role: 'admin' },
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

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()

  if (to.meta.requiresAuth && !userStore.isLoggedIn) {
    next('/login')
  } else if (to.meta.guest && userStore.isLoggedIn) {
    const role = userStore.userInfo?.userType
    if (role === 'job_seeker') {
      next('/seeker')
    } else if (role === 'employer') {
      next('/employer')
    } else {
      next()
    }
  } else if (to.meta.role) {
    const role = userStore.userInfo?.userType
    if (role !== to.meta.role) {
      if (role === 'job_seeker') {
        next('/seeker')
      } else if (role === 'employer') {
        next('/employer')
      } else {
        next('/login')
      }
    } else {
      next()
    }
  } else {
    next()
  }
})

export default router
