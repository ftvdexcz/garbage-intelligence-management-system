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
      <v-tooltip text="Sửa" location="bottom">
        <template v-slot:activator="{ props }">
          <v-btn
            icon="fas fa-pencil"
            color="primary"
            size="small"
            variant="text"
            v-bind="props"
            :to="`/trucks/edit/${item.id}`"
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
          />
        </template>
      </v-tooltip>
    </template>
  </v-data-table-server>
</template>

<script lang="ts" setup>
import { reactive, ref } from 'vue';

const data = [
  {
    id: 1,
    plate: '30H-12345',
    company: 'Điểm thu IEC',
    max_weight: 15.99,
  },
];

const headers: any = [
  { title: 'Biển số xe', key: 'plate', align: 'start', sortable: false },
  { title: 'Đơn vị thu gom', key: 'company', align: 'start' },
  {
    title: 'Khối lượng tối đa (kg)',
    key: 'max_weight',
    align: 'start',
    sortable: false,
  },
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
