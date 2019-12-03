import Vue from 'vue'
import VueRouter from 'vue-router'
import BREADCRUMB from '@/constants/breadcrumbs'

Vue.use(VueRouter)

const routes = [{
  path: '/',
  redirect: '/main'
}, {
  name: 'main',
  path: '/main',
  meta: {
    breadcrumbs: BREADCRUMB['main']
  },
  component: () => import(/* webpackChunkName: "group-main" */ '../pages/main'),
  children: [
    // Dashboard Module
    {
      name: 'dashboard',
      path: 'dashboard',
      meta: {
        breadcrumbs: BREADCRUMB['dashboard']
      },
      component: () => import(/* webpackChunkName: "group-dashboard" */ '../pages/dashboard/dashboard')
    }, {
      name: 'dashboard-antv-g2',
      path: 'dashboard/antv-g2',
      meta: {
        breadcrumbs: BREADCRUMB['dashboard-antv-g2']
      },
      component: () => import(/* webpackChunkName: "group-dashboard" */ '../pages/dashboard/antv-g2')
    }, {
      name: 'dashboard-echarts',
      path: 'dashboard/echarts',
      meta: {
        breadcrumbs: BREADCRUMB['dashboard-echarts']
      },
      component: () => import(/* webpackChunkName: "group-dashboard" */ '../pages/dashboard/echarts')
    },
    // Project Module
    {
      name: 'project',
      path: 'project',
      meta: {
        breadcrumbs: BREADCRUMB['project']
      },
      component: () => import(/* webpackChunkName: "group-project" */ '../pages/project')
    },
    // System Module
    {
      path: 'system',
      component: () => import(/* webpackChunkName: "group-system" */ '../pages/dashboard/dashboard')
    }, {
      name: 'system-user',
      path: 'system/user',
      meta: {
        breadcrumbs: BREADCRUMB['system-user']
      },
      component: () => import(/* webpackChunkName: "group-system" */ '../pages/system/user')
    }, {
      name: 'system-role',
      path: 'system/role',
      meta: {
        breadcrumbs: BREADCRUMB['system-role']
      },
      component: () => import(/* webpackChunkName: "group-system" */ '../pages/system/role')
    }, {
      name: 'system-auth',
      path: 'system/auth',
      meta: {
        breadcrumbs: BREADCRUMB['system-auth']
      },
      component: () => import(/* webpackChunkName: "group-system" */ '../pages/system/auth')
    }, {
      name: 'system-menu',
      path: 'system/menu',
      meta: {
        breadcrumbs: BREADCRUMB['system-menu']
      },
      component: () => import(/* webpackChunkName: "group-system" */ '../pages/system/menu')
    }
  ]
}]

const router = new VueRouter({
  routes: routes
})

router.beforeEach((to, from, next) => {
  console.log(to, from, next)
  next()
})

export default router