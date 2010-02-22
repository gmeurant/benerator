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

package org.databene.commons.converter;

import java.net.URLEncoder;

import org.databene.commons.ConversionException;
import org.databene.commons.Encodings;

/**
 * Decodes a String from the <code>application/x-www-form-urlencoded</code>  MIME format.
 * @see java.net.URLDecoder <br/>
 * <br/>
 * Created at 04.07.2009 07:21:15
 * @since 0.5.0
 * @author Volker Bergmann
 */

public class URLDecodeConverter extends AbstractConverter<String, String> {
	
	private String encoding;
	
    public URLDecodeConverter() {
	    this(Encodings.UTF_8);
    }

    public URLDecodeConverter(String encoding) {
	    super(String.class, String.class);
    }

    public String convert(String sourceValue) throws ConversionException {
	    try {
	        return URLEncoder.encode(sourceValue, encoding);
        } catch (Exception e) {
        	throw new ConversionException("URLDecoding of '" + sourceValue 
        			+ "' failed for encoding '" + encoding + "'", e);
        }
    }

}
