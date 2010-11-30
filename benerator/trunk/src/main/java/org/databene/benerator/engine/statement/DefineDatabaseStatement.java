/*
 * (c) Copyright 2009-2010 by Volker Bergmann. All rights reserved.
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

package org.databene.benerator.engine.statement;

import org.databene.benerator.engine.BeneratorContext;
import org.databene.benerator.engine.ResourceManager;
import org.databene.benerator.engine.Statement;
import org.databene.commons.Expression;
import org.databene.commons.expression.ExpressionUtil;
import org.databene.model.data.DataModel;
import org.databene.platform.db.DBSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Executes a &lt;database/&gt; from an XML descriptor.<br/>
 * <br/>
 * Created at 23.07.2009 07:13:02
 * @since 0.6.0
 * @author Volker Bergmann
 */

public class DefineDatabaseStatement implements Statement {
	
	private static Logger logger = LoggerFactory.getLogger(DefineDatabaseStatement.class);
	
	private Expression<String> id;
	private Expression<String> url;
	private Expression<String> driver;
	private Expression<String> user;
	private Expression<String> password;
	private Expression<String> catalog;
	private Expression<String> schema;
	private Expression<String> tableFilter;
	private Expression<String>  includeTables;
	private Expression<String>  excludeTables;
	private Expression<Boolean> batch;
	private Expression<Integer> fetchSize;
	private Expression<Boolean> readOnly;
	private Expression<Boolean> lazy;
	private Expression<Boolean> acceptUnknownColumnTypes;
	private ResourceManager resourceManager;
	
	public DefineDatabaseStatement(Expression<String> id, Expression<String> url, Expression<String> driver, 
			Expression<String> user, Expression<String> password, Expression<String> catalog, Expression<String> schema, 
			Expression<String> tableFilter, Expression<String> includeTables, Expression<String> excludeTables, 
			Expression<Boolean> batch, Expression<Integer> fetchSize, Expression<Boolean> readOnly, Expression<Boolean> lazy,
			Expression<Boolean> acceptUnknownColumnTypes, ResourceManager resourceManager) {
		this.id = id;
	    this.url = url;
	    this.driver = driver;
	    this.user = user;
	    this.password = password;
	    this.catalog = catalog;
	    this.schema = schema;
	    this.tableFilter = tableFilter;
	    this.includeTables = includeTables;
	    this.excludeTables = excludeTables;
	    this.batch = batch;
	    this.fetchSize = fetchSize;
	    this.readOnly = readOnly;
	    this.lazy = lazy;
	    this.acceptUnknownColumnTypes = acceptUnknownColumnTypes;
	    this.resourceManager = resourceManager;
    }

	@SuppressWarnings("deprecation")
    public void execute(BeneratorContext context) {
	    logger.debug("Instantiating database with id '" + id + "'");
	    String idValue = id.evaluate(context);
		DBSystem db = new DBSystem(
	    		idValue, 
	    		ExpressionUtil.evaluate(url, context), 
	    		ExpressionUtil.evaluate(driver, context), 
	    		ExpressionUtil.evaluate(user, context), 
	    		ExpressionUtil.evaluate(password, context));
	    db.setCatalog(ExpressionUtil.evaluate(catalog, context));
	    db.setSchema(ExpressionUtil.evaluate(schema, context));
	    db.setTableFilter(ExpressionUtil.evaluate(tableFilter, context));
	    db.setIncludeTables(ExpressionUtil.evaluate(includeTables, context));
	    db.setExcludeTables(ExpressionUtil.evaluate(excludeTables, context));
	    db.setBatch(ExpressionUtil.evaluate(batch, context));
	    db.setFetchSize(ExpressionUtil.evaluate(fetchSize, context));
	    db.setReadOnly(ExpressionUtil.evaluate(readOnly, context));
	    Boolean isLazy = ExpressionUtil.evaluate(lazy, context);
		db.setLazy(isLazy);
	    db.setAcceptUnknownColumnTypes(ExpressionUtil.evaluate(acceptUnknownColumnTypes, context));
	    context.set(idValue, db);
	    DataModel.getDefaultInstance().addDescriptorProvider(db, context.isValidate() && !isLazy);
	    resourceManager.addResource(db);
    }

}
