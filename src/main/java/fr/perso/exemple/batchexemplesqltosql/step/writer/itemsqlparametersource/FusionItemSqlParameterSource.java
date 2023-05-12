package fr.perso.exemple.batchexemplesqltosql.step.writer.itemsqlparametersource;

import fr.perso.exemple.batchexemplesqltosql.model.dto.FusionBrancheGarantieDto;
import org.springframework.batch.item.database.ItemSqlParameterSourceProvider;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

@Component
public class FusionItemSqlParameterSource implements ItemSqlParameterSourceProvider<FusionBrancheGarantieDto> {
    @Override
    public SqlParameterSource createSqlParameterSource(FusionBrancheGarantieDto fusionBrancheGarantieDto) {
        return new MapSqlParameterSource()
                .addValue("FUS_CODE", fusionBrancheGarantieDto.getCode())
                .addValue("FUS_LIBELLE", fusionBrancheGarantieDto.getLibelle());
    }
}
