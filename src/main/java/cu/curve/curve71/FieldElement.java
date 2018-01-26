package cu.curve.curve71;

import java.util.Objects;

public class FieldElement {

  public static final int P = 71;
  private static final FieldElement ZERO = FieldElement.of(0);
  private static final FieldElement ONE = FieldElement.of(1);

  private final int val;

  private FieldElement(int val) {
    while (val < 0) {
      val += P;
    }
    this.val = val % P;
  }

  public static FieldElement of(int val) {
    return new FieldElement(val);
  }

  public FieldElement multiply(FieldElement other) {
    return new FieldElement((this.val * other.val) % P);
  }

  public FieldElement multiply(int other) {
    return new FieldElement((this.val * other) % P);
  }

  public FieldElement add(FieldElement other) {
    return new FieldElement((this.val + other.val) % P);
  }

  public FieldElement subtract(FieldElement other) {
    return new FieldElement((this.val - other.val) % P);
  }

  public FieldElement div(FieldElement other) {
    if (isZero()) {
      return new FieldElement(0);
    }
    if (other.isOne()) {
      return this;
    }
    return this.multiply(other.inverse());
  }

  public boolean isOne() {
    return val == 1;
  }

  public boolean isZero() {
    return val == 0;
  }

  public FieldElement inverse() {
    if (isZero()) {
      throw new IllegalArgumentException("divide by zero");
    }
    if (isOne()) {
      return new FieldElement(1);
    }
    for (int i = 2; i < P; i++) {
      if ((i * val) % P == 1) {
        return new FieldElement(i);
      }
    }
    throw new AssertionError();
  }

  public FieldElement square() {
    return multiply(this);
  }

  public FieldElement twice() {
    return add(this);
  }

  public static FieldElement identity() {
    return ONE;
  }

  public static FieldElement zero() {
    return ZERO;
  }

  @Override
  public String toString() {
    return Integer.toString(val);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FieldElement that = (FieldElement) o;
    return val == that.val;
  }

  @Override
  public int hashCode() {
    return Objects.hash(val);
  }
}
