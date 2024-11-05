# API 명세

---
<details>
   <summary> 회원(Users) </summary>

## 회원 가입

---
### URL
```
[POST]/api/v1/auth/signup
```

### Request
```
body(form-data)
    signupRequest : 
{
    "email":"회원가입 할 이메일",
    "password":"비밀번호",
    "nickname":"닉네임",
    "userType":"개인(INDIVIDUAL) 또는 회사(COMPANY) 선택",
    "userRole":"유저(ROLE_USER) 또는 어드민(ROLE_ADMIN) 선택",
    "introduce":"자기소개",
    "homeAddress":"주소",
    "gitAddress":"깃헙 주소",
    "blogAddress":"블로그 주소"
}
    files : 업로드 할 프로필 이미지
```

### Response
```
body : 
{ 
   ”code” : 200, 
   ”message” : “회원 가입에 성공하였습니다.”, 
   “data”:”null”
}
```

## 로그인

---
### URL
```
[POST]/api/v1/auth/signin
```

### Request
```
body : 
{ 
   “email”:"로그인 할 이메일"
   “password“:"비밀번호"
}
```

### Response
```
header : 
    Authorization: Bearer <JWT Token>
body : 
{ 
   ”code” : 200, 
   ”message” : “로그인에 성공하였습니다.”,  
   “data”:”null”
}
```

## 회원 정보 조회

---
### URL
```
[GET]/api/v1/users/check
```

### Request
```
header : 
    Authorization: Bearer <Token>
```

### Response
```
body : 
{
   "code": 200,
   "message": "회원 정보 조회에 성공하였습니다.",
   "data": {
        "id": ,
        "email": "",
        "nickname": "",
        "userType": "",
        "userRole": "",
        "profileImage": "",
        "introduce": "",
        "homeAddress": "",
        "gitAddress": "",
        "blogAddress": "",
        "userGrade": ""
   }
}
```

## 회원 정보 수정

---
### URL
```
[PUT]/api/v1/users/update
```

### Request
```
header : 
    Authorization: Bearer <Token>
body : 
{
   "email”:”",
   "password":"",
   "nickname":"",
   "userType":"",
   "userRole":"",
   "profileImage":"",
   "introduce":"",
   "homeAddress":"",
   "gitAddress":"",
   "blogAddress":””
}
```

### Response
```
body : 
{
   "code": 200,
   "message": "회원 정보 수정에 성공하였습니다.",
   "data": {
        "id": ,
        "email": "",
        "nickname": "",
        "userType": "",
        "userRole": "",
        "profileImage": "",
        "introduce": "",
        "homeAddress": "",
        "gitAddress": "",
        "blogAddress": ""
   }
}
```

## 회원 탈퇴

---
### URL
```
[DELETE]/api/v1/users/delete
```

### Request
```
header : 
    Authorization: Bearer <Token>
```

### Response
```
body : 
{
   "code": 200,
   "message": "회원 탈퇴에 성공하였습니다.",
    "data": null
}
```

</details>
<details>
   <summary> 마이페이지(My-pages) </summary>

## 마이페이지 조회

---
### URL
```
[GET]/api/v1/mypages
```

### Request
```
header : 
    Authorization: Bearer <Token>
```

### Response
```
body : 
{
   "code": 200,
   "message": "마이페이지 조회에 성공하였습니다.",
   "data": 
   {
      "email": "",
      "nickname": "",
      "followedCount": 0,
      "followerCount": 0,
      "boards": [
       ],
     "questions": [
       ],
      "inquiries": [
       ]
    }
}
```

</details>
<details>
   <summary> 팔로우(Follows) </summary>

## 팔로우 추가

---
### URL
```
[POST]/api/v1/follows
```

### Request
```
header : 
    Authorization: Bearer <Token>
Params(Query Parms)
    followedEmail(key) : 팔로우 추가할 email(value)
```

### Response
```
body : 
{
   "code": 200,
   "message": "팔로우에 성공하였습니다.",
   "data": null
}
```

