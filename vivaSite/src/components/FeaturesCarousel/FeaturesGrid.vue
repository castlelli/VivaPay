<template>
	<div id="featureGrid" class="grid">
		<div class="cards-wrapper montserrat-text">
			<FeatureCard v-for="(feature, index) in features" :key="index" :feature="feature" :index="index" />
		</div>
		<div class="cta-container">
			<a href="https://wa.me/+5514991659515?text=Olá,%20gostaria%20de%20solicitar%20uma%20demonstração."
				target="_blank" class="cta-link">
				<button class="cta-button montserrat-title">Solicite uma demonstração</button>
			</a>
		</div>

	</div>
</template>

<script setup>
import { ref } from "vue";
import FeatureCard from "./FeatureCard.vue";

const features = ref([
	{
		title: "Facilite sua gestão.",
		image: "./assets/viva/viva-manager-black.png",
		text: "Conheça o VivaManager",
		explanation:
			"O VivaManager é uma solução completa para facilitar a gestão do seu negócio. Com ferramentas intuitivas e poderosas, você pode otimizar processos, aumentar a produtividade e tomar decisões mais informadas.",
	},
	{
		title: "Gaste menos papel.",
		image: "./assets/viva/viva-voucher-black.png",
		text: "Conheça o VivaVoucher",
		explanation:
			"Com o VivaVoucher, você pode reduzir o uso de papel e otimizar seus processos. Nossa solução digital de vouchers ajuda a economizar recursos, reduzir custos e contribuir para a sustentabilidade do seu negócio.",
	},
	{
		title: "Impulsione suas vendas",
		image: "./assets/viva/viva-sales-black.png",
		text: "Conheça o VivaSales",
		explanation:
			"O VivaSales é a ferramenta ideal para impulsionar suas vendas e aumentar seus resultados. Com recursos avançados de CRM e análise de dados, você pode identificar oportunidades, melhorar o relacionamento com clientes e fechar mais negócios.",
	},
]);
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

#featureGrid {
	display: flex;
	flex-direction: column;
	align-items: center;
	gap: 1rem;
}

.cta-container {
	text-align: center;
	display: flex;
	justify-content: center;
	align-items: center;
}

.cta-link {
	text-decoration: none;
}

.cta-button {
	background-color: #1a8fe3;
	color: white;
	border: none;
	padding: 10px 15px;
	border-radius: 10px;
	cursor: pointer;
	font-size: 1.5rem;
	transition: background-color 0.3s ease;
	height: auto;
	min-width: 200px;
	border: solid #009fe3;
	text-align: center;
}

.cta-button:hover {
	color: #1a8fe3;
	background-color: transparent;
	transition-duration: 0.3s;
}

.cards-wrapper {
	display: flex;
	flex-wrap: wrap;
	justify-content: center;
	gap: 1.5rem;
	padding: 1rem;
}

@media screen and (max-width: 567px) {
	.cards-wrapper {
		flex-direction: column;
		align-items: center;
	}
}
</style>