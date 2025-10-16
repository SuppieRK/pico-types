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

/** Abstract wrapper for {@link Double} type. */
public abstract class DoublePicoType implements PicoType<Double>, Comparable<DoublePicoType> {
  private final Double value;

  /**
   * Default constructor
   *
   * @param value to wrap. Can be {@code null}
   */
  protected DoublePicoType(Double value) {
    this.value = value;
  }

  @Override
  public Double value() {
    return value;
  }

  @Override
  public int compareTo(DoublePicoType o) {
    return Objects.requireNonNull(value(), "Cannot compare null value against another value")
        .compareTo(
            Objects.requireNonNull(
                Objects.requireNonNull(o).value(),
                "Cannot compare value against another null value"));
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    return ((value == null && ((DoublePicoType) o).value == null)
        || (value != null
            && ((DoublePicoType) o).value != null
            && Double.compare(value, ((DoublePicoType) o).value) == 0));
  }

  @Override
  public final int hashCode() {
    return Objects.hashCode(value);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "{value=" + value + '}';
  }
}
