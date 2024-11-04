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
    Authorization: BearerToken
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