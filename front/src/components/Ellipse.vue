<template>
    <div>
        <div >
            <!-- number 값에 따라 다르게 렌더링 -->
            <div v-if="number === 1" class="ellipse">
                <img class="ellipse-1" src="../assets/images/ellipse-1.svg" alt="ellipse-1">
                <img class="ellipse-2" src="../assets/images/ellipse-2.svg" alt="ellipse-2">
                <img class="ellipse-2" src="../assets/images/ellipse-2.svg" alt="ellipse-2">
                <img class="ellipse-2" src="../assets/images/ellipse-2.svg" alt="ellipse-2">
            </div>
            <div v-else-if="number === 2" class="ellipse">
                <img class="ellipse-2" src="../assets/images/ellipse-2.svg" alt="ellipse-2">
                <img class="ellipse-1" src="../assets/images/ellipse-1.svg" alt="ellipse-1">
                <img class="ellipse-2" src="../assets/images/ellipse-2.svg" alt="ellipse-2">
                <img class="ellipse-2" src="../assets/images/ellipse-2.svg" alt="ellipse-2">
            </div>
            <div v-else-if="number === 3" class="ellipse">
                <img class="ellipse-2" src="../assets/images/ellipse-2.svg" alt="ellipse-2">
                <img class="ellipse-2" src="../assets/images/ellipse-2.svg" alt="ellipse-2">
                <img class="ellipse-1" src="../assets/images/ellipse-1.svg" alt="ellipse-1">
                <img class="ellipse-2" src="../assets/images/ellipse-2.svg" alt="ellipse-2">
            </div>
            <div v-else-if="number === 4" class="ellipse">
                <img class="ellipse-2" src="../assets/images/ellipse-2.svg" alt="ellipse-2">
                <img class="ellipse-2" src="../assets/images/ellipse-2.svg" alt="ellipse-2">
                <img class="ellipse-2" src="../assets/images/ellipse-2.svg" alt="ellipse-2">
                <img class="ellipse-1" src="../assets/images/ellipse-1.svg" alt="ellipse-1">
            </div>
        </div>
    </div>
</template>

<script setup>
import { defineProps } from 'vue';

defineProps({
  number: {
    type: Number,
    default: 1,
  },
});
</script>

<style scoped>
.ellipse {
  position: absolute;
  left: 167px;
  top: 104px;
  display: flex;
  gap: 16px;
}

.ellipse-1, .ellipse-2 {
  width: 12px;
  height: 12px;
}
</style>
