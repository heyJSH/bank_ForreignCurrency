package dto;
// 환율 클래스의 정보를 DB에 저장하기 위한 매개 클래스
public class ExchangesVo {
	private String ex_date;
	private String currency_name;
	private double base_rate;
	private double purchase_krw;
	private double selling_krw;
	
	public String getEx_date() {
		return ex_date;
	}
	public void setEx_date(String ex_date) {
		this.ex_date = ex_date;
	}
	public String getCurrency_name() {
		return currency_name;
	}
	public void setCurrency_name(String currency_name) {
		this.currency_name = currency_name;
	}
	public double getBase_rate() {
		return base_rate;
	}
	public void setBase_rate(double base_rate) {
		this.base_rate = base_rate;
	}
	public double getPurchase_krw() {
		return purchase_krw;
	}
	public void setPurchase_krw(double purchase_krw) {
		this.purchase_krw = purchase_krw;
	}
	public double getSelling_krw() {
		return selling_krw;
	}
	public void setSelling_krw(double selling_krw) {
		this.selling_krw = selling_krw;
	}
	@Override
	public String toString() {
		return "ExchangesVO [ ex_date=" + ex_date + ", currency_name=" + currency_name + ", base_rate="
				+ base_rate + ", purchase_krw=" + purchase_krw + ", selling_krw=" + selling_krw;
	}


}
