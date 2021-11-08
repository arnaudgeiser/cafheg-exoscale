package ch.hearc.cafheg.infrastructure.transaction;

import ch.hearc.cafheg.business.common.Montant;
import ch.hearc.cafheg.business.versements.VersementService;
import ch.hearc.cafheg.infrastructure.persistance.Database;

public class VersementServiceTransactional {

  VersementService versementService;

  public VersementServiceTransactional(VersementService versementService) {
    this.versementService = versementService;
  }

  public byte[] exportPDFVersements(long allocataireId) {
    return Database.inTransaction(() -> versementService.exportPDFVersements(allocataireId));
  }

  public Montant findSommeAllocationNaissanceParAnnee(int year) {
    return Database.inTransaction(
        () -> versementService.findSommeAllocationNaissanceParAnnee(year));
  }

  public Montant findSommeAllocationParAnnee(int year) {
    return Database.inTransaction(() -> versementService.findSommeAllocationParAnnee(year));
  }

  public byte[] exportPDFAllocataire(long allocataireId) {
    return Database.inTransaction(() -> versementService.exportPDFAllocataire(allocataireId));
  }
}
