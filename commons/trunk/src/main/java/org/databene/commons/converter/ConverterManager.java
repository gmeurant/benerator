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

package org.databene.commons.converter;

import org.databene.commons.ConfigurationError;
import org.databene.commons.IOUtil;
import org.databene.commons.BeanUtil;
import org.databene.commons.ReaderLineIterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.io.IOException;

/**
 * Manages typed converters. A default configuration is provided and can be overwritten by a local file 'converters.txt',
 * that lists each convertercustom 's class name, one name per line, e.g.
 * <pre>
 *     com.my.MyString2ThingConverter
 *     com.my.MyString2ComplexConverter
 * </pre
 * <br/>
 * Created: 04.08.2007 19:43:17
 */
public class ConverterManager {

    private static final Log logger = LogFactory.getLog(ConverterManager.class);

    private static ConverterManager instance;

    private List<BidirectionalConverter> converters;
    private static final String DEFAULT_SETUP_FILENAME = "org/databene/commons/converter/converters.txt";
    private static final String CUSTOM_SETUP_FILENAME = "converters.txt";

    public static ConverterManager getInstance() {
        if (instance == null)
            instance = new ConverterManager();
        return instance;
    }

    public BidirectionalConverter getConverter(Class srcType, Class dstType) {
        if (srcType == dstType || (dstType.isAssignableFrom(srcType) && !dstType.isPrimitive()))
            return new NoOpConverter();
        for (BidirectionalConverter converter : converters) {
            if (converter.getSourceType() == srcType && converter.getTargetType() == dstType)
                return converter;
            else if (converter.getSourceType() == dstType && converter.getTargetType() == srcType)
                return new ReverseConverter(converter);
        }
        return null;
    }

    public void register(BidirectionalConverter converter) {
        converters.add(converter);
    }

    // private helpers -------------------------------------------------------------------------------------------------

    private ConverterManager() {
        this.converters = new ArrayList<BidirectionalConverter>();
        try {
            if (IOUtil.isURIAvailable(CUSTOM_SETUP_FILENAME))
                readConfigFile(CUSTOM_SETUP_FILENAME);
            else
                logger.info("No custom converter setup '" + CUSTOM_SETUP_FILENAME + "' found; using defaults.");
            readConfigFile(DEFAULT_SETUP_FILENAME);
        } catch (IOException e) {
            throw new ConfigurationError("Error reading setup file: " + DEFAULT_SETUP_FILENAME);
        }
    }

    private void readConfigFile(String filename) throws IOException {
        ReaderLineIterator iterator = new ReaderLineIterator(IOUtil.getReaderForURI(filename));
        while (iterator.hasNext()) {
            String className = iterator.next();
            BidirectionalConverter converter = (BidirectionalConverter) BeanUtil.newInstance(className);
            register(converter);
        }
        iterator.close();
    }


}