## 나를 팔로우한 사용자 목록 조회

---
### URL
```
[GET]/api/v1/follows/followers
```

### Request
```
header : 
    Authorization: Bearer <Token>
```

### Response
```
body : 
{
   "code": 200,
   "message": "팔로우 조회에 성공하였습니다.",
   "data": [
      {
         "userId": ,
         "email": ""
        }
     ]
}
```

## 팔로우한 사용자 목록 조회(내가 팔로우한 사용자들)

---
### URL
```
[GET]/api/v1/follows/followings
```

### Request
```
header : 
    Authorization: Bearer <Token>
```

### Response
```
body : 
{
   "code": 200,
   "message": "팔로우 조회에 성공하였습니다.",
   "data": [
      {
         "userId": ,
         "email": ""
        }
     ]
}
```

## 팔로우 해지

---
### URL
```
[DELETE]/api/v1/follows/{followedEmail}
```

### Request
```
header : 
    Authorization: Bearer <Token>
PathVariable :
    followedEmail : 팔로우 삭제할 email
```

### Response
```
body : 
{
   "code": 200,
   "message": "언팔로우에 성공하였습니다.",
   "data": null
}
```

</details>
<details>
   <summary> 게시글(Boards)(자유 게시판) </summary>

## 자유 게시글 등록

---
### URL
```
[POST]/api/v1/boards/bulletins
```

### Request
```
header : 
    Authorization: Bearer <Token>
body :
{
”title”:”자유 게시글 제목”,
”content”:”자유 게시글 내용”
}

```

### Response
```
header : 
    Content-Type : application/json
body : 
{
"code": 200,
"message": "일반 게시글 등록에 성공하였습니다.",
"data": {
"id": 13,
"title": "일반 게시물",
"content": "일반 게시물"
}
}
```

## 자유 게시글 전체 조회

---
### URL
```
[GET]/api/v1/boards/bulletins?page=&size=
```

### Request
```
```

### Response
```
header : 
    Content-Type : application/json
body : 
{
"code": 200,
"message": "일반 게시글 조회(전체)에 성공하였습니다.",
"data": {
"content": [
{
"id": 1,
"title": "일반 게시물",
"content": "수정된 content1"
},
{
"id": 5,
"title": "일반 게시물",
"content": "일반 게시물"
},
{
"id": 6,
"title": "일반 게시물",
"content": "일반 게시물"
},
{
"id": 7,
"title": "일반 게시물",
"content": "일반 게시물"
},
{
"id": 13,
"title": "일반 게시물",
"content": "일반 게시물"
}
],
"page": {
"size": 10,
"number": 0,
"totalElements": 12,
"totalPages": 2
}
}
}
```

## 자유 게시글 단건 조회

---
### URL
```
[GET]/api/v1/boards/bulletins/{boardsid}
```

### Request
```
PathVariable : 
    boardid = 자유 게시글 id
```

### Response
```
header : 
    Content-Type : application/json
body : 
{
"code": 200,
"message": "일반 게시글 조회(단건)에 성공하였습니다.",
"data": {
"id": 6,
"title": "일반 게시물",
"content": "일반 게시물",
"view": 2,
"response": {
"content": [],
"page": {
"size": 10,
"number": 0,
"totalElements": 0,
"totalPages": 0
}
}
}
}
```

## 자유 게시글 검색

---
### URL
```
[GET]/api/v1/boards/bulletins/search?page=&size=&title=
```

### Request
```
RequestParam : 
    title = 자유 게시글 제목
```

### Response
```
header : 
    Content-Type : application/json
body : 
{
"code": 200,
"message": "일반 게시글 검색에 성공하였습니다.",
"data": {
"content": [
{
"id": 1,
"title": "일반 게시물",
"content": "수정된 content1"
},
{
"id": 5,
"title": "일반 게시물",
"content": "일반 게시물"
},
{
"id": 6,
"title": "일반 게시물",
"content": "일반 게시물"
},
{
"id": 7,
"title": "일반 게시물",
"content": "일반 게시물"
},
{
"id": 13,
"title": "일반 게시물",
"content": "일반 게시물"
}
],
"page": {
"size": 10,
"number": 0,
"totalElements": 12,
"totalPages": 2
}
}
}
```

