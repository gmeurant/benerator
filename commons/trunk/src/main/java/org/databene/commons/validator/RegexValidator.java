/*
 * (c) Copyright 2009 by Volker Bergmann. All rights reserved.
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

package org.databene.commons.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Pattern;

import org.databene.commons.Validator;

/**
 * Databene {@link Validator} and JSR 303 {@link ConstraintValidator} implementation 
 * that validates a String by a regular expression.<br/>
 * <br/>
 * Created at 15.07.2009 15:22:21
 * @since 0.6.0
 * @author Volker Bergmann
 */

public class RegexValidator implements Validator<String>, ConstraintValidator<Pattern, String> {
	
	// TODO support Pattern.flags
	
	private String regexp;
	private java.util.regex.Pattern pattern;

    public RegexValidator(String regexp) {
	    setRegexp(regexp);
    }

	public void initialize(Pattern params) {
	    setRegexp(params.regexp());
    }

    public boolean isValid(String string, ConstraintValidatorContext context) {
	    return valid(string);
    }

    public String getRegexp() {
	    return regexp;
    }

    public void setRegexp(String regexp) {
	    this.regexp = regexp;
	    this.pattern = java.util.regex.Pattern.compile(regexp);
    }

    public boolean valid(String string) {
    	return pattern.matcher(string).matches();
    }

}
