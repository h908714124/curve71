package cu.curve.curve71;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FieldElementTest {

  @Test
  void add() {
    Assertions.assertEquals(FieldElement.of(7), FieldElement.of(17).add(FieldElement.of(61)));
  }

  @Test
  void sub() {
    Assertions.assertEquals(FieldElement.of(61), FieldElement.of(7).subtract(FieldElement.of(17)));
  }

  @Test
  void mul() {
    Assertions.assertEquals(FieldElement.of(1), FieldElement.of(36).multiply(FieldElement.of(2)));
  }

  @Test
  void div() {
    Assertions.assertEquals(FieldElement.of(36), FieldElement.of(1).div(FieldElement.of(2)));
  }
}
