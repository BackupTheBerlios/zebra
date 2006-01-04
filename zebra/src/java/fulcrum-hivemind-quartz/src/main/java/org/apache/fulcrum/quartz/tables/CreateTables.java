package org.apache.fulcrum.quartz.tables;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class CreateTables {

	private ArrayList<String> sql = new ArrayList<String>();

	protected void addSql(String sqlCommand) {
		sql.add(sqlCommand);
	}

	public void createTables(Connection conn) throws SQLException {
		addCommands();
		Iterator<String> itr = sql.iterator();
		while (itr.hasNext()) {
			try
			{
				Statement stmt = conn.createStatement();
				String q = itr.next();
				System.out.println(q);
				conn.setAutoCommit(true);
				stmt.execute(q);
			}
			catch (SQLException sqle)
			{
				/*
				 * ignore this if it is a table does not exist exception from
				 * postgresql on a drop statement.
				 */
				if (!sqle.getMessage().endsWith("does not exist"))
				{
					sqle.printStackTrace();
				}
			}
		}
		conn.setAutoCommit(false);

	}

	public abstract void addCommands();
}
