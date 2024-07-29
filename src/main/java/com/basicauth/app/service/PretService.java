package com.basicauth.app.service;

import com.basicauth.app.entity.Pret;
import com.basicauth.app.repository.PretRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PretService {
    @Autowired
    private PretRepo pretRepository;

    public Pret enregistrerPret(Pret pret) {
        return pretRepository.save(pret);
    }


}
