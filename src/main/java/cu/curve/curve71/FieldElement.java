package cu.curve.curve71;

import java.util.Objects;

// The prime field P_71
public class FieldElement {

  public static final int P = 71;

  private final int x;

  private static final FieldElement[] CACHE = new FieldElement[P];

  private FieldElement(int x) {
    while (x < 0) {
      x += P;
    }
    this.x = x % P;
  }

  static {
    for (int i = 0; i < P; i++) {
      CACHE[i] = new FieldElement(i);
    }
  }

  private static FieldElement at(int x) {
    while (x < 0) {
      x += P;
    }
    return CACHE[x % P];
  }

  public static FieldElement of(int val) {
    return at(val);
  }

  public FieldElement multiply(FieldElement other) {
    return at(this.x * other.x);
  }

  public FieldElement multiply(int other) {
    return at(this.x * other);
  }

  public FieldElement add(FieldElement other) {
    return at(this.x + other.x);
  }

  public FieldElement subtract(FieldElement other) {
    return at(this.x - other.x);
  }

  public FieldElement div(FieldElement other) {
    if (isZero()) {
      return at(0);
    }
    if (other.isOne()) {
      return this;
    }
    return this.multiply(other.inverse());
  }

  public boolean isOne() {
    return x == 1;
  }

  public boolean isZero() {
    return x == 0;
  }

  public FieldElement inverse() {
    if (isZero()) {
      throw new IllegalArgumentException("inverting zero");
    }
    if (isOne()) {
      return at(1);
    }
    for (int i = 2; i < P; i++) {
      if ((i * x) % P == 1) {
        return at(i);
      }
    }
    throw new AssertionError();
  }

  public FieldElement square() {
    return pow(2);
  }

  public FieldElement pow(int exp) {
    FieldElement res = one();
    for (int i = 0; i < exp; i++) {
      res = res.multiply(this);
    }
    return res;
  }

  public FieldElement twice() {
    return at(2 * x);
  }

  public static FieldElement zero() {
    return at(0);
  }

  public static FieldElement one() {
    return at(1);
  }

  @Override
  public String toString() {
    return Integer.toString(x);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FieldElement that = (FieldElement) o;
    return x == that.x;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x);
  }
}
