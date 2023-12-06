<template>
  <v-toolbar flat color="primary" class="d-flex align-center">
    <v-toolbar-title>Chi tiết xe rác</v-toolbar-title>
  </v-toolbar>
  <v-container class="mx-auto mt-8" style="width: 70%">
    <v-card variant="outlined">
      <template #title> Thông tin xe rác </template>
      <v-card-text>
        <v-form>
          <v-container>
            <v-row>
              <v-col cols="12">
                <v-text-field
                  v-model="truck.plate"
                  readonly
                  label="Biển số xe"
                ></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-text-field
                  v-model="truck.company"
                  label="Đơn vị thu gom"
                  readonly
                ></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-text-field
                  v-model="truck.capacity"
                  label="Khối lượng tối đa (kg)"
                  readonly
                ></v-text-field>
              </v-col>
            </v-row>
          </v-container>
        </v-form>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script setup lang="ts">
import { onBeforeMount, ref, defineProps } from 'vue';
import * as truckService from '@/services/truck.service';
import { Truck, newTruck } from '@/models/truck';

const truck = ref<Truck>(newTruck());

const props = defineProps<{
  plate: string;
}>();

onBeforeMount(async () => {
  const response = await truckService.getTruckByPlate(props.plate);
  if (response.code === 200) {
    truck.value = response.data;
  }
});
</script>
