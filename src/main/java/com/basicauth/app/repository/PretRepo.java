package com.basicauth.app.repository;

import com.basicauth.app.entity.Conge;
import com.basicauth.app.entity.Pret;
import com.basicauth.app.enums.StatutDemande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PretRepo extends JpaRepository<Pret,Long> {
    List<Pret> findByDateDemande(LocalDate dateDemande);
    List<Pret> findByUtilisateur_Id( Long  userid);

    List<Pret> findByUtilisateur_ChefHierarchique_Id(Long chefId);

    long countByDateDemandeBetween(LocalDate start, LocalDate end);

    long countByDateDemandeBetweenAndStatut(LocalDate start, LocalDate end, StatutDemande statut);
}
