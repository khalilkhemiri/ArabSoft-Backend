package com.basicauth.app.service;

import com.basicauth.app.entity.Conge;
import com.basicauth.app.entity.DocumentAdministratif;
import com.basicauth.app.enums.StatutDemande;
import com.basicauth.app.enums.TypeDemande;
import com.basicauth.app.repository.CongeRepository;
import com.basicauth.app.repository.DocumentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DocumentService {
    @Autowired
    private DocumentRepo documentRepo;
    public DocumentAdministratif createDocument(DocumentAdministratif doc) {
        doc.setDateDemande(LocalDate.now());
        doc.setType(TypeDemande.DocumentAdministratif);
        doc.setStatut(StatutDemande.EN_ATTENTE);
        return documentRepo.save(doc);
    }
    public DocumentAdministratif save(DocumentAdministratif document) {
        return documentRepo.save(document);
    }
}
