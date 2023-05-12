package fr.perso.exemple.batchexemplesqltosql.step.reader.cursoritemreader;

import fr.perso.exemple.batchexemplesqltosql.model.dto.BrancheDtoGarantieDto;
import org.springframework.batch.item.*;
import org.springframework.batch.item.database.AbstractCursorItemReader;
import org.springframework.stereotype.Component;

import java.util.SortedMap;
import java.util.function.BiFunction;

/**
 * Composant pouvant prendre autant de {@link org.springframework.batch.item.database.JdbcCursorItemReader} que voulu.
 * Il n'est pas possible nativement dans SpringBatch pour un reader de prendre en paramètre plusieurs requêtes SQL.
 * C'est pour cette raison que ce composant est écrit.
 *
 * @param <T> L'objet retour.
 */
@Component
public class CompositeCursorItemReader<T> implements ItemStreamReader<BrancheDtoGarantieDto> {

    // Cette map permet de stocker les différents PersoBatchJdbcCursorItemReader du reader.
    private SortedMap<PersoBatchJdbcCursorItemReader<?>, BiFunction<?, ?, ? >> mapJdbcCursorItemReader;

    @Override
    public BrancheDtoGarantieDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        BrancheDtoGarantieDto brancheDtoGarantieDto = null;
        for (AbstractCursorItemReader<?> cursorItemReader : mapJdbcCursorItemReader.keySet()) { // On boucle sur le PersoBatchJdbcCursorItemReader
            Object object = cursorItemReader.read(); // Lecture du reader
            if (object != null) {
                // On récupère le mapper associé et on map.
                BiFunction<Object, BrancheDtoGarantieDto, BrancheDtoGarantieDto> mapper = (BiFunction<Object, BrancheDtoGarantieDto, BrancheDtoGarantieDto>) mapJdbcCursorItemReader.get(cursorItemReader);
                brancheDtoGarantieDto = mapper.apply(object,brancheDtoGarantieDto);
            }
        }

        if(brancheDtoGarantieDto != null) {
            return brancheDtoGarantieDto;
        }

        return null;
    }

    /**
     * Permet d'initialiser les readers avec le contexte d'exécution.
     *
     * @param executionContext Le contexte.
     *
     * @throws ItemStreamException L'exception remontée.
     */
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        for(AbstractCursorItemReader<?> cursorItemReader : mapJdbcCursorItemReader.keySet()) {
            cursorItemReader.open(executionContext);
        }

    }

    /**
     * Permet de mettre à jour les readers avec le contexte d'exécution.
     *
     * @param executionContext Le contexte.
     *
     * @throws ItemStreamException L'exception remontée.
     */
    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        for(AbstractCursorItemReader<?> cursorItemReader : mapJdbcCursorItemReader.keySet()) {
            cursorItemReader.update(executionContext);
        }
    }

    /**
     * Permet de fermer les readers.
     *
     * @throws ItemStreamException L'exception remontée.
     */
    @Override
    public void close() throws ItemStreamException {
        for(AbstractCursorItemReader<?> cursorItemReader : mapJdbcCursorItemReader.keySet()) {
            cursorItemReader.close();
        }
    }

    public SortedMap<PersoBatchJdbcCursorItemReader<?>, BiFunction<?, ?, ?>> getMapJdbcCursorItemReader() {
        return mapJdbcCursorItemReader;
    }

    public void setMapJdbcCursorItemReader(SortedMap<PersoBatchJdbcCursorItemReader<?>, BiFunction<?, ?, ?>> mapJdbcCursorItemReader) {
        this.mapJdbcCursorItemReader = mapJdbcCursorItemReader;
    }
}
