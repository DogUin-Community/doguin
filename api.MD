# API 명세

---
<details>
   <summary> 회원(Users) </summary>

## 회원 가입

---
### Request
```
/api/v1/auth/signup
```
### Body
```
{
    "email":"test@naver.com",
    "password":"qwer12!!",
    "nickname":"망곰이",
    "userType":"COMPANY",
    "userRole":"ROLE_USER",
    "profileImage":"",
    "introduce":"안녕하세요.딸기입니다.",
    "homeAddress":"전북 익산시 땡땡땡",
    "gitAddress":"https://github.com/yunseohhe",
    "blogAddress":"https://ysh2.tistory.com/"
}
```

### Response
```

```

</details>
<details>
   <summary> 마이페이지(My-pages) </summary>

| 담당자 | 진행상황 | method | 기능 | URL | request header | response header | request | response |
|-----|------|--------|----|-----|----------------|-----------------|---------|----------|
|     |      |        |    |     |                |                 |         |          |

</details>
<details>
   <summary> 팔로우(Follows) </summary>

| 담당자 | 진행상황 | method | 기능 | URL | request header | response header | request | response |
|-----|------|--------|----|-----|----------------|-----------------|---------|----------|
|     |      |        |    |     |                |                 |         |          |

</details>
<details>
   <summary> 게시글(Boards)(자유 게시판) </summary>

| 담당자 | 진행상황 | method | 기능 | URL | request header | response header | request | response |
|-----|------|--------|----|-----|----------------|-----------------|---------|----------|
|     |      |        |    |     |                |                 |         |          |

</details>
<details>
   <summary> 게시글(Boards)(공지) </summary>

| 담당자 | 진행상황 | method | 기능 | URL | request header | response header | request | response |
|-----|------|--------|----|-----|----------------|-----------------|---------|----------|
|     |      |        |    |     |                |                 |         |          |

</details>
<details>
   <summary> 게시글(Boards)(이벤트) </summary>

| 담당자 | 진행상황 | method | 기능 | URL | request header | response header | request | response |
|-----|------|--------|----|-----|----------------|-----------------|---------|----------|
|     |      |        |    |     |                |                 |         |          |

</details>
<details>
   <summary> 게시글(Boards)(1대1 문의) </summary>

| 담당자 | 진행상황 | method | 기능 | URL | request header | response header | request | response |
|-----|------|--------|----|-----|----------------|-----------------|---------|----------|
|     |      |        |    |     |                |                 |         |          |

</details>
<details>
   <summary> 신고(Reports) </summary>

| 담당자 | 진행상황 | method | 기능 | URL | request header | response header | request | response |
|-----|------|--------|----|-----|----------------|-----------------|---------|----------|
|     |      |        |    |     |                |                 |         |          |

</details>
<details>
   <summary> 질문(Questions) </summary>

| 담당자 | 진행상황 | method | 기능 | URL | request header | response header | request | response |
|-----|------|--------|----|-----|----------------|-----------------|---------|----------|
|     |      |        |    |     |                |                 |         |          |

</details>
<details>
   <summary> 답변 (자유 게시판, 답변, 1대1 문의, 질문에 적용 가능) </summary>

## 답변 생성

---
### URL
```
[POST] /api/v1/questions/{question_id}/answers
```

### Request
```
header : Authorization: BearerToken
body : 
{
“content”: “답변 본문”
}
```

### Response
```
{
“code”: 200,
“message”: “답변 등록이 완료되었습니다.”,
“data”: {
    “id”: 답변ID,
    “content”: “답변 본문”,
    “created_at”: “2024-11-04”,
    “update_at”: “2024-11-04”
    }
}
```
## 답변 수정

---
### URL
```
[UPDATE] /api/v1/questions/{question_id}/answers/{answer_id}
```

### Request
```
header : Authorization: BearerToken
body : 
{
“content”: “답변 본문”
}
```

