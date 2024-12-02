import encode.impl.Base64Encode;
import service.EncodeService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class EncodeMain {

    public static void main(String[] args) {

        final EncodeService service = new EncodeService();

        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        service.encoding62(input);
        service.encoding64(input);

    }
}
