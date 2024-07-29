package com.basicauth.app.controller;

import com.basicauth.app.entity.Conge;
import com.basicauth.app.service.CongeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/conges")
@CrossOrigin(origins = "*")
public class CongeController {

    @Autowired
    private CongeService congeService;

    @PostMapping("/demande")
    public ResponseEntity<Conge> createConge(@RequestBody Conge conge) {
        Conge newConge = congeService.createConge(conge);
        return ResponseEntity.ok(newConge);
    }
}
