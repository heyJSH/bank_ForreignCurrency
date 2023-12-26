package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import dto.ExchangesVo;
import dto.TradingVo;
import util.DBManager;

// 라이브러리 클래스
// DB에서 정보를 조회(select)할 수 있는 클래스
public class SelectInfo {
	
//	[[보유 달러(USD) 조회]] => [??]에 사용
	public static TradingVo amountUSD() {
		String sql = "SELECT SUM(amount_krw) AS amount_krw, SUM(amount_usd) AS amount_usd\r\n"
				+ "    FROM trading_income";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		
		TradingVo amountUSD = new TradingVo();
		
		try {
			conn = DBManager.getConnection();		// DB 연결
			pstmt = conn.prepareStatement(sql);		// 쿼리문 실행
			rs = pstmt.executeQuery();				// 쿼리문 결과 처리
			
			// 보유한 달러가 null이면 0.0으로 표시, 아니면 그대로 조회
			while(rs.next()) {
				if (rs.getDouble("amount_usd") == 0) {
					System.out.println( 0.0 );
				} else {
					System.out.println(rs.getDouble("amount_usd"));
				}				
			}

		} catch(Exception e) {
			System.out.println("예외 발생시 처리할 코드: 쿼리문 조회(보유 금액)");
		}
		DBManager.close(conn, pstmt, rs);	// DB 닫기
		return amountUSD;
	}
	
	
//	[[Exchanges 테이블 조회(select)]] : 모든 정보 조회
	// DB 조회(select) 함수 - exchanges 테이블 내용 조회(select)
	public static void exchangesAllInfo() {
		String sql = "SELECT * FROM exchanges";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		
		try {
			conn = DBManager.getConnection();	// DB 연결
			
			
//			(3단계) PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);		// 쿼리문 실행
			/* 교수님 예시
			pstmt.setInt(1, code);			// 1: 물음표 순서, code: 물음표 값
			 */
			
			
//			(4단계) SQL문 실행 결과 처리
			rs = pstmt.executeQuery();			// 쿼리문 결과 처리
			
//			rs.next(): 다음 행(row)을 확인(반환값타입: boolean)
//			if(rs.next()) {		// if는 한번만 실행하므로 반복실행은 while
			while(rs.next()) {
				String ex_date = rs.getString("ex_date");
				String currency_name = rs.getString("currency_name");
				double base_rate = rs.getDouble("base_rate");
				double purchase_krw = rs.getDouble("purchase_krw");
				double selling_krw = rs.getDouble("purchase_krw");
				
				System.out.print(ex_date + "\t");
				System.out.print(currency_name + "\t");
				System.out.print(base_rate + "\t");
				System.out.print(purchase_krw + "\t");
				System.out.print(selling_krw + "\n");
				
			}
		} catch(Exception e) {
			System.out.println("예외 발생시 처리할 코드: 쿼리문 조회");
		}
		DBManager.close(conn, pstmt, rs);		// DB 닫기
	}
	
//	[[Exchanges 테이블 조회(select)]] : 현재(sysdate) 환율 조회
	public static ExchangesVo exchangesInfo() {
		String sql = "SELECT TO_CHAR(ex_date, 'YYYY-MM-DD') AS ex_date\r\n"
				+ "        , base_rate, purchase_krw, selling_krw\r\n"
				+ "    FROM exchanges \r\n"
				+ "    WHERE TO_CHAR(ex_date, 'YYYY-MM-DD') \r\n"
				+ "        IN TO_CHAR(SYSDATE, 'YYYY-MM-DD')";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		
		ExchangesVo todayCurrency = new ExchangesVo();;
		
		try {
			conn = DBManager.getConnection();		// DB 연결
			pstmt = conn.prepareStatement(sql);		// 쿼리문 실행
			rs = pstmt.executeQuery();				// 쿼리문 결과 처리
			
			while(rs.next()) {
//				String ex_date = rs.getString("ex_date");
//				String currency_name = rs.getString("currency_name");
//				double base_rate = rs.getDouble("base_rate");
//				double purchase_krw = rs.getDouble("purchase_krw");
//				double selling_krw = rs.getDouble("selling_krw");
				
				System.out.println(" [달러 환율]");
				System.out.println(" 날짜\t\t환율\t\t달러 살 때\t\t달러 팔 때");
				System.out.println( " " +rs.getString("ex_date") + "\t" + rs.getDouble("base_rate") + "\t\t" 
								+ rs.getDouble("purchase_krw") + "(원)\t\t" + rs.getDouble("selling_krw") + "(원)\t");
			}
			
		} catch(Exception e) {
			System.out.println("예외 발생시 처리할 코드: 쿼리문 조회(오늘 환율)");
		}
		DBManager.close(conn, pstmt, rs);	// DB 닫기
		return todayCurrency;
	}
	
	
	// 거래 기록에 필요한 변수 선언
	// DB 연동하여 조회 및 계산하여 출력 필요 ★★★★★★★★★★★★
//	double amount_usd = 0;				// 보유 달러	// DB에서 조회해야 함
//	double amount_krw = 0;				// 보유 원화	// DB에서 조회해야 함
//	double totalPurchaseAmount_KRW = 0;	// 총 매수 금액(원)		// DB에서 조회+계산해야 함
//	double totalSellAmount_KRW = 0;		// 총 매도 금액(원)		// DB에서 조회+계산해야 함
//	double totalRevenueRate = 0;		// 총 수익률(%)			// DB에서 조회+계산해야 함
//	double base_rate = 0;				// 매매 기준율(%), 현재 환율	// DB에서 조회해야 함
//	double purchase_KRW = 0;			// 달러 살때(원)				// DB에서 조회해야 함
//	double selling_KRW = 0;				// 달러 팔때(원)				// DB에서 조회해야 함
//	double fluctMin_52weeks;			// 52주 변동폭(Min, 최소값)		// DB에서 조회해야 함
//	double fluctMax_52weeks;			// 52주 변동폭(Max, 최대값)		// DB에서 조회해야 함
//	double fluctMin_today;				// 금일 변동폭(Min, 최소값)		// DB에서 조회해야 함
//	double fluctMax_today;				// 금일 변동폭(Max, 최대값)		// DB에서 조회해야 함
//	String fluctRange_52weeks;	// 52주 변동폭			// DB에서 조회해야 함
//	String fluctRange_today;		// 금일 변동폭			// DB에서 조회해야 함
	

}
