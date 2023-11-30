import { createApp } from 'vue';
import App from './App.vue';
import router from './router';

// Vuetify
import { createVuetify } from 'vuetify';
import { loadFonts } from './plugins/webfontloader';
import 'vuetify/styles';
import * as components from 'vuetify/components';
import * as directives from 'vuetify/directives';
import { vi, en } from 'vuetify/locale';

// font-awesome icons
import { aliases, fa } from 'vuetify/iconsets/fa-svg';
import { library } from '@fortawesome/fontawesome-svg-core';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { fas } from '@fortawesome/free-solid-svg-icons';
import { far } from '@fortawesome/free-regular-svg-icons';

// material js icons
// import { aliases, mdi } from 'vuetify/iconsets/mdi-svg';
// import { mdiAccount } from '@mdi/js';

// css
import '@/assets/styles/global.css';
import 'leaflet/dist/leaflet.css';
import { createPinia } from 'pinia';

loadFonts();

const vuetify = createVuetify({
  components,
  directives,
  icons: {
    defaultSet: 'fa',
    aliases,
    sets: {
      fa,
    },
  },
  // icons: {
  //   defaultSet: 'mdi',
  //   aliases: {
  //     ...aliases,
  //     account: mdiAccount,
  //   },
  //   sets: {
  //     mdi,
  //   },
  // },
  locale: {
    locale: 'vi',
    fallback: 'vi',
    messages: { vi, en },
  },
  theme: {
    defaultTheme: 'dark',
  },
});

const app = createApp(App);

app.component('font-awesome-icon', FontAwesomeIcon);
library.add(fas);
library.add(far);
app.use(createPinia());
app.use(router);
app.use(vuetify);
app.mount('#app');