### Response
```
{
“code”: 200,
“message”: “답변 수정이 완료되었습니다.”,
“data”: {
    “id”: 답변ID,
    “content”: “수정된 답변 본문”,
    “created_at”: “2024-11-02”,
    “update_at”: “2024-11-04”
    }
}
```
## 답변 다건 조회

---
### URL
```
[GET] /api/v1/questions/{question_id}/answers
```

### Request
```


```

### Response
```
{
“code”: 200,
“message”: “답변 수정이 완료되었습니다.”,
“data”: {
    “id”: 답변ID,
    “content”: “수정된 답변 본문”,
    “created_at”: “2024-11-02”,
    “update_at”: “2024-11-04”
    }
}
```
## 답변 단건 조회

---
### URL
```
[GET] /api/v1/questions/{question_id}/answers/{answer_id}
```

### Request
```

```

### Response
```
{
“code”: 200,
“message”: “답변 조회가 완료되었습니다.”,
“data”: [
    {
        “id”: 답변ID,
        “content”: “답변 본문”,
        “created_at”: “2024-11-04”,
        “update_at”: “2024-11-04”
    },
    {
        “id”: 답변ID,
        “content”: “답변 본문”,
        “created_at”: “2024-11-04”,
        “update_at”: “2024-11-04”
    }, ...
    ]
}
```
## 답변 삭제

---
### URL
```
[DELETE] /api/v1/questions/{question_id}/answers/{answer_id}
```

### Request
```
header : Authorization: BearerToken
```

### Response
```
{
    “code”: 200,
    “message”: “답변 삭제가 완료되었습니다.”
}
```

</details>
<details>
   <summary> 토론 (Discussions) </summary>

</details>
<details>
   <summary> 포트폴리오(Portfolios) </summary>

## 포트폴리오 단건 조회

---
### URL
```
[GET] /api/v1/portfolios/{portfolioId}
```

### Request
```
-
```

### Response
```
{
  "code": 200,
  "message": "포트폴리오 작업 요청 성공",
  "data": {
    "id": 1,
    "user_id": 1,
    "title": "aaa",
    "content": "Lookin123123123g for a senior Java developer position with extensive experience in Spring Boot and microservices architecture.",
    "work_experience": 5,
    "work_type": "Full-time",
    "project_history": "Developed and led multiple large-scale enterprise applications including an e-commerce platform and a financial risk management system.",
    "area": "SEOUL",
    "filePath": [
      "https://doguin.s3.ap-northeast-2.amazonaws.com/user/1/portfolio/1/5c10fea5-0e20-4868-af13-3b4881e50215빅토르4.jpg"
    ],
    "createdAt": "2024-11-03T20:13:58.732251",
    "updatedAt": "2024-11-03T20:15:25.56348"
  }
}
```

## 포트폴리오 생성

---
### URL
```
[POST] /api/v1/portfolios
```

### Request
```
header : Authorization: Bearer <JWT Token>
body : 
{
    “portfolioId”: 1,
    “userId” : 1,
    “title” : “example”,    
    “content” : “example”,
    “skill” : “Java”,
    “work_experience” : 3,
    “area” : “Seoul”,
    “project_history” : “project_hisotrys…”,
    “work_type” : “재택근무”
  }
```

### Response
```
{
    "code": 200,
    "message": "포트폴리오 작업 요청 성공",
    "data": {
        "id": 3,
        "user_id": 1,
        "title": "Senior Developer Portfolio",
        "content": "This portfolio highlights my experience in backend development with Java and Spring.",
        "work_experience": 10,
        "work_type": "Remote",
        "project_history": "Developed multiple microservices for an e-commerce platform.",
        "area": "SEOUL",
        "fileId": [
            7,
            8
        ],
        "createdAt": "2024-11-04T13:18:03.546149",
        "updatedAt": "2024-11-04T13:18:03.546149"
    }
}
```

## 포트폴리오 수정

---
### URL
```
[PUT] /api/v1/portfolios
```

### Request
```
header : Authorization: Bearer <JWT Token>
body : 
{
    “portfolioId”: 1,
    “userId” : 1,
    “title” : “example”,    
    “content” : “example”,
    “skill” : “Java”,
    “work_experience” : 3,
    “area” : “Seoul”,
    “project_history” : “project_hisotrys…”,
    “work_type” : “재택근무”
  }
```

