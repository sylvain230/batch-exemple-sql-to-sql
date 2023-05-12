package fr.perso.exemple.batchexemplesqltosql.framework;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;

import java.util.Date;

public class PersoBatchCurrentDateJobIncrementer implements JobParametersIncrementer {
    @Override
    public JobParameters getNext(JobParameters jobParameters) {
        return new JobParametersBuilder().addDate("currentDate", new Date()).toJobParameters();
    }
}
