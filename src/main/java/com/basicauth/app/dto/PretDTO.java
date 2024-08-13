package com.basicauth.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)public class PretDTO {
    private Long id;
    private String type;
    private double montant;
    private LocalDate dateDemande;
    private String statut;
    private Long utilisateurId;
}