package com.basicauth.app.repository;

import com.basicauth.app.entity.Autorisation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AutorisationRepo extends JpaRepository<Autorisation,Long> {
    List<Autorisation> findByDateDemande(LocalDate dateDemande);
    List<Autorisation> findByUtilisateur_Id(Long  userid);

}
