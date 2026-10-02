<template>
	<div id="featureCarousel" class="carousel" :class="{ expanded: isExpanded }">
		<div class="carousel-inner">
			<div class="cards-wrapper">
				<FeatureCard v-for="(feature, index) in features" :key="index" :feature="feature" :index="index"
					:activeIndex="activeIndex" :isExpanded="isExpanded && activeIndex === index"
					@activate="activateCard" @expand="expandCard" />
				<div v-if="isExpanded" class="explanation">
					<h3>{{ features[activeIndex].title }}</h3>
					<p>{{ features[activeIndex].explanation }}</p>
					<button class="btn btn-primary" @click="closeExpanded">Voltar</button>
				</div>
			</div>
		</div>
		<button v-if="!isExpanded" class="carousel-control-prev" @click="prevCard">
			<span class="carousel-control-prev-icon" aria-hidden="true"></span>
			<span class="visually-hidden">Previous</span>
		</button>
		<button v-if="!isExpanded" class="carousel-control-next" @click="nextCard">
			<span class="carousel-control-next-icon" aria-hidden="true"></span>
			<span class="visually-hidden">Next</span>
		</button>
	</div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from "vue";
import FeatureCard from "./FeatureCard.vue";

const features = ref([
	{
		title: "Facilite sua gestão.",
		image: "../../public/assets/viva/viva-manager-black.png",
		text: "Conheça o VivaManager",
		explanation:
			"O VivaManager é uma solução completa para facilitar a gestão do seu negócio. Com ferramentas intuitivas e poderosas, você pode otimizar processos, aumentar a produtividade e tomar decisões mais informadas.",
	},
	{
		title: "Gaste menos papel.",
		image: "../../public/assets/viva/viva-voucher-black.png",
		text: "Conheça o VivaVoucher",
		explanation:
			"Com o VivaVoucher, você pode reduzir o uso de papel e otimizar seus processos. Nossa solução digital de vouchers ajuda a economizar recursos, reduzir custos e contribuir para a sustentabilidade do seu negócio.",
	},
	{
		title: "Impulsione suas vendas",
		image: "../../public/assets/viva/viva-sales-black.png",
		text: "Conheça o VivaSales",
		explanation:
			"O VivaSales é a ferramenta ideal para impulsionar suas vendas e aumentar seus resultados. Com recursos avançados de CRM e análise de dados, você pode identificar oportunidades, melhorar o relacionamento com clientes e fechar mais negócios.",
	},
]);

const activeIndex = ref(1);
const isExpanded = ref(false);
let observer;

const activateCard = (index) => {
	activeIndex.value = index;
	const prevIcon = document.querySelector(".carousel-control-prev-icon");
	const nextIcon = document.querySelector(".carousel-control-next-icon");

	if (index === 0) {
		prevIcon.classList.add("disabled-icon");
	} else {
		prevIcon.classList.remove("disabled-icon");
	}

	if (index === features.value.length - 1) {
		nextIcon.classList.add("disabled-icon");
	} else {
		nextIcon.classList.remove("disabled-icon");
	}
};

onMounted(() => {
	if (window.innerWidth <= 567) {
		const options = {
			root: null,
			threshold: 0.5,
		};

		observer = new IntersectionObserver((entries) => {
			entries.forEach((entry) => {
				if (entry.isIntersecting) {
					const index = entry.target.getAttribute("data-index");
					activateCard(Number(index));
				}
			});
		}, options);

		const cards = document.querySelectorAll(".card");
		cards.forEach((card) => {
			observer.observe(card);
		});
	}
});

onBeforeUnmount(() => {
	if (observer) {
		observer.disconnect();
	}
});

const nextCard = () => {
	activeIndex.value = (activeIndex.value + 1) % features.value.length;
};

const prevCard = () => {
	activeIndex.value =
		(activeIndex.value - 1 + features.value.length) % features.value.length;
};
</script>

<style scoped>
.cards-wrapper {
	display: flex;
	justify-content: center;
	padding: 0 5rem;
	transition: all 0.5s ease;
	height: 100%;
}

.carousel-control-next-icon,
.carousel-control-prev-icon {
	width: 3rem;
	height: 3rem;
	margin: 0.5rem;
}

.carousel.expanded .cards-wrapper {
	justify-content: flex-start;
}

.explanation {
	width: 50%;
	padding: 2rem;
	background-color: #f8f9fa00;
	border-radius: 10px;
	margin-left: 2rem;
	transition: all 1.5s ease;
	color: white;
}

.disabled-icon {
	display: none;
	cursor: not-allowed;
}

@media screen and (max-width: 567px) {
	.cards-wrapper {
		justify-content: center;
		flex-wrap: wrap;
		flex-direction: column;
		padding: 0;
	}

	.carousel-control-next-icon,
	.carousel-control-prev-icon {
		display: none;
		cursor: not-allowed;
	}

	.explanation {
		width: 100%;
		margin-left: 0;
		margin-top: 1rem;
	}
}
</style>
