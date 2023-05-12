package fr.perso.exemple.batchexemplesqltosql.configuration;

import fr.perso.exemple.batchexemplesqltosql.model.dto.BrancheDto;
import fr.perso.exemple.batchexemplesqltosql.model.dto.BrancheDtoGarantieDto;
import fr.perso.exemple.batchexemplesqltosql.model.dto.GarantieDto;
import fr.perso.exemple.batchexemplesqltosql.step.reader.cursoritemreader.CompositeCursorItemReader;
import fr.perso.exemple.batchexemplesqltosql.step.reader.cursoritemreader.PersoBatchJdbcCursorItemReader;
import fr.perso.exemple.batchexemplesqltosql.step.reader.cursoritemreader.PersoBatchJdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.TreeMap;
import java.util.function.BiFunction;

@Configuration
public class CursorItemReaderConfiguration {

    @Qualifier("dataSourceFonc")
    private final DataSource dataSourceFonc;

    private final RowMapper<GarantieDto> rowMapperGarantie;
    private final RowMapper<BrancheDto> rowMapperBranche;
    private final BiFunction<BrancheDto, BrancheDtoGarantieDto, BrancheDtoGarantieDto> peuplerCustomObjectWitBrancheDto;
    private final BiFunction<GarantieDto, BrancheDtoGarantieDto, BrancheDtoGarantieDto> peuplerCustomObjectWitGarantieDto;

    /**
     * Requêtes des readers.
     */
    @Value("${sql.select.garantie}")
    private String sqlSelectGarantie;
    @Value("${sql.select.branche}")
    private String sqlSelectBranche;

    public CursorItemReaderConfiguration(
            DataSource dataSourceFonc,
            RowMapper<GarantieDto> rowMapperGarantie,
            RowMapper<BrancheDto> rowMapperBranche,
            BiFunction<BrancheDto, BrancheDtoGarantieDto, BrancheDtoGarantieDto> peuplerCustomObjectWitBrancheDto,
            BiFunction<GarantieDto, BrancheDtoGarantieDto, BrancheDtoGarantieDto> peuplerCustomObjectWitGarantieDto) {
        this.dataSourceFonc = dataSourceFonc;
        this.rowMapperGarantie = rowMapperGarantie;
        this.rowMapperBranche = rowMapperBranche;
        this.peuplerCustomObjectWitBrancheDto = peuplerCustomObjectWitBrancheDto;
        this.peuplerCustomObjectWitGarantieDto = peuplerCustomObjectWitGarantieDto;
    }

    @Bean
    public CompositeCursorItemReader<BrancheDtoGarantieDto> itemReaderCursor() {
        CompositeCursorItemReader<BrancheDtoGarantieDto> itemReader = new CompositeCursorItemReader<>();

        // Création d'une map pour pouvoir rajouter facilement des JdbcCursorItemReader.
        TreeMap<PersoBatchJdbcCursorItemReader<?>, BiFunction<?, ?, ?>> map = new TreeMap<>();

        // On ajoute les readers dans la map.
        // Ils vont être lu par ordre alphabétique de nameReader.
        // Le nameReader est défini dans la méthode build() du PersoBatchJdbcCursorItemReaderBuilder.
        map.put(cursorItemReaderGarantie(), peuplerCustomObjectWitGarantieDto);
        map.put(cursorItemReaderBranche(), peuplerCustomObjectWitBrancheDto);
        itemReader.setMapJdbcCursorItemReader(map);

        return itemReader;
    }

    /**
     * Reader pou récupérer l'ensemble des garanties.
     *
     * @return un {@link PersoBatchJdbcCursorItemReader}.
     */
    @Bean
    public PersoBatchJdbcCursorItemReader<GarantieDto> cursorItemReaderGarantie() {
        return new PersoBatchJdbcCursorItemReaderBuilder<GarantieDto>()
                .name("itemReaderGarantie")
                .dataSource(dataSourceFonc) // Datasource de l'application
                .sql(sqlSelectGarantie) // Requête de lecture
                .rowMapper(rowMapperGarantie) // Mapper pour transformer les données lues
                .build();
    }

    /**
     * Reader pou récupérer l'ensemble des branches.
     *
     * @return un {@link PersoBatchJdbcCursorItemReader}.
     */
    @Bean
    public PersoBatchJdbcCursorItemReader<BrancheDto> cursorItemReaderBranche() {
        return new PersoBatchJdbcCursorItemReaderBuilder<BrancheDto>()
                .name("itemReaderBranche")
                .dataSource(dataSourceFonc) // Datasource de l'application
                .sql(sqlSelectBranche) // Requête de lecture
                .rowMapper(rowMapperBranche) // Mapper pour transformer les données lues
                .build();
    }

}
