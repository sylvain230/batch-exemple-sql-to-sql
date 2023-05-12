package fr.perso.exemple.batchexemplesqltosql.model.dto;

public class BrancheDtoGarantieDto {

    private BrancheDto brancheDto;
    private GarantieDto garantieDto;

    public BrancheDto getBrancheDto() {
        return brancheDto;
    }

    public void setBrancheDto(BrancheDto brancheDto) {
        this.brancheDto = brancheDto;
    }

    public GarantieDto getGarantieDto() {
        return garantieDto;
    }

    public void setGarantieDto(GarantieDto garantieDto) {
        this.garantieDto = garantieDto;
    }
}