### Response
```
{
    "code": 200,
    "message": "포트폴리오 작업 요청 성공",
    "data": null
}
```
## 포트폴리오 삭제

---
### URL
```
[DELETE] /api/v1/portfolios/{portfolioId}
```

### Request
```
header : Authorization: Bearer <JWT Token>

```

### Response
```
{
    "code": 200,
    "message": "포트폴리오 작업 요청 성공",
    "data": null
}
```

## 모든 포트폴리오 조회

---
### URL
```
[GET] /api/v1/portfolios/other
```

### Request
```
requestParam : 
page (null 허용, default 0)
size (null 허용, default 10)
sort (null 허용, default desc)
```

### Response
```
{
    "code": 200,
    "message": "포트폴리오 작업 요청 성공",
    "data": {
        "content": [
            {
                "id": 3,
                "userId": 1,
                "title": "Senior Developer Portfolio",
                "content": "This portfolio highlights my experience in backend development with Java and Spring.",
                "work_experience": 10,
                "work_type": "Remote",
                "project_history": "Developed multiple microservices for an e-commerce platform.",
                "area": "SEOUL",
                "filePath": [
                    "https://doguin.s3.ap-northeast-2.amazonaws.com/user/1/portfolio/3/eefebea9-778b-43fa-82a1-63ee76ebd31090b987ef-e145-4b4f-92b2-b4d844d1aafb_Viktor_600x600(2).jpg",
                    "https://doguin.s3.ap-northeast-2.amazonaws.com/user/1/portfolio/3/7609739b-875d-418d-b254-8065aee286f690b987ef-e145-4b4f-92b2-b4d844d1aafb_Viktor_600x600(3).jpg"
                ],
                "createdAt": "2024-11-04T13:18:03.546149",
                "updatedAt": "2024-11-04T13:18:03.546149"
            },
            {
                "id": 2,
                "userId": 1,
                "title": "Senior Developer Portfolio",
                "content": "This portfolio highlights my experience in backend development with Java and Spring.",
                "work_experience": 10,
                "work_type": "Remote",
                "project_history": "Developed multiple microservices for an e-commerce platform.",
                "area": "SEOUL",
                "filePath": [],
                "createdAt": "2024-11-04T13:17:53.536317",
                "updatedAt": "2024-11-04T13:17:53.536317"
            },
            {
                "id": 1,
                "userId": 1,
                "title": "test123",
                "content": "Lookin123123123g for a senior Java developer position with extensive experience in Spring Boot and microservices architecture.",
                "work_experience": 5,
                "work_type": "Full-time",
                "project_history": "Developed and led multiple large-scale enterprise applications including an e-commerce platform and a financial risk management system.",
                "area": "SEOUL",
                "filePath": [
                    "https://doguin.s3.ap-northeast-2.amazonaws.com/user/1/portfolio/1/5c10fea5-0e20-4868-af13-3b4881e50215빅토르4.jpg"
                ],
                "createdAt": "2024-11-03T20:13:58.732251",
                "updatedAt": "2024-11-04T13:21:48.407785"
            }
        ],
        "page": {
            "size": 10,
            "number": 0,
            "totalElements": 3,
            "totalPages": 1
        }
    }
}
```

## 자신의 포트폴리오 전체 조회

---
### URL
```
[GET] /api/v1/portfolios/my
```

### Request
```
requestParam :
page (null 허용, default 0)
size (null 허용, default 10)
sort (null 허용, default desc)
```

