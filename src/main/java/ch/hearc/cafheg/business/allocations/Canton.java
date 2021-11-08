package ch.hearc.cafheg.business.allocations;

import java.util.stream.Stream;

public enum Canton {
  AG, AI, AR, BE, BL, BS, FR, GE, GL, GR, JU, LU, NE, NW, OW, SG, SH, SO, SZ, TG, TI, UR, VD, VS, ZG, ZH;

  public static Canton

  fromValue(String value) {
    return Stream.of(Canton.values())
        .filter(c -> c.name().equals(value))
        .findAny()
        .orElse(null);
  }


}
