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

package org.databene.html;

import junit.framework.TestCase;

import java.io.*;
import java.text.ParseException;

import org.databene.commons.SystemInfo;


/**
 * Created: 25.01.2007 17:31:28
 */
public class HTML2XMLTest extends TestCase {

    private static String SEP = SystemInfo.lineSeparator();

    private static final String HTML1 = "<html>" + SEP +
            "\t<?XXX level=\"3\"?>" + SEP +
            "\t<!-- some comment -->" + SEP +
            "\t<tr>" + SEP +
            "\t\t<td>" + SEP +
            "\t\t\t<a></td>" + SEP +
            "\t\t<td>" + SEP +
            "\t\t\t<img src=\"http://databene.org\"></td>" + SEP +
            "\t\t<td>" + SEP +
            "\t\t\t<img></td>" + SEP +
            "\t</tr>" + SEP +
            "</html>";

    private static final String XML1 =
        "<?xml version=\"1.0\"?>" + SEP +
        "<html>" + SEP +
        "\t<?XXX level=\"3\"?>" + SEP +
        "\t<!-- some comment -->" + SEP +
        "\t<tr>" + SEP +
        "\t\t<td>" + SEP +
        "\t\t\t<a></a></td>" + SEP +
        "\t\t<td>" + SEP +
        "\t\t\t<img src=\"http://databene.org\"></img></td>" + SEP +
        "\t\t<td>" + SEP +
        "\t\t\t<img></img></td>" + SEP +
        "\t</tr>" + SEP +
        "</html>";

    private static final String HTML2 = 
    	"<a><img src=\"http://databene.org\"><br></a>";

    private static final String XML2 =
        "<?xml version=\"1.0\"?>" + SEP +
        "<html><a><img src=\"http://databene.org\"></img><br></br></a></html>";

    private static final String HTML3 = 
    	"R&B";

    private static final String XML3 =
        "<?xml version=\"1.0\"?>" + SEP +
        "<html>R&amp;B</html>";

    public void testNormal() throws IOException, ParseException {
		check(HTML1, XML1);
    }

    public void testMissingHtml() throws IOException, ParseException {
		check(HTML2, XML2);
    }

    public void testAmpersand() throws IOException, ParseException {
		check(HTML3, XML3);
    }

	private void check(String source, String result) throws IOException,
			ParseException {
		StringReader in = new StringReader(source);
        StringWriter out = new StringWriter();
        HTML2XML.convert(in, out);
        in.close();
        out.close();
		assertEquals(result, out.toString());
	}
}