## 자유 게시글 수정

---
### URL
```
[PUT]/api/v1/boards/bulletins/{boardsid}
```

### Request
```
header : 
    Authorization: Bearer <Token>
PathVariable : 
    boardid = 자유 게시글 id
```

### Response
```
header : 
    Content-Type : application/json
body : 
{
"code": 200,
"message": "일반 게시글 수정에 성공하였습니다.",
"data": {
"id": 13,
"title": "일반 게시물",
"content": "수정된 content1"
}
}
```

## 자유 게시글 삭제

---
### URL
```
[DELETE]/api/v1/boards/bulletins/{boardsid}
```

### Request
```
header : 
    Authorization: Bearer <Token>
PathVariable : 
    boardid = 자유 게시글 id
```

### Response
```
header : 
    Content-Type : application/json
body : 
{
"code": 200,
"message": "일반 게시글 삭제에 성공하였습니다.",
"data": null
}
```

</details>
<details>
   <summary> 게시글(Boards)(공지) </summary>

## 공지 등록

---
### URL
```
[POST]/api/v1/boards/notices
```

### Request
```
header :
    Authorization: Bearer <Token>
body :
{
   ”title”:”공지 제목”,
   ”content”:”공지 내용”
}
```

### Response
```
header : 
    Content-Type : application/json
body : 
{
   ”code” : 200,
   ”message” : “success”,
   ”data” : {
     "id": 1
     "title": "공지사항",
     "content": "공지사항”
   }
}
```

## 공지 전체 조회

---
### URL
```
[GET]/api/v1/boards/notices?page=&size=
```

### Request
```
```

### Response
```
header : 
    Content-Type : application/json
body : 
{
   "code": 200,
   "message": "공지 조회(전체)에 성공하였습니다.",
   "data": {
     "content": [
       {
        "id": 8,
        "title": "공지사항",
        "content": "공지사항"
       },
       {
        "id": 12,
        "title": "공지사항",
        "content": "공지사항"
       }
     ],
    "page": {
      "size": 10,
      "number": 0,
      "totalElements": 12,
      "totalPages": 2
    }
   }
}
```

## 공지 단건 조회

---
### URL
```
[GET]/api/v1/boards/notices/{boardsid}
```

### Request
```
PathVariable : 
    boardid = 공지 id
```

### Response
```
header : 
    Content-Type : application/json
body : 
{
"code": 200,
"message": "공지 조회(단건)에 성공하였습니다.",
"data": {
"id": 8,
"title": "공지사항",
"content": "공지사항",
"view": 2,
"response": {
"content": [],
"page": {
"size": 10,
"number": 0,
"totalElements": 0,
"totalPages": 0
}
}
}
}
```

## 공지 검색

---
### URL
```
[GET]/api/v1/boards/notices/search?page=&size=&title=
```

### Request
```
RequestParam : 
    title = 공지 제목
```

### Response
```
header : 
    Content-Type : application/json
body : 
{
"code": 200,
"message": "공지 검색에 성공하였습니다.",
"data": {
"content": [
{
"id": 8,
"title": "공지사항",
"content": "공지사항"
},
{
"id": 12,
"title": "공지사항",
"content": "공지사항"
}
],
"page": {
"size": 10,
"number": 0,
"totalElements": 12,
"totalPages": 2
}
}
}
```

## 공지 수정

---
### URL
```
[PUT]/api/v1/boards/notices/{boardsid}
```

### Request
```
header : 
    Authorization: Bearer <Token>
PathVariable : 
    boardid = 공지 id
```

