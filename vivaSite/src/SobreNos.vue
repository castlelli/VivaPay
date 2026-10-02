<template>
  <NavBar />
  <div class="page">
    <SobreViva />
    <VivaEquipe />
    <NossaHistoria />
  </div>
</template>

<script setup>
import { ref } from "vue";
import NavBar from "./components/NavBar.vue";
import SobreViva from "./components/SobreViva/SobreViva.vue";
import VivaEquipe from "./components/SobreViva/VivaEquipe.vue";
import NossaHistoria from "./components/SobreViva/NossaHistoria.vue";

</script>

<style scoped>
* {
  padding: 0;
  margin: 0;
  box-sizing: border-box;
  background-color: #0c0f14;
}
</style>