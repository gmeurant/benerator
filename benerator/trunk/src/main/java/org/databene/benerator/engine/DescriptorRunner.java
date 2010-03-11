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

package org.databene.benerator.engine;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.databene.commons.IOUtil;
import org.databene.commons.RoundedNumberFormat;
import org.databene.commons.xml.XMLUtil;
import org.databene.model.consumer.FileExporter;
import org.databene.model.data.DataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import static org.databene.benerator.engine.DescriptorConstants.*;

/**
 * Parses and executes a benerator descriptor file.<br/>
 * <br/>
 * Created at 26.02.2009 15:51:59
 * @since 0.5.8
 * @author Volker Bergmann
 */

public class DescriptorRunner implements ResourceManager {

    public static final String LOCALE_VM_PARAM = "benerator.locale";
	
	private static final Logger logger = LoggerFactory.getLogger(DescriptorRunner.class);

	// attributes ------------------------------------------------------------------------------------------------------

	private String uri;

	private BeneratorContext context;

	DataModel dataModel = DataModel.getDefaultInstance();
	private List<String> generatedFiles;
	
	private ResourceManagerSupport resourceManager = new ResourceManagerSupport();
	long startTime = 0;

	
	// constructor -----------------------------------------------------------------------------------------------------
	
	public DescriptorRunner(String uri) {
		this.uri = uri;
		this.context = new BeneratorContext(".");
		this.generatedFiles = new ArrayList<String>();
	}
	
	// interface -------------------------------------------------------------------------------------------------------
	
	public BeneratorContext getContext() {
		return context;
	}

    public void run() throws IOException {
    	Runtime.getRuntime().addShutdownHook(new BeneratorShutdownHook(this));
		BeneratorRootStatement rootStatement = parseDescriptorFile();			
		execute(rootStatement);
	}

	public BeneratorRootStatement parseDescriptorFile() throws IOException {
	    Document document = XMLUtil.parse(uri, context.isValidate());
	    Element root = document.getDocumentElement();
	    XMLUtil.mapAttributesToProperties(root, context, true, new XMLNameNormalizer());
	    BeneratorRootStatement mainTask = new BeneratorRootStatement();
	    // process sub elements
	    for (Element element : XMLUtil.getChildElements(root)) {
			String elementName = element.getNodeName();
            DescriptorParser elementParser = ParserFactory.getParser(elementName, EL_SETUP);
	    	Statement statement = elementParser.parse(element, this);
	    	mainTask.addSubStatement(statement);
	    }
		// prepare system
		generatedFiles = new ArrayList<String>();
		context.setContextUri(IOUtil.getContextUri(uri));
	    return mainTask;
    }
	
	public void execute(BeneratorRootStatement rootStatement) {
	    try {
	    	startTime = System.currentTimeMillis();

			// run AST
			rootStatement.execute(context);
			
			// calculate and print statistics
			long elapsedTime = java.lang.System.currentTimeMillis() - startTime;
			resourceManager.close();
			StringBuilder message = new StringBuilder("Created a total of ")
				.append(context.getTotalGenerationCount()).append(" entities ");
			if (elapsedTime != 0) {
	            long throughput = context.getTotalGenerationCount() * 3600000L / elapsedTime;
	            message.append("in ").append(elapsedTime).append(" ms (~")
					.append(RoundedNumberFormat.format(throughput, 0)).append(" p.h.)");
            }
			logger.info(message.toString());
			List<String> generations = getGeneratedFiles();
			if (generations.size() > 0)
				logger.info("Generated file(s): " + generations);
		} finally {
			context.close();
		}
    }

	public List<String> getGeneratedFiles() {
		return generatedFiles;
	}

	// ResourceManager interface implementation ------------------------------------------------------------------------

	public boolean addResource(Closeable resource) {
		if (!resourceManager.addResource(resource))
			return false;
		else if (resource instanceof FileExporter)
			generatedFiles.add(((FileExporter<?>) resource).getUri());
	    return true;
    }

    public void close() {
	    resourceManager.close();
    }
    
	// java.lang.Object overrides --------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

}
