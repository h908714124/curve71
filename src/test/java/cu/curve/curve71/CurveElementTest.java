package cu.curve.curve71;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CurveElementTest {

  private Set<CurveElement> group;

  @BeforeEach
  void init() {
    Set<CurveElement> set = new LinkedHashSet<>();
    set.add(CurveElement.infinity());
    for (int i = 0; i < FieldElement.P; i++) {
      for (int j = 0; j < FieldElement.P; j++) {
        FieldElement x = FieldElement.of(i);
        FieldElement y = FieldElement.of(j);
        if (CurveElement.isOnCurve(x, y)) {
          CurveElement element = CurveElement.of(i, j);
          set.add(element);
        }
      }
    }
    this.group = unmodifiableSet(set);
  }

  @Test
  void order() {
    for (CurveElement element : group) {
      assertTrue(element.order() <= 36);
    }
  }

  @Test
  void inGroup() {
    for (CurveElement element : group) {
      CurveElement t = element;
      while (!t.isInfinity()) {
        assertTrue(group.contains(t));
        assertTrue(CurveElement.isOnCurve(t.getXCoord(), t.getYCoord()));
        t = t.add(element).normalize();
      }
    }
  }

  @Test
  void numberOfElements() {
    assertEquals(72, group.size());
  }
}
