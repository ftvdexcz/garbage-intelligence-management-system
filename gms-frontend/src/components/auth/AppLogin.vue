<template>
  <div class="app-map">
    <div class="leaflet-map">
      <l-map
        ref="map"
        :center="[20.980605, 105.787527]"
        :zoom="10"
        :max-zoom="10"
        :min-zoom="10"
      >
        <l-tile-layer
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          layer-type="base"
          name="OpenStreetMap"
        />
      </l-map>
    </div>

    <div
      class="w-100 h-100 d-flex justify-center align-center"
      style="
        position: absolute;
        top: 0;
        left: 0;
        background-color: rgba(0, 0, 0, 0.1);
        z-index: 1000;
      "
    >
      <v-card
        variant="outlined"
        width="500px"
        style="background-color: #212121"
      >
        <template #title> Đăng nhập </template>
        <v-card-text>
          <v-form v-model="valid">
            <v-container>
              <v-row>
                <v-col cols="12" class="mb-3">
                  <v-text-field
                    input
                    type="email"
                    :rules="emailRules"
                    v-model="email"
                    label="Email"
                  ></v-text-field>
                </v-col>
                <v-col cols="12" class="mb-3">
                  <v-text-field
                    input
                    v-model="password"
                    :rules="passwordRules"
                    label="Mật khẩu"
                    type="password"
                  ></v-text-field>
                </v-col>
              </v-row>
              <v-btn
                @click.self="login"
                width="100%"
                size="large"
                color="primary"
                class="mt-4"
              >
                Đăng nhập
              </v-btn>
            </v-container>
          </v-form>
        </v-card-text>
      </v-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

import { LMap, LTileLayer } from '@vue-leaflet/vue-leaflet';
const valid = ref<boolean>(false);
const email = ref<string>('');
const password = ref<string>('');

const emailRules = [
  (value: string) => {
    if (value) return true;

    return 'Vui lòng nhập email!';
  },
  (value: string) => {
    if (/.+@.+\..+/.test(value)) return true;

    return 'Email không hợp lệ!';
  },
];

const passwordRules = [
  (value: string) => {
    if (value.length > 0) return true;

    return 'Vui lòng nhập mật khẩu!';
  },
];

const login = () => {
  if (valid.value) {
    //
  }
};
</script>

<style scoped>
.app-map {
  height: 100%;
  width: 100%;
  position: relative;
}

.leaflet-map {
  height: 100%;
  width: 100%;
}
</style>
