package com.basicauth.app.controller;

import com.basicauth.app.dto.ChangementSituationDTO;
import com.basicauth.app.dto.CongeDTO;
import com.basicauth.app.entity.ChangementSituation;
import com.basicauth.app.entity.Conge;
import com.basicauth.app.entity.UserProfile;
import com.basicauth.app.enums.StatutDemande;
import com.basicauth.app.enums.TypeDemande;
import com.basicauth.app.repository.ChangementSituationRepo;
import com.basicauth.app.repository.RegisterNewUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/situation")
@CrossOrigin(origins = "*")
public class ChangementSituationController {

@Autowired
    ChangementSituationRepo situation;
    @Autowired
    RegisterNewUserRepository repoUser;


    @PostMapping("/demande")
    public ResponseEntity<Map<String, String>> createConge(@RequestBody ChangementSituationDTO ChangementSituationDTO) {
        // Convert DTO to entity
        ChangementSituation conge = new ChangementSituation();
        conge.setDateDemande(LocalDate.now());
        conge.setNouvelleSituation(ChangementSituationDTO.getNouvelleSituation());
        conge.setStatut(StatutDemande.EN_ATTENTE);
        conge.setType(TypeDemande.ChangementSituation);

        // Fetch the UserProfile by ID
        UserProfile utilisateur = repoUser.findById(ChangementSituationDTO.getUtilisateurId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        conge.setUtilisateur(utilisateur);

        // Save the conge entity
        situation.save(conge);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Demande de ChangementSituation envoyée avec succès!");

        return ResponseEntity.ok(response);
    }



}
