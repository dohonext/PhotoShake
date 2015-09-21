package simpleserver;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Let's do Java web programming like Node.js at maximum.
// 대부분의 스타트업에서 Node.js를 쓰는 것은 싱글스레드 기반 서버의 성능적 측면보다는
// 코드 생산성의 측면이 부각되는 경우가 많다.
// 그렇다면 자바로는 정말로 그런 생산성이 나오지 않는 것일까?
// Node.js 예제를 보면, todo list, simple photo uploader 등이 대부분이다.
// 그런 간단한 프로그램이라면 Java도 생산적이지 않을 리 없다.
// 단지 Enterprise급 서버를 만들기 위한 프레임웍으로 간단한 프로그램을 짜려다 보니 생산성이 낮아지는 것 뿐이다.
// 간단한 todo list를 짜면서 컨트롤러가 비즈에 비즈가 다오에, 다오가 디비에서 받아 객체에 매핑하고 다시 리턴, 리턴..
// 간단한 프로그램은 간단하게 짜자.
// 간단한 서버라면 서블릿과 재사용 가능한 DB 접근 메소드만으로 충분히 Node.js + Express처럼 빨리 코딩할 수 있다.
// 나는 이것이 진정한 Object Oriented Programming이라고 생각한다.
// 물론 여기서 Object는 객체가 아니라 '목적'이다.
// 목적 지향 프로그래밍 by Lee Doho.

@WebServlet("/API/*")
public class Router extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String rest = getRestFromUrl(request);
		callMethod("Get", rest, request, response);		
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String rest = getRestFromUrl(request);
		callMethod("Post", rest, request, response);
	}
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String rest = getRestFromUrl(request);
		callMethod("Put", rest, request, response);
	}
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String rest = getRestFromUrl(request);
		callMethod("Delete", rest, request, response);
	}
	private String getRestFromUrl(HttpServletRequest request){
		String urlInfo =(String)request.getRequestURI(); 
		return urlInfo.substring(urlInfo.lastIndexOf('/') + 1);
	}
	private void callMethod(String httpMethod, String rest, HttpServletRequest request, HttpServletResponse response) throws IOException{
		MethodHandler mh = new MethodHandler(request, response);
		Method m = null;
		try {
			m = Class.forName(MethodHandler.class.getName()).getDeclaredMethod(rest+httpMethod);
		} catch (NoSuchMethodException | SecurityException
				| ClassNotFoundException e) {
			response.getWriter().print("invalid rest : "+rest);
		}
		try {
			m.invoke(mh);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
