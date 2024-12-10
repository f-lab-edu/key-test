package encode.impl;

import encode.Encode;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Base62Encode implements Encode {

  /**
   * Base62 문자집합 List.of()을 사용하여 불변성 보장 -> List를 이용하면 불필요한 객체를 많이 생성함으로 String을 이용하여 데이터 불변을 만드는게
   * best. -> 추후 성능 벤치마킹을 통해 확인 후 개선필요.
   */
  private static final List<Character> TO_BASE62 = List.of(
      '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
      'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
      'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
      'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
      'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
  );

  private static final int LENGTH_8_BIT = 8; // 8bit의 길이
  private static final BigInteger BIG_INT_62 = new BigInteger("62"); //62진법으로 계산하기 위한 수

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

    for (byte b : utf8Bytes) {
      // 각 바이트를 2진수 문자열로 변환 (8비트로 패딩)
      sb.append(byteToBinaryStream(b));
    }

    return sb.toString();
  }

  private String byteToBinaryStream(byte b) {
    return padTo8bits(Integer.toBinaryString(b & 0xFF));
  }

  /**
   * 2진수 문자열을 8bit 형식으로 변환 ex) "10011" -> "00010011"
   */
  private String padTo8bits(String binaryString) {

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
   * 2진수 -> 10진수 변환
   */
  public BigInteger binaryToDecimal(String str) {

    final BigInteger base = new BigInteger("2"); //밑수

    BigInteger sum = new BigInteger("0"); //10진수로 변환 후 누적할 변수
    char[] charArray = str.toCharArray(); // 2진수 -> char 배열

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

    StringBuilder sb = new StringBuilder();

    while (isGreaterDecimalThan62(decimal)) {

      BigInteger remainder = decimal.remainder(BIG_INT_62); //10진수를 62로 나눈 나머지
      sb.append(TO_BASE62.get(remainder.intValue()));

      decimal = decimal.divide(BIG_INT_62); //10진수를 62로 나눈 몫

      //몫이 62보다 작다면 62진법으로 변환
      if (isLessDecimalThan62(decimal)) {
        sb.append(TO_BASE62.get(decimal.intValue()));
      }
    }

    return sb.reverse().toString();
  }

  private boolean isGreaterDecimalThan62(BigInteger decimal) {
    return decimal.compareTo(BIG_INT_62) > 0;
  }

  private boolean isLessDecimalThan62(BigInteger decimal) {
    return decimal.compareTo(BIG_INT_62) < 0;
  }

}
