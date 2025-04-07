# Festimate ✨
![image](https://github.com/user-attachments/assets/baf7e6f4-741c-43d6-a26e-538143d613fa)

**축제 매칭 플랫폼, Festimate!**
![image](https://github.com/user-attachments/assets/d6d485f5-b744-4f91-bbeb-d9d556b26b6b)
![image](https://github.com/user-attachments/assets/763c190d-a14b-49fb-8c95-90eebd8541f6)

**페스티메이트**는 페스티벌에서 이성과의 네트워킹을 지원하는 **맞춤형 매칭 서비스**입니다.<br>간단한 **페스티벌** **취향 테스트**를 통해 **나의** **페스티벌 유형**을 분석받고<br>나와 잘 맞는 유형의 **페스티벌 메이트를** **매칭**해줍니다.
> 질문을 통한 성향 분석을 진행하여 페스티벌 참여자들의 매칭을 진행하는 기능을 제공합니다.<br>운영자는 어드민 페이지를 통해 유저 정보 및 포인트를 실시간으로 확인하고 관리할 수 있습니다.


<br><br>
## 🌟 Contributors

| 이름 | 역할 | 주요 담당 API |
|------|-------|---------------|
| 이현진 | 👑 BE 리드 개발자 👑<br>Public / Private Subnet 분리 작업<br>HTTPS 설정 및 도메인 연결<br>무중단 배포를 위한 스크립트 작성 | 🧩 인증 및 회원<br>- 로그인 API / 로그아웃 API<br>- 회원가입 API<br>- 닉네임 중복확인 API<br><br>👤 유저 및 참가자<br>- 닉네임 조회 API<br>- 참가자 유형 테스트 결과 조회 API / 내 유형 조회 API<br>- 참가자 프로필 생성 API<br>- 전달할 메세지 수정 API<br><br>🎉 페스티벌<br>- 내가 참여하는 페스티벌 조회 API<br>- 페스티벌 초대코드 검증 API / 페스티벌 입장 API<br>- 매칭 추가하기 API / 매칭 리스트 조회 API<br>- 축제 이름 조회 API <br><br>💰 포인트 및 어드민 기능<br>- 페스티벌 생성 API<br>- 페스티벌 참가자 전체 조회 API<br>- 포인트 충전 API / 포인트 내역 조회 API / 특정 유저의 포인트 내역 조회 API<br>- 닉네임 + 포인트 조회 API<br>- 페스티벌 전체 조회 API|

<br>

## Architecture V1

![image](https://github.com/user-attachments/assets/f45f3a48-be5f-412e-a790-b58d91081ab2)


<br><br>
## 🧾 ERD
![festimate-erd](https://github.com/user-attachments/assets/66cb66b6-a8d0-4bbd-8fc5-def7c467e951)


## 📋 Branch Convention

- `release` : 프로덕트를 배포하는 브랜치입니다.
- `main` : 프로덕트 배포 전 기능을 개발하는 브랜치입니다.
- `feat` : 단위 기능을 개발하는 브랜치로 단위 기능 개발이 완료되면 main 브랜치에 merge 합니다.
- `fix` : 버그 수정
- `docs` :  문서 작업 진행
- `refactor` : 리팩토링
- `chore`: 위 3개 아닌것
- `init`  초기 설정 작업
- `deploy`: 배포작업 진행시
<br><br>

## 📋 Commit Convetion

- **init** : 프로젝트 초기 세팅 `[init] #1 프로젝트 초기 세팅`
- **docs** : README나 wiki 등의 문서 개정 `[docs] #14 리드미 수정`
- **feat** : 새로운 기능 구현 `[feat] #11 회원가입 API 기능 구현`
- **fix** : 코드 오류 수정 `[fix] #23 회원가입 비즈니스 로직 오류 수정`
- **refactor** : 내부 로직은 변경 하지 않고 기존의 코드를 개선하는 리팩터링 `[refactor] #15 클래스 분리`
- **chore** : 의존성 추가, yml 추가와 수정, 패키지 구조 변경, 파일 이동 등의 작업 `[chore] #30 파일명 변경`
- **test**: 테스트 코드 작성, 수정 `test: 로그인 API 테스트 코드 작성 (#20)`
<br><br>

## 📋 Git Convention

[Git 컨벤션이 궁금하다면? ✔️](https://sumptuous-viscose-f29.notion.site/Git-Convention-7ff513348d1f4ea1aeca732027ec8f12?pvs=4)


## 📋 Code Convention

[코드 컨벤션이 궁금하다면? ✔️](https://sumptuous-viscose-f29.notion.site/Code-Convention-f40b5a5de8fb497faeeac3e18768f973?pvs=4)

<br>

## 📌 프로젝트 개요

### 주요 기능

| 기능 | 설명 | 주요 특징 |
|------|------|-----------|
| 1️⃣ 온보딩 및 정보 입력 | 회원가입 후 기본 정보 및 성향 정보를 입력하는 절차 | - 학교, 이름, 닉네임, MBTI 등 입력<br>- 성별 선택 및 개인정보 설정 가능 |
| 2️⃣ 유형 테스트 | 5가지 질문을 통해 유저의 성향을 파악하여 유형 도출 | - 답변에 따라 5가지 유형 도출<br>- 결과 이미지 저장 및 공유 기능 제공 |
| 3️⃣ 페스티벌 입장 | 초대코드 입력을 통해 페스티벌 참여 | - 초대코드 검증<br>- 참여 시점에 따른 상태 분기 처리 |
| 4️⃣ 참가자 정보 등록 | 프로필 등록 및 메시지 작성 기능 | - 닉네임, 전달할 메시지, 연락 정보 입력 |
| 5️⃣ 매칭 기능 | 참가자 유형과 기준을 기반으로 자동 매칭 수행 | - 유형 기반 조건 매칭<br>- 매칭 성사 시 상대에 대한 기본 정보 제공<br>--매칭 추가할 때마다 포인트 1회 차감 |
| 6️⃣ 마이페이지 | 사용자 정보 및 페스티벌 참여 이력 확인 | - 내 프로필 / 내 유형 / 페스티벌 이력 등 확인 가능<br>- 전달할 메시지 수정 및 포인트 확인 가능 |
| 7️⃣ 포인트 시스템 | 운영자가 사용자에게 포인트를 지급하여 관리 | - 포인트 충전 및 내역 확인 API 구현<br>- 포인트 부족 시 에러 처리 및 안내 |
| 8️⃣ 어드민 페이지 | 운영자가 사용자 정보 및 포인트를 실시간으로 관리 | - 관리자 전용 로그인 및 역할 분리<br>- 사용자/포인트/페스티벌 정보 실시간 조회 및 수정 |


<br>

![image](https://github.com/user-attachments/assets/156e6299-07d6-4f7f-9218-5ec285c3251c)
![image](https://github.com/user-attachments/assets/07235a2a-e421-491a-9242-84652c13d8ed)

<br><br>

## Teck Stack ✨
<hr></hr>

| IDE | IntelliJ |
|:---|:---|
| Language | Java 21 |
| Framework | Spring Boot 3.4.1, Gradle |
| Authentication | JSON Web Tokens |
| Orm | Spring Data JPA |
| Database | MySQL |
| External | AWS EC2, AWS RDS, Nginx |
| CI/CD | Github Action |
| API Docs | Notion |
| Other Tool | Discord, Postman, Figma |

<br>

## 📂 폴더 구조

```
├── main
│   ├── java
│   │   └── org
│   │       └── festimate
│   │           └── team
│   │               ├── Point
│   │               │   └── entity
│   │               │       ├── Point.java
│   │               │       └── TransactionType.java
│   │               ├── TeamFestimateApplication.java
│   │               ├── auth
│   │               │   └── infra
│   │               │       ├── Auth.java
│   │               │       ├── KakaoOAuthClient.java
│   │               │       ├── KakaoProperties.java
│   │               │       └── service
│   │               │           └── KakaoLoginService.java
│   │               ├── common
│   │               │   ├── controller
│   │               │   │   └── HealthController.java
│   │               │   ├── entity
│   │               │   │   └── BaseTimeEntity.java
│   │               │   ├── response
│   │               │   │   ├── ApiResponse.java
│   │               │   │   ├── ResponseBuilder.java
│   │               │   │   ├── ResponseError.java
│   │               │   │   └── ResponseSuccess.java
│   │               │   └── validator
│   │               │       └── LengthValidator.java
│   │               ├── config
│   │               │   ├── RestTemplateConfig.java
│   │               │   └── SecurityConfig.java
│   │               ├── exception
│   │               │   ├── FestimateException.java
│   │               │   └── GlobalExceptionHandler.java
│   │               ├── facade
│   │               │   ├── FestivalFacade.java
│   │               │   ├── LoginFacade.java
│   │               │   └── SignUpFacade.java
│   │               ├── festival
│   │               │   ├── controller
│   │               │   │   └── FestivalController.java
│   │               │   ├── dto
│   │               │   │   ├── EntryResponse.java
│   │               │   │   ├── FestivalRequest.java
│   │               │   │   ├── FestivalResponse.java
│   │               │   │   ├── FestivalVerifyRequest.java
│   │               │   │   ├── FestivalVerifyResponse.java
│   │               │   │   ├── TypeRequest.java
│   │               │   │   └── TypeResponse.java
│   │               │   ├── entity
│   │               │   │   ├── Category.java
│   │               │   │   └── Festival.java
│   │               │   ├── repository
│   │               │   │   └── FestivalRepository.java
│   │               │   ├── service
│   │               │   │   ├── FestivalService.java
│   │               │   │   └── impl
│   │               │   │       └── FestivalServiceImpl.java
│   │               │   └── validator
│   │               │       ├── DateValidator.java
│   │               │       └── FestivalRequestValidator.java
│   │               ├── global
│   │               │   └── jwt
│   │               │       ├── JwtProperties.java
│   │               │       ├── JwtService.java
│   │               │       └── TokenResponse.java
│   │               ├── matching
│   │               │   └── entity
│   │               │       ├── Matching.java
│   │               │       └── MatchingStatus.java
│   │               ├── participant
│   │               │   ├── controller
│   │               │   │   └── ParticipantController.java
│   │               │   ├── dto
│   │               │   │   └── ProfileRequest.java
│   │               │   ├── entity
│   │               │   │   ├── Participant.java
│   │               │   │   └── TypeResult.java
│   │               │   ├── repository
│   │               │   │   └── ParticipantRepository.java
│   │               │   ├── service
│   │               │   │   ├── ParticipantService.java
│   │               │   │   └── impl
│   │               │   │       └── ParticipantServiceImpl.java
│   │               │   └── validator
│   │               └── user
│   │                   ├── config
│   │                   ├── controller
│   │                   │   └── UserController.java
│   │                   ├── dto
│   │                   │   ├── SignUpRequest.java
│   │                   │   ├── UserFestivalResponse.java
│   │                   │   └── UserNicknameResponse.java
│   │                   ├── entity
│   │                   │   ├── AppearanceType.java
│   │                   │   ├── Gender.java
│   │                   │   ├── Mbti.java
│   │                   │   ├── Platform.java
│   │                   │   └── User.java
│   │                   ├── repository
│   │                   │   └── UserRepository.java
│   │                   ├── service
│   │                   │   ├── UserService.java
│   │                   │   └── impl
│   │                   │       └── UserServiceImpl.java
│   │                   └── validator
│   │                       ├── NicknameValidator.java
│   │                       └── UserRequestValidator.java
│   └── resources
│       ├── application-prod.yml
│       ├── application.properties
│       └── application.yml
└── test
    └── java
        └── org
            └── festimate
                └── team
                    ├── TeamFestimateApplicationTests.java
                    └── user
                        └── service
                            └── impl
                                └── UserServiceImplTest.java
```



## 팀 소개
![image](https://github.com/user-attachments/assets/6cb8fc52-b037-459d-91aa-e233de98d1c1)
