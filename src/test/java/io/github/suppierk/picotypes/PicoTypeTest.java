package io.github.suppierk.picotypes;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;

class PicoTypeTest {

  private final TestType nullValue = new TestType(null);
  private final TestType nonNullValue = new TestType(ThreadLocalRandom.current().nextInt());

  @Test
  void isPresent_must_return_correct_value() {
    assertFalse(nullValue.isPresent());
    assertTrue(nonNullValue.isPresent());
  }

  @Test
  void isEmpty_must_return_correct_value() {
    assertTrue(nullValue.isEmpty());
    assertFalse(nonNullValue.isEmpty());
  }

  @Test
  void ifPresent_must_invoke_action_correctly() {
    nullValue.ifPresent(value -> fail());

    var invoked = new AtomicBoolean(false);
    nonNullValue.ifPresent(value -> invoked.set(true));
    assertTrue(invoked.get());
  }

  @Test
  void ifPresentOrElse_must_invoke_correct_action() {
    var actionInvoked = new AtomicBoolean(false);
    var emptyActionInvoked = new AtomicBoolean(false);

    nullValue.ifPresentOrElse(value -> actionInvoked.set(true), () -> emptyActionInvoked.set(true));
    assertFalse(actionInvoked.get());
    assertTrue(emptyActionInvoked.get());

    // Reset and repeat for non-null
    actionInvoked.set(false);
    emptyActionInvoked.set(false);

    nonNullValue.ifPresentOrElse(
        value -> actionInvoked.set(true), () -> emptyActionInvoked.set(true));
    assertTrue(actionInvoked.get());
    assertFalse(emptyActionInvoked.get());
  }

  @Test
  void or_must_invoke_supplier_as_expected() {
    var replacement = new TestType(ThreadLocalRandom.current().nextInt());

    assertEquals(replacement, nullValue.or(() -> replacement));
    assertEquals(nonNullValue, nonNullValue.or(() -> replacement));
  }

  @Test
  void stream_must_return_expected_value() {
    assertTrue(nullValue.stream().toList().isEmpty());
    assertEquals(nonNullValue.value(), nonNullValue.stream().toList().get(0));
  }

  @Test
  void orElse_must_return_correct_value() {
    var replacement = ThreadLocalRandom.current().nextInt();

    assertEquals(replacement, nullValue.orElse(replacement));
    assertEquals(nonNullValue.value(), nonNullValue.orElse(replacement));
  }

  @Test
  void orElseGet_must_return_correct_value() {
    var replacement = ThreadLocalRandom.current().nextInt();

    assertEquals(replacement, nullValue.orElseGet(() -> replacement));
    assertEquals(nonNullValue.value(), nonNullValue.orElseGet(() -> replacement));
  }

  @Test
  void orElseThrow_must_throw_exception_in_correct_scenario() {
    assertThrows(NoSuchElementException.class, nullValue::orElseThrow);
    assertEquals(nonNullValue.value(), assertDoesNotThrow(() -> nonNullValue.orElseThrow()));
  }

  @Test
  void orElseThrow_must_throw_correct_exception_in_correct_scenario() {
    assertThrows(
        IllegalStateException.class, () -> nullValue.orElseThrow(IllegalStateException::new));
    assertEquals(
        nonNullValue.value(),
        assertDoesNotThrow(() -> nonNullValue.orElseThrow(IllegalStateException::new)));
  }

  private static class TestType implements PicoType<Integer> {
    private final Integer value;

    private TestType(Integer value) {
      this.value = value;
    }

    @Override
    public Integer value() {
      return value;
    }
  }
}
