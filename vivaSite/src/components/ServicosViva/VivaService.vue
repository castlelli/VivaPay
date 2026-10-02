<template>
    <section class="viva-section py-5 bg-dark text-light">
      <div class="container">
        <VivaCard
          v-for="(card, index) in getCards()"
          :key="index"
          :title="card.title"
          :imgSrc="card.imgSrc"
          :description="card.description"
        />
      </div>
    </section>
  </template>
  
  <script>
  import VivaCard from './VivaCard.vue';
  
  export default {
    components: {
      VivaCard
    },
    methods: {
      getCards() {
        return [
          {
            title: "Viva Manager",
            imgSrc: "../../public/assets/viva/viva-manager-white.png",
            description: "Viva Manager auxilia os administradores dos eventos na sua organização no cadastro de lojas e na geração de relatório de suas vendas."
          },
          {
            title: "Viva Voucher",
            imgSrc: "../../public/assets/viva/viva-voucher-white.png",
            description: "Viva Voucher é dado ao cliente do evento o qual pode recarregá-lo com o seu dinheiro para poder utilizá-las nas lojas."
          },
          {
            title: "Viva Sales",
            imgSrc: "../../public/assets/viva/viva-sales-white.png",
            description: "Viva Sales é um aplicativo para que as lojas cadastradas possam realizar a venda de seus produtos no evento."
          }
        ];
      }
    }
  };
  </script>
  
  <style scoped>
  

  .viva-section {
    background-color: #0e1016;
  }
  
  .container {
    max-width: 1140px;
    margin: 0 auto;
  }
  </style>
  