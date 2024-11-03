# Poten Day 409기
![hoohae-icon](https://github.com/user-attachments/assets/7b8ea61a-65b8-4434-8724-7324c407f5f2)

### 서비스 소개
```
가볍게 후회를 날리고 유쾌하게 위로와 조언을 받으며 성장하는 힐링 서비스

[핵심 기능]

1. 익명으로 연령대별 후회 공유
2. 후해 리포트
3. 오늘의 질문
```
### IA(Information Architecture)
![image](https://github.com/user-attachments/assets/43543082-afd7-497b-97dc-18f33d553af9)

### 화면 구성
<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/07db5b22-014f-4226-95c2-6df9602cf49d" alt="Screen 1" width="400"></td>
    <td><img src="https://github.com/user-attachments/assets/7d48e63d-1019-42c2-ab35-9703670cd223" alt="Screen 2" width="400"></td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/06b6344a-22b6-4175-bf16-921be1d63093" alt="Screen 3" width="400"></td>
    <td><img src="https://github.com/user-attachments/assets/07bf7c3c-e219-4ac3-8df9-97b616f98848" alt="Screen 4" width="400"></td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/352dfbe3-6f3b-4363-8cc5-acd371cd4e4f" alt="Screen 5" width="400"></td>
    <td><img src="https://github.com/user-attachments/assets/89531abb-e214-4766-b345-8bbb12954727" alt="Screen 6" width="400"></td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/00f660d4-102d-4405-81fc-6c557f73b6b3" alt="Project Introduction 1" width="400"></td>
    <td><img src="https://github.com/user-attachments/assets/69d300a9-f00e-48c5-939d-166650c65582" alt="Project Introduction 2" width="400"></td>
  </tr>
</table>

### 기술 스택
|언어 | 프레임워크|ORM|보안|DB|CI/CD|
|:---:|:---:|:---:|:---:|:---:|:---:|
|Java|Spring|Jpa, QueryDSL|Security, OAuth|MySQL|Github Action|
---


```
└───main
     ├─java.com.poten.hoohae
     │          ├─auth
     │          │    ├─config
     │          │    ├─controller
     │          │    ├─domain
     │          │    ├─dto
     │          │    │  ├─req
     │          │    │  └─res
     │          │    ├─filter
     │          │    ├─handler
     │          │    ├─provider
     │          │    ├─repository
     │          │    └─service
     │          │
     │          └─client
     │               ├─common
     │               ├─config
     │               ├─controller
     │               ├─domain
     │               ├─dto
     │               │  ├─req
     │               │  └─res
     │               ├─repository
     │               └─service  
     │ 
     └─resources
            └─application.yml

```

---
### ERD
![image](https://github.com/user-attachments/assets/3c386394-146a-4e4b-94eb-1cdf0f1048aa)

