package fr.perso.exemple.batchexemplesqltosql.step.reader.peupler;

import fr.perso.exemple.batchexemplesqltosql.model.dto.BrancheDto;
import fr.perso.exemple.batchexemplesqltosql.model.dto.BrancheDtoGarantieDto;
import org.springframework.stereotype.Service;

import java.util.function.BiFunction;

@Service
public class PeuplerBrancheDtoGarantieDtoWithBrancheDto implements BiFunction<BrancheDto, BrancheDtoGarantieDto, BrancheDtoGarantieDto> {
    @Override
    public BrancheDtoGarantieDto apply(BrancheDto brancheDto, BrancheDtoGarantieDto brancheDtoGarantieDto) {
        if (brancheDtoGarantieDto == null)
         brancheDtoGarantieDto = new BrancheDtoGarantieDto();

            brancheDtoGarantieDto.setBrancheDto(brancheDto);
            return brancheDtoGarantieDto;

    }
}
