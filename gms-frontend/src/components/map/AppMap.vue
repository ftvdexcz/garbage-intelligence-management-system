<template>
  <div class="app-map">
    <div id="leaflet-map" ref="mapElement"></div>

    <v-slide-y-reverse-transition>
      <info-map v-if="infoMapVisible" @close="closeInfoMap" :bin="activeBin!" />
    </v-slide-y-reverse-transition>

    <v-slide-y-reverse-transition>
      <info-route
        v-if="infoRouteVisible"
        @close="closeInfoRoute"
        @update-instruction="updateInstruction"
        :route="activeRoute!"
      />
    </v-slide-y-reverse-transition>
  </div>
</template>

<script lang="ts" setup>
import * as binService from '@/services/bin.service';
import InfoMap from './InfoMap.vue';
import InfoRoute from './InfoRoute.vue';
import L from 'leaflet';
import 'leaflet-routing-machine';
import { Ref, onMounted, ref } from 'vue';
import { Bin, newBin } from '@/models/bin';
import * as utils from '@/helpers/utils';
import { useCommonStore } from '@/stores/common';
import { AlertType } from '@/models/alert';

const binIcons = [
  new URL('./../../assets/green_bin.png', import.meta.url).href,
  new URL('./../../assets/yellow_bin.png', import.meta.url).href,
  new URL('./../../assets/red_bin.png', import.meta.url).href,
];
const carIcon = new URL('./../../assets/garbage_truck.png', import.meta.url)
  .href;
const greenMarkerIcon = new URL(
  './../../assets/green-marker-icon.png',
  import.meta.url
).href;

const instructionIcon = new URL(
  './../../assets/instruction_marker.png',
  import.meta.url
).href;

const infoMapVisible = ref<boolean>(false);
const infoRouteVisible = ref<boolean>(false);
let activeBin = ref<Bin>(newBin());
const bins = ref<Bin[]>(new Array<Bin>());
const activeRoute = ref<L.Routing.IRoute>();

const closeInfoMap = () => {
  infoMapVisible.value = false;
};

const closeInfoRoute = () => {
  infoRouteVisible.value = false;
};

const updateInstruction = (lat: number, lng: number) => {
  instructionMarker.setLatLng([lat, lng]);
};

// instruction marker
const instructionMarker = L.marker([0, 0], {
  icon: L.icon({
    iconUrl: instructionIcon,
    iconSize: [20, 24],
  }),
  draggable: false,
});

const showInfoMap = (bin: Bin) => {
  activeBin.value = bin;
  infoRouteVisible.value = false;
  infoMapVisible.value = true;
};

const showInfoRoute = () => {
  infoMapVisible.value = false;
  infoRouteVisible.value = true;
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

    let carGps = L.latLng(21.0279256, 105.8355841);

    const showRoute = (position: L.LatLng) => {
      const carGps = position;
      const waypoints: L.LatLng[] = bins.value.map((bin) =>
        L.latLng(bin.lat, bin.lon)
      );

      // car
      const carMarker = L.marker(carGps, {
        icon: L.icon({
          iconUrl: carIcon,
          iconSize: [28, 16],
        }),
        draggable: false,
      })
        .on('click', showInfoRoute)
        .addTo(map.value);
      waypoints.unshift(carGps);

      let timeoutEvent: number[] = [];
      L.Routing.control({
        waypoints: waypoints,
        router: L.Routing.osrmv1({
          language: 'vi',
          profile: 'car',
          serviceUrl: `${process.env.VUE_APP_OSRM_URL}/route/v1`,
        }),
        addWaypoints: false,
        routeWhileDragging: true,

        plan: L.Routing.plan(waypoints, {
          createMarker(
            waypointIndex: number,
            waypoint: L.Routing.Waypoint,
            numberOfWaypoints: number
          ) {
            if (waypointIndex == 0) {
              const marker = L.marker(waypoint.latLng, {
                icon: L.icon({
                  iconUrl: greenMarkerIcon,
                  iconSize: [20, 30],
                }),
                draggable: true,
              }).addEventListener('move', (event: L.LeafletEvent) => {
                timeoutEvent.forEach((t) => {
                  clearTimeout(t);
                });
              });
              return marker;
            }

            const marker = L.marker(waypoint.latLng, {
              icon: L.icon({
                iconUrl:
                  binIcons[
                    utils.capacityPercent(
                      bins.value[waypointIndex - 1].weight.totalWeight,
                      bins.value[waypointIndex - 1].capacity
                    )
                  ],
                iconSize: [16, 24],
              }),
              draggable: false,
            }).on(
              'click',
              L.bind(showInfoMap, null, bins.value[waypointIndex - 1])
            );

            return marker;
          },
        }),
        show: false,
        useZoomParameter: true,
        autoRoute: true,
      })
        .on('routesfound', (event: L.Routing.RoutingResultEvent) => {
          console.log(event);
          activeRoute.value = event.routes[0];
          event.routes[0].coordinates?.forEach((coor, idx) => {
            timeoutEvent.push(
              setTimeout(() => {
                carMarker.setLatLng([coor.lat, coor.lng]);
              }, 500 * idx)
            );
          });
          updateInstruction(0, 0);
          instructionMarker.addTo(map.value);
        })
        .addTo(map.value);
    };

    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          carGps = L.latLng(
            position.coords.latitude,
            position.coords.longitude
          );
          showRoute(carGps);
        },
        (error) => {
          showRoute(carGps);
          const commonStore = useCommonStore();
          const { _alert } = commonStore;
          switch (error.code) {
            case error.PERMISSION_DENIED:
              _alert('Không cho phép quyền GPS', AlertType.Info);
              break;
            case error.POSITION_UNAVAILABLE:
              _alert('Vị trí không xác định', AlertType.Error);

              break;
            case error.TIMEOUT:
              _alert('Timeout', AlertType.Error);
              break;
          }
        }
      );
    } else {
      const commonStore = useCommonStore();
      const { _alert } = commonStore;
      showRoute(carGps);
      _alert('Không lấy được thông tin GPS', AlertType.Error);
    }
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