### Response
```
body : 
{
"code": 200,
"message": "공지 수정에 성공하였습니다.",
"data": {
"id": 12,
"title": "수정된 title1",
"content": "수정된 content1"
}
}
```

## 공지 삭제

---
### URL
```
[DELETE]/api/v1/boards/notices/{boardsid}
```

### Request
```
header : 
    Authorization: Bearer <Token>
PathVariable : 
    boardid = 공지 id
```

### Response
```
body : 
{
"code": 200,
"message": "공지 삭제에 성공하였습니다.",
"data": null
}
```

</details>
<details>
   <summary> 게시글(Boards)(이벤트) </summary>

## 이벤트 등록

---
### URL
```
[POST]/api/v1/boards/events
```

### Request
```
header : 
    Authorization: Bearer <Token>
```

### Response
```
header : 
    Content-Type : application/json
body : 
{
"code": 200,
"message": "이벤트 등록에 성공하였습니다.",
"data": {
"id": 15,
"title": "이벤트",
"content": "이벤트"
}
}
```

## 이벤트 전체 조회

---
### URL
```
[GET]/api/v1/boards/events?page=&size=
```

### Request
```
```

### Response
```
header : 
    Content-Type : application/json
body : 
{
"code": 200,
"message": "이벤트 조회(전체)에 성공하였습니다.",
"data": {
"content": [
{
"id": 2,
"title": "이벤트",
"content": "이벤트"
},
{
"id": 15,
"title": "이벤트",
"content": "이벤트"
}
],
"page": {
"size": 10,
"number": 0,
"totalElements": 12,
"totalPages": 2
}
}
}
```

## 이벤트 단건 조회

---
### URL
```
[GET]/api/v1/boards/events/{boardsid}
```

### Request
```
PathVariable :
    boardid = 이벤트 id
```

### Response
```
header : 
    Content-Type : application/json
body : 
{
"code": 200,
"message": "이벤트 조회(단건)에 성공하였습니다.",
"data": {
"id": 15,
"title": "이벤트",
"content": "이벤트",
"view": 0,
"response": {
"content": [],
"page": {
"size": 10,
"number": 0,
"totalElements": 0,
"totalPages": 0
}
}
}
}
```

## 이벤트 검색

---
### URL
```
[GET]/api/v1/boards/events/search?page=&size=&title=
```

### Request
```
RequestParam : 
    title = 이벤트 제목
```

### Response
```
header : 
    Content-Type : application/json
body : 
{
"code": 200,
"message": "이벤트 검색에 성공하였습니다.",
"data": {
"content": [
{
"id": 2,
"title": "이벤트",
"content": "이벤트"
},
{
"id": 15,
"title": "이벤트",
"content": "이벤트"
}
],
"page": {
"size": 10,
"number": 0,
"totalElements": 12,
"totalPages": 2
}
}
}
```

## 이벤트 수정

---
### URL
```
[PUT]/api/v1/boards/events/{boardsid}
```

### Request
```
header : 
    Authorization: Bearer <Token>
PathVariable : 
    boardid = 이벤트 id
```

### Response
```
header : 
    Content-Type : application/json
body : 
{
"code": 200,
"message": "이벤트 수정에 성공하였습니다.",
"data": {
"id": 15,
"title": "수정된 title1",
"content": "수정된 content1"
}
}
```

## 이벤트 삭제

---
### URL
```
[DELETE]/api/v1/boards/events/{boardsid}
```

### Request
```
header : 
    Authorization: Bearer <Token>
PathVariable : 
    boardid = 이벤트 id
```

### Response
```
header : 
    Content-Type : application/json
body : 
{
"code": 200,
"message": "이벤트 삭제에 성공하였습니다.",
"data": null
}
```

</details>
<details>
   <summary> 게시글(Boards)(1대1 문의) </summary>

## 문의 등록

---
### URL
```
[POST]/api/v1/boards/inquiries
```

### Request
```
header : 
    Authorization: Bearer <Token>
```

