<template>
    <NavBar />
    <div class="page">
        <PromoCard />
        <FeaturesGrid id="features"/>
        <CompContato id="help" />
    </div>
</template>

<script setup>
import { ref } from "vue";
import NavBar from "./components/NavBar.vue";
import PromoCard from "./components/Promo/PromoCard.vue";
import DownloadSection from "./components/DownloadSection.vue";
import FeaturesGrid from "./components/FeaturesCarousel/FeaturesGrid.vue";
import CompContato from "./components/ContactSection/CompContato.vue";
import SobreNos from "./SobreNos.vue";

const buttonValue = ref("Value");

</script>

<style scoped>
* {
    margin: 0;
    box-sizing: border-box;
    background-color: #0c0f14;

}

.page {
    width: 100%;
    background-color: #0c0f14;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: space-around;
    padding: 20px;
}
</style>