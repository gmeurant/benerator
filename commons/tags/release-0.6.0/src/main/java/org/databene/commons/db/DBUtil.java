/*
 * (c) Copyright 2007-2009 by Volker Bergmann. All rights reserved.
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

import org.databene.commons.ArrayBuilder;
import org.databene.commons.ArrayFormat;
import org.databene.commons.BeanUtil;
import org.databene.commons.ConfigurationError;
import org.databene.commons.ConnectFailedException;
import org.databene.commons.ErrorHandler;
import org.databene.commons.IOUtil;
import org.databene.commons.LogCategories;
import org.databene.commons.ReaderLineIterator;
import org.databene.commons.StringUtil;
import org.databene.commons.SystemInfo;
import org.databene.commons.converter.AnyConverter;
import org.databene.commons.converter.ToStringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

import javax.sql.PooledConnection;

/**
 * Provides database related utility methods.<br/>
 * <br/>
 * Created: 06.01.2007 19:27:02
 * @author Volker Bergmann
 */
public class DBUtil {

    private static final Logger logger = LoggerFactory.getLogger(DBUtil.class);

    private static final Logger jdbcLogger = LoggerFactory.getLogger(LogCategories.JDBC);
    private static final Logger sqlLogger = LoggerFactory.getLogger(LogCategories.SQL);
    
    /** private constructor for preventing instantiation. */
    private DBUtil() {}
    
	public static Connection connect(JDBCConnectData data) throws ConnectFailedException {
	    return connect(data.url, data.driver, data.user, data.password);
    }

    public static Connection connect(String url, String driverClassName, String user, String password) throws ConnectFailedException {
		try {
			// Instantiate driver
            Class<Driver> driverClass = BeanUtil.forName(driverClassName);
            Driver driver = driverClass.newInstance();
            
            // Wrap connection properties
	        java.util.Properties info = new java.util.Properties();
			if (user != null)
			    info.put("user", user);
			if (password != null)
			    info.put("password", password);
			
			// connect
            jdbcLogger.debug("opening connection to " + url);
            Connection connection = driver.connect(url, info);
            if (connection == null)
            	throw new ConnectFailedException("Connecting the database failed silently - " +
            			"probably due to wrong driver (" + driverClassName + ") or wrong URL format (" + url + ")");
			return connection;
        } catch (Exception e) {
			throw new ConnectFailedException("Connecting " + url + " failed: ", e);
		}
	}

    public static boolean available(String url, String driverClass, String user, String password) {
		try {
	    	Connection connection = connect(url, driverClass, user, password);
	    	DBUtil.close(connection);
            return true;
        } catch (Exception e) {
            return false;
		}
	}

    /** Closes a database connection silently */
    public static void close(Connection connection) {
        if (connection == null)
            return;
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Error closing connection", e);
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

    public static Object parseResultSet(ResultSet resultSet) throws SQLException { 
    	// TODO v0.6 create LoggingResultSet. SQL and JDBC log should happen only in handler classes
        List<Object[]> rows = new ArrayList<Object[]>();
        while (resultSet.next()) {
            int columnCount = resultSet.getMetaData().getColumnCount();
            Object[] cells = new Object[columnCount];
            for (int i = 0; i < columnCount; i++)
                cells[i] = resultSet.getObject(i + 1);
            rows.add(cells);
        }
        if (rows.size() == 1 && rows.get(0).length == 1)
        	return rows.get(0)[0];
        else {
	        Object[][] array = new Object[rows.size()][];
	        return rows.toArray(array);
        }
    }

    /** @deprecated replaced by ConvertingIterable(ResultSetIterator, ResultSetConverter) */
    @Deprecated
    public static Object[] nextLine(ResultSet resultSet) throws SQLException {
        if (!resultSet.next())
            return null;
        return currentLine(resultSet);
    }

    /** @deprecated replaced by ResultSetConverter */
    @Deprecated
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
            builder.append(metaData.getColumnName(i)).append(i < columnCount ? ", " : SystemInfo.getLineSeparator());
        // format cells
        Object parsed = parseResultSet(resultSet);
        if (parsed instanceof Object[][]) {
	        for (Object[] row : (Object[][]) parsed) {
	            builder.append(ArrayFormat.format(", ", row)).append(SystemInfo.getLineSeparator());
	        }
        } else
        	builder.append(ToStringConverter.convert(parsed, "null"));
        return builder.toString();
    }

