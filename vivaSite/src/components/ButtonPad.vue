<template>
  <label for="button" class="button">
    <img :src="image" alt="" />
    <input type="submit" name="button" :value="value" />
  </label>
</template>

<script lang="ts" setup>
import { defineProps } from "vue";

const props = defineProps({
  value: {
    type: String,
    required: true,
  },
  image: {
    type: String,
  },
});
</script>

<style scoped>
input {
  border: none;
  background-color: transparent;
  font-size: 27px;
  color: #ffffff;
  font-weight: bold;
  width: 100px;
}
.button {
  display: flex;
  row-gap: 5px;
  justify-content: center;
  align-items: center;
  width: 200px;
  height: 60px;
  border-radius: 30px;
  border: none;
  background: linear-gradient(135deg, #00b6ff, #00b6ff, #0c6284);
}
</style>
