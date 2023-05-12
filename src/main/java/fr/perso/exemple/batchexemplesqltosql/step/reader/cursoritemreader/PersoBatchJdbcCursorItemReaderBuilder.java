package fr.perso.exemple.batchexemplesqltosql.step.reader.cursoritemreader;

import org.springframework.jdbc.core.*;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.List;

/**
 * Classe reprise de {@link org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder}.
 * Cette réécriture a pour but de pouvoir setter le nameReader déclaré dans {@link PersoBatchJdbcCursorItemReader}.
 * @param <T>
 */
public class PersoBatchJdbcCursorItemReaderBuilder<T> {
    private DataSource dataSource;
    private int fetchSize = -1;
    private int maxRows = -1;
    private int queryTimeout = -1;
    private boolean ignoreWarnings;
    private boolean verifyCursorPosition = true;
    private boolean driverSupportsAbsolute;
    private boolean useSharedExtendedConnection;
    private PreparedStatementSetter preparedStatementSetter;
    private String sql;
    private RowMapper<T> rowMapper;
    private boolean saveState = true;
    private String name;
    private int maxItemCount = Integer.MAX_VALUE;
    private int currentItemCount;
    private boolean connectionAutoCommit;

    public PersoBatchJdbcCursorItemReaderBuilder() {
    }

    public PersoBatchJdbcCursorItemReaderBuilder<T> saveState(boolean saveState) {
        this.saveState = saveState;
        return this;
    }

    public PersoBatchJdbcCursorItemReaderBuilder<T> name(String name) {
        this.name = name;
        return this;
    }

    public PersoBatchJdbcCursorItemReaderBuilder<T> maxItemCount(int maxItemCount) {
        this.maxItemCount = maxItemCount;
        return this;
    }

    public PersoBatchJdbcCursorItemReaderBuilder<T> currentItemCount(int currentItemCount) {
        this.currentItemCount = currentItemCount;
        return this;
    }

    public PersoBatchJdbcCursorItemReaderBuilder<T> dataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    public PersoBatchJdbcCursorItemReaderBuilder<T> fetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
        return this;
    }

    public PersoBatchJdbcCursorItemReaderBuilder<T> maxRows(int maxRows) {
        this.maxRows = maxRows;
        return this;
    }

    public PersoBatchJdbcCursorItemReaderBuilder<T> queryTimeout(int queryTimeout) {
        this.queryTimeout = queryTimeout;
        return this;
    }

    public PersoBatchJdbcCursorItemReaderBuilder<T> ignoreWarnings(boolean ignoreWarnings) {
        this.ignoreWarnings = ignoreWarnings;
        return this;
    }

    public PersoBatchJdbcCursorItemReaderBuilder<T> verifyCursorPosition(boolean verifyCursorPosition) {
        this.verifyCursorPosition = verifyCursorPosition;
        return this;
    }

    public PersoBatchJdbcCursorItemReaderBuilder<T> driverSupportsAbsolute(boolean driverSupportsAbsolute) {
        this.driverSupportsAbsolute = driverSupportsAbsolute;
        return this;
    }

    public PersoBatchJdbcCursorItemReaderBuilder<T> useSharedExtendedConnection(boolean useSharedExtendedConnection) {
        this.useSharedExtendedConnection = useSharedExtendedConnection;
        return this;
    }

    public PersoBatchJdbcCursorItemReaderBuilder<T> preparedStatementSetter(PreparedStatementSetter preparedStatementSetter) {
        this.preparedStatementSetter = preparedStatementSetter;
        return this;
    }

    public PersoBatchJdbcCursorItemReaderBuilder<T> queryArguments(Object... args) {
        this.preparedStatementSetter = new ArgumentPreparedStatementSetter(args);
        return this;
    }

    public PersoBatchJdbcCursorItemReaderBuilder<T> queryArguments(Object[] args, int[] types) {
        this.preparedStatementSetter = new ArgumentTypePreparedStatementSetter(args, types);
        return this;
    }

    public PersoBatchJdbcCursorItemReaderBuilder<T> queryArguments(List<?> args) {
        Assert.notNull(args, "The list of arguments must not be null");
        this.preparedStatementSetter = new ArgumentPreparedStatementSetter(args.toArray());
        return this;
    }

    public PersoBatchJdbcCursorItemReaderBuilder<T> sql(String sql) {
        this.sql = sql;
        return this;
    }

    public PersoBatchJdbcCursorItemReaderBuilder<T> rowMapper(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
        return this;
    }

    public PersoBatchJdbcCursorItemReaderBuilder<T> beanRowMapper(Class<T> mappedClass) {
        this.rowMapper = new BeanPropertyRowMapper(mappedClass);
        return this;
    }

    public PersoBatchJdbcCursorItemReaderBuilder<T> connectionAutoCommit(boolean connectionAutoCommit) {
        this.connectionAutoCommit = connectionAutoCommit;
        return this;
    }

    public PersoBatchJdbcCursorItemReader<T> build() {
        if (this.saveState) {
            Assert.hasText(this.name, "A name is required when saveState is set to true");
        }

        Assert.hasText(this.sql, "A query is required");
        Assert.notNull(this.dataSource, "A datasource is required");
        Assert.notNull(this.rowMapper, "A rowmapper is required");
        PersoBatchJdbcCursorItemReader<T> reader = new PersoBatchJdbcCursorItemReader();
        if (StringUtils.hasText(this.name)) {
            reader.setName(this.name);
        }

        reader.setSaveState(this.saveState);
        reader.setPreparedStatementSetter(this.preparedStatementSetter);
        reader.setRowMapper(this.rowMapper);
        reader.setSql(this.sql);
        reader.setCurrentItemCount(this.currentItemCount);
        reader.setDataSource(this.dataSource);
        reader.setDriverSupportsAbsolute(this.driverSupportsAbsolute);
        reader.setFetchSize(this.fetchSize);
        reader.setIgnoreWarnings(this.ignoreWarnings);
        reader.setMaxItemCount(this.maxItemCount);
        reader.setMaxRows(this.maxRows);
        reader.setQueryTimeout(this.queryTimeout);
        reader.setUseSharedExtendedConnection(this.useSharedExtendedConnection);
        reader.setVerifyCursorPosition(this.verifyCursorPosition);
        reader.setConnectionAutoCommit(this.connectionAutoCommit);

        // On assigne le nom du reader.
        reader.setOrdre(this.name);

        return reader;
    }
}
