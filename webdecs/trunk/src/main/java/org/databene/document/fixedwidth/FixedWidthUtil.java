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

package org.databene.document.fixedwidth;

import org.databene.commons.StringUtil;
import org.databene.commons.ConfigurationError;
import org.databene.commons.format.Alignment;

import java.text.ParsePosition;
import java.text.NumberFormat;

/**
 * Provides utility methods for processing flat files.<br/>
 * <br/>
 * Created: 03.09.2007 23:39:57
 * @author Volker Bergmann
 */
public class FixedWidthUtil {

    public static FixedWidthColumnDescriptor[] parseProperties(String properties) {
        if (properties == null)
            return null;
        String[] propertyFormats = StringUtil.tokenize(properties, ',');
        FixedWidthColumnDescriptor[] descriptors = new FixedWidthColumnDescriptor[propertyFormats.length];
        for (int i = 0; i < propertyFormats.length; i++) {
            String propertyFormat = propertyFormats[i];
            int lbIndex = propertyFormat.indexOf('[');
            if (lbIndex < 0)
                throw new ConfigurationError("'[' expected in property format descriptor '" + propertyFormat + "'");
            int rbIndex = propertyFormat.indexOf(']');
            if (rbIndex < 0)
                throw new ConfigurationError("']' expected in property format descriptor '" + propertyFormat + "'");
            String propertyName = propertyFormat.substring(0, lbIndex);
            ParsePosition pos = new ParsePosition(lbIndex + 1);
            int width = NumberFormat.getInstance().parse(propertyFormat, pos).intValue();
            Alignment alignment = Alignment.LEFT;
            if (pos.getIndex() < rbIndex) {
                char alignmentCode = propertyFormat.charAt(pos.getIndex());
                switch (alignmentCode) {
                    case 'l' : alignment = Alignment.LEFT; break;
                    case 'r' : alignment = Alignment.RIGHT; break;
                    case 'c' : alignment = Alignment.CENTER; break;
                    default: throw new ConfigurationError("Illegal alignment code '" + alignmentCode + "' in property format descriptor '" + propertyFormat + "'");
                }
                pos.setIndex(pos.getIndex() + 1);
            }
            char padChar = ' ';
            if (pos.getIndex() < rbIndex) {
                padChar = propertyFormat.charAt(pos.getIndex());
                pos.setIndex(pos.getIndex() + 1);
            }
            assert pos.getIndex() == rbIndex;
            descriptors[i] = new FixedWidthColumnDescriptor(propertyName, width, alignment, padChar);
        }
        return descriptors;
    }
}
