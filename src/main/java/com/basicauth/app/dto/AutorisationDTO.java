package com.basicauth.app.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AutorisationDTO {
    private Long id;
    private LocalDate dateDemande;
    private String motif;
    private String type;
    private Date dateDebut;
    private Date dateFin;
    private String statut;
    private Long utilisateurId;

}
