package service;

import encode.Encode;
import encode.impl.Base62Encode;
import encode.impl.Base64Encode;

public class EncodeService {

  Encode encode62 = new Base62Encode();
  Encode encode64 = new Base64Encode();

  public void encoding62(String input) {

    String base62Str = encode62.encode(input);

    System.out.println("base62 인코딩 : " + base62Str);
    System.out.println("base62 인코딩된 문자열의 길이 : " + base62Str.length());
    System.out.println("base62 특수문자 포함 여부 : base62는 특수 문자를 포함하지 않음");
  }

  public void encoding64(String input) {

    String base64Str = encode64.encode(input);

    System.out.println("base64 인코딩 : " + base64Str);
    System.out.println("base62 인코딩된 문자열의 길이 : " + base64Str.length());
    System.out.println(
        "base62 특수문자 포함 여부 : " + (base64Str.contains("+") || base64Str.contains("/")));
  }
}
