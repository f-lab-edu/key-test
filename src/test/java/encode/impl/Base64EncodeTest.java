package encode.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.EncodeService;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Base64EncodeTest {

    Base64Encode base64 = new Base64Encode();
    EncodeService service = new EncodeService();

    @Test
    @DisplayName("Hello World! base64 인코딩")
    void test() {
        String encode64 = service.encoding64("Hello World!");

        assertThat(encode64).isEqualTo("SGVsbG8gV29ybGQh");
    }

    @Test
    @DisplayName("2진수를 6bit 단위로 쪼개기")
    void divideFor64bit() {
        List list = base64.divideFor6bit("010010000110010101101100");
        System.out.println(list);

    }

    @Test
    @DisplayName("2진수 -> 64진법")
    void binaryTo64() {
        ArrayList<String> arr = new ArrayList<String>();
        arr.add("010010");
        arr.add("000110");
        arr.add("010101");
        arr.add("101100");

        final String s = base64.binaryTo64(arr);
        System.out.println("s = " + s);
    }

}