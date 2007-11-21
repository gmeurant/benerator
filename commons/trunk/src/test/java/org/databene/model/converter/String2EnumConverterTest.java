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

package org.databene.model.converter;

import junit.framework.TestCase;
import org.databene.SomeEnum;
import org.databene.model.ConversionException;

/**
 * Tests the String2EnumConverter.<br/>
 * <br/>
 * Created: 20.08.2007 07:14:04
 */
public class String2EnumConverterTest extends TestCase {

    public void testNull() throws ConversionException {
        assertNull(String2EnumConverter.convert(null, SomeEnum.class));
    }

    public void testNormal() throws ConversionException {
        for (SomeEnum instance : SomeEnum.values()) {
            check(instance);
        }
    }

    public void testIllegalArgument() {
        try {
            String2EnumConverter.convert("0", SomeEnum.class);
            fail("ConversionException expected");
        } catch (ConversionException e) {
            // this is the required result
        }
    }

    // private helpers -------------------------------------------------------------------------------------------------

    private void check(SomeEnum instance) throws ConversionException {
        String2EnumConverter converter = new String2EnumConverter(instance.getClass());
        String name = instance.name();
        assertEquals(instance, converter.convert(name));
        assertEquals(name, converter.revert(instance));
    }
}
