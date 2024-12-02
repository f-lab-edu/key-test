package encode.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.EncodeService;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.*;

class Base62EncodeTest {

    Base62Encode base62 = new Base62Encode();
    EncodeService service = new EncodeService();

    @Test
    @DisplayName("Hello World! base62 인코딩")
    void test() {
        String encode62 = service.encoding62("Hello World!");

        assertThat(encode62).isEqualTo("T8dgcjRGkZ3aysdN");
    }

    @Test
    @DisplayName("10진수 -> 2진수 변환 테스트")
    void decimalToBinary() {
        String binary1 = base62.decimalToBinary(127);
        String binary2 = base62.decimalToBinary(126);

        assertThat(binary1).isEqualTo("01111111");
        assertThat(binary2).isEqualTo("01111110");
    }

    @Test
    @DisplayName("문자열 -> 2진수 문자열 변환 테스트")
    void stringToBinaryStream() {
        String binaryStream1 = base62.stringToBinaryStream("Hel");
        String binaryStream2 = base62.stringToBinaryStream("ABC");
        String binaryStream3 = base62.stringToBinaryStream("Hello World!");

        assertThat(binaryStream1).isEqualTo("010010000110010101101100");
        assertThat(binaryStream2).isEqualTo("010000010100001001000011");
        assertThat(binaryStream3).isEqualTo("010010000110010101101100011011000110111100100000010101110110111101110010011011000110010000100001");
    }

    @Test
    @DisplayName("2진수 -> 10진수")
    void binaryToDecimal() {
        BigInteger decimal1 = base62.binaryToDecimal("010010000110010101101100");
        BigInteger decimal2 = base62.binaryToDecimal("010000010100001001000011");

        assertThat(decimal1).isEqualTo(new BigInteger("4744556"));
        assertThat(decimal2).isEqualTo(new BigInteger("4276803"));
    }

    @Test
    @DisplayName("10진수 -> 62진수")
    void decimalTo62Test() {
        String s1 = base62.decimalTo62(new BigInteger("22405534230753928650781647905"));
        assertThat(s1).isEqualTo("T8dgcjRGkZ3aysdN");
    }
}