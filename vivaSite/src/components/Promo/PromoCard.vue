<template>
	<div class="promo-card">
		<div class="promo-content">
			<h2 class="promo-title montserrat-title">
				<span class="highlight-title">VivaPay</span> <br /> A solução de pagamentos com fichas.
			</h2>
			<p class="promo-description montserrat-text">
				<span class="highlight-description">VivaPay</span> otimiza e facilita o pagamento
				em fichas em eventos para lojas e caixas.
			</p>
			<!---
		<button class="cta-button montserrat-title">Saiba como</button>
		-->
		</div>
		<div class="promo-image">
			<img src="../../../public/assets/payment_img.png" alt="Pagamento em fichas" />
			<div class="promo-image-glow"></div>
		</div>
	</div>
</template>

<script setup>
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
	font-weight: 400;
	font-style: normal;
}

.promo-image-glow {
	content: '';
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	border-radius: 50%;
	background: rgb(0, 123, 184);
	filter: blur(20px);
	z-index: -1;
}

.promo-card {
	background-color: #0c0f14;
	border-radius: 16px;
	display: flex;
	flex-direction: row;
	align-items: center;
	color: white;
	background: rgba(255, 255, 255, 0);
	border-radius: 16px;
	box-shadow: 0 4px 30px rgba(0, 0, 0, 0.1);
	backdrop-filter: blur(13.1px);
	-webkit-backdrop-filter: blur(13.1px);
	padding-top: 5rem;
}

.promo-content {
	max-width: 60%;
}

.promo-title {
	font-size: 3rem;
	margin-bottom: 16px;
}

.promo-title,
.promo-description {
	word-break: normal;
	overflow-wrap: break-word;
}

.highlight-title {
	color: #009fe3;
	font-weight: bold;
}

.highlight-description {
	color: #ed9b50;
	font-weight: bold;
}

.promo-description {
	font-size: 2rem;
	margin-bottom: 24px;
	word-break: break-word;
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

}

.cta-button:hover {
	color: #1a8fe3;
	background-color: transparent;
	transition-duration: 0.3s;
}

.promo-image {
	position: relative;
}

.promo-image>img {
	display: block;
	width: 100%;
	height: auto;
	border-radius: 10px;
}

@media (max-width: 768px) {
	.promo-card {
		flex-direction: column;
		align-items: center;
	}

	.promo-content {
		order: 2;
		max-width: 100%;
		padding: 20px;
		align-items: center;
	}

	.promo-image {
		order: 1;
		margin-bottom: 20px;
	}

	.promo-image>img {
		width: 100%;
		height: auto;
	}

	.promo-title,
	.promo-description {
		text-align: center;
	}

	.cta-button {
		display: block;
		margin: 0 auto;
	}
}
</style>