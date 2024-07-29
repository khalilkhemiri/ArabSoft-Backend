package com.basicauth.app.controller;

import com.basicauth.app.entity.Autorisation;
import com.basicauth.app.service.AutorisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/autorisation")
@CrossOrigin(origins = "*")
public class AutorisationController {
    @Autowired
    private AutorisationService autorisationService;

    @PostMapping("/demande")
    public ResponseEntity<Autorisation> createConge(@RequestBody Autorisation autorisation) {
        Autorisation newAutorisation = autorisationService.createAutorisation(autorisation);
        return ResponseEntity.ok(newAutorisation);
    }
}
