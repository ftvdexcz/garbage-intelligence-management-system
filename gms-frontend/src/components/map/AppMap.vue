<template>
  <div style="height: 100%; width: 100%">
    <l-map ref="map" v-model:zoom="zoom" :center="[21.23385, 105.811164]">
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
      <l-marker
        :lat-lng="[21.23385, 105.811164]"
        icon-url="http://203.162.10.109:2209/static/media/green_bin.8b4bbbea249d9fac22a1.png"
        :icon-size="[20, 20]"
      >
      </l-marker>
    </l-map>
  </div>
</template>

<script lang="ts" setup>
import 'leaflet/dist/leaflet.css';
import {
  LMap,
  LTileLayer,
  LControlLayers,
  LMarker,
} from '@vue-leaflet/vue-leaflet';
import { ref } from 'vue';

const zoom = ref<number>(16);
</script>

<style></style>
