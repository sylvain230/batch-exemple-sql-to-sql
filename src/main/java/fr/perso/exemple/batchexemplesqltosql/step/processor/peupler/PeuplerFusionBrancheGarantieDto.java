package fr.perso.exemple.batchexemplesqltosql.step.processor.peupler;

import fr.perso.exemple.batchexemplesqltosql.model.dto.BrancheDtoGarantieDto;
import fr.perso.exemple.batchexemplesqltosql.model.dto.FusionBrancheGarantieDto;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class PeuplerFusionBrancheGarantieDto implements Function<BrancheDtoGarantieDto, FusionBrancheGarantieDto> {
    @Override
    public FusionBrancheGarantieDto apply(BrancheDtoGarantieDto brancheDtoGarantieDto) {
        FusionBrancheGarantieDto fusionBrancheGarantieDto = null;
        if(brancheDtoGarantieDto != null) {
            fusionBrancheGarantieDto = new FusionBrancheGarantieDto();
            fusionBrancheGarantieDto.setCode(brancheDtoGarantieDto.getBrancheDto().getCode() + " " + brancheDtoGarantieDto.getGarantieDto().getCode());
            fusionBrancheGarantieDto.setLibelle(brancheDtoGarantieDto.getBrancheDto().getLibelle() + " " + brancheDtoGarantieDto.getGarantieDto().getLibelle());

        }
        return fusionBrancheGarantieDto;
    }
}
