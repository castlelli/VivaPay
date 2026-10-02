import { createRouter, createWebHistory } from 'vue-router';
//@ts-ignore
import { RouteRecordRaw } from 'vue-router';
//@ts-ignore
import HomePage from '../src/HomePage.vue'
//@ts-ignore
import SobreNos from '@/SobreNos.vue';
//@ts-ignore
import Services from '@/Services.vue';

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '/home',
    name: 'Home',
    component: HomePage
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

export default router
