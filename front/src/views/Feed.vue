<template>
  <div>
    <LayoutHeader title="피드" :backBtn="false" :alrmBtn="true"/>
    <div class="content">
      <div class="inner">
        <div class="category-tab">
          <div class="overlap-group"><div class="rectangle"></div></div>
          <div class="navbar">
            <div class="div">전체</div>
            <div class="text-wrapper">10대</div>
            <div class="text-wrapper">20대</div>
            <div class="text-wrapper">30대</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import LayoutHeader from '../components/LayoutHeader.vue'
import axios from 'axios';

// 토큰값은 쿠키에서 가져오면된다.
const token = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJrYWthb18zNzI4MzA5MTczIiwicm9sZSI6IlJPTEVfVVNFUiIsImlhdCI6MTcyNzc4NjUyMCwiZXhwIjoxNzMwMzc4NTIwfQ.kypmPwVroi5rSbyCQEauKdIA1Yo5ST_OI_YFZ5whsgk';

axios.get('/api/board/list', {
  params: {
    page: 1,
    age: 10
  },
  headers: {
    Authorization: `Bearer ${token}`
  }
})
.then(response => {
  console.log(response.data);
})
.catch(error => {
  console.error(error);
});




</script>

<style scoped>
.category-tab {
  position: relative;
  width: 100%;
  height: 100%; /* height 100% for full height */

  .overlap-group {
    position: absolute;
    width: 100%; /* 100% width for full width */
    height: 3px;
    top: 29px;
    left: 0;
    background-color: #F1F1F1;
    border-radius: 100px;
  }

  .rectangle {
    width: 25%; /* 25% width to fit proportionally */
    height: 3px;
    background-color: #FF8F39;
    border-radius: 100px;
  }

  .navbar {
    position: absolute;
    width: 100%; /* 100% width for full width */
    height: 21px;
    top: 0;
    left: 0;
    display: flex;
    justify-content: space-around; /* Distributes items evenly */
  }

  .text-wrapper {
    font-family: "Pretendarde-Medium", Helvetica;
    font-weight: 500;
    color: #A7A7A7;
    font-size: 18px;
    text-align: center;
    letter-spacing: 0;
    line-height: normal;
    white-space: nowrap;
  }

  .div {
    font-family: "Pretendarde-Medium", Helvetica;
    font-weight: 600;
    color: #FF8F39;
    font-size: 18px;
    text-align: center;
    letter-spacing: 0;
    line-height: normal;
    white-space: nowrap;
  }
}
</style>
