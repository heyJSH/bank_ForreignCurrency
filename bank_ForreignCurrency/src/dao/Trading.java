package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

import dto.ExchangesVo;
import dto.TradingVo;
import util.DBManager;

//라이브러리 클래스 => OperatingSystemExchangesProject에서 실행
//2. 거래 화면
public class Trading {
	// 필드(속성)
	public String select = null;
	public String tradingMethod = null;
	public String fromTradingCurrency;	// 입력값에 따른 기준 화폐단위
	public String toTradingCurrency;	// 입력값에 따른 변환되는 화폐단위
	
	// 메소드(기능)
	// String null값이면 0으로 return하는 메소드
	private static double parseStringToDouble(String value, double defaultValue) {
		// String value: double로 반환할 문자열, double defaultValue: value가 null이거나 비어있으면 반환할 기본값
		return value == null || value.isEmpty() ? defaultValue : Double.parseDouble(value);
	}
	
	// 2. 거래 시작 화면 조회
//	유스케이스 ID: UC-U02
	public void startTrading(Scanner sc) {
		select = null;
		SelectInfo selectInfo = new SelectInfo();	// 조회 클래스 객체 생성
		MainMenu mainMenu = new MainMenu();			// 메인 메뉴 객체 생성
		
		System.out.println("===============================================================");
		amount();	// 보유 달러 및 원화 조회
		System.out.println("===============================================================");
		System.out.println(" [거래 선택]");
		System.out.println(" 원화는 메뉴의 번호를 입력해 주세요.");
		System.out.println("| 1. 금액 충전 | 2. 원화/달러 거래 | 3. 메인 메뉴로 돌아가기 |");
		System.out.println("===============================================================");
		System.out.println(" 입력>> ");
		select = sc.nextLine();	// 입력값 할당
		switch(select) {
			case "1":		// 금액 충전
				System.out.println(" \n<<거래를 위한 금액 충전을 선택하셨습니다.>>");
				chargeAmount(sc);
				break;
			case "2":		// 원화/달러 거래
				System.out.println(" \n<<원화/달러 거래를 선택하셨습니다.>>");
				// 함수 삽입할 것 ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
				break;
			case "3":		// 메인 메뉴로 돌아가기
				System.out.println(" \n<<메인 메뉴를 선택하셨습니다.>>");
				mainMenu.mainMenu(sc);
				break;
			default:	// 잘못 입력한 경우
				mainMenu.wrong(sc);
				startTrading(sc);
				break;
		}
	}

	//	[[보유 금액 조회]] : 보유 달러, 원화 조회 => [보유 금액(USD, KRW)] 동시 조회
	public static TradingVo amount() {
		String sql = "SELECT SUM(amount_krw) AS amount_krw, SUM(amount_usd) AS amount_usd\r\n"
				+ "    FROM trading_income";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		
		TradingVo amount = new TradingVo();
		
		try {
			conn = DBManager.getConnection();		// DB 연결
			pstmt = conn.prepareStatement(sql);		// 쿼리문 실행
			rs = pstmt.executeQuery();				// 쿼리문 결과 처리
			
			// 보유한 달러/원화가 null이면 0.0으로 표시, 아니면 그대로 조회
			while(rs.next()) {
				System.out.println(" [보유 금액]");
				if (rs.getDouble("amount_usd") == 0) {
					System.out.println(" 보유한 달러(USD): \t" + "0.0" + "\t(달러)");
				} else {
					System.out.println(" 보유한 달러(USD): \t" + rs.getDouble("amount_usd") + "\t(달러)");
				}
				if (rs.getDouble("amount_krw") == 0) {
					System.out.println(" 보유한 원화(KRW): \t" + 0.0 + "\t(원)");
				} else {
					System.out.println(" 보유한 원화(KRW): \t" + rs.getDouble("amount_krw") + "\t(원)");
				}
				
			}

		} catch(Exception e) {
			System.out.println("예외 발생시 처리할 코드: 쿼리문 조회(보유 금액)");
		}
		DBManager.close(conn, pstmt, rs);	// DB 닫기
		return amount;
	}
	
