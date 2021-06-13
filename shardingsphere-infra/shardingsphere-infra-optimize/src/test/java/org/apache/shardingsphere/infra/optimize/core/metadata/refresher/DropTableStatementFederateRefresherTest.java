package org.apache.shardingsphere.infra.optimize.core.metadata.refresher;

import com.google.common.collect.ImmutableMap;
import org.apache.shardingsphere.infra.metadata.schema.ShardingSphereSchema;
import org.apache.shardingsphere.infra.metadata.schema.builder.SchemaBuilderMaterials;
import org.apache.shardingsphere.infra.metadata.schema.model.ColumnMetaData;
import org.apache.shardingsphere.infra.metadata.schema.model.IndexMetaData;
import org.apache.shardingsphere.infra.metadata.schema.model.TableMetaData;
import org.apache.shardingsphere.infra.metadata.schema.refresher.SchemaRefresher;
import org.apache.shardingsphere.infra.metadata.schema.refresher.type.DropTableStatementSchemaRefresher;
import org.apache.shardingsphere.infra.optimize.core.metadata.FederateSchemaMetadata;
import org.apache.shardingsphere.infra.optimize.core.metadata.refresher.type.DropTableStatementFederateRefresher;
import org.apache.shardingsphere.sql.parser.sql.common.segment.generic.table.SimpleTableSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.generic.table.TableNameSegment;
import org.apache.shardingsphere.sql.parser.sql.common.statement.ddl.DropTableStatement;
import org.apache.shardingsphere.sql.parser.sql.common.value.identifier.IdentifierValue;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.mysql.ddl.MySQLDropTableStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.oracle.ddl.OracleDropTableStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.postgresql.ddl.PostgreSQLDropTableStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.sql92.ddl.SQL92DropTableStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.sqlserver.ddl.SQLServerDropTableStatement;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

public class DropTableStatementFederateRefresherTest {

    @Test
    public void refreshForMySQL() throws SQLException {
        refresh(new MySQLDropTableStatement());
    }

    @Test
    public void refreshForOracle() throws SQLException {
        refresh(new OracleDropTableStatement());
    }

    @Test
    public void refreshForPostgreSQL() throws SQLException {
        refresh(new PostgreSQLDropTableStatement());
    }

    @Test
    public void refreshForSQL92() throws SQLException {
        refresh(new SQL92DropTableStatement());
    }

    @Test
    public void refreshForSQLServer() throws SQLException {
        refresh(new SQLServerDropTableStatement());
    }

    private void refresh(final DropTableStatement dropTableStatement) throws SQLException {
        FederateSchemaMetadata schema = buildSchema();
        FederateRefresher<DropTableStatement> schemaRefresher = new DropTableStatementFederateRefresher();
        dropTableStatement.getTables().add(new SimpleTableSegment(new TableNameSegment(1, 3, new IdentifierValue("t_order"))));
        schemaRefresher.refresh(schema, Collections.emptyList(), dropTableStatement, mock(SchemaBuilderMaterials.class));
        assertFalse(schema.getTables().containsKey("t_order"));
    }

    @Test
    public void refreshWithUnConfiguredForMySQL() throws SQLException {
        refreshWithUnConfigured(new MySQLDropTableStatement());
    }

    @Test
    public void refreshWithUnConfiguredForOracle() throws SQLException {
        refreshWithUnConfigured(new OracleDropTableStatement());
    }

    @Test
    public void refreshWithUnConfiguredForPostgreSQL() throws SQLException {
        refreshWithUnConfigured(new PostgreSQLDropTableStatement());
    }

    @Test
    public void refreshWithUnConfiguredForSQL92() throws SQLException {
        refreshWithUnConfigured(new SQL92DropTableStatement());
    }

    @Test
    public void refreshWithUnConfiguredForSQLServer() throws SQLException {
        refreshWithUnConfigured(new SQLServerDropTableStatement());
    }

    private void refreshWithUnConfigured(final DropTableStatement dropTableStatement) throws SQLException {
        FederateSchemaMetadata schema = buildSchema();
        FederateRefresher<DropTableStatement> schemaRefresher = new DropTableStatementFederateRefresher();
        dropTableStatement.getTables().add(new SimpleTableSegment(new TableNameSegment(1, 3, new IdentifierValue("t_order_item"))));
        schemaRefresher.refresh(schema, Collections.singletonList("t_order_item"), dropTableStatement, mock(SchemaBuilderMaterials.class));
    }

    private FederateSchemaMetadata buildSchema() {
        Map<String, TableMetaData> metaData = ImmutableMap.of("t_order", new TableMetaData(Collections.singletonList(new ColumnMetaData("order_id", 1, false, false, false)),
                Collections.singletonList(new IndexMetaData("index"))));
        return new FederateSchemaMetadata("t_order", metaData);
    }
}
