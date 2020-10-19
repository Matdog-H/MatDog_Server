# 🐶 MatDog 🐶 (2020.05.31 - )

#### 분양. 유기견을 위한 강아지 종 인식 및 매칭 서비스

## 환경
![](https://img.shields.io/badge/IntelliJIDEA-2020.1.2x64-green) ![](https://img.shields.io/badge/Postman-blue) ![](https://img.shields.io/badge/MySQLNotifier-1.1.8-yellow) 

## 로고
<img width="380" alt="matdog" src="https://user-images.githubusercontent.com/43837954/90312413-40201700-df3f-11ea-9c47-f413c15f6d32.png">
<img width="380" alt="matdog" src="https://user-images.githubusercontent.com/43837954/90312416-431b0780-df3f-11ea-93c4-0e1c196b141a.png">     

## 구성도
<img width="700" alt="matdog" src="https://user-images.githubusercontent.com/57608585/93016244-bffed700-f5fa-11ea-9a52-de9049c4bdb8.PNG">

## 흐름도
<img width="400" alt="matdog" src="https://user-images.githubusercontent.com/57608585/89750088-6ee45a80-db05-11ea-974f-b1ebd14318c5.PNG">

## 알고리즘 명세서
<img width="450" alt="matdog" src="https://user-images.githubusercontent.com/57608585/92076306-51996800-edf5-11ea-89ca-ee8dfd1eafa8.PNG">

## 주요 기술
- 사용자 정보 관리 : DB에서 관리하여 로그인, 마이페이지 등 애플리케이셔늘 편리하게 이용
- 이미지 처리 : Front-end에서 받은 강아지 이미지는 딥러닝을 이용하여 강아지 인식을 하고 품종 분석 결과를 제공
- 공고 등록 : 보호소 관련 공고, 임시 보호 관련, 실종 관련 세 가지 케이스로 분류하여 테이블에 저장하여 관리
- API 사용 : 공공데이터에서 제공하는 API를 매 시간마다 호출하여 DB에 저장한 뒤 전달

## 주요 기능
|             Function             |                         Description                          |
| :------------------------------: | :----------------------------------------------------------: |
|         ![](https://img.shields.io/badge/로그인,회원가입-skyblue)         | -아이디와 비밀번호를 입력하여 로그인을 한다.</br>-아이디, 비밀번호, 이름, 전화번호, 거주지를 모두 입력한다.</br>-번호, 이메일, DM 중 연락 방법을 선택한다.|
| ![](https://img.shields.io/badge/메인화면-skyblue) | 	- <강아지 인식하기>, <분양 공고 보기>, <유기견 찾기>, <마이페이지>의 메뉴를 볼 수 있다. |
|          ![](https://img.shields.io/badge/강아지인식하기-skyblue)          | -카메라로 강아지를 인식하면 어떤 종과 몇퍼센트 인식하는지 알 수 있다.</br>-<추천 공고 보기>, <공고 등록하기> 메뉴로 넘어갈 수 있다. |
|     ![](https://img.shields.io/badge/분양공고보기-skyblue)     | 	-인식된 종을 바탕으로 추천 리스트를 띄워준다.</br>-정렬 기능과 검색 기능이 존재한다. |
|       ![](https://img.shields.io/badge/공고상세화면-skyblue)       |  -해당 공고의 상세 내용을 볼 수 있다.</br>-공고 저장 버튼(좋아요)가 존재한다.</br>-분양 받고 싶다는 버튼을 눌렀을 때 연락 방법을 보여준다.  |
|         ![](https://img.shields.io/badge/공고등록-skyblue)         | 	-카메라에서 최대 3장까지 사진을 가져와 등록할 수 있다.</br>-강아지 종 인식을 통해 자동으로 가져와 종을 등록하거나 변경할 수 있다.</br>-연락방법을 수정할 수 있다.</br>-버튼 클릭시, 공고를 등록할 수 있다. |
|    ![](https://img.shields.io/badge/유기견찾기(강아지,주인찾기)-skyblue)     | -카메라로 강아지를 인식하면 위 기능과 다르게 <주인을 찾아주세요>, <강아지를 찾아주세요> 버튼을 볼 수 있다.</br>-그 이후에는 분양 공고 보기와 공고 상세화면과 같다. |
|     ![](https://img.shields.io/badge/마이페이지-skyblue)     |	-사용자 프로필을 수정할 수 있는 버튼이 존재한다.</br>-나의 공고 저장 리스트를 볼 수 있고, 내가 쓴 공고들을 확인할 수 있다. |


#### Member

- [김민진](https://github.com/kim003512)
- [김유빈](https://github.com/luwbe1)

