package com.basicauth.app.entity;

import com.basicauth.app.enums.StatutDemande;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Pret {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double montant;
    private Date dateDemande;
    private StatutDemande statut;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private UserProfile utilisateur;

    @ManyToOne
    @JoinColumn(name = "chef_hierarchique_id")
    private UserProfile chefHierarchique;

    @ManyToOne
    @JoinColumn(name = "administrateur_id")
    private UserProfile administrateur;
}
