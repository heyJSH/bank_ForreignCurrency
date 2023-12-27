package main;
import java.sql.Connection;
import java.sql.PreparedStatement;	// 3. DB 쿼리문 사용
import java.sql.ResultSet;			// 4. DB 쿼리문 수행 결과
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import dao.MainMenu;
import dao.Test;
import dao.Trading;
import dto.AreaVo;
import dto.CalculateVo;
import dto.ExchangesVo;
import dto.TradingVo;
import util.DBManager;

public class OperatingSystemExchangesProject {

	public static void main(String[] args) {
			
		Scanner sc = new Scanner(System.in);	// 원하는 메뉴 선택을 위한 스캐너
		String select;		// 스캐너가 받을 입력값 선언
		String tradingMethod = null;
		
		ExchangesVo exchangesVo = new ExchangesVo();
		TradingVo tradingVo = new TradingVo();
		CalculateVo calculatevo = new CalculateVo();
		
		MainMenu main = new MainMenu();
		Trading trading = new Trading();
		
//		boolean run = true;		// 프로그램 실행: true
		// ArrayList 함수 교수님 예시 ==============================================
//		listArea();	 //main 함수에서 실행하는 코드
		
		// 1. 메인 메뉴 화면 조회 ==============================================
		// 1. 프로그램 실행 - 메인 메뉴 ★★★★★★
		select = main.mainMenu(sc);
		
		// 2. 거래 시작 화면 조회 ==============================================
//		trading.startTrading(sc);
		
		// 2-1. 금액(원화) 충전 화면 조회
//		trading.amountKRW();
//		trading.chargeAmount(sc);
		
		// 현재 환율 정보 조회(sysdate)
//		selectInfo.exchangesInfo();
		
//		if(exchangesInfo.exchangesInfo() == null) {
//			System.out.println("오늘 정보가 없다.");
//		};
		
		// 랜덤 환율 조회
//		trading.randomRateInfo();
		
		// 조회한 random 환율을 삽입
//		trading.checkTodayDate();
		
		// 2-2. 원화/달러 거래 ==============================================
//		trading.trading(sc);
		
		// 현재 달러 환율 조회 => (원화/달러 거래에 사용)
//		selectInfo.exchangesInfo();
		
		// 보유 금액(달러, 원화) 조회 => 금액충전, 원화/달러 거래, 거래 기록 조회에 사용
//		trading.amount();
		
		
		// 3. 거래 기록 조회 ==============================================
//		SelectTradingRecord tradingRecord = new SelectTradingRecord();
//		tradingRecord.runTradingRecord(sc);
		

		
		
//		==================================================================================================
//		==================================================================================================
		// exchanges 테이블 조회 (select)
//		exchangesAllInfo();
		
		// trading_income 테이블 조회 (select)
//		trading_incomeAllInfo();
		
		
		// exchanges 테이블에 삽입 (insert)
//		insertExchangesRate(1.31, 1300.25, 1270.75);
		
//		System.out.println("================================================================");
		// trading_income 테이블에 내용 삽입(insert)
		
//		insertTradingIncome("매수", "1");
		// trading_income 테이블 모든 내용 조회 (select)
//		trading_incomeAllInfo();
		
		sc.close();
	}

	
//	============================================================
	// 컬렉션(3): List컬렉션 => 배열처럼 인덱스번호로 데이터를 저장, 로드 가능
	// 관련함수:
//	1. list.add(객체값);			=> 데이터 객체 추가
//	2. list.remove(인덱스번호);		=> 데이터 객체 추가
//	3. list.get(인덱스번호);		=> 데이터 객체 로드
//	4. list.size();					=> 데이터 객체 크기 조회
	// List 컬렉션 실습 예시 ①
//	listArea();	 //main 함수에서 실행하는 코드
	static void listArea() {
//		인터페이스 변수 = 구현객체;
		List list = new ArrayList();		// ArrayList 객체 생성
		
		AreaVo aVo = null;
		for(int i=0; i<3;i++) {
			aVo = new AreaVo();
			aVo.setCode(i);				//code에 데이터 저장
			aVo.setName("영국" + i);	//name에 데이터 저장
			
			list.add(aVo);		// 1. 데이터 객체 추가
		}
//		list.remove(0);			// 2. 데이터 객체 삭제(인덱스)
//		list.remove(1);			
		
		int size = list.size();	// 4. 데이터 크기 조회
//		System.out.println(size);
		
		AreaVo aVo1 = null;
		for(int i=0; i<size;i++) {
			aVo1 = new AreaVo();
			aVo1 = (AreaVo) list.get(i);	// 3. 데이터 객체 로드
			System.out.println(aVo1.getCode());
			System.out.println(aVo1.getName());
		}
	}
	
//	============================================================
	// DB 삽입(insert) 함수
	// Exchanges 테이블에 환율 정보 insert
	static void insertExchangesRate(double base_rate, double purchase_krw, double selling_krw) {
		String sql = "INSERT INTO exchanges VALUES (sysdate, 'USD', ?, ?, ?)";
		
		PreparedStatement pstmt = null;
		Connection conn = null;
		
		try {
			conn = DBManager.getConnection();		// DB 연결
			
//			(3단계) PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);		// 쿼리문 실행
//			pstmt.setString(1, ex_date);			// 1: 첫 번째 물음표
			pstmt.setDouble(1, base_rate);			// 2: 두 번째 물음표
			pstmt.setDouble(2, purchase_krw);		// 3: 세 번째 물음표
			pstmt.setDouble(3, selling_krw);		// 4: 네 번째 물음표
			
//			(4단계) SQL문 실행 결과 처리
			pstmt.executeUpdate();			// insert/update/delete 쿼리문 결과 처리
		} catch(Exception e) {
			System.out.println("예외 발생시 처리할 코드: 쿼리문 삽입");
		}
		DBManager.close(conn, pstmt);		// DB 닫기
		
	}
	
//	============================================================
	// DB 삽입(insert) 함수
	// trading_income 테이블에 환율 정보 insert
	static void insertTradingIncome(String trading_method, String amount_usd) {
		String sql = "INSERT INTO trading_income VALUES (trading_seq.NEXTVAL, to_char(sysdate, 'YYYY-MM-DD'), ?, 1302.17 * ?, ?)";
//		INSERT INTO trading_income VALUES(trading_seq.NEXTVAL, '2023-12-20', '매수', 1302.17 * 1, 1);
		
		PreparedStatement pstmt = null;
		Connection conn = null;
		
		try {
			conn = DBManager.getConnection();		// DB 연결
			
//			(3단계) PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);		// 쿼리문 실행
//			pstmt.setString(1, ex_date);			// 1: 첫 번째 물음표
			pstmt.setString(1, trading_method);		// 2: 두번째 물음표
//			pstmt.setDouble(3, 1302.17 * amount_usd);	// 1302.17은 1달러:1302.17원이라 곱함
														// 상황따라 달라져야 함
			pstmt.setString(2, amount_usd);
			pstmt.setString(3, amount_usd);
			
			
//			(4단계) SQL문 실행 결과 처리
			pstmt.executeUpdate();			// insert/update/delete 쿼리문 결과 처리
			
		} catch(Exception e) {
			System.out.println("예외 발생시 처리할 코드: 쿼리문 삽입");
		}
		DBManager.close(conn, pstmt);		// DB 닫기
		
	}
	
	
//	============================================================
	// DB 조회(select) 함수 - exchanges 테이블 내용 조회(select)
	static void exchangesAllInfo() {
		String sql = "SELECT TO_CHAR(ex_date, 'YYYY-MM-DD') AS ex_date, currency_name"
				+ ", base_rate, purchase_krw, selling_krw\r\n"
				+ "    FROM exchanges";
		
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
				System.out.print(purchase_krw + "\n");
				
			}
		} catch(Exception e) {
			System.out.println("예외 발생시 처리할 코드: 쿼리문 조회");
		}
		DBManager.close(conn, pstmt, rs);		// DB 닫기
	}
	
//	============================================================
	// DB 조회(select) 함수 - trading_income 테이블 내용 조회(select)
	static void trading_incomeAllInfo() {
		String sql = "SELECT * FROM trading_income";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		
//		List list = new ArrayList();		// 1. ArrayList 객체 추가
		
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
				int trading_seq = rs.getInt("trading_seq");
				String ex_date = rs.getString("ex_date");
				String trading_method = rs.getString("trading_method");
				double amount_krw = rs.getDouble("amount_krw");
				double amount_usd = rs.getDouble("amount_usd");
				
				System.out.print(trading_seq + "\n");
				System.out.print(ex_date + "\n");
				System.out.print(trading_method + "\n");
				System.out.print(amount_krw + "\n");
				System.out.print(amount_usd + "\n");
				
			}
		} catch(Exception e) {
			System.out.println("예외 발생시 처리할 코드: 쿼리문 조회");
		}
		DBManager.close(conn, pstmt, rs);		// DB 닫기
		
	}
	

	
}
