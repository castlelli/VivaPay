<template>
    <div class="sobre-viva">
      <h1>{{ title }}</h1>
      <p>{{ description }}</p>
      <img :src="logo" alt="Logo Viva" class="viva-logo"/>
    </div>
  </template>
  
  <script lang="ts">
  export default {
    props: {
      title: {
        type: String,
        default: 'Sobre o Viva'
      },
      description: {
        type: String,
        default: 'O projeto viva surgiu de uma necessidade enfrentada em eventos que necessitam de fichas. Para isso, o nosso sistema automatiza e otimiza os caixas e as lojas envolvidas para facilitar suas vendas e a verificação de seus lucros, além de ajudar os clientes fornecendo uma ficha única recarregavel durante o evento.'
      },
      logo: {
        type: String,
        default: '../../public/assets/logo_v.png'
      }
    }
  }
  </script>
  
  <style scoped>
  .sobre-viva {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px;
  }
  .viva-logo {
    width: 150px;
    height: auto;
  }
  </style>
  