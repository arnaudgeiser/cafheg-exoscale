package ch.hearc.cafheg.business.allocations;

import static org.junit.Assert.assertEquals;

import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.math.BigDecimal;
import org.mockito.Mockito;

public class AllocationServiceStepDefinition {

  private AllocationService allocationService;
  private AllocationMapper allocationMapper;
  private AllocataireMapper allocataireMapper;
  private SituationFamille sf;
  private String droit;

  @Before
  public void setUp() {
    sf = SituationFamille.empty();
  }

  @Given("Un seul parent a une activité lucrative {string} {string}")
  public void unSeulParentAUneActivitéLucrative(String parent1, String parent2) {
    sf = new SituationFamille(
        sf.getEnfantResidence(), sf.getEnfantCantonResidence(), sf.getParent1Residence(),
        sf.getParent2Residence(), sf.getParent1CantonTravail(), sf.getParent2CantonTravail(),
        Boolean.parseBoolean(parent1), Boolean.parseBoolean(parent2),
        sf.isParentsEnsemble(), sf.getParent1Salaire(), sf.getParent2Salaire(),
        sf.isParent1AutoriteParentale(),
        sf.isParent2AutoriteParentale(), sf.isParent1Salarie(), sf.isParent2Salarie(),
        sf.isParent1Independant(), sf.isParent2Independant());
  }

  @When("Je demande à qui revient le droit aux allocations")
  public void jeDemandeÀQuiRevientLeDroitAuxAllocations() {
    allocataireMapper = Mockito.mock(AllocataireMapper.class);
    allocationMapper = Mockito.mock(AllocationMapper.class);
    allocationService = new AllocationService(allocataireMapper, allocationMapper);
    droit = allocationService.getParentDroitAllocation(sf);
  }

  @Then("Le parent qui exerce une activité lucrative a le droit aux allocations {string}")
  public void leParentQuiExerceUneActivitéLucrativeALeDroitAuxAllocations(String expected) {
    assertEquals(expected, droit);
  }

  @Given("Les deux parents ont une activité lucrative")
  public void lesDeuxParentsOntUneActivitéLucrative() {
    sf = new SituationFamille(
        sf.getEnfantResidence(), sf.getEnfantCantonResidence(), sf.getParent1Residence(),
        sf.getParent2Residence(), sf.getParent1CantonTravail(), sf.getParent2CantonTravail(),
        true, true,
        sf.isParentsEnsemble(), sf.getParent1Salaire(), sf.getParent2Salaire(),
        sf.isParent1AutoriteParentale(),
        sf.isParent2AutoriteParentale(), sf.isParent1Salarie(), sf.isParent2Salarie(),
        sf.isParent1Independant(), sf.isParent2Independant());
  }

  @And("Un seul parent a l'autorité parentale {string} {string}")
  public void unSeulParentALAutoritéParentale(String parent1, String parent2) {
    sf = new SituationFamille(
        sf.getEnfantResidence(), sf.getEnfantCantonResidence(), sf.getParent1Residence(),
        sf.getParent2Residence(), sf.getParent1CantonTravail(), sf.getParent2CantonTravail(),
        sf.isParent1ActiviteLucrative(), sf.isParent2ActiviteLucrative(),
        sf.isParentsEnsemble(), sf.getParent1Salaire(), sf.getParent2Salaire(),
        Boolean.parseBoolean(parent1),
        Boolean.parseBoolean(parent2), sf.isParent1Salarie(), sf.isParent2Salarie(),
        sf.isParent1Independant(), sf.isParent2Independant());
  }

  @Then("Le parent qui a l'autorité parentale a le droit aux allocations {string}")
  public void leParentQuiALAutoritéParentaleALeDroitAuxAllocations(String expected) {
    assertEquals(expected, droit);
  }

