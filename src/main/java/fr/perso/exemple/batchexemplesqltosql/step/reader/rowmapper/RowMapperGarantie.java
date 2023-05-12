package fr.perso.exemple.batchexemplesqltosql.step.reader.rowmapper;

import fr.perso.exemple.batchexemplesqltosql.model.dto.GarantieDto;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RowMapperGarantie implements RowMapper<GarantieDto> {
    @Override
    public GarantieDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        GarantieDto garantieDto = new GarantieDto();
        garantieDto.setId(rs.getLong(1));
        garantieDto.setCode(rs.getString(2));
        garantieDto.setLibelle(rs.getString(3));
        garantieDto.setMontantLCI(rs.getBigDecimal(4));
        return garantieDto;
    }
}
