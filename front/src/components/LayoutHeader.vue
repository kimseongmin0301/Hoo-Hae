<template>
	<header id="header">
	  <div class="inner">
		<!-- <input type="hidden" name="alrmCnt" id="alrmCnt" value="1" /> -->
		<button type="button" class="menuBtn"></button>
		<!-- <button type="button" class="alrmBtn" @click="location.href='#'"></button> -->
		<h1>{{ title }}</h1>
		<button type="button" class="menuBtn"></button>
	  </div>
	</header>
  </template>
  
  <script setup>
  defineProps({
	title: String
  });
  </script>
  
  <style scoped></style>
  