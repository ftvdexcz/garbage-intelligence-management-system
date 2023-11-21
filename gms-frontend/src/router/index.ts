import AppBin from '@/components/bins/AppBin.vue';
import AppMap from '@/components/map/AppMap.vue';
import HomeView from '@/views/HomeView.vue';
import CreateBin from '@/components/bins/CreateBin.vue';
import ListBin from '@/components/bins/ListBin.vue';
import EditBin from '@/components/bins/EditBin.vue';
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
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
