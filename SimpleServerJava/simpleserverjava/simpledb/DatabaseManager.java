package simpledb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;

public class DatabaseManager {
	private static final String address = "jdbc:mysql://127.0.0.1:3306/photoshake";
	private static final String id = "root";
	private static final String pw = "";
	private String query = null;
	private ArrayList<Object> conditionsList = null;
	
	public void setQuery(String query){
		this.query = query;
	}
	
	@SuppressWarnings("unchecked")
	public <T> void setConditions(T...conditions){
		this.conditionsList = makeGenericArrayList(conditions);
	}
	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> makeGenericArrayList(T...values){
		ArrayList<T> array = new ArrayList<T>();
		for(T a : values){  
			  array.add(a);
		}  
		return array;
	}
	
	public String insert(){
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		int updatedRows = 0;
		String message = null;
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(address, id, pw);
			pstmt = conn.prepareStatement(query);
			int size = conditionsList.size();
			for ( int i = 0; i < size ; i++) {
				if (conditionsList.get(i) instanceof String) {                  // TODO: 타입 추가 
					pstmt.setString(i+1, (String) conditionsList.get(i));
				} else {
					pstmt.setInt(i+1, (int) conditionsList.get(i));
				}
			}		
			updatedRows = pstmt.executeUpdate();
		} catch (Exception e) {
			
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		if (updatedRows > 0) {
			message = "{ \"message\":" + "\"" + updatedRows + "data(s) applied\"";    //TODO : 메세지 더 자세하게
		}else {
			message = "{ \"errorMessag\": \"insert failed\" }";
		}
		return message;
	}
	public String select(){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String returnObject = null;
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(address, id, pw);
			pstmt = conn.prepareStatement(query);
			int size = conditionsList.size();
			
			for ( int i = 0; i < size ; i++) {
				if (conditionsList.get(i) instanceof String) {
					pstmt.setString(i+1, (String) conditionsList.get(i));
				} else {
					pstmt.setInt(i+1, (int) conditionsList.get(i));
				}
			}	
			
			rs = pstmt.executeQuery();                                     // TODO: 리턴오브젝트 [0]번에 메세지 삽입.
			if (!rs.isBeforeFirst() ) { 
				returnObject = "{ \"errorMessage\": \"no data\"}";   
			}else {
				returnObject = convertFromResultSetToJson(rs);
			}
		} catch (Exception e) {

		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return returnObject;
	}
	public String update(){
		String message = insert();
		return message;
	}
	public String delete(){
		String message = insert();
		return message;
	}
	private String convertFromResultSetToJson(ResultSet resultSet)
	        throws Exception {
		Gson gson = new Gson();
	    ArrayList<HashMap<String, Object>> resultSetHashMapArrayList = new ArrayList<HashMap<String, Object>>();
	    while (resultSet.next()) {
	        int totalRows = resultSet.getMetaData().getColumnCount();
	        HashMap<String, Object> obj = new HashMap<String, Object>();
	        for (int i = 0; i < totalRows; i++) {
	            obj.put(resultSet.getMetaData().getColumnLabel(i + 1)
	                    .toLowerCase(), resultSet.getObject(i + 1));
	        }
	        resultSetHashMapArrayList.add(obj);
	    }
	    return gson.toJson(resultSetHashMapArrayList);
	}
}

