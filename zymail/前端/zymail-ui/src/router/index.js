import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '介绍', icon: 'dashboard' }
    }]
  },
  {
    path: '/mail',
    component: Layout,
    meta: {
      title: '邮件功能',
      icon: 'el-icon-message'
    },
    alwaysShow: true,
    children: [
      {
        path: 'sendMail',
        component: () => import('@/views/mail/sendMail/index.vue'),
        meta: {
          title: '发送邮件',
          icon: 'el-icon-s-promotion'
        },
      },
      {
        path: 'mailList',
        component: () => import('@/views/mail/mailList/index.vue'),
        meta: {
          title: '历史邮件',
          icon: 'el-icon-s-marketing'
        },
      },
      {
        path: 'mailTemplate',
        component: () => import('@/views/mail/mailList/template.vue'),
        meta: {
          title: '邮件模板',
          icon: 'el-icon-copy-document'

        },
      }
    ]
  },
  {
    path: '/source',
    component: Layout,
    meta: {
      title: '调度管理',
      icon: 'el-icon-monitor'
    },
    children: [{
      path: '/',
      name: '/',
      component: () => import('@/views/mail/source/index.vue'),
      hidden: true
    }]
  },
  {
    path: '/logs',
    component: Layout,
    meta: {
      title: '日志管理',
      icon: 'el-icon-s-order'
    },
    children: [{
      path: '/',
      name: '/',
      component: () => import('@/views/mail/log/index.vue'),
      hidden: true
    }]
  },


  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
