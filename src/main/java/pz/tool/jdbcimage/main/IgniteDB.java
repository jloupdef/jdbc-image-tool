package pz.tool.jdbcimage.main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;

/**
 * DB facade for MariaDB.
 */
public class IgniteDB extends DBFacade {
	@Override
	public void setupDataSource(BasicDataSource bds) {
		bds.setDefaultTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
		// bds.setConnectionInitSqls(Collections.singletonList("SET
		// REFERENTIAL_INTEGRITY FALSE"));
	}

	@Override
	public List<String> getDbUserTables(Connection con) throws SQLException {
		List<String> retVal = new ArrayList<>();
		try (ResultSet tables = con.getMetaData().getTables(con.getCatalog(), con.getSchema(), "%", new String[] { "TABLE" })) {
			while (tables.next()) {
				String tableName = tables.getString(3);
				retVal.add(tableName);
			}
		}
		return retVal;
	}

	@Override
	public String escapeColumnName(String s) {
		return "\"" + s + "\"";
	}

	@Override
	public String escapeTableName(String s) {
		return "\"" + s + "\"";
	}

	@Override
	public void modifyConstraints(boolean enable) throws SQLException {
		if (!enable)
			mainToolBase.out.println("Foreign key checks disabled in created database connections.");
	}

	@Override
	public void modifyIndexes(boolean enable) throws SQLException {
		mainToolBase.out.println("Index " + (enable ? "enable" : "disable") + " not supported on IgniteDB!");
	}

    @Override
    public String getTruncateTableSql(String tableName) {
        // unable to use TRUNCATE TABLE on MSSQL server even with CONSTRAINTS DISABLED!
        return "DELETE FROM " + escapeTableName(tableName);
    }

	@Override
	public boolean canCreateBlobs() {
		return false;
	}

	@Override
	public void importStarted() {
		super.importStarted();
	}
}
