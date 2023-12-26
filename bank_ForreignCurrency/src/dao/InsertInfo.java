package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

import dto.TradingVo;
import util.DBManager;

// 라이브러리 클래스
// DB에 정보를 삽입(Insert)할 수 있는 클래스
public class InsertInfo {
	
	// 메소드
//	[[보유 원화(KRW) 저장]] 
//		=> [원화 충전]		: 입력된 값이 +로 저장
//		=> [원화/달러 거래] : 매수(달러구매)라면, -로 저장
//							: 매도(달러판매)라면, +로 저장
	public static TradingVo amount() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		TradingVo chargeKRW = new TradingVo();	// TradingVo 클래스 객체 생성
				
		try {
			String sql = "INSERT INTO trading_income(trading_seq, ex_date, amount_krw) "
							+ "VALUES(trading_seq.NEXTVAL, SYSDATE, ?)";
			
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setDouble(1, chargeKRW.getAmount_krw());
			
			pstmt.executeUpdate();
			
			
		} catch (Exception e) {
			System.out.println("예외 발생시 처리할 코드: 쿼리문 삽입(원화 충전)");
		}
		DBManager.close(conn, pstmt);
		return chargeKRW;
	}

}
