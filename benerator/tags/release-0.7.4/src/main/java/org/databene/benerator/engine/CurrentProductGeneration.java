/*
 * (c) Copyright 2011 by Volker Bergmann. All rights reserved.
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

package org.databene.benerator.engine;

import java.io.Closeable;

import org.databene.benerator.Generator;
import org.databene.benerator.GeneratorContext;
import org.databene.benerator.util.WrapperProvider;
import org.databene.benerator.wrapper.ProductWrapper;
import org.databene.commons.Resettable;

/**
 * Uses a {@link Generator} to create the currently processed object.<br/><br/>
 * Created: 01.09.2011 19:03:38
 * @since 0.7.0
 * @author Volker Bergmann
 */
public class CurrentProductGeneration implements Statement, Preparable, Resettable, Closeable {
	
	private String instanceName;
	private Generator<Object> source;
	private WrapperProvider<Object> provider;
	private boolean initialized;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public CurrentProductGeneration(String instanceName, Generator<?> source) {
		this.instanceName = instanceName;
		this.source = (Generator) source;
		this.provider = new WrapperProvider<Object>();
		this.initialized = false;
	}

	public void prepare(BeneratorContext context) {
		if (initialized)
			reset();
		else {
			init(context);
			initialized = true; 
		}
	}
	
	public void init(GeneratorContext context) {
		source.init(context);
	}

	public boolean execute(BeneratorContext context) {
		ProductWrapper<Object> wrapper = source.generate(provider.get());
		context.setCurrentProduct(wrapper);
		if (instanceName != null)
			if (wrapper != null)
				context.set(instanceName, wrapper.unwrap());
			else
				context.remove(instanceName);
		return (wrapper != null);
	}

	public void reset() {
		source.reset();
	}

	public void close() {
		source.close();
	}

	@Override
	public String toString() {
		return instanceName + ':' + source;
	}

}
