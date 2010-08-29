/*
 * (c) Copyright 2007-2010 by Volker Bergmann. All rights reserved.
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

import org.databene.commons.CollectionUtil;

import java.util.Set;

/**
 * Provides utility methods for HTML processing.<br/>
 * <br/>
 * Created: 15.06.2007 19:42:19
 * @author Volker Bergmann
 */
public class HTMLUtil {

    private static final Set<String> EMPTY_TAGS = CollectionUtil.toSet("br", "img", "meta", "link");

    public static boolean isEmptyTag(String tagName) {
        return EMPTY_TAGS.contains(tagName.toLowerCase());
    }

	public static String resolveEntities(String text) {
		StringBuilder result = new StringBuilder(text.length());
	    int i;
        while ((i = text.indexOf('&')) >= 0) {
            HTMLEntity entity = HTMLEntity.getEntity(text, i);
            if (entity != null) {
                result.append(text.substring(0, i));
                if ("nbsp".equals(entity.htmlCode))
                	result.append(' ');
                else if ("ndash".equals(entity.htmlCode))
                    	result.append('-');
                    else
                	result.append(entity.character);
                text = text.substring(i + entity.htmlCode.length() + 2);
            } else {
                result.append(text.substring(0, i));
                result.append("&");
                text = text.substring(i + 1);
            }
        }
        result.append(text);
        return result.toString();
    }
	
}
