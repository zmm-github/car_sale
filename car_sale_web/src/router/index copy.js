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
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect/index')
      }
    ]
  },
  
  {
    path: '/login',
    component: () => import('@/views/login/login'),
    hidden: true
  },

  {
    path: '/register',
    component: () => import('@/views/register/register'),
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
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '首页', icon: 'home', affix: "true" }
    }]
  },

  
  // {
  //   path: '/dashboard',
  //   component: Layout,
  //   redirect: '/dashboard/index',
  //   children: [{
  //     path: 'index',
  //     name: 'Index',
  //     component: () => import('@/views/dashboard/index'),
  //     meta: { title: '首页', icon: 'home', affix: 'true' }
  //   }]
  // },

  {
    path: '/brandCarModel',
    component: Layout,
    redirect: '/brandCarModel/carSeries',
    name: 'BrandCarModel',
    meta: { title: '品牌车型', icon: 'brand' },
    children: [
      {
        path: 'carSeries',
        name: 'CarSeries',
        component: () => import('@/views/brandCarModel/carSeries'),
        meta: { title: '车系管理', icon: 'car_series' }
      },
      {
        path: 'carModel',
        name: 'CarModel',
        component: () => import('@/views/brandCarModel/carModel'),
        meta: { title: '车型管理', icon: 'car_model' }
      },
      {
        path: 'accessory',
        name: 'Accessory',
        component: () => import('@/views/brandCarModel/accessory'),
        meta: { title: '配件管理', icon: 'assessory' }
      }
    ]
  },

  {
    path: '/carGuide',
    component: Layout,
    redirect: '/carGuide/lamp',
    name: 'CarGuide',
    meta: { title: '用车指南', icon: 'el-icon-s-help' },
    children: [
      {
        path: 'lamp',
        name: 'Lamp',
        component: () => import('@/views/carGuide/lamp'),
        meta: { title: '故障灯管理', icon: 'table' }
      },
      {
        path: 'lectureHall',
        name: 'LectureHall',
        component: () => import('@/views/carGuide/lectureHall'),
        meta: { title: '爱车讲堂', icon: 'tree' }
      },
      {
        path: 'guide',
        name: 'Guide',
        component: () => import('@/views/carGuide/guide'),
        meta: { title: '用车指南', icon: 'guide' }
      }
    ]
  },

  {
    path: '/promotionInfo',
    component: Layout,
    children: [
      {
        path: 'promotion',
        name: 'PromotionInfo',
        component: () => import('@/views/promotionInfo/promotion'),
        meta: { title: '促销信息', icon: 'form' }
      }
    ]
  },

  {
    path: '/customerManage',
    component: Layout,
    redirect: '/customerManage/customer',
    name: 'CustomerManage',
    meta: {
      title: '客户管理',
      icon: 'nested'
    },
    children: [
      {
        path: 'customer',
        component: () => import('@/views/customerManage/customer'), // Parent router-view
        name: 'Customer',
        meta: { title: '客户管理', icon: 'nested' },
      },
      {
        path: 'appointment',
        component: () => import('@/views/customerManage/appointment'),
        name: 'Appointment',
        meta: { title: '预约记录', icon: 'nested' }
      },
      {
        path: 'rescue',
        component: () => import('@/views/customerManage/rescue'),
        name: 'Rescue',
        meta: { title: '救援记录', icon: 'nested' }
      }
    ]
  },

  {
    path: 'external-link',
    component: Layout,
    children: [
      {
        path: 'https://panjiachen.github.io/vue-element-admin-site/#/',
        meta: { title: 'External Link', icon: 'link' }
      }
    ]
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
