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

import org.databene.commons.format.Alignment;
import org.databene.commons.format.PadFormat;

/**
 * Describes a column of a flat file.<br/>
 * <br/>
 * Created: 07.06.2007 13:06:39
 * @author Volker Bergmann
 */
public class FixedWidthColumnDescriptor {

    private String name;
    private PadFormat format;

    public FixedWidthColumnDescriptor(int width, Alignment alignment) {
        this(null, width, alignment, ' ');
    }

    public FixedWidthColumnDescriptor(String name, int width, Alignment alignment) {
        this(name, width, alignment, ' ');
    }

    public FixedWidthColumnDescriptor(int width, Alignment alignment, char padChar) {
        this(null, width, alignment, padChar);
    }

    public FixedWidthColumnDescriptor(String name, int width, Alignment alignment, char padChar) {
        this.name = name;
        this.format = new PadFormat(width, alignment, padChar);
    }

    public String getName() {
        return name;
    }

    public int getWidth() {
        return format.getLength();
    }

    public Alignment getAlignment() {
        return format.getAlignment();
    }

    public char getPadChar() {
        return format.getPadChar();
    }

    public PadFormat getFormat() {
        return format;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((format == null) ? 0 : format.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final FixedWidthColumnDescriptor other = (FixedWidthColumnDescriptor) obj;
		if (format == null) {
			if (other.format != null)
				return false;
		} else if (!format.equals(other.format))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
    
    @Override
    public String toString() {
    	return name + '[' + format.getLength() + format.getAlignment().getId() + format.getPadChar() + ']';
    }
}
