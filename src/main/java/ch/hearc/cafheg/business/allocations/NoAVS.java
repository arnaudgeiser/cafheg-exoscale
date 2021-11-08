package ch.hearc.cafheg.business.allocations;

import java.util.Objects;

public class NoAVS {

  private final String value;

  public NoAVS(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof NoAVS)) {
      return false;
    }
    NoAVS noAVS = (NoAVS) o;
    return getValue().equals(noAVS.getValue());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getValue());
  }
}
