package simpleserver;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import simpledb.DatabaseManager;
import simpledb.QueryStrings;

// 노드제이에스처럼 짜보고 싶어서 일부러 하나에 모아봤음. 
// No Biz, No Dao. Only functions.
// DB Access Method returns JSON String(or Strings array).

public class MethodHandler {
	HttpServletRequest request; 
	HttpServletResponse response;

	public MethodHandler(HttpServletRequest request, HttpServletResponse response){
		this.request = request;
		this.response = response;
	}
	// [Core methods]
	private void responseWriter(String json) throws IOException{
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().print(json);
	}

	// : /user
	public void userGet() throws IOException{
		if (request.getParameter("userId") != null){
			userGetById();
		}else {
			userGetByNone();
		}
	}
	private void userGetById() throws IOException{     
		DatabaseManager db = new DatabaseManager();
		db.setQuery(QueryStrings.getUserGetByIdQuery());       
		db.setConditions(request.getParameter("userId"));  
		responseWriter(db.select());    
	}
	private void userGetByNone() throws IOException{
		DatabaseManager db = new DatabaseManager();
		db.setQuery(QueryStrings.getUserGetByNoneQuery());
		db.setConditions();
		responseWriter(db.select());
	}
	
	public void userPost() throws IOException{   // TODO: id 및 nickname 중복체크 로직 추가, 쿠키 토큰스트링 생성 및 디비 인서트 로직 추가 
		DatabaseManager db = new DatabaseManager();
		db.setQuery(QueryStrings.getUserPostQuery());
		db.setConditions(request.getParameter("userIdName"), request.getParameter("userNickname"), request.getParameter("userPassword"));
		responseWriter(db.insert());
	}
	
	public void userPut() throws IOException{    // TODO: query문 작성 
		DatabaseManager db = new DatabaseManager();
		//db.setQuery(QueryStrings.getUserPutQuery());
		db.setConditions(request.getParameter("userIdName"), request.getParameter("userNickname"), request.getParameter("userPassword"));
		responseWriter(db.update());
	}
	
	
	// : /login
	public void loginGet() throws IOException{
		
	}
	
	public void loginPost() throws IOException{
		
	}
	
	// : /bestposting
	public void bestpostingGet() throws IOException{
		if (request.getParameter("bestpostingId") != null){
			bestpostingGetById();
		}else {
			bestpostingGetByPage();
		}
	}
	private void bestpostingGetById() throws IOException {
		DatabaseManager db = new DatabaseManager();
		db.setQuery(QueryStrings.getBestpostingGetByIdQuery());
		db.setConditions(request.getParameter("bestpostingId"));
		responseWriter(db.select());
	}
	private void bestpostingGetByPage() throws IOException {
		final int postingsPerPage = 10;
		int page = 0;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page")) - 1;
		}
		DatabaseManager db = new DatabaseManager();
		db.setQuery(QueryStrings.getBestpostingGetByPageQuery());
		db.setConditions(postingsPerPage, page*postingsPerPage);
		responseWriter(db.select());
	}
	
	// : /posting
	public void postingGet() throws IOException{
		if (request.getParameter("postingId") != null){  
			postingGetById();
		}else { 
			postingGetByPage();
		}
	}	
	private void postingGetById() throws IOException{
		DatabaseManager db = new DatabaseManager();
		db.setQuery(QueryStrings.getPostingGetByIdQuery());
		db.setConditions(request.getParameter("postingId"));
		responseWriter(db.select());
	}
	private void postingGetByPage() throws IOException {
		int postingsPerPage = 10;
		int page = 0;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page")) - 1;
		}
		DatabaseManager db = new DatabaseManager();
		db.setQuery(QueryStrings.getPostingGetByPageQuery());
		db.setConditions(postingsPerPage, page*postingsPerPage);
		responseWriter(db.select());
	}
	
	public void postingPost() throws IOException{
		
	}
	
	public void postingPut() throws IOException{
		
	}
	
	public void postingDelete() throws IOException{
		
	}
	
	// : /comment
	public void commentGet() throws IOException{
		DatabaseManager db = new DatabaseManager();
		db.setQuery(QueryStrings.getCommentGetQuery());
		db.setConditions(request.getParameter("postingId"));
		responseWriter(db.select());
	}
	
	public void commentPost() throws IOException{
		
	}
	
	public void commentPut() throws IOException{
		
	}
	
	public void commentDelete() throws IOException{
		
	}
	
	// : /userpage
	public void userpageGet() throws IOException{
		
	}
	
	public void userpagePost() throws IOException{
		
	}
	
	public void javajigiGet() throws IOException{
		DatabaseManager db = new DatabaseManager();
		db.setQuery("SELECT * from User WHERE UserId = ?");
		db.setConditions(request.getParameter("userId"));
		responseWriter(db.select());
	}
}
