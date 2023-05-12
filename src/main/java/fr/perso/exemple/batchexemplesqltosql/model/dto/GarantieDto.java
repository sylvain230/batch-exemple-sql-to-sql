package fr.perso.exemple.batchexemplesqltosql.model.dto;

import java.math.BigDecimal;

public class GarantieDto {

    private Long id;
    private String code;
    private String libelle;
    private BigDecimal montantLCI;

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

    public BigDecimal getMontantLCI() {
        return montantLCI;
    }

    public void setMontantLCI(BigDecimal montantLCI) {
        this.montantLCI = montantLCI;
    }
}
