<template>
  <div class="info-container">
    <v-toolbar color="blue" height="48">
      <template #default>
        <v-tabs v-model="tab">
          <v-tab value="info">thông tin</v-tab>
          <v-tab value="camera">camera</v-tab>
        </v-tabs>
      </template>
      <template #append>
        <v-btn @click="$emit('close')">
          <v-icon icon="fas fa-xmark" />
        </v-btn>
      </template>
    </v-toolbar>

    <v-window v-model="tab" direction="vertical">
      <v-window-item value="info">
        <v-row justify="center" class="ma-2">
          <v-col cols="12" lg="4" class="mx-3" md="5" sm="12">
            <v-card :height="200">
              <v-list>
                <v-list-item prepend-icon="far fa-building">
                  <template #default> Doanh nghiệp </template>
                  <template #append> {{ bin?.company }} </template>
                </v-list-item>
                <v-list-item prepend-icon="fas fa-scale-balanced">
                  <template #default> Khối lượng </template>
                  <template #append>
                    {{ bin?.weight.totalWeight }} (gram)
                  </template>
                </v-list-item>
                <v-list-item prepend-icon="fas fa-location-dot">
                  <template #default> Vị trí </template>
                  <template #append> {{ bin?.lat }}, {{ bin?.lon }} </template>
                </v-list-item>
              </v-list>
            </v-card>
          </v-col>

          <v-col cols="12" lg="4" class="mx-3" md="5" sm="12">
            <v-card :height="200">
              <v-list>
                <v-list-item prepend-icon="far fa-id-card">
                  <template #default> Mã doanh nghiệp </template>
                  <template #append> {{ bin?.id }} </template>
                </v-list-item>
                <v-list-item prepend-icon="fas fa-user">
                  <template #default> Chủ sở hữu </template>
                  <template #append> {{ bin?.owner.name }} </template>
                </v-list-item>
                <v-list-item prepend-icon="fas fa-house">
                  <template #default> Địa chỉ </template>
                  <template #append> {{ bin?.address }} </template>
                </v-list-item>
              </v-list>
            </v-card>
          </v-col>

          <v-col offset-lg="1" cols="12" lg="2" md="5" sm="12">
            <v-img :height="200" cover :src="bin?.imageUrl"></v-img>
          </v-col>
        </v-row>
      </v-window-item>
      <v-window-item value="camera">
        <v-card height="calc(100vh - 64px - 48px)">
          <video controls style="width: 100%; height: 100%">
            <source
              src="ptitsure.tk:8888/index-80/index.m3u8"
              type="application/x-mpegURL"
            />
          </video>
        </v-card>
      </v-window-item>
    </v-window>
  </div>
</template>

<script setup lang="ts">
import { Bin } from '@/models/bin';
import { ref, defineEmits, defineProps } from 'vue';

const tab = ref<string>('');

defineEmits({
  close: null,
});

defineProps<{
  bin: Bin;
}>();
</script>

<style scoped>
.info-container {
  position: absolute;
  right: 0;
  top: calc(100vh - 64px - 240px - 48px);
  left: 0;
  background-color: #212121;
  z-index: 1000;
}
</style>
