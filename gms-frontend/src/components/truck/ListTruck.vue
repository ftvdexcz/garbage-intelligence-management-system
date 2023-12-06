<template>
  <v-data-table-server
    v-model:items-per-page="size"
    :items-length="totalItems"
    :items="trucks"
    :loading="commonStore.loading"
    item-value="name"
    :headers="headers"
    @update:options="loadItems"
  >
    <template #top>
      <v-toolbar flat color="primary" class="d-flex align-center">
        <v-toolbar-title>Xe rác</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-btn
          to="/trucks/add"
          prepend-icon="fas fa-plus"
          variant="outlined"
          flat
        >
          Thêm Xe rác
        </v-btn>
      </v-toolbar>
    </template>
    <!-- eslint-disable-next-line vue/valid-v-slot -->
    <template #item.actions="{ item }">
      <v-tooltip text="Chi tiết" location="bottom">
        <template v-slot:activator="{ props }">
          <v-btn
            icon="fas fa-eye"
            color="white"
            size="small"
            variant="text"
            v-bind="props"
            :to="`/trucks/detail/${item.plate}`"
          />
        </template>
      </v-tooltip>

      <v-tooltip text="Xóa" location="bottom">
        <template v-slot:activator="{ props }">
          <v-btn
            icon="fas fa-trash"
            color="red"
            size="small"
            variant="text"
            v-bind="props"
            @click.prevent="deleteTruck(item.plate)"
          />
          <v-dialog
            v-model="delDialog"
            activator="parent"
            width="400"
            :transition="false"
          >
            <v-card color="white">
              <v-card-text>
                Bạn có chắc muốn xóa xe rác với biển số: {{ selectedPlate }} ?
              </v-card-text>
              <v-card-actions class="pt-3">
                <v-spacer></v-spacer>
                <v-btn @click="delDialog = false" color="grey">Hủy</v-btn>
                <v-btn @click="confirmDelete" color="primary" variant="outlined"
                  >Xóa</v-btn
                >
              </v-card-actions>
            </v-card>
          </v-dialog>
        </template>
      </v-tooltip>
    </template>
  </v-data-table-server>
</template>

<script lang="ts" setup>
import { AlertType } from '@/models/alert';
import { Truck } from '@/models/truck';
import * as truckService from '@/services/truck.service';
import { useCommonStore } from '@/stores/common';
import { ref } from 'vue';

const headers: any = [
  { title: 'Biển số xe', key: 'plate', align: 'start', sortable: false },
  { title: 'Đơn vị thu gom', key: 'company', align: 'start' },
  {
    title: 'Khối lượng tối đa (kg)',
    key: 'capacity',
    align: 'start',
    sortable: false,
  },
  { title: 'Actions', key: 'actions', sortable: false, align: 'center' },
];

const size = ref<number>(10);
const trucks = ref<Truck[]>(new Array<Truck>());
const totalItems = ref<number>(0);
const delDialog = ref<boolean>(false);
const selectedPlate = ref<string>('');

const commonStore = useCommonStore();

const loadItems = async ({ page, itemsPerPage, sortBy }: any) => {
  let sortOptions = '';
  if (sortBy?.length) {
    sortOptions = sortBy[0].key + '-' + sortBy[0].order;
  }
  console.log(sortOptions);
  const response = await truckService.listBinsPagination({
    page,
    size: itemsPerPage,
    sortOptions,
  });
  if (response.code === 200) {
    const data = response.data;
    trucks.value = data.results;
    totalItems.value = data.totals;
  }
};

const deleteTruck = (plate: string) => {
  selectedPlate.value = plate;
};

const confirmDelete = async () => {
  const commonStore = useCommonStore();
  const { _alert } = commonStore;
  const response = await truckService.deleteTruck(selectedPlate.value);
  if (response.code === 200) {
    _alert(response.message, AlertType.Success);
    trucks.value = trucks.value.filter(
      (truck) => truck.plate !== selectedPlate.value
    );
  }
  delDialog.value = false;
};
</script>
