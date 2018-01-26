package cu.curve.curve71;

import static java.util.Collections.unmodifiableSet;

import java.util.LinkedHashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// https://stackoverflow.com/questions/8389324/how-to-calculate-point-addition-using-jacobian-coordinate-system-over-elliptic-c?rq=1
// http://www.hyperelliptic.org/EFD/g1p/auto-shortw-jacobian-0.html#addition-add-2007-bl
// https://en.wikibooks.org/wiki/Cryptography/Prime_Curve/Jacobian_Coordinates
// https://en.wikipedia.org/wiki/Elliptic_curve
class CurveElementTest {

  private Set<CurveElement> curve;

  @BeforeEach
  void init() {
    Set<CurveElement> curve = new LinkedHashSet<>();
    curve.add(CurveElement.infinity());
    for (int i = 0; i < FieldElement.P; i++) {
      for (int j = 0; j < FieldElement.P; j++) {
        FieldElement x = FieldElement.of(i);
        FieldElement y = FieldElement.of(j);
        if (x.multiply(x).multiply(x).subtract(x).equals(y.square())) {
          CurveElement element = CurveElement.of(i, j);
          curve.add(element);
        }
      }
    }
    this.curve = unmodifiableSet(curve);
  }

  @Test
  void getG() {
    for (CurveElement element : curve) {
      int order = order(element);
      System.out.println(element + ": " + order);
    }
  }

  private int order(CurveElement g) {
    int n = 0;
    CurveElement t = g;
    while (!t.isInfinity()) {
      t = t.add(g);
      if (!curve.contains(t)) {
        throw new IllegalArgumentException("t: " + t + ", g: " + g);
      }
      n++;
    }
    return n;
  }
}
