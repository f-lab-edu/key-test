package encode.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class Base62EncodeTest {

  Base62Encode base62 = new Base62Encode();

  @Test
  @DisplayName("Hello World! base62 인코딩")
  void test() {
    String encode62 = base62.encode("Hello World!");
    assertThat(encode62).isEqualTo("T8dgcjRGkZ3aysdN");
  }
}