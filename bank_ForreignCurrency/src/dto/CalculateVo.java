package dto;

// Calculate 클래스의 정보를 DB에 저장하기 위한 매개 클래스
public class CalculateVo {
	private String ex_date;
	private double dollar_gap;
	private double appropriate_rate;
	private double revenue_rate;
	
	public String getEx_date() {
		return ex_date;
	}
	public void setEx_date(String ex_date) {
		this.ex_date = ex_date;
	}
	public double getDollar_gap() {
		return dollar_gap;
	}
	public void setDollar_gap(double dollar_gap) {
		this.dollar_gap = dollar_gap;
	}
	public double getAppropriate_rate() {
		return appropriate_rate;
	}
	public void setAppropriate_rate(double appropriate_rate) {
		this.appropriate_rate = appropriate_rate;
	}
	public double getRevenue_rate() {
		return revenue_rate;
	}
	public void setRevenue_rate(double revenue_rate) {
		this.revenue_rate = revenue_rate;
	}
	@Override
	public String toString() {
		return "CalculateVo [ex_date=" + ex_date + ". dollar_gap=" + dollar_gap + ", appropriate_rate="
				+ appropriate_rate + ", revenue_rate=" + revenue_rate + "]";
	}

	
	

}
