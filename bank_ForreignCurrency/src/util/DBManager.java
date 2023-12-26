package util;

import java.sql.Connection;			// 1. DB 연결
import java.sql.DriverManager;		// 2. DB 관리
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBManager {
	// 필드	
	// 생성자
	// 메소드: DB관리(1.DB 연결 / 2.DB 닫기)
	// 1. DB 연결
	public static Connection getConnection() {
		Connection conn = null;
		try {
//			(1단계) JDBC 드라이버(클래스) 로드
			Class.forName("oracle.jdbc.OracleDriver");			//예외 발생 가능1
			
//			(2단계) 데이터 베이스 연결 객체 생성
			String url = "jdbc:oracle:thin:@localhost:1521:orcl";
			String uid = "user_exchanges";
			String pass = "1234";
			conn = DriverManager.getConnection(url, uid, pass);	// 예외 발생 가능 2
		} catch(Exception e) {
			System.out.println("예외 발생시 처리할 코드: DB 연결");
		}
		return conn;
	}
	
	// 2. DB 닫기(select)
	public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
		try {
			if(rs != null) {
				rs.close();
			}
			if(pstmt != null) {
				pstmt.close();
			}
			if(conn != null) {
				conn.close();
			}
		} catch(Exception e) {
			System.out.println("예외 발생시 처리할 코드: DB 닫기(select)");
		}
	}
	
	// 2. DB 닫기(insert, update, delete)
	public static void close(Connection conn, PreparedStatement pstmt) {
		try {
			if(pstmt != null) {
				pstmt.close();
			}
			if(conn != null) {
				conn.close();
			}
		} catch(Exception e) {
			System.out.println("예외 발생시 처리할 코드: DB 닫기(insert, update, delete)");
		}
	}

}
