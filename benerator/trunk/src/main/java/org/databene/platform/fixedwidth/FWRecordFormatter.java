/*
 * (c) Copyright 2014 by Volker Bergmann. All rights reserved.
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

package org.databene.platform.fixedwidth;

import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Locale;

import org.databene.commons.Assert;
import org.databene.commons.ConfigurationError;
import org.databene.commons.Converter;
import org.databene.commons.converter.AccessingConverter;
import org.databene.commons.converter.ConverterChain;
import org.databene.commons.converter.FormatFormatConverter;
import org.databene.formats.fixedwidth.FixedWidthColumnDescriptor;
import org.databene.formats.fixedwidth.FixedWidthUtil;
import org.databene.model.data.ComponentAccessor;
import org.databene.model.data.Entity;

/**
 * Formats Entities' attributes as a fixed-width table.<br/><br/>
 * Created: 20.02.2014 14:03:25
 * @since 0.9.0
 * @author Volker Bergmann
 */

public class FWRecordFormatter {

    private Converter<Entity, String> converters[];

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public FWRecordFormatter(String columnFormatList, String nullString, Locale locale) {
        Assert.notNull(columnFormatList, "columnFormatList");
        try {
            FixedWidthColumnDescriptor[] descriptors = FixedWidthUtil.parseBeanColumnsSpec(columnFormatList, "", nullString, locale).getColumnDescriptors();
            this.converters = new Converter[descriptors.length];
            for (int i = 0; i < descriptors.length; i++) {
            	FixedWidthColumnDescriptor descriptor = descriptors[i];
                ConverterChain<Entity, String> chain = new ConverterChain<Entity, String>();
                chain.addComponent(new AccessingConverter<Entity, Object>(Entity.class, Object.class, new ComponentAccessor(descriptor.getName())));
                chain.addComponent(new FormatFormatConverter(String.class, descriptor.getFormat(), true));
                this.converters[i] = chain;
            }
        } catch (ParseException e) {
            throw new ConfigurationError("Invalid column definition: " + columnFormatList, e);
        }
    }
    
    public void format(Entity entity, PrintWriter printer) {
        for (Converter<Entity, String> converter : converters)
			printer.print(converter.convert(entity));
    }
    
}