### Response
```
{
    "code": 200,
    "message": "포트폴리오 작업 요청 성공",
    "data": {
        "content": [
            {
                "id": 3,
                "userId": 1,
                "title": "Senior Developer Portfolio",
                "content": "This portfolio highlights my experience in backend development with Java and Spring.",
                "work_experience": 10,
                "work_type": "Remote",
                "project_history": "Developed multiple microservices for an e-commerce platform.",
                "area": "SEOUL",
                "filePath": [
                    "https://doguin.s3.ap-northeast-2.amazonaws.com/user/1/portfolio/3/eefebea9-778b-43fa-82a1-63ee76ebd31090b987ef-e145-4b4f-92b2-b4d844d1aafb_Viktor_600x600(2).jpg",
                    "https://doguin.s3.ap-northeast-2.amazonaws.com/user/1/portfolio/3/7609739b-875d-418d-b254-8065aee286f690b987ef-e145-4b4f-92b2-b4d844d1aafb_Viktor_600x600(3).jpg"
                ],
                "createdAt": "2024-11-04T13:18:03.546149",
                "updatedAt": "2024-11-04T13:18:03.546149"
            },
            {
                "id": 2,
                "userId": 1,
                "title": "Senior Developer Portfolio",
                "content": "This portfolio highlights my experience in backend development with Java and Spring.",
                "work_experience": 10,
                "work_type": "Remote",
                "project_history": "Developed multiple microservices for an e-commerce platform.",
                "area": "SEOUL",
                "filePath": [],
                "createdAt": "2024-11-04T13:17:53.536317",
                "updatedAt": "2024-11-04T13:17:53.536317"
            },
            {
                "id": 1,
                "userId": 1,
                "title": "test123",
                "content": "Lookin123123123g for a senior Java developer position with extensive experience in Spring Boot and microservices architecture.",
                "work_experience": 5,
                "work_type": "Full-time",
                "project_history": "Developed and led multiple large-scale enterprise applications including an e-commerce platform and a financial risk management system.",
                "area": "SEOUL",
                "filePath": [
                    "https://doguin.s3.ap-northeast-2.amazonaws.com/user/1/portfolio/1/5c10fea5-0e20-4868-af13-3b4881e50215빅토르4.jpg"
                ],
                "createdAt": "2024-11-03T20:13:58.732251",
                "updatedAt": "2024-11-04T13:21:48.407785"
            }
        ],
        "page": {
            "size": 10,
            "number": 0,
            "totalElements": 3,
            "totalPages": 1
        }
    }
}
```
</details>
<details>
   <summary> 북마킹 (Bookmarks) </summary>

## 내 북마킹 조회

---
### URL
```
[GET] /api/v1/bookmarks
```

### Request
```
header : Authorization: Bearer <JWT Token>

```

### Response
```
{
    "code": 200,
    "message": "북마크 작업에 성공 하였습니다",
    "data": {
        "content": [
            {
                "id": 1,
                "userId": 1,
                "targetId": 1,
                "target": "OUTSOURCING",
                "createdAt": "2024-11-04T13:42:17.237135",
                "updatedAt": "2024-11-04T13:42:17.237135"
            }
        ],
        "page": {
            "size": 10,
            "number": 0,
            "totalElements": 1,
            "totalPages": 1
        }
    }
}
```

## 북마킹 생성

---
### URL
```
[POST] /api/v1/bookmarks
```

### Request
```
header : Authorization: Bearer <JWT Token>
body : 
{
    "targetId": 1,
    "target" : "OUTSOURCING"
}
```

### Response
```
{
    "code": 200,
    "message": "북마크 작업에 성공 하였습니다",
    "data": null
}
```

## 북마킹 삭제

---
### URL
```
[DELETE] /api/v1/bookmarks
```

### Request
```
header : Authorization: Bearer <JWT Token>
```

### Response
```
{
    "code": 200,
    "message": "북마크 작업에 성공 하였습니다",
    "data": null
}
```
</details>
<details>
   <summary> 매칭 (Matching) </summary>

## 자신의 모든 매칭 조회

---
### URL
```
[GET] /api/v1/matchings 
```

### Request
```
header : Authorization: Bearer <JWT Token>
requestParam
page (null 허용, default 0)
size (null 허용, default 10)
sort (null 허용, default desc)
```

