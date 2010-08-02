/*
 * (c) Copyright 2007-2010 by Volker Bergmann. All rights reserved.
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

package org.databene.benerator.main;

import java.util.Arrays;
import java.util.List;

import org.databene.commons.IOUtil;
import org.databene.commons.NumberUtil;
import org.databene.commons.RoundedNumberFormat;
import org.databene.commons.StringUtil;
import org.databene.commons.SystemInfo;
import org.databene.commons.ui.ProgressMonitor;
import org.databene.model.consumer.Consumer;
import org.databene.model.data.Entity;
import org.databene.model.data.TypeDescriptor;
import org.databene.platform.db.DBSystem;
import org.databene.platform.db.SQLEntityExporter;
import org.databene.platform.dbunit.DbUnitEntityExporter;
import org.databene.platform.xls.XLSEntityExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates a snapshot of a database schema and exports it in DbUnit XML file format.
 * @since 0.3.04
 * @author Volker Bergmann
 */
public class DBSnapshotTool {
	
	public static final String DBUNIT_FORMAT = "dbunit";
	public static final String XLS_FORMAT = "xls";
	public static final String SQL_FORMAT = "sql";

	public static final String DEFAULT_FORMAT = DBUNIT_FORMAT;
    
	public static final String DB_PASSWORD = "dbPassword";
	public static final String DB_URL = "dbUrl";
	public static final String DB_DRIVER = "dbDriver";
	public static final String DB_SCHEMA = "dbSchema";
	public static final String DB_USER = "dbUser";
	public static final String FORMAT = "format";
	public static final String DIALECT = "dialect";
	
	// TODO v0.6.x test with each database
    private static final Logger logger = LoggerFactory.getLogger(DBSnapshotTool.class);
    
    public static String[] supportedFormats() {
    	return new String[] {
    		DBUNIT_FORMAT, XLS_FORMAT, SQL_FORMAT
    	};
    }
    
    public static void main(String[] args) {
        logger.info("Starting " + DBSnapshotTool.class.getSimpleName());
        String format = System.getProperty(FORMAT);
        if (format == null)
        	format = DEFAULT_FORMAT;
        String filename = (args.length > 0 ? args[0] : defaultFilename(format));
        
        String dbUrl = System.getProperty(DB_URL);
        if (StringUtil.isEmpty(dbUrl))
            throw new IllegalArgumentException("No database URL specified. " +
            		"Please provide the JDBC URL as an environment property like '-DdbUrl=jdbc:...'");
        String dbDriver = System.getProperty(DB_DRIVER);
        if (StringUtil.isEmpty(dbDriver))
            throw new IllegalArgumentException("No database driver specified. " +
                    "Please provide the JDBC driver class name as an environment property like '-DdbDriver=...'");
        String dbUser = System.getProperty(DB_USER);
        String dbPassword = System.getProperty(DB_PASSWORD);
        String dbSchema = System.getProperty(DB_SCHEMA);
        String dialect = System.getProperty(DIALECT);
        
        logger.info("Exporting data of database '" + dbUrl + "' with driver '" + dbDriver + "' as user '" + dbUser 
                + "'" + (dbSchema != null ? " using schema '" + dbSchema + "'" : "") 
                + " in " + format + " format to file " + filename);

        export(dbUrl, dbDriver, dbSchema, dbUser, dbPassword, filename, format, dialect);
    }

	private static String defaultFilename(String format) {
		if (XLS_FORMAT.equals(format))
		    return "snapshot.xls";
		else if (SQL_FORMAT.equals(format))
		    return "snapshot.sql";
		else
			return "snapshot.dbunit.xml";
    }

	public static void export(String dbUrl, String dbDriver, String dbSchema,
			String dbUser, String dbPassword, String filename, String format, String dialect) {
		export(dbUrl, dbDriver, dbSchema, dbUser, dbPassword, filename, format, dialect, null);
	}
	
	public static void export(String dbUrl, String dbDriver, String dbSchema,
			String dbUser, String dbPassword, String filename, String format, String dialect, ProgressMonitor monitor) {
        if (dbUser == null)
            logger.warn("No JDBC user specified");
        String fileEncoding = SystemInfo.getFileEncoding();
        String lineSeparator = SystemInfo.getLineSeparator();
		long startTime = System.currentTimeMillis();

		Consumer<Entity> exporter = null;
        DBSystem db = null;
        int count = 0;
        try {
        	// connect DB
            db = new DBSystem("db", dbUrl, dbDriver, dbUser, dbPassword);
            if (dbSchema != null)
                db.setSchema(dbSchema);
            db.setDynamicQuerySupported(false);

            // create exporter
            if (DBUNIT_FORMAT.equals(format.toLowerCase()))
            	exporter = new DbUnitEntityExporter(filename, fileEncoding);
            else if (XLS_FORMAT.equals(format))
            	exporter = new XLSEntityExporter(filename);
            else if (SQL_FORMAT.equals(format)) {
            	if (dialect == null)
            		dialect = db.getDialect().getSystem();
            	exporter = new SQLEntityExporter(filename, fileEncoding, lineSeparator, dialect);
            } else
            	throw new IllegalArgumentException("Unknown format: " + format);

            // export data
            List<TypeDescriptor> descriptors = Arrays.asList(db.getTypeDescriptors());
            logger.info("Starting export");
            for (TypeDescriptor descriptor : descriptors) {
                String note = "Exporting table " + descriptor.getName();
                if (monitor != null) {
                	monitor.setNote(note);
                	if (monitor.isCanceled())
                		throw new RuntimeException("Export cancelled");
                }
				logger.info(note);
				Thread.yield();
                for (Entity entity : db.queryEntities(descriptor.getName(), null, null)) {
                    exporter.startConsuming(entity);
                    exporter.finishConsuming(entity);
                    count++;
                }
                if (monitor != null)
                	monitor.advance();
            }
            long duration = System.currentTimeMillis() - startTime;
            if (count == 0)
            	logger.warn("No entities found for snapshot.");
            else
            	logger.info("Exported " + NumberUtil.format(count, 0) + " entities in " + 
            			RoundedNumberFormat.format(duration, 0) + " ms " +
            			"(" + RoundedNumberFormat.format(count * 3600000L / duration, 0) + " p.h.)");
        } finally {
            IOUtil.close(exporter);
            if (db != null)
                db.close();
        }
	}
	
}
