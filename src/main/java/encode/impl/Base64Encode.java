package encode.impl;

import encode.Encode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Base64Encode implements Encode {

  /**
   * Base64 문자집합 List.of()을 사용하여 불변성 보장 -> List를 이용하면 불필요한 객체를 많이 생성함으로 String을 이용하여 데이터 불변을 만드는게
   * best. -> 추후 성능 벤치마킹을 통해 확인 후 개선필요.
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
  private static final int BASE64_CALCULATE_SIZE = 4;

  @Override
  public String encode(String input) {

    //1.문자열 -> 2진수 변환
    String binaryStream = stringToBinaryStream(input);

    //2.binaryStream -> 6bit씩 분할
    List<String> list = splitInto6bit(binaryStream);

    //3.이진수 -> 64진수 변환
    return binaryToBase64(list);
  }

  /**
   * 문자열 -> 2진수 변환
   */
  private String stringToBinaryStream(String input) {

    StringBuilder sb = new StringBuilder();      //2진수를 담은 문자열
    byte[] utf8Bytes = input.getBytes(StandardCharsets.UTF_8);

    for (byte b : utf8Bytes) {
      // 각 바이트를 2진수 문자열로 변환 (8비트로 패딩)
      sb.append(byteToBinaryStream(b));
    }

    return sb.toString();
  }

  private String byteToBinaryStream(byte b) {
    return formatTo8bits(Integer.toBinaryString(b & 0xFF));
  }

  /**
   * 2진수 문자열 -> 8bit 형식 변환 ex) "10011" -> "00010011"
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

  public List<String> splitInto6bit(String binaryStream) {

    char[] charArr = binaryStream.toCharArray();
    List<String> arr = new ArrayList<>();

    for (int i = 0; i < charArr.length; i = i + LENGTH_6_BIT) {
      StringBuilder sb = new StringBuilder();

      for (int j = i; j < i + LENGTH_6_BIT && j < charArr.length; j++) {
        sb.append(charArr[j]);
      }

      arr.add(sb.toString());
    }

    return arr;
  }

  /**
   * 2진수 -> 64진수
   */
  public String binaryToBase64(List<String> list) {

    StringBuilder sb = new StringBuilder();

    for (String str : list) {
      char[] charArray = str.toCharArray();

      int decimal = binaryToDecimal(charArray);

      sb.append(TO_BASE64.get(decimal));
    }

    appendPaddingCharacter(sb);

    return sb.toString();
  }

  /**
   * 2진수 -> 10진수 변환
   */
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

  /**
   * base64는 3byte(24bit) 단위로 계산하므로 3byte씩 나누어 떨어지게 빈 공간에는 패딩값(=)을 채워줘야 함.
   */
  private void appendPaddingCharacter(StringBuilder sb) {

    int mod = sb.length() % BASE64_CALCULATE_SIZE;

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
