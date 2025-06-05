package io.github.suppierk.picotypes;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class PasswordPicoTypeTest {
  private final byte[] value =
      String.valueOf(ThreadLocalRandom.current().nextInt()).getBytes(StandardCharsets.UTF_8);

  private final TestType nullWrapper = new TestType(null);
  private final TestType wrapper = new TestType(value);

  @Test
  void must_have_correct_equals_and_hashCode() {
    EqualsVerifier.forClass(PasswordPicoType.class).verify();
  }

  @Test
  void subclass_must_inherit_correct_equals_and_hashCode() {
    EqualsVerifier.forClass(TestType.class).verify();
  }

  @Test
  void must_create_internal_immutable_copy() {
    var byteArray =
        String.valueOf(ThreadLocalRandom.current().nextInt()).getBytes(StandardCharsets.UTF_8);
    var arrayWrapper = new TestType(byteArray);

    assertNotEquals(byteArray, arrayWrapper.value());
    assertArrayEquals(byteArray, arrayWrapper.value());

    byteArray[0] = (byte) (byteArray[0] + 1);

    assertThrows(AssertionError.class, () -> assertArrayEquals(byteArray, arrayWrapper.value()));
  }

  @Test
  void must_copy_value_on_retrieval() {
    var arrayWrapper =
        new TestType(
            String.valueOf(ThreadLocalRandom.current().nextInt()).getBytes(StandardCharsets.UTF_8));
    var byteArray = arrayWrapper.value();

    assertArrayEquals(byteArray, arrayWrapper.value());

    byteArray[0] = (byte) (byteArray[0] + 1);

    assertThrows(AssertionError.class, () -> assertArrayEquals(byteArray, arrayWrapper.value()));
  }

  @Test
  void must_return_passed_value_as_is() {
    assertNull(nullWrapper.value());
    assertArrayEquals(value, wrapper.value());
  }

  @Test
  void must_have_correct_toString() {
    assertEquals(TestType.class.getSimpleName() + "{value=*******}", wrapper.toString());
  }

  private static class TestType extends PasswordPicoType {
    public TestType(byte[] value) {
      super(value);
    }
  }
}
