<template>
  <v-toolbar flat color="primary" class="d-flex align-center">
    <v-toolbar-title>Sửa điểm thu</v-toolbar-title>
  </v-toolbar>
  <v-container class="mx-auto mt-8" style="width: 70%">
    <v-row align="start" style="height: 150px">
      <v-col lg="8" cols="12">
        <v-card variant="outlined">
          <template #title> Thông tin điểm thu </template>
          <v-card-text>
            <v-form v-model="valid1">
              <v-container>
                <v-row>
                  <v-col cols="12" class="mb-3">
                    <v-text-field
                      v-model="bin.id"
                      label="Mã doanh nghiệp"
                      hide-details
                      disabled
                    ></v-text-field>
                  </v-col>

                  <v-col cols="12" class="mb-3">
                    <v-text-field
                      input
                      tye="text"
                      v-model="bin.company"
                      label="Tên doanh nghiệp"
                      :rules="blankRule"
                      required
                    ></v-text-field>
                  </v-col>

                  <v-col cols="12" class="mb-3">
                    <v-text-field
                      v-model="bin.owner.name"
                      label="Chủ sở hữu"
                      :rules="blankRule"
                      required
                    ></v-text-field>
                  </v-col>

                  <v-col cols="12" class="mb-3">
                    <v-text-field
                      v-model="bin.owner.phone"
                      :rules="phoneRules"
                      label="SĐT"
                      required
                    ></v-text-field>
                  </v-col>

                  <v-col cols="12" class="mb-3">
                    <v-text-field
                      v-model="bin.owner.email"
                      :rules="emailRules"
                      label="Email"
                      required
                    ></v-text-field>
                  </v-col>

                  <v-col cols="12" class="mb-3">
                    <v-text-field
                      v-model="bin.address"
                      label="Địa chỉ"
                    ></v-text-field>
                  </v-col>

                  <v-col cols="12" class="mb-3">
                    <v-text-field
                      input
                      type="number"
                      v-model="bin.capacity"
                      label="Khối lượng tối đa"
                      required
                    ></v-text-field>
                  </v-col>

                  <v-col cols="12" class="mb-3">
                    <v-text-field
                      input
                      type="text"
                      v-model="bin.cameraUrl"
                      label="Camera url"
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
            <v-form v-model="valid2">
              <v-container>
                <v-row>
                  <v-col cols="12" class="mb-3">
                    <v-text-field
                      input
                      type="number"
                      v-model="bin.lat"
                      :rules="latRule"
                      label="Vĩ độ"
                      required
                    ></v-text-field>
                  </v-col>

                  <v-col cols="12">
                    <v-text-field
                      input
                      type="number"
                      v-model="bin.lon"
                      :rules="lonRule"
                      label="Kinh độ"
                      required
                    ></v-text-field>
                  </v-col>
                  <v-col cols="12">
                    <div>Cập nhật ảnh điểm thu</div>
                    <v-file-input
                      v-model="imageFile"
                      color="primary"
                      label="Tải ảnh"
                      show-size
                      accept="image/png, image/jpeg, image/bmp"
                      :dirty="true"
                      :clearable="true"
                      :rules="imageRule"
                      variant="underlined"
                      prepend-icon="fas fa-camera-retro"
                    ></v-file-input>
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

        <v-container>
          <v-row justify="space-between">
            <v-col cols="6">
              <v-btn
                width="100%"
                color="grey-lighten-5"
                @click.prevent="cancelSubmit"
              >
                Hủy
              </v-btn>
            </v-col>

            <v-col cols="6">
              <v-btn
                width="100%"
                color="primary"
                append-icon="fas fa-floppy-disk"
                @click.prevent="submit"
              >
                Lưu
              </v-btn>
            </v-col>
          </v-row>
        </v-container>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { onBeforeMount, ref, defineProps } from 'vue';
import * as binService from '@/services/bin.service';
import { Bin, newBin } from '@/models/bin';
import { decamelizeKeys } from 'humps';
import { useRouter } from 'vue-router';
import { useCommonStore } from '@/stores/common';
import { AlertType } from '@/models/alert';

const router = useRouter();
const commonStore = useCommonStore();
const { _alert } = commonStore;

const valid1 = ref<boolean>(false);
const valid2 = ref<boolean>(false);
const bin = ref<Bin>(newBin());
const imageFile = ref<File[]>();

const props = defineProps<{
  id: string;
}>();

const emailRules = [
  (value: string) => {
    if (/.+@.+\..+/.test(value)) return true;

    return 'Email không hợp lệ';
  },
];

const blankRule = [
  (value: string) => {
    if (value.trim().length > 0) return true;

    return 'Không được để trống';
  },
];

const latRule = [
  (value: number) => {
    if (-90 <= value && value <= 90) return true;

    return 'Vĩ độ (lat) phải lớn hơn -90 và nhỏ hơn 90';
  },
];

const lonRule = [
  (value: number) => {
    if (-180 <= value && value <= 180) return true;

    return 'Kinh độ (lon) phải lớn hơn -180 và nhỏ hơn 180';
  },
];

const phoneRules = [
  (value: string) => {
    if (/(0[3|5|7|8|9])+([0-9]{8})/.test(value)) return true;

    return 'SĐT không hợp lệ';
  },
];

const imageRule = [
  (value: File[]) => {
    const acceptedImageTypes = ['image/gif', 'image/jpeg', 'image/png'];

    return (
      !value ||
      !value.length ||
      acceptedImageTypes.includes(value[0]['type']) ||
      'File không hợp lệ'
    );
  },
];

const cancelSubmit = () => {
  router.push('/bins');
};

const submit = async () => {
  if (valid1.value && valid2.value) {
    const formData = new FormData();

    if (imageFile.value && imageFile.value[0]) {
      formData.append('bin_image', imageFile.value[0], 'bin_image');
    }
    const data = new Blob(
      [
        JSON.stringify(
          decamelizeKeys({
            company: bin.value.company,
            address: bin.value.address,
            lat: bin.value.lat,
            lon: bin.value.lon,
            capacity: bin.value.capacity,
            ownerName: bin.value.owner.name,
            ownerPhone: bin.value.owner.phone,
            ownerEmail: bin.value.owner.email,
            cameraUrl: bin.value.cameraUrl,
          })
        ),
      ],
      {
        type: 'application/json',
      }
    );
    formData.append('data', data, 'data');

    console.log(formData.get('data'));
    const response = await binService.updateBin(formData, props.id);
    console.log(response);
    if (response.code === 200) {
      _alert(response.message, AlertType.Success);
      window.setTimeout(() => {
        router.push('/bins');
      }, 1000);
    }
  }
};

onBeforeMount(async () => {
  const response = await binService.getBinById(props.id);
  if (response.code === 200) {
    bin.value = response.data;
  }
});
</script>