	//	[[보유 원화(KRW) 조회]] => [원화 충전]에 사용
	public TradingVo amountKRW() {
		String sql = "SELECT SUM(amount_krw) AS amount_krw\r\n"
				+ "    FROM trading_income";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		
		TradingVo amountKRW = new TradingVo();	// TradingVo 클래스 객체 생성
		double amount_krw = 0;
		
		try {
			conn = DBManager.getConnection();		// DB 연결
			pstmt = conn.prepareStatement(sql);		// 쿼리문 실행
			rs = pstmt.executeQuery();				// 쿼리문 결과 처리
			
			// 보유한 원화가 null이면 0.0으로 표시, 아니면 그대로 조회
			while(rs.next()) {
				if (rs.getDouble("amount_krw") == 0) {
					System.out.println(0.0);
				} else {
					System.out.println(rs.getDouble("amount_krw"));
				}
				
			}

		} catch(Exception e) {
			System.out.println("예외 발생시 처리할 코드: 쿼리문 조회(보유 달러)");
		}
		DBManager.close(conn, pstmt, rs);	// DB 닫기
		return amountKRW;
	}
	
	// 2-1. 거래 - 금액 충전
//	유스케이스 ID: UC-U03
	public TradingVo chargeAmount(Scanner sc) {
		select = null;
		SelectInfo selectInfo = new SelectInfo();	// 조회 클래스 객체 생성
		MainMenu mainMenu = new MainMenu();			// 메인 메뉴 클래스 객체 생성
		TradingVo trading = new TradingVo();		// TradingVo 클래스 객체 생성
		
		// 충전 금액
		double charge_krw = 0;
		System.out.println("===============================================================");
		amount();
		System.out.println("===============================================================");
		System.out.println(" [금액 충전]");
		System.out.println(" 충전할 금액(KRW, 원)을 입력하세요.");
		System.out.println(" 충전은 최소 1,000(원)부터 가능하며, 숫자만 입력 가능합니다.");
		System.out.println(" (만약, 메인 메뉴로 돌아가기를 원하면 숫자 0을 눌러주세요.)");
		System.out.println("===============================================================");
		System.out.println(" 입력>> ");
		
		// Scanner 입력값 할당
		select = sc.nextLine();
		
		// 숫자가 아닐 경우 재입력
		while(select == "^[\\D]*$") {
			mainMenu.wrong(sc);
			chargeAmount(sc);
			break;
		}
		if(select != "^[\\D]*$") {
//				charge_krw = Integer.parseInt(select);		//String을 Int로 변환하여 사용 
			charge_krw = Double.parseDouble(select);	//String을 double로 변환하여 사용
		}
		if(select.equals("0")) {
			System.out.println(" \n<<메인 메뉴로 돌아갑니다.>>");
			select = mainMenu.mainMenu(sc);
		} else if(charge_krw < 1000) {
			System.out.println(" \n<<충전은 최소 1,000(원)부터 가능합니다. 다시 입력해 주세요.>>");
			chargeAmount(sc);
		} else if(charge_krw >= 1000) {
			
			// [1]. 입력된 charge_krw가 amount_krw(trading_income 테이블)에 임시 저장
			trading.setAmount_krw(charge_krw);	// 입력값을 보관
			
			System.out.println("\n===============================================================");
//						System.out.println(" 예상 보유 금액(KRW): " + sum + "(원)");
			System.out.println(" 충전 금액(KRW): " + trading.getAmount_krw() + "(원)");
			System.out.println(" 충전하시겠습니까?");
			System.out.println("| 1. 네 | 2. 아니오 |");
			System.out.println("===============================================================");
			System.out.println(" 입력>>");
		}
		// [3]. 충전 확인 질문에 승낙하면, trading_income 테이블에 데이터 저장(insert)
		String answer = null;
		answer = sc.nextLine();
		
		switch(answer) {
			case "1":
				Connection conn = null;
				PreparedStatement pstmt = null;
				
				try {
					String sql = "INSERT INTO trading_income(trading_seq, ex_date, amount_krw) "
									+ "VALUES(trading_seq.NEXTVAL, SYSDATE, ?)";
					conn = DBManager.getConnection();
					pstmt = conn.prepareStatement(sql);
					pstmt.setDouble(1, trading.getAmount_krw());	// 1번째 물음표, 입력값
					
					pstmt.executeUpdate();	// insert 쿼리문 결과 처리
					
				} catch(Exception e) {
					System.out.println("예외 발생시 처리할 코드: 쿼리문 삽입(원화 충전)");
				}
				System.out.println(" \n충전이 완료되었습니다. ▶ 보유한 원화: " + trading.getAmount_krw() + "(원)");
				System.out.println("=================================================");
				System.out.println(" <<메인 메뉴로 돌아갑니다.>>");
				DBManager.close(conn, pstmt);
				mainMenu.mainMenu(sc);
				break;
			case "2":
				System.out.println(" <<거래 시작 화면으로 돌아갑니다.>>");
				startTrading(sc);
				break;
			default:
				mainMenu.wrong(sc);
				chargeAmount(sc);
				break;
		}
		return trading;
	}

	
	// 2-2-0. 현재(sysdate) 환율 정보가 null인지 판단 => random으로 데이터를 삽입(insert)
	// random 환율 정보 조회
//	[[랜덤 환율 조회]] : min_환율 <= random_52주 환율 <= max_환율
	public ExchangesVo randomRateInfo() {
		String sql = "SELECT A.random_rate\r\n"
				+ "    FROM (SELECT DBMS_RANDOM.VALUE( B.min_rate, B.max_rate ) random_rate\r\n"
				+ "                FROM DUAL D\r\n"
				+ "                    , (SELECT MIN(base_rate) as min_rate\r\n"
				+ "                            , MAX(base_rate) as max_rate\r\n"
				+ "                        FROM exchanges\r\n"
				+ "                            , ( SELECT TO_CHAR(SYSDATE + LEVEL - 1\r\n"
				+ "                                    , 'YYYY-MM-DD') DT\r\n"
				+ "                                    , LEVEL\r\n"
				+ "                                FROM DUAL\r\n"
				+ "                                CONNECT BY LEVEL <= (SYSDATE - (SYSDATE-365))\r\n"
				+ "                                )\r\n"
				+ "                        ) B\r\n"
				+ "            ) A";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		
		ExchangesVo radomRate = new ExchangesVo();
		
		try {
			conn = DBManager.getConnection();		// DB 연결
			pstmt = conn.prepareStatement(sql);		// 쿼리문 실행
			rs = pstmt.executeQuery();				// 쿼리문 결과 처리
			
			while(rs.next()) {
				double base_rateR = rs.getDouble(1);		// base_rate = (min_환율 <= random_52주 환율 <= max_환율)
//				double purchase_krwR = rs.getDouble(2);	// purchase_rate = random(base_rate) * 1.0175
//				double selling_krwR = rs.getDouble(3);		// selling_rate = random(base_rate) * 0.9825
				System.out.println(base_rateR);	
			}
			
		} catch(Exception e) {
			System.out.println("예외 발생시 처리할 코드: 쿼리문 조회(랜덤 환율)");
		}
		DBManager.close(conn, pstmt, rs);	// DB 닫기
		return radomRate;
	}
	
