package com.basicauth.app.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChangementSituationDTO {
    private Long id;
    private LocalDate dateDemande;
    private String nouvelleSituation;
    private String statut;
    private String type;
    private Long utilisateurId;
}
