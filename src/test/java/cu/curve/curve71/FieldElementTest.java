package cu.curve.curve71;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class
FieldElementTest {

  @Test
  void add() {
    Assertions.assertEquals(FieldElement.of(7), FieldElement.of(17).add(FieldElement.of(61)));
  }

  @Test
  void pow() {
    Assertions.assertEquals(FieldElement.of(1), FieldElement.of(2).pow(0));
    Assertions.assertEquals(FieldElement.of(2), FieldElement.of(2).pow(1));
    Assertions.assertEquals(FieldElement.of(4), FieldElement.of(2).pow(2));
    Assertions.assertEquals(FieldElement.of(8), FieldElement.of(2).pow(3));
    Assertions.assertEquals(FieldElement.of(16), FieldElement.of(2).pow(4));
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