### Response
```
{
    "code": 200,
    "message": "매칭작업에 성공 하였습니다",
    "data": {
        "content": [
            {
                "id": 1,
                "userId": 2,
                "portfolioId": 4,
                "outsourcingId": 2,
                "status": "READY"
            }
        ],
        "page": {
            "size": 10,
            "number": 0,
            "totalElements": 1,
            "totalPages": 1
        }
    }
}
```

## 매칭 생성

---
### URL
```
[POST] /api/v1/Matchings
```

### Request
```
header : Authorization: Bearer <JWT Token>
body : 
{
  “portfolioId” : 1,
  “outsourcingId” : 1
}
```

### Response
```
{
    "code": 200,
    "message": "매칭작업에 성공 하였습니다",
    "data": null
}
```

## 매칭 수정

---
### URL
```
[PUT] /api/v1/Matchings/{matching_id}
```

### Request
```
header : Authorization: Bearer <JWT Token>
body : 
{
  "status": "NO"
}
```

### Response
```
{
    "code": 200,
    "message": "매칭작업에 성공 하였습니다",
    "data": null
}
```

## 매칭 삭제

---
### URL
```
[DELETE] /api/v1/Matchings/{matching_id}
```

### Request
```
header : Authorization: Bearer <JWT Token>
```

### Response
```
{
    "code": 200,
    "message": "매칭작업에 성공 하였습니다",
    "data": null
}
```
</details>
<details>
   <summary> 외주 (Outsourcing) </summary>

## 외주 단건 조회

---
### URL
```
[GET] /api/v1/outsourcings/{outsourcingId}
```

### Request
```
header : Authorization: Bearer <JWT Token>
```

### Response
```
{
  "code": 200,
  "message": "외주 작업 요청에 성공 하였습니다",
  "data": {
    "id": 1,
    "userId": 1,
    "title": "Mobile App Development",
    "content": "Looking for a mobile app developer to build an e-commerce app.",
    "preferential": "Experience with Flutter is preferred.",
    "work_type": "Remote",
    "price": 10000,
    "recruit_start_date": "2024-10-01T09:00:00",
    "recruit_end_date": "2024-10-31T18:00:00",
    "work_start_date": "2024-11-10T09:00:00",
    "work_end_date": "2025-01-10T18:00:00",
    "area": "SEOUL",
    "createdAt": "2024-11-03T20:35:36.984211",
    "updatedAt": "2024-11-03T20:35:36.984211",
    "filePaths": [
      "https://doguin.s3.ap-northeast-2.amazonaws.com/user/1/outsourcing/1/ad2e5d33-c814-434a-ba0f-8f28841fd96290b987ef-e145-4b4f-92b2-b4d844d1aafb_Viktor_600x600(1).jpg",
      "https://doguin.s3.ap-northeast-2.amazonaws.com/user/1/outsourcing/1/ae6f032a-f2c5-4016-bf98-45e4d4f9ced690b987ef-e145-4b4f-92b2-b4d844d1aafb_Viktor_600x600(2).jpg",
      "https://doguin.s3.ap-northeast-2.amazonaws.com/user/1/outsourcing/1/d1541839-b4bf-492e-9ce1-0be355bb5bcc90b987ef-e145-4b4f-92b2-b4d844d1aafb_Viktor_600x600(3).jpg",
      "https://doguin.s3.ap-northeast-2.amazonaws.com/user/1/outsourcing/1/cd347918-1f09-499c-b073-97c6516c230190b987ef-e145-4b4f-92b2-b4d844d1aafb_Viktor_600x600(4).jpg",
      "https://doguin.s3.ap-northeast-2.amazonaws.com/user/1/outsourcing/1/18f38b7e-706d-4bf0-a3a6-cd9ddaced48490b987ef-e145-4b4f-92b2-b4d844d1aafb_Viktor_600x600.jpg"
    ]
  }
}
```

## 외주 생성 

---
### URL
```
[POST] /api/v1/outsourcings
```

