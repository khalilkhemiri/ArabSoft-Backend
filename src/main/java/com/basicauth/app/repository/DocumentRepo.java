package com.basicauth.app.repository;

import com.basicauth.app.entity.Conge;
import com.basicauth.app.entity.DocumentAdministratif;
import com.basicauth.app.enums.StatutDemande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface DocumentRepo extends JpaRepository<DocumentAdministratif,Long> {
    List<DocumentAdministratif> findByDateDemande(LocalDate dateDemande);

    List<DocumentAdministratif> findByUtilisateurId(Long userid);

    List<DocumentAdministratif> findByUtilisateur_ChefHierarchique_Id(Long chefId);

    long countByDateDemandeBetween(LocalDate start, LocalDate end);

    long countByDateDemandeBetweenAndStatut(LocalDate start, LocalDate end, StatutDemande statut);
}
