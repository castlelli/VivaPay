<template>
	<div class="card">
		<div class="image-wrapper">
			<img :src="feature.image" alt="Feature image" />
		</div>
		<div class="card-body">
			<h5 class="card-title montserrat-title">{{ feature.title }}</h5>
			<!---
			<button class="btn btn-lg btn-outline-primary btn-know-us montserrat-text">
				{{ feature.text }}
			</button>
			-->
		</div>
	</div>
</template>

<script setup>
import { computed, defineProps, defineEmits } from "vue";

const props = defineProps({
	feature: Object,
	index: Number,
	activeIndex: Number,
	isExpanded: Boolean,
	isAnyExpanded: Boolean,
});

const emit = defineEmits(["activate", "expand"]);

const isActive = computed(() => props.activeIndex === props.index);

const activateCard = () => {
	if (!props.isExpanded) {
		emit("activate", props.index);
	}
};

const expandCard = () => {
	emit("expand");
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Montserrat:ital,wght@0,100..900;1,100..900&display=swap');

.montserrat-title {
	font-family: "Montserrat", sans-serif;
	font-optical-sizing: auto;
	font-weight: 700;
	font-style: normal;
}

.montserrat-text {
	font-family: "Montserrat", sans-serif;
	font-optical-sizing: auto;
	font-weight: 550;
	font-style: normal;
}

.card {
	width: calc(80% / 3);
	opacity: 0.5;
	background-color: #ffffff;
	box-shadow: 0 0 35px rgba(0, 159, 227, 0.5);
	display: flex;
	flex-direction: column;
	height: 30rem;
	opacity: 1;
}

.card.hide {
	display: none;
}



.card-body {
	color: rgb(0, 0, 0);
	text-align: center;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: space-between;
	padding: 1.5rem;
	flex-grow: 1;
}

.image-wrapper {
	height: 100%;
	display: flex;
	align-items: center;
	justify-content: center;
	overflow: hidden;
}

.image-wrapper img {
	max-width: 100%;
	max-height: 100%;
	object-fit: contain;
}

.card-title {
	margin-bottom: 1rem;
}

.btn-know-us {
	color: #009fe3;
	border-color: #009fe3;
	margin-top: auto;
}

.btn-know-us:hover {
	color: white;
	background-color: #009fe3;
	transition-duration: 0.3s;
}

.btn-know-us,
.card-title {
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
	width: 100%;
}


@media screen and (max-width: 567px) {
	.card {
		width: 90vw;
		height: 100%;
	}
}
</style>
