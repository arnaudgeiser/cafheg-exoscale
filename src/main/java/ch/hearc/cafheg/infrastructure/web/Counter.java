package ch.hearc.cafheg.infrastructure.web;

public class Counter {

  private int count;

  public Counter() {
    count = 0;
  }

  public int increment(int value) {
    return this.count += value;
  }

  @Override
  public String toString() {
    return Integer.toString(count);
  }
}
