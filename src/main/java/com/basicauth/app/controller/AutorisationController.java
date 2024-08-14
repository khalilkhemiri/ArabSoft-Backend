package com.basicauth.app.controller;

import com.basicauth.app.dto.AutorisationDTO;
import com.basicauth.app.entity.Autorisation;
import com.basicauth.app.entity.UserProfile;
import com.basicauth.app.enums.StatutDemande;
import com.basicauth.app.enums.TypeDemande;
import com.basicauth.app.repository.RegisterNewUserRepository;
import com.basicauth.app.service.AutorisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/autorisation")
@CrossOrigin(origins = "*")
public class AutorisationController {
    @Autowired
    private AutorisationService autorisationService;
    @Autowired
    private RegisterNewUserRepository userProfileRep;

    @PostMapping("/demande")
    public ResponseEntity<?> createAutorisation(@RequestBody AutorisationDTO autorisationDTO) {
        Autorisation autorisation = new Autorisation();
        autorisation.setDateDemande(LocalDate.now());
        autorisation.setMotif(autorisationDTO.getMotif());
        autorisation.setType(TypeDemande.AUTORISATION);
        autorisation.setDateDebut(autorisationDTO.getDateDebut());
        autorisation.setDateFin(autorisationDTO.getDateFin());
        autorisation.setStatut(StatutDemande.EN_ATTENTE);

        UserProfile utilisateur = userProfileRep.findById(autorisationDTO.getUtilisateurId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        autorisation.setUtilisateur(utilisateur);

        autorisationService.save(autorisation);

        return ResponseEntity.ok("Demande envoyée avec succès!");
    }
}
