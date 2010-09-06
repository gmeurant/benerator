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

import java.util.HashMap;

/**
 * Entity enumeration.<br/>
 * <br/>
 * Created: 26.01.2007 10:04:35
 * @author Volker Bergmann
 */
public class HTMLEntity {
	
	private static HashMap<String, HTMLEntity> HTML_CODES = new HashMap<String, HTMLEntity>();
	private static HashMap<Integer, HTMLEntity> NUMBERS = new HashMap<Integer, HTMLEntity>();

	private static HTMLEntity[] ENTITIES = {
	    new HTMLEntity("nbsp",   160),
	    new HTMLEntity("iexcl",  161),
	    new HTMLEntity("cent",   162),
	    new HTMLEntity("pound",  163),
	    new HTMLEntity("curren", 164),
	    new HTMLEntity("yen",    165),
	    new HTMLEntity("brvbar", 166),
	    new HTMLEntity("sect",   167),
	    new HTMLEntity("uml",    168),
	    new HTMLEntity("copy",   169),
	    new HTMLEntity("ordf",   170),
	    new HTMLEntity("laquo",  171),
	    new HTMLEntity("not",    172),
	    new HTMLEntity("shy",    173),
	    new HTMLEntity("reg",    174),
	    new HTMLEntity("macr",   175),
	    new HTMLEntity("deg",    176),
	    new HTMLEntity("plusmn", 177),
	    new HTMLEntity("sup2",   178),
	    new HTMLEntity("sup3",   179),
	    new HTMLEntity("acute",  180),
	    new HTMLEntity("micro",  181),
	    new HTMLEntity("para",   182),
	    new HTMLEntity("middot", 183),
	    new HTMLEntity("cedil",  184),
	    new HTMLEntity("sup1",   185),
	    new HTMLEntity("ordm",   186),
	    new HTMLEntity("raquo",  187),
	    new HTMLEntity("frac14", 188),
	    new HTMLEntity("frac12", 189),
	    new HTMLEntity("frac34", 190),
	    new HTMLEntity("iquest", 191),
	    new HTMLEntity("Agrave", 192),
	    new HTMLEntity("Aacute", 193),
	    new HTMLEntity("Acirc",  194),
	    new HTMLEntity("Atilde", 195),
	    new HTMLEntity("Auml",   196),
	    new HTMLEntity("Aring",  197),
	    new HTMLEntity("AElig",  198),
	    new HTMLEntity("Ccedil", 199),
	    new HTMLEntity("Egrave", 200),
	    new HTMLEntity("Eacute", 201),
	    new HTMLEntity("Ecirc",  202),
	    new HTMLEntity("Euml",   203),
	    new HTMLEntity("Igrave", 204),
	    new HTMLEntity("Iacute", 205),
	    new HTMLEntity("Icirc",  206),
	    new HTMLEntity("Iuml",   207),
	    new HTMLEntity("ETH",    208),
	    new HTMLEntity("Ntilde", 209),
	    new HTMLEntity("Ograve", 210),
	    new HTMLEntity("Oacute", 211),
	    new HTMLEntity("Ocirc",  212),
	    new HTMLEntity("Otilde", 213),
	    new HTMLEntity("Ouml",   214),
	    new HTMLEntity("times",  215),
	    new HTMLEntity("Oslash", 216),
	    new HTMLEntity("Ugrave", 217),
	    new HTMLEntity("Uacute", 218),
	    new HTMLEntity("Ucirc",  219),
	    new HTMLEntity("Uuml",   220),
	    new HTMLEntity("Yacute", 221),
	    new HTMLEntity("THORN",  222),
	    new HTMLEntity("szlig",  223),
	    new HTMLEntity("agrave", 224),
	    new HTMLEntity("aacute", 225),
	    new HTMLEntity("acirc",  226),
	    new HTMLEntity("atilde", 227),
	    new HTMLEntity("auml",   228),
	    new HTMLEntity("aring",  229),
	    new HTMLEntity("aelig",  230),
	    new HTMLEntity("ccedil", 231),
	    new HTMLEntity("egrave", 232),
	    new HTMLEntity("eacute", 233),
	    new HTMLEntity("ecirc",  234),
	    new HTMLEntity("euml",   235),
	    new HTMLEntity("igrave", 236),
	    new HTMLEntity("iacute", 237),
	    new HTMLEntity("icirc",  238),
	    new HTMLEntity("iuml",   239),
	    new HTMLEntity("eth",    240),
	    new HTMLEntity("ntilde", 241),
	    new HTMLEntity("ograve", 242),
	    new HTMLEntity("oacute", 243),
	    new HTMLEntity("ocirc",  244),
	    new HTMLEntity("otilde", 245),
	    new HTMLEntity("ouml",   246),
	    new HTMLEntity("divide", 247),
	    new HTMLEntity("oslash", 248),
	    new HTMLEntity("ugrave", 249),
	    new HTMLEntity("uacute", 250),
	    new HTMLEntity("ucirc",  251),
	    new HTMLEntity("uuml",   252),
	    new HTMLEntity("yacute", 253),
	    new HTMLEntity("thorn",  254),
	    new HTMLEntity("yuml",   255),
	    new HTMLEntity("fnof",     402),
	    new HTMLEntity("Alpha",    913),
	    new HTMLEntity("Beta",     914),
	    new HTMLEntity("Gamma",    915),
	    new HTMLEntity("Delta",    916),
	    new HTMLEntity("Epsilon",  917),
	    new HTMLEntity("Zeta",     918),
	    new HTMLEntity("Eta",      919),
	    new HTMLEntity("Theta",    920),
	    new HTMLEntity("Iota",     921),
	    new HTMLEntity("Kappa",    922),
	    new HTMLEntity("Lambda",   923),
	    new HTMLEntity("Mu",       924),
	    new HTMLEntity("Nu",       925),
	    new HTMLEntity("Xi",       926),
	    new HTMLEntity("Omicron",  927),
	    new HTMLEntity("Pi",       928),
	    new HTMLEntity("Rho",      929),
	    new HTMLEntity("Sigma",    931),
	    new HTMLEntity("Tau",      932),
	    new HTMLEntity("Upsilon",  933),
	    new HTMLEntity("Phi",      934),
	    new HTMLEntity("Chi",      935),
	    new HTMLEntity("Psi",      936),
	    new HTMLEntity("Omega",    937),
	    new HTMLEntity("alpha",    945),
	    new HTMLEntity("beta",     946),
	    new HTMLEntity("gamma",    947),
	    new HTMLEntity("delta",    948),
	    new HTMLEntity("epsilon",  949),
	    new HTMLEntity("zeta",     950),
	    new HTMLEntity("eta",      951),
	    new HTMLEntity("theta",    952),
	    new HTMLEntity("iota",     953),
	    new HTMLEntity("kappa",    954),
	    new HTMLEntity("lambda",   955),
	    new HTMLEntity("mu",       956),
	    new HTMLEntity("nu",       957),
	    new HTMLEntity("xi",       958),
	    new HTMLEntity("omicron",  959),
	    new HTMLEntity("pi",       960),
	    new HTMLEntity("rho",      961),
	    new HTMLEntity("sigmaf",   962),
	    new HTMLEntity("sigma",    963),
	    new HTMLEntity("tau",      964),
	    new HTMLEntity("upsilon",  965),
	    new HTMLEntity("phi",      966),
	    new HTMLEntity("chi",      967),
	    new HTMLEntity("psi",      968),
	    new HTMLEntity("omega",    969),
	    new HTMLEntity("thetasym", 977),
	    new HTMLEntity("upsih",    978),
	    new HTMLEntity("piv",      982),
	    new HTMLEntity("bull",     8226),
	    new HTMLEntity("hellip",   8230),
	    new HTMLEntity("prime",    8242),
	    new HTMLEntity("Prime",    8243),
	    new HTMLEntity("oline",    8254),
	    new HTMLEntity("frasl",    8260),
	    new HTMLEntity("weierp",   8472),
	    new HTMLEntity("image",    8465),
	    new HTMLEntity("real",     8476),
	    new HTMLEntity("trade",    8482),
	    new HTMLEntity("alefsym",  8501),
	    new HTMLEntity("larr",     8592),
	    new HTMLEntity("uarr",     8593),
	    new HTMLEntity("rarr",     8594),
	    new HTMLEntity("darr",     8595),
	    new HTMLEntity("harr",     8596),
	    new HTMLEntity("crarr",    8629),
	    new HTMLEntity("lArr",     8656),
	    new HTMLEntity("uArr",     8657),
	    new HTMLEntity("rArr",     8658),
	    new HTMLEntity("dArr",     8659),
	    new HTMLEntity("hArr",     8660),
	    new HTMLEntity("forall",   8704),
	    new HTMLEntity("part",     8706),
	    new HTMLEntity("exist",    8707),
	    new HTMLEntity("empty",    8709),
	    new HTMLEntity("nabla",    8711),
	    new HTMLEntity("isin",     8712),
	    new HTMLEntity("notin",    8713),
	    new HTMLEntity("ni",       8715),
	    new HTMLEntity("prod",     8719),
	    new HTMLEntity("sum",      8721),
	    new HTMLEntity("minus",    8722),
	    new HTMLEntity("lowast",   8727),
	    new HTMLEntity("radic",    8730),
	    new HTMLEntity("prop",     8733),
	    new HTMLEntity("infin",    8734),
	    new HTMLEntity("ang",      8736),
	    new HTMLEntity("and",      8743),
	    new HTMLEntity("or",       8744),
	    new HTMLEntity("cap",      8745),
	    new HTMLEntity("cup",      8746),
	    new HTMLEntity("int",      8747),
	    new HTMLEntity("there4",   8756),
	    new HTMLEntity("sim",      8764),
	    new HTMLEntity("cong",     8773),
	    new HTMLEntity("asymp",    8776),
	    new HTMLEntity("ne",       8800),
	    new HTMLEntity("equiv",    8801),
	    new HTMLEntity("le",       8804),
	    new HTMLEntity("ge",       8805),
	    new HTMLEntity("sub",      8834),
	    new HTMLEntity("sup",      8835),
	    new HTMLEntity("nsub",     8836),
	    new HTMLEntity("sube",     8838),
	    new HTMLEntity("supe",     8839),
	    new HTMLEntity("oplus",    8853),
	    new HTMLEntity("otimes",   8855),
	    new HTMLEntity("perp",     8869),
	    new HTMLEntity("sdot",     8901),
	    new HTMLEntity("lceil",    8968),
	    new HTMLEntity("rceil",    8969),
	    new HTMLEntity("lfloor",   8970),
	    new HTMLEntity("rfloor",   8971),
	    new HTMLEntity("lang",     9001),
	    new HTMLEntity("rang",     9002),
	    new HTMLEntity("loz",      9674),
	    new HTMLEntity("spades",   9824),
	    new HTMLEntity("clubs",    9827),
	    new HTMLEntity("hearts",   9829),
	    new HTMLEntity("diams",    9830),
	    new HTMLEntity("quot",    34),
	    new HTMLEntity("amp",     38),
	    new HTMLEntity("lt",      60),
	    new HTMLEntity("gt",      62),
	    new HTMLEntity("OElig",   338),
	    new HTMLEntity("oelig",   339),
	    new HTMLEntity("Scaron",  352),
	    new HTMLEntity("scaron",  353),
	    new HTMLEntity("Yuml",    376),
	    new HTMLEntity("circ",    710),
	    new HTMLEntity("tilde",   732),
	    new HTMLEntity("ensp",    8194),
	    new HTMLEntity("emsp",    8195),
	    new HTMLEntity("thinsp",  8201),
	    new HTMLEntity("zwnj",    8204),
	    new HTMLEntity("zwj",     8205),
	    new HTMLEntity("lrm",     8206),
	    new HTMLEntity("rlm",     8207),
	    new HTMLEntity("ndash",   8211),
	    new HTMLEntity("mdash",   8212),
	    new HTMLEntity("lsquo",   8216),
	    new HTMLEntity("rsquo",   8217),
	    new HTMLEntity("sbquo",   8218),
	    new HTMLEntity("ldquo",   8220),
	    new HTMLEntity("rdquo",   8221),
	    new HTMLEntity("bdquo",   8222),
	    new HTMLEntity("dagger",  8224),
	    new HTMLEntity("Dagger",  8225),
	    new HTMLEntity("permil",  8240),
	    new HTMLEntity("lsaquo",  8249),
	    new HTMLEntity("rsaquo",  8250),
	    new HTMLEntity("euro",    8364)
    };

