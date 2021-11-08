package ch.hearc.cafheg.business.allocations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.hearc.cafheg.business.common.Montant;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;


class AllocationServiceTest {

  private AllocationService allocationService;

  private AllocataireMapper allocataireMapper;
  private AllocationMapper allocationMapper;
  private SituationFamille sf;

  @BeforeEach
  void setUp() {
    allocataireMapper = Mockito.mock(AllocataireMapper.class);
    allocationMapper = Mockito.mock(AllocationMapper.class);
    allocationService = new AllocationService(allocataireMapper, allocationMapper);
    sf = SituationFamille.empty();
  }

  // find --------------------------------------------------------------------------------------------------------------
  @Test
  void findAllAllocataires_GivenEmptyAllocataires_ShouldBeEmpty() {
    Mockito.when(allocataireMapper.findAll("Geiser")).thenReturn(Collections.emptyList());
    List<Allocataire> all = allocationService.findAllAllocataires("Geiser");
    assertThat(all).isEmpty();
  }

  @Test
  void findAllocataireByNoAVS_GivenNoAVS_ShouldBeAllocataireWithThisNoAVS() {
    String noAVS = "756.4606.7224.18";
    Allocataire allocataire = new Allocataire(new NoAVS(noAVS), "Sayer", "Mercedez");
    Mockito.when(allocataireMapper.findByNoAVS(noAVS)).thenReturn(allocataire);
    assertEquals(allocationService.findAllocataireByNoAVS(noAVS), allocataire);
  }

  @Test
  void findAllAllocataires_Given2Geiser_ShouldBe2() {
    Mockito.when(allocataireMapper.findAll("Geiser"))
        .thenReturn(Arrays.asList(new Allocataire(new NoAVS("1000-2000"), "Geiser", "Arnaud"),
            new Allocataire(new NoAVS("1000-2001"), "Geiser", "Aurélie")));
    List<Allocataire> all = allocationService.findAllAllocataires("Geiser");
    assertAll(() -> assertThat(all.size()).isEqualTo(2),
        () -> assertThat(all.get(0).getNoAVS()).isEqualTo(new NoAVS("1000-2000")),
        () -> assertThat(all.get(0).getNom()).isEqualTo("Geiser"),
        () -> assertThat(all.get(0).getPrenom()).isEqualTo("Arnaud"),
        () -> assertThat(all.get(1).getNoAVS()).isEqualTo(new NoAVS("1000-2001")),
        () -> assertThat(all.get(1).getNom()).isEqualTo("Geiser"),
        () -> assertThat(all.get(1).getPrenom()).isEqualTo("Aurélie"));
  }

  @Test
  void findAllocationsActuelles() {
    Mockito.when(allocationMapper.findAll())
        .thenReturn(Arrays.asList(new Allocation(new Montant(new BigDecimal(1000)), Canton.NE,
            LocalDate.now(), null), new Allocation(new Montant(new BigDecimal(2000)), Canton.FR,
            LocalDate.now(), null)));
    List<Allocation> all = allocationService.findAllocationsActuelles();
    assertAll(() -> assertThat(all.size()).isEqualTo(2),
        () -> assertThat(all.get(0).getMontant()).isEqualTo(new Montant(new BigDecimal(1000))),
        () -> assertThat(all.get(0).getCanton()).isEqualTo(Canton.NE),
        () -> assertThat(all.get(0).getDebut()).isEqualTo(LocalDate.now()),
        () -> assertThat(all.get(0).getFin()).isNull(),
        () -> assertThat(all.get(1).getMontant()).isEqualTo(new Montant(new BigDecimal(2000))),
        () -> assertThat(all.get(1).getCanton()).isEqualTo(Canton.FR),
        () -> assertThat(all.get(1).getDebut()).isEqualTo(LocalDate.now()),
        () -> assertThat(all.get(1).getFin()).isNull());
  }

  // delete ------------------------------------------------------------------------------------------------------------
  @Test
  void deleteAllocataireWithoutVersement_GivenNoAVS_ShouldAllocataireBeDeleted() {
    String noAVS = "756.4606.7224.18";
    Allocataire allocataire = new Allocataire(new NoAVS(noAVS), "Sayer", "Mercedez");
    Mockito.when(allocataireMapper.allocataireHasVersement(noAVS)).thenReturn(false);
    Mockito.when(allocataireMapper.findByNoAVS(noAVS)).thenReturn(allocataire);
    boolean resultat = allocationService.deleteAllocataire(noAVS);
    Mockito.verify(allocataireMapper, Mockito.times(1)).deleteByNoAVS(noAVS);
    assertEquals(true, resultat);
    assertEquals(allocataireMapper.findByNoAVS(noAVS), allocataire);
  }

