<template>
  <v-navigation-drawer v-model="drawer" :rail="rail" permanent>
    <v-list-item height="64px">
      <template #prepend>
        <span class="block" style="font-size: 1.5rem" v-show="!rail"
          >Bản đồ</span
        >
      </template>

      <template #default v-if="rail">
        <div @click.stop="rail = !rail" style="cursor: pointer">
          <v-icon icon="fas fa-chevron-left"></v-icon>
        </div>
      </template>
      <template #append v-else>
        <div @click.stop="rail = !rail" style="cursor: pointer">
          <v-icon icon="fas fa-chevron-left"></v-icon>
        </div>
      </template>
    </v-list-item>

    <v-divider></v-divider>

    <v-list density="compact">
      <v-list-item
        v-for="item in navItem"
        :key="item.title"
        :prepend-icon="item.icon"
        :title="item.title"
        :value="item.value"
        active-color="#2196F3"
        class="py-3"
      />
    </v-list>
  </v-navigation-drawer>
</template>

<script lang="ts" setup>
import { ref } from 'vue';

const drawer = ref<boolean>(true);
const rail = ref<boolean>(true);

const navItem = [
  {
    title: 'Bản đồ',
    value: 'Bản đồ',
    icon: 'fas fa-globe-europe',
  },
  {
    title: 'Log',
    value: 'Log',
    icon: 'fas fa-terminal',
  },
  {
    title: 'Xe rác',
    value: 'Xe rác',
    icon: 'fas fa-truck-moving',
  },
  {
    title: 'Điểm thu rác',
    value: 'Điểm thu rác',
    icon: 'fas fa-trash-alt',
  },
  {
    title: 'Thông báo',
    value: 'Thông báo',
    icon: 'fas fa-bell',
  },
];
</script>
