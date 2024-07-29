package com.basicauth.app.service;

import com.basicauth.app.repository.AutorisationRepo;
import com.basicauth.app.repository.CongeRepository;
import com.basicauth.app.repository.PretRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AllDemand {
    @Autowired
    private PretRepo pretRepository;
    @Autowired
    private CongeRepository congeRepository;
    @Autowired
    private AutorisationRepo autoRepository;

    public List<Object> getAllDemandes() {
        List<Object> allDemandes = new ArrayList<>();
        allDemandes.addAll(pretRepository.findAll());
        allDemandes.addAll(congeRepository.findAll());
        allDemandes.addAll(autoRepository.findAll());

        return allDemandes;
    }

    public List<Object> getDemandesDuJour() {
        LocalDate today = LocalDate.now();
        List<Object> demandesDuJour = new ArrayList<>();
        demandesDuJour.addAll(pretRepository.findByDateDemande(today));
        demandesDuJour.addAll(congeRepository.findByDateDemande(today));
        demandesDuJour.addAll(autoRepository.findByDateDemande(today));

        return demandesDuJour;
    }
    public List<Object> getNbrDemandesDuJour() {
        LocalDate today = LocalDate.now();
        List<Object> demandesDuJour = new ArrayList<>();
        demandesDuJour.addAll(pretRepository.findByDateDemande(today));
        demandesDuJour.addAll(congeRepository.findByDateDemande(today));
        demandesDuJour.addAll(autoRepository.findByDateDemande(today));

        return Collections.singletonList(demandesDuJour.stream().count());
    }


    public List<Object> getDemandesByUtilisateurId(Long utilisateurId) {
        List<Object> userDemandes = new ArrayList<>();
        userDemandes.addAll(pretRepository.findByUtilisateur_Id(utilisateurId));
        userDemandes.addAll(congeRepository.findByUtilisateur_Id(utilisateurId));
        userDemandes.addAll(autoRepository.findByUtilisateur_Id(utilisateurId));

        return userDemandes;
    }
}
