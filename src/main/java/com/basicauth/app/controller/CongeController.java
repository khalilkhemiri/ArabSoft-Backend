package com.basicauth.app.controller;

import com.basicauth.app.dto.CongeDTO;
import com.basicauth.app.entity.Conge;
import com.basicauth.app.entity.UserProfile;
import com.basicauth.app.enums.StatutDemande;
import com.basicauth.app.enums.TypeDemande;
import com.basicauth.app.repository.RegisterNewUserRepository;
import com.basicauth.app.service.CongeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/conges")
@CrossOrigin(origins = "*")
public class CongeController {

    @Autowired
    private CongeService congeService;
    @Autowired
    RegisterNewUserRepository repoUser;
    @PostMapping("/demandes")
    public ResponseEntity<Conge> createConge(@RequestBody Conge conge) {
        Conge newConge = congeService.createConge(conge);
        return ResponseEntity.ok(newConge);
    }
    @PostMapping("/demande")
    public ResponseEntity<Map<String, String>> createConge(@RequestBody CongeDTO congeDTO) {
        Conge conge = new Conge();
        conge.setDateDemande(LocalDate.now());
        conge.setDateDebut(congeDTO.getDateDebut());
        conge.setDateFin(congeDTO.getDateFin());
        conge.setMotif(congeDTO.getMotif());
        conge.setStatut(StatutDemande.EN_ATTENTE);
        conge.setType(TypeDemande.CONGE);

        UserProfile utilisateur = repoUser.findById(congeDTO.getUtilisateurId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        conge.setUtilisateur(utilisateur);

        congeService.save(conge);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Demande de congé envoyée avec succès!");

        return ResponseEntity.ok(response);
    }

}
