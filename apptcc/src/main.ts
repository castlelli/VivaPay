

import { createApp } from 'vue'
import App from './App.vue'
import router from './router';
import { Storage } from '@ionic/storage';

import { IonicVue } from '@ionic/vue';

import { library } from '@fortawesome/fontawesome-svg-core';
import { faCheck } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';

library.add(faCheck);


/* Core CSS required for Ionic components to work properly */
import '@ionic/vue/css/core.css';
import '@fortawesome/fontawesome-free/css/all.min.css';

/* Basic CSS for apps built with Ionic */
import '@ionic/vue/css/normalize.css';
import '@ionic/vue/css/structure.css';
import '@ionic/vue/css/typography.css';

/* Optional CSS utils that can be commented out */
import '@ionic/vue/css/padding.css';
import '@ionic/vue/css/float-elements.css';
import '@ionic/vue/css/text-alignment.css';
import '@ionic/vue/css/text-transformation.css';
import '@ionic/vue/css/flex-utils.css';
import '@ionic/vue/css/display.css';

/**
 * Ionic Dark Mode
 * -----------------------------------------------------
 * For more info, please see:
 * https://ionicframework.com/docs/theming/dark-mode
 */

/* @import '@ionic/vue/css/palettes/dark.always.css'; */
/* @import '@ionic/vue/css/palettes/dark.class.css'; */
import '@ionic/vue/css/palettes/dark.system.css';

/* Theme variables */
import './theme/variables.css';

const app = createApp(App)
  .use(IonicVue)
  .use(router)

const storage = new Storage();
storage.create(); // Configuração opcional dependendo da versão do Ionic Storage

app.config.globalProperties.$storage = storage;
app.component('font-awesome-icon', FontAwesomeIcon);
app.provide("$storage", storage);

router.isReady().then(() => {
  app.mount('#app')
    ;
});
