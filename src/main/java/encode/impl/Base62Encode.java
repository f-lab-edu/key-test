package encode.impl;

import encode.Encode;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Base62Encode implements Encode {

    /**
     * Base62 문자집합
     * List.of()을 사용하여 불변성 보장 -개발자가 실수로 추가, 수정, 삭제 시 UnsupportedOperationException
     */
    private static final List<Character> TO_BASE62 = List.of(
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    );

    private static final int LENGTH_8_BIT = 8; // 8bit의 길이

    @Override
    public String encode(String input) {

        //1.문자열 -> 2진수 변환
        String binaryStream = stringToBinaryStream(input);

        //2.2진수 -> 10진수 변환
        BigInteger decimal = binaryToDecimal(binaryStream);

        //3.10진수 -> 62진법 변환
        return decimalToBase62(decimal);
    }

    /**
     * 문자열 -> 2진수로 변환
     */
    private String stringToBinaryStream(String input) {

        StringBuilder sb = new StringBuilder();      //2진수를 담은 문자열
        byte[] utf8Bytes = input.getBytes(StandardCharsets.UTF_8);

        //수정사항 - for문 안에서 어떠한 일을 하는지 private 함수로 분리하여 함수의 이름으로 왜 하는것인지 명시하면 어떨까요?
        for (byte b : utf8Bytes) {
            // 각 바이트를 2진수 문자열로 변환 (8비트로 패딩)
            sb.append(byteToBinaryStream(b));
        }

        return sb.toString();
    }

    private String byteToBinaryStream(byte b) {
        //수정사항 - 2진수 변환 시 8bit 자릿수를 맞추기 위해 replace()를 사용해 공백을 0으로 패딩 -> replace() 성능문제로 수정
        return formatTo8bits(Integer.toBinaryString(b & 0xFF));
    }

    /**
     * 2진수 문자열을 8bit 형식으로 변환 ex) "10011" -> "00010011"
     */
    private String formatTo8bits(String binaryString) {

        StringBuilder sb = new StringBuilder();
        sb.append(binaryString);

        //8bit 형식을 만들기 왼쪽에 0을 채워야 하는 횟수
        int count = LENGTH_8_BIT - binaryString.length();

        for (int i = 0; i < count; i++) {
            sb.insert(0, "0");
        }

        return sb.toString();
    }

    /**
     * 10진수를 2진수로 변환 이 로직이는 코드에서 필요없는 로직이지만 배울게 많아 우선 놔뒀습니다!
     */
    public String decimalToBinary(Integer decimal) {

        StringBuilder sb = new StringBuilder();

        //수정사항 - 로직을 통해 이해할 수 있지만, while, if 등의 조건을 private 함수로 분리하면 더 가독성이 높아질것 같은데 어떻게 생각하시나요~?
        while (decimal >= 2) {
            if (decimal % 2 == 0) {
                //나누어 떨어지면 0
                sb.append("0");
            } else {
                //나누어 떨어지지 않으면 1
                sb.append("1");
            }
            decimal = decimal / 2;
        }

        if (decimal == 1) {
            sb.append("1");
        }

        //ASCII 코드는 1바이트다. 8bit가 되어야하므로 빈 공간은 0으로 채우자.
        while (sb.length() < 8) {
            sb.append("0");
        }

        return sb.reverse().toString();
    }

    /**
     * 2진수 -> 10진수 변환
     */
    public BigInteger binaryToDecimal(String str) {

        char[] charArray = str.toCharArray(); // 2진수 -> char 배열

        BigInteger sum = new BigInteger("0"); //10진수로 변환 후 누적할 변수

        //밑수
        BigInteger base = new BigInteger("2");

        for (int i = charArray.length - 1, j = 0; i >= 0; i--, j++) {

            char c = charArray[j];

            if (c == '1') {
                sum = sum.add(base.pow(i));
            }
        }

        return sum;
    }

    /**
     * 10진수 -> 62진수 변환
     */
    public String decimalToBase62(BigInteger bigInteger) {
        BigInteger decimal = bigInteger;

        //62진법으로 계산하기 위한 수
        final BigInteger bigInt62 = new BigInteger("62");

        StringBuilder sb = new StringBuilder();

        while (decimal.compareTo(bigInt62) > 0) {

            BigInteger remainder = decimal.remainder(bigInt62); //10진수를 62로 나눈 나머지
            sb.append(TO_BASE62.get(remainder.intValue())); // 나눈 나머지를 62진법으로 변환 후 StringBuilder 추가

            decimal = decimal.divide(bigInt62); //10진수를 62로 나눈 몫

            //몫이 62보다 작다면 62진법으로 변환
            if (decimal.compareTo(bigInt62) < 0) {
                sb.append(TO_BASE62.get(decimal.intValue()));
            }
        }

        //StringBuilder 추가된 문자열 reverse 후 return
        return sb.reverse().toString();
    }

}
