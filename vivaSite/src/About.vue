<template>
  <NavBar />
  <div class="page">
    <PromoCard />
    <DownloadSection />
  </div>
</template>

<script lang="ts" setup>
//@ts-ignore
import { ref } from "vue";
//@ts-ignore
import NavBar from "@/components/NavBar.vue";
//@ts-ignore
import PromoCard from "./components/Promo/PromoCard.vue";
//@ts-ignore
import PromoSection from "./components/Promo/PromoSection.vue";
//@ts-ignore
import FeaturesCarousel from "./components/FeaturesCarousel/FeaturesCarousel.vue";
//@ts-ignore
import CompContato from "./components/ContactSection/CompContato.vue";
//@ts-ignore
import DownloadSection from "./components/DownloadSection.vue";
//@ts-ignore
import FeaturesGrid from "./components/FeaturesCarousel/FeaturesGrid.vue";


const buttonValue = ref("Value");
const buttonImage = ref("./public/assets/gmail.png");

</script>

<style scoped>
* {
  padding: 0;
  margin: 0;
  box-sizing: border-box;
}
</style>