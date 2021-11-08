package ch.hearc.cafheg.infrastructure.api;

import ch.hearc.cafheg.business.allocations.Allocataire;
import ch.hearc.cafheg.business.allocations.Allocation;
import ch.hearc.cafheg.business.allocations.AllocationService;
import ch.hearc.cafheg.business.allocations.SituationFamille;
import ch.hearc.cafheg.business.versements.VersementService;
import ch.hearc.cafheg.infrastructure.pdf.PDFExporter;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;
import ch.hearc.cafheg.infrastructure.persistance.EnfantMapper;
import ch.hearc.cafheg.infrastructure.persistance.VersementMapper;
import ch.hearc.cafheg.infrastructure.transaction.AllocationServiceTransactional;
import ch.hearc.cafheg.infrastructure.transaction.VersementServiceTransactional;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RESTController {

  private final AllocationServiceTransactional allocationServiceTransactional;
  private final VersementServiceTransactional versementServiceTransactional;

  public RESTController() {
    this.allocationServiceTransactional = new AllocationServiceTransactional(
        new AllocationService(new AllocataireMapper(), new AllocationMapper()));
    this.versementServiceTransactional = new VersementServiceTransactional(
        new VersementService(new VersementMapper(), new AllocataireMapper(),
            new PDFExporter(new EnfantMapper())));
  }

  @PostMapping("/droits/quel-parent")
  public String getParentDroitAllocation(@RequestBody SituationFamille sf) {
    return allocationServiceTransactional.getParentDroitAllocation(sf);
  }

  @DeleteMapping("/allocataires/{noAVS}")
  public boolean deleteALlocataire(@PathVariable("noAVS") String noAVS) {
    return allocationServiceTransactional.deleteAllocataire(noAVS);
  }

  @PostMapping("/allocataires/{noAVS}/update")
  public boolean updateNomOuPrenomAllocataire(@RequestBody Allocataire allocataire,
      @PathVariable("noAVS") String noAVS) {
    return allocationServiceTransactional.updateNomOuPrenom(noAVS, allocataire.getPrenom(),
        allocataire.getNom());
  }

  @GetMapping("/allocataires")
  public List<Allocataire> allocataires(
      @RequestParam(value = "startsWith", required = false) String start) {
    return allocationServiceTransactional.findAllAllocataires(start);
  }

  @GetMapping("/allocations")
  public List<Allocation> allocations() {
    return allocationServiceTransactional.findAllocationsActuelles();
  }

  @GetMapping("/allocations/{year}/somme")
  public BigDecimal sommeAs(@PathVariable("year") int year) {
    return versementServiceTransactional.findSommeAllocationParAnnee(year).getValue();
  }

  @GetMapping("/allocations-naissances/{year}/somme")
  public BigDecimal sommeAns(@PathVariable("year") int year) {
    return versementServiceTransactional.findSommeAllocationNaissanceParAnnee(year).getValue();
  }

  @GetMapping(value = "/allocataires/{allocataireId}/allocations", produces = MediaType.APPLICATION_PDF_VALUE)
  public byte[] pdfAllocations(@PathVariable("allocataireId") int allocataireId) {
    return versementServiceTransactional.exportPDFAllocataire(allocataireId);
  }

  @GetMapping(value = "/allocataires/{allocataireId}/versements", produces = MediaType.APPLICATION_PDF_VALUE)
  public byte[] pdfVersements(@PathVariable("allocataireId") int allocataireId) {
    return versementServiceTransactional.exportPDFVersements(allocataireId);
  }
}