  @And("Les deux parents ont l'autorité parentale")
  public void lesDeuxParentsOntLAutoritéParentale() {
    sf = new SituationFamille(
        sf.getEnfantResidence(), sf.getEnfantCantonResidence(), sf.getParent1Residence(),
        sf.getParent2Residence(), sf.getParent1CantonTravail(), sf.getParent2CantonTravail(),
        sf.isParent1ActiviteLucrative(), sf.isParent2ActiviteLucrative(),
        sf.isParentsEnsemble(), sf.getParent1Salaire(), sf.getParent2Salaire(), true,
        true, sf.isParent1Salarie(), sf.isParent2Salarie(), sf.isParent1Independant(),
        sf.isParent2Independant());
  }

  @And("Les parents vivent séparés {string} {string}")
  public void lesParentsViventSéparés(String parent1, String parent2) {
    sf = new SituationFamille(
        "Fribourg", sf.getEnfantCantonResidence(), parent1,
        parent2, sf.getParent1CantonTravail(), sf.getParent2CantonTravail(),
        sf.isParent1ActiviteLucrative(), sf.isParent2ActiviteLucrative(),
        sf.isParentsEnsemble(), sf.getParent1Salaire(), sf.getParent2Salaire(),
        sf.isParent1AutoriteParentale(),
        sf.isParent2AutoriteParentale(), sf.isParent1Salarie(), sf.isParent2Salarie(),
        sf.isParent1Independant(), sf.isParent2Independant());
  }

  @Then("Le parent qui vit avec l'enfant a le droit aux allocations {string}")
  public void leParentQuiVitAvecLEnfantALeDroitAuxAllocations(String expected) {
    assertEquals(expected, droit);
  }

  @And("Les parents vivent ensemble")
  public void lesParentsViventEnsemble() {
    sf = new SituationFamille(
        sf.getEnfantResidence(), sf.getEnfantCantonResidence(), sf.getParent1Residence(),
        sf.getParent2Residence(), sf.getParent1CantonTravail(), sf.getParent2CantonTravail(),
        sf.isParent1ActiviteLucrative(), sf.isParent2ActiviteLucrative(),
        true, sf.getParent1Salaire(), sf.getParent2Salaire(), sf.isParent1AutoriteParentale(),
        sf.isParent2AutoriteParentale(), sf.isParent1Salarie(), sf.isParent2Salarie(),
        sf.isParent1Independant(), sf.isParent2Independant());
  }

  @And("Un seul parent travaille dans le canton de domicile de l'enfant {string} {string}")
  public void unSeulParentTravailleDansLeCantonDeDomicileDeLEnfant(String parent1, String parent2) {
    sf = new SituationFamille(
        sf.getEnfantResidence(), Canton.FR, sf.getParent1Residence(),
        sf.getParent2Residence(), Canton.valueOf(parent1), Canton.valueOf(parent2),
        sf.isParent1ActiviteLucrative(), sf.isParent2ActiviteLucrative(),
        sf.isParentsEnsemble(), sf.getParent1Salaire(), sf.getParent2Salaire(),
        sf.isParent1AutoriteParentale(),
        sf.isParent2AutoriteParentale(), sf.isParent1Salarie(), sf.isParent2Salarie(),
        sf.isParent1Independant(), sf.isParent2Independant());
  }

  @Then("Le parent qui travaille dans le canton de domicile de l'enfant a le droit aux allocations {string}")
  public void leParentQuiTravailleDansLeCantonDeDomicileDeLEnfantALeDroitAuxAllocations(
      String expected) {
    assertEquals(expected, droit);
  }

  @And("Les parents travaillent dans le canton de domicile de l'enfant")
  public void lesParentsTravaillentDansLeCantonDeDomicileDeLEnfant() {
    sf = new SituationFamille(
        sf.getEnfantResidence(), Canton.FR, sf.getParent1Residence(),
        sf.getParent2Residence(), Canton.FR, Canton.FR,
        sf.isParent1ActiviteLucrative(), sf.isParent2ActiviteLucrative(),
        sf.isParentsEnsemble(), sf.getParent1Salaire(), sf.getParent2Salaire(),
        sf.isParent1AutoriteParentale(),
        sf.isParent2AutoriteParentale(), sf.isParent1Salarie(), sf.isParent2Salarie(),
        sf.isParent1Independant(), sf.isParent2Independant());
  }

