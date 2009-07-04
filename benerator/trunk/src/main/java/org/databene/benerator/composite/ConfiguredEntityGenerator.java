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

package org.databene.benerator.composite;

import java.util.Map;

import org.databene.benerator.Generator;
import org.databene.benerator.IllegalGeneratorStateException;
import org.databene.commons.Context;
import org.databene.model.data.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Encapsulates variables, entity generator and context behind a Generator interface.<br/><br/>
 * Created: 30.01.2008 20:45:21
 * @since 0.4.0
 * @author Volker Bergmann
 */
public class ConfiguredEntityGenerator implements Generator<Entity> {
	
    private static Logger logger = LoggerFactory.getLogger(ConfiguredEntityGenerator.class);
    
    private static long entityCount = 0;

    private Generator<Entity> entityGenerator;
	private Map<String, Generator<? extends Object>> variables;
	private Context context;
	private boolean variablesInitialized;
	
	public ConfiguredEntityGenerator(Generator<Entity> entityGenerator, Map<String, Generator<? extends Object>> variables, Context context) {
		this.entityGenerator = entityGenerator;
		this.variables = variables;
		this.context = context;
		this.variablesInitialized = false;
	}
	
	// Generator implementation ----------------------------------------------------------------------------------------
	
	public Class<Entity> getGeneratedType() {
		return Entity.class;
	}

	public void validate() {
        for (Generator<? extends Object> varGen : variables.values())
        	varGen.validate();
        entityGenerator.validate();
	}

	public boolean available() {
		if (!variablesInitialized) {
	        for (Generator<? extends Object> varGen : variables.values()) {
	            if (!varGen.available()) {
	            	if (logger.isDebugEnabled())
	            		logger.debug("No more available: " + varGen);
	                return false;
	            }
	        }
	        for (Map.Entry<String, Generator<? extends Object>> entry : variables.entrySet())
	            context.set(entry.getKey(), entry.getValue().generate());
	        variablesInitialized = true;
		}
        return entityGenerator.available();
	}

	public Entity generate() {
		if (!available())
			throw new IllegalGeneratorStateException("Generator is not available");
        Object product = entityGenerator.generate();
        Entity entity = (Entity) product;
//        for (String variableName : variables.keySet())
//            context.remove(variableName);
        variablesInitialized = false;
        if (logger.isDebugEnabled())
        	logger.debug("Generated " + entity);
        entityCount++;
        return entity;
	}

	public void reset() {
		for (Generator<? extends Object> variable : variables.values())
			variable.reset();
		variablesInitialized = false;
		entityGenerator.reset();
	}

	public void close() {
		for (Generator<? extends Object> variable : variables.values())
			variable.close();
		entityGenerator.close();
        for (String variableName : variables.keySet())
            context.remove(variableName);
	}

	public static long entityCount() {
	    return entityCount;
	}
	
	// java.lang.Object overrides --------------------------------------------------------------------------------------
	
	@Override
	public String toString() {
	    return getClass().getSimpleName() + "[\n"
	        + (variables.size() > 0 ? "    variables" + variables + "\n" : "")
	        + "    " + entityGenerator + "\n"
	        + "]";
	}

}
