package ch.hearc.cafheg.infrastructure.transaction;

import ch.hearc.cafheg.business.allocations.Allocataire;
import ch.hearc.cafheg.business.allocations.Allocation;
import ch.hearc.cafheg.business.allocations.AllocationService;
import ch.hearc.cafheg.business.allocations.SituationFamille;
import ch.hearc.cafheg.infrastructure.persistance.Database;
import java.util.List;

public class AllocationServiceTransactional {

  AllocationService allocationService;

  public AllocationServiceTransactional(AllocationService allocationService) {
    this.allocationService = allocationService;
  }

  public List<Allocataire> findAllAllocataires(String likeNom) {
    return Database.inTransaction(() -> allocationService.findAllAllocataires(likeNom));
  }

  public List<Allocation> findAllocationsActuelles() {
    return Database.inTransaction(() -> allocationService.findAllocationsActuelles());
  }

  public Allocataire findAllocataireByNoAVS(String noAVS) {
    return Database.inTransaction(() -> allocationService.findAllocataireByNoAVS(noAVS));
  }

  public boolean deleteAllocataire(String noAVS) {
    return Database.inTransaction(() -> allocationService.deleteAllocataire(noAVS));
  }

  public boolean updateNomOuPrenom(String noAVS, String prenom, String nom) {
    return Database.inTransaction(() -> allocationService.updateNomOuPrenom(noAVS, prenom, nom));
  }

  public String getParentDroitAllocation(SituationFamille sf) {
    return Database.inTransaction(() -> allocationService.getParentDroitAllocation(sf));
  }
}
