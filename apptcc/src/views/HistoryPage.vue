<template>
  <ion-page>
    <div v-if="isLoadingPage" class="isPageLoading">
      <div class="loadingContainer">
        <p class="loadingText">CARREGANDO...</p>
        <div class="containerLoading">
          <div class="loader-line"></div>
        </div>
      </div>
    </div>
    <div v-if="!isLoadingPage" style="height: auto; background-color: #fff; overflow-y: scroll">
      <div class="container">
        <div class="bg-header-black">
          <div class="exit" @click="exit">
            <i class="fa-solid fa-right-from-bracket"></i>
          </div>
          <div class="header d-flex">
            <div class="left-header">
              <i class="fa-solid fa-user style-user"></i>
              <div class="hello-text">
                <p>Bem vindo de volta,</p>
                <p>
                  <span class="font-name-header">{{ nameLoja }}</span>
                </p>
              </div>
            </div>
            <!-- <i class="fa-regular fa-bell"></i> -->
          </div>
          <div class="info-card">
            <p class="font-account">Sua conta</p>
            <div class="info-in">
              <p>${{ saldo }}</p>
              <!-- <i class="fa-regular fa-eye"></i> -->
            </div>
          </div>
        </div>
        <div class="texts">
          <div class="bg-texts-black">
            <p class="font-recenty">Atividades Recentes</p>
            <div class="view-all">
              <p class="font-view">Ver tudo</p>
              <i class="fa-solid fa-arrow-right"></i>
            </div>
          </div>
        </div>
        <div style="width: 100%">
          <div class="cards" v-for="transacao in listTransacoes">
            <div class="card-transf">
              <div class="card-info">
                <i class="fa-solid fa-user style-user"></i>
                <div class="info-text">
                  <p class="font-name-card">CLIENTE</p>
                  <p class="font-prod-card">{{ transacao.voucher }}</p>
                </div>
              </div>
              <div class="value-date">
                <p class="font-value" :class="{
                  debito: transacao.tipo === 'C',
                  credito: transacao.tipo === 'D',
                }">
                  +${{ transacao.valor }},00
                </p>
                <p class="font-date">
                  {{ formatarData(transacao.criadoEm) }}
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <PopUp v-show="showComponent" :message="message" :type="type" :saldo="saldo" :page="'Consultar'"
      :function-click="functionClickPopUp" @close-popup="hideComponent" />
  </ion-page>
</template>

<script lang="ts">
import { defineComponent, inject } from "vue";
import axios from "axios";
import PopUp from "@/components/PopUp.vue";
import InputCampo from "@/components/InputCampo.vue";
import Button from "@/components/Button.vue";
import Alternate from "@/components/Alternate.vue";
import { IonContent, IonPage } from "@ionic/vue";
import { DateTime } from "luxon";

interface Transacao {
  tipo: "D" | "C";
  valor: number;
  criadoEm: string;
  voucher: string;
}

export default defineComponent({
  name: "ConsultarPage",
  components: {
    IonPage,
    PopUp,
    IonContent,
    InputCampo,
    Button,
    Alternate,
  },
  data() {
    return {
      nameLoja: "",
      storedData: "",
      vendedorEvento: "",
      listTransacoes: [] as Transacao[],
      inputVoucher: "",
      message: "",
      inputValor: 0,
      type: "",
      showComponent: false,
      saldo: "",
      isLoadingPage: true,
      functionClickPopUp: '',
    };
  },
  setup() {
    const pageGlobal = inject("pageGlobal");
    return { pageGlobal };
  },
  ionViewWillEnter() {
    this.checkAuthentication();
  },
  created() {
    this.getVendedorEvento();
  },
  methods: {
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
    },
    formatarData(dataISO: string) {
      return DateTime.fromISO(dataISO).toFormat("MM-dd-yyyy hh:mma");
    },
    async exit() {
      localStorage.clear();
      this.pageGlobal = 'Home'
      await this.$router.push({ name: "Home" }).then(() => { });
    },
    hideComponent() {
      this.showComponent = false;
    },
    getVendedorEvento() {
      this.pageGlobal = 'loading'
      this.storedData = localStorage.getItem("token") as string;
      axios
        .get(
          "https://tcc-r46r.onrender.com/vendedorevento/selecao/buscarsaldo",
          {
            headers: {
              Authorization: `Bearer ${this.storedData}`,
            },
          }
        )
        .then((response) => {
          this.saldo = response.data.saldo[0].saldo;
        });
      axios
        .get(
          `https://tcc-r46r.onrender.com/vendedorevento/selecao/porvendedorproprio`,
          {
            headers: {
              Authorization: `Bearer ${this.storedData}`,
            },
          }
        )
        .then((response) => {
          this.vendedorEvento =
            response.data.vendedorEventos[0].idVendedorEvento;
          this.nameLoja = response.data.usuarioAtual[0].nomeUsuario;
          axios
            .get(
              `https://tcc-r46r.onrender.com/transacoes/selecao/porvendedorevento/${this.vendedorEvento}/1`,
              {
                headers: {
                  Authorization: `Bearer ${this.storedData}`,
                },
              }
            )
            .then((response) => {
              this.listTransacoes = response.data.transacoes as Transacao[];
            })
            .catch((then) => {
              console.error(then);
            })
            .finally(() => {
              this.isLoadingPage = false;
              this.pageGlobal = 'Historico'
            });
        })
        .catch((then) => {
          console.error(then);
        });
    },
    soma() {
      if (Array.isArray(this.listTransacoes)) {
        const total = this.listTransacoes.reduce(
          (total, item) => total + item.valor,
          0
        );
        return total.toFixed(2);
      }
      return "0.00";
    },
    // gerarComprovante() {
    //   axios({
    //     url: 'https://tcc-r46r.onrender.com/pdf/criacao/comprovante',
    //     method: 'GET',
    //     responseType: 'blob',
    //   })
    //     .then((response) => {
    //       const url = window.URL.createObjectURL(new Blob([response.data]));

    //       const link = document.createElement('a');
    //       link.href = url;
    //       link.setAttribute('download', 'comprovante.pdf');
    //       document.body.appendChild(link);
    //       link.click();

    //       // Limpar o link temporário
    //       document.body.removeChild(link);
    //     })
    //     .catch((error) => {
    //       console.error('Erro ao gerar o comprovante:', error);
    //     });
    // }
  },
});
</script>