	// 환율 테이블에서 오늘 날짜 값이 있는지 조회 => 있으면 ex_date, 없으면 0으로 반환
	public ExchangesVo checkTodayDate() {
		ExchangesVo checkTodayDate = new ExchangesVo();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		double base_rateR = 0;
		double purchase_krwR = 0;
		double selling_krwR = 0;
		
		try {
			// EXCHANGES 테이블에서 현재(SYSDATE)날짜가 없으면 0값으로 조회하는 SQL
			/*
				SELECT DECODE(str1, str2, str3, str4) FROM DUAL;
				str1 = str2 이면, str3을 반환
				str1 != str2 이면, str4을 반환
			 */
			String sql = "SELECT DISTINCT DECODE(TO_CHAR(ex_date), NULL, TO_CHAR(ex_date), 0) AS today\r\n"
					+ "    FROM exchanges";
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {

				if(rs.getString(1).equals("0")) {	// sysdate의 환율 데이터가 없으면 radom환율 정보를 조회 후, 삽입
					// random의 base_rate 조회
					randomRateInfo();
					
				} else if (!rs.getString(1).equals("0")) {
					
				}
			}
			
			
		} catch (Exception e) {
			System.out.println("예외 발생시 처리할 코드: 쿼리문 조회(sysdate의 환율정보 확인)");
		}
		
		DBManager.close(conn, pstmt, rs);	// DB 닫기(select)
		return checkTodayDate;
	}
	
	// 조회한 random 환율을 insert
	public void randomExchanges() {
		ExchangesVo exchangesInfo = new ExchangesVo();
		SelectInfo selectInfo = new SelectInfo();
		String infoDate = exchangesInfo.getEx_date().toString();
		
		if(Objects.isNull(infoDate)) {
			Connection conn = null;
			PreparedStatement pstmt = null;
			
			try {
				
				String sql = "INSERT INTO exchanges VALUES (to_char(sysdate, 'yyyy-mm-dd'), 'USD', ?, ?, ?);";
				
				double randomRate = Double.parseDouble( randomRateInfo().toString() );
				
				conn = DBManager.getConnection();
				pstmt = conn.prepareStatement(sql);
				pstmt.setDouble( 1, randomRate );					// base_rateR
				pstmt.setDouble( 2, (randomRate * 1.0175) );		// purchase_rateR
				pstmt.setDouble( 3, (randomRate * 0.9825) );		// selling_rateR
				
				pstmt.executeUpdate();
				
			} catch(Exception e) {
				System.out.println("예외 발생시 처리할 코드: 쿼리문 삽입(환율 정보 random 삽입)");
			}
			DBManager.close(conn, pstmt);
		}
	}
	
