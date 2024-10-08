package com.basicauth.app.entity;

import com.basicauth.app.enums.StatutDemande;
import com.basicauth.app.enums.TypeDemande;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
public class Pret {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private TypeDemande type;
    private double montant;
    private LocalDate dateDemande;
    @Enumerated(EnumType.STRING)
    private StatutDemande statut;
    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private UserProfile utilisateur;


}
