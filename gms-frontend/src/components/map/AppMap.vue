<template>
  <div style="height: 100%; width: 100%; position: relative">
    <div style="height: 100%; width: 100%">
      <l-map ref="map" v-model:zoom="zoom" :center="[20.980605, 105.787527]">
        <l-control-layers position="topright"></l-control-layers>
        <l-tile-layer
          url="https://mt1.google.com/vt/lyrs=m&x={x}&y={y}&z={z}"
          name="GoogleMap"
          layer-type="base"
        />

        <l-tile-layer
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          layer-type="base"
          name="OpenStreetMap"
        />
        <l-marker :lat-lng="[20.980605, 105.787527]">
          <l-icon :icon-url="greenBinIcon" :icon-size="[16, 24]" />
        </l-marker>
      </l-map>
    </div>

    <info-map />
  </div>
</template>

<script lang="ts" setup>
import InfoMap from './InfoMap.vue';

import 'leaflet/dist/leaflet.css';
import {
  LMap,
  LTileLayer,
  LControlLayers,
  LMarker,
  LIcon,
} from '@vue-leaflet/vue-leaflet';
import { ref } from 'vue';

const greenBinIcon = new URL('./../../assets/green_bin.png', import.meta.url)
  .href;

const zoom = ref<number>(16);
</script>

<style></style>
