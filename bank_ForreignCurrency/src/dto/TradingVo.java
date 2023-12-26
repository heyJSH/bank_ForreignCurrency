package dto;

// Trading 클래스의 정보를 DB에 저장하기 위한 매개 클래스
public class TradingVo {
	private int trading_seq;
	private String ex_date;
	private String trading_method;
	private double amount_krw;
	private double amount_usd;
	
	public int getTrading_seq() {
		return trading_seq;
	}
	public void setTrading_seq(int trading_seq) {
		this.trading_seq = trading_seq;
	}
	public String getEx_date() {
		return ex_date;
	}
	public void setEx_date(String ex_date) {
		this.ex_date = ex_date;
	}
	public String getTrading_method() {
		return trading_method;
	}
	public void setTrading_method(String trading_method) {
		this.trading_method = trading_method;
	}
	public double getAmount_krw() {
		return amount_krw;
	}
	public void setAmount_krw(double amount_krw) {
		this.amount_krw = amount_krw;
	}
	public double getAmount_usd() {
		return amount_usd;
	}
	public void setAmount_usd(double amount_usd) {
		this.amount_usd = amount_usd;
	}
//	@Override
//	public String toString() {
//		return "TradingVo [trading_seq=" + trading_seq + ", ex_date=" + ex_date + ", trading_method="
//				+ trading_method + ", amount_krw=" + amount_krw + ", amount_usd=" + amount_usd + "]";
//	}


}