### Request
```
header : Authorization: Bearer <JWT Token>
body : 
{
    "title": "Mobile App Development",
    "content": "Looking for a mobile app developer to build an e-commerce app.",
    "preferential": "Experience with Flutter is preferred.",
    "work_type": "Remote",
    "price": 10000,
    "recruit_start_date": "2024-10-01T09:00:00",
    "recruit_end_date": "2024-10-31T18:00:00",
    "work_start_date": "2024-11-10T09:00:00",
    "work_end_date": "2025-01-10T18:00:00",
    "area": "SEOUL"
}
```

### Response
```
{
    "code": 200,
    "message": "외주 작업 요청에 성공 하였습니다",
    "data": {
        "id": 3,
        "userId": 1,
        "title": "Mobile App Development",
        "content": "Looking for a mobile app developer to build an e-commerce app.",
        "preferential": "Experience with Flutter is preferred.",
        "work_type": "Remote",
        "price": 10000,
        "recruit_start_date": "2024-10-01T09:00:00",
        "recruit_end_date": "2024-10-31T18:00:00",
        "work_start_date": "2024-11-10T09:00:00",
        "work_end_date": "2025-01-10T18:00:00",
        "area": "SEOUL",
        "createdAt": "2024-11-04T13:57:02.213324",
        "updatedAt": "2024-11-04T13:57:02.213324",
        "fileIds": [
            9,
            10,
            11,
            12,
            13
        ]
    }
}
```

## 외주 수정

---
### URL
```
[PUT] /api/v1/outsourcings/{outsourcing_id}
```

### Request
```
header : Authorization: Bearer <JWT Token>
body : 
{
    "title": "Mobile App11 Development",
    "content": "Looking f11or a mobile app developer to build an e-commerce app.",
    "preferential": "Experie11nce with Flutter is preferred.",
    "work_type": "Remote",
    "price": 10000,
    "recruit_start_date": "2024-10-01T09:00:00",
    "recruit_end_date": "2024-10-31T18:00:00",
    "work_start_date": "2024-11-10T09:00:00",
    "work_end_date": "2025-01-10T18:00:00",
    "area": "SEOUL"
}
```

### Response
```
{
    "code": 200,
    "message": "외주 작업 요청에 성공 하였습니다",
    "data": null
}
```

## 외주 삭제

---
### URL
```
[DELETE] /api/v1/outsourcings/{outsourcing_id}
```

### Request
```
header : Authorization: Bearer <JWT Token>
```

### Response
```
{
    "code": 200,
    "message": "외주 작업 요청에 성공 하였습니다",
    "data": null
}
```

## 외주 다건 조회

---
### URL
```
[GET] /api/v1/outsourcings
```

### Request
```
requestParam : 
page (null 허용, default 0)
size (null 허용, default 10)
sort (null 허용, default desc)
```

