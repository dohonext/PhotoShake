# PhotoShake

![alt tag](https://github.com/dohonext/PhotoShake/blob/master/Screen%20Shot%202015-09-14%20at%2001.27.45.png)

## 사진을 업로드하고, 인기 사진을 추천해 주는 싱글 페이지 웹 서비스입니다.
- 백엔드 : Servlet&JSP를 통해 직접 제작한 라이브러리 SimpleServer.Java 를 통해 구현하였습니다.
- 프론트엔드 : JQuery 와 Handlebars(템플릿엔진) 두가지 라이브러리를 사용하여 만들었습니다.
- 이미지를 잘라내지 않고 고유의 비율을 유지하면서 여러장을 화면에 배치  
- 화면 사이즈 (모바일 포함)에 따라 이미지들을 몇장 넣을지 감안해서 배치  
- 이미지 배치 로직은 직접 구현하였습니다.
- 서버에서 JSON파일을 받아서 화면에 렌더링합니다. 
- 사진을 클릭하면 큰 화면으로 보여주기 (전체화면기능)  

