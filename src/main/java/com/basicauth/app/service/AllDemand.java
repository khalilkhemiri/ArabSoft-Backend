package com.basicauth.app.service;

import com.basicauth.app.entity.*;
import com.basicauth.app.enums.StatutDemande;
import com.basicauth.app.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
@Service
public class AllDemand {
    @Autowired
    private RegisterNewUserRepository userRepository;
    @Autowired
    private PretRepo pretRepository;
    @Autowired
    private CongeRepository congeRepository;
    @Autowired
    private AutorisationRepo autoRepository;
    @Autowired
    private DocumentRepo documentRepository;
    @Autowired
    private ChangementSituationRepo changementSituationRepo;
    public List<Object> getAllDemandes() {
        List<Object> allDemandes = new ArrayList<>();
        allDemandes.addAll(pretRepository.findAll());
        allDemandes.addAll(congeRepository.findAll());
        allDemandes.addAll(autoRepository.findAll());
        allDemandes.addAll(documentRepository.findAll());
        allDemandes.addAll(changementSituationRepo.findAll());

        return allDemandes;
    }

    public List<Object> getDemandesByChefId(Long chefId) {
        List<Object> demandesByChef = new ArrayList<>();
        demandesByChef.addAll(congeRepository.findByUtilisateur_ChefHierarchique_Id(chefId));
        demandesByChef.addAll(autoRepository.findByUtilisateur_ChefHierarchique_Id(chefId));
        return demandesByChef;
    }

    public List<Object> getDemandesDuJour() {
        LocalDate today = LocalDate.now();
        List<Object> demandesDuJour = new ArrayList<>();
        demandesDuJour.addAll(pretRepository.findByDateDemande(today));
        demandesDuJour.addAll(congeRepository.findByDateDemande(today));
        demandesDuJour.addAll(autoRepository.findByDateDemande(today));
        demandesDuJour.addAll(documentRepository.findByDateDemande(today));
        demandesDuJour.addAll(changementSituationRepo.findByDateDemande(today));

        return demandesDuJour;
    }
    public List<Object> getNbrDemandesDuJour() {
        LocalDate today = LocalDate.now();
        List<Object> demandesDuJour = new ArrayList<>();
        demandesDuJour.addAll(pretRepository.findByDateDemande(today));
        demandesDuJour.addAll(congeRepository.findByDateDemande(today));
        demandesDuJour.addAll(autoRepository.findByDateDemande(today));
        demandesDuJour.addAll(documentRepository.findByDateDemande(today));
        demandesDuJour.addAll(changementSituationRepo.findByDateDemande(today));

        return Collections.singletonList(demandesDuJour.stream().count());
    }


    public List<Object> getDemandesByUtilisateurId(Long utilisateurId) {
        List<Object> userDemandes = new ArrayList<>();
        userDemandes.addAll(pretRepository.findByUtilisateur_Id(utilisateurId));
        userDemandes.addAll(congeRepository.findByUtilisateur_Id(utilisateurId));
        userDemandes.addAll(autoRepository.findByUtilisateur_Id(utilisateurId));
        userDemandes.addAll(documentRepository.findByUtilisateurId(utilisateurId));
        userDemandes.addAll(congeRepository.findByUtilisateur_Id(utilisateurId));

        return userDemandes;
    }


    public String generateReport(Long id) throws FileNotFoundException, JRException {
        String path = "C:\\khalil";

        Optional<UserProfile> optionalUserProfile = userRepository.findById(id);

        if (optionalUserProfile.isEmpty()) {
            throw new FileNotFoundException("User not found with ID: " + id);
        }

        UserProfile userProfile = optionalUserProfile.get();

        List<UserProfile> employeeList = new ArrayList<>();
        employeeList.add(userProfile);

        File file = ResourceUtils.getFile("classpath:Attestation.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(employeeList);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "My self");

        JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, datasource);
        JasperExportManager.exportReportToPdfFile(print, path + "\\arabsoft.pdf");

        return "PDF generation successful";
    }


