package fr.perso.exemple.batchexemplesqltosql.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
public class DatasourceConfigurationTest {

    @Bean("dataSourceFonc")
    @Profile("test")
    public DataSource dataSourceH2() {
        EmbeddedDatabaseBuilder embeddedDatabaseBuilder = new EmbeddedDatabaseBuilder();
        embeddedDatabaseBuilder.setType(EmbeddedDatabaseType.H2);
        embeddedDatabaseBuilder.setName("MODE=PostgreSQL");
        embeddedDatabaseBuilder.generateUniqueName(true);
        return embeddedDatabaseBuilder.build();
    }

}