    public final String htmlCode;
    public final int xmlCode;
    public final char character;

    public HTMLEntity(String htmlCode, int xmlCode) {
        this.htmlCode = htmlCode;
        this.xmlCode = xmlCode;
        this.character = (char) xmlCode;
        HTML_CODES.put(htmlCode, this);
        NUMBERS.put(xmlCode, this);
    }

    public static HTMLEntity[] getAllInstances() {
        return ENTITIES;
    }

    public static HTMLEntity getEntity(String s, int position) {
    	int semIndex = s.indexOf(';', position);
    	if (semIndex < 0)
    		return null;
    	boolean num = (s.charAt(position + 1) == '#');
    	boolean hex = (num ? s.charAt(position + 2) == 'x' : false);
    	if (hex)
    		return findByNumber(Integer.parseInt(s.substring(position + 3, semIndex), 16));
    	else if (num)
    		return findByNumber(Integer.parseInt(s.substring(position + 2, semIndex)));
    	else
    		return findByHtmlCode(s.substring(position + 1, semIndex));
    }
    
    private static HTMLEntity findByHtmlCode(String code) {
	    return HTML_CODES.get(code);
    }

	private static HTMLEntity findByNumber(int number) {
	    return NUMBERS.get(number);
    }

	@Override
    public String toString() {
        return htmlCode;
    }
}
