<template>
  <ion-app>
    <ion-router-outlet />
    <Alternate v-if="showOptions && pageGlobal !== 'Home' && showOptions && pageGlobal !== 'loading'" :page="pageGlobal"
      @item-clicked="togglePage" />
  </ion-app>
</template>

<script setup lang="ts">
import Alternate from "@/components/Alternate.vue";
import { IonApp, IonRouterOutlet } from "@ionic/vue";
import { ref, provide } from "vue";
import { useRouter } from "vue-router";
const router = useRouter();

const showOptions = ref(true);

const pageGlobal = ref("Home");

function setPageGlobal(page: string) {
  pageGlobal.value = page;
}

async function togglePage(page: any) {
  console.log(page);
  if (page === "Consultar") {
    pageGlobal.value = "Consultar";
    await router.push({ name: "Consultar" });
  } else if (page == "Historico") {
    pageGlobal.value = "Historio";
    await router.push({ name: "Historico" });
  } else if (page === "Pagamento") {
    pageGlobal.value = "Pagamento";
    await router.push({ name: "Pagamento" });
  }
}

provide("pageGlobal", pageGlobal);
provide("setPageGlobal", setPageGlobal);
</script>