    public static String queryString(PreparedStatement statement) { 
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
    
    public static long queryLong(String query, Connection connection) {
    	return AnyConverter.convert(queryScalar(query, connection), Long.class);
    }

    public static Object queryScalar(String query, Connection connection) {
        try {
        	Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (!resultSet.next())
                throw new RuntimeException("Expected a row.");
            Object value = resultSet.getObject(1);
            if (resultSet.next())
                throw new RuntimeException("Expected exactly one row, found more.");
            resultSet.close();
            statement.close();
            return value;
        } catch (SQLException e) {
            throw new RuntimeException("Database query failed: ", e);
        }
    }

    public static Object runScript(
    		String scriptUri, String encoding, Connection connection, boolean ignoreComments, ErrorHandler errorHandler) 
    			throws IOException {
		BufferedReader reader = IOUtil.getReaderForURI(scriptUri, encoding);
		return runScript(reader, connection, ignoreComments, errorHandler);
    }

    public static Object runScript(String scriptText, Connection connection, boolean ignoreComments, ErrorHandler errorHandler) {
    	StringReader reader = new StringReader(scriptText);
		return runScript(reader, connection, ignoreComments, errorHandler);
    }

	private static Object runScript(
			Reader reader, Connection connection, boolean ignoreComments, ErrorHandler errorHandler) {
		ReaderLineIterator iterator = new ReaderLineIterator(reader);
		SQLScriptException exception = null;
		Object result = null;
		try {
			StringBuilder cmd = new StringBuilder();
			while (iterator.hasNext()) {
			    String line = iterator.next().trim();
			    if (line.startsWith("--"))
			        continue;
			    if (cmd.length() > 0)
			        cmd.append('\n');
			    cmd.append(line);
			    if (line.endsWith(";") || !iterator.hasNext()) {
			    	if (line.endsWith(";"))
			    		cmd.delete(cmd.length() - 1, cmd.length()); // delete the trailing ';'
			        String sql = cmd.toString().trim();
			        if (sql.length() > 0 && (!ignoreComments || !StringUtil.startsWithIgnoreCase(sql, "COMMENT"))) {
			        	try {
				        	if (sql.toLowerCase().startsWith("select"))
				        		result = query(sql, connection);
				        	else
				        		result = executeUpdate(sql, connection);
						} catch (SQLException e) {
							if (errorHandler == null)
								errorHandler = new ErrorHandler(DBUtil.class);
							errorHandler.handleError("Error in executing SQL: " + SystemInfo.getLineSeparator() + cmd, e);
							// if we arrive here, the ErrorHandler decided not to throw an exception
							// so we save the exception and line number and continue execution
							if (exception != null) // only the first exception is saved
								exception = new SQLScriptException(e, iterator.lineCount());
						}
				    }
			        cmd.delete(0, cmd.length());
			    }
			}
			return (exception != null ? exception : result);
        } finally {
			iterator.close();
        }
	}

    public static int executeUpdate(String sql, Connection connection) throws SQLException {
    	if (sql == null || sql.trim().length() == 0) {
    		logger.warn("Empty SQL string in executeUpdate()");
    		return 0;
    	}
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

    public static <T> T[] queryScalarArray(String query, Class<T> componentType, Connection connection) throws SQLException {
    	Statement statement = null;
    	ResultSet resultSet = null;
    	try {
	    	statement = connection.createStatement();
	    	resultSet = statement.executeQuery(query);
	        ArrayBuilder<T> builder = new ArrayBuilder<T>(componentType);
	        while (resultSet.next())
	        	builder.add(AnyConverter.convert(resultSet.getObject(1), componentType));
	        return builder.toArray();
    	} finally {
	    	DBUtil.close(resultSet);
	    	DBUtil.close(statement);
    	}
    }

    public static Object query(String query, Connection connection) throws SQLException {
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

	public static PreparedStatement prepareStatement(Connection connection, String sql, boolean readOnly) throws SQLException {
		return prepareStatement(connection, sql, readOnly, 
				ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
	}

	public static PreparedStatement prepareStatement(
			Connection connection, 
			String sql, 
			boolean readOnly,
			int resultSetType,
            int resultSetConcurrency,
            int resultSetHoldability) throws SQLException {
		jdbcLogger.debug("preparing statement: " + sql);
		checkReadOnly(sql, readOnly);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (connection instanceof PooledConnection)
        	connection = ((PooledConnection) connection).getConnection();
		PreparedStatement statement = connection.prepareStatement(sql);
		if (sqlLogger.isDebugEnabled() || jdbcLogger.isDebugEnabled())
			statement = (PreparedStatement) Proxy.newProxyInstance(classLoader, 
				new Class[] { PreparedStatement.class }, 
				new LoggingPreparedStatementHandler(statement, sql));
		return statement;
	}

	public static String escape(String text) {
		return text.replace("'", "''");
	}

    public static ResultsWithMetadata queryWithMetadata(String query, Connection connection) throws SQLException {
    	Statement statement = null;
    	ResultSet resultSet = null;
    	try {
	    	statement = connection.createStatement();
	    	resultSet = statement.executeQuery(query);
            ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++)
            	columnNames[i - 1] = metaData.getColumnName(i);
	        List<Object[]> rows = new ArrayList<Object[]>();
	        while (resultSet.next()) {
	            String[] cells = new String[columnCount];
	            for (int i = 0; i < columnCount; i++)
	                cells[i] = resultSet.getString(i + 1);
	            rows.add(cells);
	        }
	        String[][] array = new String[rows.size()][];
	        return new ResultsWithMetadata(columnNames, rows.toArray(array));
    	} finally {
	    	DBUtil.close(resultSet);
	    	DBUtil.close(statement);
    	}
    }

	public static void checkReadOnly(String sql, boolean readOnly) {
		if (readOnly && !sql.trim().toLowerCase().startsWith("select"))
			throw new IllegalStateException("Tried to mutate a database with read-only settings: " + sql);
	}

    public static void logMetaData(Connection connection) {
    	try {
	        DatabaseMetaData metaData = connection.getMetaData();
	        jdbcLogger.info("Connected to " + metaData.getDatabaseProductName() + ' ' + metaData.getDatabaseProductVersion());
	        jdbcLogger.info("Using driver " + metaData.getDriverName() + ' ' + metaData.getDriverVersion());
	        jdbcLogger.info("JDBC version " + metaData.getJDBCMajorVersion() + '.' + metaData.getJDBCMinorVersion());
	        
        } catch (SQLException e) {
        	logger.error("Failed to fetch metadata from connection " + connection);
        }
    }

}
