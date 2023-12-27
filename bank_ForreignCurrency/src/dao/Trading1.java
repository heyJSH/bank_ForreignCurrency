package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Scanner;

import dto.ExchangesVo;
import dto.TradingVo;
import util.DBManager;

//라이브러리 클래스 => OperatingSystemExchangesProject에서 실행
//2. 거래 화면
public class Trading1 {
   // 필드(속성)
   public String select = null;
   public String tradingMethod;
   
   // 메소드(기능)
   // String null값이면 0으로 return하는 메소드
   private static double parseStringToDouble(String value, double defaultValue) {
      // String value: double로 반환할 문자열, double defaultValue: value가 null이거나 비어있으면 반환할 기본값
      return value == null || value.isEmpty() ? defaultValue : Double.parseDouble(value);
   }
   
   // 2. 거래 시작 화면 조회
//   유스케이스 ID: UC-U02
   public void startTrading(Scanner sc) {
      select = null;
      SelectInfo selectInfo = new SelectInfo();   // 조회 클래스 객체 생성
      MainMenu mainMenu = new MainMenu();         // 메인 메뉴 객체 생성
      
      System.out.println("===============================================================");
      amount();   // 보유 달러 및 원화 조회
      System.out.println("===============================================================");
      System.out.println(" [거래 선택]");
      System.out.println(" 원화는 메뉴의 번호를 입력해 주세요.");
      System.out.println("| 1. 금액 충전 | 2. 원화/달러 거래 | 3. 메인 메뉴로 돌아가기 |");
      System.out.println("===============================================================");
      System.out.println(" 입력>> ");
      select = sc.nextLine();   // 입력값 할당
      switch(select) {
         case "1":      // 금액 충전
            System.out.println(" \n<<거래를 위한 금액 충전을 선택하셨습니다.>>");
            chargeAmount(sc);
            break;
         case "2":      // 원화/달러 거래
            System.out.println(" \n<<원화/달러 거래를 선택하셨습니다.>>");
            trading(sc);
            break;
         case "3":      // 메인 메뉴로 돌아가기
            System.out.println(" \n<<메인 메뉴를 선택하셨습니다.>>");
            mainMenu.mainMenu(sc);
            break;
         default:   // 잘못 입력한 경우
            mainMenu.wrong(sc);
            startTrading(sc);
            break;
      }
   }

