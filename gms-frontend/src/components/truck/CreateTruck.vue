<template>
  <v-toolbar flat color="primary" class="d-flex align-center">
    <v-toolbar-title>Thêm xe rác</v-toolbar-title>
  </v-toolbar>
  <v-container class="mx-auto mt-8" style="width: 70%">
    <v-card variant="outlined">
      <template #title> Thông tin xe rác </template>
      <v-card-text>
        <v-form v-model="valid">
          <v-container>
            <v-row>
              <v-col cols="12" class="mb-3">
                <v-text-field
                  input
                  type="text"
                  v-model="plate"
                  :rules="blankRule"
                  label="Biển số xe"
                  required
                ></v-text-field>
              </v-col>
              <v-col cols="12" class="mb-3">
                <v-text-field
                  input
                  type="text"
                  v-model="company"
                  :rules="blankRule"
                  label="Đơn vị thu gom"
                  required
                ></v-text-field>
              </v-col>
              <v-col cols="12" class="mb-3">
                <v-text-field
                  input
                  type="number"
                  :rules="numberRule"
                  v-model="capacity"
                  label="Khối lượng tối đa (kg)"
                  required
                ></v-text-field>
              </v-col>
            </v-row>
          </v-container>
        </v-form>
      </v-card-text>
    </v-card>

    <v-container>
      <v-btn @click.prevent="cancelSubmit" color="grey-lighten-5" class="mr-4">
        Hủy
      </v-btn>
      <v-btn
        @click.prevent="submit"
        color="primary"
        append-icon="fas fa-floppy-disk"
      >
        Lưu
      </v-btn>
    </v-container>
  </v-container>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import * as truckService from '@/services/truck.service';
import { useCommonStore } from '@/stores/common';
import { AlertType } from '@/models/alert';
const commonStore = useCommonStore();
const { _alert } = commonStore;

const valid = ref<boolean>(false);
const plate = ref<string>('');
const company = ref<string>('');
const capacity = ref<number>(0);

const router = useRouter();

const blankRule = [
  (value: string) => {
    if (value.trim().length > 0) return true;

    return 'Không được để trống';
  },
];

const numberRule = [
  (value: number) => {
    if (value >= 0) return true;

    return 'Khối lượng tối đa không được âm';
  },
];

const cancelSubmit = () => {
  router.push('/trucks');
};

const submit = async () => {
  if (valid.value) {
    const response = await truckService.createTruck({
      plate: plate.value,
      company: company.value,
      capacity: capacity.value,
    });
    console.log(response);
    if (response.code === 201) {
      _alert(response.message, AlertType.Success);
      window.setTimeout(() => {
        router.push('/trucks');
      }, 1000);
    }
  }
};
</script>
