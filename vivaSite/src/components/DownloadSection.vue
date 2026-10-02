<template>
  <div class="download-section">
    <div class="header">
      <h1 class="montserrat-title">Já é cliente?</h1>
      <h2 class="montserrat-title">Instalar o Viva:</h2>
    </div>
    <div class="products">
      <div class="product-group">
        <div class="image-wrapper">
          <img src="../../public/assets/viva/viva-manager-white.png" alt="Logo Desktop" />
        </div>
        <div class="button-group">
          <button class="btn btn-secondary download-product montserrat-text" type="button">Instalar para</button>
          <div class="dropdown">
            <button class="btn btn-secondary dropdown-toggle select-device montserrat-text" type="button"
              id="dropdownMenuButton" data-bs-toggle="dropdown" aria-expanded="false">
              {{ managerSelectedDevice != null ? managerSelectedDevice.file : 'Selecione o dispositivo.' }}
            </button>
            <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton">
              <li v-for="(item, index) in dropdownItemsDesktop" :key="index">
                <a class="dropdown-item montserrat-text" @click="selectDeviceManager(item)">{{ item.name }}</a>
              </li>
            </ul>
          </div>
        </div>
      </div>

      <div class="product-group">
        <div class="image-wrapper">
          <img src="../../public/assets/viva/viva-sales-white.png" alt="Logo Mobile" />
        </div>
        <div class="button-group">
          <button class="btn btn-secondary download-product montserrat-text" type="button">Instalar para</button>
          <div class="dropdown">
            <button class="btn btn-secondary dropdown-toggle select-device montserrat-text" type="button"
              id="dropdownMenuButton" data-bs-toggle="dropdown" aria-expanded="false">
              {{ salesSelectedDevice != null ? salesSelectedDevice.file : 'Selecione o dispositivo.' }}
            </button>
            <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton">
              <li v-for="(item, index) in dropdownItemsMobile" :key="index">
                <a class="dropdown-item montserrat-text" @click="selectDeviceSales(item)">{{ item.name }}</a>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'DownloadSection',
  data() {
    return {
      dropdownItemsDesktop: [
        { name: 'Macbook (Apple)', value: 'darwin', file: 'MacOS (10.15+)' },
        { name: 'Windows', value: 'windows', file: 'Windows (x64)' }
      ],
      dropdownItemsMobile: [
        { name: 'IOS (Iphone)', value: 'darwin', file: 'IOS (8.5+)' },
        { name: 'Android', value: 'android', file: 'Android (10+)' }
      ],
      salesSelectedDevice: null,
      managerSelectedDevice: null
    }
  },
  methods: {
    selectDeviceSales(item) {
      this.salesSelectedDevice = item
    },
    selectDeviceManager(item) {
      this.managerSelectedDevice = item
    }
  }
}

</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Montserrat:ital,wght@0,100..900;1,100..900&display=swap');

.montserrat-title {
  font-family: "Montserrat", sans-serif;
  font-optical-sizing: auto;
  font-weight: 700;
  font-style: normal;
}

.montserrat-text {
  font-family: "Montserrat", sans-serif;
  font-optical-sizing: auto;
  font-weight: 550;
  font-style: normal;
}

.dropdown-item,
.dropdown-menu {
  background-color: #1573b1;
  color: #ffffff;
}

.dropdown-item:hover {
  background-color: #0099ff;
}

.dropdown-item,
li {
  width: 100%;
  box-sizing: border-box;
}

.select-device {
  border-radius: 0 20px 20px 0;
  margin-left: 0.5px;
}

.download-product {
  border-radius: 20px 0 0 20px;
  margin-right: 0.5px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.download-product .desktop-button {
  margin-right: 5px;
}

.button-group {
  display: flex;
}

.download-product,
.select-device {
  background-color: #1573b1;
  color: white;
  border: #0099ff;
}

.image-wrapper {
  height: 40vh;
  width: 40vh;
  display: flex;
  justify-content: center;
  align-items: center;
}

.image-wrapper img {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}

.download-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border-radius: 10px;
  margin-bottom: 5rem
}

.header {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
  flex-direction: column;
}

.header h1,
.header h2 {
  color: white;
  margin-right: 10px;
  font-size: xx-large;
  display: block;
}

h1 {
  margin-top: 2.5rem;
}

h2 {
  margin-top: 0.5rem;
}

.products {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
}

.product-group {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 0 20px;
}


.desktop-button,
.mobile-button {
  background-color: #1a8fe3;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  font-size: 1rem;
  transition: background-color 0.3s ease;
  margin-top: 10px;
}

.desktop-button:hover .mobile-button:hover {
  background-color: #1573b1;
}



@media (max-width: 768px) {
  .products {
    flex-direction: column;
  }

  .product-group {
    margin: 0;
  }
}
</style>