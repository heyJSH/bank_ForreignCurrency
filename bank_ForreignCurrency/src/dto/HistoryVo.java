package dto;

// History 클래스의 정보를 DB에 저장 및 로드하기 위한 매개 클래스
public class HistoryVo {
	private int num;
	private int year;
	private String description;
	private int character_num;
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getCharacter_num() {
		return character_num;
	}
	public void setCharacter_num(int character_num) {
		this.character_num = character_num;
	}
	
	@Override
	public String toString() {
		return "HistoryVo [num=" + num + ", year=" + year + ", description=" + description + ", character_num="
				+ character_num + "]";
	}		
	
}