	// 2-2. 거래 시작 화면 조회
//	유스케이스 ID: UC-U04
	// 2-2. 원화/달러 거래
//	유스케이스 ID: UC-U04
	public void trading(Scanner sc) {
		select = null;
		
		SelectInfo selectInfo = new SelectInfo();	// 조회 클래스 객체 생성
		MainMenu mainMenu = new MainMenu();			// 메인 메뉴 객체 생성
		TradingVo trade = new TradingVo();
		ExchangesVo exchanges = new ExchangesVo();
		
		System.out.println("==================================================");
		amount();	// 보유 달러 및 원화 조회
		System.out.println("==================================================");
		System.out.println(" [원화/달러 거래]");
		System.out.println("| 1. 매수 | 2. 매도 | 3. 메인 메뉴로 돌아가기 |");
		System.out.println("==================================================");
		
		selectInfo.exchangesInfo();		// 현재 환율 조회
		System.out.println(" 입력>> ");
		
		// 2-2-1. 거래방법(매수/매도) 선택
		select = sc.nextLine();
		switch(select) {
			case "1":
				this.tradingMethod = "매수";
				trade.setTrading_method(tradingMethod);	// 임시저장
				System.out.println("\n <<거래 방법 - " + tradingMethod + ">>");
				System.out.println("==================================================");
//				currency(sc);
				break;
			case "2":
				if(trade.getTrading_method() == null) {
					System.out.println(" 보유한 달러 기록이 없으므로");
					System.out.println(" 현재는 매수만 가능합니다.");
				}
				this.tradingMethod = "매도";
				trade.setTrading_method(tradingMethod);	// 임시저장
				System.out.println("\n <<거래 방법 - " + tradingMethod + ">>");
				System.out.println("==================================================");
//				currency(sc);
				break;
			case "3":
				System.out.println(" <<메인 메뉴로 돌아갑니다.>>");
				mainMenu.mainMenu(sc);
				break;
			default:
				mainMenu.wrong(sc);
				trading(sc);
				break;
		}
	}
	
	// 2-2-2. 거래 기준 화폐(원화/달러) 선택 <============================== 입력 통화는 달러(USD)만 가능하도록.
	public void currency(Scanner sc) {
		select = null;
		
		SelectInfo selectInfo = new SelectInfo();	// 조회 클래스 객체 생성
		MainMenu mainMenu = new MainMenu();			// 메인 메뉴 객체 생성
		TradingVo trade = new TradingVo();
		
		System.out.println("==================================================");
		amount();	// 보유 달러 및 원화 조회
		System.out.println("==================================================");
		System.out.println(" [화폐 단위 선택]");
		System.out.println(" 입력을 원하는 화폐 단위의 번호를 입력해 주세요.");
		System.out.println("| 1. 원화(KRW) | 2. 달러(USD) |");
		System.out.println("==================================================");
		System.out.println(" 입력>> ");
		
		select = sc.nextLine();
		
		switch(select) {
			case "1":	// 입력 화폐 단위: 원화(KRW)
				this.fromTradingCurrency = "원화(KRW)";
				this.toTradingCurrency = "달러(USD)";
				System.out.println("\n <<입력 화폐 단위: " + fromTradingCurrency + ">>");
				System.out.println(" " + fromTradingCurrency + " => " + toTradingCurrency);
				break;
			case "2":	// 입력 화폐 단위: 달러(USD)
				this.fromTradingCurrency = "달러(USD)";
				this.toTradingCurrency = "원화(KRW)";
				System.out.println("\n <<입력 화폐 단위: " + fromTradingCurrency + ">>");
				System.out.println(" " + fromTradingCurrency + " => " + toTradingCurrency);
				break;
			default:
				mainMenu.wrong(sc);
				trading(sc);
				break;
		}
	}
	
	// 2-2-3. 거래 금액 입력(입력 통화는 달러(USD)만 가능)
	public void inputTradingAmount(Scanner sc) {
		select = "0";
		
		System.out.println("===============================================================");
		
	}
	
	
}
