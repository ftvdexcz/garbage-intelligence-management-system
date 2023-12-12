<template>
  <v-toolbar flat color="primary" class="d-flex align-center">
    <v-toolbar-title>Chi tiết điểm thu</v-toolbar-title>
  </v-toolbar>
  <v-container class="mx-auto mt-8" style="width: 70%">
    <v-row align="start" style="height: 150px">
      <v-col lg="8" cols="12">
        <v-card variant="outlined">
          <template #title> Thông tin điểm thu </template>
          <v-card-text>
            <v-form>
              <v-container>
                <v-row>
                  <v-col cols="12" class="mb-3">
                    <v-text-field
                      v-model="bin.id"
                      label="Mã doanh nghiệp"
                      hide-details
                      readonly
                    ></v-text-field>
                  </v-col>

                  <v-col cols="12" class="mb-3">
                    <v-text-field
                      v-model="bin.company"
                      label="Tên doanh nghiệp"
                      hide-details
                      readonly
                    ></v-text-field>
                  </v-col>

                  <v-col cols="12" class="mb-3">
                    <v-text-field
                      v-model="bin.owner.name"
                      label="Chủ sở hữu"
                      hide-details
                      readonly
                    ></v-text-field>
                  </v-col>

                  <v-col cols="12" class="mb-3">
                    <v-text-field
                      v-model="bin.owner.phone"
                      label="SĐT"
                      hide-details
                      readonly
                    ></v-text-field>
                  </v-col>

                  <v-col cols="12" class="mb-3">
                    <v-text-field
                      v-model="bin.owner.email"
                      label="Email"
                      hide-details
                      readonly
                    ></v-text-field>
                  </v-col>

                  <v-col cols="12" class="mb-3">
                    <v-text-field
                      v-model="bin.address"
                      label="Địa chỉ"
                      hide-details
                      readonly
                    ></v-text-field>
                  </v-col>

                  <v-col cols="12" class="mb-3">
                    <v-text-field
                      v-model="bin.capacity"
                      label="Khối lượng tối đa"
                      hide-details
                      readonly
                    ></v-text-field>
                  </v-col>

                  <v-col cols="12" class="mb-3">
                    <v-text-field
                      label="Camera url"
                      v-model="bin.cameraUrl"
                      hide-details
                      readonly
                    ></v-text-field>
                  </v-col>
                </v-row>
              </v-container>
            </v-form>
          </v-card-text>
        </v-card>
      </v-col>

      <v-col lg="4" cols="12">
        <v-card variant="outlined">
          <div class="v-card-title">Vị trí</div>

          <v-card-text>
            <v-form>
              <v-container>
                <v-row>
                  <v-col cols="12" class="mb-3">
                    <v-text-field
                      v-model="bin.lat"
                      label="Vĩ độ"
                      readonly
                      hide-details
                    ></v-text-field>
                  </v-col>

                  <v-col cols="12">
                    <v-text-field
                      v-model="bin.lon"
                      label="Kinh độ"
                      readonly
                      hide-details
                    ></v-text-field>
                  </v-col>
                </v-row>
              </v-container>
            </v-form>
          </v-card-text>

          <div class="v-card-title">Ảnh điểm thu</div>
          <v-card-text>
            <v-img :height="200" cover :src="bin.imageUrl"></v-img>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { onBeforeMount, ref, defineProps } from 'vue';
import * as binService from '@/services/bin.service';
import { Bin, newBin } from '@/models/bin';

const bin = ref<Bin>(newBin());

const props = defineProps<{
  id: string;
}>();

onBeforeMount(async () => {
  const response = await binService.getBinById(props.id);
  if (response.code === 200) {
    bin.value = response.data;
  }
});
</script>
