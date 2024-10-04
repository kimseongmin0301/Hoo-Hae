<template>
  <div class="splash-screen" :class="{ 'fade-out': isFading }">
    <img src="../assets/images/splash.svg" alt="스플래쉬" class="splash-image">
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';

const isFading = ref(false);

onMounted(() => {
  // 1000ms 후에 애니메이션 시작
  setTimeout(() => {
    isFading.value = true; // fade-out 클래스를 추가하여 애니메이션 시작
    setTimeout(() => {
      const hideSplashEvent = new CustomEvent('hideSplash');
      window.dispatchEvent(hideSplashEvent); // hideSplash 이벤트 발생
    }, 300); // 300ms 후에 이벤트 발생
  }, 1000); // 1000ms 지연
});
</script>

<style scoped>
.splash-screen {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  width: 100vw;
  background-color: #FFF3E5;
  opacity: 1;
  transform: scale(1);
  transition: opacity 300ms ease-out, transform 300ms ease-out; /* 300ms ease-out 애니메이션 */
}

.splash-screen.fade-out {
  opacity: 0; /* 투명해지며 페이드 아웃 */
  transform: scale(0.95); /* 화면이 작아지면서 사라짐 */
}

.splash-image {
  width: 100vw;
  height: 100vh;
  object-fit: cover; /* 이미지를 화면에 맞추되, 비율을 유지하면서 잘리지 않도록 */
}
</style>
