<template>
    <div class="card">
      <img :src="img" :alt="'Imagem ' + index" class="responsive-image">
      <p class="montserrat-text">{{ text }}</p>
    </div>
  </template>
  
  <script>
  export default {
    props: {
      img: {
        type: String,
        required: true
      },
      text: {
        type: String,
        required: true
      },
      index: {
        type: Number,
        required: true
      }
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
  .card {
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
    margin: 20px;
    background-color: #0c0f14;
  }
  
  .responsive-image {
    max-width: 100%;
    height: auto;
    margin-bottom: 15px;
  }
  
  p {
    font-size: 16px;
    line-height: 1.5;
    color: white;
  }
  

  @media (min-width: 768px) {
    .card {
      flex-direction: row;
      text-align: left;
    }
    
    .responsive-image {
      max-width: 50%;
      margin-right: 20px;
    }
  
    p {
      font-size: 18px;
    }
  }
  </style>
  