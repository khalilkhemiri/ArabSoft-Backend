package com.basicauth.app.controller;

import com.basicauth.app.service.AllDemand;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/chef/{chefId}")
    public ResponseEntity<List<Object>> getDemandesByChefId(@PathVariable Long chefId) {
        List<Object> demandesByChef = demandeService.getDemandesByChefId(chefId);
        return ResponseEntity.ok(demandesByChef);
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

    @GetMapping("/reports/{id}")
    public ResponseEntity<Map<String, String>> generatepdf(@PathVariable Long id) throws FileNotFoundException, JRException {
        String message = demandeService.generateReport(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/par-mois")
    public Map<String, Long> getDemandesParMois() {
        return demandeService.getDemandesParMois();
    }
    @GetMapping("/par-mois-approuvez")
    public Map<String, Long> getDemandesParMoisApprouvez() {
        return demandeService.getDemandesParMoisApprouvez();
    }


    @PutMapping("/approuver")
    public ResponseEntity<?> approuverDemande(@RequestParam Long demandeId, @RequestParam String typeDemande) {
        try {
            demandeService.approuverDemande(demandeId, typeDemande);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Demande approuver avec succès");
            return ResponseEntity.ok(response);        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/rejeter")
    public ResponseEntity<?> rejeterDemande(@RequestParam Long demandeId, @RequestParam String typeDemande) {
        try {
            demandeService.rejeterDemande(demandeId, typeDemande);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Demande rejetée avec succès");
            return ResponseEntity.ok(response);        }
        catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
