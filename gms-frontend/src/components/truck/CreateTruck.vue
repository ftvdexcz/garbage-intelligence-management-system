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
                  v-model="firstname"
                  :rules="nameRules"
                  :counter="10"
                  label="Biển số xe"
                  required
                  hide-details
                ></v-text-field>
              </v-col>

              <v-col cols="12" class="mb-3">
                <v-text-field
                  v-model="company"
                  :rules="nameRules"
                  :counter="10"
                  label="Đơn vị thu gom"
                  hide-details
                  required
                ></v-text-field>
              </v-col>

              <v-col cols="12" class="mb-3">
                <v-text-field
                  v-model="email"
                  :rules="emailRules"
                  label="Khối lượng tối đa (kg)"
                  hide-details
                  required
                ></v-text-field>
              </v-col>
            </v-row>
          </v-container>
        </v-form>
      </v-card-text>
    </v-card>

    <v-container>
      <v-btn color="grey-lighten-5" class="mr-4"> Hủy </v-btn>
      <v-btn color="primary" append-icon="fas fa-floppy-disk"> Lưu </v-btn>
    </v-container>
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
