# API 명세

---
<details>
   <summary> 회원(Users) </summary>

# 회원 가입

---
## Request
```
/api/v1/auth/signup
```
## Body
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

## Response
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

| 담당자 | method | 기능       | URL                                                 | request header             | response header                 | request                | response                                                                                                                                                                                                                                                                                                                                                  |
|-----|--------|----------|-----------------------------------------------------|----------------------------|---------------------------------|------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 유태이 | POST   | 답변 생성    | /api/v1/questions/{question_id}/answers             | Authorization: BearerToken | Content-Type : application/json | { “content”: “답변 본문” } | { “code”: 200, “message”: “답변 등록이 완료되었습니다.”, “data”: { “id”: 답변ID, “content”: “답변 본문”, “created_at”: “2024-11-04”, “update_at”: “2024-11-04” } }                                                                                                                                                                                                          |  
| 유태이 | PUT    | 답변 수정    | /api/v1/questions/{question_id}/answers/{answer_id} | Authorization: BearerToken | Content-Type : application/json | { “content”: “답변 본문” } | { “code”: 200, “message”: “답변 수정이 완료되었습니다.”, “data”: { “id”: 답변ID, “content”: “수정된 답변 본문”, “created_at”: “2024-11-02”, “update_at”: “2024-11-04” } }                                                                                                                                                                                                      |  
| 유태이 | GET    | 답변 다건 조회 | /api/v1/questions/{question_id}/answers             |                            | Content-Type : application/json |                        | { “code”: 200, “message”: “답변 조회가 완료되었습니다.”, “data”: [ { “id”: 답변ID, “content”: “답변 본문”, “created_at”: “2024-11-04”, “update_at”: “2024-11-04” }, { “id”: 답변ID, “content”: “답변 본문”, “created_at”: “2024-11-04”, “update_at”: “2024-11-04” }, ] }                                                                                                          |  
| 유태이 | GET    | 답변 단건 조회 | /api/v1/questions/{question_id}/answers/{answer_id} |                            | Content-Type : application/json |                        | { “code”: 200, “message”: “답변 단건 조회가 완료되었습니다.”, “data”: [ { “id”: 답변ID, “content”: “답변 본문”, “created_at”: “2024-11-04”, “update_at”: “2024-11-04” }, { “id”: 답변ID, “content”: “답변 본문”, “created_at”: “2024-11-04”, “update_at”: “2024-11-04”, “applies”: { “id”: 답변ID, “content”: “답변 본문”, “created_at”: “2024-11-04”, “update_at”: “2024-11-04” } }, ] } |  
| 유태이 | DELETE | 답변 삭제    | /api/v1/questions/{question_id}/answers/{answer_id} | Authorization: BearerToken | Content-Type : application/json |                        | { “code”: 200, “message”: “답변 삭제가 완료되었습니다.”                                                                                                                                                                                                                                                                                                               |  

</details>
<details>
   <summary> 토론 (Discussions) </summary>

| 담당자 | method | 기능 | URL | request header | response header | request | response |
|-----|--------|----|-----|----------------|-----------------|---------|----------|
| 최욱연 |        |    |     |                |                 |         |          |

</details>
<details>
   <summary> 포트폴리오(Portfolios) </summary>

| 담당자 | method | 기능       | URL                               | request header                    | response header | request | response                                                                                                                                                                                                                                                   |
|-----|--------|----------|-----------------------------------|-----------------------------------|-----------------|---------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 김경민 | GET    | 포트폴리오 조회 | /api/v1/portfolios/{portfolio_id} | Authorization: Bearer <JWT Token> |                 |         | { ”code” : 200, ”message” : “success”, ”data” : { “portfolioId”: 1, “userId” : 1, “title” : “example”, “content” : “example”, “skill” : “Java”, “work_experience” : 3, “area” : “Seoul”, “project_history” : “project_hisotrys…”, “work_type” : “재택근무” } } |
| 김경민 | POST   | 포트폴리오 생성 | /api/v1/portfolios                | Authorization: Bearer <JWT Token> |                 |         | { ”code” : 200, ”message” : “요청에 성공 하였습니다”, ”data” : null }                                                                                                                                                                                                |
| 김경민 | PUT    | 포트폴리오 수정 | /api/v1/portfolios/{portfolio_id} | Authorization: Bearer <JWT Token> |                 |         | { ”code” : 200, ”message” : “요청에 성공 하였습니다”, ”data” : null }                                                                                                                                                                                                |
| 김경민 | DELETE | 포트폴리오 삭제 | /api/v1/portfolios/{portfolio_id} | Authorization: Bearer <JWT Token> |                 |         | { ”code” : 200, ”message” : “요청에 성공 하였습니다”, ”data” : null }                                                                                                                                                                                                |

</details>
<details>
   <summary> 북마킹 (Bookmarks) </summary>

| 담당자 | method | 기능         | URL               | request header                    | response header | request | response                                                           |
|-----|--------|------------|-------------------|-----------------------------------|-----------------|---------|--------------------------------------------------------------------|
| 김경민 | GET    | 본인의 북마킹 조회 | /api/v1/bookmarks | Authorization: Bearer <JWT Token> |                 |         | { ”code” : 200, ”message” : “요청에 성공 하였습니다”, ”data” : [1,2,3,4,5] } |
| 김경민 | POST   | 북마킹 생성     | /api/v1/bookmarks | Authorization: Bearer <JWT Token> |                 |         | { ”code” : 201, ”message” : “요청에 성공 하였습니다”, ”data” : null }        |
| 김경민 | DELETE | 북마킹 삭제     | /api/v1/bookmarks | Authorization: Bearer <JWT Token> |                 |         | { ”code” : 201, ”message” : “요청에 성공 하였습니다”, ”data” : null }        |

</details>
<details>
   <summary> 매칭 (Matching) </summary>

| 담당자 | method | 기능    | URL                             | request header                    | response header | request                                    | response                                                                           |
|-----|--------|-------|---------------------------------|-----------------------------------|-----------------|--------------------------------------------|------------------------------------------------------------------------------------|
| 김경민 | GET    | 매칭 조회 | /api/v1/Matchings/{matching_id} | Authorization: Bearer <JWT Token> |                 |                                            | { ”code” : 200, ”message” : “요청에 성공 하였습니다”, ”data” : { “status” : “connection” } } | 
| 김경민 | POST   | 매칭 생성 | /api/v1/Matchings               | Authorization: Bearer <JWT Token> |                 | { “portfolioId” : 1, “outsourcingId” : 1 } | { ”code” : 200, ”message” : “요청에 성공 하였습니다”, ”data” : null }                        |
| 김경민 | PUT    | 매칭 수정 | /api/v1/Matchings/{matching_id} | Authorization: Bearer <JWT Token> |                 | { “status” : “dis connection” }            | { ”code” : 200, ”message” : “요청에 성공 하였습니다”, ”data” : null }                        |  
| 김경민 | DELETE | 매칭 삭제 | /api/v1/Matchings/{matching_id} | Authorization: Bearer <JWT Token> |                 |                                            | { ”code” : 200, ”message” : “요청에 성공 하였습니다”, ”data” : null }                        |

</details>
<details>
   <summary> 외주 (Outsourcing) </summary>

| 담당자 | method | 기능    | URL                                   | request header                    | response header | request                                                                                                                                                                                                                                                                                                         | response                                                    |
|-----|--------|-------|---------------------------------------|-----------------------------------|-----------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------|
| 김경민 | GET    | 외주 조회 | /api/v1/outsourcings/{outsourcing_id} | Authorization: Bearer <JWT Token> |                 |                                                                                                                                                                                                                                                                                                                 | { ”code” : 200, ”message” : “요청에 성공 하였습니다”, ”data” : null } |
| 김경민 | POST   | 외주 생성 | /api/v1/outsourcings                  | Authorization: Bearer <JWT Token> |                 | { “outsourctingId”: 1, “userId” : 1, “title” : “example”, “content” : “example”, “preferential” : “Java 잘하는 사람 우대”, “work_type” : “재택근무” “skill” : “Java”, “price” : 1,000,000 “dead_line” : 2024-01-02, “start_date” : 2024-01-01 “area” : “Seoul”, “period” : “6개월”, “area” : “Seoul”, “skill” : “java” }     |                                                             |
| 김경민 | PUT    | 외주 수정 | /api/v1/outsourcings/{outsourcing_id} | Authorization: Bearer <JWT Token> |                 | { “outsourctingId”: 2, “userId” : 2, “title” : “example1”, “content” : “example1”, “preferential” : “Java 1잘하는 사람 우대”, “work_type” : “재택근무1” “skill” : “Java”, “price” : 1,000,000 “dead_line” : 2024-01-02, “start_date” : 2024-01-01 “area” : “Seoul”, “period” : “6개월”, “area” : “Seoul”, “skill” : “java” } | { ”code” : 200, ”message” : “요청에 성공 하였습니다”, ”data” : null } |
| 김경민 | DELETE | 외주 삭제 | /api/v1/outsourcings/{outsourcing_id} | Authorization: Bearer <JWT Token> |                 |                                                                                                                                                                                                                                                                                                                 | { ”code” : 200, ”message” : “요청에 성공 하였습니다”, ”data” : null } |

</details>