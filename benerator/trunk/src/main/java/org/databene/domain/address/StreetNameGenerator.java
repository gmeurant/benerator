/*
 * (c) Copyright 2007-2011 by Volker Bergmann. All rights reserved.
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

package org.databene.domain.address;

import java.util.Stack;

import org.databene.benerator.Generator;
import org.databene.benerator.GeneratorContext;
import org.databene.benerator.csv.WeightedDatasetCSVGenerator;
import org.databene.benerator.wrapper.GeneratorProxy;
import org.databene.commons.ConfigurationError;
import org.databene.commons.Encodings;
import org.databene.commons.StringUtil;
import org.databene.dataset.DatasetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generates a street name for a region.<br/>
 * <br/>
 * Created: 12.06.2006 00:08:28
 * @since 0.1
 * @author Volker Bergmann
 */
public class StreetNameGenerator extends GeneratorProxy<String> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StreetNameGenerator.class);

    private static final String REGION = "org/databene/dataset/region";
    private static final String FILENAME_PATTERN = "/org/databene/domain/address/street_{0}.csv";
    
    private String datasetName;

    public StreetNameGenerator() {
    	this(null);
    }

    public StreetNameGenerator(String datasetName) {
        super(null);
        this.datasetName = datasetName;
    }
    
	public void setDataset(String datasetName) {
		this.datasetName = datasetName;
	}

    @Override
    public synchronized void init(GeneratorContext context) {
    	Stack<String> datasetOptions = new Stack<String>();
    	datasetOptions.push(DatasetUtil.fallbackRegionName());
    	datasetOptions.push(DatasetUtil.defaultRegionName());
    	if (!StringUtil.isEmpty(datasetName))
    		datasetOptions.push(datasetName);
    	init(datasetOptions, context);
    }

	private void init(Stack<String> datasetOptions, GeneratorContext context) {
		String currentOption = datasetOptions.pop();
    	try {
			source = createSource(currentOption);
	        super.init(context);
    	} catch (Exception e) {
    		// if the call fails, try another option
    		if (datasetOptions.isEmpty())
        		throw new ConfigurationError(getClass().getSimpleName() + " could not be initialized");
    		String nextOption = datasetOptions.peek();
    		LOGGER.error("Error initializing " + getClass().getSimpleName() + " with dataset '" + currentOption + "': " + 
    				e.getMessage() + ". Falling back to '" + nextOption + "'");
			init(datasetOptions, context);
    	}
	}
    
	private static Generator<String> createSource(String datasetName) {
	    return new WeightedDatasetCSVGenerator<String>(FILENAME_PATTERN, datasetName, REGION, Encodings.UTF_8);
    }

}