  @Test
  void deleteAllocataireWithVersement_GivenNoInvocation() {
    String noAVS = "756.6404.3020.57";
    Mockito.when(allocataireMapper.allocataireHasVersement(noAVS)).thenReturn(true);
    boolean resultat = allocationService.deleteAllocataire(noAVS);
    Mockito.verify(allocataireMapper, Mockito.times(0)).deleteByNoAVS(noAVS);
    assertEquals(false, resultat);
  }

  // update ------------------------------------------------------------------------------------------------------------
  @Test
  void updateNomOuPrenomChanged_GivenNoAVSPrenom_ShouldPrenomBeUpdated() {
    String noAVS = "756.4284.5291.11";
    String nom = "Maclin";
    String newPrenom = "Tom";
    Allocataire allocataire = new Allocataire(new NoAVS(noAVS), nom, newPrenom);
    Mockito.when(allocataireMapper.nomOuPrenomChanged(noAVS, newPrenom, nom)).thenReturn(true);
    Mockito.when(allocataireMapper.findByNoAVS(noAVS)).thenReturn(allocataire);
    boolean resultat = allocationService.updateNomOuPrenom(noAVS, newPrenom, nom);
    Mockito.verify(allocataireMapper, Mockito.times(1)).updateNomOuPrenom(noAVS, newPrenom, nom);
    assertEquals(true, resultat);
    assertEquals(allocataireMapper.findByNoAVS(noAVS), allocataire);
  }

  @Test
  void updateNomOuPrenomChanged_GivenNoAVSNom_ShouldNomBeUpdated() {
    String noAVS = "756.4284.5291.11";
    String newNom = "Washington";
    String prenom = "Tien";
    Allocataire allocataire = new Allocataire(new NoAVS(noAVS), newNom, prenom);
    Mockito.when(allocataireMapper.nomOuPrenomChanged(noAVS, prenom, newNom)).thenReturn(true);
    Mockito.when(allocataireMapper.findByNoAVS(noAVS)).thenReturn(allocataire);
    boolean resultat = allocationService.updateNomOuPrenom(noAVS, prenom, newNom);
    Mockito.verify(allocataireMapper, Mockito.times(1)).updateNomOuPrenom(noAVS, prenom, newNom);
    assertEquals(true, resultat);
    assertEquals(allocataireMapper.findByNoAVS(noAVS), allocataire);
  }

  @Test
  void updateNomOuPrenomChanged_GivenNoAVSPrenomNom_ShouldPrenomNomBeUpdated() {
    String noAVS = "756.4284.5291.11";
    String newNom = "Washington";
    String newPrenom = "Tom";
    Allocataire allocataire = new Allocataire(new NoAVS(noAVS), newNom, newPrenom);
    Mockito.when(allocataireMapper.nomOuPrenomChanged(noAVS, newPrenom, newNom)).thenReturn(true);
    Mockito.when(allocataireMapper.findByNoAVS(noAVS)).thenReturn(allocataire);
    boolean resultat = allocationService.updateNomOuPrenom(noAVS, newPrenom, newNom);
    Mockito.verify(allocataireMapper, Mockito.times(1)).updateNomOuPrenom(noAVS, newPrenom, newNom);
    assertEquals(true, resultat);
    assertEquals(allocataireMapper.findByNoAVS(noAVS), allocataire);
  }

  @Test
  void updateNomOuPrenomChanged_GivenNoAVSPrenomNom_ShouldPrenomNomNotBeUpdated() {
    String noAVS = "756.4284.5291.11";
    String nom = "Maclin";
    String prenom = "Tien";
    Mockito.when(allocataireMapper.nomOuPrenomChanged(noAVS, prenom, nom)).thenReturn(false);
    boolean resultat = allocationService.updateNomOuPrenom(noAVS, prenom, nom);
    Mockito.verify(allocataireMapper, Mockito.times(0)).updateNomOuPrenom(noAVS, prenom, nom);
    assertEquals(false, resultat);
  }

  // getParentDroitAllocation-------------------------------------------------------------------------------------------
  @ParameterizedTest
  @CsvSource({
      "false, false, none",
      "true, false, Parent1",
      "false, true, Parent2"
  })
  void GetParentDroitAllocationGivenDifferentParentWorkSitutation_shouldBeExceptionOrAParent(
      boolean parent1ActiviteLucrative, boolean parent2ActiviteLucrative, String result) {
    AllocataireMapper allocataireMapper = new AllocataireMapper();
    AllocationService allocationService = new AllocationService(allocataireMapper,
        new AllocationMapper());

    sf = new SituationFamille(
        sf.getEnfantResidence(), sf.getEnfantCantonResidence(), sf.getParent1Residence(),
        sf.getParent2Residence(),
        sf.getParent1CantonTravail(), sf.getParent2CantonTravail(), parent1ActiviteLucrative,
        parent2ActiviteLucrative,
        sf.isParentsEnsemble(), sf.getParent1Salaire(), sf.getParent2Salaire(),
        sf.isParent1AutoriteParentale(),
        sf.isParent2AutoriteParentale(), sf.isParent1Salarie(), sf.isParent2Salarie(),
        sf.isParent1Independant(),
        sf.isParent2Independant());

    assertEquals(result, allocationService.getParentDroitAllocation(sf));
  }

