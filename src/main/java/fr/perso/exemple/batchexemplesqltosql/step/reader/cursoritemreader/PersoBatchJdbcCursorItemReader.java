package fr.perso.exemple.batchexemplesqltosql.step.reader.cursoritemreader;

import org.springframework.batch.item.database.JdbcCursorItemReader;

/**
 * Classe qui surcharge le {@link JdbcCursorItemReader} dans 2 buts :
 * - Gérer le nom du reader non disponible nativement
 * - Implémenter un {@link Comparable} pour pouvoir trier plusieurs {@link JdbcCursorItemReader}.
 *
 * @param <T> L'objet retourné par le reader.
 */
public class PersoBatchJdbcCursorItemReader<T> extends JdbcCursorItemReader implements Comparable<PersoBatchJdbcCursorItemReader> {

    // Ce champ est utilisé pour l'ordonnancement des readers.
    private String ordre;

    public String getOrdre() {
        return ordre;
    }

    public void setOrdre(String ordre) {
        this.ordre = ordre;
    }

    @Override
    public int compareTo(PersoBatchJdbcCursorItemReader o) {
        return this.ordre.compareTo(o.getOrdre());
    }
}
