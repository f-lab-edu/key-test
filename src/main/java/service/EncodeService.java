package service;

import encode.impl.Base62Encode;
import encode.impl.Base64Encode;

import java.math.BigInteger;
import java.util.List;

public class EncodeService {

    Base62Encode encode62 = new Base62Encode();
    Base64Encode encode64 = new Base64Encode();

    public String encoding62(String input) {

        //1.문자열 -> 2진수 변환
        String binaryStream62 = encode62.stringToBinaryStream(input);

        //2.2진수 -> 10진수 변환
        BigInteger decimal = encode62.binaryToDecimal(binaryStream62);

        //3. 10진수 -> 문자열 변환
        String base62Str = encode62.decimalTo62(decimal);
        System.out.println("base62 인코딩 : " + base62Str);
        System.out.println("base62 인코딩된 문자열의 길이 : " + base62Str.length());
        System.out.println("base62 특수문자 포함 여부 : base62는 특수 문자를 포함하지 않음");

        return base62Str;
    }

    public String encoding64(String input){

        //1.문자열 -> 2진수 변환
        String binaryStream64 = encode64.stringToBinaryStream(input);

        //2.2진수 -> 6bit로 뽀개기
        List list = encode64.divideFor6bit(binaryStream64);

        //3.2진수 -> 64진수로 변환
        String base64Str = encode64.binaryTo64(list);
        System.out.println("base64 인코딩 : " + base64Str);
        System.out.println("base62 인코딩된 문자열의 길이 : " + base64Str.length());
        System.out.println("base62 특수문자 포함 여부 : " + (base64Str.contains("+") || base64Str.contains("/")));

        return base64Str;
    }
}
