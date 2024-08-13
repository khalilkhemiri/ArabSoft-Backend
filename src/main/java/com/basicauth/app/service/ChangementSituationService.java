package com.basicauth.app.service;

import com.basicauth.app.entity.ChangementSituation;
import com.basicauth.app.entity.Conge;
import com.basicauth.app.repository.ChangementSituationRepo;
import com.basicauth.app.repository.CongeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChangementSituationService {
    @Autowired
    private ChangementSituationRepo SituationRepository;


    public ChangementSituation save(ChangementSituation s) {
        return SituationRepository.save(s);
    }

}
