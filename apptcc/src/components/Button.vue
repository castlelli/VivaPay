<template>
  <div class="button-class" :class="{ loading: isLoading == true, loaded: isLoading == false }">
    <input class="button-enviar" @click="handleClick" type="submit" :value="isLoading ? 'Verificando...' : valor"
      :disabled="isLoading" />
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";

export default defineComponent({
  name: "Button",
  props: {
    isLoading: {
      type: Boolean,
      default: false
    },
    valor: {
      type: String,
      default: "",
    },
  },

  methods: {
    handleClick() {
      this.$emit("item-clicked");
    },
  },
});
</script>

<style>
.loading {
  background-color: #e68734ff;
}

.loaded {
  background-color: #00abef;
}

.button-class {
  transition: 0.2s;
  width: 70%;
  height: 55px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 10px;
  box-shadow: -5px 9px 24px -10px rgba(0, 0, 0, 0.43);
}

.button-enviar {
  width: 100%;
  height: 100%;
  font-size: 25px;
  color: #ffffff;
  background-color: transparent;
  border: none;
  outline: none;
}

.button-class:active {
  background-color: #0698d1;

}
</style>