  @Test
  void GetParentDroitAllocationGivenOnlyParent1Autority_shouldBeParent1() {
    AllocataireMapper allocataireMapper = new AllocataireMapper();
    AllocationService allocationService = new AllocationService(allocataireMapper,
        new AllocationMapper());

    sf = new SituationFamille(
        sf.getEnfantResidence(), sf.getEnfantCantonResidence(), sf.getParent1Residence(),
        sf.getParent2Residence(),
        sf.getParent1CantonTravail(), sf.getParent2CantonTravail(), true, true,
        sf.isParentsEnsemble(), sf.getParent1Salaire(), sf.getParent2Salaire(), true,
        false, sf.isParent1Salarie(), sf.isParent2Salarie(), sf.isParent1Independant(),
        sf.isParent2Independant());

    assertEquals("Parent1", allocationService.getParentDroitAllocation(sf));
  }

  @Test
  void GetParentDroitAllocationGivenOnlyParent2Autority_shouldBeParent2() {
    AllocataireMapper allocataireMapper = new AllocataireMapper();
    AllocationService allocationService = new AllocationService(allocataireMapper,
        new AllocationMapper());

    sf = new SituationFamille(
        sf.getEnfantResidence(), sf.getEnfantCantonResidence(), sf.getParent1Residence(),
        sf.getParent2Residence(),
        sf.getParent1CantonTravail(), sf.getParent2CantonTravail(), true, true,
        sf.isParentsEnsemble(), sf.getParent1Salaire(), sf.getParent2Salaire(), false,
        true, sf.isParent1Salarie(), sf.isParent2Salarie(), sf.isParent1Independant(),
        sf.isParent2Independant());

    assertEquals("Parent2", allocationService.getParentDroitAllocation(sf));
  }

  @Test
  void GetParentDroitAllocationGivenParentSeparateParent1LiveWith_shouldBeParent1() {
    AllocataireMapper allocataireMapper = new AllocataireMapper();
    AllocationService allocationService = new AllocationService(allocataireMapper,
        new AllocationMapper());

    sf = new SituationFamille(
        "Fribourg", sf.getEnfantCantonResidence(), "Fribourg", "Bienne",
        sf.getParent1CantonTravail(), sf.getParent2CantonTravail(), true, true,
        false, sf.getParent1Salaire(), sf.getParent2Salaire(), true,
        true, sf.isParent1Salarie(), sf.isParent2Salarie(), sf.isParent1Independant(),
        sf.isParent2Independant());

    assertEquals("Parent1", allocationService.getParentDroitAllocation(sf));
  }

  @Test
  void GetParentDroitAllocationGivenParentSeparateParent2LiveWith_shouldBeParent2() {
    AllocataireMapper allocataireMapper = new AllocataireMapper();
    AllocationService allocationService = new AllocationService(allocataireMapper,
        new AllocationMapper());

    sf = new SituationFamille(
        "Fribourg", sf.getEnfantCantonResidence(), "Bienne", "Fribourg",
        sf.getParent1CantonTravail(), sf.getParent2CantonTravail(), true, true,
        false, sf.getParent1Salaire(), sf.getParent2Salaire(), true,
        true, sf.isParent1Salarie(), sf.isParent2Salarie(), sf.isParent1Independant(),
        sf.isParent2Independant());

    assertEquals("Parent2", allocationService.getParentDroitAllocation(sf));
  }

  @Test
  void GetParentDroitAllocationGivenOnlyParent1WorkINCantonResidence_shouldBeParent1() {
    AllocataireMapper allocataireMapper = new AllocataireMapper();
    AllocationService allocationService = new AllocationService(allocataireMapper,
        new AllocationMapper());

    sf = new SituationFamille(
        "Fribourg", Canton.FR, "Fribourg", "Bienne",
        Canton.FR, Canton.BE, true, true,
        true, sf.getParent1Salaire(), sf.getParent2Salaire(), true,
        true, sf.isParent1Salarie(), sf.isParent2Salarie(), sf.isParent1Independant(),
        sf.isParent2Independant());

    assertEquals("Parent1", allocationService.getParentDroitAllocation(sf));
  }

