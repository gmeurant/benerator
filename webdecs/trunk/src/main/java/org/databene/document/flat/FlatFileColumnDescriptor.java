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

package org.databene.document.flat;

import org.databene.model.format.Alignment;
import org.databene.model.format.PadFormat;

/**
 * Describes a column of a flat file.<br/>
 * <br/>
 * Created: 07.06.2007 13:06:39
 */
public class FlatFileColumnDescriptor {

    private String name;
    private PadFormat format;

    public FlatFileColumnDescriptor(int width, Alignment alignment) {
        this(null, width, alignment, ' ');
    }

    public FlatFileColumnDescriptor(String name, int width, Alignment alignment) {
        this(name, width, alignment, ' ');
    }

    public FlatFileColumnDescriptor(int width, Alignment alignment, char padChar) {
        this(null, width, alignment, padChar);
    }

    public FlatFileColumnDescriptor(String name, int width, Alignment alignment, char padChar) {
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
}
