<template>
  <div class="bg-popUp">
    <div class="pop-up">
      <div v-show="type == 'success'" class="type success">
        <i class="fa fa-check" aria-hidden="true"></i>
      </div>
      <div v-show="type == 'error' || type == 'sessao'" class="type error">
        <i class="fa fa-times" aria-hidden="true"></i>
      </div>
      <p class="title-message">
        {{
          type === "success" && saldo == null
            ? "Sucesso!"
            : type === "error"
              ? "Erro!"
              : type === "help"
                ? page
                : saldo
        }}
      </p>
      <p class="body-message" :class="{
        align: type === 'help',
      }">
        {{ message }}
      </p>
      <input @click="handleClick()" class="button" :class="{
        success: type === 'success',
        error: type === 'error' || type === 'sessao',
        help: type === 'help',
      }" type="submit" value="Finalizar" />
    </div>
  </div>

</template>

<script lang="ts">
import { defineComponent, inject } from "vue";

export default defineComponent({
  name: "PopUp",
  props: {
    type: {
      type: String,
    },
    message: {
      type: String,
    },
    saldo: {
      type: String,
    },
    page: {
      type: String,
    },
    functionClick: {
      type: String,
      default: ''
    }
  },

  methods: {
    async handleClick() {
      if (this.functionClick === 'exit') {
        this.pageGlobal = 'Home';
        await this.$router.push({ name: "Home" });
      } else {
        this.$emit('close-popup')
      }
    },
  },
  setup() {
    const pageGlobal = inject("pageGlobal");
    return { pageGlobal };
  },
});
</script>

<style>
@import url("https://fonts.cdnfonts.com/css/koulen");

* {
  margin: 0;
}

.fade-slide-up-enter-active,
.fade-slide-up-leave-active {
  transition: transform 0.5s ease;
}

.fade-slide-up-enter,
.fade-slide-up-leave-to {
  transform: translate(0% 0%);
}

.bg-popUp {
  display: flex;
  justify-content: center;
  align-items: center;
  position: absolute;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.1);
}

.type {
  font-size: 100px;
  width: 130px;
  height: 130px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.success {
  background-color: #52e32e;
  box-shadow: 0px 0px 10px 0px #52e32e;
}

.error {
  background-color: #d50f0f;
  box-shadow: 0px 0px 10px 0px #d50f0f;
}

.help {
  background-color: #858585;
  box-shadow: 0px 0px 10px 0px #858585;
}

i {
  color: #ffffff;
}

.title-message {
  font-size: 45px;
  color: #000000;
  font-family: "Koulen", sans-serif !important;
}

.align {
  text-align: start !important;
}

.body-message {
  max-width: 55%;
  text-align: center;
  color: #c1c1c1;
  margin-bottom: 5px;
}

.button {
  font-weight: 500;
  font-size: 25px;
  border-radius: 5px;
  height: 50px;
  width: 230px;
  border-radius: 25px;
  border: none;
  font-family: "Koulen", sans-serif;
  color: #ffffff;
}

.pop-up {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  width: 300px;
  max-height: 450px;
  padding: 30px 0;
  background-color: white;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  row-gap: 10px;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}
</style>
