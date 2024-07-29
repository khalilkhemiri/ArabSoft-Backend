package com.basicauth.app.controller;

import com.basicauth.app.entity.Pret;
import com.basicauth.app.enums.StatutDemande;
import com.basicauth.app.service.PretService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/prets")
@CrossOrigin(origins = "*")
public class PretController {
    @Autowired
    private PretService pretService;

    @PostMapping
    public Pret createPret(@RequestBody Pret pret) {
        pret.setDateDemande(LocalDate.now());
        pret.setStatut(StatutDemande.EN_ATTENTE);
        return pretService.enregistrerPret(pret);
    }
}
