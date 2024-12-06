package encode.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class Base64EncodeTest {

  Base64Encode base64 = new Base64Encode();

  @Test
  @DisplayName("base64 인코딩 테스트")
  void base64Test() {
    String str = base64.encode("Hello World!");
    assertThat(str).isEqualTo("SGVsbG8gV29ybGQh");
  }

}
