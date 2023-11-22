<template>
  <v-toolbar flat color="primary" class="d-flex align-center">
    <v-toolbar-title>Thêm mới điểm thu</v-toolbar-title>
  </v-toolbar>
  <v-container class="mx-auto mt-8" style="width: 70%">
    <v-row align="start" style="height: 150px">
      <v-col lg="8" cols="12">
        <v-card variant="outlined">
          <template #title> Thông tin điểm thu </template>

          <v-card-text>
            <v-form v-model="valid">
              <v-container>
                <v-row>
                  <v-col cols="12" class="mb-3">
                    <v-text-field
                      v-model="firstname"
                      :rules="nameRules"
                      :counter="10"
                      label="Mã doanh nghiệp"
                      required
                      hide-details
                    ></v-text-field>
                  </v-col>

                  <v-col cols="12" class="mb-3">
                    <v-text-field
                      v-model="lastname"
                      :rules="nameRules"
                      :counter="10"
                      label="Tên doanh nghiệp"
                      hide-details
                      required
                    ></v-text-field>
                  </v-col>

                  <v-col cols="12" class="mb-3">
                    <v-text-field
                      v-model="email"
                      :rules="emailRules"
                      label="Chủ sở hữu"
                      hide-details
                      required
                    ></v-text-field>
                  </v-col>

                  <v-col cols="12" class="mb-3">
                    <v-text-field
                      v-model="email"
                      :rules="emailRules"
                      label="SĐT"
                      hide-details
                      required
                    ></v-text-field>
                  </v-col>

                  <v-col cols="12" class="mb-3">
                    <v-text-field
                      v-model="email"
                      :rules="emailRules"
                      label="Email"
                      hide-details
                      required
                    ></v-text-field>
                  </v-col>

                  <v-col cols="12" class="mb-3">
                    <v-text-field
                      v-model="email"
                      :rules="emailRules"
                      label="Địa chỉ"
                      hide-details
                      required
                    ></v-text-field>
                  </v-col>

                  <v-col cols="12" class="mb-3">
                    <v-text-field
                      v-model="email"
                      :rules="emailRules"
                      label="Khối lượng tối đa"
                      hide-details
                      required
                    ></v-text-field>
                  </v-col>

                  <v-col cols="12" class="mb-3">
                    <v-text-field
                      v-model="email"
                      :rules="emailRules"
                      label="Camera"
                      hide-details
                      required
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
            <v-form v-model="valid">
              <v-container>
                <v-row>
                  <v-col cols="12" class="mb-3">
                    <v-text-field
                      v-model="firstname"
                      :rules="nameRules"
                      :counter="10"
                      label="Vĩ độ"
                      required
                      hide-details
                    ></v-text-field>
                  </v-col>

                  <v-col cols="12">
                    <v-text-field
                      v-model="firstname"
                      :rules="nameRules"
                      :counter="10"
                      label="Kinh độ"
                      required
                      hide-details
                    ></v-text-field>
                  </v-col>
                </v-row>
              </v-container>
            </v-form>
          </v-card-text>

          <div class="v-card-title">Ảnh điểm thu</div>
          <v-card-text>
            <v-btn color="primary" append-icon="fas fa-paperclip">
              Tải lên
            </v-btn>
          </v-card-text>
        </v-card>

        <v-container>
          <v-row justify="space-between">
            <v-col cols="6">
              <v-btn width="100%" color="grey-lighten-5"> Hủy </v-btn>
            </v-col>

            <v-col cols="6">
              <v-btn
                width="100%"
                color="primary"
                append-icon="fas fa-floppy-disk"
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
import { ref } from 'vue';

const valid = ref<boolean>(false);
const firstname = ref<string>('');
const lastname = ref<string>('');
const email = ref<string>('');

const nameRules = [
  (value: string) => {
    if (value) return true;

    return 'Name is required.';
  },
  (value: string) => {
    if (value?.length <= 10) return true;

    return 'Name must be less than 10 characters.';
  },
];

const emailRules = [
  (value: string) => {
    if (value) return true;

    return 'E-mail is requred.';
  },
  (value: string) => {
    if (/.+@.+\..+/.test(value)) return true;

    return 'E-mail must be valid.';
  },
];
</script>
