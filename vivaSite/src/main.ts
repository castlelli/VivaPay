import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import 'bootstrap/dist/css/bootstrap.min.css';
import { createApp } from 'vue'
//@ts-ignore
import App from './App.vue'
//@ts-ignore
import router from '../router/index'
//@ts-ignore
import NavBar from '@/components/NavBar.vue'
//@ts-ignore
import PromoCard from '@/components/Promo/PromoCard.vue'
//@ts-ignore
import PromoSection from '@/components/Promo/PromoSection.vue'
//@ts-ignore
import ContactSection from './components/ContactSection/CompContato.vue'
//@ts-ignore
import DownloadSection from './components/DownloadSection.vue'
//@ts-ignore
import SobreViva from './components/SobreViva/SobreViva.vue'
//@ts-ignore
import VivaEquipe from './components/SobreViva/VivaEquipe.vue'
//@ts-ignore
import NossaHistoria from './components/SobreViva/NossaHistoria.vue'
//@ts-ignore
import ServicosViva from './components/ServicosViva/ServicosViva.vue'
//@ts-ignore
import VivaService from './components/ServicosViva/VivaService.vue'
//@ts-ignore
import SobreNos from './SobreNos.vue';

const app = createApp(App)
app.use(router)
app.mount('#app')
app.component('nav-bar', NavBar)
app.component('promo-card', PromoCard)
app.component('promo-section', PromoSection)
app.component('comp-contato', ContactSection)
app.component('download-section', DownloadSection)



app.component('sobre-viva', SobreViva)
app.component('viva-equipe', VivaEquipe)
app.component('nossa-historia', NossaHistoria)
app.component('sobrenos', SobreNos)

app.component('servicos-viva', ServicosViva)
app.component('viva-service', VivaService)