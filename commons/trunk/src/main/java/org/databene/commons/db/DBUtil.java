/*
 * (c) Copyright 2007 by Volker Bergmann. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, is permitted under the terms of the
 * GNU General Public License.
 *
 * For redistributing this software or a derivative work under a license other
 * than the GPL-compatible Free Software License as defined by the Free
 * Software Foundation or approved by OSI, you must first obtain a commercial
 * license to this software product from Volker Bergmann.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED CONDITIONS,
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE
 * HEREBY EXCLUDED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.databene.commons.db;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.databene.commons.ArrayFormat;
import org.databene.commons.ConfigurationError;
import org.databene.commons.ConnectFailedException;
import org.databene.commons.ErrorHandler;
import org.databene.commons.IOUtil;
import org.databene.commons.ReaderLineIterator;
import org.databene.commons.StringUtil;
import org.databene.commons.SystemInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

/**
 * Provides database related utility methods.<br/>
 * <br/>
 * Created: 06.01.2007 19:27:02
 * @author Volker Bergmann
 */
public class DBUtil {

    private static final Log logger = LogFactory.getLog(DBUtil.class);

    private static final Log sqlLogger = LogFactory.getLog("org.databene.SQL"); 
    private static final Log jdbcLogger = LogFactory.getLog("org.databene.JDBC"); 
    
    /** private constructor for preventing instantiation. */
    private DBUtil() {}
    
    public static Connection connect(String url, String driver, String user, String password) throws ConnectFailedException {
		try {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url, user, password);
            return connection;
        } catch (ClassNotFoundException e) {
            throw new ConfigurationError("JDBC driver not found: " + driver, e);
        } catch (Exception e) {
			throw new ConnectFailedException("Connecting " + url + " failed: ", e);
		}
	}

    /** Closes a database connection silently */
    public static void close(Connection connection) {
        if (connection == null)
            return;
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error(e, e);
        }
    }

    public static void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new ConfigurationError("Closing statement failed", e);
            }
        }
    }

    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new ConfigurationError("Closing statement failed", e);
            }
        }
    }

    public static String[][] parseResultSet(ResultSet resultSet) throws SQLException {
        List<String[]> rows = new ArrayList<String[]>();
        while (resultSet.next()) {
            int columnCount = resultSet.getMetaData().getColumnCount();
            String[] cells = new String[columnCount];
            for (int i = 0; i < columnCount; i++)
                cells[i] = resultSet.getString(i + 1);
            rows.add(cells);
        }
        String[][] array = new String[rows.size()][];
        return rows.toArray(array);
    }

    /** @deprecated replaced by ConvertingIterable(ResultSetIterator, ResultSetConverter) */
    public static Object[] nextLine(ResultSet resultSet) throws SQLException {
        if (!resultSet.next())
            return null;
        return currentLine(resultSet);
    }

    /** @deprecated replaced by ResultSetConverter */
    public static Object[] currentLine(ResultSet resultSet) throws SQLException {
        int columnCount = resultSet.getMetaData().getColumnCount();
        Object[] cells = new Object[columnCount];
        for (int i = 0; i < columnCount; i++)
            cells[i] = resultSet.getObject(i + 1);
        return cells;
    }

    public static String format(ResultSet resultSet) throws SQLException {
        StringBuilder builder = new StringBuilder();
        // format column names
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++)
            builder.append(metaData.getColumnName(i)).append(i < columnCount ? ", " : SystemInfo.lineSeparator());
        // format cells
        String[][] cells = parseResultSet(resultSet);
        for (String[] row : cells) {
            builder.append(ArrayFormat.format(", ", row)).append(SystemInfo.lineSeparator());
        }
        return builder.toString();
    }

    public static String queryWithOneCellResult(PreparedStatement statement) {
        try {
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next())
                throw new RuntimeException("Expected a row.");
            String value = resultSet.getString(1);
            if (resultSet.next())
                throw new RuntimeException("Expected exactly one row, found more.");
            resultSet.close();
            return value;
        } catch (SQLException e) {
            throw new RuntimeException("Database query failed: ", e);
        }
    }

    public static void runScript(String scriptUri, String encoding, Connection connection, boolean ignoreComments, ErrorHandler errorHandler) throws IOException, SQLScriptException {
		BufferedReader reader = IOUtil.getReaderForURI(scriptUri, encoding);
		try {
			 runScript(reader, connection, ignoreComments, errorHandler);
		} catch (SQLScriptException e) {
            throw e.withUri(scriptUri); // add URI information to the exception
		}
    }

    public static void runScript(String scriptText, Connection connection, boolean ignoreComments, ErrorHandler errorHandler) throws IOException, SQLScriptException {
    	StringReader reader = new StringReader(scriptText);
		runScript(reader, connection, ignoreComments, errorHandler);
    }

	private static SQLScriptException runScript(Reader reader, Connection connection, boolean ignoreComments, ErrorHandler errorHandler)
			throws SQLScriptException {
		ReaderLineIterator iterator = new ReaderLineIterator(reader);
		SQLScriptException exception = null;
		try {
			StringBuilder cmd = new StringBuilder();
			while (iterator.hasNext()) {
			    String line = iterator.next();
			    if (line.startsWith("--"))
			        continue;
			    if (cmd.length() > 0)
			        cmd.append('\r');
			    line = line.trim();
			    cmd.append(line);
			    if (line.endsWith(";")) {
			        // delete the trailing ';'
			        cmd.delete(cmd.length() - 1, cmd.length());
			        String sql = cmd.toString();
			        if (!ignoreComments || !StringUtil.startsWithIgnoreCase(sql.trim(), "COMMENT")) {
			        	try {
				        	executeUpdate(sql, connection);
						} catch (SQLException e) {
							errorHandler.handleError("Error in executing SQL: " + SystemInfo.lineSeparator() + cmd, e);
							// if we arrive here, the ErrorHandler decided not to throw an exception
							// so we save the exception and line number and continue execution
							if (exception != null) // only the first exception is saved
								exception = new SQLScriptException(e, iterator.lineCount());
						}
				    }
			        cmd.delete(0, cmd.length());
			    }
			}
			return exception;
        } finally {
			iterator.close();
        }
	}

    public static int executeUpdate(String sql, Connection connection) throws SQLException {
        if (sqlLogger.isDebugEnabled())
            sqlLogger.debug(sql);
        int result = 0;
        Statement statement = null;
        try {
            statement = connection.createStatement();
            result = statement.executeUpdate(sql);
        } finally {
            if (statement != null)
                statement.close();
        }
        return result;
    }

    public static <T> String[][] query(String query, Connection connection) throws SQLException {
    	Statement statement = null;
    	ResultSet resultSet = null;
    	try {
	    	statement = connection.createStatement();
	    	resultSet = statement.executeQuery(query);
	    	return parseResultSet(resultSet);
    	} finally {
	    	DBUtil.close(resultSet);
	    	DBUtil.close(statement);
    	}
    }

	public static PreparedStatement prepareStatement(Connection connection, String sql) throws SQLException {
		jdbcLogger.debug("preparing statement: " + sql);
		return new LoggingPreparedStatement(connection, sql);
	}

	public static String escape(String text) {
		return text.replace("'", "''");
	}

}
