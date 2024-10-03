<template>
	<header id="header">
	  <div class="inner">
		<!-- <input type="hidden" name="alrmCnt" id="alrmCnt" value="1" /> -->
		<button v-if="backBtn" type="button" class="menuBtn"></button>
		<button v-if="alrmBtn" type="button" class="alrmBtn" @click="location.href='#'"></button>
		<h1>{{ title }}</h1>
	  </div>
	</header>
  </template>
  
  <script setup>
  defineProps({
	title: String,
	backBtn: {
		type: Boolean,
		default: true, // 기본값을 true로 설정
	},
	alrmBtn: {
		type: Boolean,
		default: false, // 기본값을 true로 설정
	},
  });


  


  </script>
  
  <style scoped></style>
  