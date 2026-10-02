<template>
	<div class="contact-container">
		<p class="contact-title montserrat-title">Algum problema?</p>
		<p class="contact-description montserrat-text">
			Estamos aqui para ajudar! Se precisar de ajuda, mande um e-mail para a
			equipe de SAC da Cashflow.
		</p>
		<div class="button-contact">
			<ContactButton subject="Suporte" buttonText="Fale com a equipe de suporte" textColor="#00a3ff"
				backgroundColor="#0c0f1400" hoverTextColor="white" hoverBackgroundColor="#00a3ff" />
		</div>
	</div>
</template>

<script setup>
import ContactButton from "./ContactButton.vue"; 
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

.contact-container {
	background-color: #0c0f14;
	border-radius: 20px;
	padding: 20px;
	text-align: center;
	border: 2px solid #00a3ff;
	width: 100%;
	margin: auto;
	margin-bottom: 5rem;
	margin-top: 2.5rem;
}

.button-contact {
	display: flex;
	justify-content: center;
	align-items: center;
}

.contact-title {
	font-size: 45px;
	color: #ffffff;
	margin-bottom: 10px;
	font-weight: 600;
}

.contact-description {
	font-size: 20px;
	color: #7a7e87;
	margin-bottom: 20px;
}
</style>