### Response
```
header : 
    Content-Type : application/json
body : 
{
"code": 200,
"message": "문의 등록에 성공하였습니다.",
"data": {
"id": 14,
"title": "문의2",
"content": "문의2"
}
}
```

## 내가 등록한 문의 전체 조회

---
### URL
```
[GET]/api/v1/boards/inquiries?page=&size=
```

### Request
```
header : 
    Authorization: Bearer <Token>
```

### Response
```
header : 
    Content-Type : application/json
body : 
{
"code": 200,
"message": "문의 조회(전체)에 성공하였습니다.",
"data": {
"content": [
{
"id": 3,
"title": "문의2",
"content": "문의2"
},
{
"id": 4,
"title": "문의2",
"content": "문의2"
},
{
"id": 9,
"title": "문의2",
"content": "문의2"
},
{
"id": 10,
"title": "문의2",
"content": "문의2"
},
{
"id": 14,
"title": "문의2",
"content": "문의2"
}
],
"page": {
"size": 10,
"number": 0,
"totalElements": 12,
"totalPages": 2
}
}
}
```

## 내가 등록한 문의 단건 조회

---
### URL
```
[GET]/api/v1/boards/inquiries/{boardsid}
```

### Request
```
header : 
    Authorization: Bearer <Token>
PathVariable : 
    boardid = 문의 id
```

### Response
```
header : 
    Content-Type : application/json
body : 
{
"code": 200,
"message": "문의 조회(단건)에 성공하였습니다.",
"data": {
"id": 14,
"title": "문의2",
"content": "문의2",
"view": 1,
"response": {
"content": [],
"page": {
"size": 10,
"number": 0,
"totalElements": 0,
"totalPages": 0
}
}
}
}
```

## 내가 작성한 문의 검색

---
### URL
```
[GET]/api/v1/boards/inquiries/search?page=&size=&title=
```

### Request
```
header : 
    Authorization: Bearer <Token>
RequestParam : 
    title = 문의 제목
```

### Response
```
header : 
    Content-Type : application/json
body : 
{
"code": 200,
"message": "문의 검색에 성공하였습니다.",
"data": {
"content": [
{
"id": 3,
"title": "문의2",
"content": "문의2"
},
{
"id": 4,
"title": "문의2",
"content": "문의2"
},
{
"id": 9,
"title": "문의2",
"content": "문의2"
},
{
"id": 10,
"title": "문의2",
"content": "문의2"
},
{
"id": 14,
"title": "문의2",
"content": "문의2"
}
],
"page": {
"size": 10,
"number": 0,
"totalElements": 12,
"totalPages": 2
}
}
}
```

## 등록한 문의 수정

---
### URL
```
[PUT]/api/v1/boards/inquiries/{boardsid}
```

### Request
```
header : 
    Authorization: Bearer <Token>
PathVariable : 
    boardid = 문의 id
```

### Response
```
header : 
    Content-Type : application/json
body : 
{
"code": 200,
"message": "문의 수정에 성공하였습니다.",
"data": {
"id": 14,
"title": "수정된 title1",
"content": "수정된 content1"
}
}
```

## 문의 삭제

---
### URL
```
[DELETE]/api/v1/boards/inquiries/{boardsid}
```

### Request
```
header : 
    Authorization: Bearer <Token>
PathVariable : 
    boardid = 문의 id
```

### Response
```
header : 
    Content-Type : application/json
body : 
{
"code": 200,
"message": "문의 삭제에 성공하였습니다.",
"data": null
}
```

</details>
<details>
   <summary> 신고(Reports) </summary>

## 신고하기

---
### URL
```
[POST]/api/v1/reports
```

### Request
```
header : 
    Authorization: Bearer <Token>
```

### Response
```
header : 
    Content-Type : application/json
body : 
{
    ”code” : 200,
    ”message” : “success”,
    ”data” : {}
}
```

## 신고 결과 결정

---
### URL
```
[PUT]/api/v1/reports/{reportid}
```

