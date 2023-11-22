import AppBin from '@/components/bins/AppBin.vue';
import AppTruck from '@/components/truck/AppTruck.vue';
import AppWarning from '@/components/warning/AppWarning.vue';
import AppMap from '@/components/map/AppMap.vue';
import HomeView from '@/views/HomeView.vue';
import CreateBin from '@/components/bins/CreateBin.vue';
import ListBin from '@/components/bins/ListBin.vue';
import EditBin from '@/components/bins/EditBin.vue';
import CreateTruck from '@/components/truck/CreateTruck.vue';
import ListTruck from '@/components/truck/ListTruck.vue';
import EditTruck from '@/components/truck/EditTruck.vue';
import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';

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
            path: 'edit/:id',
            component: EditTruck,
            props: true,
          },
        ],
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
