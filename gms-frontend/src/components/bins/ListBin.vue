<template>
  <v-data-table-server
    v-model:items-per-page="size"
    :items-length="totalItems"
    :items="bins"
    :loading="commonStore.loading"
    item-value="name"
    :headers="headers"
    @update:options="loadItems"
  >
    <template #top>
      <v-toolbar flat color="primary" class="d-flex align-center">
        <v-toolbar-title>Điểm thu rác</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-btn
          to="/bins/add"
          prepend-icon="fas fa-plus"
          variant="outlined"
          flat
        >
          Thêm điểm thu rác
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
            :to="`/bins/detail/${item.id}`"
          />
        </template>
      </v-tooltip>
      <v-tooltip text="Sửa" location="bottom">
        <template v-slot:activator="{ props }">
          <v-btn
            icon="fas fa-pencil"
            color="primary"
            size="small"
            variant="text"
            v-bind="props"
            :to="`/bins/edit/${item.id}`"
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
            @click.prevent="deleteBin(item.id)"
          />

          <v-dialog
            v-model="delDialog"
            activator="parent"
            width="400"
            :transition="false"
          >
            <v-card color="white">
              <v-card-text>
                Bạn có chắc muốn xóa điểm thu rác với mã: {{ selectedId }} ?
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
import { Bin } from '@/models/bin';
import { useCommonStore } from '@/stores/common';
import { ref } from 'vue';
import * as binService from '@/services/bin.service';
import { AlertType } from '@/models/alert';

const headers: any = [
  { title: 'Mã doanh nghiệp', key: 'id', align: 'start', sortable: false },
  { title: 'Tên doanh nghiệp', key: 'company', align: 'start' },
  { title: 'Vĩ độ (Lat)', key: 'lat', align: 'start', sortable: false },
  { title: 'Kinh độ (Lon)', key: 'lon', align: 'start', sortable: false },
  {
    title: 'Chủ sở hữu',
    key: 'owner.name',
    align: 'start',
    sortable: false,
  },
  { title: 'Actions', key: 'actions', sortable: false, align: 'center' },
];
const size = ref<number>(10);
const bins = ref<Bin[]>(new Array<Bin>());
const totalItems = ref<number>(0);
const delDialog = ref<boolean>(false);
const selectedId = ref<string>('');

const commonStore = useCommonStore();

const loadItems = async ({ page, itemsPerPage, sortBy }: any) => {
  let sortOptions = '';
  if (sortBy?.length) {
    sortOptions = sortBy[0].key + '-' + sortBy[0].order;
  }
  console.log(sortOptions);
  const response = await binService.listBinsPagination({
    page,
    size: itemsPerPage,
    sortOptions,
  });
  if (response.code === 200) {
    const data = response.data;
    bins.value = data.results;
    totalItems.value = data.totals;
  }
};

const deleteBin = (id: string) => {
  selectedId.value = id;
};

const confirmDelete = async () => {
  const commonStore = useCommonStore();
  const { _alert } = commonStore;
  const response = await binService.deleteBin(selectedId.value);
  if (response.code === 200) {
    _alert(response.message, AlertType.Success);
    bins.value = bins.value.filter((bin) => bin.id !== selectedId.value);
  }
  delDialog.value = false;
};
</script>