### Response
```
{
  "code": 200,
  "message": "외주 작업 요청에 성공 하였습니다",
  "data": {
    "content": [
      {
        "id": 3,
        "userId": 1,
        "title": "Mobile App Development",
        "content": "Looking for a mobile app developer to build an e-commerce app.",
        "preferential": "Experience with Flutter is preferred.",
        "work_type": "Remote",
        "price": 10000,
        "recruit_start_date": "2024-10-01T09:00:00",
        "recruit_end_date": "2024-10-31T18:00:00",
        "work_start_date": "2024-11-10T09:00:00",
        "work_end_date": "2025-01-10T18:00:00",
        "area": "SEOUL",
        "createdAt": "2024-11-04T13:57:02.213324",
        "updatedAt": "2024-11-04T13:57:02.213324",
        "filePaths": [
          "https://doguin.s3.ap-northeast-2.amazonaws.com/user/1/outsourcing/3/efd7f8ce-3675-4055-80a5-0dafd1da80bf90b987ef-e145-4b4f-92b2-b4d844d1aafb_Viktor_600x600(1).jpg",
          "https://doguin.s3.ap-northeast-2.amazonaws.com/user/1/outsourcing/3/2b2b9f21-45ad-4bab-a1de-8b22a890e22590b987ef-e145-4b4f-92b2-b4d844d1aafb_Viktor_600x600(2).jpg",
          "https://doguin.s3.ap-northeast-2.amazonaws.com/user/1/outsourcing/3/2541c579-2217-4fe5-856f-160db53498fc90b987ef-e145-4b4f-92b2-b4d844d1aafb_Viktor_600x600(3).jpg",
          "https://doguin.s3.ap-northeast-2.amazonaws.com/user/1/outsourcing/3/03608855-755e-43dc-a7ff-bea950b91e5690b987ef-e145-4b4f-92b2-b4d844d1aafb_Viktor_600x600(4).jpg",
          "https://doguin.s3.ap-northeast-2.amazonaws.com/user/1/outsourcing/3/db42f23a-1600-4e74-adfd-5244adc3f03690b987ef-e145-4b4f-92b2-b4d844d1aafb_Viktor_600x600.jpg"
        ]
      },
      {
        "id": 2,
        "userId": 1,
        "title": "Mobile App Development",
        "content": "Looking for a mobile app developer to build an e-commerce app.",
        "preferential": "Experience with Flutter is preferred.",
        "work_type": "Remote",
        "price": 10000,
        "recruit_start_date": "2024-10-01T09:00:00",
        "recruit_end_date": "2024-10-31T18:00:00",
        "work_start_date": "2024-11-10T09:00:00",
        "work_end_date": "2025-01-10T18:00:00",
        "area": "SEOUL",
        "
```

## 외주 검색

---
### URL
```
[GET] /api/v1/outsourcings/search
```

### Request
```
requestParam : 
page (null 허용, default 0)
size (null 허용, default 10)
sort (null 허용, default desc)
title (null 허용)
nickname (null 허용)
content (null 허용)

```

