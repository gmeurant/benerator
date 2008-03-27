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

import org.databene.commons.IOUtil;
import org.databene.commons.SystemInfo;

import java.io.*;
import java.text.ParseException;
import java.util.Stack;
import java.util.Map;

/**
 * Frovides utility methods for converting HTML to XML.<br/>
 * <br/>
 * Created: 25.01.2007 17:10:37
 * @author Volker Bergmann
 */
public class HTML2XML {

    public static String convert(String html) throws ParseException {
        Reader reader = new StringReader(html);
        StringWriter writer = new StringWriter();
        try {
            convert(reader, writer);
            return writer.getBuffer().toString();
        } catch (IOException e) {
            throw new RuntimeException(e); // this is not supposed to happen
        } finally {
            IOUtil.close(reader);
        }
    }

    public static void convert(Reader reader, Writer writer) throws IOException, ParseException {
        writeHeader(writer);
        HTMLTokenizer tokenizer = new DefaultHTMLTokenizer(reader);
        Stack<String> path = new Stack<String>();
        int token;
        boolean rootCreated = false;
        while ((token = tokenizer.nextToken()) != HTMLTokenizer.END) {
            switch (token) {
                case (HTMLTokenizer.START_TAG) :
                    if ("script".equalsIgnoreCase(tokenizer.name()))
                        continue;
                    if (!rootCreated && !"html".equals(tokenizer.name())) {
                        // ensure that there is an html root tag
                        writeStartTag(writer, tokenizer);
                        path.push(tokenizer.name());
                        rootCreated = true;
                    } else {
                        if (path.size() > 0) {
                            String lastTagName = path.peek();
                            if (HTMLUtil.isEmptyTag(lastTagName) && !tokenizer.name().equals(lastTagName)) {
                                writer.write("</" + lastTagName + '>');
                                path.pop();
                            }
                        }
                    }
                    writeStartTag(writer, tokenizer);
                    rootCreated = true;
                    path.push(tokenizer.name());
                    break;
                case (HTMLTokenizer.CLOSED_TAG):
                    writeEmptyTag(writer, tokenizer);
                case (HTMLTokenizer.END_TAG):
                    if ("script".equalsIgnoreCase(tokenizer.name()))
                        continue;
                    boolean done = false;
                    if (contains(path, tokenizer.name())) {
                        do {
                            String pathTagName = path.pop();
                            writer.write("</" + pathTagName + '>');
                            if (pathTagName.equals(tokenizer.name()))
                                done = true;
                        } while (!done);
                    }
                    if ("html".equalsIgnoreCase(tokenizer.name()))
                        return;
                    break;
                case HTMLTokenizer.TEXT:
                    writeText(writer, tokenizer.text());
                    break;
                case HTMLTokenizer.COMMENT:
                    writeText(writer, tokenizer.text());
                    break;
                case HTMLTokenizer.DOCUMENT_TYPE:
                    // leave out doc type
                    break;
                case HTMLTokenizer.PROCESSING_INSTRUCTION:
                    writeText(writer, tokenizer.text());
                    break;
                case HTMLTokenizer.SCRIPT:
                    // ignore this
                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported token type: " + token);
            }
        }
        while (path.size() > 0) {
            String tagName = path.pop();
            writer.write("</" + tagName + '>');
        }
    }

    private static boolean contains(Stack<String> path, String name) {
        for (String tagName : path)
            if (tagName.equals(name))
                return true;
        return false;
    }

    private static void writeHeader(Writer writer) throws IOException {
        writer.write("<?xml version=\"1.0\"?>" + SystemInfo.lineSeparator());
    }

    private static void writeEmptyTag(Writer writer, HTMLTokenizer tokenizer) throws IOException {
        writer.write('<' + tokenizer.name());
        writeAttributes(writer, tokenizer);
        writer.write("/>");
    }

    private static void writeStartTag(Writer writer, HTMLTokenizer tokenizer) throws IOException {
        writer.write('<' + tokenizer.name());
        writeAttributes(writer, tokenizer);
        writer.write('>');
    }

    private static void writeAttributes(Writer writer, HTMLTokenizer tokenizer) throws IOException {
        for (Map.Entry<String,String> entry : tokenizer.attributes().entrySet()) {
            String value = entry.getValue();
            char quote = '"';
            if (value == null)
                value = "";
            else if (value.contains("\""))
                quote = '\'';
            writer.write(' ');
            writer.write(entry.getKey());
            writer.write('=');
            writer.write(quote);
            writeText(writer, value);
            writer.write(quote);
        }
    }

    private static void writeText(Writer writer, String s) throws IOException {
        int i;
        while ((i = s.indexOf('&')) >= 0) {
            HTMLEntity entity = HTMLEntity.getEntity(s, i);
            if (entity != null) {
                writer.write(s.substring(0, i + 1));
                writer.write("#" + entity.xmlCode + ";");
                s = s.substring(i + entity.htmlCode.length() + 2);
            } else {
                writer.write(s.substring(0, i));
                writer.write("&amp;");
                s = s.substring(i + 1);
            }
        }
        writer.write(s);
    }

}
