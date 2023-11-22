<template>
  <v-app-bar>
    <template #prepend>
      <v-app-bar-nav-icon
        class="d-md-none"
        @click.stop="drawer = !drawer"
      ></v-app-bar-nav-icon>
    </template>
    <template #title>
      <v-app-bar-title>
        <v-img src="@/assets/gms-icon.png" width="120"></v-img>
      </v-app-bar-title>
    </template>
    <template #append>
      <div class="d-none d-sm-flex align-sm-center">
        <v-badge :content="44" color="info" class="mx-2">
          <v-icon icon="fas fa-bell"></v-icon>
        </v-badge>
        <v-icon icon="fas fa-gear" class="mx-2"></v-icon>
        <div class="text-subtitle-1 ml-2">Xin chào, Đặng Quốc Long</div>

        <v-menu>
          <template v-slot:activator="{ props }">
            <v-btn icon="fas fa-ellipsis-vertical" v-bind="props"></v-btn>
          </template>

          <v-list>
            <v-list-item
              v-for="(item, i) in [{ title: 'Hồ sơ' }, { title: 'Đăng xuất' }]"
              :key="i"
              :value="i"
            >
              <v-list-item-action>{{ item.title }}</v-list-item-action>
            </v-list-item>
          </v-list>
        </v-menu>
      </div>
    </template>
  </v-app-bar>

  <v-navigation-drawer v-model="drawer" :permanent="$vuetify.display.mdAndUp">
    <v-list density="compact" class="pa-0">
      <v-list-item
        v-for="item in navItem"
        :key="item.title"
        :title="item.title"
        :value="item.value"
        active-color="#2196F3"
        class="py-3 pl-10"
        :to="item.link"
      >
        <template #prepend>
          <v-icon :icon="item.icon" size="20"></v-icon>
        </template>
      </v-list-item>
    </v-list>
  </v-navigation-drawer>

  <v-main>
    <router-view />
  </v-main>
</template>

<script lang="ts" setup>
import { ref } from 'vue';

const drawer = ref<boolean>(true);

const navItem = [
  {
    title: 'Bản đồ',
    value: 'Bản đồ',
    icon: 'fas fa-globe-europe',
    link: '/map',
  },
  {
    title: 'Log',
    value: 'Log',
    icon: 'fas fa-terminal',
    link: '/logs',
  },
  {
    title: 'Xe rác',
    value: 'Xe rác',
    icon: 'fas fa-truck',
    link: '/trucks',
  },
  {
    title: 'Điểm thu rác',
    value: 'Điểm thu rác',
    icon: 'fas fa-trash-alt',
    link: '/bins',
  },
  {
    title: 'Cảnh báo',
    value: 'Cảnh báo',
    icon: 'fas fa-triangle-exclamation',
    link: '/warning',
  },
];
</script>
