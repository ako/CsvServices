// This file was generated by Mendix Studio Pro.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package mxdbutils.actions;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.function.Function;
import com.mendix.core.Core;
import com.mendix.core.CoreException;
import com.mendix.logging.ILogNode;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.webui.CustomJavaAction;
import mxdbutils.helpers.DbHelper;
import com.mendix.systemwideinterfaces.core.IMendixObject;

/**
 * Locks a specific object on database level and introduces a NOWAIT to skip locking if the object is already locked by another connection.
 * 
 * Executes a SELECT FOR UPDATE; when nowait is true, it adds NOWAIT which skips if the record is already locked (at PostgreSQL).
 */
public class LockObject extends CustomJavaAction<java.lang.Boolean>
{
	private IMendixObject obj;
	private java.lang.Boolean nowait;

	public LockObject(IContext context, IMendixObject obj, java.lang.Boolean nowait)
	{
		super(context);
		this.obj = obj;
		this.nowait = nowait;
	}

	@java.lang.Override
	public java.lang.Boolean executeAction() throws Exception
	{
		// BEGIN USER CODE
		if(!Core.dataStorage().executeWithConnection(getContext(), lockObject(obj, nowait))) {
			throw new CoreException("Unable to obtain lock.");
		}
		return true;
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 * @return a string representation of this action
	 */
	@java.lang.Override
	public java.lang.String toString()
	{
		return "LockObject";
	}

	// BEGIN EXTRA CODE
	private Function<Connection, Boolean> lockObject(IMendixObject obj, boolean nowait) throws Exception {
		return connection -> {
			ILogNode logger = Core.getLogger(this.getClass().getSimpleName());
			try {
				if (connection.getAutoCommit()) {
					connection.setAutoCommit(false);
				}
				
				String query = "SELECT id FROM " + 
						DbHelper.escapeIdentifier(connection, Core.getDatabaseTableName(obj.getMetaObject())) + " WHERE id = " +
						obj.getId().toLong() + " FOR UPDATE";
				
				if (nowait)
					query += " NOWAIT";
				
				Statement stat = connection.createStatement();
				
				try {
					ResultSet rs = stat.executeQuery(query);
					rs.close();
				} finally {
					stat.close();
				}
			} catch (Exception e) {
				logger.error("Error while obtaining database locK: " + e.getMessage());
				return false;
			}
			return true;
		};
	}
	// END EXTRA CODE
}
