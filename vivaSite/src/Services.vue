<template>
  <NavBar />
  <ServicosViva />
  <VivaService />
</template>

<script setup>
import { ref } from "vue";
import NavBar from "./components/NavBar.vue";
import ServicosViva from "./components/ServicosViva/ServicosViva.vue";
import VivaService from "./components/ServicosViva/VivaService.vue";



</script>

<style scoped>
* {
  padding: 0;
  margin: 0;
  box-sizing: border-box;
  background-color: #0c0f14;
}
</style>