<template>
    <div>
        <LayoutHeader title="" :backBtn="true" :alrmBtn="false"/>
        <div class="content white">
          <div class="inner">
            <Ellipse :number="3"></Ellipse>
            <div class="plain-text">
              <p class="text-1">나를 설정해봐!</p>
              <!-- <p class="text-2">주변 친구들의 후회를 먼저 보여줄게!</p> -->
            </div>
            <div class="cards">
              <div class="card-one" id="card-one-on">
                  <img class="age-img-one" src="../assets/images/10age-2.svg" alt="10대">
              </div>   
            </div>
            <div>
              <input type="button" :class="checked ? 'orange-btn onboard':'gray-btn onboard'"  value="완벽해!" @click="saveAndNext()"/>
            </div>
          </div>
        </div>
    </div>
  </template>
    
  <script setup>
  import LayoutHeader from '../components/LayoutHeader.vue'
  import Ellipse from '../components/Ellipse.vue'
  import { ref } from 'vue'
  import axios from 'axios';
  import router from '@/router';
  
  let age = ref(null); // 선택한 연령대
  let checked = ref(false); // 체크 여부
  
  const saveAndNext = () => { // 저장후 다음 페이지
    
  }
  </script>
  
  
  <style scoped>
  .cards {
    width: 390px;
    height:490px;
    left:20px;
    top:279px;
    position:absolute;
  
  }
  #card-one-on {
    padding: 32px 56px;
    border-radius: 8px;
    background-color: #FFDDDD;
    width: 100%;
    height:fit-content;
    border: #FF5353 2px solid;
    height:150px;
  }
  #card-two-on {
    padding: 32px 56px;
    border-radius: 8px;
    background-color: #E8D9FF;
    width: 100%;
    height:fit-content;
    border: #8C40FF 2px solid;
    height:150px;
  
  }
  
  #card-three-on {
    padding: 32px 56px;
    border-radius: 8px;
    background-color: #B2D7FF;
    width: 100%;
    height:fit-content;
    border: #007AFF 2px solid;
    height:150px;
  
  }
  
  #card-off {
    padding: 32px 56px;
    border-radius: 8px;
    background-color: #F6F7F9;
    width: 100%;
    height:fit-content;
    height:150px;
  
  }
  
  .card-one {
    position:absolute;
    top: 0px;
  }
  .card-two {
    position:absolute;
    top: 171px;
  }
  .card-three {
    position:absolute;
    top:336px;
  }
  
  .age-img-one {
    width: 89px;
    height: 91px;
    left: 245px;
    top: 32px;
    position:absolute;
  }
  
  .age-img-two {
    width: 97px;
    height: 85px;
    left: 243px;
    position:absolute;
    top: 32px;
  }
  
  .age-img-three {
    width: 86px;
    height: 90px;
    left: 251px;
    top: 32px;
    position:absolute;
  }
  
    .plain-text {
      left: 20px;
      text-align: left;
      position:absolute;
    }
  
    .text-1 {
      color: #222222;
      font-family: "Pretendard-SemiBold", Helvetica;
      font-size: 32px;
    }
  
    .text-2 {
      color: #7A7A7A;
      font-family: "Pretendard-Medium", Helvetica;
      font-size: 18px;
    }
  
    .age-text-one-on {
      color: #FF5353;
      font-family: "Pretendard-SemiBold", Helvetica;
      font-size: 24px;
      text-align: left;
      position:absolute;
    }  
    .age-text-two-on {
      color: #8C40FF;
      font-family: "Pretendard-SemiBold", Helvetica;
      font-size: 24px;
      text-align: left;
      position:absolute;
    }  
    .age-text-three-on {
      color: #007AFF;
      font-family: "Pretendard-SemiBold", Helvetica;
      font-size: 24px;
      text-align: left;
      position:absolute;
    }
    .age-text-off {
      color: #7A7A7A;
      font-family: "Pretendard-SemiBold", Helvetica;
      font-size: 24px;
      text-align: left;
      position:absolute;
    }
  
    .gray-btn {
      background-color: #F1F1F1;
      border: none;
      border-radius: 8px;
      color: #e2e8f0;
      font-size: 16px;
      cursor: pointer;
      width: 390px;
      height: 52px;
      padding: 8px 24px;
      text-align: center;
    }
    .orange-btn {
      background-color: #FF8F39;
      border: none;
      border-radius: 8px;
      color: #FFFFFF;
      font-size: 16px;
      cursor: pointer;
      width: 390px;
      height: 52px;
      padding: 8px 24px;
      text-align: center;
    }
  
    .onboard {
      position: absolute;
      left: 20px;
      top: 814px;
      font-family: "Pretendard-Medium", Helvetica;
  
    }
  </style>
  