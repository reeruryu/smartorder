# 🥤 smartorder

스마트 오더 (간편 주문) 시스템
<br/>
<br/>

## ✅ 프로젝트 기획 배경

`카페를 자주가는 고객으로서 가장 많이 사용하는 서비스는 ‘스타벅스 사이렌 오더’와 같은 스마트 오더 서비스입니다.
이 외에도 일상에서 다양한 스마트 오더 서비스를 통해 쉽고 빠르게 주문을 할 수 있습니다. 이러한 경험을 바탕으로
고객의 입장은 물론 점주 입장에서도 이용하는 데에 편리한 서비스를 고안해 보고자 프로젝트 기획을 하게 되었습니다.`
<br/>
<br/>

## ✅ 프로젝트 기획 목적
- 대량의 데이터를 자주 조회할 때 캐시를 통해 응답 속도를 향상시킬 수 있는 방법과, 시스템 대용량의 일괄 처리를 할 때 
Spring Batch를 통해 운영이 간편하고 수동으로 처리하지 않는 방법을 학습해 보려 합니다.
- 결제 프로세스가 있는 서비스에서 어떻게 테이블 구성하면 좋을지 생각하고, mocking하여 실제 결제를 구현해 보려 합니다.
<br/>

## ✔ 활용 기술

- Spring boot 2.7.6 (JDK1.8)
- Spring Security
- JSON Web Token
- Spring Batch
- MariaDB
- Redis
- JPA
- MyBatis
- Gradle
- Junit5
- Mockito
<br/>

## ✔ ERD
![image](https://user-images.githubusercontent.com/87798704/213650379-31bf4a69-07b2-4430-9cbf-5a4e268d6d35.png)
<br/>
<br/>

## 🌟 API

### ▪ Auth
1. 회원가입
2. 로그인
3. 이메일 인증
<br/>

### ▪ Admin
1. 카테고리 관리
 >- 카테고리 등록, 조회, 수정, 삭제
2. 메뉴 관리
 >- 메뉴 등록, 조회, 수정, 삭제
3. 회원 관리
 >- 회원 조회
 >- 회원 권한 변경 
4. 매장 관리
 >- 매장 등록, 조회, 수정, 삭제
 <br/>

### ▪ Smartorder
1. 점주 - 매장 영업 관리
 >- 매장 영업 요일, 시간, 임시 중지 변경
2. 점주 - 매장 메뉴 관리
 >- 매장 메뉴 품절, 숨김 여부 지정
 >- 매장 조회
3. 고객 - 위치
 >- 현재 위치 등록 및 변경
 >- 현재 위치 2km 반경 내 가까운 매장 10곳을 조회
4. 고객 -장바구니
 >- 메뉴 담기 및 추가, 수량 변경, 삭제
5. 점주, 고객 - 주문, 주문 내역
 >- 주문 내역 기간 별 조회
 >- 주문 취소
 <br/>
 
### ▪ SmartPay
> Under development
