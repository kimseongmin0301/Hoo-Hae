<template>
  <div>
    <LayoutHeader title="글 쓰기"/>
    <div class="content">
      <div class="inner">
        <div class="flex-col">
          <div class="category">
            <input type="text" @click="openBottomSheet" placeholder="카테고리를 선택해줘" v-model="category" readonly />
            <img class="text_arrow" src="../assets/images/arrow-down.svg" alt="피드 선택됨">
          </div>
          <div class="input-container">
            <input type="text" placeholder="제목을 지어줘! (3자~15자)" v-model="title" />
            <button class="delete-button" @click="deleteTitle">
              <img class="text_arrow" src="../assets/images/btn_close_2.svg" alt="삭제">
            </button>
          </div>
          <input type="text" class="longtext" placeholder="무슨 후회야? 궁금하다!" v-model="contents">
          <div class="plain">
            <p class="orange">앞으로 같은 상황에 어떻게 할 건지 작성해봐~</p>
          </div>
          <div class="photo">
          </div>  
          <div class="my-answer">
            <div class="text-wrapper-5">
              <p>내가 원하는 답변은</p>
            </div>
            <div class="group-2">
              <div class="overlap-2" :class="{ 'border': selectedType === 'hoosidin' }" @click="selectType('hoosidin')">
                <div class="group-wrapper">
                  <div class="">
                    <div class="">후시딘</div>
                    <div class="">긍정적인 조언으로 후~</div>
                  </div>
                </div>
              </div>
              <div class="overlap-2" :class="{ 'border': selectedType === 'hoochoo' }" @click="selectType('hoochoo')">
                <div class="group-wrapper">
                  <div class="">
                    <div class="">후추</div>
                    <div class="">진지한 조언으로 후~</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <input v-if="!category||!title||!selectedType||!contents" type="button" value="공유할래!" @click="save()">
          <input v-else type="button" style="background-color: #FF8F39" value="공유할래!" @click="save(true)">
        </div>
      </div>
      <div v-if="isBottomSheetOpen" class="bottom-sheet-wrapper">
        <div class="dimmed-background" @click="closeBottomSheet"></div>
        <div class="bottom-sheet">
          <div class="bottom-sheet-content">
            <div class="flex-row">
              <div style="justify-content: center;">
                <h2 class="sheet-title">카테고리를 선택해줘</h2>
              </div>
              <button class="close-btn" @click="closeBottomSheet">
                <img class="text_arrow" src="../assets/images/btn_close.svg" alt="삭제">
              </button>
            </div>
            <div class="sheet-description">
              <p @click="selectCategory('진로')">진로</p>
              <p @click="selectCategory('연애')">연애</p>
              <p @click="selectCategory('자기계발')">자기계발</p>
              <p @click="selectCategory('건강')">건강</p>
              <p @click="selectCategory('대인관계')">대인관계</p>
              <p @click="selectCategory('경제')">경제</p>
              <p @click="selectCategory('시간')">시간</p>
              <p @click="selectCategory('기타')">기타</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import LayoutHeader from '../components/LayoutHeader.vue'
import axios from 'axios';

const isBottomSheetOpen = ref(false); // 바텀 시트 열림 상태
let category = ref(null);
let title = ref(null);
let selectedType = ref(null);
let contents = ref(null);
// 바텀 시트를 열기 위한 함수
const openBottomSheet = () => {
  isBottomSheetOpen.value = true; // 바텀 시트 열기
};

// 바텀 시트를 닫기 위한 함수
const closeBottomSheet = () => {
  isBottomSheetOpen.value = false; // 바텀 시트 닫기
};

// 카테고리를 선택할 때 호출되는 함수
const selectCategory = (selectedCategory) => {
  category.value = selectedCategory; // 선택한 카테고리를 category에 저장
  closeBottomSheet(); // 카테고리를 선택한 후 바텀 시트 닫기
};

const deleteTitle = () => {
  title.value = null;
}

const selectType = (type) => {
  selectedType.value = type;
}

