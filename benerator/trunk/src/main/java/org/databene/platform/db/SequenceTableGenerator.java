/*
 * (c) Copyright 2010 by Volker Bergmann. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, is permitted under the terms of the
 * GNU General Public License (GPL).
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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.databene.benerator.GeneratorContext;
import org.databene.benerator.InvalidGeneratorSetupException;
import org.databene.benerator.engine.BeneratorContext;
import org.databene.benerator.util.SimpleGenerator;
import org.databene.commons.HeavyweightIterator;
import org.databene.commons.HeavyweightTypedIterable;
import org.databene.script.ScriptUtil;

/**
 * TODO Document class.<br/><br/>
 * Created: 09.08.2010 14:44:06
 * @since 0.6.4
 * @author Volker Bergmann
 */
public class SequenceTableGenerator<E extends Number> extends SimpleGenerator<E> {
	
	private String table;
	private String column;
	private DBSystem db;
	private String selector;
	protected Long increment = 1L;
	
	private String query;
	private IncrementorStrategy incrementorStrategy;

    public SequenceTableGenerator(String table, String column, DBSystem db) {
	    this(table, column, db, null);
    }

    public SequenceTableGenerator(String table, String column, DBSystem db, String selector) {
    	this.table = table;
	    this.column = column;
	    this.db = db;
	    this.selector = selector;
    }
    
	public void setTable(String table) {
    	this.table = table;
    }

	public void setColumn(String column) {
    	this.column = column;
    }

	public void setSelector(String selector) {
	    this.selector = selector;
    }

	// Generator interface implementation ------------------------------------------------------------------------------
	
    @SuppressWarnings("unchecked")
    public Class<E> getGeneratedType() {
        return (Class<E>) Number.class;
    }

	@Override
    public void init(GeneratorContext context) throws InvalidGeneratorSetupException {
        // check preconditions
        assertNotInitialized();
        if (db == null)
        	throw new InvalidGeneratorSetupException("db is null");
        
        // initialize
        query = "select " + column + " from " + table;
        if (selector != null)
        	query = ScriptUtil.combineScriptableParts(query, " where ", selector);
    	incrementorStrategy = createIncrementor();
        super.init(context);
    }

	private IncrementorStrategy createIncrementor() {
        if (increment == null)
        	return null;
    	String incrementorSql = "update " + table + " set " + column + " = ?";
    	if (selector != null)
    		incrementorSql = ScriptUtil.combineScriptableParts(incrementorSql, " where ", selector);
    	if (selector == null || !ScriptUtil.isScript(selector)) 
    		return new PreparedStatementStrategy(incrementorSql, db);
    	else
    		return new StatementStrategy(incrementorSql, db);
    }

	public E generate() {
		assertInitialized();
		HeavyweightTypedIterable<E> iterable = db.query(query, context);
		HeavyweightIterator<E> iterator = iterable.iterator();
		if (!iterator.hasNext())
			return null;
		E result = iterator.next();
		incrementorStrategy.run(result.longValue(), (BeneratorContext) context);
		return result;
	}
	
	@Override
    public String toString() {
        return getClass().getSimpleName() + "[" + selector + "]";
    }
	
	// IncrementorStrategy ---------------------------------------------------------------------------------------------

    interface IncrementorStrategy {
    	void run(long currentValue, BeneratorContext context);
    }

    class PreparedStatementStrategy implements IncrementorStrategy {
    	
    	private PreparedStatement stmt;

	    public PreparedStatementStrategy(String incrementorSql, DBSystem db) {
	    	try {
	            stmt = db.getConnection().prepareStatement(incrementorSql);
            } catch (SQLException e) {
            	throw new RuntimeException(e);
            }
        }

		public void run(long currentValue, BeneratorContext context) {
		    try {
		    	stmt.setLong(1, currentValue + increment);
		    	stmt.executeUpdate();
	        } catch (SQLException e) {
		        throw new RuntimeException(e);
	        }
	    }
    }

    class StatementStrategy implements IncrementorStrategy {
    	
    	private Statement stmt;
    	private String sql;

	    public StatementStrategy(String sql, DBSystem db) {
	        try {
	            this.stmt = db.getConnection().createStatement();
	            this.sql = sql;
            } catch (SQLException e) {
            	throw new RuntimeException(e);
            }
        }

		public void run(long currentValue, BeneratorContext context) {
			try {
	            String cmd = sql.replace("?", String.valueOf(currentValue + increment));
	            cmd = ScriptUtil.parseUnspecificText(cmd).evaluate(context).toString();
	            stmt.executeUpdate(cmd);
            } catch (SQLException e) {
            	throw new RuntimeException(e);
            }
	    }
    }

}