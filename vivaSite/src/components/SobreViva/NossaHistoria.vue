<template>
    <section class="nossa-historia">
      <h2 class="montserrat-tile">Nossa história</h2>
      <div class="container">
        <HistoriaCard
          v-for="(item, index) in historia"
          :key="index"
          :img="item.img"
          :text="item.text"
          :index="index"
        />
      </div>
    </section>
  </template>
  
  <script>
  import HistoriaCard from './HistoriaCard.vue';
  
  export default {
    components: {
      HistoriaCard
    },
    data() {
      return {
        historia: [
          { img: "../../public/assets/group_teamworking.png", text: "Texto para a primeira imagem" },
          { img: "../../public/assets/teamworking.png", text: "Texto para a segunda imagem" },
          { img: "../../public/assets/idea_teamworking.png", text: "Texto para a terceira imagem" }
        ]
      };
    }
  };
  </script>
  
  <style scoped>
  .montserrat-title {
  font-family: "Montserrat", sans-serif;
  font-optical-sizing: auto;
  font-weight: 700;
  font-style: normal;
}

.montserrat-text {
  font-family: "Montserrat", sans-serif;
  font-optical-sizing: auto;
  font-weight: 400;
  font-style: normal;
}
  .nossa-historia {
    padding: 40px;
    background-color: #0c0f14;
  }
  
  h2 {
    text-align: center;
    font-size: 28px;
    color: white;
    
  }
  
  .container {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
  }
  
  /* Media Query for larger screens */
  @media (min-width: 768px) {
    .container {
      justify-content: space-between;
    }
  }
  </style>
  