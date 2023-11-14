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
        <v-btn>
          <v-icon icon="fas fa-xmark" />
        </v-btn>
      </template>
    </v-toolbar>

    <div style="height: 260px">
      <v-container>
        <v-row align="center" justify="center" class="ma-2">
          <v-col cols="12" lg="4" class="mx-3" md="5" sm="12">
            <v-card>
              <v-list>
                <v-list-item prepend-icon="far fa-building">
                  <template #default> Doanh nghiệp </template>
                  <template #append> PTIT </template>
                </v-list-item>
                <v-list-item prepend-icon="fas fa-scale-balanced">
                  <template #default> Khối lượng </template>
                  <template #append> 300.000 kg </template>
                </v-list-item>
                <v-list-item prepend-icon="fas fa-location-dot">
                  <template #default> Vị trí </template>
                  <template #append> 20.981612, 105.786347 </template>
                </v-list-item>
              </v-list>
            </v-card>
          </v-col>

          <v-col cols="12" lg="4" class="mx-3" md="5" sm="12">
            <v-card>
              <v-list>
                <v-list-item prepend-icon="far fa-id-card">
                  <template #default> Mã doanh nghiệp </template>
                  <template #append> 5 </template>
                </v-list-item>
                <v-list-item prepend-icon="fas fa-user">
                  <template #default> Chủ sở hữu </template>
                  <template #append> Đặng Quốc Long </template>
                </v-list-item>
                <v-list-item prepend-icon="fas fa-house">
                  <template #default> Địa chỉ </template>
                  <template #append> Km10 Nguyễn Trãi </template>
                </v-list-item>
              </v-list>
            </v-card>
          </v-col>

          <v-col offset="1" cols="12" lg="2" md="5" sm="12">
            <v-img
              :height="180"
              aspect-ratio="16/9"
              src="http://203.162.10.109:2208//bin/bin_1693499509653.png"
            ></v-img>
          </v-col>
        </v-row>
      </v-container>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const tab = ref<string>('');
</script>

<style scoped>
.info-container {
  position: absolute;
  width: 100%;
  bottom: 0;
  left: 0;
  background-color: #212121;
  z-index: 1000;
  box-shadow: 0 -4px 3px -5px #212121;
}
</style>