  @Test
  void GetParentDroitAllocationGivenOnlyParent2WorkINCantonResidence_shouldBeParent2() {
    AllocataireMapper allocataireMapper = new AllocataireMapper();
    AllocationService allocationService = new AllocationService(allocataireMapper,
        new AllocationMapper());

    sf = new SituationFamille(
        "Fribourg", Canton.FR, "Bienne", "Fribourg",
        Canton.BE, Canton.FR, true, true,
        true, sf.getParent1Salaire(), sf.getParent2Salaire(), true,
        true, sf.isParent1Salarie(), sf.isParent2Salarie(), sf.isParent1Independant(),
        sf.isParent2Independant());

    assertEquals("Parent2", allocationService.getParentDroitAllocation(sf));
  }

  @Test
  void GetParentDroitAllocationGivenParent1EmployeeParent2Independent_shouldBeParent1() {
    AllocataireMapper allocataireMapper = new AllocataireMapper();
    AllocationService allocationService = new AllocationService(allocataireMapper,
        new AllocationMapper());

    sf = new SituationFamille(
        "Fribourg", Canton.FR, "Fribourg", "Fribourg",
        Canton.FR, Canton.FR, true, true,
        true, sf.getParent1Salaire(), sf.getParent2Salaire(), true,
        true, true, false, false,
        true);

    assertEquals("Parent1", allocationService.getParentDroitAllocation(sf));
  }

  @Test
  void GetParentDroitAllocationGivenParent1IndependentParent2Employee_shouldBeParent2() {
    AllocataireMapper allocataireMapper = new AllocataireMapper();
    AllocationService allocationService = new AllocationService(allocataireMapper,
        new AllocationMapper());

    sf = new SituationFamille(
        "Fribourg", Canton.FR, "Fribourg", "Fribourg",
        Canton.FR, Canton.FR, true, true,
        true, sf.getParent1Salaire(), sf.getParent2Salaire(), true,
        true, false, true, true,
        false);

    assertEquals("Parent2", allocationService.getParentDroitAllocation(sf));
  }

  @Test
  void GetParentDroitAllocationGivenParentsEmployeeParent1BestSalary_shouldBeParent1() {
    AllocataireMapper allocataireMapper = new AllocataireMapper();
    AllocationService allocationService = new AllocationService(allocataireMapper,
        new AllocationMapper());

    sf = new SituationFamille(
        "Fribourg", Canton.FR, "Fribourg", "Fribourg",
        Canton.FR, Canton.FR, true, true,
        true, new BigDecimal(5000), new BigDecimal(4000), true,
        true, true, true, false,
        false);

    assertEquals("Parent1", allocationService.getParentDroitAllocation(sf));
  }

  @Test
  void GetParentDroitAllocationGivenParentsEmployeeParent2BestSalary_shouldBeParent2() {
    AllocataireMapper allocataireMapper = new AllocataireMapper();
    AllocationService allocationService = new AllocationService(allocataireMapper,
        new AllocationMapper());

    sf = new SituationFamille(
        "Fribourg", Canton.FR, "Fribourg", "Fribourg",
        Canton.FR, Canton.FR, true, true,
        true, new BigDecimal(4000), new BigDecimal(5000), true,
        true, true, true, false,
        false);

    assertEquals("Parent2", allocationService.getParentDroitAllocation(sf));
  }

  @Test
  void GetParentDroitAllocationGivenParentsIndependentParent1BestSalary_shouldBeParent1() {
    AllocataireMapper allocataireMapper = new AllocataireMapper();
    AllocationService allocationService = new AllocationService(allocataireMapper,
        new AllocationMapper());

    sf = new SituationFamille(
        "Fribourg", Canton.FR, "Fribourg", "Fribourg",
        Canton.FR, Canton.FR, true, true,
        true, new BigDecimal(5000), new BigDecimal(4000), true,
        true, false, false, true,
        true);

    assertEquals("Parent1", allocationService.getParentDroitAllocation(sf));
  }

  @Test
  void GetParentDroitAllocationGivenParentsIndependentParent2BestSalary_shouldBeParent2() {
    AllocataireMapper allocataireMapper = new AllocataireMapper();
    AllocationService allocationService = new AllocationService(allocataireMapper,
        new AllocationMapper());

    sf = new SituationFamille(
        "Fribourg", Canton.FR, "Fribourg", "Fribourg",
        Canton.FR, Canton.FR, true, true,
        true, new BigDecimal(4000), new BigDecimal(5000), true,
        true, false, false, true,
        true);

    assertEquals("Parent2", allocationService.getParentDroitAllocation(sf));
  }
}
