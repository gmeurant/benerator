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

package org.databene.document.csv;

import junit.framework.TestCase;

import java.io.StringWriter;
import java.io.IOException;

import org.databene.script.ConstantScript;
import org.databene.document.csv.ArrayCSVWriter;
import org.databene.commons.SystemInfo;

/**
 * Created: 16.06.2007 06:07:52
 */
public class ArrayCSVWriterTest extends TestCase {

    private static final String SEP = SystemInfo.lineSeparator();

    private static String RESULT =
            "header" + SEP + "1;2;3" + SEP + "4;5;6" + SEP + "footer";

    public void test() throws IOException {
        StringWriter out = new StringWriter();
        ArrayCSVWriter<Integer> writer = new ArrayCSVWriter<Integer>(out, ';',
                new ConstantScript("header" + SEP), new ConstantScript("footer"));
        writer.writeElement(new Integer[] { 1, 2, 3 });
        writer.writeElement(new Integer[] { 4, 5, 6 });
        writer.close();
        assertEquals(RESULT, out.toString());
    }
}
