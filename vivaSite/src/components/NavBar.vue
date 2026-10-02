<template>
	<header class="navbar">
		<nav class="navbar-container">
			<div class="navbar-logo">
				<img
					src="../../public/assets/logo.png"
					alt="Logo"
					class="logo-img"
				/>
				<span class="logo-text montserrat-title">VivaPay</span>
			</div>
			<div class="navbar-menu-container">
				<div class="navbar-menu">
					<router-link
						to="/home"
						class="montserrat-text"
						>Home</router-link
					>
					<a
						href="#features"
						class="montserrat-text"
						>Produtos</a
					>
					<a
						href="#help"
						class="montserrat-text"
						>Ajuda</a
					>
				</div>
			</div>
		</nav>
	</header>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Montserrat:ital,wght@0,100..900;1,100..900&display=swap');

.montserrat-title {
	font-family: 'Montserrat', sans-serif;
	font-optical-sizing: auto;
	font-weight: 700;
	font-style: normal;
}

.montserrat-text {
	font-family: 'Montserrat', sans-serif;
	font-optical-sizing: auto;
	font-weight: 400;
	font-style: normal;
}

.navbar {
	position: fixed;
	background-color: #0c0f14;
	padding: 20px;
	display: flex;
	align-items: center;
	width: 100%;
	z-index: 10;
}

.navbar-container {
	display: flex;
	align-items: center;
	width: 100%;
}

.logo-img {
	height: 32px;
	width: 32px;
}

.logo-text {
	color: white;
	font-size: 18px;
	font-weight: bold;
}

.navbar-menu-container {
	flex: 1;
	display: flex;
	justify-content: center;
}

.navbar-menu {
	display: flex;
	gap: 24px;
	font-family: 'Montserrat', sans-serif;
}

.navbar-menu a {
	color: white;
	font-size: 14px;
	text-decoration: none;
	transition: color 0.3s;
}

.navbar-menu a:hover {
	color: #a9a9a9;
}

@media (max-width: 768px) {
	.logo-text {
		display: none;
	}

	.logo-img {
		margin-right: 1px;
	}
}
</style>
