package fr.perso.exemple.batchexemplesqltosql.step.reader.pagingitemreader;

import org.springframework.batch.item.database.JdbcPagingItemReader;

/**
 * Classe qui surcharge le {@link org.springframework.batch.item.database.JdbcPagingItemReader} dans 2 buts :
 * - Gérer le nom du reader non disponible nativement
 * - Implémenter un {@link Comparable} pour pouvoir trier plusieurs {@link org.springframework.batch.item.database.JdbcPagingItemReader}.
 *
 * @param <T> L'objet retourné par le reader.
 */
public class PersoBatchJdbcPagingItemReader<T> extends JdbcPagingItemReader implements Comparable<PersoBatchJdbcPagingItemReader> {

    // Ce champ est utilisé pour l'ordonnancement des readers.
    private String ordre;

    public String getOrdre() {
        return ordre;
    }

    public void setOrdre(String ordre) {
        this.ordre = ordre;
    }

    @Override
    public int compareTo(PersoBatchJdbcPagingItemReader o) {
        return this.ordre.compareTo(o.getOrdre());
    }
}
