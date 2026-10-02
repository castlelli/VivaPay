<template>
    <div class="row align-items-center viva-card">
      <div class="col-12 col-md-6 text-center">
        <img :src="imgSrc" :alt="title" class="img-fluid viva-img">
      </div>
      <div class="col-12 col-md-6">
        <h3 class="text-center text-md-left montserrat-title">{{ title }}</h3>
        <p class="montserrat-text">{{ description }}</p>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    props: {
      title: {
        type: String,
        required: true
      },
      imgSrc: {
        type: String,
        required: true
      },
      description: {
        type: String,
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
  .viva-card {
    margin-bottom: 50px;
  }
  
  .viva-img {
    max-width: 100px;
    margin: 0 auto 20px;
  }
  
  h3 {
    font-size: 1.5rem;
    font-weight: bold;
    margin-bottom: 15px;
  }
  
  @media (min-width: 768px) {
    h3 {
      font-size: 2rem;
    }
  }
  </style>
  