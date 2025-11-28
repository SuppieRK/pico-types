/*
 * MIT License
 *
 * Copyright 2025 Roman Khlebnov
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package io.github.suppierk.picotypes;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * Core interface with default contract and {@link Optional}-like API
 *
 * @param <T> is the type of the wrapped value
 */
public interface PicoType<T> {
  /**
   * Returns current value
   *
   * @return current value (might be {@code null})
   */
  @Nullable T value();

  /**
   * If a value is present, returns {@code true}, otherwise {@code false}.
   *
   * @return {@code true} if a value is present, otherwise {@code false}
   */
  default boolean isPresent() {
    return value() != null;
  }

  /**
   * If a value is not present, returns {@code true}, otherwise {@code false}.
   *
   * @return {@code true} if a value is not present, otherwise {@code false}
   */
  default boolean isEmpty() {
    return value() == null;
  }

  /**
   * If a value is present, performs the given action with the value, otherwise does nothing.
   *
   * @param action the action to be performed, if a value is present
   * @throws NullPointerException if value is present and the given action is {@code null}
   */
  default void ifPresent(@NonNull Consumer<? super T> action) {
    if (isPresent()) {
      Objects.requireNonNull(action).accept(value());
    }
  }

  /**
   * If a value is present, performs the given action with the value, otherwise performs the given
   * empty-based action.
   *
   * @param action the action to be performed, if a value is present
   * @param emptyAction the empty-based action to be performed, if no value is present
   * @throws NullPointerException if a value is present and the given action is {@code null}, or no
   *     value is present and the given empty-based action is {@code null}.
   */
  default void ifPresentOrElse(@NonNull Consumer<? super T> action, @NonNull Runnable emptyAction) {
    if (isPresent()) {
      Objects.requireNonNull(action).accept(value());
    } else {
      Objects.requireNonNull(emptyAction).run();
    }
  }

  /**
   * If a value is present, returns an {@code PicoType} describing the value, otherwise returns an
   * {@code PicoType} produced by the supplying function.
   *
   * @param supplier the supplying function that produces an {@code PicoType} to be returned
   * @return returns an {@code PicoType} describing the value of this {@code PicoType}, if a value
   *     is present, otherwise an {@code PicoType} produced by the supplying function.
   * @throws NullPointerException if the supplying function is {@code null} or produces a {@code
   *     null} result
   */
  default PicoType<T> or(@NonNull Supplier<? extends PicoType<? extends T>> supplier) {
    if (isPresent()) {
      return this;
    } else {
      @SuppressWarnings("unchecked")
      PicoType<T> r = (PicoType<T>) Objects.requireNonNull(supplier).get();
      return Objects.requireNonNull(r);
    }
  }

  /**
   * If a value is present, returns a sequential {@link Stream} containing only that value,
   * otherwise returns an empty {@code Stream}.
   *
   * @return the optional value as a {@code Stream}
   */
  default @NonNull Stream<T> stream() {
    if (isPresent()) {
      return Stream.of(value());
    } else {
      return Stream.empty();
    }
  }

  /**
   * If a value is present, returns the value, otherwise returns {@code other}.
   *
   * @param other the value to be returned, if no value is present. May be {@code null}.
   * @return the value, if present, otherwise {@code other}
   */
  default @Nullable T orElse(@Nullable T other) {
    return isPresent() ? value() : other;
  }

  /**
   * If a value is present, returns the value, otherwise returns the result produced by the
   * supplying function.
   *
   * @param supplier the supplying function that produces a value to be returned
   * @return the value, if present, otherwise the result produced by the supplying function
   * @throws NullPointerException if no value is present and the supplying function is {@code null}
   */
  default @NonNull T orElseGet(@NonNull Supplier<? extends T> supplier) {
    return isPresent() ? Objects.requireNonNull(value()) : Objects.requireNonNull(supplier).get();
  }

  /**
   * If a value is present, returns the value, otherwise throws {@code NoSuchElementException}.
   *
   * @return the non-{@code null} value described by this {@code PicoType}
   * @throws NoSuchElementException if no value is present
   */
  default @NonNull T orElseThrow() {
    if (isEmpty()) {
      throw new NoSuchElementException("No value present");
    }
    return Objects.requireNonNull(value());
  }

  /**
   * If a value is present, returns the value, otherwise throws an exception produced by the
   * exception supplying function.
   *
   * @param <X> Type of the exception to be thrown
   * @param exceptionSupplier the supplying function that produces an exception to be thrown
   * @return the value, if present
   * @throws X if no value is present
   * @throws NullPointerException if no value is present and the exception supplying function is
   *     {@code null}
   */
  default <X extends Throwable> @NonNull T orElseThrow(
      @NonNull Supplier<? extends X> exceptionSupplier) throws X {
    if (isPresent()) {
      return Objects.requireNonNull(value());
    } else {
      throw Objects.requireNonNull(exceptionSupplier).get();
    }
  }
}
