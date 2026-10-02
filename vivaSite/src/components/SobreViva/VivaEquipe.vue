<template>
    <section class="equipe">
      <h2 class="montserrat-title">Equipe</h2>
      <div class="team-photo">
        <img src="../../../public/assets/foto_equipe.jpg" alt="Equipe">
      </div>
      <div class="team-info">
        <p class="montserrat-text" v-for="(membro, index) in equipe" :key="index">{{ membro }}</p>
      </div>
    </section>
  </template>
  
  <script>
  export default {
    data() {
      return {
        equipe: [
          "Leticia Garcia de Oliveira - Dev",
          "Ana Luisa Leal - Project Manager",
          "Luis Felipe Matosinho Silva - Tech Lead",
          "Vinicius Sampaio Castelli de Deus - Dev",
          "Richard Wallace Oliveira Camargo - Dev",
          "Juliana Ayumi Tano - UX/UI Lead",
          "Ana Elisa Avante Dourado - Dev"
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

  p {
    font-family: Arial, sans-serif;
    color: #fff;
}

h2 {
    text-align: center;
    color: #fff;
}


.equipe .team-photo {
    display: flex;
    justify-content: center;
}

.equipe img {
    max-width: 75%;
    border-radius: 10px;
}

.equipe .team-info {
    text-align: center;
    margin-top: 10px;
}

.sobre-viva .content {
    display: flex;
    justify-content: space-around;
    align-items: center;
    gap: 20px;
    flex-wrap: wrap;
    padding: 20px;
}


  </style>
  