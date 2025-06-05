package io.github.suppierk.picotypes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.ThreadLocalRandom;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class StringPicoTypeTest {
  private final int randomValue = ThreadLocalRandom.current().nextInt(0, 1_000_000);

  private final String value = String.valueOf(randomValue);
  private final TestType wrapper = new TestType(value);
  private final TestType nullWrapper = new TestType(null);
  private final TestType biggerWrapper = new TestType(String.valueOf(randomValue + 1));

  @Test
  void must_have_correct_equals_and_hashCode() {
    EqualsVerifier.forClass(StringPicoType.class).verify();
  }

  @Test
  void subclass_must_inherit_correct_equals_and_hashCode() {
    EqualsVerifier.forClass(TestType.class).verify();
  }

  @Test
  void must_return_passed_value_as_is() {
    assertNull(nullWrapper.value());
    assertEquals(value, wrapper.value());
  }

  @Test
  void must_have_correct_toString() {
    assertEquals(TestType.class.getSimpleName() + "{value=" + value + '}', wrapper.toString());
  }

  @Test
  void must_have_correct_compareTo() {
    assertEquals(-1, wrapper.compareTo(biggerWrapper));
    assertEquals(0, wrapper.compareTo(wrapper));
    assertEquals(0, biggerWrapper.compareTo(biggerWrapper));
    assertEquals(1, biggerWrapper.compareTo(wrapper));

    assertThrows(NullPointerException.class, () -> nullWrapper.compareTo(wrapper));
    assertThrows(NullPointerException.class, () -> wrapper.compareTo(null));
    assertThrows(NullPointerException.class, () -> wrapper.compareTo(nullWrapper));
  }

  @Test
  void must_be_correctly_serialized_with_Jackson() throws Exception {
    var mapper = new ObjectMapper();
    var serialized = mapper.writeValueAsString(wrapper);
    var deserialized = mapper.readValue(serialized, TestType.class);

    assertEquals("\"" + value + "\"", serialized);
    assertEquals(wrapper, deserialized);
  }

  private static class TestType extends StringPicoType {
    @JsonCreator
    public TestType(String value) {
      super(value);
    }

    @Override
    @JsonValue
    public String value() {
      return super.value();
    }
  }
}
