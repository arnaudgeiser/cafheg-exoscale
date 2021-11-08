package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AllocationService {

  private static final String PARENT_1 = "Parent1";
  private static final String PARENT_2 = "Parent2";
  private static final Logger logger = LoggerFactory.getLogger(AllocationService.class);

  private final AllocataireMapper allocataireMapper;
  private final AllocationMapper allocationMapper;

  public AllocationService(
      AllocataireMapper allocataireMapper,
      AllocationMapper allocationMapper) {
    this.allocataireMapper = allocataireMapper;
    this.allocationMapper = allocationMapper;
  }

  public List<Allocataire> findAllAllocataires(String likeNom) {
    logger.debug("Rechercher tous les allocataires");
    return allocataireMapper.findAll(likeNom);
  }

  public List<Allocation> findAllocationsActuelles() {
    return allocationMapper.findAll();
  }

  public Allocataire findAllocataireByNoAVS(String noAVS) {
    logger.debug("Rechercher l'allocataire " + noAVS);
    return allocataireMapper.findByNoAVS(noAVS);
  }

  public boolean deleteAllocataire(String noAVS) {
    if (!allocataireMapper.allocataireHasVersement(noAVS)
        && allocataireMapper.findByNoAVS(noAVS) != null) {
      logger.debug("Suppression de l'allocataire" + noAVS);
      allocataireMapper.deleteByNoAVS(noAVS);
      return true;
    } else {
      logger.info("Impossible de supprimer l'allocataire car il a des versements");
      return false;
    }
  }

  public boolean updateNomOuPrenom(String noAVS, String prenom, String nom) {
    if (allocataireMapper.nomOuPrenomChanged(noAVS, prenom, nom)
        && allocataireMapper.findByNoAVS(noAVS) != null) {
      logger.debug("Modification du nom et/ou prénom de l'allocataire" + noAVS);
      allocataireMapper.updateNomOuPrenom(noAVS, prenom, nom);
      return true;
    } else {
      logger.info("Aucune modification apportée car aucun changement");
      return false;
    }
  }

  public String getParentDroitAllocation(SituationFamille sf) {

    String parent = "none";

    logger.debug("Déterminer quel parent a le droit aux allocations");

    if (sf.isParent1ActiviteLucrative() != sf.isParent2ActiviteLucrative()) {
      parent = sf.isParent1ActiviteLucrative() ? PARENT_1 : PARENT_2;
    } else if (sf.isParent1ActiviteLucrative()) {
      if (sf.isParent1AutoriteParentale() != sf.isParent2AutoriteParentale()) {
        parent = sf.isParent1AutoriteParentale() ? PARENT_1 : PARENT_2;
      } else if (!sf.isParentsEnsemble()) {
        parent = sf.getEnfantResidence().contains(sf.getParent1Residence()) ? PARENT_1 : PARENT_2;
      } else if (sf.getEnfantCantonResidence() != sf.getParent1CantonTravail()
          || sf.getEnfantCantonResidence() != sf.getParent2CantonTravail()) {
        parent =
            sf.getEnfantCantonResidence() == sf.getParent1CantonTravail() ? PARENT_1 : PARENT_2;
      } else if (!(!sf.isParent1Salarie() || !sf.isParent2Salarie())
          || sf.isParent1Independant() && sf.isParent2Independant()) {
        parent =
            sf.getParent1Salaire().doubleValue() > sf.getParent2Salaire().doubleValue() ? PARENT_1
                : PARENT_2;
      } else if (sf.isParent1Salarie() && sf.isParent2Independant()) {
        parent = PARENT_1;
      } else if (sf.isParent1Independant() && sf.isParent2Salarie()) {
        parent = PARENT_2;
      }
    } else {
      logger.info("Cas non traité");
    }
    return parent;
  }
}
