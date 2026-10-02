<template>
  <a :href="mailToLink">
    <button class="contact-button" :style="buttonStyle" @mouseover="hover = true" @mouseleave="hover = false">
      <span class="contact-button-icon montserrat-text">{{ buttonText }}</span>
    </button>
  </a>
</template>

<script setup>
import { computed, ref } from "vue";

const props = defineProps({
  email: {
    type: String,
    default: "contato.cashflowbr@gmail.com",
  },
  subject: {
    type: String,
    default: "Contato",
  },
  buttonText: {
    type: String,
    default: "Contato",
  },
  textColor: {
    type: String,
    default: "white",
  },
  backgroundColor: {
    type: String,
    default: "transparent",
  },
  hoverTextColor: {
    type: String,
    default: "black",
  },
  hoverBackgroundColor: {
    type: String,
    default: "white",
  },
});

const hover = ref(false);

const mailToLink = computed(() => {
  return `mailto:${props.email}?subject=${encodeURIComponent(props.subject)}`;
});

const buttonStyle = computed(() => {
  return {
    color: hover.value ? props.hoverTextColor : props.textColor,
    backgroundColor: hover.value
      ? props.hoverBackgroundColor
      : props.backgroundColor,
    border: `1px solid ${hover.value ? props.hoverBackgroundColor : props.textColor
      }`,
  };
});
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Montserrat:ital,wght@0,100..900;1,100..900&display=swap');

.montserrat-text {
  font-family: "Montserrat", sans-serif;
  font-optical-sizing: auto;
  font-weight: 400;
  font-style: normal;
}

a {
  text-decoration: none;
}

.contact-button {
  padding: 8px 16px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background-color 0.3s, color 0.3s;
  text-align: center;
}

.contact-button-icon {
  font-size: 18px;
  font-weight: bold;
}
</style>
