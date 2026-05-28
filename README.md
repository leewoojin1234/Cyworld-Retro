# Cyworld Retro

2000년대 싸이월드 미니홈피 감성을 Spring Boot 기반 REST API로 구현한 SNS 프로젝트입니다.

## 1. 기술 스택

- Java 21
- Spring Boot 4.0.5
- Spring Security + JWT
- Spring Data JPA
- H2 in-memory Database
- Redis
- QueryDSL
- Docker, Docker Compose
- Gradle

## 2. 주요 기능

- 회원가입 / 로그인 / 토큰 재발급 / 로그아웃
- 회원가입 시 미니홈피 자동 생성
- 미니홈피 조회, 프로필 수정, 방문자 수 증가
- 방명록 CRUD 및 비밀글 권한 처리
- 다이어리 CRUD 및 공개/비공개 권한 처리
- QueryDSL 기반 방명록/다이어리 검색
- 일촌 신청, 수락, 거절, 목록 조회
- 도토리 충전, 상점 아이템 구매, 거래 내역 조회

## 3. 실행 방법

### Docker Compose 실행

프로젝트 루트 디렉토리에서 아래 명령어를 실행합니다.

```bash
docker compose up -d --build
```

실행 후 API 기본 주소는 다음과 같습니다.

```text
http://127.0.0.1:8080
```

Docker Compose 구성:

- `app`: Spring Boot 애플리케이션
- `redis`: Refresh Token 저장 및 Today 방문자 수 카운팅용 Redis

Redis는 healthcheck를 통해 준비 상태를 확인하고, Spring Boot 애플리케이션은 Redis가 healthy 상태가 된 뒤 실행됩니다.

### 로컬 실행

Redis를 먼저 실행합니다.

```bash
docker compose up -d redis
```

Spring Boot 애플리케이션을 실행합니다.

```bash
./gradlew bootRun
```

로컬 실행 시 접속 주소:

```text
http://127.0.0.1:8080
```

## 4. 테스트 데이터

본 프로젝트는 과제 요구사항에 맞춰 H2 in-memory DB를 사용합니다.

애플리케이션 실행 시 `CommandLineRunner` 기반의 `TestDataInitializer`가 자동으로 테스트 데이터를 생성합니다. 따라서 서버를 재시작하면 DB는 초기화되고, 동일한 테스트 데이터가 다시 생성됩니다.

### 샘플 계정

| 역할 | 이메일 | 비밀번호 | 설명 |
| --- | --- | --- | --- |
| 미니홈피 주인 | `owner@test.com` | `password123` | 기본 테스트 계정 |
| 방문자 | `writer@test.com` | `password123` | 방명록 작성 및 일촌 테스트 계정 |
| 친구 | `friend@test.com` | `password123` | 일촌 신청 대기 데이터 포함 |

### 자동 생성 데이터

- 회원 3명
- 회원별 미니홈피
- 공개/비공개 다이어리
- 공개/비밀 방명록
- 수락된 일촌 1건
- 대기 중인 일촌 신청 1건
- 도토리 충전/구매 이력

## 5. H2 Console

H2 Console 주소:

```text
http://127.0.0.1:8080/h2-console
```

Docker Compose 실행 시에도 동일하게 아래 주소를 사용합니다.

```text
http://127.0.0.1:8080/h2-console
```

접속 정보:

| 항목 | 값 |
| --- | --- |
| JDBC URL | `jdbc:h2:mem:cyworld` |
| User Name | `sa` |
| Password | 빈 값 |

## 6. Postman 테스트 방법

Postman에서 아래 순서대로 요청하면 주요 기능을 확인할 수 있습니다.

Base URL:

```text
http://127.0.0.1:8080
```

인증이 필요한 API는 로그인 응답의 `data.accessToken` 값을 아래 헤더에 넣어 요청합니다.

```http
Authorization: Bearer {accessToken}
```

### 6.1 로그인

자동 생성된 샘플 계정으로 로그인합니다.

```http
POST /api/auth/login
Content-Type: application/json
```

```json
{
  "email": "owner@test.com",
  "password": "password123"
}
```

응답 예시:

```json
{
  "success": true,
  "message": "로그인에 성공하였습니다.",
  "data": {
    "accessToken": "...",
    "refreshToken": "..."
  }
}
```

### 6.2 내 정보 및 미니홈피 확인

```http
GET /api/members/me
Authorization: Bearer {accessToken}
```

```http
GET /api/minihomes/me
Authorization: Bearer {accessToken}
```

샘플 데이터 기준 `owner@test.com`의 미니홈피 ID는 일반적으로 `1`입니다.

### 6.3 미니홈피 방문 처리

```http
POST /api/minihomes/1/visit
Authorization: Bearer {accessToken}
```

### 6.4 다이어리 목록 조회

```http
GET /api/minihomes/1/diaries?page=0&size=10
Authorization: Bearer {accessToken}
```

검색 조건 예시:

```http
GET /api/minihomes/1/diaries?keyword=기록&emotion=HAPPY&page=0&size=10
Authorization: Bearer {accessToken}
```

### 6.5 다이어리 작성

```http
POST /api/diaries
Authorization: Bearer {accessToken}
Content-Type: application/json
```

```json
{
  "title": "Postman 테스트",
  "content": "다이어리 작성 API 테스트입니다.",
  "emotion": "HAPPY",
  "publicPost": true
}
```

### 6.6 방명록 목록 조회

```http
GET /api/minihomes/1/guestbooks?page=0&size=10
Authorization: Bearer {accessToken}
```

### 6.7 방명록 작성

방문자 계정으로 로그인한 뒤 테스트하면 권한 흐름을 더 명확히 확인할 수 있습니다.

```http
POST /api/minihomes/1/guestbooks
Authorization: Bearer {writerAccessToken}
Content-Type: application/json
```

```json
{
  "content": "Postman으로 왔다 갑니다!",
  "secret": false
}
```

### 6.8 일촌 신청 및 수락

`writer@test.com`으로 로그인 후 `owner@test.com`에게 신청:

```http
POST /api/ilchons/requests
Authorization: Bearer {writerAccessToken}
Content-Type: application/json
```

```json
{
  "receiverId": 1
}
```

`owner@test.com`으로 로그인 후 받은 신청 확인:

```http
GET /api/ilchons/requests/received
Authorization: Bearer {ownerAccessToken}
```

신청 ID를 확인한 뒤 수락:

```http
PATCH /api/ilchons/requests/{ilchonId}/accept
Authorization: Bearer {ownerAccessToken}
Content-Type: application/json
```

```json
{
  "ilchonName": "베프"
}
```

### 6.9 도토리 충전

```http
POST /api/acorns/charge
Authorization: Bearer {accessToken}
Content-Type: application/json
```

```json
{
  "amount": 50
}
```

### 6.10 상점 아이템 목록 및 구매

```http
GET /api/acorns/shop/items
Authorization: Bearer {accessToken}
```

```http
POST /api/acorns/purchase
Authorization: Bearer {accessToken}
Content-Type: application/json
```

```json
{
  "itemType": "SKIN_SKY"
}
```

거래 내역 조회:

```http
GET /api/acorns/histories?page=0&size=10
Authorization: Bearer {accessToken}
```

## 7. Gradle 테스트 실행

```bash
./gradlew test
```

## 8. 종료 방법

컨테이너 종료:

```bash
docker compose down
```

H2는 in-memory DB이므로 애플리케이션 종료 시 데이터가 사라집니다. 다시 실행하면 테스트 데이터가 자동으로 재생성됩니다.
