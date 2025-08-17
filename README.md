# Festimate

<img width="5760" height="3240" alt="표지" src="https://github.com/user-attachments/assets/08d91daf-2c8f-4ec4-b151-4fa92c39b381" />

[**축제 매칭 플랫폼, Festimate!**](https://festimate.kr/)

Festimate는 페스티벌에서 이성과의 네트워킹을 지원하는 **맞춤형 매칭 서비스**입니다.  
간단한 **페스티벌 취향 테스트**를 통해 나의 **페스티벌 유형**을 분석하고, 나와 잘 맞는 유형의 **페스티벌 메이트를 매칭**해줍니다.

> 질문을 통한 성향 분석을 진행하여 페스티벌 참여자들의 매칭을 진행하는 기능을 제공합니다.  
운영자는 어드민 페이지를 통해 유저 정보 및 포인트를 실시간으로 확인하고 관리할 수 있습니다.

---

## 🌟 Contributors

| 이름       | 역할             | 주요 담당 API |
|------------|------------------|---------------|
| 이현진     | 👑 **BE 리드 개발자** 👑 <br> Public / Private Subnet 분리 작업 <br> HTTPS 설정 및 도메인 연결 <br> 무중단 배포를 위한 스크립트 작성 | 🧩 **인증 및 회원** <br> - 카카오 OAuth 로그인 API / 토큰 재발급 API <br> - 회원가입 API / 닉네임 중복확인 API <br> - 닉네임 조회 API <br><br> 👤 **유저 및 참가자** <br> - 참가자 유형 테스트 API (5가지 질문 기반) <br> - 내 유형 조회 API / 참가자 프로필 생성 API <br> - 전달할 메시지 수정 API / 내가 참여하는 페스티벌 조회 API <br><br> 🎉 **페스티벌** <br> - 페스티벌 초대코드 검증 API / 페스티벌 입장 API <br> - 매칭 추가하기 API / 매칭 리스트 조회 API <br> - 매칭 상세 정보 조회 API / 축제 이름 조회 API <br><br> 💰 **포인트 및 어드민 기능** <br> - 페스티벌 생성 API / 페스티벌 전체 조회 API <br> - 페스티벌 상세 조회 API / 페스티벌 참가자 검색 API <br> - 포인트 충전/차감 API / 포인트 내역 조회 API <br> - 특정 참가자 포인트 내역 조회 API / 매칭 통계 조회 API <br> - 페스티벌 호스트 추가 API |

---
## 🏗️ Architecture Overview
![image](https://github.com/user-attachments/assets/6fb54cb2-aa60-41fd-ac98-ce0e6c26a85e)

---
## 🧾 ERD

<img width="1132" height="752" alt="festimate-erd" src="https://github.com/user-attachments/assets/a9b2f1d9-9e87-49a3-a9e0-da2ec06d4d88" />


## ⚙️ Tech Stack

| 구분 | 사용 기술 |
|------|-----------|
| **IDE** | IntelliJ IDEA |
| **Language** | Java 21 |
| **Framework** | Spring Boot 3.4.3 |
| **Build Tool** | Gradle |
| **Authentication** | OAuth 2.0 (Kakao), JWT |
| **Security** | Spring Security |
| **ORM** | Spring Data JPA + Hibernate |
| **Database** | MySQL |
| **Query DSL** | QueryDSL 5.0.0 |
| **Infra/Cloud** | AWS EC2, AWS RDS, Nginx, Route 53 |
| **CI/CD** | GitHub Actions, Docker, Blue-Green Deployment |
| **Monitoring** | AOP 기반 API 요청 로깅 |
| **Docs** | Notion (API), ERDCloud (ERD) |
| **API Test** | Postman |
| **Collaboration** | Discord, GitHub Projects, Figma |

## API 명세서

<img width="700" alt="image" src="https://github.com/user-attachments/assets/36e010bb-c5e2-416c-8053-c3515730a132" />

[API 명세서 바로가기](https://psychedelic-perigee-94e.notion.site/API-1ceaebccb8e480309a37d1ca2f466a93)

---

## 🌿 Branch Convention

- **release** : 배포 브랜치  
- **main** : 개발 브랜치 (배포 전 merge)  
- **feat** : 기능 단위 개발  
- **fix** : 버그 수정  
- **docs** : 문서 작성/수정  
- **refactor** : 리팩토링  
- **chore** : 의존성, 설정, 구조 변경  
- **init** : 초기 설정  
- **deploy** : 배포 관련  

---

## 🌿 Commit Convention

- **init** : 초기 세팅 → `[init] #1 프로젝트 초기 세팅`  
- **docs** : 문서 변경 → `[docs] #14 리드미 수정`  
- **feat** : 기능 추가 → `[feat] #11 회원가입 API 기능 구현`  
- **fix** : 버그 수정 → `[fix] #23 회원가입 로직 오류 수정`  
- **refactor** : 코드 개선 → `[refactor] #15 클래스 분리`  
- **chore** : 설정/구조 작업 → `[chore] #30 yml 파일 수정`  
- **test** : 테스트 작성 → `[test] #20 로그인 API 테스트 코드 작성`  

---

## 📋 Git Convention

[Git 컨벤션이 궁금하다면? ✔️](https://sumptuous-viscose-f29.notion.site/Git-Convention-7ff513348d1f4ea1aeca732027ec8f12?pvs=4)

---

## 📋 Code Convention

[코드 컨벤션이 궁금하다면? ✔️](https://sumptuous-viscose-f29.notion.site/Code-Convention-f40b5a5de8fb497faeeac3e18768f973?pvs=4)

---

## 📌 주요 기능

| 기능 | 설명 | 특징 |
|------|------|------|
| 1️⃣ 온보딩 & 정보 입력 | 회원가입 후 기본 정보/성향 입력 | - 학교, 이름, 닉네임, MBTI, 성별 선택 |
| 2️⃣ 유형 테스트 | 5문항으로 참가자 성향 도출 | - 5가지 유형 결과 제공 <br> - 결과 이미지 저장/공유 |
| 3️⃣ 페스티벌 입장 | 초대코드 기반 참여 | - 코드 검증, 참여 상태 관리 |
| 4️⃣ 참가자 프로필 | 프로필 등록/메시지 작성 | - 닉네임, 메시지, 연락처 입력 |
| 5️⃣ 매칭 기능 | 유형 기반 매칭 | - 조건 매칭, 포인트 차감 <br> - 상대 기본 정보 제공 |
| 6️⃣ 마이페이지 | 사용자 정보/이력 관리 | - 프로필, 유형, 참여 이력 확인 <br> - 메시지 수정, 포인트 조회 |
| 7️⃣ 포인트 시스템 | 포인트 충전/차감 관리 | - 포인트 부족 시 안내/에러 처리 |
| 8️⃣ 어드민 페이지 | 운영자 관리 기능 | - 사용자/포인트/페스티벌 실시간 관리 <br> - 관리자 전용 로그인 |

---

## 팀 소개

![image](https://github.com/user-attachments/assets/6cb8fc52-b037-459d-91aa-e233de98d1c1)
