package com.basicauth.app.controller;

import com.basicauth.app.service.AllDemand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/demandes")
@CrossOrigin(origins = "*")

public class DemandeController {

    @Autowired
    private AllDemand demandeService;

    @GetMapping("/all")
    public ResponseEntity<List<Object>> getAllDemandes() {
        List<Object> allDemandes = demandeService.getAllDemandes();
        return ResponseEntity.ok(allDemandes);
    }

    @GetMapping("/today")
    public ResponseEntity<List<Object>> getDemandesDuJour() {
        List<Object> demandesDuJour = demandeService.getDemandesDuJour();
        return ResponseEntity.ok(demandesDuJour);
    }
    @GetMapping("/todayNbr")
    public ResponseEntity<List<Object>> getNbrDemandesDuJour() {
        List<Object> demandesDuJour = demandeService.getNbrDemandesDuJour();
        return ResponseEntity.ok(demandesDuJour);
    }

    @GetMapping("/user/{id}")
    public List<Object> getDemandesByUtilisateurId(@PathVariable Long id) {
        return demandeService.getDemandesByUtilisateurId(id);
    }
}
