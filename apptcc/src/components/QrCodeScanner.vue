<template>
  <div class="readingQrCode" v-if="showReader" @click="closeReader">
    <div class="container-scanner">
      <qr-stream @decode="onDecode" class="mb">
        <div style="color: red" class="frame"></div>
      </qr-stream>
    </div>
  </div>
  <div v-if="!showReader" class="qrcode-option" @click="openReader">
    <i class="fa-solid fa-qrcode icon"></i>
  </div>
</template>

<script>
import { defineComponent } from "vue";
import { QrStream, QrCapture, QrDropzone } from "vue3-qr-reader";

export default defineComponent({
  name: "QrCodeScanner",
  data() {
    return {
      showReader: false,
    };
  },
  components: {
    QrStream,
    QrCapture,
    QrDropzone,
  },
  methods: {
    openReader() {
      this.showReader = true;
      this.$emit("disabledOptions");
    },
    closeReader() {
      this.showReader = false;
      this.$emit("activedOptions");
    },
  },
});
</script>

<style scoped>
.qrcode-option {
  width: 200px;
  height: 200px;
  background-color: #ffffff;
  border-radius: 15px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.readingQrCode {
  position: absolute;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.9);
  display: flex;
  justify-content: center;
  align-items: center;
}

.container-scanner {
  width: 250px;
  height: 250px;
  border-radius: 15px;
  background-color: #ffffffff;
}

.icon {
  color: #000000;
  font-size: 160px;
}
</style>
