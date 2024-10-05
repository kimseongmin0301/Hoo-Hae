<template>
  <div>
    <SplashScreen v-if="showSplash" @hideSplash="hideSplashScreen" />
    <div v-else>
      <LayoutHeader title="" :backBtn="false" :alrmBtn="false"/>
      <div class="content white">
        <div class="inner">
          <Ellipse :number = "1"></Ellipse>
          <div class="text-1">
            <p class="gray">사람은 일주일에</p><p class="orange-1">45분</p><p class="gray">을 후회한다고 해.</p> 
          </div>
          <div class="text-2">
            <p class="orange-2">후~해</p><p class="black">가 그 시간도<br>소중하게 만들어줄게!</p>
          </div>
          <img class="login-character" src="../assets/images/login.svg" alt="로그인캐릭터">
          <button type="button" @click="onClickLogin" class="login-btn">
            <img class="kakao" src="../assets/images/kakao.svg" alt="카카오">
            <p class="login-btn-txt">카카오로 3초만에 시작하기</p>
            <!-- <img class="login_btn" src="../assets/images/btn_login.svg" alt="로그인버튼"> -->
          </button>
        </div>
      </div> 
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import LayoutHeader from '../components/LayoutHeader.vue'
import SplashScreen from '../components/SplashScreen.vue';
import Ellipse from '../components/Ellipse.vue'

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
  padding: 16px 24px;
}
.login-btn-txt {
  font-size: 18px;
  font-family: "Pretendard-Medium", Helvetica;
  text-align: center;
  position: absolute;
  left: 112px;
  top: 15.5px;  
}
.login-character {
  width: 358.72px;
  height: 286px;
  position: absolute;
  left: 34.43px;
  top: 453px;
}
.kakao {
  position: absolute;
  top: 17px;
  left: 86px;
  width: 18px;
  height: 18px;
}
.text-1 {
  text-align: left;
  position: absolute;
  left: 40px;
  top: 180px;
}
.text-2 {
  text-align: left;
  position: absolute;
  left: 40px;
  top: 218px;
}

.gray {
  color:#7A7A7A;
  display: inline;
  font-family: "Pretendard-Medium", Helvetica;
  font-size: 18px;
}
.black {
  color: black;
  display: inline;
  font-family: "Pretendard-SemiBold", Helvetica;
  font-size: 32px;
}
.orange-1 {
  color: #FF8F39;
  display: inline;
  font-family: "Pretendard-Medium", Helvetica;
  font-size: 18px;
}
.orange-2 {
  color: #FF8F39;
  display: inline;
  font-family: "Pretendard-SemiBold", Helvetica;
  font-size: 32px;
}
</style>
