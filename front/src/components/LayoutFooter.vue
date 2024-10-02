<template>
 <footer id="footer">
			<ul>
				<li :class="{ active: getMenuInfo.name=='home', on: getMenuInfo.name=='home' }">
					<router-link to="/">
						<img src="../assets/images/ic_menu_1.svg" alt="홈" class="off">
						<img src="../assets/images/ic_menu_1_select.svg" alt="홈 선택됨" class="on">
						<span>홈</span>
					</router-link>
				</li>
				<li :class="{ active: getMenuInfo.name=='feed', on: getMenuInfo.name=='feed' }">
					<router-link to="/feed">
						<img src="../assets/images/ic_menu_2.svg" alt="피드" class="off">
						<img src="../assets/images/ic_menu_2_select.svg" alt="피드 선택됨" class="on">
						<span>피드</span>
					</router-link>
				</li>
				<li :class="{ active: getMenuInfo.name=='board', on: getMenuInfo.name=='task' }">
					<router-link to="/board">
						<img src="../assets/images/ic_menu_3.svg" alt="작성" class="off">
						<img src="../assets/images/ic_menu_3_select.svg" alt="작성 선택됨" class="on">
						<span>작성</span>
					</router-link>
				</li>
				<li :class="{ active: getMenuInfo.name=='my-info', on: getMenuInfo.name=='my-info' }">
					<router-link to="/my-info">
						<img src="../assets/images/ic_menu_5.svg" alt="마이페이지" class="off">
						<img src="../assets/images/ic_menu_5_select.svg" alt="마이페이지 선택됨" class="on">
						<span>마이페이지</span>
					</router-link>
				</li>
			</ul>
		</footer>
</template>

<script>
import { mapGetters} from "vuex";
export default {
  name: 'LayoutFooter',
  props: {
  },
  data() {
	return {
	}
  },
  computed: {
    ...mapGetters([
      "getMenuInfo"
    ]),
  },
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
