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

import java.util.Arrays;

/**
 * Abstract wrapper for passwords.
 *
 * @see <a href="https://security.stackexchange.com/q/172576">Why should passwords be compared by
 *     means of a byte array?</a>
 */
public abstract class PasswordPicoType implements PicoType<byte[]>, SecurePicoType {
  private final byte[] value;

  /**
   * Default constructor
   *
   * @param value to wrap. Can be {@code null}
   */
  protected PasswordPicoType(byte[] value) {
    this.value = value == null ? null : Arrays.copyOf(value, value.length);
  }

  @Override
  public byte[] value() {
    return value == null ? null : Arrays.copyOf(value, value.length);
  }

  @Override
  public final boolean equals(Object o) {
    return (o instanceof PasswordPicoType picoType) && Arrays.equals(value, picoType.value);
  }

  @Override
  public final int hashCode() {
    return Arrays.hashCode(value);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "{value=" + mask() + '}';
  }
}
