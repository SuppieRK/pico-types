package io.github.suppierk.picotypes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Modifier;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class IntegrityTest {

  static Stream<Arguments> picoTypes() {
    return Stream.of(
        Arguments.of(BigDecimalPicoType.class),
        Arguments.of(BigIntegerPicoType.class),
        Arguments.of(BooleanPicoType.class),
        Arguments.of(DoublePicoType.class),
        Arguments.of(IntegerPicoType.class),
        Arguments.of(LongPicoType.class),
        Arguments.of(PasswordPicoType.class),
        Arguments.of(StringPicoType.class),
        Arguments.of(UriPicoType.class),
        Arguments.of(UuidPicoType.class));
  }

  @ParameterizedTest
  @MethodSource("picoTypes")
  void must_have_single_private_final_field(Class<? extends PicoType<?>> picoType) {
    var fields = picoType.getDeclaredFields();
    assertEquals(1, fields.length, "Must have one field");

    var field = fields[0];
    assertTrue(Modifier.isPrivate(field.getModifiers()), "Field must be private");
    assertTrue(Modifier.isFinal(field.getModifiers()), "Field must be final");
  }

  @ParameterizedTest
  @MethodSource("picoTypes")
  void must_have_single_protected_constructor(Class<? extends PicoType<?>> picoType) {
    var constructors = picoType.getDeclaredConstructors();
    assertEquals(1, constructors.length, "Must have one declared constructor");

    var constructor = constructors[0];
    assertTrue(
        Modifier.isProtected(constructor.getModifiers()), "Declared constructor must be protected");
  }

  @ParameterizedTest
  @MethodSource("picoTypes")
  void must_have_equals_method(Class<? extends PicoType<?>> picoType) throws NoSuchMethodException {
    var equals = picoType.getDeclaredMethod("equals", Object.class);
    assertTrue(Modifier.isFinal(equals.getModifiers()), "equals must be final");
  }

  @ParameterizedTest
  @MethodSource("picoTypes")
  void must_have_final_hashCode_method(Class<? extends PicoType<?>> picoType)
      throws NoSuchMethodException {
    var hashCode = picoType.getDeclaredMethod("hashCode");
    assertTrue(Modifier.isFinal(hashCode.getModifiers()), "hashCode must be final");
  }
}
