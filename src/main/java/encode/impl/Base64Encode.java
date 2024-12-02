package encode.impl;


import encode.Encode;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Base64Encode implements Encode {

    /**
     * Base64 문자 집합
     * 주의 : 문자 순서 절대 변경 X
     */
    private static final char[] toBase64 ={
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
    };

    /**
     * 문자열 -> 2진수로 변환
     */
    public String stringToBinaryStream(String input) {

        List<Integer> charList = new ArrayList<>();  //Unicode 코드를 담을 배열
        StringBuilder sb = new StringBuilder();      //2진수를 담은 문자열


        byte[] utf8Bytes = input.getBytes(StandardCharsets.UTF_8);

        for (byte b : utf8Bytes) {
            // 각 바이트를 2진수 문자열로 변환 (8비트로 패딩)
            String binaryString = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            sb.append(binaryString);
        }

        return sb.toString();
    }

    /**
     * 10진수를 2진수로 변환
     */
    public String decimalToBinary(Integer decimal){

        //10진수 num을 2진수로 변환하자.
        int num = decimal;

        StringBuilder sb = new StringBuilder();

        while (num >= 2) {
            if (num % 2 == 0) {
                //나누어 떨어지면 0
                sb.append("0");
            }else{
                //나누어 떨어지지 않으면 1
                sb.append("1");
            }
            num = num/2;
        }

        if (num == 1) {
            sb.append("1");
        }

        //ASCII 코드는 1바이트다. 8bit가 되어야하므로 빈 공간은 0으로 채우자.
        while (sb.length() < 8) {
            sb.append("0");
        }

        return sb.reverse().toString();
    }

    //6bit  쪼개버리자.
    public List divideFor6bit(String binaryStream) {

        List<String> arr = new ArrayList<>();

        char[] charArray = binaryStream.toCharArray();

        for (int i = 0; i < binaryStream.length() ; i = i + 6) {

            StringBuilder sb = new StringBuilder(); //6bit로 쪼개서 저장

            for (int j = i ; j < i + 6 && j < binaryStream.length() ; j++) {
                sb.append(charArray[j]);
            }
            arr.add(sb.toString());

        }

        return arr;
    }

    /**
     * 2진수 -> 64진수로
     */
    public String binaryTo64(List<String> list) {

        StringBuilder sb = new StringBuilder();

        for (String str : list) {

            char[] charArray = str.toCharArray();
            int sum = 0;

            for (int i = 0 , j = charArray.length-1 ; i <charArray.length; i++, j--){

                char c = charArray[i];
                if(c == '1'){
                    sum = sum + (int)Math.pow(2,j);
                }

            }

            sb.append(toBase64[sum]);

        }

        int mod = sb.length() % 4;

        switch (mod) {
            case 0 :
                break;
            case 1 : sb.append("===");
                break;
            case 2: sb.append("==");
                break;
            case 3: sb.append("=");
        }

        return sb.toString();
    }

}
