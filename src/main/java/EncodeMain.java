import java.util.Scanner;
import service.EncodeService;

public class EncodeMain {

  public static void main(String[] args) {

    EncodeService service = new EncodeService();

    String input = "";

    Scanner sc = new Scanner(System.in);
    input = sc.nextLine();

    // null 또는 빈 칸이 입력되면 사용자가 다시 입력을 유도하도록 flow를 만들자.
    while (input == null && input.trim().isEmpty()) {
      System.out.println("빈 값이 입력되었습니다.문자열을 입력해주세요!!(종료를 원하시면 exit)");
      input = sc.nextLine();

      //exit 입력 후 종료
      if ("exit".equals(input)) {
        System.out.println("문자열 입력이 종료되었습니다.");
        return;
      }
    }

    service.encoding62(input);
    service.encoding64(input);
  }

}
