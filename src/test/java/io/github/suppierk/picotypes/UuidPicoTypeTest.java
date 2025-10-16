package io.github.suppierk.picotypes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class UuidPicoTypeTest {
  private final UUID value = UUID.fromString("00000000-0000-0000-0000-000000000001");

  private final TestType nullWrapper = new TestType((UUID) null);
  private final TestType wrapper = new TestType(value);
  private final TestType biggerWrapper =
      new TestType(UUID.fromString("00000000-0000-0000-0000-000000000002"));

  @Test
  void must_have_correct_equals_and_hashCode() {
    EqualsVerifier.simple().forClass(UuidPicoType.class).verify();
  }

  @Test
  void subclass_must_inherit_correct_equals_and_hashCode() {
    EqualsVerifier.simple().forClass(TestType.class).verify();
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

  private static class TestType extends UuidPicoType {
    public TestType(UUID value) {
      super(value);
    }

    @JsonCreator
    public TestType(String value) {
      this(UUID.fromString(value));
    }

    @Override
    @JsonValue
    public UUID value() {
      return super.value();
    }
  }
}
