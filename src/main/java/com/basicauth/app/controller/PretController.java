package com.basicauth.app.controller;

import com.basicauth.app.dto.PretDTO;
import com.basicauth.app.entity.Pret;
import com.basicauth.app.entity.UserProfile;
import com.basicauth.app.enums.StatutDemande;
import com.basicauth.app.enums.TypeDemande;
import com.basicauth.app.repository.RegisterNewUserRepository;
import com.basicauth.app.service.PretService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/prets")
@CrossOrigin(origins = "*")
public class PretController {
    @Autowired
    private PretService pretService;
    @Autowired
    RegisterNewUserRepository repoUser;
    public Pret createPret(@RequestBody Pret pret) {
        pret.setDateDemande(LocalDate.now());
        pret.setStatut(StatutDemande.EN_ATTENTE);
        return pretService.enregistrerPret(pret);
    }
    @PostMapping
    public ResponseEntity<?> createPret(@RequestBody PretDTO pretDTO) {
        Pret pret = new Pret();
        pret.setType(TypeDemande.valueOf(pretDTO.getType()));
        pret.setMontant(pretDTO.getMontant());
        pret.setDateDemande(LocalDate.now());
        pret.setStatut(StatutDemande.EN_ATTENTE);

        UserProfile utilisateur = repoUser.findById(pretDTO.getUtilisateurId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        pret.setUtilisateur(utilisateur);

        pretService.save(pret);

        return ResponseEntity.ok("Demande de prêt envoyée avec succès!");
    }
}
