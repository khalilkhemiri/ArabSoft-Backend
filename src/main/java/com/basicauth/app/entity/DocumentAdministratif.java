package com.basicauth.app.entity;

import com.basicauth.app.enums.StatutDemande;
import com.basicauth.app.enums.TypeDemande;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class DocumentAdministratif {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateDemande;
    private String motif;
    private String typeDocument;

    @Enumerated(EnumType.STRING)
    private StatutDemande statut;
    @Enumerated(EnumType.STRING)
    private TypeDemande type;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private UserProfile utilisateur;

}