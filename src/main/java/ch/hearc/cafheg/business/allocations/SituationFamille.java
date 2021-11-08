package ch.hearc.cafheg.business.allocations;

import java.math.BigDecimal;
import java.util.Objects;

public final class SituationFamille {

  private final String enfantResidence;
  private final Canton enfantCantonResidence;
  private final String parent1Residence;
  private final String parent2Residence;
  private final Canton parent1CantonTravail;
  private final Canton parent2CantonTravail;
  private final boolean parent1ActiviteLucrative;
  private final boolean parent2ActiviteLucrative;
  private final boolean parentsEnsemble;
  private final BigDecimal parent1Salaire;
  private final BigDecimal parent2Salaire;
  private final boolean parent1AutoriteParentale;
  private final boolean parent2AutoriteParentale;
  private final boolean parent1Salarie;
  private final boolean parent2Salarie;
  private final boolean parent1Independant;
  private final boolean parent2Independant;

  public SituationFamille(String enfantResidence, Canton enfantCantonResidence,
      String parent1Residence,
      String parent2Residence, Canton parent1CantonTravail, Canton parent2CantonTravail,
      boolean parent1ActiviteLucrative, boolean parent2ActiviteLucrative,
      boolean parentsEnsemble, BigDecimal parent1Salaire, BigDecimal parent2Salaire,
      boolean parent1AutoriteParentale, boolean parent2AutoriteParentale,
      boolean parent1Salarie, boolean parent2Salarie,
      boolean parent1Independant, boolean parent2Independant) {
    this.enfantResidence = enfantResidence;
    this.enfantCantonResidence = enfantCantonResidence;
    this.parent1Residence = parent1Residence;
    this.parent2Residence = parent2Residence;
    this.parent1CantonTravail = parent1CantonTravail;
    this.parent2CantonTravail = parent2CantonTravail;
    this.parent1ActiviteLucrative = parent1ActiviteLucrative;
    this.parent2ActiviteLucrative = parent2ActiviteLucrative;
    this.parentsEnsemble = parentsEnsemble;
    this.parent1Salaire = parent1Salaire;
    this.parent2Salaire = parent2Salaire;
    this.parent1AutoriteParentale = parent1AutoriteParentale;
    this.parent2AutoriteParentale = parent2AutoriteParentale;
    this.parent1Salarie = parent1Salarie;
    this.parent2Salarie = parent2Salarie;
    this.parent1Independant = parent1Independant;
    this.parent2Independant = parent2Independant;
  }

  public static SituationFamille empty() {
    return new SituationFamille(null, null, null,
        null, null, null,
        false, false, false,
        null, null, false,
        false, false, false,
        false, false);
  }

  public String getEnfantResidence() {
    return enfantResidence;
  }

  public Canton getEnfantCantonResidence() {
    return enfantCantonResidence;
  }

  public Canton getParent1CantonTravail() {
    return parent1CantonTravail;
  }

  public Canton getParent2CantonTravail() {
    return parent2CantonTravail;
  }

  public boolean isParent1ActiviteLucrative() {
    return parent1ActiviteLucrative;
  }

  public String getParent1Residence() {
    return parent1Residence;
  }

  public boolean isParent2ActiviteLucrative() {
    return parent2ActiviteLucrative;
  }

  public String getParent2Residence() {
    return parent2Residence;
  }

  public boolean isParentsEnsemble() {
    return parentsEnsemble;
  }

  public BigDecimal getParent1Salaire() {
    return parent1Salaire;
  }

  public BigDecimal getParent2Salaire() {
    return parent2Salaire;
  }

  public boolean isParent1AutoriteParentale() {
    return parent1AutoriteParentale;
  }

  public boolean isParent2AutoriteParentale() {
    return parent2AutoriteParentale;
  }

  public boolean isParent1Salarie() {
    return parent1Salarie;
  }

  public boolean isParent2Salarie() {
    return parent2Salarie;
  }

  public boolean isParent1Independant() {
    return parent1Independant;
  }

  public boolean isParent2Independant() {
    return parent2Independant;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof SituationFamille)) {
      return false;
    }
    SituationFamille that = (SituationFamille) o;
    return parent1ActiviteLucrative == that.parent1ActiviteLucrative && parent2ActiviteLucrative ==
        that.parent2ActiviteLucrative && parentsEnsemble == that.parentsEnsemble
        && parent1AutoriteParentale ==
        that.parent1AutoriteParentale && parent2AutoriteParentale == that.parent2AutoriteParentale
        && parent1Salarie ==
        that.parent1Salarie && parent2Salarie == that.parent2Salarie && parent1Independant ==
        that.parent1Independant && parent2Independant ==
        that.parent2Independant && Objects.equals(enfantResidence, that.enfantResidence)
        && enfantCantonResidence ==
        that.enfantCantonResidence && Objects.equals(parent1Residence, that.parent1Residence)
        && Objects.equals(parent2Residence, that.parent2Residence) && parent1CantonTravail ==
        that.parent1CantonTravail && parent2CantonTravail ==
        that.parent2CantonTravail && Objects.equals(parent1Salaire, that.parent1Salaire)
        && Objects.equals(parent2Salaire, that.parent2Salaire);
  }

  @Override
  public int hashCode() {
    return Objects.hash(enfantResidence, enfantCantonResidence, parent1Residence, parent2Residence,
        parent1CantonTravail, parent2CantonTravail, parent1ActiviteLucrative,
        parent2ActiviteLucrative,
        parentsEnsemble, parent1Salaire, parent2Salaire, parent1AutoriteParentale,
        parent2AutoriteParentale,
        parent1Salarie, parent2Salarie, parent1Independant, parent2Independant);
  }
}
