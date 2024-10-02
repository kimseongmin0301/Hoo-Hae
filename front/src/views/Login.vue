<template>
  <div class="content">
    <div class="inner">
      로그인 페이지
      <button type="button" @click="onClickLogin">
        <div class="loginText">Login</div>
        <a href="https://api.hoohae.com/api/v1/auth/oauth2/kakao">카카오 로그인</a>
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useStore } from 'vuex';
import { useRouter } from 'vue-router';
import authAPI from "@/api/authAPI";

// Vuex store와 router를 사용합니다.
const store = useStore();
const router = useRouter();

// 데이터 선언
const id = ref("neighbor");
const password = ref("1234");

// Vuex의 getters 사용
const menuInfo = store.getters.getMenuInfo;

// 로그인 버튼 클릭 시 실행할 함수
const onClickLogin = async () => {
  alert(1);
  if (id.value && password.value) {
    const data = await authAPI.login(id.value.trim(), password.value.trim());
    console.log(data);
    router.replace("/");
  }
};
</script>

<style scoped>
</style>
