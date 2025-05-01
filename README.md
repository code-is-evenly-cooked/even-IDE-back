# Web IDE 프로젝트

웹 기반 코드 에디터 플랫폼으로, 사용자들이 온라인에서 코드 작성, 실행, 실시간 협업, 채팅 기능을 제공받을 수 있는 서비스입니다.

[Even Web IDE](https://even-ide.vercel.app) <br/>

#### 관련 레포
[Frontend Repo](https://github.com/code-is-evenly-cooked/even-IDE-front) <br/>
[Code Runner Repo](https://github.com/code-is-evenly-cooked/even-IDE-coderunner) <br/>


## 주요 기능

- 회원 관리
	- 회원 가입: 이메일 및 비밀번호를 이용한 신규 회원 등록
	- 로그인: 기존 회원 인증 및 회원 전용 서비스 이용
	- 소셜 로그인: Google, Kakao 등 외부 OAuth 인증을 통한 간편 로그인
	- 비밀번호 변경: 로그인 후 비밀번호 수정 기능 제공
- 프로젝트 및 파일 관리
  - 프로젝트 CRUD: 프로젝트 생성, 조회, 수정, 삭제 기능
  - 파일 CRUD: 코드 파일 생성, 조회, 수정, 삭제 기능
- 코드 에디터
  - 코드 실행: 다양한 언어(Java, Python, JavaScript) 코드 실행 기능 - [Code Runner Repo](https://github.com/code-is-evenly-cooked/even-IDE-coderunner)
  - 실시간 코드 협업: 여러 사용자가 동시에 코드 수정 및 편집 가능
  - 코드 잠금 기능: 파일 편집 권한 제어를 위한 잠금 및 해제 기능 제공
- 채팅
  - 실시간 채팅: 프로젝트별 채팅방을 통한 실시간 커뮤니케이션
  - 채팅방 리스트: 참여 중인 채팅방 목록 조회 기능 제공
- AI 기능
  - 일반 챗봇: 코드 관련 질문 및 답변 지원
  - 힌트/리팩토링 모드: 코딩 힌트 제공 및 코드 리팩토링 지원 모드
- 메모 기능
  - 메모 작성 및 관리: 파일별 메모 작성 및 수정, 삭제 기능

## 기술 스택

| 기술 아키텍처 | 기술 스택 |
|--------------|-----------|
| <img src="https://github.com/user-attachments/assets/b674fd09-a0b5-4917-a84f-c5bc5bbd8d4f" width="650"/> | **Spring Boot** – API 서버 및 비즈니스 로직 구현<br>**Spring Security + JWT** – 인증 및 인가 처리<br>**Spring Data JPA** – ORM 기반 데이터베이스 접근<br>**Spring AI** – AI 기능<br>**STOMP** – 웹소켓 통신 (실시간)<br>**Redis** – 캐시 및 세션 관리<br>**MySQL** – 관계형 DB<br>**소셜 로그인 API** – OAuth 2.0<br>**Docker** – 컨테이너화<br>**Nginx** – 리버스 프록시, HTTPS<br>**Certbot** – SSL 인증서 자동 갱신<br>**AWS EC2** – 서버 호스팅<br>**Jenkins** – CI/CD 자동화 |

## 진행 프로세스
<img width="939" alt="image" src="https://github.com/user-attachments/assets/71d66c0d-e701-4ee1-8d0d-f0f48ac26eb0" />

## 결과물
[풀스택_12회차_1팀_결과보고서.pdf](https://github.com/user-attachments/files/19969576/_12._1._.pdf)

|로그인|회원가입|
|--|--|
|<img width="1414" alt="스크린샷 2025-04-30 오전 9 38 35" src="https://github.com/user-attachments/assets/b6eb92fd-8265-45cd-829a-561cd818d7e5" />|<img width="1414" alt="스크린샷 2025-04-30 오전 9 38 59" src="https://github.com/user-attachments/assets/8cfd3936-4ccc-4917-93f4-7c23e8fd5478" />|
|비밀번호재발급|비밀번호 재설정|
|<img width="1414" alt="image" src="https://github.com/user-attachments/assets/c89d6b71-3911-4634-a470-4c0fc4411412" /><img src="https://github.com/user-attachments/assets/b2594734-a422-4b22-9e09-ef2e8be10d44" width="150"/>|<img width="1414" alt="image" src="https://github.com/user-attachments/assets/95d492b8-d859-4b9c-b535-21649603f8cd" />|
|랜딩페이지|에디터조회|
|<img width="1414" alt="image" src="https://github.com/user-attachments/assets/e9261095-08ac-4b34-a207-fd88c9c393d8" />|<img width="1370" alt="image" src="https://github.com/user-attachments/assets/316155c4-1fd4-41ae-b212-dd2b79d83a45" />|
|프로젝트 및 파일 생성|코드 실행|
|<img width="1414" alt="스크린샷 2025-04-30 오전 9 44 13" src="https://github.com/user-attachments/assets/556e76d9-f6c0-4505-9fdf-110f521a910c" />|![image](https://github.com/user-attachments/assets/abbdd7f1-0415-43b0-b6fc-171fc542857b)|
|채팅창|AI 기능 |
|![image](https://github.com/user-attachments/assets/2420feb2-32dc-4320-9799-c8c3658c911b)|![image](https://github.com/user-attachments/assets/a0b87096-5b6e-4e5d-8087-912c92dcc6bb)|




