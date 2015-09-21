package simpleserver;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import simpledb.DatabaseManager;
import simpledb.QueryStrings;

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
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.getWriter().print(json);
	}
	
	private String errorMessage(String message) {
		String errorMessage = "{ \"Error\": \"" + message + "\"}";
		return errorMessage;
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
		int userId = checkCookie();
		if (userId == 0){
			responseWriter(errorMessage("Sign in please."));
			return;
		}
		
		DatabaseManager db = new DatabaseManager();
		db.setQuery(QueryStrings.getUserGetByIdQuery());       
		db.setConditions(userId);  
		responseWriter(db.select());    
	}
	private void userGetByNone() throws IOException{             //for 'user exp ranking chart'
		DatabaseManager db = new DatabaseManager();
		db.setQuery(QueryStrings.getUserGetByNoneQuery());
		db.setConditions();
		responseWriter(db.select());
	}
	
	public void userPost() throws IOException{   
		if( checkUserIdNameAlreadyExists(request.getParameter("userIdName")) ){
			responseWriter(errorMessage("That user ID already exists."));
			return;
		}
		if( !checkUserIdNameHasProferLength(request.getParameter("userIdName")) ){
			responseWriter(errorMessage("User ID has to have 4-12 length."));
			return;
		}
		if( !checkUserNickNameHasProferLength(request.getParameter("userNickname")) ){
			responseWriter(errorMessage("User Nickname has to have 4-16 length."));
			return;
		}
		if( !checkUserPasswordHasProferLength(request.getParameter("userPassword")) ){
			responseWriter(errorMessage("User Password has to have 8-20 length."));
			return;
		}
		String userCookieString = makeUserCookieString(request.getParameter("userIdName"));
		DatabaseManager db = new DatabaseManager();
		db.setQuery(QueryStrings.getUserPostQuery());
		db.setConditions(request.getParameter("userIdName"), request.getParameter("userNickname"), 
				request.getParameter("userPassword"), request.getParameter("userProfile"), 
				request.getParameter("userProfilePic"), userCookieString);
		responseWriter(db.insert());
	}
	private boolean checkUserIdNameAlreadyExists(String userIdName){
		DatabaseManager db = new DatabaseManager();
		db.setQuery("SELECT * from User WHERE UserIdName = ?");
		db.setConditions(request.getParameter("userIdName"));
		if(db.select().equalsIgnoreCase(errorMessage("no data."))){
			return false;
		}
		return true;
	}
	private boolean checkUserIdNameHasProferLength(String userIdName){
		final int minNum = 4;
		final int maxNum = 12;
		if (userIdName.length() < minNum || userIdName.length() > maxNum){
			return false;
		}
		return true;
	}
	private boolean checkUserNickNameHasProferLength(String userNickname){
		final int minNum = 4;
		final int maxNum = 16;
		if (userNickname.length() < minNum || userNickname.length() > maxNum){
			return false;
		}
		return true;
	}
	private boolean checkUserPasswordHasProferLength(String userPassword){
		final int minNum = 8;
		final int maxNum = 20;
		if (userPassword.length() < minNum || userPassword.length() > maxNum){
			return false;
		}
		return true;
	}
	private String makeUserCookieString(String userId) throws UnsupportedEncodingException{
		byte[] bytes = userId.getBytes("US-ASCII");
		int wholeNumber = 0;
		int bytesLength = bytes.length;
		Random rn = new Random();
		for (int i=0; i < bytesLength; i++){
			wholeNumber =+ (bytes[i] * ((bytesLength - i) * 1783) + 31) * rn.nextInt(756) + 257;
		}
		String cookieString = Integer.toString(wholeNumber);
		return cookieString;
	}
	
	public void userPut() throws IOException{    // update user info            TODO: query문 작성 , login cookie check
		//DatabaseManager db = new DatabaseManager();
		//db.setQuery(QueryStrings.getUserPutQuery());
		//db.setConditions(request.getParameter("userNickname"), request.getParameter("userPassword"));  // TODO  : password shouldn't be passed by PUT method.
		//responseWriter(db.update());
	}
	
	
	// : /login, logout
	public void loginPost() throws IOException{     // check if the user id matches the password. If it's true, bake cookies to browser.
		if ( checkIfIdMatchesPassword(request.getParameter("userIdName"), request.getParameter("userPassword")) ){
			try {
				bakeCookies(request.getParameter("userIdName"));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return;
		}
		responseWriter(errorMessage("User Id or Password is unvalid."));
	}
	private boolean checkIfIdMatchesPassword(String userIdName, String userPassword){
		DatabaseManager db = new DatabaseManager();
		db.setQuery("SELECT * from User WHERE UserIdName = ? AND UserPassword = ?");
		db.setConditions(request.getParameter("userIdName"), request.getParameter("userPassword"));
		if(db.select().equalsIgnoreCase(errorMessage("no data."))){
			return false;
		}
		return true;
	}
	private void bakeCookies(String userIdName) throws SQLException{
		DatabaseManager db = new DatabaseManager();
		db.setQuery("SELECT UserCookieString from User WHERE UserIdName = ?");
		db.setConditions(userIdName);
		JsonObject jo = (JsonObject)new JsonParser().parse(db.select()).getAsJsonArray().get(0);
		String userCookieString = jo.get("usercookiestring").getAsString();
		
		Cookie cookie1 = new Cookie("userId", userIdName);
		Cookie cookie2 = new Cookie("userCookieString", userCookieString);
		cookie1.setMaxAge(60*60*24); 
		cookie2.setMaxAge(60*60*24); 
		response.addCookie(cookie1);
		response.addCookie(cookie2); 
	}
	
	public void logoutGet() throws IOException{     
		Cookie cookie1 = new Cookie("userId", "");
		Cookie cookie2 = new Cookie("userCookieString", "");
		cookie1.setMaxAge(0); 
		cookie2.setMaxAge(0);
		response.addCookie(cookie1);
		response.addCookie(cookie2);
	}
	
	private int checkCookie(){
		Map<String, String> cookieMap = new HashMap<String, String>();
		Cookie[] cookies = request.getCookies();
		for(Cookie cookie : cookies){
		    cookieMap.put(cookie.getName(), cookie.getValue());
		}
		
		DatabaseManager db = new DatabaseManager();
		db.setQuery("SELECT UserId from User WHERE UserIdName = ? AND UserCookieString = ?");
		db.setConditions(cookieMap.get("userId"), cookieMap.get("userCookieString"));
		if(db.select().equalsIgnoreCase(errorMessage("no data."))){
			return 0;
		}
		
		JsonObject jo = (JsonObject)new JsonParser().parse(db.select()).getAsJsonArray().get(0);
		int userId = jo.get("userid").getAsInt();
		
		return userId;
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
		final int postingsPerPage = 25;
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
		int postingsPerPage = 25;
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
		int userId = checkCookie();
		if (userId == 0){
			responseWriter(errorMessage("Sign in please."));
			return;
		}
		
		DatabaseManager db = new DatabaseManager();
		db.setQuery(QueryStrings.getPostingPostQuery());
		db.setConditions(request.getParameter("postingSubject"), request.getParameter("postingText"), request.getParameter("postingPic"), userId);  
		// TODO: postingPic -> get url from cdn after file upload.
		responseWriter(db.insert());
	}
	
	public void postingPut() throws IOException{  // Use URL QueryString
		int userId = checkCookie();
		if (userId == 0){
			responseWriter(errorMessage("Sign in please."));
			return;
		}
		
		DatabaseManager db = new DatabaseManager();
		db.setQuery("UPDATE Posting SET PostingSubject = ? , PostingText = ? , PostingPic = ? WHERE PostingId = ? AND UserId = ?");
		db.setConditions(request.getParameter("postingSubject"), request.getParameter("postingText"), request.getParameter("postingPic"), request.getParameter("postingId"), userId);  
		// TODO: postingPic -> get url from cdn after file upload.
		responseWriter(db.update());
	}
	
	public void postingDelete() throws IOException{  
		int userId = checkCookie();
		if (userId == 0){
			responseWriter(errorMessage("Sign in please."));
			return;
		}
		
		DatabaseManager db = new DatabaseManager();
		db.setQuery("UPDATE Posting SET PostingDelete = true WHERE PostingId = ? AND UserId = ?");
		db.setConditions(request.getParameter("postingId"), userId);  
		responseWriter(db.update());

	}
	
	public void postingLikePut() throws IOException{            // TODO: (like-hate)countCheck() -> goToBestposting() or goOutOfBestposting()
		int userId = checkCookie();
		if (userId == 0){
			responseWriter(errorMessage("Sign in please."));
			return;
		}
		// checkIfAlreadyLiked();    -> TABLE Likers      : SELECT * FROM Likers WHERE PostingId = ? AND UserId = ?/userId;
			// if not
			// INSERT INTO Likers(PostingId, UserId) VALUES(?, ?/userID); 
			// UPDATE Posting SET PostingLike = PostingLike + 1 WHERE posting id = ?;
		
		// checkLikeMinusHateCount() : SELECT PostingLike - PostingHate From Posting where postingId = ?;  
	}
	
	public void postingHatePut() throws IOException{			 // TODO: (like-hate)countCheck() -> goToBestposting() or goOutOfBestposting()
		int userId = checkCookie();
		if (userId == 0){
			responseWriter(errorMessage("Sign in please."));
			return;
		}
		// checkIfAlreadyHated();    -> TABLE Haters
			// if not
			// INSERT INTO Likers(PostingId, UserId) VALUES(?, UserID); 
			// UPDATE Posting SET PostingHate = PostingLike + 1 WHERE posting id = ?
	}
	
	          
	// : /comment
	public void commentGet() throws IOException{
		DatabaseManager db = new DatabaseManager();
		db.setQuery(QueryStrings.getCommentGetQuery()); 
		db.setConditions(request.getParameter("postingId"));
		responseWriter(db.select());
	}
	
	public void commentPost() throws IOException{  
		int userId = checkCookie();
		if (userId == 0){
			responseWriter(errorMessage("Sign in please."));
			return;
		}
		System.out.println(request.getParameter("data"));
		System.out.println(request.getParameter("postingId"));
		DatabaseManager db = new DatabaseManager();
		db.setQuery(QueryStrings.getCommentPostQuery());  
		db.setConditions(request.getParameter("commentText"), request.getParameter("postingId"), userId);
		responseWriter(db.insert());
	}
	
	public void commentPut() throws IOException{ 
		int userId = checkCookie();
		if (userId == 0){
			responseWriter(errorMessage("Sign in please."));
			return;
		}
		DatabaseManager db = new DatabaseManager();
		db.setQuery("UPDATE Comment SET CommentText = ? WHERE commentId = ? AND UserId = ?");  
		db.setConditions(request.getParameter("commentText"), request.getParameter("commentId"), userId);
		responseWriter(db.update());
	}
	
	public void commentDelete() throws IOException{  
		int userId = checkCookie();
		if (userId == 0){
			responseWriter(errorMessage("Sign in please."));
			return;
		}
		
		DatabaseManager db = new DatabaseManager();
		db.setQuery("UPDATE Comment SET CommentDelete = true WHERE CommentId = ? AND UserId = ?");
		db.setConditions(request.getParameter("commentId"), userId);  
		responseWriter(db.update());
	}
	
	
	// : /userpage
	public void userpageGet() throws IOException{
		
	}
	
	public void userpagePost() throws IOException{  //check on frontend -> if (the page's userId == cookie userID) -> show 'post' button -> postingPost()
		// Do nothing -> delete after developing frontend page.
	}
	
}