   // [[현재(sysdate) 환율 조회(select)]] : TO_CHAR(sysdate, 'YYYY-MM-DD')
   public ExchangesVo exchangesInfo() {
      String sql = "SELECT TO_CHAR(ex_date, 'YYYY-MM-DD') AS ex_date, base_rate, purchase_krw, selling_krw FROM exchanges WHERE TO_CHAR(ex_date, 'YYYY-MM-DD') IN TO_CHAR(SYSDATE, 'YYYY-MM-DD')";
      
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      Connection conn = null;
      
      ExchangesVo todayCurrency = new ExchangesVo();;
      String ex_date;
      String currency_name;
      double base_rate;
      double purchase_krw;
      double selling_krw;
      
      try {
         conn = DBManager.getConnection();      // DB 연결
         pstmt = conn.prepareStatement(sql);      // 쿼리문 실행
         rs = pstmt.executeQuery();            // 쿼리문 결과 처리
         
         while(rs.next()) {
            ex_date = rs.getString("ex_date");
            currency_name = rs.getString("currency_name");
            base_rate = rs.getDouble("base_rate");
            purchase_krw = rs.getDouble("purchase_krw");
            selling_krw = rs.getDouble("selling_krw");
            
            todayCurrency.setEx_date(ex_date);
            todayCurrency.setCurrency_name(currency_name);
            todayCurrency.setBase_rate(base_rate);
            todayCurrency.setPurchase_krw(purchase_krw);
            todayCurrency.setSelling_krw(selling_krw);
         }
         
      } catch(Exception e) {
         System.out.println("예외 발생시 처리할 코드: 쿼리문 조회(오늘 환율)");
      }
      DBManager.close(conn, pstmt, rs);   // DB 닫기(select)
      return todayCurrency;
   }
   
   
   // 환율 테이블에서 오늘 날짜 값이 있는지 조회 
   //   => 있으면 ex_date, 없으면 0으로 반환하여 데이터를 insert
   public String checkTodayData() {
      String exData = null;
//      ExchangesVo checkTodayData = new ExchangesVo();
      Connection conn = null;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      ExchangesVo eVo = null;      
      try {
         // EXCHANGES 테이블에서 현재(SYSDATE)날짜가 없으면 0값으로 조회하는 SQL
         /*
            SELECT DECODE(str1, str2, str3, str4) FROM DUAL;
            str1 = str2 이면, str3을 반환
            str1 != str2 이면, str4을 반환
          */
         String sql = "SELECT DISTINCT DECODE(TO_CHAR(ex_date), NULL, TO_CHAR(ex_date), 0) AS today FROM exchanges";
         conn = DBManager.getConnection();
         pstmt = conn.prepareStatement(sql);
         rs = pstmt.executeQuery();
         
         if(rs.next()) {
        	 exData = rs.getString("today");
         }
      } catch (Exception e) {
//         System.out.println("예외 발생시 처리할 코드: 쿼리문 조회(sysdate의 환율정보 확인)");
      }
      
      DBManager.close(conn, pstmt, rs);   // DB 닫기(select)
      return exData;
   }
   
   
   // 날짜가 없으면 사용할 환률 정보를 디비로 부터 조회하여 계산
   public ExchangesVo clacExRate(String exData) {
//      ExchangesVo checkTodayData = new ExchangesVo();
      Connection conn = null;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      ExchangesVo eVo = null;      
      try {
         // EXCHANGES 테이블에서 현재(SYSDATE)날짜가 없으면 0값으로 조회하는 SQL
         /*
            SELECT DECODE(str1, str2, str3, str4) FROM DUAL;
            str1 = str2 이면, str3을 반환
            str1 != str2 이면, str4을 반환
          */
         String sql = "select * from exchanges where ex_date=to_char(sysdate, 'yyyy-mm-dd')";
         conn = DBManager.getConnection();
         pstmt = conn.prepareStatement(sql);
         rs = pstmt.executeQuery();
         
         if(rs.next()) {
            eVo = new ExchangesVo();
            
            double base_rateR = rs.getDouble("BASE_RATE");
            double purchase_krwR = rs.getDouble("PURCHASE_KRW");
            double selling_krwR = rs.getDouble("SELLING_KRW");
            
            
//            double base_rateR = eVo.getBase_rate();         // DB의 base_rate에 random 값 저장
//            double purchase_krwR = base_rateR * 1.0175;      // purchase_krw = random(base_rate) * 1.0175
//            double selling_krwR = base_rateR * 0.9825;      // selling_krw = random(base_rate) * 0.9825
               
            eVo.setPurchase_krw(purchase_krwR);   // DB의 purchase_rate에 값 저장
            eVo.setSelling_krw(selling_krwR);    // DB의 selling_rate에 값 저장
            // random 환율정보 insert
         }
      } catch (Exception e) {
//         System.out.println("예외 발생시 처리할 코드: 쿼리문 조회(sysdate의 환율정보 확인)");
      }
      
      DBManager.close(conn, pstmt, rs);   // DB 닫기(select)
      return eVo;
   }
   
   // random 환율 정보 조회
   public ExchangesVo randomRateInfo() {

      String sql = "SELECT DBMS_RANDOM.VALUE( B.min_rate, B.max_rate ) random_rate FROM DUAL D, (SELECT MIN(base_rate) as min_rate, MAX(base_rate) as max_rate FROM exchanges, ( SELECT TO_CHAR(SYSDATE + LEVEL - 1, 'YYYY-MM-DD') DT, LEVELFROM DUAL CONNECT BY LEVEL <= ( TO_CHAR(SYSDATE, 'YYYY-MM-DD') - ( TO_CHAR(SYSDATE-365, 'YYYY-MM-DD') )))) B";
      Connection conn = null;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      ExchangesVo eVo = null;      
      try {

         conn = DBManager.getConnection();
         pstmt = conn.prepareStatement(sql);
         rs = pstmt.executeQuery();
         
         if(rs.next()) {
            eVo = new ExchangesVo();

            double base_rateR = rs.getDouble("random_rate");         // DB의 base_rate에 random 값 저장
            System.out.println(base_rateR);
            double purchase_krwR = base_rateR * 1.0175;      // purchase_krw = random(base_rate) * 1.0175
            double selling_krwR = base_rateR * 0.9825;      // selling_krw = random(base_rate) * 0.9825
            
            eVo.setBase_rate(base_rateR);   
            eVo.setPurchase_krw(purchase_krwR);   // DB의 purchase_rate에 값 저장
            eVo.setSelling_krw(selling_krwR);    // DB의 selling_rate에 값 저장
            
            System.out.println(eVo);
         }
      } catch (Exception e) {
//         System.out.println("예외 발생시 처리할 코드: 쿼리문 조회(sysdate의 환율정보 확인)");
      }
      
      DBManager.close(conn, pstmt, rs);   // DB 닫기(select)
      return eVo;
   }
   
