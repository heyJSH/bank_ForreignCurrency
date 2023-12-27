package dao;

import java.util.Scanner;
import dto.ExchangesVo;
import dto.TradingVo;
// 3. 거래 기록 조회 화면
// 유스케이스 ID: UC-U05
public class TradingRecord {
//라이브러리 클래스 => OperatingSystemExchangesProject에서 실행
	
	public String select;
	public MainMenu mainMenu = new MainMenu();
	public Trading trading = new Trading();
	public TradingVo tVo;
	public ExchangesVo eVo;
	
	public double amount_usd = 0;				// 보유 달러
	public double amount_krw = 0;				// 보유 원화
	public double Pamount_krwT = 0;				// 총 매수 금액(원)
	public double Samount_krwT = 0;				// 총 매도 금액(원)
	public double RevenueRateT = 0;				// 총 수익률(%)
	public double base_rate = 0;				// 매매 기준율, 현재 환율
	public double purchase_krw = 0;				// 달러 살때(원)
	public double selling_krw = 0;				// 달러 팔때(원)
	public double flucMinW = 0;					// 52주 변동폭(Min)
	public double flucMaxW = 0;					// 52주 변동폭(Max)
	public double flucMinT = 0;					// 금일 변동폭(Min)
	public double flucMaxT = 0;					// 금일 변동폭(Max)
	
	// 거래 기록 조회에 필요한 목록 계산 및 정리
	public void calRecord() {
		// trading_income 모든 내용 조회
		
	}
	
	// 3. 거래 기록 조회 화면
	public void tradingRecord(Scanner sc) {
		System.out.println(" <<>>");
	}
}
