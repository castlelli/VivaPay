<template>
    <section class="sobre-viva">
      <h2 class="montserrat-title">Sobre o Viva</h2>
      <div class="content">
        <p class="montserrat-text">{{ texto }}</p>
        <img src="../../../public/assets/logo_v.png" alt="Logo Viva">
      </div>
    </section>
  </template>
  
  <script>
  export default {
    data() {
      return {
        texto: "O projeto viva surgiu de uma necessidade enfrentada em eventos que necessitam de fichas. Para isso, o nosso sistema automatiza e otimiza os caixas e as lojas envolvidas para facilitar suas vendas e a verificação de seus lucros, além de ajudar os clientes fornecendo uma ficha única recarregavel durante o evento ."
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




.sobre-viva .content {
    display: flex;
    justify-content: space-around;
    align-items: center;
    gap: 20px;
    padding: 20px;
}

.sobre-viva img {
    max-width: 300px;
}


  </style>
  