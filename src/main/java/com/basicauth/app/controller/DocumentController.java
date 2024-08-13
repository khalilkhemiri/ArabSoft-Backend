package com.basicauth.app.controller;


import com.basicauth.app.dto.DocumentAdministratifDTO;
import com.basicauth.app.entity.Conge;
import com.basicauth.app.entity.DocumentAdministratif;
import com.basicauth.app.entity.UserProfile;
import com.basicauth.app.enums.StatutDemande;
import com.basicauth.app.enums.TypeDemande;
import com.basicauth.app.repository.RegisterNewUserRepository;
import com.basicauth.app.service.CongeService;
import com.basicauth.app.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/document")
@CrossOrigin(origins = "*")
public class DocumentController {
    @Autowired
    private DocumentService documentService;
    @Autowired
private RegisterNewUserRepository userProfileRepository;
    @PostMapping("/demandes")
    public ResponseEntity<DocumentAdministratif> createdocuments(@RequestBody DocumentAdministratif document) {
        DocumentAdministratif newDocument = documentService.createDocument(document);
        return ResponseEntity.ok(newDocument);
    }


    @PostMapping("/demande")
    public ResponseEntity<Map<String, String>> createDocument(@RequestBody DocumentAdministratifDTO documentDTO) {
        // Convert DTO to entity
        DocumentAdministratif document = new DocumentAdministratif();
        document.setDateDemande(LocalDate.now());
        document.setMotif(documentDTO.getMotif());
        document.setTypeDocument(documentDTO.getTypeDocument());
        document.setStatut(StatutDemande.EN_ATTENTE);
        document.setType(TypeDemande.DocumentAdministratif);

        // Fetch the UserProfile by ID
        UserProfile utilisateur = userProfileRepository.findById(documentDTO.getUtilisateurId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        document.setUtilisateur(utilisateur);

        // Save the document entity
        documentService.save(document);

        // Return a JSON response
        Map<String, String> response = new HashMap<>();
        response.put("message", "Demande de document administratif envoyée avec succès!");
        return ResponseEntity.ok(response);
    }
}
