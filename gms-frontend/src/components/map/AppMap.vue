<template>
  <div class="app-map">
    <div id="leaflet-map" ref="mapElement"></div>

    <v-slide-y-reverse-transition>
      <info-map
        v-show="infoMapVisible"
        @close="closeInfoMap"
        :bin="activeBin!"
      />
    </v-slide-y-reverse-transition>
  </div>
</template>

<script lang="ts" setup>
import * as binService from '@/services/bin.service';
import InfoMap from './InfoMap.vue';
import L from 'leaflet';
import { Ref, onMounted, ref } from 'vue';
import { Bin, newBin } from '@/models/bin';
import * as utils from '@/helpers/utils';

const binIcons = [
  new URL('./../../assets/green_bin.png', import.meta.url).href,
  new URL('./../../assets/yellow_bin.png', import.meta.url).href,
  new URL('./../../assets/red_bin.png', import.meta.url).href,
];

const infoMapVisible = ref<boolean>(false);
let activeBin = ref<Bin>(newBin());
const bins = ref<Bin[]>(new Array<Bin>());

const closeInfoMap = () => {
  infoMapVisible.value = false;
};

const showInfoMap = (bin: Bin) => {
  activeBin.value = bin;
  infoMapVisible.value = true;
};

const map = ref<L.Map>() as Ref<L.Map>;
const mapElement = ref<HTMLElement>() as Ref<HTMLElement>;

onMounted(async () => {
  const response = await binService.listBins();
  console.log(response);
  if (response.code === 200) {
    bins.value = response.data;
  }
  if (mapElement.value) {
    map.value = L.map(mapElement.value).setView([20.980605, 105.787527], 16);
    const baseLayers = {
      GoogleMap: L.tileLayer(
        'https://mt1.google.com/vt/lyrs=m&x={x}&y={y}&z={z}',
        {
          attribution:
            '&copy; <a href="http://osm.org/copyright">GoogleMap</a> contributors',
        }
      ),
      OpenStreetMap: L.tileLayer(
        'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
        {
          attribution:
            '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors',
        }
      ),
    };
    baseLayers.OpenStreetMap.addTo(map.value);
    L.control.layers(baseLayers).addTo(map.value);
    bins.value.forEach((bin) => {
      L.marker([bin.lat, bin.lon], {
        icon: L.icon({
          iconUrl:
            binIcons[
              utils.capacityPercent(bin.weight.totalWeight, bin.capacity)
            ],
          iconSize: [16, 24],
        }),
      })
        .on('click', L.bind(showInfoMap, null, bin))
        .addTo(map.value);
    });
  }
});
</script>

<style scoped>
.app-map {
  height: 100%;
  width: 100%;
  position: relative;
}

#leaflet-map {
  height: 100%;
  width: 100%;
}
</style>
