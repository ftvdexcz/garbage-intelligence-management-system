<template>
  <v-layout class="h-screen">
    <nav-bar />

    <v-app-bar flat title="Application bar"></v-app-bar>

    <v-main> Main Content </v-main>
  </v-layout>
</template>

<script lang="ts" setup>
import NavBar from '@/components/layout/NavBar.vue';
</script>
