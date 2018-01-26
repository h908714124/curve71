package cu.curve.curve71;

import static cu.curve.curve71.FieldElement.identity;
import static cu.curve.curve71.FieldElement.zero;

import java.util.Objects;

public class CurveElement {

  private static final FieldElement a = FieldElement.of(5);
  private static final FieldElement b = FieldElement.of(7);

  private static final CurveElement G = new CurveElement(a, b, identity());
  private static final CurveElement INF = new CurveElement(zero(), identity(), zero());

  private final FieldElement x;
  private final FieldElement y;
  private final FieldElement z;

  private CurveElement(FieldElement x, FieldElement y, FieldElement z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public static CurveElement of(int x, int y) {
    return new CurveElement(FieldElement.of(x), FieldElement.of(y), identity());
  }

  public static CurveElement of(int x, int y, int z) {
    return new CurveElement(FieldElement.of(x), FieldElement.of(y), FieldElement.of(z));
  }

  public CurveElement twice() {
    if (isInfinity())
      return INF;
    FieldElement S = x.multiply(4).multiply(y.square());
    FieldElement M = x.multiply(3).square().add(a.multiply(z.square().multiply(z.square())));
    FieldElement X_ = M.square().subtract(S.twice());
    FieldElement Y_ = M.multiply(S.subtract(X_)).subtract(y.square().multiply(y.square()).multiply(8));
    FieldElement Z_ = y.multiply(z).twice();
    return new CurveElement(X_, Y_, Z_);
  }

  public CurveElement add(CurveElement other) {

    FieldElement X1 = this.x;
    FieldElement X2 = other.x;
    FieldElement Y1 = this.y;
    FieldElement Y2 = other.y;
    FieldElement Z1 = this.z;
    FieldElement Z2 = other.z;

    FieldElement Z1Z1 = Z1.square();
    FieldElement Z2Z2 = Z2.square();

    FieldElement U1 = X1.multiply(Z2Z2);
    FieldElement U2 = X2.multiply(Z1Z1);


    FieldElement S1 = Y1.multiply(Z2).multiply(Z2Z2);
    FieldElement S2 = Y2.multiply(Z1).multiply(Z1Z1);

    if (U1.equals(U2)) {
      if (!S1.equals(S2)) {
        return INF;
      } else {
        return this.twice();
      }
    }

    FieldElement H = U2.subtract(U1);
    FieldElement I = H.twice().square();
    FieldElement J = H.multiply(I);
    FieldElement r = S2.subtract(S1).twice();
    FieldElement V = U1.multiply(I);

    FieldElement X3 = r.square().subtract(J).subtract(V.twice());
    FieldElement Y3 = r.multiply(V.subtract(X3)).subtract(S1.multiply(J).twice());
    FieldElement Z3 = Z1.add(Z2).square().subtract(Z1Z1).subtract(Z2Z2).multiply(H);

    return new CurveElement(X3, Y3, Z3);
  }

  public static CurveElement getG() {
    return G;
  }

  public boolean isInfinity() {
    return z.isZero();
  }

  public static CurveElement infinity() {
    return INF;
  }

  @Override
  public String toString() {
    return String.format("(%s:%s:%s)", x, y, z);
  }

  public CurveElement normalize() {
    if (z.isOne()) {
      return this;
    }
    if (isInfinity()) {
      return INF;
    }
    FieldElement U = z.inverse();
    FieldElement X_ = x.multiply(U.square());
    FieldElement Y_ = y.multiply(U.multiply(U).multiply(U));
    return new CurveElement(X_, Y_, FieldElement.identity());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CurveElement that = ((CurveElement) o).normalize();
    CurveElement n = this.normalize();
    return Objects.equals(n.x, that.x) &&
      Objects.equals(n.y, that.y);
  }

  @Override
  public int hashCode() {
    CurveElement n = this.normalize();
    return Objects.hash(n.x, n.y);
  }
}
