import AppBin from '@/components/bins/AppBin.vue';
import AppTruck from '@/components/truck/AppTruck.vue';
import AppWarning from '@/components/warning/AppWarning.vue';
import AppMap from '@/components/map/AppMap.vue';
import HomeView from '@/views/HomeView.vue';
import CreateBin from '@/components/bins/CreateBin.vue';
import ListBin from '@/components/bins/ListBin.vue';
import EditBin from '@/components/bins/EditBin.vue';
import DetailBin from '@/components/bins/DetailBin.vue';
import CreateTruck from '@/components/truck/CreateTruck.vue';
import ListTruck from '@/components/truck/ListTruck.vue';
import DetailTruck from '@/components/truck/DetailTruck.vue';
import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';
import AppLogin from '@/components/auth/AppLogin.vue';

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    component: HomeView,
    redirect: '/map',
    children: [
      {
        path: '/map',
        component: AppMap,
      },
      {
        path: '/warning',
        component: AppWarning,
      },
      {
        path: '/logs',
        component: AppWarning,
      },
      {
        path: '/bins',
        component: AppBin,
        children: [
          {
            path: '',
            component: ListBin,
          },
          {
            path: 'add',
            component: CreateBin,
          },
          {
            path: 'edit/:id',
            component: EditBin,
            props: true,
          },
          {
            path: 'detail/:id',
            component: DetailBin,
            props: true,
          },
        ],
      },
      {
        path: '/trucks',
        component: AppTruck,
        children: [
          {
            path: '',
            component: ListTruck,
          },
          {
            path: 'add',
            component: CreateTruck,
          },
          {
            path: 'detail/:plate',
            component: DetailTruck,
            props: true,
          },
        ],
      },
    ],
  },
  {
    path: '/login',
    component: AppLogin,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