### Request
```
header : 
    Authorization: Bearer <Token>
PathVariable : 
    reportid = 신고 id
```

### Response
```
header : 
    Content-Type : application/json
body : 
{
    ”code” : 200,
    ”message” : “success”,
    ”data” : {}
}
```

## 신고 내역 전체 조회

---
### URL
```
[GET]/api/v1/reports
```

### Request
```
header : 
    Authorization: Bearer <Token>
body :
{
    “judgement”:””
}
```

### Response
```
header : 
    Content-Type : application/json
body : 
{
    ”code” : 200,
    ”message” : “success”,
    ”data” : {}
}
```

## 신고 내역 검색

---
### URL
```
[GET]/api/v1/reports?name=
```

### Request
```
header : 
    Authorization: Bearer <Token>
RequestParam : 
    name = 신고대상자 이름
```

### Response
```
header : 
    Content-Type : application/json
body : 
{
”code” : 200,
”message” : “success”,
”data” : {“id”:”신고 id”,
“content”:”문의 제목”}
}
```

</details>
<details>
   <summary> 질문(Questions) </summary>

## 질문 생성

---
### URL
```
[POST]api/v1/questions
```

### Request
```
header : 
    Authorization: Bearer <Token>
body :
{
“title”: “질문 제목”,
“content”: “질문 본문”,
“first_category”: “대분류 카테고리”,
“second_category”: “중분류 카테고리”,
“last_category”: “소분류 카테고리”,
”tag”: “태그명”,
“status”: “채택 여부”
}
```

### Response
```
header : 
    Content-Type : application/json
body : 
{
“code”: 200,
“message”: “질문 등록이 완료되었습니다.”,
“data”: {
“id”: 질문ID
“title”: “질문 제목”,
“content”: “질문 본문”,
“first_category”: “대분류 카테고리”,
“second_category”: “중분류 카테고리”,
“last_category”: “소분류 카테고리”,
”tag”: “태그명”,
“status”: “채택 여부”
}
}
```

## 질문 수정

---
### URL
```
[UPDATE]api/v1/questions/{question_id}
```

### Request
```
header : 
    Authorization: Bearer <Token>
body :
{
“title”: “질문 제목”,
“content”: “질문 본문”,
“first_category”: “대분류 카테고리”,
“second_category”: “중분류 카테고리”,
“last_category”: “소분류 카테고리”,
”tag”: “태그명”,
“status”: “채택 여부”
}
```

### Response
```
header : 
    Content-Type : application/json
body : 
{
“code”: 200,
“message”: “질문 수정이 완료되었습니다.”,
“data”: {
“id”: 질문ID
“title”: “수정된 질문 제목”,
“content”: “수정된 질문 본문”,
“first_category”: “수정된 대분류 카테고리”,
“second_category”: “수정된 중분류 카테고리”,
“last_category”: “수정된 소분류 카테고리”,
”tag”: “수정된 태그명”,
“status”: “수정된 채택 여부”
}
}
}
```

## 질문 다건 조회

---
### URL
```
[GET]api/v1/questions
```

### Request
```
```

### Response
```
header : 
    Content-Type : application/json
body : 
{
“code”: 200,
“message”: “질문 조회가 완료되었습니다.”,
“data”: [
{
“id”: 질문ID
“title”: “질문 제목”,
“content”: “질문 본문”,
“first_category”: “대분류 카테고리”,
“second_category”: “중분류 카테고리”,
“last_category”: “소분류 카테고리”,
”tag”: “태그명”,
“status”: “채택 여부”
},
{
“id”: 질문ID
“title”: “질문 제목”,
“content”: “질문 본문”,
“first_category”: “대분류 카테고리”,
“second_category”: “중분류 카테고리”,
“last_category”: “소분류 카테고리”,
”tag”: “태그명”,
“status”: “채택 여부”
}
]
}
```

## 질문 단건 조회

---
### URL
```
[GET]api/v1/questions/{question_id}
```

### Request
```
```

