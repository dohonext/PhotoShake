package simpleserver;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	// Remind that Http PUT Method should use URL QueryString
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
