package fr.perso.exemple.batchexemplesqltosql.model.dto;

import java.math.BigDecimal;

public class GarantieProduitDto {

    private Long id;
    private String code;
    private String libelle;
    private String description;
    private BigDecimal tauxFraisGestion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getTauxFraisGestion() {
        return tauxFraisGestion;
    }

    public void setTauxFraisGestion(BigDecimal tauxFraisGestion) {
        this.tauxFraisGestion = tauxFraisGestion;
    }
}