package com.basicauth.app.repository;

import com.basicauth.app.entity.ChangementSituation;
import com.basicauth.app.entity.Conge;
import com.basicauth.app.enums.StatutDemande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface ChangementSituationRepo  extends JpaRepository<ChangementSituation,Long> {
    List<ChangementSituation> findByDateDemande(LocalDate dateDemande);
    List<ChangementSituation> findByUtilisateur_Id(Long  userid);
    List<ChangementSituation> findByUtilisateur_ChefHierarchique_Id(Long chefId);


    long countByDateDemandeBetween(LocalDate start, LocalDate end);

    long countByDateDemandeBetweenAndStatut(LocalDate start, LocalDate end, StatutDemande statut);
}
