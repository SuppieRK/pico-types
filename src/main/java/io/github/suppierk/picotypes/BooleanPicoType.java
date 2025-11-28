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

import java.util.Objects;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/** Abstract wrapper for {@link Boolean} type. */
public abstract class BooleanPicoType implements PicoType<Boolean>, Comparable<BooleanPicoType> {
  @Nullable private final Boolean value;

  /**
   * Default constructor
   *
   * @param value to wrap. Can be {@code null}
   */
  protected BooleanPicoType(@Nullable Boolean value) {
    this.value = value;
  }

  /** {@inheritDoc} */
  @Override
  public @Nullable Boolean value() {
    return value;
  }

  /** {@inheritDoc} */
  @Override
  public int compareTo(@NonNull BooleanPicoType o) {
    return Objects.requireNonNull(value(), "Cannot compare null value against another value")
        .compareTo(
            Objects.requireNonNull(
                Objects.requireNonNull(o).value(),
                "Cannot compare value against another null value"));
  }

  /**
   * Error Prone check suppressed - the intent here is that PicoTypes represent instances of
   * specific IDs which are not meant to be comparable between themselves.
   *
   * <p>{@inheritDoc}
   */
  @Override
  @SuppressWarnings("EqualsGetClass")
  public final boolean equals(@Nullable Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    return Objects.equals(value, ((BooleanPicoType) o).value);
  }

  /** {@inheritDoc} */
  @Override
  public final int hashCode() {
    return Objects.hashCode(value);
  }

  /** {@inheritDoc} */
  @Override
  public @NonNull String toString() {
    return getClass().getSimpleName() + "{value=" + value + '}';
  }
}
