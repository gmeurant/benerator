/*
 * (c) Copyright 2009-2010 by Volker Bergmann. All rights reserved.
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

package org.databene.domain.lang;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Represents a language.<br/>
 * <br/>
 * Created at 16.07.2009 19:20:15
 * @since 0.6.0
 * @author Volker Bergmann
 */

public class Language {
	
	private Locale locale;
	private LanguageResourceBundle bundle;

	public static Language getInstance(Locale locale) {
		return new Language(locale);
	}

	public Language(Locale locale) {
	    this.locale = locale;
	    this.bundle = (LanguageResourceBundle) ResourceBundle.getBundle(LanguageResourceBundle.class.getName(), locale);
    }
	
	public Locale getLocale() {
    	return locale;
    }

	public String definiteArticle(int gender, boolean plural) {
		return bundle.getString("definite.article." + (plural ? "plural." : "singular.") + gender);
	}
	
	public String indefiniteArticle(int gender, boolean plural) {
		return bundle.getString("indefinite.article." + (plural ? "plural." : "singular.") + gender);
	}
	
}
