package com.basicauth.app.service;


import com.basicauth.app.entity.Conge;
import com.basicauth.app.enums.StatutDemande;
import com.basicauth.app.enums.TypeDemande;
import com.basicauth.app.repository.CongeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CongeService {

    @Autowired
    private CongeRepository congeRepository;

    public Conge createConge(Conge conge) {
        conge.setDateDemande(LocalDate.now());
        conge.setType(TypeDemande.CONGE);
        conge.setStatut(StatutDemande.EN_ATTENTE);
        return congeRepository.save(conge);
    }



}
