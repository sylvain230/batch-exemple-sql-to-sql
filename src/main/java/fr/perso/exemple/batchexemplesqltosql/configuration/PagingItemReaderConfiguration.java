package fr.perso.exemple.batchexemplesqltosql.configuration;

import fr.perso.exemple.batchexemplesqltosql.model.dto.BrancheDto;
import fr.perso.exemple.batchexemplesqltosql.model.dto.BrancheDtoGarantieDto;
import fr.perso.exemple.batchexemplesqltosql.model.dto.GarantieDto;
import fr.perso.exemple.batchexemplesqltosql.step.reader.pagingitemreader.CompositePagingItemReader;
import fr.perso.exemple.batchexemplesqltosql.step.reader.pagingitemreader.PersoBatchJdbcPagingItemReader;
import fr.perso.exemple.batchexemplesqltosql.step.reader.pagingitemreader.PersoBatchJdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.Order;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiFunction;

/**
 * Classe pour configurer l'ensemble du {@link CompositePagingItemReader}
 */
@Configuration
public class PagingItemReaderConfiguration {

    @Qualifier("dataSourceFonc")
    private final DataSource dataSourceFonc;

    @Value("${page.size}")
    private Integer pageSize;

    @Value("${sql.select.clause.garantie}")
    private String selectClauseGarantie;
    @Value("${sql.from.clause.garantie}")
    private String fromClauseGarantie;
    @Value("${sql.sortkey.garantie}")
    private String sortKeyGarantie;

    @Value("${sql.select.clause.branche}")
    private String selectClauseBranche;
    @Value("${sql.from.clause.branche}")
    private String fromClauseBranche;
    @Value("${sql.sortkey.branche}")
    private String sortKeyBranche;

    private final RowMapper<GarantieDto> rowMapperGarantie;
    private final RowMapper<BrancheDto> rowMapperBranche;
    private final BiFunction<BrancheDto, BrancheDtoGarantieDto, BrancheDtoGarantieDto> peuplerCustomObjectWitBrancheDto;
    private final BiFunction<GarantieDto, BrancheDtoGarantieDto, BrancheDtoGarantieDto> peuplerCustomObjectWitGarantieDto;

    public PagingItemReaderConfiguration(
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
    public CompositePagingItemReader<BrancheDtoGarantieDto> itemReaderPaging() {
        CompositePagingItemReader<BrancheDtoGarantieDto> itemReader = new CompositePagingItemReader<>();

        // Création d'une map pour pouvoir rajouter facilement des JdbcCursorItemReader.
        TreeMap<PersoBatchJdbcPagingItemReader<?>, BiFunction<?, ?, ?>> map = new TreeMap<>();

        // On ajoute les readers dans la map.
        // Ils vont être lu par ordre alphabétique de nameReader.
        // Le nameReader est défini dans la méthode build() du PersoBatchJdbcCursorItemReaderBuilder.
        map.put(pagingItemReaderGarantie(), peuplerCustomObjectWitGarantieDto);
        map.put(pagingItemReaderBranche(), peuplerCustomObjectWitBrancheDto);
        itemReader.setMapJdbcPagingItemReader(map);

        return itemReader;
    }

    /**
     * Reader pou récupérer l'ensemble des garanties.
     *
     * @return un {@link PersoBatchJdbcPagingItemReader}
     */
    @Bean
    public PersoBatchJdbcPagingItemReader<GarantieDto> pagingItemReaderGarantie() {
        return new PersoBatchJdbcPagingItemReaderBuilder<GarantieDto>()
                .name("itemReaderGarantie")
                .dataSource(dataSourceFonc) // Datasource de l'application
                // Requête de lecture
                .selectClause(selectClauseGarantie)
                .fromClause(fromClauseGarantie)
                .rowMapper(rowMapperGarantie) // Mapper pour transformer les données lues
                .pageSize(pageSize)
                .sortKeys(Map.of(sortKeyGarantie, Order.ASCENDING))
                .build();
    }

    /**
     * Reader pou récupérer l'ensemble des branches.
     *
     * @return un {@link PersoBatchJdbcPagingItemReader}
     */
    @Bean
    public PersoBatchJdbcPagingItemReader<BrancheDto> pagingItemReaderBranche() {
        return new PersoBatchJdbcPagingItemReaderBuilder<BrancheDto>()
                .name("itemReaderGarantie")
                .dataSource(dataSourceFonc) // Datasource de l'application
                // Requête de lecture
                .selectClause(selectClauseBranche)
                .fromClause(fromClauseBranche)
                .rowMapper(rowMapperBranche) // Mapper pour transformer les données lues
                .pageSize(pageSize)
                .sortKeys(Map.of(sortKeyGarantie, Order.ASCENDING))
                .build();
    }

}
