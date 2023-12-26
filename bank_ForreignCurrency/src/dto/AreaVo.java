package dto;

public class AreaVo {
	private int code;
	private String name;
	private String is_around;
	private int country_code;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIs_around() {
		return is_around;
	}
	public void setIs_around(String is_around) {
		this.is_around = is_around;
	}
	public int getCountry_code() {
		return country_code;
	}
	public void setCountry_code(int country_code) {
		this.country_code = country_code;
	}
	@Override
	public String toString() {
		return "AreaVO [code=" + code + ", name=" + name + ", is_around=" + is_around + ", country_code=" + country_code
				+ "]";
	}
	
}