const save =async (full)=> {
  if(!category.value) {
    alert("카테고리를 선택해줘!")
    return;
  }
  if(full) { // 오렌지 버튼
    alert("저장가능!")
    const formData = new FormData();
    formData.append('subject', title.value);
    formData.append('body', contents.value);
    formData.append('image', '');  // 실제로 이미지 파일이 있으면 여기에 파일 객체를 넣어야 합니다.
    formData.append('userId', 'userId');  // 실제 사용자 ID로 대체
    formData.append('nickname', 'nickname');  // 실제 닉네임으로 대체
    try {
    const response = await axios.post('http://211.188.52.53:39880/api/board/save', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });

    console.log('게시물 저장 성공:', response.data);
    // 성공 후 필요한 추가 작업 수행 (예: 알림, 리다이렉트 등)
  } catch (error) {
    console.error('게시물 저장 실패:', error);
    // 에러 처리
  }

  }

}
</script>





<style scoped>
/* 공통 */

.flex-col {
  display: flex;
  flex-direction: column;
  gap: 15px;
  width: 100%;
}
.flex-row {
  display: flex;
}

button {
  background: none;
  border: none;
  cursor: pointer;
}

/* 입력 및 카테고리 */
.category {
  display: flex;
  align-items: center;
  gap: 10px;
  border: 1px solid #ccc;
  padding: 10px;
  border-radius: 8px;
}

.category input {
  flex-grow: 1;
  border: none;
  background: transparent;
  cursor: pointer;
}

input[type="text"], .longtext {
  width: 100%;
  padding: 10px;
  border-radius: 8px;
}
@media (max-width: 768px) {
  input[type="button"] {
    width: 100%; /* 작은 화면에서는 부모 요소에 맞게 너비를 자동 조정 */
  }
}
.longtext {
  height: 200px;
}

/* 버튼 */
button img {
  width: 20px;
  height: 20px;
}

input[type="button"] {
  padding: 10px 20px;
  background-color: #f1f1f1;
  border: none;
  border-radius: 8px;
  color: #e2e8f0;
  font-size: 16px;
  cursor: pointer;
  max-width: 390px;
  height: 52px;
  
}

/* 바텀 시트 */
.bottom-sheet-wrapper {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 999;
}

.dimmed-background {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
}

.bottom-sheet {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: #fff;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.2);
  padding: 20px;
  border-top-left-radius: 30px;
  border-top-right-radius: 30px;
  z-index: 1000;
  animation: slide-up 0.3s ease-in-out;
}

@keyframes slide-up {
  from {
    transform: translateY(100%);
  }
  to {
    transform: translateY(0);
  }
}

.bottom-sheet-content {
  align-items:left;
}

.sheet-title {
  margin-bottom: 16px;
  font-size: 20px;
  font-weight: bold;
  text-align: center;
}

.sheet-description p {
  padding: 10px 20px;
}

.sheet-description p:hover {
  background-color: #ddd;
}

/* 카테고리 선택 텍스트 스타일 */

.group-wrapper, .overlap-2 {
  background-color: #f1f1f1;
  border-radius: 8px;
  padding: 10px;
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 15px;
}

.border {
  border: 1px solid #000;
}
.group-2 {
  display: flex;
}

.group-2 > * {
  margin-right: 15px; /* 모든 자식 요소의 오른쪽에 15px 마진 추가 */
}

/* 마지막 요소의 오른쪽 마진을 제거 */
.group-2 > *:last-child {
  margin-right: 0;
}

.plain {
  text-align: center;
  width: 390px;
  border-radius: 8px;
}
.orange {
  color: #FF8F39;
  background-color: #4e4e4e;
  border-radius: 8px;
  height: 35px;
}

.my-answer {
  margin: 20px;
}
.input-container {
  position: relative;
  width: 100%; /* 원하는 너비 설정 */
  display: flex;             
  align-items: center;      
}
.input-container input {
  margin-right: 15px;     
  flex: 1;           
  width: 100%;   
  padding-right: 40px;  
}
.input-container button {
  padding: 8px 12px;        /* 버튼의 패딩 설정 */
}
.delete-button {
  position: absolute; /* 절대 위치 설정 */
  right: 10px; /* 오른쪽에서 10px 떨어진 위치 */
  top: 50%; /* 세로 중앙 정렬 */
  transform: translateY(-50%); /* 버튼을 수직 중앙으로 이동 */
  background: transparent; /* 버튼 배경 투명 설정 */
  border: none; /* 버튼 테두리 없애기 */
  cursor: pointer; /* 커서를 포인터로 변경 */
}
.close-btn {
  left: 381px;
  position: sticky;
}
</style>
