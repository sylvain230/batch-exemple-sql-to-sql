package fr.perso.exemple.batchexemplesqltosql.step.reader.peupler;

import fr.perso.exemple.batchexemplesqltosql.model.dto.BrancheDto;
import fr.perso.exemple.batchexemplesqltosql.model.dto.BrancheDtoGarantieDto;
import fr.perso.exemple.batchexemplesqltosql.model.dto.GarantieDto;
import org.springframework.stereotype.Service;

import java.util.function.BiFunction;

@Service
public class PeuplerBrancheDtoGarantieDtoWithGarantieDto implements BiFunction<GarantieDto, BrancheDtoGarantieDto, BrancheDtoGarantieDto> {
    @Override
    public BrancheDtoGarantieDto apply(GarantieDto garantieDto, BrancheDtoGarantieDto brancheDtoGarantieDto) {
        if (brancheDtoGarantieDto == null)
         brancheDtoGarantieDto = new BrancheDtoGarantieDto();

            brancheDtoGarantieDto.setGarantieDto(garantieDto);
            return brancheDtoGarantieDto;

    }
}
