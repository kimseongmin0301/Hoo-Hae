# Poten Day 409기
![hoohae-icon](https://github.com/user-attachments/assets/7b8ea61a-65b8-4434-8724-7324c407f5f2)

### 서비스 소개
```
가볍게 **후회**를 날리고 유쾌하게 **위로**와 **조언**을 받으며 성장하는 **힐링** 서비스

[핵심 기능]

1. 익명으로 연령대별 후회 공유
2. 후해 리포트
3. 오늘의 질문
```



### 기술 스택
|언어 | 프레임워크|ORM|보안|DB|CI/CD|
|:---:|:---:|:---:|:---:|:---:|:---:|
|Java|Spring|Jpa|Security, OAuth|MySQL|Github Action|
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
![image](https://github.com/user-attachments/assets/d49925e9-8f25-4cbc-bddf-f0f0147a5ff1)
