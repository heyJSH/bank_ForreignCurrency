package dao;
import java.util.Scanner;

//라이브러리 클래스 => OperatingSystemExchangesProject에서 실행
//1. 프로그램 실행시, 메인메뉴 화면을 조회
//유스케이스 ID: UC-U01
public class MainMenu {
	public String select;
	
	// 메소드
	// 메인 메뉴 화면
	public String mainMenu(Scanner sc) {
		
		String select = null;
		
//		boolean run = true;	// 항상 실행하는 프로그램
		Trading trading = new Trading();	// Trading 클래스 객체 생성
		
		System.out.println("=================================================");
		System.out.println(" [달러 투자 계산기]");
		System.out.println("  원하는 메뉴의 번호를 입력해 주세요.");
		System.out.println("| 1. 거래 시작 | 2. 거래 기록 조회 | 3. 종료 |");
		System.out.println("=================================================");
		System.out.println(" 입력>> ");
		select = sc.nextLine();
		switch(select) {
			case "1":		// 거래 시작
				System.out.println(" \n<<거래 시작을 선택하셨습니다.>>");
				trading.startTrading(sc);
				break;
			case "2":		// 거래 기록 조회
				System.out.println(" \n<<거래 기록 조회를 선택하셨습니다.>>");
				break;
			case "3":		// 프로그램 종료
				System.out.println(" \n<<프로그램 종료를 선택하셨습니다.>>");
				programEnd(sc);
				break;
			default:		// 잘못 입력한 경우
				wrong(sc);
				mainMenu(sc);
				break;
		}
		return select;
	}
	
	// 잘 못 입력된 경우
	public void wrong(Scanner sc) {
		String select = null;
		System.out.println("\n<<잘못된 값을 입력하였습니다. 다시 입력해 주세요.>>");
//		return select;
	}
	
	// 프로그램 종료
	public void programEnd(Scanner sc) {
		String select = null;
		
			System.out.println("=================================================");
			System.out.println(" [프로그램을 종료하시겠습니까?]");
			System.out.println(" 원하는 메뉴의 번호를 입력해 주세요.");
			System.out.println("| 1. 네 | 2. 아니오 |");
			System.out.println(" 입력>> ");
			
			select = sc.nextLine();
			switch(select) {
				case "1":
					System.out.println("\n <<프로그램 종료>>");
					break;
				case "2":
					System.out.println("\n <<메인 메뉴로 돌아갑니다.>>");
					mainMenu(sc);
					break;
				default:
					wrong(sc);
					break;
			}

	}
	

}