### Response
```
header : 
    Content-Type : application/json
body : 
{
“code”: 200,
“message”: “질문 단건 조회가 완료되었습니다.”,
“data”: [
{
“id”: 질문ID
“title”: “질문 제목”,
“content”: “질문 본문”,
“first_category”: “대분류 카테고리”,
“second_category”: “중분류 카테고리”,
“last_category”: “소분류 카테고리”,
“status”: “채택 여부”
},
{
“id”: 질문ID
“title”: “질문 제목”,
“content”: “질문 본문”,
“first_category”: “대분류 카테고리”,
“second_category”: “중분류 카테고리”,
“last_category”: “소분류 카테고리”,
“status”: “채택 여부”,
“answer”: {
“id”: 답변ID,
“content”: “답변 본문”,
“created_at”: “2024-11-04”,
“update_at”: “2024-11-04”
}
}
]
}
```

## 질문 삭제

---
### URL
```
[DELETE]api/v1/questions/{question_id}
```

### Request
```
header : 
    Authorization: Bearer <Token>
```

### Response
```
header :
    Content-Type : application/json
body : 
{
“code”: 200,
“message”: “질문 삭제가 완료되었습니다.”
}
```

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
header : 
    Authorization: BearerToken
```

### Response
```
header :
    Content-Type : application/json
body :
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
header :
    Content-Type : application/json
body :
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
header : 
    Authorization: Bearer <JWT Token>
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
header :
    Content-Type : application/json
body :
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
header : 
    Authorization: Bearer <JWT Token>
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
header :
    Content-Type : application/json
body :
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
header :
    Authorization: Bearer <JWT Token>

```

### Response
```
header :
    Content-Type : application/json
body :
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
header :
    Content-Type : application/json
body :
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
header :
    Content-Type : application/json
body :
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
header : 
    Authorization: Bearer <JWT Token>
```

### Response
```
header :
    Content-Type : application/json
body :
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
header : 
    Authorization: Bearer <JWT Token>
body : 
{
    "targetId": 1,
    "target" : "OUTSOURCING"
}
```

### Response
```
header :
    Content-Type : application/json
body :
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
header : 
    Authorization: Bearer <JWT Token>
```

### Response
```
header :
    Content-Type : application/json
body :
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
header :
    Content-Type : application/json
body :
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
header : 
    Authorization: Bearer <JWT Token>
body : 
{
  “portfolioId” : 1,
  “outsourcingId” : 1
}
```

### Response
```
header :
    Content-Type : application/json
body :
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
header : 
    Authorization: Bearer <JWT Token>
body : 
{
  "status": "NO"
}
```

### Response
```
header :
    Content-Type : application/json
body :
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
header : 
    Authorization: Bearer <JWT Token>
```

### Response
```
header :
    Content-Type : application/json
body :
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
header : 
    Authorization: Bearer <JWT Token>
```

### Response
```
header :
    Content-Type : application/json
body :
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
header : 
    Authorization: Bearer <JWT Token>
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
header :
    Content-Type : application/json
body :
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
header : 
    Authorization: Bearer <JWT Token>
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
header :
    Content-Type : application/json
body :
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
header :
    Authorization: Bearer <JWT Token>
```

### Response
```
header :
    Content-Type : application/json
body :
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
header :
    Content-Type : application/json
body :
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
header :
    Content-Type : application/json
body :
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
header : 
    Authorization: Bearer <JWT Token>
```

### Response
```
header :
    Content-Type : application/json
body :
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
header : 
    Authorization: Bearer <JWT Token>
```

### Response
```
header :
    Content-Type : application/json
body :
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
header : 
    Authorization: Bearer <JWT Token>
```

### Response
```
header :
    Content-Type : application/json
body :
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
header : 
    Authorization: Bearer <JWT Token>
```

### Response
```
header :
    Content-Type : application/json
body :
{
    "code": 200,
    "message": "파일 작업요청에 성공 하였습니다",
    "data": null
}
```
</details>