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

package org.databene.text;

import junit.framework.TestCase;

import java.io.IOException;

/**
 * (c) Copyright 2006 by Volker Bergmann
 * Created: 03.09.2006 19:29:56
 */
public class DelocalizingConverterTest extends TestCase {

    public void testConversion() throws IOException {
        checkConversion("Abc", "Abc");
        checkConversion("ƒ÷‹‰ˆ¸ﬂ", "AeOeUeaeoeuess");
        checkConversion("·‡‚a", "aaaa");
        checkConversion("ÈËÍe", "eeee");
        checkConversion("ÌÏÓi", "iiii");
        checkConversion("ÛÚÙo", "oooo");
        checkConversion("˙˘˚u", "uuuu");
    }

    private void checkConversion(String source, String expectedResult) throws IOException {
        DelocalizingConverter converter = new DelocalizingConverter();
        String result = converter.convert(source);
        assertEquals("Delocalization failed. ", expectedResult, result);
    }

}
