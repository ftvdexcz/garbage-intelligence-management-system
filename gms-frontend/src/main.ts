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
import 'leaflet-routing-machine/dist/leaflet-routing-machine.css';
import '@/assets/styles/global.css';
import 'leaflet/dist/leaflet.css';
import { createPinia } from 'pinia';
import Smartlook from 'smartlook-client';

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

Smartlook.init('3aa1186c6a1831e13561c708ce13b0805267f22e', { region: 'eu' });

const app = createApp(App);

import VueGtag from 'vue-gtag';

app.use(VueGtag, {
  config: { id: 'G-1YWVVW18D6' },
});

app.component('font-awesome-icon', FontAwesomeIcon);
library.add(fas);
library.add(far);
app.use(createPinia());
app.use(router);
app.use(vuetify);
app.mount('#app');
