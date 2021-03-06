# PhotoShake

![alt tag](https://github.com/dohonext/PhotoShake/blob/master/Screen%20Shot%202015-09-14%20at%2001.27.45.png)

- http://54.64.239.231:8080/photoshake/ 로 접속하면 실행해 보실 수 있습니다. (개발중)   

## 사진을 업로드하고, 인기 사진을 추천해 주는 싱글 페이지 웹 서비스입니다.

### 백엔드
- Servlet&JSP를 사용해 직접 제작한 라이브러리 SimpleServer.Java 를 통해 API서버를 구현하였습니다. 

### 프론트엔드 
- JQuery 와 Handlebars(템플릿엔진) 두가지 라이브러리를 사용하여 만들었습니다.  
(이미지 배치 로직은 직접 제작한 ImageDisposer 라이브러리를 사용하였습니다.)

### 프론트엔드 디자인 
- css만을 이용해 직접 제작하였습니다.

### 서비스
- 사진을 업로드하여, 많은 추천을 받은 사진을 정렬해 보여주는 서비스입니다.

### 기능
- 이미지를 잘라내지 않고 고유의 비율을 유지하면서 여러장을 화면에 배치  
- 반응형 웹사이트로서, 페이지의 크기가 변함에 따라 유동적으로 사진을 재배치
- 화면 사이즈 (모바일 포함)에 따라 이미지들을 몇장 넣을지 감안해서 배치  
- 싱글페이지 웹앱으로, 서버에서 JSON파일을 받아서 템플릿 엔진을 통해 화면에 렌더링합니다. 

### TODO 
- 사진을 클릭하면 큰 화면으로 보여주기 (전체화면기능-그리드 유아이 적용 예정)
- 로딩된 순서 혹은 순차적으로 애니메이션 적용하기


### API 
- http://54.64.239.231:8080/photoshake/API/ 

![alt tag](https://github.com/dohonext/PhotoShake/blob/master/Screen%20Shot%202015-09-21%20at%2000.39.14.png)
