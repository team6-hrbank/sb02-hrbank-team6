# HR-BANK (Backend)

<br />

<div align="center">
  
<img width="350" alt="아이콘" src="https://github.com/user-attachments/assets/d2034f69-e442-4d44-8d77-131611c2becc" />

<h3>기업의 인적 자원을 안전하게 관리하는 인사 관리 시스템</h3>

</div>

<br />

<div>
<img src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java">
<img src="https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="Spring Data JPA">
<img src="https://img.shields.io/badge/Spring%20Boot%20Scheduler-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot Scheduler">
<img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL">
<img src="https://img.shields.io/badge/QuerySQL-336791?style=for-the-badge&logo=postgresql&logoColor=white" alt="QuerySQL">
<img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white" alt="Redis">
<img src="https://img.shields.io/badge/MapStruct-7D4698?style=for-the-badge&logo=java&logoColor=white" alt="MapStruct">
<img src="https://img.shields.io/badge/Swagger%20UI-85EA2D?style=for-the-badge&logo=swagger&logoColor=black" alt="Swagger UI">
<img src="https://img.shields.io/badge/Railway-0B0D0E?style=for-the-badge&logo=railway&logoColor=white" alt="Railway">
</div>

</div>

## 🔎 목차
- [프로젝트 구조](#⚙️-프로젝트-구조)
- [ERD](#💾-erd)
- [주요 기능](#⭐️-주요-기능)
- [프로젝트 결과물](#🖥️-프로젝트-결과물-시연-화면-시연-링크-배포-링크)
- [팀 구성](#👥-팀-구성)
- [최종 발표 자료](#✔️-최종-발표-자료)

<br />


## ⚙️ 프로젝트 구조

[프로젝트 계획서](https://www.notion.so/1dc6caf3785b80779f8dd1b59a0f73ca)

<img width="874" alt="프로젝트 구조1" src="https://github.com/user-attachments/assets/280283d9-149a-4ad8-aeb1-ed766664e810" />
<img width="851" alt="프로젝트 구조2" src="https://github.com/user-attachments/assets/71bf18e3-3dec-4b3f-8338-93632e9c21d8" />

<br />

## 💾 ERD
![최종ERD](https://github.com/user-attachments/assets/d2d6d77d-e896-4af0-ab0d-3313be6cd5cb)

<br />

## ⭐️ 주요 기능

### 1. 부서 관리
부서의 이름, 설명, 설립일 정보를 관리합니다. 부서 등록, 수정, 삭제, 목록 조회 기능을 제공합니다.

### 2. 직원 정보 관리
직원의 기본 정보(이름, 이메일, 부서, 직함, 입사일, 상태 등)를 등록, 수정, 삭제하고 상세 조회 및 목록 조회를 지원합니다. 사원 번호는 자동 부여되며, 프로필 이미지를 함께 관리할 수 있습니다.

### 3. 파일 관리
직원의 프로필 이미지 파일과 데이터 백업 파일을 관리합니다. 파일 업로드, 다운로드, 삭제 기능을 제공합니다.

### 4. 직원 정보 수정 이력 관리
직원 추가, 수정, 삭제 시 발생하는 이력을 기록하고 조회할 수 있습니다. 변경 상세 내용, 메모, IP 주소, 시간 등의 정보를 저장하고 관리합니다.

### 5. 데이터 백업 관리
직원 데이터 변경 시 CSV 파일로 백업하거나 실패 로그를 저장합니다. 스케줄러를 통해 주기적으로 백업을 자동 수행하고, 백업 이력을 관리합니다.

### 6. 대시보드 관리
총 직원 수, 최근 수정 이력 건수, 입사자 수, 마지막 백업 시간, 직원 분포 추이 등을 통계로 제공하여 전체 인사 현황을 한눈에 파악할 수 있습니다.

<br />

## 🖥️ 프로젝트 결과물 (시연 화면, 시연 링크, 배포 링크)
<img width="1710" alt="대시보드" src="https://github.com/user-attachments/assets/c68ea2a8-50d1-48cd-b79b-f4730c2bd4f4" />
<img width="1710" alt="부서관리" src="https://github.com/user-attachments/assets/884ad64b-3dc1-4bc7-a57d-0b255a68e3ba" />
<img width="1710" alt="직원 관리" src="https://github.com/user-attachments/assets/a402a096-bb28-4f30-b6d5-e40825decf1e" />
<img width="1710" alt="수정이력" src="https://github.com/user-attachments/assets/de435111-14ad-4bb9-a933-b2ac7c995279" />
<img width="1710" alt="데이터백업" src="https://github.com/user-attachments/assets/66fc2125-1f10-4d6f-92fa-9119734529c4" />

<br />

[시연 영상](docs/videos/6팀_hrbank_시연영상.mp4)

[배포 링크](https://sb02-hrbank-team6-production.up.railway.app)

<br />

## 👥 팀 구성

|이름|프로필|역할|
|-|-|-|
|**[공한나](https://github.com/HANNAKONG)**|![공한나_6팀](https://github.com/user-attachments/assets/fc551741-b0f1-4010-8c5c-9ec28e093bf8)|백엔드 - 수정 이력 관리 |
|**[김현호](https://github.com/smthswt)**|![김현호_6팀](https://github.com/user-attachments/assets/00663b76-9a85-43fa-a248-b455fc92bd9d) |백엔드 - 파일 관리, 데이터 백업 관리 |
|**[도효림](https://github.com/coderimspace)**| ![도효림_6팀](https://github.com/user-attachments/assets/915085af-ffa0-4ec0-929d-c71305e2006e) |백엔드 - 직원 정보 관리 |
|**[양성준](https://github.com/GogiDosirak)**|![양성준_6팀](https://github.com/user-attachments/assets/16eb9a47-0b1a-489a-b829-739d97a05571) |백엔드 - 직원 통계 조회 |
|**[최규원](https://github.com/GYUWON-CHOI)**|![최규원_6팀](https://github.com/user-attachments/assets/08a98183-e718-44fd-8692-ec24a83c0ddc) |백엔드 - 부서 정보 관리 |

<br />

## ✔ 최종 발표 자료

[최종 발표 자료](HRBANK_최종발표자료.pdf)
