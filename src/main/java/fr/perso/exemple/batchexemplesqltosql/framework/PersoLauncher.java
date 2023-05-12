package fr.perso.exemple.batchexemplesqltosql.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;

public class PersoLauncher {
    private static final Logger log = LoggerFactory.getLogger(PersoLauncher.class);

    public PersoLauncher() {
    }

    public static void runSpringBatch(Class<?> clazz, String[] args) {
        log.debug("[BATCH] Démarrage de l'application Spring");
        int exitcode = SpringApplication.exit(SpringApplication.run(clazz, args), new ExitCodeGenerator[0]);
        log.debug("[BATCH] code retour calculé SprigBatch {}", exitcode);
    }
}
