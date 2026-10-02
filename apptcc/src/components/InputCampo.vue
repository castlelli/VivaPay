<template>
  <div class="input-class">
    <div class="label-input">
      <label for="input">{{ label }}</label>
    </div>
    <input class="input-text" :value="modelValue" @input="updateValue" name="input" :type="type" :placeholder="campo" />
  </div>
</template>

<script lang="ts">
import { defineComponent, PropType } from "vue";

export default defineComponent({
  name: "InputCampo",
  props: {
    campo: {
      type: String,
    },
    type: {
      type: String,
      default: "text"
    },
    modelValue: {
      type: [String, Number],
      default: null,
    },
    label: {
      type: String,
    }
  },

  methods: {
    updateValue(event: Event) {
      const input = event.target as HTMLInputElement;
      this.$emit("update:modelValue", input.value);
    },
  },
});
</script>

<style scoped>
.input-class {
  transition: all;
  background-color: #f4f4f4;
  width: 100%;
  height: 65px;
  display: flex;
  align-items: center;
  flex-direction: column;
  justify-content: space-between;
  border-radius: 10px;
  box-shadow: -3px 4px 10px -2px rgba(0, 0, 0, 0.5);
  padding: 10px 25px;
}

.label-input {
  color: #828282;
  width: 100%;
  display: flex;
  justify-content: start;
}

.input-text {
  outline: none;
  background-color: transparent;
  border: none;
  color: #000000;
  border-bottom: 1px solid #000000;
}

.input-text::placeholder {
  color: #c8c8c8;
}
</style>
