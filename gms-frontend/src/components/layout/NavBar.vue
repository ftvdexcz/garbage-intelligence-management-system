<template>
  <v-navigation-drawer v-model="drawer" :rail="rail" permanent :rail-width="70">
    <!-- <v-list>
      <v-list-item>
        <template #prepend>
          <div>
            <p class="block" v-show="!rail">Bản đồ</p>
          </div>
        </template>
        <template #default>
          <div class="d-flex justify-space-between">
            <v-icon icon="fas fa-chevron-left" @click.stop="rail = !rail" />
          </div>
        </template>
      </v-list-item>
    </v-list> -->

    <v-list-item
      prepend-avatar="https://randomuser.me/api/portraits/men/85.jpg"
      nav
    >
      <template v-slot:append>
        <v-btn
          variant="text"
          icon="fas fa-chevron-left"
          @click.stop="rail = !rail"
        ></v-btn>
      </template>
    </v-list-item>

    <v-divider></v-divider>

    <v-list density="compact">
      <v-list-item
        prepend-icon="fas fa-globe-europe"
        title="Bản đồ"
        value="Bản đồ"
      ></v-list-item>
      <v-list-item
        prepend-icon="fas fa-terminal"
        title="Log"
        value="Log"
      ></v-list-item>
      <v-list-item
        prepend-icon="fas fa-truck-moving"
        title="Xe rác"
        value="Xe rác"
      ></v-list-item>
      <v-list-item
        prepend-icon="fas fa-trash-alt"
        title="Điểm thu rác"
        value="Điểm thu rác"
      ></v-list-item>
      <v-list-item
        prepend-icon="fas fa-bell"
        title="Thông báo"
        value="Thông báo"
      ></v-list-item>
    </v-list>
  </v-navigation-drawer>
</template>

<script lang="ts" setup>
import { ref } from 'vue';

const drawer = ref<boolean>(true);
const rail = ref<boolean>(true);
</script>
