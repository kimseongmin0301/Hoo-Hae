<template>
  <section id="container">
		<router-view />
    <LayoutFooter msg="홍길동" v-if="getMenuInfo.isShowFooter" />
  </section>
</template>

<script setup>
import { computed } from 'vue';
import { useStore } from 'vuex';
import LayoutFooter from './components/LayoutFooter.vue';

const store = useStore();
const getMenuInfo = computed(() => store.getters.getMenuInfo);

</script>

<style>
@import './assets/css/app.css';
</style>
