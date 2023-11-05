import AppMap from '@/components/map/AppMap.vue';
import HomeView from '@/views/HomeView.vue';
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
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
