package ch.hearc.cafheg.infrastructure.web;

import static ch.hearc.cafheg.infrastructure.persistance.Database.inTransaction;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import ch.hearc.cafheg.business.allocations.Allocataire;
import ch.hearc.cafheg.business.allocations.AllocationService;
import ch.hearc.cafheg.business.allocations.SituationFamille;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WEBController {

  private AllocataireMapper allocataireMapper;
  private AllocationMapper allocationMapper;
  private AllocationService allocationService;

  public WEBController() {
    allocataireMapper = new AllocataireMapper();
    allocationMapper = new AllocationMapper();
    allocationService = new AllocationService(allocataireMapper, allocationMapper);
  }

  @GetMapping("/web")
  public ModelAndView index() {
    ModelAndView mav = new ModelAndView("index");
    return mav;
  }

  @GetMapping("/web/allocataires")
  public ModelAndView allocataires() {
    ModelAndView mav = new ModelAndView("allocataires");
    mav.addObject("allocataires",
        inTransaction(() -> allocationService.findAllAllocataires("")));
    mav.addObject("counter", new Counter());
    return mav;
  }

  @GetMapping("/web/allocations")
  public ModelAndView allocations() {
    ModelAndView mav = new ModelAndView("allocations");
    mav.addObject("allocations",
        inTransaction(() -> allocationService.findAllocationsActuelles()));
    mav.addObject("counter", new Counter());
    return mav;
  }

  @GetMapping("/web/manage")
  public ModelAndView manageAllocataire(@RequestParam String noAVS,
      @RequestParam(required = false) String error) {
    ModelAndView mav = new ModelAndView(("manage"));
    Allocataire allocataire = inTransaction(() -> allocationService.findAllocataireByNoAVS(noAVS));
    mav.addObject("allocataire", allocataire);
    mav.addObject("error", error);
    return mav;
  }

  @GetMapping("/web/delete/{noAVS}")
  public String deleteAllocataire(@PathVariable("noAVS") String noAVS) {
    boolean succes = inTransaction(() -> allocationService.deleteAllocataire(noAVS));
    if (succes) {
      return "redirect:/web/allocataires";
    } else {
      return "redirect:/web/manage?noAVS=" + noAVS + "&error=versement";
    }
  }

  @RequestMapping(
      value = "/web/update/{noAVS}",
      method = POST,
      consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
  public String updateAllocataire(@PathVariable("noAVS") String noAVS, Allocataire allocataire) {
    inTransaction(
        () -> allocationService.updateNomOuPrenom(noAVS, allocataire.getPrenom(),
            allocataire.getNom()));
    return "redirect:/web/allocataires";
  }

  @GetMapping("/web/droits")
  public ModelAndView droitsAllocation(@RequestParam(required = false) String result) {
    ModelAndView mav = new ModelAndView(("droits"));
    SituationFamille sf = SituationFamille.empty();
    mav.addObject("sf", sf);
    mav.addObject("result", result);
    return mav;
  }

  @RequestMapping(
      value = "/web/droits/execute",
      method = POST,
      consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
  public String quelParent(SituationFamille sf) {
    String result = inTransaction(() -> allocationService.getParentDroitAllocation(sf));
    if (result.equals("Parent1")) {
      return "redirect:/web/droits?result=parent1";
    } else if (result.equals("Parent2")) {
      return "redirect:/web/droits?result=parent2";
    } else {
      return "redirect:/web/droits?result=error";
    }
  }
}
