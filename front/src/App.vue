<template>
 <section id="container">
		<router-view />
		<LayoutFooter msg="홍길동" v-if="getMenuInfo.isShowFooter" />
 </section>
</template>

<script>
import LayoutFooter from './components/LayoutFooter.vue'
import { mapGetters} from "vuex";

export default {
  name: 'App',
  components: {
	LayoutFooter,
  },
  computed: {
    ...mapGetters([
      "getMenuInfo"
    ]),
  },
}
</script>

<style>
@import './assets/css/app.css';
</style>
