<template>
	<div class="promo-section">
		<div class="promo-content">
			<div class="promo-image">
				<img src="../../../public/assets/promo-image.png" alt="Ilustração Promocional" />
			</div>
			<div class="promo-text">
				<h1>Viva Pay: <br />Facilitar o pagamento em fichas</h1>
				<p>
					<span class="highlight">Viva Pay</span> otimiza e facilita o pagamento
					em fichas em eventos para lojas e caixas.
				</p>
				<button class="cta-button">Saiba como</button>
			</div>
		</div>
	</div>
</template>

<script setup>
</script>

<style scoped>
.promo-section {
	display: flex;
	justify-content: center;
	align-items: center;
	background-color: #0f0f0f;
	padding: 40px;
	border-radius: 20px;
}

.promo-content {
	display: flex;
	align-items: center;
}

.promo-image img {
	max-width: 100%;
	height: auto;
}

.promo-text {
	margin-left: 20px;
}

.promo-text h1 {
	color: #1a8fe3;
	font-size: 2rem;
	margin-bottom: 20px;
}

.promo-text p {
	color: #ffffff;
	margin-bottom: 20px;
}

.highlight {
	color: #f28b20;
}

.cta-button {
	background-color: #1a8fe3;
	color: white;
	padding: 10px 20px;
	border: none;
	border-radius: 20px;
	cursor: pointer;
	font-size: 1rem;
	transition: background-color 0.3s ease;
}

.cta-button:hover {
	background-color: #1573b1;
}

@media (max-width: 768px) {
	.promo-section {
		flex-direction: column;
	}
}
</style>
