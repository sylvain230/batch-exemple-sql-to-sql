package fr.perso.exemple.batchexemplesqltosql.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

/**
 * Configuration Spring Batch spécifique.
 */
@Configuration
public class SpringBatchConfiguration {

    /**
     * Datasource HSQL embarquée, utilisée pour suivre les processus Spring Batch.
     * @return La datasource.
     */
    @Bean("dataSource")
    public EmbeddedDatabase embeddedDatabase() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:org/springframework/batch/core/schema-drop-h2.sql")
                .addScript("classpath:org/springframework/batch/core/schema-h2.sql")
                .generateUniqueName(true)
                .build();
    }
}
