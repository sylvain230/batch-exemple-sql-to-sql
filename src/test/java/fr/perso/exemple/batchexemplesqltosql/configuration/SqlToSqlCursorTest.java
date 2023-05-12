package fr.perso.exemple.batchexemplesqltosql.configuration;

import org.checkerframework.checker.units.qual.A;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBatchTest
@SpringJUnitConfig({ExportJobConfiguration.class, DatasourceConfigurationTest.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles({"test", "cursoritemreader"})
public class SqlToSqlCursorTest {

    @Autowired
    @Qualifier("dataSourceFonc")
    private DataSource dataSource;

    @Autowired
    @Qualifier("jobCursorItemReader")
    private Job jobCursorItemreader;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobRepository jobRepository;

    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    @Qualifier("dataSource")
    private EmbeddedDatabase dataSourceSpringBatch;

    private JdbcTemplate getJdbTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
        return jdbcTemplate;
    }

    @BeforeEach
    private void before() throws SQLException {
        ScriptUtils.executeSqlScript(getJdbTemplate().getDataSource().getConnection(),
                new FileSystemResource("src/test/resources/sql/create-database-test.sql"));
    }

    @AfterEach
    private void after() {
        JdbcTestUtils.dropTables(getJdbTemplate(),
                "springbatch.FUSION_BRANCHE_GARANTIE_FUS",
                "springbatch.GARANTIE_PRODUIT_GPR",
                "springbatch.BRANCHE_BRA",
                "springbatch.GARANTIE_GAR",
                "springbatch.PRODUIT_PRO");
    }

    @Test
    void test() throws Exception {
        jobLauncherTestUtils = new JobLauncherTestUtils();
        jobLauncherTestUtils.setJobLauncher(jobLauncher);
        jobLauncherTestUtils.setJobRepository(jobRepository);
        jobLauncherTestUtils.setJob(jobCursorItemreader);
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        Assert.assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
        Integer nbLignes = getJdbTemplate().queryForObject("select count(1) from springbatch.FUSION_BRANCHE_GARANTIE_FUS", Integer.class);
        Assert.assertEquals("On doit avoir 1 ligne.",1, nbLignes.intValue());
    }
}
