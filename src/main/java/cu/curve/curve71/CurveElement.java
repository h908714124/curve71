package cu.curve.curve71;

import java.util.Objects;

import static cu.curve.curve71.FieldElement.one;
import static cu.curve.curve71.FieldElement.zero;

public class CurveElement {

  private static final CurveElement INF = new CurveElement(zero(), one(), zero());

  private final FieldElement x;
  private final FieldElement y;
  private final FieldElement z;

  private CurveElement(FieldElement x, FieldElement y, FieldElement z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public static CurveElement of(int x, int y) {
    FieldElement x_ = FieldElement.of(x);
    FieldElement y_ = FieldElement.of(y);
    // The curve parameters are a = -1, b = 0
    // i.e. the curve is defined by y^2 = x^3 - x, with x and y from P_71
    // (Source: https://en.wikipedia.org/wiki/Elliptic_curve#Elliptic_curves_over_finite_fields)
    if (!isOnCurve(x_, y_)) {
      throw new IllegalArgumentException("not on curve");
    }
    return new CurveElement(x_, y_, one());
  }

  public static boolean isOnCurve(FieldElement x, FieldElement y) {
    return x.pow(3).subtract(x).equals(y.square());
  }

  // https://en.wikibooks.org/wiki/Cryptography/Prime_Curve/Jacobian_Coordinates#Point_Doubling_(4M_+_6S_or_4M_+_4S)
  public CurveElement twice() {
    if (isInfinity())
      return INF;
    FieldElement S = x.multiply(4).multiply(y.square());
    FieldElement M = x.square().multiply(3).add(z.pow(4).multiply(-1));
    FieldElement X_ = M.square().subtract(S.twice());
    FieldElement Y_ = M.multiply(S.subtract(X_)).subtract(y.pow(4).multiply(8));
    FieldElement Z_ = y.twice().multiply(z);
    return new CurveElement(X_, Y_, Z_);
  }

  // https://en.wikibooks.org/wiki/Cryptography/Prime_Curve/Jacobian_Coordinates#Point_Addition_(12M_+_4S)
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
      return S1.equals(S2) ? twice() : INF;
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

  public boolean isInfinity() {
    return z.isZero();
  }

  public static CurveElement infinity() {
    return INF;
  }

  @Override
  public String toString() {
    CurveElement n = normalize();
    return String.format("(%s:%s:%s)", n.x, n.y, n.z);
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
    return new CurveElement(X_, Y_, FieldElement.one());
  }

  public int order() {
    int n = 1;
    CurveElement t = this;
    while (!t.isInfinity()) {
      t = t.add(this);
      n++;
    }
    return n;
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

  public FieldElement getXCoord() {
    return x;
  }

  public FieldElement getYCoord() {
    return y;
  }

  public FieldElement getZCoord() {
    return z;
  }
}
