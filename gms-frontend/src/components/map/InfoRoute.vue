<template>
  <div class="info-container">
    <v-toolbar color="blue" height="48">
      <template #default>
        <v-tabs v-model="tab">
          <v-tab value="route-info">Thông tin</v-tab>
        </v-tabs>
      </template>
      <template #append>
        <v-btn @click="$emit('close')">
          <v-icon icon="fas fa-xmark" />
        </v-btn>
      </template>
    </v-toolbar>

    <v-window v-model="tab" direction="vertical">
      <v-window-item value="route-info">
        <v-card height="360px" class="overflow-y-auto">
          <v-stepper v-model="step" non-linear>
            <v-stepper-header v-for="(i, idx) in route.instructions" :key="idx">
              <v-stepper-item
                :title="i.text"
                :value="idx + 1"
                editable
                color="primary"
                @click.prevent="clickRoute(i)"
              >
                <template #icon>
                  {{ idx + 1 }}
                </template>
              </v-stepper-item>
            </v-stepper-header>
          </v-stepper>
        </v-card>
      </v-window-item>
    </v-window>
  </div>
</template>

<script setup lang="ts">
import { ref, toRefs } from 'vue';
import L from 'leaflet';

const emits = defineEmits({
  close: null,
  updateInstruction: null,
});

const props = defineProps<{
  route: any; // fix missing index in type L.Routing.IRoute
}>();

const { route } = toRefs(props);

const tab = ref<string>('');
const step = ref(1);
const instructions = ref(route.value.instructions);
const coordinates = ref(route.value.coordinates);
const selectedIndex = ref(route.value.instructions[0].index);
emits(
  'updateInstruction',
  route.value.coordinates[selectedIndex.value].lat,
  route.value.coordinates[selectedIndex.value].lng
);

const clickRoute = (i: any) => {
  selectedIndex.value = i.index;
  console.log(selectedIndex.value);
  emits(
    'updateInstruction',
    route.value.coordinates[selectedIndex.value].lat,
    route.value.coordinates[selectedIndex.value].lng
  );
};
</script>

<style scoped>
.info-container {
  position: absolute;
  right: 0;
  top: calc(100vh - 64px - 360px - 48px);
  left: 0;
  background-color: #212121;
  z-index: 1000;
}
</style>
