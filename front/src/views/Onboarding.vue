<template>
  <div>
    <OnboardingPopup v-if="isSaved"/>
    <div v-else>
      <LayoutHeader title="" :backBtn="false" :alrmBtn="false"/>
      <div class="content white">
        <div class="inner">
          <Ellipse :number="2"></Ellipse>
          <div class="plain-text">
            <p class="text-1">나의 연령대는?</p>
            <p class="text-2">주변 친구들의 후회를 먼저 보여줄게!</p>
          </div>
          <div class="cards">
            <div v-if="age=='10'" class="card-one" id="card-one-on">
              <p class="age-text-one-on">10대</p>
              <img class="age-img-one" src="../assets/images/10age-2.svg" alt="10대">
            </div>
            <div v-else class="card-one" id="card-off" @click="ontenage">
              <p class="age-text-off">10대</p>
              <img class="age-img-one" src="../assets/images/10age-1.svg" alt="10대">
            </div>
            <div v-if="age=='20'" class="card-two" id="card-two-on">
              <p class="age-text-two-on">20대</p>
              <img class="age-img-two" src="../assets/images/20age-2.svg" alt="20대">
            </div>
            <div v-else class="card-two" id="card-off" @click="ontwentyage">
              <p class="age-text-off">20대</p>
              <img class="age-img-two" src="../assets/images/20age-1.svg" alt="20대">
            </div>
            <div v-if="age=='30'" class="card-three" id="card-three-on" @click="onthirtyage">
              <p class="age-text-three-on">30대</p>
              <img class="age-img-three" src="../assets/images/30age-2.svg" alt="30대">
            </div>  
            <div v-else class="card-three" id="card-off" @click="onthirtyage">
              <p class="age-text-off">30대</p>
              <img class="age-img-three" src="../assets/images/30age-1.svg" alt="30대">
            </div>      
          </div>
          <div>
            <input type="button" :class="checked ? 'orange-btn onboard':'gray-btn onboard'"  value="했어!" @click="saveAndNext()"/>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
  
<script setup>
import { ref } from 'vue'
import axios from 'axios';
import router from '@/router';
import LayoutHeader from '../components/LayoutHeader.vue'
import OnboardingPopup from '../components/OnboardingPopup.vue'
import Ellipse from '../components/Ellipse.vue'

let age = ref(null); // 선택한 연령대
let checked = ref(false); // 체크 여부
let isSaved = ref(false);
const ontenage = () => {
  age.value = 10;
  checked.value = true;
}
const ontwentyage = () => {
  age.value = 20;
  checked.value = true;

}
const onthirtyage = () => {
  age.value = 30;
  checked.value = true;
}

const saveAndNext = () => { // 저장후 다음 페이지
  isSaved.value = true;
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
    text-align: left;
  }

  .text-1 {
    color: #222222;
    font-family: "Pretendard-SemiBold", Helvetica;
    font-size: 32px;
    top: 180px;
    left: 20px;
    position: absolute;
  }

  .text-2 {
    color: #7A7A7A;
    font-family: "Pretendard-Medium", Helvetica;
    font-size: 18px;
    top: 226px;
    left: 20px;
    position: absolute;
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
