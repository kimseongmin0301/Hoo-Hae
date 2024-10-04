<template>
  <SplashScreen v-if="showSplash" @hideSplash="hideSplashScreen" />
  <div v-else class="content white">
    <div class="inner">
      <button type="button" @click="onClickLogin" class="login-btn">
        카카오로 3초만에 시작하기
        <!-- <img class="login_btn" src="../assets/images/btn_login.svg" alt="로그인버튼"> -->
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import SplashScreen from '../components/SplashScreen.vue';

const showSplash = ref(true);

const hideSplashScreen = () => {
  showSplash.value = false;
};

const onClickLogin = async () => {

  const currentUrl = window.location.origin +'/onboarding'; // 현재 페이지의 호스트와 포트를 얻음
  window.location.href = `https://api.hoohae.com/api/v1/auth/oauth2/kakao?redirectUri=${encodeURIComponent(currentUrl)}`;



  // // 카카오 로그인 리다이렉트
  // window.location.href = "https://api.hoohae.com/api/v1/auth/oauth2/kakao";
};

onMounted(() => {
  setTimeout(() => {
    hideSplashScreen();
  }, 1000);
});
</script>

<style scoped>

.inner {
  width: 100%;
  height: 100%;
}

.login-btn {
  position: absolute;
  width: 390px;
  height: 52px;
  left: 20px;   /* x축 20 */
  top: 814px;   /* y축 814 */
  border-radius: 8px;
  background-color: #FEE500;
}

</style>
