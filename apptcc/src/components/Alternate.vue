<template>
  <div
    v-if="showOptions"
    :style="{
      top: `${position.top - alternate}px`,
      left: `${position.left + 15}px`,
    }"
    class="options"
  >
    <div
      :class="{ 'bg-selected': page == 'Historico' }"
      class="option"
      @click="handleClick('Historico')"
    >
      <i class="fa-solid fa-clock-rotate-left icon"></i>
    </div>
    <div
      :class="{ 'bg-selected': page == 'Pagamento' }"
      class="option"
      @click="handleClick('Pagamento')"
    >
      <i class="fa-solid fa-money-bill icon"></i>
    </div>
    <div
      :class="{ 'bg-selected': page == 'Saldo' }"
      class="option"
      @click="handleClick('Consultar')"
    >
      <i class="fa-solid fa-receipt icon"></i>
    </div>
  </div>
  <div
    :style="{ top: `${position.top}px`, left: `${position.left}px` }"
    class="alternate-button"
    @mousedown="startDrag"
    @click="toggleViewOptions"
    @touchstart="startDrag"
    @touchend.prevent="toggleViewOptions"
  >
    <i class="fa-solid fa-bars" style="color: white; font-size: 30px"></i>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";

export default defineComponent({
  name: "InputCampo",
  data() {
    return {
      position: { top: 10, left: 10 },
      isDragging: false,
      dragOffset: { x: 0, y: 0 },
      showOptions: false,
      alternate: 100,
    };
  },
  props: {
    page: {
      type: String,
      default: "",
    },
  },

  methods: {
    startDrag(event: any) {
      event.preventDefault();

      const clientX =
        event.type === "touchstart" ? event.touches[0].clientX : event.clientX;
      const clientY =
        event.type === "touchstart" ? event.touches[0].clientY : event.clientY;

      this.isDragging = true;

      this.dragOffset.x = clientX - this.position.left;
      this.dragOffset.y = clientY - this.position.top;

      document.addEventListener("mousemove", this.onDrag);
      document.addEventListener("touchmove", this.onDrag);
      document.addEventListener("mouseup", this.endDrag);
      document.addEventListener("touchend", this.endDrag);
    },
    onDrag(event: any) {
      if (this.isDragging) {
        const clientX =
          event.type === "touchmove" ? event.touches[0].clientX : event.clientX;
        const clientY =
          event.type === "touchmove" ? event.touches[0].clientY : event.clientY;

        this.position.left = clientX - this.dragOffset.x;
        this.position.top = clientY - this.dragOffset.y;
      }
    },
    endDrag() {
      this.isDragging = false;
      document.removeEventListener("mousemove", this.onDrag);
      document.removeEventListener("touchmove", this.onDrag);
      document.removeEventListener("mouseup", this.endDrag);
      document.removeEventListener("touchend", this.endDrag);
    },
    toggleViewOptions() {
      if (this.showOptions) this.showOptions = false;
      else {
        if (this.position.top > window.innerHeight / 2) this.alternate = 200;
        else this.alternate = -100;

        this.showOptions = true;
      }
    },
    handleClick(page: any) {
      this.$emit("item-clicked", page);
    },
  },
});
</script>

<style scoped>
@import url("https://fonts.cdnfonts.com/css/koulen");

.container-alternate {
  position: fixed;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 30px;
}

.alternate-button {
  position: absolute;
  width: 80px;
  display: flex;
  height: 80px;
  justify-content: center;
  align-items: center;
  border-radius: 100%;
  background-color: #00abef;
  box-shadow: 1px 1px 10px 1px #0778a5;
}

.options {
  position: absolute;
  gap: 15px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.option {
  width: 50px;
  height: 50px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #ffffff;
  border-radius: 100%;
  box-shadow: 1px 1px 10px 1px black;
}

.bg-selected {
  background-color: #0778a5;
}

.icon {
  color: #000000;
  font-size: 30px;
}
</style>
