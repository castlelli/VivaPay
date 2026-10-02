<template>
  <ion-page>
    <div style="height: 100vh; background-color: #fff">
      <div class="title-page">
        <p>LOGIN</p>
      </div>
      <div class="header-box-one">
        <div class="welcome">Bem Vindo</div>
      </div>
      <div class="container">
        <img src="/assets/imagem.png" alt="" />
        <div class="campo">
          <InputCampo
            :label="'Login'"
            :campo="'VivaCTI@company.br'"
            :class="{ error: error === true }"
            v-model="inputNome"
          />
        </div>
        <div class="campo">
          <InputCampo
            :label="'Senha'"
            :campo="'********'"
            :type="'password'"
            :class="{ error: error === true }"
            v-model="inputSenha"
          />
        </div>
        <Button
          :valor="'Entrar'"
          @item-clicked="chamarAPI()"
          :isLoading="isLoading"
        />
      </div>
    </div>
  </ion-page>
</template>

<script lang="ts">
import InputCampo from "@/components/InputCampo.vue";
import Button from "@/components/Button.vue";
import { defineComponent, inject } from "vue";
import { IonPage, IonContent } from "@ionic/vue";
import axios from "axios";

export default defineComponent({
  name: "HomePage",
  components: {
    InputCampo,
    Button,
    IonPage,
    IonContent,
  },
  setup() {
    const pageGlobal = inject("pageGlobal");
    return { pageGlobal };
  },
  data() {
    return {
      inputNome: "",
      inputSenha: "",
      storedData: null,
      error: false,
      isLoading: false,
    };
  },
  methods: {
    async chamarAPI() {
      if (
        (this.inputNome != null && this.inputSenha != null) ||
        (this.inputNome != "" && this.inputSenha != "")
      ) {
        this.isLoading = true;
        axios
          .post("https://tcc-r46r.onrender.com/login", {
            login: this.inputNome,
            senha: this.inputSenha,
          })
          .then(async (response) => {
            this.isLoading = false;
            if (response.data.sucesso != false) {
              this.error = false;
              localStorage.setItem(
                "token",
                response.data.obj.usuario.token.token
              );
              this.inputNome = "";
              this.inputSenha = "";
              this.isLoading = false;
              this.pageGlobal = "Consultar";
              await this.$router.push({ name: "Consultar" });
            } else {
              this.error = true;
            }
          })
          .catch((error) => {
            this.isLoading = false;
            console.error("Erro ao chamar a API:", error);
          });
      }
    },
  },
});
</script>
<style scoped>
@import url("https://fonts.googleapis.com/css?family=Imprima");
@import url("https://fonts.cdnfonts.com/css/koulen");

* {
  padding: 0;
  margin: 0;
  box-sizing: border-box;
}

.error {
  box-shadow: 0px 0px 10px 0px #d50f0f;
  border: 2px solid #d50f0f;
}

.header-box-one {
  height: 9%;
  background-color: #000000;
  display: flex;
  flex-direction: column;
  justify-content: end;
  align-items: end;
  border: none;
}

.title-page {
  border-radius: 0 0 40px 0;
  background-color: #000000;
  height: 20%;
  align-items: center;
  width: 100%;
  display: flex;
  justify-content: center;
  border: none;
}

.title-page > p {
  font-family: "Koulen", sans-serif;
  color: #ffffff;
}

.welcome {
  display: flex;
  justify-content: center;
  align-items: end;
  font-size: 30px;
  border: none;
  height: 100%;
  width: 100%;
  color: #000000;
  background-color: #ffffff;
  border-radius: 50px 0 0 0;
  display: flex;
  justify-content: center;
}

.container {
  background-color: #ffffff;
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

inner-scroll {
  background-color: #ffffff;
}
</style>