   // random 환율 insert
   public void insertExchangesRate(double base_rate, double purchase_krw, double selling_krw) {
      String sql = "INSERT INTO exchanges VALUES (TO_CHAR(SYSDATE, 'YYYY-MM-DD'), 'USD', ?, ?, ?)";
      
      PreparedStatement pstmt = null;
      Connection conn = null;
      
      try {
         conn = DBManager.getConnection();      // DB 연결
         
//         (3단계) PreparedStatement 객체 생성
         pstmt = conn.prepareStatement(sql);      // 쿼리문 실행
//         pstmt.setString(1, ex_date);         // 1: 첫 번째 물음표
         pstmt.setDouble(1, base_rate);         // 2: 두 번째 물음표
         pstmt.setDouble(2, purchase_krw);      // 3: 세 번째 물음표
         pstmt.setDouble(3, selling_krw);      // 4: 네 번째 물음표
         
//         (4단계) SQL문 실행 결과 처리
         pstmt.executeUpdate();         // insert/update/delete 쿼리문 결과 처리
      } catch(Exception e) {
         System.out.println("예외 발생시 처리할 코드: 쿼리문 삽입");
      }
      DBManager.close(conn, pstmt);      // DB 닫기
      
   }
   
   
   
   
   
   
   
   
   
   
   
   
   
   // [[현재 환율 표시]]
   public void exchangesP() {
      ExchangesVo exPresent;
      exPresent = exchangesInfo();
      
      String ex_date = null;
      double[] exchangesP = new double[3];
      ex_date = exPresent.getEx_date();
      exchangesP[0] = exPresent.getBase_rate();
      exchangesP[1] = exPresent.getPurchase_krw();
      exchangesP[2] = exPresent.getSelling_krw();
      
      System.out.println(" [달러 환율]");
      System.out.println(" 날짜\t환율\t\t달러 살 때\t\t달러 팔 때");
      System.out.print( " " + ex_date + "\t" );
      System.out.print( String.format("%.2f", exchangesP[0]) + "(원)\t" );
      for(int i=1; i<exchangesP.length; i++) {
         System.out.print( String.format("%.2f", exchangesP[i]) + "(원)\t\t");
      }
      System.out.println();
   }
   
