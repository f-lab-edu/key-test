package encode.impl;

import encode.Encode;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Base64Encode implements Encode {

    /**
     * Base64 문자집합
     * List.of()을 사용하여 불변성 보장 - 추가, 수정, 삭제 시 UnsupportedOperationException 발생
     */
    private static final List<Character> TO_BASE64 = List.of(
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
    );

    private static final int LENGTH_8_BIT = 8;
    private static final int LENGTH_6_BIT = 6;

    @Override
    public String encode(String input) {

        //1.문자열 -> 2진수 변환
        String binaryStream = stringToBinaryStream(input);

        //2.binaryStream -> 6bit씩 분할
        List<String> list = divideFor6bit(binaryStream);

        //3.이진수 -> 64진수 변환
        return binaryToBase64(list);
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

    //6bit 쪼개버리자.
    public List<String> divideFor6bit(String binaryStream) {

        List<String> arr = new ArrayList<>();
        int count = -1;

        char[] charArray = binaryStream.toCharArray();

        for (int i = 0; i < binaryStream.length(); i = i + 6) {

            StringBuilder sb = new StringBuilder(); //6bit로 쪼개서 저장

            for (int j = i; j < i + LENGTH_6_BIT && j < binaryStream.length(); j++) {
                sb.append(charArray[j]);

            }
            count++;

            arr.add(sb.toString());
        }

        padToSixBits(arr, count);

        return arr;
    }

    private void padToSixBits(List<String> arr, int count) {

        //6으로 나누어 떨어지지 않는다면 6 - 나머지 만큼 0 패딩
        int mod = arr.get(count).length() % LENGTH_6_BIT;

        if (mod != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(arr.get(count));
            for (int i = 0; i < LENGTH_6_BIT - mod; i++) {
                sb.append("0");
            }
            arr.set(count, sb.toString());
        }
    }

    /**
     * 2진수 -> 64진수로
     */
    public String binaryToBase64(List<String> list) {

        StringBuilder sb = new StringBuilder();

        for (String str : list) {
            char[] charArray = str.toCharArray();

            int decimal = binaryToDecimal(charArray);

            sb.append(TO_BASE64.get(decimal));
        }

        padToBase64Block(sb);

        return sb.toString();
    }

    //2진수 -> 10진수 변환
    private int binaryToDecimal(char[] charArray) {

        int decimal = 0;

        for (int i = 0, j = charArray.length - 1; i < charArray.length; i++, j--) {

            char c = charArray[i];

            if (c == '1') {
                decimal = decimal + (int) Math.pow(2, j);
            }

        }

        return decimal;
    }

    private void padToBase64Block(StringBuilder sb) {

        //base64는 3byte(24bit) 단위로 계산하므로 3byte씩 나누어 떨어지게 빈 공간에는 패딩값(=)을 채워줘야 함. -> 이러한 주석은 어떻게 없어애하는지 모르겠다.
        int mod = sb.length() % 4;

        if (mod == 0) {
            return;
        }

        switch (mod) {
            case 1:
                sb.append("===");
                break;
            case 2:
                sb.append("==");
                break;
            case 3:
                sb.append("=");
        }
    }

}