### Response
```
{
  "code": 200,
  "message": "외주 작업 요청에 성공 하였습니다",
  "data": {
    "content": [
      {
        "id": 3,
        "userId": 1,
        "title": "Mobile App Development",
        "content": "Looking for a mobile app developer to build an e-commerce app.",
        "preferential": "Experience with Flutter is preferred.",
        "work_type": "Remote",
        "price": 10000,
        "recruit_start_date": "2024-10-01T09:00:00",
        "recruit_end_date": "2024-10-31T18:00:00",
        "work_start_date": "2024-11-10T09:00:00",
        "work_end_date": "2025-01-10T18:00:00",
        "area": "SEOUL",
        "createdAt": "2024-11-04T13:57:02.213324",
        "updatedAt": "2024-11-04T13:57:02.213324",
        "filePaths": [
          "https://doguin.s3.ap-northeast-2.amazonaws.com/user/1/outsourcing/3/efd7f8ce-3675-4055-80a5-0dafd1da80bf90b987ef-e145-4b4f-92b2-b4d844d1aafb_Viktor_600x600(1).jpg",
          "https://doguin.s3.ap-northeast-2.amazonaws.com/user/1/outsourcing/3/2b2b9f21-45ad-4bab-a1de-8b22a890e22590b987ef-e145-4b4f-92b2-b4d844d1aafb_Viktor_600x600(2).jpg",
          "https://doguin.s3.ap-northeast-2.amazonaws.com/user/1/outsourcing/3/2541c579-2217-4fe5-856f-160db53498fc90b987ef-e145-4b4f-92b2-b4d844d1aafb_Viktor_600x600(3).jpg",
          "https://doguin.s3.ap-northeast-2.amazonaws.com/user/1/outsourcing/3/03608855-755e-43dc-a7ff-bea950b91e5690b987ef-e145-4b4f-92b2-b4d844d1aafb_Viktor_600x600(4).jpg",
          "https://doguin.s3.ap-northeast-2.amazonaws.com/user/1/outsourcing/3/db42f23a-1600-4e74-adfd-5244adc3f03690b987ef-e145-4b4f-92b2-b4d844d1aafb_Viktor_600x600.jpg"
        ]
      },
      {
        "id": 2,
        "userId": 1,
        "title": "Mobile App Development",
        "content": "Looking for a mobile app developer to build an e-commerce app.",
        "preferential": "Experience with Flutter is preferred.",
        "work_type": "Remote",
        "price": 10000,
        "recruit_start_date": "2024-10-01T09:00:00",
        "recruit_end_date": "2024-10-31T18:00:00",
        "work_start_date": "2024-11-10T09:00:00",
        "work_end_date": "2025-01-10T18:00:00",
        "area": "SEOUL",
        "createdAt": "2024-11-03T20:37:39.682276",
        "updatedAt": "2024-11-03T20:37:39.682276",
        "filePaths": []
      },
      {
        "id": 1,
        "userId": 1,
        "title": "aa1",
        "content": "test co111ntetn",
        "preferential": "test123213",
        "work_type": "test123123",
        "price": 222222,
        "recruit_start_date": "2024-10-01T09:00:00",
        "recruit_end_date": "2024-10-31T18:00:00",
        "work_start_date": "2024-11-10T09:00:00",
        "work_end_date": "2025-01-10T18:00:00",
        "area": "INCHEON",
        "createdAt": "2024-11-03T20:35:36.984211",
        "updatedAt": "2024-11-04T14:00:47.298459",
        "filePaths": [
          "https://doguin.s3.ap-northeast-2.amazonaws.com/user/1/outsourcing/1/ad2e5d33-c814-434a-ba0f-8f28841fd96290b987ef-e145-4b4f-92b2-b4d844d1aafb_Viktor_600x600(1).jpg",
          "https://doguin.s3.ap-northeast-2.amazonaws.com/user/1/outsourcing/1/ae6f032a-f2c5-4016-bf98-45e4d4f9ced690b987ef-e145-4b4f-92b2-b4d844d1aafb_Viktor_600x600(2).jpg",
          "https://doguin.s3.ap-northeast-2.amazonaws.com/user/1/outsourcing/1/d1541839-b4bf-492e-9ce1-0be355bb5bcc90b987ef-e145-4b4f-92b2-b4d844d1aafb_Viktor_600x600(3).jpg",
          "https://doguin.s3.ap-northeast-2.amazonaws.com/user/1/outsourcing/1/cd347918-1f09-499c-b073-97c6516c230190b987ef-e145-4b4f-92b2-b4d844d1aafb_Viktor_600x600(4).jpg",
          "https://doguin.s3.ap-northeast-2.amazonaws.com/user/1/outsourcing/1/18f38b7e-706d-4bf0-a3a6-cd9ddaced48490b987ef-e145-4b4f-92b2-b4d844d1aafb_Viktor_600x600.jpg"
        ]
      }
    ],
    "page": {
      "size": 10,
      "number": 0,
      "totalElements": 3,
      "totalPages": 1
    }
  }
}
```
</details>
<details>
    <summary> 파일 (Outsourcing) </summary>

## 파일 전체 조회

---
### URL
```
[GET] /api/v1/attachments/fileIds/{fileIds}
```

### Request
```
header : Authorization: Bearer <JWT Token>
```

### Response
```
{
    "code": 200,
    "message": "파일 작업요청에 성공 하였습니다",
    "data": null
}
```

## 파일 생성

---
### URL
```
[POST] /api/v1/attachments/targetId/{targetId}/target/{target}
```

### Request
```
header : Authorization: Bearer <JWT Token>
```

### Response
```
{
    "code": 200,
    "message": "파일 작업요청에 성공 하였습니다",
    "data": null
}
```

## 파일 수정

---
### URL
```
/api/v1/attachments/targetId/{targetId}/target/{target}
```

### Request
```
header : Authorization: Bearer <JWT Token>
```

### Response
```
{
    "code": 200,
    "message": "파일 작업요청에 성공 하였습니다",
    "data": null
}
```

## 파일 삭제

---
### URL
```
[DELETE] /api/v1/attachments/fileIds/{fileIds}
```

### Request
```
header : Authorization: Bearer <JWT Token>
```

### Response
```
{
    "code": 200,
    "message": "파일 작업요청에 성공 하였습니다",
    "data": null
}
```
</details>