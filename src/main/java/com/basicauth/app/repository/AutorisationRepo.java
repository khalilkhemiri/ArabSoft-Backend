package com.basicauth.app.repository;

import com.basicauth.app.entity.Autorisation;
import com.basicauth.app.enums.StatutDemande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AutorisationRepo extends JpaRepository<Autorisation,Long> {
    List<Autorisation> findByDateDemande(LocalDate dateDemande);
    List<Autorisation> findByUtilisateur_Id(Long  userid);
    List<Autorisation> findByUtilisateur_ChefHierarchique_Id(Long chefId);

    long countByDateDemandeBetween(LocalDate start, LocalDate end);

    long countByDateDemandeBetweenAndStatut(LocalDate start, LocalDate end, StatutDemande statut);
}
