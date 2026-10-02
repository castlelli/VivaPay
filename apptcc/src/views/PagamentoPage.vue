<template>
  <ion-page>
    <div v-if="isLoadingPage">Carregando...</div>
    <div v-else style="height: 100vh; background-color: #fff">
      <div class="title-page">
        <div class="exit" @click="exit">
          <i class="fa-solid fa-right-from-bracket"></i>
        </div>
        <p>PAGAMENTO</p>
        <QrCodeScanner
          @disabledOptions="toggleViewOptions"
          @activedOptions="toggleViewOptions"
        />
      </div>
      <div class="header-box-one">
        <div class="campo-input">
          <div class="campo">
            <InputCampo
              :campo="'Voucher'"
              label="Voucher"
              v-model="inputVoucher"
            />
          </div>
        </div>
      </div>
      <div class="container">
        <div class="campo">
          <InputCampo
            :campo="'10'"
            :label="'Valor'"
            v-model="formattedValue"
            @input="handleInput"
          />
        </div>
        <Button
          :valor="'PAGAR'"
          :isLoading="isLoading"
          @item-clicked="pagarValor"
        />
      </div>
    </div>
    <PopUp
      v-show="showComponent"
      :message="message"
      :type="type"
      :page="'Pagamento'"
      :function-click="functionClickPopUp"
      @close-popup="hideComponent"
    />
  </ion-page>
</template>

<script lang="ts">
import { defineComponent, inject } from "vue";
import { QrCapture, QrDropzone } from "vue3-qr-reader";
import axios from "axios";
import PopUp from "@/components/PopUp.vue";
import InputCampo from "@/components/InputCampo.vue";
import Button from "@/components/Button.vue";
import Alternate from "@/components/Alternate.vue";
import { onBeforeUnmount } from "vue";
import { IonContent, IonPage } from "@ionic/vue";

export default defineComponent({
  name: "PagamentoPage",
  components: {
    IonPage,
    IonContent,
    InputCampo,
    Button,
    Alternate,
    QrCapture,
    QrDropzone,
    PopUp,
  },
  setup() {
    const pageGlobal = inject("pageGlobal");
    return { pageGlobal };
  },
  data() {
    return {
      inputVoucher: "",
      inputValor: 0,
      storedData: "",
      message: "",
      isLoadingPage: true,
      type: "",
      isLoading: false,
      showComponent: false,
      showOptions: true,
      functionClickPopUp: '',
    };
  },
  computed: {
    formattedValue: {
      get() {
        return (this.inputValor / 100).toFixed(2);
      },
      set(newValue: any) {
        this.inputValor = parseInt(newValue.replace(/\D/g, "")) || 0;
      },
    },
  },
  ionViewWillEnter() {
    this.checkAuthentication();
  },
  methods: {
    toggleViewOptions() {
      if (this.showOptions) this.showOptions = false;
      else this.showOptions = true;
    },
    async checkAuthentication() {
      this.storedData = localStorage.getItem("token") as string;
      if (!this.storedData) {
        this.showComponent = true;
        this.type = "sessao";
        this.message = "Sua sessão expirou, faço login novamente por favor!";
        this.functionClickPopUp = 'exit'
      } else {
        this.showComponent = false;
      }
      this.isLoadingPage = false;
    },
    handleInput(event: any) {
      const input = event.target.value;
      this.formattedValue = input;
    },
    hideComponent() {
      this.showComponent = false;
    },
    onDecode(data: string) {
      this.inputVoucher = data;
    },
    async exit() {
      localStorage.clear();
      this.pageGlobal = 'Home'
      await this.$router.push({ name: "Home" }).then(() => {});
    },
    openHelp() {
      this.message = `
      Está tela serve para a realização
      do pagamento.
      Receba o voucher por QRCODE
      ou digite-o no campo voucher.
      Faça a mesma coisa com o valor,
      inserindo desta vez, no campo
      valor.
      Depois clique em pagar e
      aparecerá um PopUp
      com o resultado do pagemento.`;
      this.type = "help";
      this.showComponent = true;
    },
    async pagarValor() {
      this.storedData = localStorage.getItem("token") as string;

      const requestBody = {
        voucher: this.inputVoucher,
        valor: this.formattedValue,
      };
      axios
        .put(`https://tcc-r46r.onrender.com/caixa/pagamento`, requestBody, {
          headers: {
            Authorization: `Bearer ${this.storedData}`,
          },
        })
        .then((response) => {
          if (response.data.sucesso != true) {
            this.showComponent = true;
            this.message = response.data.mensagem;
            this.type = "error";
            this.inputVoucher = "";
            this.formattedValue = "";
          } else {
            this.showComponent = true;
            this.message = response.data.mensagem;
            this.type = "success";
            this.inputVoucher = "";
            this.formattedValue = "";
          }
        })
        .catch((error) => {
          console.error("Erro ao chamar a API:", error);
        });
    },
  },
});
</script>

<style scoped>
.loader {
  margin-top: 10px;
  width: 150px;
  height: 150px;
  border: 3px solid #bad8ed;
  border-top: 3px solid #1e6ea3;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

.isPageLoading {
  background-color: rgba(255, 255, 255, 0.5);
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }

  100% {
    transform: rotate(360deg);
  }
}

* {
  margin: 0;
  box-sizing: border-box;
}

.header-box-one {
  width: 100%;
  height: 15%;
  background-color: #000000;
  display: flex;
  flex-direction: column;
  justify-content: end;
  align-items: end;
  margin-bottom: 0px;
}

.qrcode {
  width: 60%;
}

.title-page {
  border-radius: 0 0 40px 0;
  background-color: #000000;
  height: 60%;
  align-items: center;
  width: 100%;
  row-gap: 20px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.option-help {
  width: 30px;
  height: 30px;
  background-color: #000000;
  border: 2px solid #ffffff;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.exit {
  width: 30px;
  height: 30px;
  background-color: #000000;
  border: 2px solid #ffffff;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  position: absolute;
  top: 5%;
  right: 3%;
  transform: translate(-50%, -50%);
}

.title-page > p {
  font-family: "Koulen", sans-serif;
  color: #ffffff;
}

.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  row-gap: 30px;
}

p {
  font-size: 45px;
}

.campo {
  display: flex;
  align-items: center;
  justify-content: center;
  column-gap: 10px;
  background-color: white;
  border-radius: 40px;
}

.campo-input {
  height: 100%;
  width: 100%;
  background-color: #ffffff;
  border-radius: 50px 0 0 0;
  display: flex;
  justify-content: center;
}
</style>