  @And("Un parent est salarié et l'autre parent est indépendant {string} {string} {string} {string}")
  public void unParentEstSalariéEtLAutreParentEstIndépendant(String parent1Sal, String parent1Ind,
      String parent2Sal, String parent2Ind) {
    sf = new SituationFamille(
        sf.getEnfantResidence(), sf.getEnfantCantonResidence(), sf.getParent1Residence(),
        sf.getParent2Residence(), sf.getParent1CantonTravail(), sf.getParent2CantonTravail(),
        sf.isParent1ActiviteLucrative(), sf.isParent2ActiviteLucrative(),
        sf.isParentsEnsemble(), sf.getParent1Salaire(), sf.getParent2Salaire(),
        sf.isParent1AutoriteParentale(),
        sf.isParent2AutoriteParentale(), Boolean.parseBoolean(parent1Sal),
        Boolean.parseBoolean(parent2Sal),
        Boolean.parseBoolean(parent1Ind), Boolean.parseBoolean(parent2Ind));
  }

  @Then("Le parent qui est salarié a le droit aux allocations {string}")
  public void leParentQuiEstSalariéALeDroitAuxAllocations(String expected) {
    assertEquals(expected, droit);
  }

  @And("Les deux parents sont salariés {string} {string}")
  public void lesDeuxParentsSontSalariés(String parent1, String parent2) {
    sf = new SituationFamille(
        sf.getEnfantResidence(), sf.getEnfantCantonResidence(), sf.getParent1Residence(),
        sf.getParent2Residence(), sf.getParent1CantonTravail(), sf.getParent2CantonTravail(),
        sf.isParent1ActiviteLucrative(), sf.isParent2ActiviteLucrative(),
        sf.isParentsEnsemble(), new BigDecimal(parent1), new BigDecimal(parent2),
        sf.isParent1AutoriteParentale(),
        sf.isParent2AutoriteParentale(), true, true,
        sf.isParent1Independant(), sf.isParent2Independant());
  }

  @Then("Le parent avec le revenu soumis à l'AVS le plus élevé a le droit aux allocations {string}")
  public void leParentAvecLeRevenuSoumisÀLAVSLePlusÉlevéALeDroitAuxAllocations(String expected) {
    assertEquals(expected, droit);
  }

  @And("Les deux parents sont indépendants {string} {string}")
  public void lesDeuxParentsSontIndépendants(String parent1, String parent2) {
    sf = new SituationFamille(
        sf.getEnfantResidence(), sf.getEnfantCantonResidence(), sf.getParent1Residence(),
        sf.getParent2Residence(), sf.getParent1CantonTravail(), sf.getParent2CantonTravail(),
        sf.isParent1ActiviteLucrative(), sf.isParent2ActiviteLucrative(),
        sf.isParentsEnsemble(), new BigDecimal(parent1), new BigDecimal(parent2),
        sf.isParent1AutoriteParentale(),
        sf.isParent2AutoriteParentale(), sf.isParent1Salarie(), sf.isParent2Salarie(),
        true, true);
  }

  @Given("Aucun parent n'exerce une activité lucrative")
  public void aucunParentNExerceUneActivitéLucrative() {
    sf = new SituationFamille(
        sf.getEnfantResidence(), sf.getEnfantCantonResidence(), sf.getParent1Residence(),
        sf.getParent2Residence(), sf.getParent1CantonTravail(), sf.getParent2CantonTravail(),
        false, false,
        sf.isParentsEnsemble(), sf.getParent1Salaire(), sf.getParent2Salaire(),
        sf.isParent1AutoriteParentale(),
        sf.isParent2AutoriteParentale(), sf.isParent1Salarie(), sf.isParent2Salarie(),
        true, true);
  }

  @Then("Aucun des parents n'a le droit aux allocations")
  public void aucunDesParentsNALeDroitAuxAllocations() {
    assertEquals("none", droit);
  }
}
