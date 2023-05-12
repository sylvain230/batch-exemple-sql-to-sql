package fr.perso.exemple.batchexemplesqltosql.step.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameter;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class PersoBatchJobExecutionListener implements JobExecutionListener {
    private final Logger log = LoggerFactory.getLogger(PersoBatchJobExecutionListener.class);
    private static final String LOG_LINE  = "=========";
    private static final String START_MESSAGE  = "========= Démarrage du Job '{}' le '{}'";
    private static final String END_MESSAGE  = "========= Informations de fin du Job '{}'";
    private static final String TEMPS_EXEC_MESSAGE = "Temps d'exécution: {}";
    private static final String STATUT_JOB_MESSAGE = "Statut du Job SpringBatch: {}";
    private static final String SORTIE_JOB_MESSAGE = "Statut de sortie SpringBatch: {}";
    private static final String CODE_RETOUR_MESSAGE = "Code retour: {}";
    private static final String MESSAGE_SORTIE = "Code Message de sortie: {}";
    private static final String FIN_JOB = "Fin du Job: {}";
    private static final String START_PARAMS_MESSAGE  = "========= Paramètres du Job";
    private static final String END_PARAMS_MESSAGE  = "========= Fin des Paramètres du Job";
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss:SS");


    @Override
    public void beforeJob(JobExecution jobExecution) {
        LocalDateTime dt = this.calculerLocalDateTime(jobExecution.getStartTime()) ;
        log.info(START_MESSAGE, jobExecution.getJobInstance().getJobName(), this.dateTimeFormatter.format(dt));

        log.info(START_PARAMS_MESSAGE);
        jobExecution.getJobParameters().getParameters().keySet().forEach((p) -> {
            if(JobParameter.ParameterType.DATE.equals(jobExecution.getJobParameters().getParameters().get(p).getType())) {
                Date d = (Date) jobExecution.getJobParameters().getParameters().get(p).getValue();
                log.info("{} - {}", p, this.dateTimeFormatter.format(calculerLocalDateTime(d)));
            } else {
                log.info("{} - {}", p, jobExecution.getJobParameters().getParameters().get(p));
            }
        });
        log.info(END_PARAMS_MESSAGE);
    }

    private LocalDateTime calculerLocalDateTime(Date startTime) {
        return startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info(END_MESSAGE, jobExecution.getJobInstance().getJobName());
        log.info(TEMPS_EXEC_MESSAGE,calculerTempsEcoule(calculerLocalDateTime(jobExecution.getStartTime()), calculerLocalDateTime(jobExecution.getEndTime())));
        log.info(STATUT_JOB_MESSAGE, jobExecution.getStatus());
        log.info(SORTIE_JOB_MESSAGE, jobExecution.getExitStatus().getExitCode());

        log.info(CODE_RETOUR_MESSAGE, "");

        if(jobExecution.getExitStatus().getExitDescription() != null && !jobExecution.getExitStatus().getExitDescription().isEmpty()) {
            log.info(jobExecution.getExitStatus().getExitDescription());
        }
        log.info(FIN_JOB, jobExecution.getJobInstance().getJobName());

    }

    private String calculerTempsEcoule(LocalDateTime debut, LocalDateTime fin) {
        if(debut!= null && fin != null) {
            return formatDuration(calculerDuration(debut, fin));
        } else {
            throw new IllegalArgumentException("debut et fin ne doivent pas être null.");
        }
    }

    private String formatDuration(Duration duration) {
        if(duration != null && !duration.isZero() && !duration.isNegative()) {
            StringBuilder formattedDuration = new StringBuilder();
            long hours = duration.toHours();
            long minutes = duration.toMinutes();
            long secondes = duration.toSeconds();
            long millis = duration.toMillis();
            if(hours != 0L) {
                formattedDuration.append(hours).append(" h");
            }

            if(minutes != 0L) {
                formattedDuration.append(minutes - TimeUnit.HOURS.toMinutes(hours)).append("m ");
            }

            if(secondes != 0L) {
                formattedDuration.append(minutes - TimeUnit.MINUTES.toSeconds(minutes)).append("m ");
            }

            if(millis != 0L) {
                formattedDuration.append(minutes - TimeUnit.SECONDS.toMinutes(secondes)).append("m ");
            }
             return formattedDuration.toString();
        } else {
            return "0ms";
        }
    }

    private Duration calculerDuration(LocalDateTime debut, LocalDateTime fin) {
        return debut != null && fin != null ? Duration.between(debut, fin) : null;
    }

    private void completerJobExceutionAvecErreurPersonnalisee(JobExecution jobExecution) {
    }
}
