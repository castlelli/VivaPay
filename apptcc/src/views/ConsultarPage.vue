<template>
  <ion-page>
    <div v-if="isLoadingPage">Carregando...</div>
    <div v-else style="height: 100vh; background-color: #fff">
      <div class="title-page">
        <div class="exit" @click="exit">
          <i class="fa-solid fa-right-from-bracket"></i>
        </div>
        <p>Consulta</p>
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
        <Button
          :valor="'VERIFICAR'"
          :isLoading="isLoading"
          @item-clicked="verificarSaldo"
        />
      </div>
    </div>

    <PopUp
      v-show="showComponent"
      :message="message"
      :type="type"
      :saldo="saldo"
      :page="'Consultar'"
      @close-popup="hideComponent"
      :functionClick="functionClickPopUp"
    />
  </ion-page>
</template>

<script lang="ts">
import { defineComponent, inject } from "vue";
import axios from "axios";
import PopUp from "@/components/PopUp.vue";
import InputCampo from "@/components/InputCampo.vue";
import Button from "@/components/Button.vue";
import QrCodeScanner from "@/components/QrCodeScanner.vue";
import { IonContent, IonPage } from "@ionic/vue";

type PageGlobalType = string;

export default defineComponent({
  name: "ConsultarPage",
  components: {
    QrCodeScanner,
    IonPage,
    PopUp,
    IonContent,
    InputCampo,
    Button,
  },
  setup() {
    const pageGlobal = inject("pageGlobal");
    return { pageGlobal };
  },
  data() {
    return {
      showOptions: true,
      inputVoucher: "",
      storedData: "",
      message: "",
      inputValor: 0,
      type: "",
      showComponent: false,
      saldo: "",
      isLoadingPage: true,
      isLoading: false,
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
    this.showOptions = true
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
    clear() {
      this.inputVoucher = "";
      this.message = "";
      this.inputValor = 0;
      this.saldo = "";
    },
    handleInput(event: any) {
      const input = event.target.value;
      this.formattedValue = input;
    },
    onDecode(data: string) {
      this.inputVoucher = data;
      this.verificarSaldo();
    },
    hideComponent() {
      this.showComponent = false;
      this.clear();
    },
    async exit() {
      localStorage.clear();
      this.pageGlobal = "Home";
      await this.$router.push({ name: "Home" }).then(() => {});
    },
    openHelp() {
      this.message = `
      Está tela serve para a verificação
      de saldo.
      Receba o voucher por QRCODE
      ou digite-o no campo voucher.
      Logo após o voucher ser
      inserido, aparecerá um PopUp
      com o saldo do cliente.`;
      this.type = "help";
      this.showComponent = true;
    },
    async verificarSaldo() {
      this.storedData = localStorage.getItem("token") as string;
      this.isLoading = true;
      axios
        .get(`https://tcc-r46r.onrender.com/caixa/saldo/${this.inputVoucher}`, {
          headers: {
            Authorization: `Bearer ${this.storedData}`,
          },
        })
        .then((response) => {
          this.message = response.data.mensagem;
          this.saldo = "$" + response.data.saldo;
          this.type = "success";
        })
        .catch((then) => {
          this.message = "Usuario não encontrado";
          this.type = "error";
        })
        .finally(() => {
          this.showComponent = true;
          this.inputVoucher = "";
          this.isLoading = false;
        });
    },
    async pagarValor() {
      this.storedData = localStorage.getItem("token") as string;
      this.isLoading = true;
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
            this.message = response.data.mensagem;
            this.type = "error";
          } else {
            this.message = response.data.mensagem;
            this.type = "success";
          }
        })
        .catch((error) => {
          console.error("Erro ao chamar a API:", error);
        })
        .finally(() => {
          this.showComponent = true;
          this.inputVoucher = "";
          this.formattedValue = "";
          this.isLoading = false;
        });
    },
  },
});
</script>

<style scoped>
@import url("https://fonts.googleapis.com/css?family=Imprima");
@import url("https://fonts.cdnfonts.com/css/koulen");

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
}

.title-page {
  border-radius: 0 0 40px 0;
  background-color: #000000;
  height: 65%;
  align-items: center;
  width: 100%;
  row-gap: 20px;
  display: flex;
  flex-direction: column;

  justify-content: center;
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

.qrcode {
  width: 60vw;
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

.campo-input {
  height: 100%;
  width: 100%;
  background-color: #ffffff;
  border-radius: 50px 0 0 0;
  display: flex;
  justify-content: center;
}
</style>
