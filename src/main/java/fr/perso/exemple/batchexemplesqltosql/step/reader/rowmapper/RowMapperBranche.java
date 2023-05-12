package fr.perso.exemple.batchexemplesqltosql.step.reader.rowmapper;


import fr.perso.exemple.batchexemplesqltosql.model.dto.BrancheDto;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RowMapperBranche implements RowMapper<BrancheDto> {
    @Override
    public BrancheDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        BrancheDto brancheDto = new BrancheDto();
        brancheDto.setId(rs.getLong(1));
        brancheDto.setCode(rs.getString(2));
        brancheDto.setLibelle(rs.getString(3));
        return brancheDto;
    }
}
