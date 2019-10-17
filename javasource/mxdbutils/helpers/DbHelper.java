package mxdbutils.helpers;

import java.sql.Connection;
import java.sql.SQLException;

public class DbHelper {
	private static String IDENTIFIERQUOTESTRING = null;
	
	private static String getIdentifierQuoteString(Connection connection) throws SQLException {
		if (IDENTIFIERQUOTESTRING == null) {
			IDENTIFIERQUOTESTRING = connection.getMetaData().getIdentifierQuoteString();
		}
		return IDENTIFIERQUOTESTRING;
	}
	
	public static String escapeIdentifier(Connection connection, String identifier) throws SQLException {
		return getIdentifierQuoteString(connection) + identifier + getIdentifierQuoteString(connection);
	}
}