<style scoped>
@import url("https://fonts.googleapis.com/css?family=Imprima");
@import url("https://fonts.cdnfonts.com/css/koulen");
@import url("https://fonts.googleapis.com/css2?family=Inter:wght@400;700&display=swap");

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
  font-family: "Inter", sans-serif;
}

body {
  overflow-y: scroll;
}

.bg-header-black {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #000000;
  border-radius: 0 0 50px 0;
}

.style-user {
  width: 75px;
  height: 75px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50px;
  background-color: #cacaca;
  font-size: 40px;
  color: #7d7d7d;
}

.container {
  display: flex;
  align-items: center;
  flex-direction: column;
  min-height: 100vh;
  height: auto;
}

.loadingContainer {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 10px;
}

.containerLoading {
  position: relative;
  width: 200px;
  height: 3px;
  background-color: #2892cf34;
  overflow: hidden;
}

.loader-line {
  position: absolute;
  width: 100%;
  height: 100%;
  background-color: #2893cf;
  animation: loading 2s linear infinite;
}


@keyframes loading {
  0% {
    transform: translateX(-100%);
  }

  100% {
    transform: translateX(100%);
  }
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
  top: 5vh;
  right: 3%;
  transform: translate(-50%, -50%);
}

.alternate {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 8vh;
}

.header {
  width: 100%;
  height: 25vw;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 100px;
}

.header>i {
  color: #197bb2;
  font-size: 40px;
}

.left-header {
  display: flex;
  align-items: center;
  column-gap: 30px;
}

.hello-text {
  color: #fff;
}

.font-name-header {
  font-weight: 700;
  color: #fff;
  font-size: 30px;
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

.info-card {
  color: #ffffff;
  padding: 10px;
  width: 85vw;
  height: 27vh;
  border-radius: 15px 15px 15px 15px;
  background: linear-gradient(45deg,
      #67c8ff,
      #2893cf,
      #197bb2,
      #197bb2,
      #104e72);
  gap: 5px;
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 10px;
}

.font-account {
  width: 90%;
  font-size: 19px;
}

.info-in {
  border-top: 1px solid #ffffff;
  width: 90%;
  display: flex;
  align-items: end;
  column-gap: 10px;
}

.info-in>i {
  font-size: 25px;
  padding-bottom: 20px;
}

.info-in>p {
  font-weight: 600;
  font-family: "Koulen", sans-serif;
  font-size: 40px;
}

.texts {
  background-color: #000000;
  height: 7vh;
  display: flex;
  align-items: center;
  width: 100%;
  font-size: 20px;
  justify-content: end;
}

.bg-texts-black {
  border-radius: 100px 0 0 0;
  width: 100%;
  display: flex;
  justify-content: center;
  gap: 30px;
  align-items: center;
  height: 100%;
  background-color: #ffffff;
}

.font-recenty {
  color: #575757;
}

.view-all {
  display: flex;
  color: #197bb2;
  column-gap: 5px;
}

.view-all>i {
  color: #197bb2;
}

.cards {
  height: auto;
  flex-direction: column;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.card-transf {
  margin: 10px 0;
  min-height: 90px;
  padding: 0 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #ececec;
  width: 85%;
  border-radius: 15px;
  height: 10%;
  box-shadow: 0 7px 15px -3px rgb(0, 0, 0, 0.25);
}

.photo {
  width: 75px;
  height: 75px;
  border-radius: 100%;
  background-color: #a8a8a8;
}

.card-info {
  display: flex;
  align-items: center;
  column-gap: 15px;
}

.font-name-card {
  color: #000000;
  font-weight: 700;
  font-size: 20px;
}

.font-prod-card {
  font-size: 12px;
  color: #7d7d7d;
}

.value-date {
  align-items: end;
  display: flex;
  flex-direction: column;
}

.font-value {
  font-weight: 700;
}

.font-date {
  color: #7d7d7d;
  font-size: 10px;
}

.credito {
  color: #4bc92c;
}

.debito {
  color: #ab2626;
}

.loadingText {
  color: #2893cf;
  font-size: 25px;
}
</style>
