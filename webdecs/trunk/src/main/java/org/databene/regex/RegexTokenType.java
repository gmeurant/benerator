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

package org.databene.regex;

/**
 * Provides all token types that may occur in a regular expression.<br/>
 * <br/>
 * Created: 20.08.2006 10:36:14
 */
public enum RegexTokenType {

    /** Indicates that the end of a regular expression has been reached. */
    END,

    /** Indicates a character */
    CHARACTER,

    /** Indicates a predefined character class, e.g. '\d' for digits (0-9) */
    PREDEFINED_CLASS,

    /** Indicates the start of a group '(' */
    GROUP_START,

    /** Indicates the end of a group ')' */
    GROUP_END,

    /** Indicates a separator '|' */
    ALTERNATIVE_SEPARATOR,

    /** Indicates the start of a character set '[' */
    SET_START,

    /** Indicates the end of a character set ']' */
    SET_END,

    /** Indicates a negation '^' */
    NEGATION,

    /** Indicates a quantifier, '?', '*' or '+' */
    QUANTIFIER,

    /** Indicates the beginning of a range quantifier '{' */
    QUANTIFIER_START,

    /** Indicates the end of a range quantifier '}' */
    QUANTIFIER_END,

    /** Indicates a digit '0' - '9' */
    DIGIT
}
