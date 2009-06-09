/*
 * (c) Copyright 2009 by Volker Bergmann. All rights reserved.
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

package org.databene.benclipse.internal.content;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.eclipse.core.runtime.content.IContentDescription;
import org.xml.sax.InputSource;

/**
 * Content describer implementation for Benerator descriptor files.<br/>
 * <br/>
 * Created at 10.02.2009 20:01:33
 * @since 0.5.9
 * @author Volker Bergmann
 */

public class BeneratorDescriptorContentDescriber extends XMLContentDescriber {
	  /**
	   * Determines the validation status for the given contents.
	   * 
	   * @param contents the contents to be evaluated
	   * @return one of the following:<ul>
	   * <li><code>VALID</code></li>,
	   * <li><code>INVALID</code></li>,
	   * <li><code>INDETERMINATE</code></li>
	   * </ul>
	   * @throws IOException
	   */
	  private int checkCriteria(InputSource contents) throws IOException {
		  /* TODO v0.6 parse benerator descriptor 
	    PomHandler pomHandler = new PomHandler();
	    try {
	      if(!pomHandler.parseContents(contents)) {
	        return INDETERMINATE;
	      }
	    } catch(SAXException e) {
	      // we may be handed any kind of contents... it is normal we fail to parse
	      return INDETERMINATE;
	    } catch(ParserConfigurationException e) {
	      // some bad thing happened - force this describer to be disabled
	      String message = "Internal Error: XML parser configuration error during content description for Ant buildfiles";
	      throw new RuntimeException(message);
	    }

	    // Check to see if we matched our criteria.
	    if(pomHandler.hasRootProjectElement()) {
	      if(pomHandler.hasArtifactIdElement()) {
	        //project and artifactId element 
	        return VALID;
	      }
	      //only a top level project element: maybe a POM file, but maybe an Ant buildfile, a site descriptor, ...
	      return INDETERMINATE;
	    }
	    return INDETERMINATE;
	    */
		  return INDETERMINATE;
	}

	@Override
	public int describe(InputStream contents, IContentDescription description) throws IOException {
		// call the basic XML describer to do basic recognition
		if (super.describe(contents, description) == INVALID) {
			return INVALID;
		}
		// super.describe will have consumed some chars, need to rewind
		contents.reset();
		// Check to see if we matched our criteria.
		return checkCriteria(new InputSource(contents));
	}

	@Override
	public int describe(Reader contents, IContentDescription description) throws IOException {
		// call the basic XML describer to do basic recognition
		if (super.describe(contents, description) == INVALID) {
			return INVALID;
		}
		// super.describe will have consumed some chars, need to rewind
		contents.reset();
		// Check to see if we matched our criteria.
		return checkCriteria(new InputSource(contents));
	}
}
