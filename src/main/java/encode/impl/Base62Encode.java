package encode.impl;

import encode.Encode;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Base62Encode implements Encode {

    /**
     * Base62 문자 집합
     * 주의 : 문자 순서 절대 변경 X
     */
    private static final char[] toBase62 ={
            '0','1','2','3','4','5','6','7','8','9',
            'A','B','C','D','E','F','G','H','I','J','K','L','M',
            'N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
            'a','b','c','d','e','f','g','h','i','j','k','l','m',
            'n','o','p','q','r','s','t','u','v','w','x','y','z'
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
        //ASCII 코드는 1바이트다.

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

    /**
     * 2진수 -> 10진수로
     */
    public BigInteger binaryToDecimal(String str) {

        char[] charArray = str.toCharArray(); // 2진수 -> char 배열

        BigInteger sum = new BigInteger("0"); //10진수로 변환 후 누적할 변수

        //밑수
        BigInteger base = new BigInteger("2");

        for (int i = charArray.length-1, j = 0 ; i >= 0 ; i--,j++) {

            char c = charArray[j];

            if(c == '1'){
                sum = sum.add(base.pow(i));
            }
        }

        return sum;
    }

    /**
     * 10진수 -> 62진수 변환
     */
    public String decimalTo62(BigInteger bigInteger) {
        BigInteger decimal = bigInteger;

        //62진법으로 계산하기 위한 수
        final BigInteger bigInt62 = new BigInteger("62");

        StringBuilder sb = new StringBuilder();

        while (decimal.compareTo(bigInt62) > 0) {

            BigInteger remainder = decimal.remainder(bigInt62); //10진수를 62로 나눈 나머지
            sb.append(toBase62[remainder.intValue()]); // 나눈 나머지를 62진법으로 변환 후 StringBuilder 추가

            decimal = decimal.divide(bigInt62); //10진수를 62로 나눈 몫

            //몫이 62보다 작다면 62진법으로 변환
            if (decimal.compareTo(bigInt62) < 0) {
                sb.append(toBase62[decimal.intValue()]);
            }
        }

        //StringBuilder 추가된 문자열 reverse 후 return
        return sb.reverse().toString();
    }

}
