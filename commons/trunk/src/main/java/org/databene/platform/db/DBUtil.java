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

package org.databene.platform.db;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.databene.commons.SystemInfo;
import org.databene.commons.ArrayFormat;
import org.databene.commons.ConfigurationError;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Provides database related utility methods.<br/>
 * <br/>
 * Created: 06.01.2007 19:27:02
 */
public class DBUtil {

    /** The class' logger */
    private static final Log logger = LogFactory.getLog(DBUtil.class);
    
    /** private constructor for preventing instantiation. */
    private DBUtil() {}

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

    /** TODO define a converter for this ? */
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

    // TODO v0.3 replace this by ResultSetFormat
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

}
