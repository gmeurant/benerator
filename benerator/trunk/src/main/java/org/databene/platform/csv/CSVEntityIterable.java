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

package org.databene.platform.csv;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.databene.commons.Converter;
import org.databene.model.data.ComplexTypeDescriptor;

/**
 * Imports Entities from a CSV file.<br/>
 * <br/>
 * Created: 26.08.2007 12:16:08
 * @author Volker Bergmann
 */
public class CSVEntityIterable extends CSVEntitySource {

	private static final Log logger = LogFactory.getLog(CSVEntityIterable.class);

	public CSVEntityIterable() {
		super();
		logger.warn(getClass().getName() + " is deprecated. " +
				"Use " + CSVEntitySource.class.getName() + " instead.");
	}

	public CSVEntityIterable(String uri, ComplexTypeDescriptor descriptor,
			Converter<String, String> preprocessor, char separator,
			String encoding) {
		super(uri, descriptor, preprocessor, separator, encoding);
		logger.warn(getClass().getName() + " is deprecated. " +
				"Use " + CSVEntitySource.class.getName() + " instead.");
	}

	public CSVEntityIterable(String uri, String entityName, char separator,
			String encoding) {
		super(uri, entityName, separator, encoding);
		logger.warn(getClass().getName() + " is deprecated. " +
				"Use " + CSVEntitySource.class.getName() + " instead.");
	}

	public CSVEntityIterable(String uri, String entityName, char separator) {
		super(uri, entityName, separator);
		logger.warn(getClass().getName() + " is deprecated. " +
				"Use " + CSVEntitySource.class.getName() + " instead.");
	}

	public CSVEntityIterable(String uri, String entityName,
			Converter<String, String> preprocessor, char separator,
			String encoding) {
		super(uri, entityName, preprocessor, separator, encoding);
		logger.warn(getClass().getName() + " is deprecated. " +
				"Use " + CSVEntitySource.class.getName() + " instead.");
	}

	public CSVEntityIterable(String uri, String entityName) {
		super(uri, entityName);
		logger.warn(getClass().getName() + " is deprecated. " +
				"Use " + CSVEntitySource.class.getName() + " instead.");
	}

}