    public Map<String, Long> getDemandesParMois() {
        int currentYear = LocalDate.now().getYear();
        Map<String, Long> demandesParMois = new HashMap<>();
        for (Month month : Month.values()) {
            LocalDate start = LocalDate.of(currentYear, month, 1);
            LocalDate end = start.with(TemporalAdjusters.lastDayOfMonth());

            long countPret = pretRepository.countByDateDemandeBetween(start, end);
            long countConge = congeRepository.countByDateDemandeBetween(start, end);
            long countAuto = autoRepository.countByDateDemandeBetween(start, end);
            long countDocument = documentRepository.countByDateDemandeBetween(start, end);
            long changementSituation = changementSituationRepo.countByDateDemandeBetween(start, end);

            long totalDemandes = countPret + countConge + countAuto + countDocument+changementSituation;
            demandesParMois.put(month.name(), totalDemandes);
        }
        return demandesParMois;
    }
    public Map<String, Long> getDemandesParMoisApprouvez() {
        int currentYear = LocalDate.now().getYear();
        Map<String, Long> demandesParMois = new HashMap<>();

        // Utilisez l'instance de l'enum au lieu de la chaîne
        StatutDemande statutApprouve = StatutDemande.APPROUVEE;

        for (Month month : Month.values()) {
            LocalDate start = LocalDate.of(currentYear, month, 1);
            LocalDate end = start.with(TemporalAdjusters.lastDayOfMonth());

            long countPret = pretRepository.countByDateDemandeBetweenAndStatut(start, end, statutApprouve);
            long countConge = congeRepository.countByDateDemandeBetweenAndStatut(start, end, statutApprouve);
            long countAuto = autoRepository.countByDateDemandeBetweenAndStatut(start, end, statutApprouve);
            long countDocument = documentRepository.countByDateDemandeBetweenAndStatut(start, end, statutApprouve);
            long changementSituation = changementSituationRepo.countByDateDemandeBetweenAndStatut(start, end, statutApprouve);

            long totalDemandes = countPret + countConge + countAuto + countDocument + changementSituation;
            demandesParMois.put(month.name(), totalDemandes);
        }

        return demandesParMois;
    }


    @Transactional
    public void approuverDemande(Long demandeId, String typeDemande) {
        switch (typeDemande.toLowerCase()) {
            case "pret":
                Pret pret = pretRepository.findById(demandeId).orElseThrow(() -> new RuntimeException("Demande non trouvée"));
                pret.setStatut(StatutDemande.APPROUVEE);
                pretRepository.save(pret);
                break;
            case "conge":
                Conge conge = congeRepository.findById(demandeId).orElseThrow(() -> new RuntimeException("Demande non trouvée"));
                conge.setStatut(StatutDemande.APPROUVEE);
                congeRepository.save(conge);
                break;
            case "autorisation":
                Autorisation autorisation = autoRepository.findById(demandeId).orElseThrow(() -> new RuntimeException("Demande non trouvée"));
                autorisation.setStatut(StatutDemande.APPROUVEE);
                autoRepository.save(autorisation);
                break;
            case "document":
                DocumentAdministratif document = documentRepository.findById(demandeId).orElseThrow(() -> new RuntimeException("Demande non trouvée"));
                document.setStatut(StatutDemande.APPROUVEE);
                documentRepository.save(document);
                break;
            case "changement_situation":
                ChangementSituation changementSituation = changementSituationRepo.findById(demandeId).orElseThrow(() -> new RuntimeException("Demande non trouvée"));
                changementSituation.setStatut(StatutDemande.APPROUVEE);
                changementSituationRepo.save(changementSituation);
                break;
            default:
                throw new RuntimeException("Type de demande non supporté");
        }
    }

    @Transactional
    public void rejeterDemande(Long demandeId, String typeDemande) {
        switch (typeDemande.toLowerCase()) {
            case "pret":
                Pret pret = pretRepository.findById(demandeId).orElseThrow(() -> new RuntimeException("Demande non trouvée"));
                pret.setStatut(StatutDemande.REJETEE);
                pretRepository.save(pret);
                break;
            case "conge":
                Conge conge = congeRepository.findById(demandeId).orElseThrow(() -> new RuntimeException("Demande non trouvée"));
                conge.setStatut(StatutDemande.REJETEE);
                congeRepository.save(conge);
                break;
            case "autorisation":
                Autorisation autorisation = autoRepository.findById(demandeId).orElseThrow(() -> new RuntimeException("Demande non trouvée"));
                autorisation.setStatut(StatutDemande.REJETEE);
                autoRepository.save(autorisation);
                break;
            case "document":
                DocumentAdministratif document = documentRepository.findById(demandeId).orElseThrow(() -> new RuntimeException("Demande non trouvée"));
                document.setStatut(StatutDemande.REJETEE);
                documentRepository.save(document);
                break;
            case "changement_situation":
                ChangementSituation changementSituation = changementSituationRepo.findById(demandeId).orElseThrow(() -> new RuntimeException("Demande non trouvée"));
                changementSituation.setStatut(StatutDemande.REJETEE);
                changementSituationRepo.save(changementSituation);
                break;
            default:
                throw new RuntimeException("Type de demande non supporté");
        }
    }
}