   //   [[보유 금액 조회]] : 보유 달러, 원화 조회 => [보유 금액(USD, KRW)] 동시 조회
   public static TradingVo amount() {
      String sql = "SELECT SUM(amount_krw) AS amount_krw, SUM(amount_usd) AS amount_usd\r\n"
            + "    FROM trading_income";
      
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      Connection conn = null;
      
      TradingVo amount = new TradingVo();
      
      try {
         conn = DBManager.getConnection();      // DB 연결
         pstmt = conn.prepareStatement(sql);      // 쿼리문 실행
         rs = pstmt.executeQuery();            // 쿼리문 결과 처리
         
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
      DBManager.close(conn, pstmt, rs);   // DB 닫기
      return amount;
   }
   
   //   [[보유 원화(KRW) 조회]] => [원화 충전]에 사용
   public static TradingVo amountKRW() {
      String sql = "SELECT SUM(amount_krw) AS amount_krw\r\n"
            + "    FROM trading_income";
      
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      Connection conn = null;
      
      TradingVo tVo = new TradingVo();   // TradingVo 클래스 객체 생성
      double a_krw = 0;
      
      try {
         conn = DBManager.getConnection();      // DB 연결
         pstmt = conn.prepareStatement(sql);      // 쿼리문 실행
         rs = pstmt.executeQuery();            // 쿼리문 결과 처리
         
         // 보유한 원화가 null이면 0.0으로 표시, 아니면 그대로 조회
         while(rs.next()) {
            if (rs.getDouble("amount_krw") == 0) {
               a_krw = 0.0;
//               System.out.println(a_krw);      // 디버깅
               tVo.setAmount_krw(a_krw);
            } else {
               a_krw = rs.getDouble("amount_krw");
//               System.out.println(a_krw);      // 디버깅
               tVo.setAmount_krw(a_krw);
            }
         }
      } catch(Exception e) {
         System.out.println("예외 발생시 처리할 코드: 쿼리문 조회(보유 달러)");
      }
      DBManager.close(conn, pstmt, rs);   // DB 닫기
      return tVo;
   }
   
   //   [[보유 달러(USD) 조회]] => [원화/달러 거래, 거래 기록]에 사용
   public static TradingVo amountUSD() {
      String sql = "SELECT SUM(amount_krw) AS amount_krw, SUM(amount_usd) AS amount_usd\r\n"
            + "    FROM trading_income";
      
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      Connection conn = null;
      
      TradingVo tVo = new TradingVo();
      double a_usd = 0;
      
      try {
         conn = DBManager.getConnection();      // DB 연결
         pstmt = conn.prepareStatement(sql);      // 쿼리문 실행
         rs = pstmt.executeQuery();            // 쿼리문 결과 처리
         
         // 보유한 달러가 null이면 0.0으로 표시, 아니면 그대로 조회
         while(rs.next()) {
            if (rs.getDouble("amount_usd") == 0) {
               a_usd = 0.0;
//               System.out.println(a_usd);
               tVo.setAmount_usd(a_usd);
            } else {
               a_usd = rs.getDouble("amount_usd");
//               System.out.println(a_usd);
               tVo.setAmount_usd(a_usd);
            }            
         }

      } catch(Exception e) {
         System.out.println("예외 발생시 처리할 코드: 쿼리문 조회(보유 금액)");
      }
      DBManager.close(conn, pstmt, rs);   // DB 닫기
      return tVo;
   }
   
   // 2-1. 거래 - 금액(원화) 충전
//   유스케이스 ID: UC-U03
   public TradingVo chargeAmount(Scanner sc) {
      select = null;
      SelectInfo selectInfo = new SelectInfo();   // 조회 클래스 객체 생성
      MainMenu mainMenu = new MainMenu();         // 메인 메뉴 클래스 객체 생성
      TradingVo trading = new TradingVo();      // TradingVo 클래스 객체 생성
      
      TradingVo tVo;
      tVo = amountKRW();
      double amount_krwP = tVo.getAmount_krw();   // 현재 보유금액(원화)
      
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
//            charge_krw = Integer.parseInt(select);      //String을 Int로 변환하여 사용 
         charge_krw = Double.parseDouble(select);   //String을 double로 변환하여 사용
      }
      if(select.equals("0")) {
         System.out.println(" \n<<메인 메뉴로 돌아갑니다.>>");
         select = mainMenu.mainMenu(sc);
      } else if(charge_krw < 1000) {
         System.out.println(" \n<<충전은 최소 1,000(원)부터 가능합니다. 다시 입력해 주세요.>>");
         chargeAmount(sc);
      } else if(charge_krw >= 1000) {
         
         // [1]. 입력된 charge_krw가 amount_krw(trading_income 테이블)에 임시 저장
         trading.setAmount_krw(charge_krw);   // 입력값을 보관
         
         double amount_krwF[] = new double[2];
         amount_krwF[0] = amount_krwP;
         amount_krwF[1] = trading.getAmount_krw();
         
         double sum = Arrays.stream(amount_krwF).sum();
         
         System.out.println("\n===============================================================");
                  System.out.println(" 예상 보유 금액(KRW): " + sum + "(원)");
//         System.out.println(" 충전 금액(KRW): " + trading.getAmount_krw() + "(원)");
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
               pstmt.setDouble(1, trading.getAmount_krw());   // 1번째 물음표, 입력값
               
               pstmt.executeUpdate();   // insert 쿼리문 결과 처리
               
            } catch(Exception e) {
               System.out.println("예외 발생시 처리할 코드: 쿼리문 삽입(원화 충전)");
            }
            System.out.println(" \n충전이 완료되었습니다. ▶ 보유한 원화: " + amount_krwP + "(원)");
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

   
   
   // 2-2. 원화/달러 거래
//   유스케이스 ID: UC-U04
   public String trading(Scanner sc) {
      String select = null;
      
      SelectInfo selectInfo = new SelectInfo();   // 조회 클래스 객체 생성
      MainMenu mainMenu = new MainMenu();         // 메인 메뉴 객체 생성
      TradingVo tVo = new TradingVo();
      ExchangesVo exchanges = new ExchangesVo();
      
      System.out.println("===============================================================");
      amount();         // 보유 달러 및 원화 조회
      System.out.println("===============================================================");
//      System.out.println();
      exchangesP();      // 현재 환율 조회
      System.out.println();
      System.out.println("===============================================================");
      System.out.println(" [원화/달러 거래]");
      System.out.println("| 1. 매수 | 2. 매도 | 3. 메인 메뉴로 돌아가기 |");
      System.out.println("===============================================================");
      System.out.println(" 입력>> ");
      
      // 2-2-1. 거래방법(매수/매도) 선택
      select = sc.nextLine();
      tradingMethod = null;
      
      switch(select) {
         case "1":
            this.tradingMethod = "매수";
            tVo.setTrading_method(tradingMethod);   // 임시저장
//            System.out.println("\n <<거래 방법 - " + tradingMethod + ">>");
//            System.out.println("==================================================");
            inputTradingAmount(sc, tradingMethod);         // 금액 입력 메소드로 이동
//            currency(sc);
            break;
         case "2":
            if(tVo.getTrading_method() == null) {
               System.out.println(" 보유한 달러 기록이 없으므로");
               System.out.println(" 현재는 매수만 가능합니다.");
               trading(sc);
               break;
            } else {
               this.tradingMethod = "매도";
               tVo.setTrading_method(tradingMethod);   // 임시저장
//               System.out.println("\n <<거래 방법 - " + tradingMethod + ">>");
//               System.out.println("==================================================");
               inputTradingAmount(sc, tradingMethod);      // 금액 입력 메소드로 이동
//               currency(sc);
               break;
            }
            
         case "3":
            System.out.println(" <<메인 메뉴로 돌아갑니다.>>");
            mainMenu.mainMenu(sc);
            break;
         default:
            mainMenu.wrong(sc);
            trading(sc);
            break;
      }
      return tradingMethod;
   }
   
   // 2-2-3. 거래 금액 입력(입력 통화는 달러(USD)만 가능)
   public Double inputTradingAmount(Scanner sc, String tradingMethod) {
      MainMenu mainMenu = new MainMenu();
      TradingVo trading = new TradingVo();
      
      select = null;
      Double inputTradingAmount = null;
      TradingVo tVo1, tVo2;
      tVo1 = amountUSD();
      tVo2 = amountKRW();
      double amount_usdP = tVo1.getAmount_usd();   // 현재 보유금액(달러)
      double amount_krwP = tVo2.getAmount_krw();   // 현재 보유금액(원화)
      
      ExchangesVo eVo;
      eVo = exchangesInfo();
//      tradingMethod = trading.getTrading_method();
      double baseP = eVo.getBase_rate();
      double purchaseP = eVo.getPurchase_krw();
      double sellingP = eVo.getSelling_krw();
      
      System.out.println("\n << 거래 금액을 입력해 주세요.>>");
      System.out.println("===============================================================");
      amount();         // 보유 달러 및 원화 조회
      System.out.println("===============================================================");
//      System.out.println();
      exchangesP();   // 현재 환율 조회
      System.out.println("===============================================================");
      System.out.println(" [거래 방법 - " + tradingMethod + "]");
      System.out.println(" 입력 기준 통화는 달러(USD) 입니다.");
      System.out.println(" 거래는 최소 1(달러) 부터 가능하며, 숫자만 입력 가능합니다.");
      System.out.println("===============================================================");
      System.out.println(" 입력>>");
      
      select = sc.nextLine();
      
      // 숫자가 아닐 경우 재입력
      while(select == "^[\\D]*$") {
         mainMenu.wrong(sc);
         inputTradingAmount(sc, tradingMethod);
         break;
      }
      if(select != "^[\\D]*$") {
         inputTradingAmount = Double.parseDouble(select);   // String을 double로 변환하여 사용
      }
      if(inputTradingAmount < 1) {
         System.out.println(" \n<<충전은 최소 1,000(원)부터 가능합니다. 다시 입력해 주세요.>>");
         inputTradingAmount(sc, tradingMethod);
      } else if(inputTradingAmount >= 1) {
         // 입력된 내용들을 trading_income 테이블에 임시 저장
         trading.setAmount_usd(inputTradingAmount);
         
         // 거래 확인 여부 선택
         select = null;
         
         // 입력한 거래 금액(달러)
         double input_usd = trading.getAmount_krw();
         // cal_krw = 입력한 달러(input_usd) x 달러 살때 금액(purchaseP)
         double cal_krw =0;
         cal_krw = (input_usd * purchaseP);
         double amount_usdF = 0;   // 예상 달러(USD)
         double amount_krwF = 0;   // 예상 원화(KRW)
         double apply_usd = 0;   // DB에 적용되는 달러(apply_usd)
         double apply_krw = 0;   // DB에 적용되는 원화(apply_krw)
         
         System.out.println("\n << 거래 방법 => trading.getTrading_method() >>");
         System.out.println("===============================================================");
         System.out.println(" [거래 후 예상 금액]");
         // 매수일 때
         if(tradingMethod.equals("매수")) {         // 매수일 경우
            
            // 예상 달러(amount_usdF) = 현재 보유 달러(amount_usdP) + 입력한 달러(input_usd)
            amount_usdF = amount_usdP + input_usd;
            // 예상 원화(amount_krwF) = ( 현재 보유 원화(amount_krwP) - 매수할 달러(cal_krw) )
            amount_krwF = amount_krwP - cal_krw;
            
            if(cal_krw > amount_krwP) {      // 매수할 USD > 보유한 KRW일 때
               System.out.println(" 현재 보유한 원화(KRW)를 초과합니다.\n 다시 입력해 주세요.");
               System.out.println("===============================================================");
               System.out.println(" 입력>>");
            } else {
               // 예상 금액(달러, 원화) 조회
               System.out.println(" 예상 달러: " + amount_usdF);
               System.out.println(" 예상 원화: " + amount_krwF);
               // 데이터를 임시 저장
               trading.setAmount_krw( -1 * cal_krw);   // 원화는 매수할 달러만큼 (-)
               trading.setAmount_usd(cal_krw);         // 달러는 매수할 달러만큼 (+)   
            }
            
         // 매도일 때
         } else if(tradingMethod.equals("매도")) {      // 매도일 경우
            
            // 예상 달러(amount_usdF) = 현재 보유 달러(amount_usdP) - 입력한 달러(input_usd)
            amount_usdF = amount_usdP - input_usd;
            // 예상 원화(amount_krwF) = ( 현재 보유 원화(amount_krwP) + 매도할 달러(cal_krw) )
            amount_krwF = amount_krwP + cal_krw;
            if(cal_krw > amount_usdP) {            // 매도할 USD > 보유한 USD일 때
               System.out.println(" 현재 보유한 달러(USD)를 초과합니다.\n 다시 입력해 주세요.");
               System.out.println("===============================================================");
               System.out.println(" 입력>>");
            } else {
               // 예상 금액(달러, 원화) 조회
               System.out.println(" 예상 달러: " + amount_usdF);
               System.out.println(" 예상 원화: " + amount_krwF);
               // 데이터를 임시 저장
               trading.setAmount_krw(cal_krw);         // 원화는 매도할 달러만큼 (+)
               trading.setAmount_usd(-1 * cal_krw);   // 달러는 매도할 달러만큼 (-)
            }
         }
         
         // 충전 확인 질문에 승낙하면, trading_income 테이블에 데이터 저장(insert)
         System.out.println("===============================================================");
         System.out.println(" [거래를 완료하시겠습니까?]");
         System.out.println(" 원하는 대답의 번호를 입력해 주세요.");
         System.out.println(" | 1. 네 | 2. 아니오 |");
         System.out.println("===============================================================");
         System.out.println(" 입력>>");
         
         select = null;
         select = sc.nextLine();
         
         switch(select) {
            case "1":
               Connection conn = null;
               PreparedStatement pstmt = null;
               
               apply_usd = trading.getAmount_usd();
               apply_krw = trading.getAmount_krw();
               
               try {
                  
                  String sql = "INSERT INTO trading_income "
                        + "VALUES(trading_seq.NEXTVAL, SYSDATE, ?, ?, ?)";
                  conn = DBManager.getConnection();
                  pstmt = conn.prepareStatement(sql);
                  pstmt.setString(1, tradingMethod);      // 거래 방법
                  pstmt.setDouble(2, apply_krw);         // 거래한 원화
                  pstmt.setDouble(3, apply_usd);         // 거래한 달러
                  
                  pstmt.executeUpdate();      // insert 쿼리문 결과 처리
                  
               } catch(Exception e) {
                  System.out.println("예외 발생시 처리할 코드: 쿼리문 삽입(매도/매수)");
               }
               DBManager.close(conn, pstmt);
               
               System.out.println(" \n거래가 완료되었습니다.");
               System.out.println("===============================================================");
               System.out.println( "<<메인 메뉴로 돌아갑니다.>>");
               mainMenu.mainMenu(sc);
               break;
            case "2":
               System.out.println(" <<거래 시작 화면으로 돌아갑니다.>>");
               startTrading(sc);
               break;
            default:
               mainMenu.wrong(sc);
               inputTradingAmount(sc, tradingMethod);
               break;
         }
         
      }
      return inputTradingAmount;
   }
   

   

}