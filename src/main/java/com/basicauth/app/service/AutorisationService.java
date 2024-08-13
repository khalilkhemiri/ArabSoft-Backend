package com.basicauth.app.service;

import com.basicauth.app.entity.Autorisation;
import com.basicauth.app.enums.StatutDemande;
import com.basicauth.app.enums.TypeDemande;
import com.basicauth.app.repository.AutorisationRepo;
import com.basicauth.app.repository.CongeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AutorisationService {
    @Autowired
    private AutorisationRepo autorepo;

    public Autorisation createAutorisation(Autorisation autorisation) {
        autorisation.setDateDemande(LocalDate.now());
        autorisation.setStatut(StatutDemande.EN_ATTENTE);
        autorisation.setType(TypeDemande.AUTORISATION);
        return autorepo.save(autorisation);
    }
    public Autorisation save(Autorisation autorisation) {
        return autorepo.save(autorisation);
    }
}
