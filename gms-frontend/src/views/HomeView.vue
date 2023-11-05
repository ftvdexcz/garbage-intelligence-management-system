<template>
  <v-layout>
    <v-app-bar :elevation="1" flat title="Application bar"></v-app-bar>

    <nav-bar />

    <v-main> main </v-main>
  </v-layout>
</template>

<script lang="ts" setup>
import NavBar from '@/components/layout/NavBar.vue';
</script>

<style scoped></style>
