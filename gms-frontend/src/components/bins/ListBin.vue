<template>
  <v-data-table-server
    v-model:items-per-page="itemsPerPage"
    :items-length="totalItems"
    :items="serverItems"
    :loading="loading"
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
      <v-icon
        size="small"
        class="me-4"
        @click="console.log(item)"
        icon="fas fa-pencil"
        color="primary"
      />

      <v-icon
        size="small"
        @click="console.log(item)"
        icon="fas fa-trash"
        color="red"
      />
    </template>
  </v-data-table-server>
</template>

<script lang="ts" setup>
import { reactive, ref } from 'vue';

const data = [
  {
    id: 'ptit',
    name: 'Điểm thu IEC',
    lat: 20.980075,
    lon: 105.794389,
    owner: 'Đặng Quốc Long',
  },
  {
    id: 'ptit 2',
    name: 'Điểm thu IEC 2',
    lat: 20.980075,
    lon: 105.794389,
    owner: 'Đặng Quốc Long 2',
  },
];

const headers: any = [
  { title: 'Mã doanh nghiệp', key: 'id', align: 'start', sortable: false },
  { title: 'Tên doanh nghiệp', key: 'name', align: 'start' },
  { title: 'Vĩ độ (Lat)', key: 'lat', align: 'start', sortable: false },
  { title: 'Kinh độ (Lon)', key: 'lon', align: 'start', sortable: false },
  { title: 'Chủ sở hữu', key: 'owner', align: 'start' },
  { title: 'Actions', key: 'actions', sortable: false, align: 'center' },
];

const FakeAPI = {
  async fetch({ page, itemsPerPage, sortBy }: any): Promise<{
    items: {
      id: string;
      name: string;
      lat: number;
      lon: number;
      owner: string;
    }[];
    total: number;
  }> {
    return new Promise((resolve) => {
      setTimeout(() => {
        const start = (page - 1) * itemsPerPage;
        const end = start + itemsPerPage;
        const items = data.slice();

        if (sortBy?.length) {
          const sortKey: string = sortBy[0].key;
          const sortOrder: string = sortBy[0].order;
          items.sort((a: any, b: any) => {
            const aValue = a[sortKey];
            const bValue = b[sortKey];
            return sortOrder === 'desc' ? bValue - aValue : aValue - bValue;
          });
        }

        const paginated = items.slice(start, end);

        resolve({ items: paginated, total: items.length });
      }, 500);
    });
  },
};

const itemsPerPage = ref<number>(5);

const serverItems = ref<
  {
    id: string;
    name: string;
    lat: number;
    lon: number;
    owner: string;
  }[]
>();

const editedIndex = ref<number>(-1);
const editedItem = reactive({
  name: '',
  calories: 0,
  fat: 0,
  carbs: 0,
  protein: 0,
});
const loading = ref<boolean>(true);
const totalItems = ref<number>(0);

const loadItems = ({ page, itemsPerPage, sortBy }: any) => {
  loading.value = true;
  FakeAPI.fetch({ page, itemsPerPage, sortBy }).then(({ items, total }) => {
    serverItems.value = items;
    totalItems.value = total;
    loading.value = false;
  });
};
</script>
