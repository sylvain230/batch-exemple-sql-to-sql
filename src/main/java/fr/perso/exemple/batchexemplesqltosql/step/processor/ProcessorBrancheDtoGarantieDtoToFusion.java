package fr.perso.exemple.batchexemplesqltosql.step.processor;

import fr.perso.exemple.batchexemplesqltosql.model.dto.BrancheDtoGarantieDto;
import fr.perso.exemple.batchexemplesqltosql.model.dto.FusionBrancheGarantieDto;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ProcessorBrancheDtoGarantieDtoToFusion implements ItemProcessor<BrancheDtoGarantieDto, FusionBrancheGarantieDto> {

    @Autowired
    Function<BrancheDtoGarantieDto, FusionBrancheGarantieDto> peuplerFusionBrancheGarantieDto;

    @Override
    public FusionBrancheGarantieDto process(BrancheDtoGarantieDto brancheDtoGarantieDto) throws Exception {
        if(brancheDtoGarantieDto != null) {
            return peuplerFusionBrancheGarantieDto.apply(brancheDtoGarantieDto);
        }
        return null;
    }
}
