/*
 * (c) Copyright 2008-2009 by Volker Bergmann. All rights reserved.
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

package org.databene.benerator.maven;

import org.databene.benerator.main.DBSnapshotTool;
import org.databene.commons.Assert;

/**
 * Creates a database snapshot in DbUnit data file format.<br/>
 * <br/>
 * Created: 09.07.2008 18:50:23
 * @since 0.5.4
 * @author Volker Bergmann
 * @goal dbsnapshot
 */
public class DBSnapshotMojo extends AbstractBeneratorMojo {
    
	/**
	 * The fully qualified name of the JDBC database driver.
	 * @parameter
	 * @required
	 */
	protected String dbDriver;
	
	/**
	 * The JDBC database url.
	 * @parameter
	 * @required
	 */
	protected String dbUrl;
	
	/**
	 * The database user name.
	 * @parameter expression="${user.name}"
	 * @required
	 */
	protected String dbUser;
	
	/**
	 * The database password.
	 * @parameter expression="${user.name}"
	 */
	protected String dbPassword;
	
	/**
	 * The database schema to use.
	 * @parameter expression="${user.name}"
	 */
	protected String dbSchema;
	
	@Override
	protected void setSystemProperties() {
		super.setSystemProperties();
		setSystemProperty("dbDriver", dbDriver);
		setSystemProperty("dbUrl", dbUrl);
		setSystemProperty("dbUser", dbUser);
		setSystemProperty("dbPassword", dbPassword);
		setSystemProperty("dbSchema", dbSchema);
	}

	/**
	 * 'Main' method of the Mojo which calls the DbSnapshotTool using the pom's benerator configuration.
	 */
    public void execute() {
		getLog().info(getClass().getName());
		setSystemProperties();
		Assert.notNull(dbUrl, "dbUrl");
		Assert.notNull(dbDriver, "dbDriver");
		Assert.notNull(dbUser, "dbUser");
		DBSnapshotTool.export(dbUrl, dbDriver, dbSchema, dbUser, dbPassword, "snapshot.dbunit.xml", "dbunit");
		// TODO support different export formats
	}

}
