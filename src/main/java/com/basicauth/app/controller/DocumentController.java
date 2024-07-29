package com.basicauth.app.controller;


import com.basicauth.app.entity.Conge;
import com.basicauth.app.entity.DocumentAdministratif;
import com.basicauth.app.service.CongeService;
import com.basicauth.app.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/document")
@CrossOrigin(origins = "*")
public class DocumentController {
    @Autowired
    private DocumentService documentService;

    @PostMapping("/demande")
    public ResponseEntity<DocumentAdministratif> createdocument(@RequestBody DocumentAdministratif document) {
        DocumentAdministratif newDocument = documentService.createDocument(document);
        return ResponseEntity.ok(newDocument);
    }
}
