package fr.perso.exemple.batchexemplesqltosql.step.reader.pagingitemreader;

import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.*;
import org.springframework.batch.support.DatabaseType;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.Map;

/**
 * Classe reprise de {@link org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder}.
 * Cette réécriture a pour but de pouvoir setter le nameReader déclaré dans {@link PersoBatchJdbcPagingItemReader}.
 * @param <T>
 */
public class PersoBatchJdbcPagingItemReaderBuilder<T> {
        private DataSource dataSource;
        private int fetchSize = -1;
        private PagingQueryProvider queryProvider;
        private RowMapper<T> rowMapper;
        private Map<String, Object> parameterValues;
        private int pageSize = 10;
        private String groupClause;
        private String selectClause;
        private String fromClause;
        private String whereClause;
        private Map<String, Order> sortKeys;
        private boolean saveState = true;
        private String name;
        private int maxItemCount = Integer.MAX_VALUE;
        private int currentItemCount;

        public PersoBatchJdbcPagingItemReaderBuilder() {
        }

        public PersoBatchJdbcPagingItemReaderBuilder<T> saveState(boolean saveState) {
            this.saveState = saveState;
            return this;
        }

        public PersoBatchJdbcPagingItemReaderBuilder<T> name(String name) {
            this.name = name;
            return this;
        }

        public PersoBatchJdbcPagingItemReaderBuilder<T> maxItemCount(int maxItemCount) {
            this.maxItemCount = maxItemCount;
            return this;
        }

        public PersoBatchJdbcPagingItemReaderBuilder<T> currentItemCount(int currentItemCount) {
            this.currentItemCount = currentItemCount;
            return this;
        }

        public PersoBatchJdbcPagingItemReaderBuilder<T> dataSource(DataSource dataSource) {
            this.dataSource = dataSource;
            return this;
        }

        public PersoBatchJdbcPagingItemReaderBuilder<T> fetchSize(int fetchSize) {
            this.fetchSize = fetchSize;
            return this;
        }

        public PersoBatchJdbcPagingItemReaderBuilder<T> rowMapper(RowMapper<T> rowMapper) {
            this.rowMapper = rowMapper;
            return this;
        }

        public PersoBatchJdbcPagingItemReaderBuilder<T> beanRowMapper(Class<T> mappedClass) {
            this.rowMapper = new BeanPropertyRowMapper(mappedClass);
            return this;
        }

        public PersoBatchJdbcPagingItemReaderBuilder<T> parameterValues(Map<String, Object> parameterValues) {
            this.parameterValues = parameterValues;
            return this;
        }

        public PersoBatchJdbcPagingItemReaderBuilder<T> pageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public PersoBatchJdbcPagingItemReaderBuilder<T> groupClause(String groupClause) {
            this.groupClause = groupClause;
            return this;
        }

        public PersoBatchJdbcPagingItemReaderBuilder<T> selectClause(String selectClause) {
            this.selectClause = selectClause;
            return this;
        }

        public PersoBatchJdbcPagingItemReaderBuilder<T> fromClause(String fromClause) {
            this.fromClause = fromClause;
            return this;
        }

        public PersoBatchJdbcPagingItemReaderBuilder<T> whereClause(String whereClause) {
            this.whereClause = whereClause;
            return this;
        }

        public PersoBatchJdbcPagingItemReaderBuilder<T> sortKeys(Map<String, Order> sortKeys) {
            this.sortKeys = sortKeys;
            return this;
        }

        public PersoBatchJdbcPagingItemReaderBuilder<T> queryProvider(PagingQueryProvider provider) {
            this.queryProvider = provider;
            return this;
        }

        public PersoBatchJdbcPagingItemReader<T> build() {
            Assert.isTrue(this.pageSize > 0, "pageSize must be greater than zero");
            Assert.notNull(this.dataSource, "dataSource is required");
            if (this.saveState) {
                Assert.hasText(this.name, "A name is required when saveState is set to true");
            }

            PersoBatchJdbcPagingItemReader<T> reader = new PersoBatchJdbcPagingItemReader();
            reader.setMaxItemCount(this.maxItemCount);
            reader.setCurrentItemCount(this.currentItemCount);
            reader.setName(this.name);
            reader.setSaveState(this.saveState);
            reader.setDataSource(this.dataSource);
            reader.setFetchSize(this.fetchSize);
            reader.setParameterValues(this.parameterValues);
            if (this.queryProvider == null) {
                Assert.hasLength(this.selectClause, "selectClause is required when not providing a PagingQueryProvider");
                Assert.hasLength(this.fromClause, "fromClause is required when not providing a PagingQueryProvider");
                Assert.notEmpty(this.sortKeys, "sortKeys are required when not providing a PagingQueryProvider");
                reader.setQueryProvider(this.determineQueryProvider(this.dataSource));
            } else {
                reader.setQueryProvider(this.queryProvider);
            }

            reader.setRowMapper(this.rowMapper);
            reader.setPageSize(this.pageSize);

            reader.setOrdre(this.name);

            return reader;
        }

        private PagingQueryProvider determineQueryProvider(DataSource dataSource) {
            try {
                DatabaseType databaseType = DatabaseType.fromMetaData(dataSource);
                Object provider;
                switch (databaseType) {
                    case DERBY:
                        provider = new DerbyPagingQueryProvider();
                        break;
                    case DB2:
                    case DB2VSE:
                    case DB2ZOS:
                    case DB2AS400:
                        provider = new Db2PagingQueryProvider();
                        break;
                    case H2:
                        provider = new H2PagingQueryProvider();
                        break;
                    case HSQL:
                        provider = new HsqlPagingQueryProvider();
                        break;
                    case SQLSERVER:
                        provider = new SqlServerPagingQueryProvider();
                        break;
                    case MYSQL:
                        provider = new MySqlPagingQueryProvider();
                        break;
                    case ORACLE:
                        provider = new OraclePagingQueryProvider();
                        break;
                    case POSTGRES:
                        provider = new PostgresPagingQueryProvider();
                        break;
                    case SYBASE:
                        provider = new SybasePagingQueryProvider();
                        break;
                    case SQLITE:
                        provider = new SqlitePagingQueryProvider();
                        break;
                    default:
                        throw new IllegalArgumentException("Unable to determine PagingQueryProvider type from database type: " + databaseType);
                }

                ((AbstractSqlPagingQueryProvider)provider).setSelectClause(this.selectClause);
                ((AbstractSqlPagingQueryProvider)provider).setFromClause(this.fromClause);
                ((AbstractSqlPagingQueryProvider)provider).setWhereClause(this.whereClause);
                ((AbstractSqlPagingQueryProvider)provider).setGroupClause(this.groupClause);
                ((AbstractSqlPagingQueryProvider)provider).setSortKeys(this.sortKeys);
                return (PagingQueryProvider)provider;
            } catch (MetaDataAccessException var4) {
                throw new IllegalArgumentException("Unable to determine PagingQueryProvider type", var4);
            }
        }
}
