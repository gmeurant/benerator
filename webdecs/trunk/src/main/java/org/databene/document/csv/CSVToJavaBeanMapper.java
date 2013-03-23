/*
 * (c) Copyright 2007-2013 by Volker Bergmann. All rights reserved.
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

package org.databene.document.csv;

import org.databene.commons.BeanUtil;
import org.databene.commons.ConfigurationError;
import org.databene.commons.IOUtil;
import org.databene.commons.UpdateFailedException;
import org.databene.commons.bean.PropertyMutatorFactory;
import org.databene.commons.mutator.NamedMutator;
import org.databene.webdecs.DataContainer;
import org.databene.webdecs.DataIterator;
import org.databene.webdecs.util.ThreadLocalDataContainer;

import java.io.*;

/**
 * Reads a CSV file and maps its columns to JavaBeans.<br/>
 * <br/>
 * Created: 21.07.2006 15:07:36
 * @author Volker Bergmann
 */
public class CSVToJavaBeanMapper<E> implements DataIterator<E> {

    private CSVLineIterator iterator;
    private Class<E> type;
    private String emptyValue;
    
    private NamedMutator[] mutators;
    private ThreadLocalDataContainer<String[]> dataContainer = new ThreadLocalDataContainer<String[]>();

    public CSVToJavaBeanMapper(Reader reader, Class<E> type) throws IOException {
        this(reader, type, ',', null);
    }

    public CSVToJavaBeanMapper(Reader reader, Class<E> type, char separator, String emptyValue) throws IOException {
        this.iterator = new CSVLineIterator(reader, separator);
        this.type = type;
        this.emptyValue = emptyValue;
        String[] attributeNames = this.iterator.next(dataContainer.get()).getData();
        this.mutators = new NamedMutator[attributeNames.length];
        for (int i = 0; i < attributeNames.length; i++) {
            String attributeName = attributeNames[i];
            mutators[i] = PropertyMutatorFactory.getPropertyMutator(type, attributeName, false);
        }
    }

    @Override
	public Class<E> getType() {
    	return type;
    }
    
	@Override
	public DataContainer<E> next(DataContainer<E> wrapper) {
        int i = 0;
        try {
            DataContainer<String[]> tmp = iterator.next(dataContainer.get());
            if (tmp == null)
            	return null;
			String[] line = tmp.getData();
            E bean = BeanUtil.newInstance(type);
            int columns = Math.min(line.length, mutators. length);
            for (i = 0; i < columns; i++) {
                String value = line[i];
                if (value.length() == 0)
                    value = emptyValue;
                mutators[i].setValue(bean, value);
            }
            return wrapper.setData(bean);
        } catch (UpdateFailedException e) {
            throw new ConfigurationError("Failed to set property '" + 
                    mutators[i].getName() + "' on class " + type);
        }
    }

    @Override
	public void close() {
    	IOUtil.close(iterator);
    }

}
