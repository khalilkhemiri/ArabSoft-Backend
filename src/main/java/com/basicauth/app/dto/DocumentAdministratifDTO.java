package com.basicauth.app.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class DocumentAdministratifDTO {
    private Long id;
    private LocalDate dateDemande;
    private String motif;
    private String typeDocument;
    private String statut; 
    private String type; 
    private Long utilisateurId;
}
