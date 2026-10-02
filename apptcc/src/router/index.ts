import { createRouter, createWebHistory } from '@ionic/vue-router';
import { RouteRecordRaw } from 'vue-router';
import HomePage from '../views/HomePage.vue'
import ConsultarPage from '../views/ConsultarPage.vue'
import HistoryPage from '../views/HistoryPage.vue'
import PagamentoPage from '../views/PagamentoPage.vue';

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '/home',
    name: 'Home',
    component: HomePage,
  },
  {
    path: '/consulta',
    name: 'Consultar',
    component: ConsultarPage,
    props: true
  },
  {
    path: '/pagamento',
    name: 'Pagamento',
    component: PagamentoPage
  },
  {
    path: '/historico',
    name: 'Historico',
    component: HistoryPage
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

export default router
