package ch.hearc.cafheg.business.common;

import java.math.BigDecimal;
import java.util.Objects;

public class Montant {

  public final BigDecimal value;

  public Montant(BigDecimal value) {
    this.value = value;
  }

  public BigDecimal getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Montant)) {
      return false;
    }
    Montant montant = (Montant) o;
    return getValue().equals(montant.getValue());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getValue());
  }
}
