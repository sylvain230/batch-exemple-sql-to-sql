package fr.perso.exemple.batchexemplesqltosql.configuration;

import fr.perso.exemple.batchexemplesqltosql.framework.PersoBatchCurrentDateJobIncrementer;
import fr.perso.exemple.batchexemplesqltosql.model.dto.BrancheDtoGarantieDto;
import fr.perso.exemple.batchexemplesqltosql.model.dto.FusionBrancheGarantieDto;
import fr.perso.exemple.batchexemplesqltosql.step.reader.cursoritemreader.CompositeCursorItemReader;
import fr.perso.exemple.batchexemplesqltosql.step.listener.PersoBatchJobExecutionListener;
import fr.perso.exemple.batchexemplesqltosql.step.reader.pagingitemreader.CompositePagingItemReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.ItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@EnableBatchProcessing
@ComponentScan({"fr.perso.exemple"})
public class ExportJobConfiguration {

    private final Logger log = LoggerFactory.getLogger(ExportJobConfiguration.class);

    @Qualifier("dataSourceFonc")
    private final DataSource dataSourceFonc;

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    /**
     * Reader Composite {@link org.springframework.batch.item.database.JdbcCursorItemReader} .
     */
    private final CompositeCursorItemReader<BrancheDtoGarantieDto> compositeCursorItemReader;

    /**
     * Reader Composite {@link org.springframework.batch.item.database.JdbcPagingItemReader}.
     */
    private final CompositePagingItemReader<BrancheDtoGarantieDto> compositePagingItemReader;

    /**
     * Processor du batch.
     */
    private final ItemProcessor<BrancheDtoGarantieDto, FusionBrancheGarantieDto> processorBrancheDtogarantieDtoToFusion;

    /**
     * Objets nécessaires pour le writer.
     */
    private final ItemSqlParameterSourceProvider<FusionBrancheGarantieDto> fusionBrancheGarantieDtoItemSqlParameterSourceProvider;

    /**
     * Requête du writer.
     */
    @Value("${sql.insert.fusion}")
    private String sqlInsert;

    /**
     * Indique la taille des paquets que l'on envoit au writer.
     */
    @Value("${chunk.size}")
    private int chunkSize;

    public ExportJobConfiguration(
            DataSource dataSourceFonc,
            JobBuilderFactory jobBuilderFactory,
            StepBuilderFactory stepBuilderFactory,
            CompositeCursorItemReader<BrancheDtoGarantieDto> compositeCursorItemReader,
            CompositePagingItemReader<BrancheDtoGarantieDto> compositePagingItemReader,
            ItemProcessor<BrancheDtoGarantieDto, FusionBrancheGarantieDto> processorBrancheDtogarantieDtoToFusion,
            ItemSqlParameterSourceProvider<FusionBrancheGarantieDto> fusionBrancheGarantieDtoItemSqlParameterSourceProvider) {
        this.dataSourceFonc = dataSourceFonc;
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.compositeCursorItemReader = compositeCursorItemReader;
        this.compositePagingItemReader = compositePagingItemReader;
        this.processorBrancheDtogarantieDtoToFusion = processorBrancheDtogarantieDtoToFusion;
        this.fusionBrancheGarantieDtoItemSqlParameterSourceProvider = fusionBrancheGarantieDtoItemSqlParameterSourceProvider;
    }

    @Bean("jobCursorItemReader")
    @Profile("cursoritemreader")
    public Job jobCursorItemreader() {
        return this.jobBuilderFactory.get("jobExportdataSqlCursor")
                .incrementer(new PersoBatchCurrentDateJobIncrementer())
                .listener(new PersoBatchJobExecutionListener())
                .start(stepExportDataSqlCursor())
                .build();
    }

    @Bean
    public Step stepExportDataSqlCursor() {
        return stepBuilderFactory.get("stepExportDataSqlCursor")
                .listener(new PersoBatchJobExecutionListener())
                .<BrancheDtoGarantieDto, FusionBrancheGarantieDto>chunk(chunkSize)
                .reader(compositeCursorItemReader)
                .processor(processorBrancheDtogarantieDtoToFusion)
                .writer(itemWriter())
                .startLimit((1))
                .build();
    }


    @Bean("jobPagingItemReader")
    @Profile("pagingitemreader")
    public Job jobPagingItemreader() {
        return this.jobBuilderFactory.get("jobExportdataSqlPaging")
                .incrementer(new PersoBatchCurrentDateJobIncrementer())
                .listener(new PersoBatchJobExecutionListener())
                .start(stepExportDataSqlPaging())
                .build();
    }

    @Bean
    public Step stepExportDataSqlPaging() {
        return stepBuilderFactory.get("stepExportDataSqlPaging")
                .listener(new PersoBatchJobExecutionListener())
                .<BrancheDtoGarantieDto, FusionBrancheGarantieDto>chunk(chunkSize)
                .reader(compositePagingItemReader)
                .processor(processorBrancheDtogarantieDtoToFusion)
                .writer(itemWriter())
                .startLimit((1))
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<FusionBrancheGarantieDto> itemWriter() {
        return new JdbcBatchItemWriterBuilder<FusionBrancheGarantieDto>()
                .dataSource(dataSourceFonc)
                .sql(sqlInsert)
//                .itemSqlParameterSourceProvider(fusionBrancheGarantieDtoItemSqlParameterSourceProvider)
                .itemPreparedStatementSetter(new ItemPreparedStatementSetter<FusionBrancheGarantieDto>() {
                    @Override
                    public void setValues(FusionBrancheGarantieDto fusionBrancheGarantieDto, PreparedStatement preparedStatement) throws SQLException {
                        preparedStatement.setString(1, fusionBrancheGarantieDto.getCode());
                        preparedStatement.setString(2, fusionBrancheGarantieDto.getLibelle());
                    }
                })
                .build();

    }
}
