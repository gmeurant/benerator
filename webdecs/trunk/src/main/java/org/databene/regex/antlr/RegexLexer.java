// $ANTLR 3.1.2 /Users/volker/Desktop/Regex/Regex.g 2009-09-20 18:40:33
 
	package org.databene.regex.antlr;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class RegexLexer extends Lexer {
    public static final int HEXDIGIT=25;
    public static final int T__35=35;
    public static final int RANGE=18;
    public static final int LETTER=22;
    public static final int T__36=36;
    public static final int ALPHANUM=5;
    public static final int EXCL=20;
    public static final int OCTALCHAR=9;
    public static final int CHOICE=14;
    public static final int LBRACKET=28;
    public static final int T__33=33;
    public static final int ESCAPEDEDCHARACTER=7;
    public static final int LBRACE=26;
    public static final int RBRACE=27;
    public static final int CLASS=17;
    public static final int T__30=30;
    public static final int QUANT=21;
    public static final int OCTALDIGIT=24;
    public static final int CODEDCHAR=11;
    public static final int T__31=31;
    public static final int INT=13;
    public static final int SEQUENCE=15;
    public static final int HEXCHAR=10;
    public static final int EOF=-1;
    public static final int T__32=32;
    public static final int RBRACKET=29;
    public static final int SIMPLEQUANTIFIER=12;
    public static final int INCL=19;
    public static final int DIGIT=23;
    public static final int SPECIALCHARACTER=6;
    public static final int PREDEFINEDCLASS=4;
    public static final int FACTOR=16;
    public static final int NONTYPEABLECHARACTER=8;
    public static final int T__34=34;

    	boolean numQuantifierMode = false;
    	boolean classMode = false;
    	
    	@Override
    	public Token nextToken() {
    		while (true) {
    			state.token = null;
    			state.channel = Token.DEFAULT_CHANNEL;
    			state.tokenStartCharIndex = input.index();
    			state.tokenStartCharPositionInLine = input.getCharPositionInLine();
    			state.tokenStartLine = input.getLine();
    			state.text = null;
    			if ( input.LA(1)==CharStream.EOF ) {
    				return Token.EOF_TOKEN;
    			}
    			try {
    				mTokens();
    				if ( state.token==null ) {
    					emit();
    				}
    				else if ( state.token==Token.SKIP_TOKEN ) {
    					continue;
    				}
    				return state.token;
    			}
    			catch (RecognitionException re) {
    				reportError(re);
    				throw new RuntimeException(getClass().getSimpleName() + " error", re); // or throw Error
    			}
    		}
    	}



    // delegates
    // delegators

    public RegexLexer() {;} 
    public RegexLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public RegexLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "/Users/volker/Desktop/Regex/Regex.g"; }

    // $ANTLR start "T__30"
    public final void mT__30() throws RecognitionException {
        try {
            int _type = T__30;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Desktop/Regex/Regex.g:41:7: ( '^' )
            // /Users/volker/Desktop/Regex/Regex.g:41:9: '^'
            {
            match('^'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__30"

    // $ANTLR start "T__31"
    public final void mT__31() throws RecognitionException {
        try {
            int _type = T__31;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Desktop/Regex/Regex.g:42:7: ( '$' )
            // /Users/volker/Desktop/Regex/Regex.g:42:9: '$'
            {
            match('$'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__31"

    // $ANTLR start "T__32"
    public final void mT__32() throws RecognitionException {
        try {
            int _type = T__32;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Desktop/Regex/Regex.g:43:7: ( '|' )
            // /Users/volker/Desktop/Regex/Regex.g:43:9: '|'
            {
            match('|'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__32"

    // $ANTLR start "T__33"
    public final void mT__33() throws RecognitionException {
        try {
            int _type = T__33;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Desktop/Regex/Regex.g:44:7: ( '-' )
            // /Users/volker/Desktop/Regex/Regex.g:44:9: '-'
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__33"

    // $ANTLR start "T__34"
    public final void mT__34() throws RecognitionException {
        try {
            int _type = T__34;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Desktop/Regex/Regex.g:45:7: ( '(' )
            // /Users/volker/Desktop/Regex/Regex.g:45:9: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__34"

    // $ANTLR start "T__35"
    public final void mT__35() throws RecognitionException {
        try {
            int _type = T__35;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Desktop/Regex/Regex.g:46:7: ( ')' )
            // /Users/volker/Desktop/Regex/Regex.g:46:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__35"

    // $ANTLR start "T__36"
    public final void mT__36() throws RecognitionException {
        try {
            int _type = T__36;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Desktop/Regex/Regex.g:47:7: ( ',' )
            // /Users/volker/Desktop/Regex/Regex.g:47:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__36"

    // $ANTLR start "CHOICE"
    public final void mCHOICE() throws RecognitionException {
        try {
            // /Users/volker/Desktop/Regex/Regex.g:145:8: ()
            // /Users/volker/Desktop/Regex/Regex.g:145:10: 
            {
            }

        }
        finally {
        }
    }
    // $ANTLR end "CHOICE"

    // $ANTLR start "SEQUENCE"
    public final void mSEQUENCE() throws RecognitionException {
        try {
            // /Users/volker/Desktop/Regex/Regex.g:147:9: ()
            // /Users/volker/Desktop/Regex/Regex.g:147:11: 
            {
            }

        }
        finally {
        }
    }
    // $ANTLR end "SEQUENCE"

    // $ANTLR start "FACTOR"
    public final void mFACTOR() throws RecognitionException {
        try {
            // /Users/volker/Desktop/Regex/Regex.g:149:8: ()
            // /Users/volker/Desktop/Regex/Regex.g:149:10: 
            {
            }

        }
        finally {
        }
    }
    // $ANTLR end "FACTOR"

    // $ANTLR start "CLASS"
    public final void mCLASS() throws RecognitionException {
        try {
            // /Users/volker/Desktop/Regex/Regex.g:151:7: ()
            // /Users/volker/Desktop/Regex/Regex.g:151:9: 
            {
            }

        }
        finally {
        }
    }
    // $ANTLR end "CLASS"

    // $ANTLR start "RANGE"
    public final void mRANGE() throws RecognitionException {
        try {
            // /Users/volker/Desktop/Regex/Regex.g:153:7: ()
            // /Users/volker/Desktop/Regex/Regex.g:153:9: 
            {
            }

        }
        finally {
        }
    }
    // $ANTLR end "RANGE"

    // $ANTLR start "INCL"
    public final void mINCL() throws RecognitionException {
        try {
            // /Users/volker/Desktop/Regex/Regex.g:155:6: ()
            // /Users/volker/Desktop/Regex/Regex.g:155:8: 
            {
            }

        }
        finally {
        }
    }
    // $ANTLR end "INCL"

    // $ANTLR start "EXCL"
    public final void mEXCL() throws RecognitionException {
        try {
            // /Users/volker/Desktop/Regex/Regex.g:157:6: ()
            // /Users/volker/Desktop/Regex/Regex.g:157:8: 
            {
            }

        }
        finally {
        }
    }
    // $ANTLR end "EXCL"

    // $ANTLR start "QUANT"
    public final void mQUANT() throws RecognitionException {
        try {
            // /Users/volker/Desktop/Regex/Regex.g:159:7: ()
            // /Users/volker/Desktop/Regex/Regex.g:159:9: 
            {
            }

        }
        finally {
        }
    }
    // $ANTLR end "QUANT"

    // $ANTLR start "PREDEFINEDCLASS"
    public final void mPREDEFINEDCLASS() throws RecognitionException {
        try {
            int _type = PREDEFINEDCLASS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Desktop/Regex/Regex.g:166:2: ( '.' | '\\\\d' | '\\\\D' | '\\\\s' | '\\\\S' | '\\\\w' | '\\\\W' )
            int alt1=7;
            int LA1_0 = input.LA(1);

            if ( (LA1_0=='.') ) {
                alt1=1;
            }
            else if ( (LA1_0=='\\') ) {
                switch ( input.LA(2) ) {
                case 'd':
                    {
                    alt1=2;
                    }
                    break;
                case 'D':
                    {
                    alt1=3;
                    }
                    break;
                case 's':
                    {
                    alt1=4;
                    }
                    break;
                case 'S':
                    {
                    alt1=5;
                    }
                    break;
                case 'w':
                    {
                    alt1=6;
                    }
                    break;
                case 'W':
                    {
                    alt1=7;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 1, 2, input);

                    throw nvae;
                }

            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;
            }
            switch (alt1) {
                case 1 :
                    // /Users/volker/Desktop/Regex/Regex.g:166:4: '.'
                    {
                    match('.'); 

                    }
                    break;
                case 2 :
                    // /Users/volker/Desktop/Regex/Regex.g:167:4: '\\\\d'
                    {
                    match("\\d"); 


                    }
                    break;
                case 3 :
                    // /Users/volker/Desktop/Regex/Regex.g:168:4: '\\\\D'
                    {
                    match("\\D"); 


                    }
                    break;
                case 4 :
                    // /Users/volker/Desktop/Regex/Regex.g:169:4: '\\\\s'
                    {
                    match("\\s"); 


                    }
                    break;
                case 5 :
                    // /Users/volker/Desktop/Regex/Regex.g:170:4: '\\\\S'
                    {
                    match("\\S"); 


                    }
                    break;
                case 6 :
                    // /Users/volker/Desktop/Regex/Regex.g:171:4: '\\\\w'
                    {
                    match("\\w"); 


                    }
                    break;
                case 7 :
                    // /Users/volker/Desktop/Regex/Regex.g:172:4: '\\\\W'
                    {
                    match("\\W"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "PREDEFINEDCLASS"

    // $ANTLR start "ALPHANUM"
    public final void mALPHANUM() throws RecognitionException {
        try {
            int _type = ALPHANUM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Desktop/Regex/Regex.g:175:9: ( LETTER | {...}? => DIGIT )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( ((LA2_0>='A' && LA2_0<='Z')||(LA2_0>='a' && LA2_0<='z')) ) {
                alt2=1;
            }
            else if ( ((LA2_0>='0' && LA2_0<='9')) && ((!numQuantifierMode))) {
                alt2=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // /Users/volker/Desktop/Regex/Regex.g:175:11: LETTER
                    {
                    mLETTER(); 

                    }
                    break;
                case 2 :
                    // /Users/volker/Desktop/Regex/Regex.g:176:4: {...}? => DIGIT
                    {
                    if ( !((!numQuantifierMode)) ) {
                        throw new FailedPredicateException(input, "ALPHANUM", "!numQuantifierMode");
                    }
                    mDIGIT(); 

                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ALPHANUM"

    // $ANTLR start "SPECIALCHARACTER"
    public final void mSPECIALCHARACTER() throws RecognitionException {
        try {
            int _type = SPECIALCHARACTER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Desktop/Regex/Regex.g:179:2: ( ' ' | '!' | '\\'' | '\"' | '%' | '&' | '/' | ':' | ';' | '<' | '=' | '>' | '@' | '_' | '`' | '~' | {...}? => ',' | {...}? => ( '?' | '*' | '+' ) )
            int alt3=18;
            alt3 = dfa3.predict(input);
            switch (alt3) {
                case 1 :
                    // /Users/volker/Desktop/Regex/Regex.g:179:4: ' '
                    {
                    match(' '); 

                    }
                    break;
                case 2 :
                    // /Users/volker/Desktop/Regex/Regex.g:179:10: '!'
                    {
                    match('!'); 

                    }
                    break;
                case 3 :
                    // /Users/volker/Desktop/Regex/Regex.g:179:16: '\\''
                    {
                    match('\''); 

                    }
                    break;
                case 4 :
                    // /Users/volker/Desktop/Regex/Regex.g:179:23: '\"'
                    {
                    match('\"'); 

                    }
                    break;
                case 5 :
                    // /Users/volker/Desktop/Regex/Regex.g:179:29: '%'
                    {
                    match('%'); 

                    }
                    break;
                case 6 :
                    // /Users/volker/Desktop/Regex/Regex.g:179:35: '&'
                    {
                    match('&'); 

                    }
                    break;
                case 7 :
                    // /Users/volker/Desktop/Regex/Regex.g:180:4: '/'
                    {
                    match('/'); 

                    }
                    break;
                case 8 :
                    // /Users/volker/Desktop/Regex/Regex.g:180:10: ':'
                    {
                    match(':'); 

                    }
                    break;
                case 9 :
                    // /Users/volker/Desktop/Regex/Regex.g:180:17: ';'
                    {
                    match(';'); 

                    }
                    break;
                case 10 :
                    // /Users/volker/Desktop/Regex/Regex.g:180:23: '<'
                    {
                    match('<'); 

                    }
                    break;
                case 11 :
                    // /Users/volker/Desktop/Regex/Regex.g:180:29: '='
                    {
                    match('='); 

                    }
                    break;
                case 12 :
                    // /Users/volker/Desktop/Regex/Regex.g:180:35: '>'
                    {
                    match('>'); 

                    }
                    break;
                case 13 :
                    // /Users/volker/Desktop/Regex/Regex.g:181:4: '@'
                    {
                    match('@'); 

                    }
                    break;
                case 14 :
                    // /Users/volker/Desktop/Regex/Regex.g:181:10: '_'
                    {
                    match('_'); 

                    }
                    break;
                case 15 :
                    // /Users/volker/Desktop/Regex/Regex.g:181:17: '`'
                    {
                    match('`'); 

                    }
                    break;
                case 16 :
                    // /Users/volker/Desktop/Regex/Regex.g:181:23: '~'
                    {
                    match('~'); 

                    }
                    break;
                case 17 :
                    // /Users/volker/Desktop/Regex/Regex.g:182:4: {...}? => ','
                    {
                    if ( !((!numQuantifierMode)) ) {
                        throw new FailedPredicateException(input, "SPECIALCHARACTER", "!numQuantifierMode");
                    }
                    match(','); 

                    }
                    break;
                case 18 :
                    // /Users/volker/Desktop/Regex/Regex.g:183:4: {...}? => ( '?' | '*' | '+' )
                    {
                    if ( !((classMode)) ) {
                        throw new FailedPredicateException(input, "SPECIALCHARACTER", "classMode");
                    }
                    if ( (input.LA(1)>='*' && input.LA(1)<='+')||input.LA(1)=='?' ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;}


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SPECIALCHARACTER"

    // $ANTLR start "ESCAPEDEDCHARACTER"
    public final void mESCAPEDEDCHARACTER() throws RecognitionException {
        try {
            int _type = ESCAPEDEDCHARACTER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Desktop/Regex/Regex.g:187:2: ( '\\\\$' | '\\\\&' | '\\\\(' | '\\\\)' | | '\\\\,' | '\\\\-' | '\\\\.' | '\\\\[' | '\\\\]' | '\\\\^' | '\\\\{' | '\\\\}' | '\\\\\\\\' | '\\\\|' | '\\\\*' | '\\\\+' | '\\\\?' )
            int alt4=18;
            alt4 = dfa4.predict(input);
            switch (alt4) {
                case 1 :
                    // /Users/volker/Desktop/Regex/Regex.g:187:4: '\\\\$'
                    {
                    match("\\$"); 


                    }
                    break;
                case 2 :
                    // /Users/volker/Desktop/Regex/Regex.g:187:12: '\\\\&'
                    {
                    match("\\&"); 


                    }
                    break;
                case 3 :
                    // /Users/volker/Desktop/Regex/Regex.g:187:20: '\\\\('
                    {
                    match("\\("); 


                    }
                    break;
                case 4 :
                    // /Users/volker/Desktop/Regex/Regex.g:187:28: '\\\\)'
                    {
                    match("\\)"); 


                    }
                    break;
                case 5 :
                    // /Users/volker/Desktop/Regex/Regex.g:188:2: 
                    {
                    }
                    break;
                case 6 :
                    // /Users/volker/Desktop/Regex/Regex.g:188:4: '\\\\,'
                    {
                    match("\\,"); 


                    }
                    break;
                case 7 :
                    // /Users/volker/Desktop/Regex/Regex.g:188:12: '\\\\-'
                    {
                    match("\\-"); 


                    }
                    break;
                case 8 :
                    // /Users/volker/Desktop/Regex/Regex.g:188:20: '\\\\.'
                    {
                    match("\\."); 


                    }
                    break;
                case 9 :
                    // /Users/volker/Desktop/Regex/Regex.g:188:28: '\\\\['
                    {
                    match("\\["); 


                    }
                    break;
                case 10 :
                    // /Users/volker/Desktop/Regex/Regex.g:188:36: '\\\\]'
                    {
                    match("\\]"); 


                    }
                    break;
                case 11 :
                    // /Users/volker/Desktop/Regex/Regex.g:188:44: '\\\\^'
                    {
                    match("\\^"); 


                    }
                    break;
                case 12 :
                    // /Users/volker/Desktop/Regex/Regex.g:189:4: '\\\\{'
                    {
                    match("\\{"); 


                    }
                    break;
                case 13 :
                    // /Users/volker/Desktop/Regex/Regex.g:189:12: '\\\\}'
                    {
                    match("\\}"); 


                    }
                    break;
                case 14 :
                    // /Users/volker/Desktop/Regex/Regex.g:189:20: '\\\\\\\\'
                    {
                    match("\\\\"); 


                    }
                    break;
                case 15 :
                    // /Users/volker/Desktop/Regex/Regex.g:189:29: '\\\\|'
                    {
                    match("\\|"); 


                    }
                    break;
                case 16 :
                    // /Users/volker/Desktop/Regex/Regex.g:190:4: '\\\\*'
                    {
                    match("\\*"); 


                    }
                    break;
                case 17 :
                    // /Users/volker/Desktop/Regex/Regex.g:190:12: '\\\\+'
                    {
                    match("\\+"); 


                    }
                    break;
                case 18 :
                    // /Users/volker/Desktop/Regex/Regex.g:190:20: '\\\\?'
                    {
                    match("\\?"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ESCAPEDEDCHARACTER"

    // $ANTLR start "NONTYPEABLECHARACTER"
    public final void mNONTYPEABLECHARACTER() throws RecognitionException {
        try {
            int _type = NONTYPEABLECHARACTER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Desktop/Regex/Regex.g:194:2: ( '\\\\r' | '\\\\n' | '\\\\t' | '\\\\f' | '\\\\a' | '\\\\e' )
            int alt5=6;
            int LA5_0 = input.LA(1);

            if ( (LA5_0=='\\') ) {
                switch ( input.LA(2) ) {
                case 'r':
                    {
                    alt5=1;
                    }
                    break;
                case 'n':
                    {
                    alt5=2;
                    }
                    break;
                case 't':
                    {
                    alt5=3;
                    }
                    break;
                case 'f':
                    {
                    alt5=4;
                    }
                    break;
                case 'a':
                    {
                    alt5=5;
                    }
                    break;
                case 'e':
                    {
                    alt5=6;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 5, 1, input);

                    throw nvae;
                }

            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // /Users/volker/Desktop/Regex/Regex.g:194:4: '\\\\r'
                    {
                    match("\\r"); 


                    }
                    break;
                case 2 :
                    // /Users/volker/Desktop/Regex/Regex.g:195:4: '\\\\n'
                    {
                    match("\\n"); 


                    }
                    break;
                case 3 :
                    // /Users/volker/Desktop/Regex/Regex.g:196:4: '\\\\t'
                    {
                    match("\\t"); 


                    }
                    break;
                case 4 :
                    // /Users/volker/Desktop/Regex/Regex.g:197:4: '\\\\f'
                    {
                    match("\\f"); 


                    }
                    break;
                case 5 :
                    // /Users/volker/Desktop/Regex/Regex.g:198:4: '\\\\a'
                    {
                    match("\\a"); 


                    }
                    break;
                case 6 :
                    // /Users/volker/Desktop/Regex/Regex.g:199:4: '\\\\e'
                    {
                    match("\\e"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NONTYPEABLECHARACTER"

    // $ANTLR start "OCTALCHAR"
    public final void mOCTALCHAR() throws RecognitionException {
        try {
            int _type = OCTALCHAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Desktop/Regex/Regex.g:203:2: ( '\\\\0' OCTALDIGIT OCTALDIGIT OCTALDIGIT )
            // /Users/volker/Desktop/Regex/Regex.g:203:4: '\\\\0' OCTALDIGIT OCTALDIGIT OCTALDIGIT
            {
            match("\\0"); 

            mOCTALDIGIT(); 
            mOCTALDIGIT(); 
            mOCTALDIGIT(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "OCTALCHAR"

    // $ANTLR start "HEXCHAR"
    public final void mHEXCHAR() throws RecognitionException {
        try {
            int _type = HEXCHAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Desktop/Regex/Regex.g:206:9: ( '\\\\x' HEXDIGIT HEXDIGIT | '\\\\u' HEXDIGIT HEXDIGIT HEXDIGIT HEXDIGIT )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0=='\\') ) {
                int LA6_1 = input.LA(2);

                if ( (LA6_1=='x') ) {
                    alt6=1;
                }
                else if ( (LA6_1=='u') ) {
                    alt6=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 6, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }
            switch (alt6) {
                case 1 :
                    // /Users/volker/Desktop/Regex/Regex.g:206:11: '\\\\x' HEXDIGIT HEXDIGIT
                    {
                    match("\\x"); 

                    mHEXDIGIT(); 
                    mHEXDIGIT(); 

                    }
                    break;
                case 2 :
                    // /Users/volker/Desktop/Regex/Regex.g:207:4: '\\\\u' HEXDIGIT HEXDIGIT HEXDIGIT HEXDIGIT
                    {
                    match("\\u"); 

                    mHEXDIGIT(); 
                    mHEXDIGIT(); 
                    mHEXDIGIT(); 
                    mHEXDIGIT(); 

                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "HEXCHAR"

    // $ANTLR start "CODEDCHAR"
    public final void mCODEDCHAR() throws RecognitionException {
        try {
            int _type = CODEDCHAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Desktop/Regex/Regex.g:210:10: ( '\\\\c' LETTER )
            // /Users/volker/Desktop/Regex/Regex.g:210:12: '\\\\c' LETTER
            {
            match("\\c"); 

            mLETTER(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CODEDCHAR"

    // $ANTLR start "SIMPLEQUANTIFIER"
    public final void mSIMPLEQUANTIFIER() throws RecognitionException {
        try {
            int _type = SIMPLEQUANTIFIER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Desktop/Regex/Regex.g:213:2: ({...}? => ( '?' | '*' | '+' ) )
            // /Users/volker/Desktop/Regex/Regex.g:213:4: {...}? => ( '?' | '*' | '+' )
            {
            if ( !((!classMode)) ) {
                throw new FailedPredicateException(input, "SIMPLEQUANTIFIER", "!classMode");
            }
            if ( (input.LA(1)>='*' && input.LA(1)<='+')||input.LA(1)=='?' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SIMPLEQUANTIFIER"

    // $ANTLR start "INT"
    public final void mINT() throws RecognitionException {
        try {
            int _type = INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Desktop/Regex/Regex.g:215:5: ({...}? => ( DIGIT )+ )
            // /Users/volker/Desktop/Regex/Regex.g:215:7: {...}? => ( DIGIT )+
            {
            if ( !((numQuantifierMode)) ) {
                throw new FailedPredicateException(input, "INT", "numQuantifierMode");
            }
            // /Users/volker/Desktop/Regex/Regex.g:215:30: ( DIGIT )+
            int cnt7=0;
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( ((LA7_0>='0' && LA7_0<='9')) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // /Users/volker/Desktop/Regex/Regex.g:215:30: DIGIT
            	    {
            	    mDIGIT(); 

            	    }
            	    break;

            	default :
            	    if ( cnt7 >= 1 ) break loop7;
                        EarlyExitException eee =
                            new EarlyExitException(7, input);
                        throw eee;
                }
                cnt7++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT"

    // $ANTLR start "LBRACE"
    public final void mLBRACE() throws RecognitionException {
        try {
            int _type = LBRACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Desktop/Regex/Regex.g:217:8: ( '{' )
            // /Users/volker/Desktop/Regex/Regex.g:217:10: '{'
            {
            match('{'); 
             numQuantifierMode = true; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LBRACE"

    // $ANTLR start "RBRACE"
    public final void mRBRACE() throws RecognitionException {
        try {
            int _type = RBRACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Desktop/Regex/Regex.g:219:8: ( '}' )
            // /Users/volker/Desktop/Regex/Regex.g:219:10: '}'
            {
            match('}'); 
             numQuantifierMode = false; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RBRACE"

    // $ANTLR start "LBRACKET"
    public final void mLBRACKET() throws RecognitionException {
        try {
            int _type = LBRACKET;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Desktop/Regex/Regex.g:221:9: ( '[' )
            // /Users/volker/Desktop/Regex/Regex.g:221:11: '['
            {
            match('['); 
             classMode = true; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LBRACKET"

    // $ANTLR start "RBRACKET"
    public final void mRBRACKET() throws RecognitionException {
        try {
            int _type = RBRACKET;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Desktop/Regex/Regex.g:223:9: ( ']' )
            // /Users/volker/Desktop/Regex/Regex.g:223:11: ']'
            {
            match(']'); 
             classMode = false; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RBRACKET"

    // $ANTLR start "LETTER"
    public final void mLETTER() throws RecognitionException {
        try {
            // /Users/volker/Desktop/Regex/Regex.g:230:8: ( 'A' .. 'Z' | 'a' .. 'z' )
            // /Users/volker/Desktop/Regex/Regex.g:
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "LETTER"

    // $ANTLR start "HEXDIGIT"
    public final void mHEXDIGIT() throws RecognitionException {
        try {
            // /Users/volker/Desktop/Regex/Regex.g:233:9: ( DIGIT | 'A' .. 'F' | 'a' .. 'f' )
            // /Users/volker/Desktop/Regex/Regex.g:
            {
            if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='F')||(input.LA(1)>='a' && input.LA(1)<='f') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "HEXDIGIT"

    // $ANTLR start "OCTALDIGIT"
    public final void mOCTALDIGIT() throws RecognitionException {
        try {
            // /Users/volker/Desktop/Regex/Regex.g:236:11: ( '0' .. '7' )
            // /Users/volker/Desktop/Regex/Regex.g:236:13: '0' .. '7'
            {
            matchRange('0','7'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "OCTALDIGIT"

    // $ANTLR start "DIGIT"
    public final void mDIGIT() throws RecognitionException {
        try {
            // /Users/volker/Desktop/Regex/Regex.g:239:7: ( '0' .. '9' )
            // /Users/volker/Desktop/Regex/Regex.g:239:9: '0' .. '9'
            {
            matchRange('0','9'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "DIGIT"

    public void mTokens() throws RecognitionException {
        // /Users/volker/Desktop/Regex/Regex.g:1:8: ( T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | PREDEFINEDCLASS | ALPHANUM | SPECIALCHARACTER | ESCAPEDEDCHARACTER | NONTYPEABLECHARACTER | OCTALCHAR | HEXCHAR | CODEDCHAR | SIMPLEQUANTIFIER | INT | LBRACE | RBRACE | LBRACKET | RBRACKET )
        int alt8=21;
        alt8 = dfa8.predict(input);
        switch (alt8) {
            case 1 :
                // /Users/volker/Desktop/Regex/Regex.g:1:10: T__30
                {
                mT__30(); 

                }
                break;
            case 2 :
                // /Users/volker/Desktop/Regex/Regex.g:1:16: T__31
                {
                mT__31(); 

                }
                break;
            case 3 :
                // /Users/volker/Desktop/Regex/Regex.g:1:22: T__32
                {
                mT__32(); 

                }
                break;
            case 4 :
                // /Users/volker/Desktop/Regex/Regex.g:1:28: T__33
                {
                mT__33(); 

                }
                break;
            case 5 :
                // /Users/volker/Desktop/Regex/Regex.g:1:34: T__34
                {
                mT__34(); 

                }
                break;
            case 6 :
                // /Users/volker/Desktop/Regex/Regex.g:1:40: T__35
                {
                mT__35(); 

                }
                break;
            case 7 :
                // /Users/volker/Desktop/Regex/Regex.g:1:46: T__36
                {
                mT__36(); 

                }
                break;
            case 8 :
                // /Users/volker/Desktop/Regex/Regex.g:1:52: PREDEFINEDCLASS
                {
                mPREDEFINEDCLASS(); 

                }
                break;
            case 9 :
                // /Users/volker/Desktop/Regex/Regex.g:1:68: ALPHANUM
                {
                mALPHANUM(); 

                }
                break;
            case 10 :
                // /Users/volker/Desktop/Regex/Regex.g:1:77: SPECIALCHARACTER
                {
                mSPECIALCHARACTER(); 

                }
                break;
            case 11 :
                // /Users/volker/Desktop/Regex/Regex.g:1:94: ESCAPEDEDCHARACTER
                {
                mESCAPEDEDCHARACTER(); 

                }
                break;
            case 12 :
                // /Users/volker/Desktop/Regex/Regex.g:1:113: NONTYPEABLECHARACTER
                {
                mNONTYPEABLECHARACTER(); 

                }
                break;
            case 13 :
                // /Users/volker/Desktop/Regex/Regex.g:1:134: OCTALCHAR
                {
                mOCTALCHAR(); 

                }
                break;
            case 14 :
                // /Users/volker/Desktop/Regex/Regex.g:1:144: HEXCHAR
                {
                mHEXCHAR(); 

                }
                break;
            case 15 :
                // /Users/volker/Desktop/Regex/Regex.g:1:152: CODEDCHAR
                {
                mCODEDCHAR(); 

                }
                break;
            case 16 :
                // /Users/volker/Desktop/Regex/Regex.g:1:162: SIMPLEQUANTIFIER
                {
                mSIMPLEQUANTIFIER(); 

                }
                break;
            case 17 :
                // /Users/volker/Desktop/Regex/Regex.g:1:179: INT
                {
                mINT(); 

                }
                break;
            case 18 :
                // /Users/volker/Desktop/Regex/Regex.g:1:183: LBRACE
                {
                mLBRACE(); 

                }
                break;
            case 19 :
                // /Users/volker/Desktop/Regex/Regex.g:1:190: RBRACE
                {
                mRBRACE(); 

                }
                break;
            case 20 :
                // /Users/volker/Desktop/Regex/Regex.g:1:197: LBRACKET
                {
                mLBRACKET(); 

                }
                break;
            case 21 :
                // /Users/volker/Desktop/Regex/Regex.g:1:206: RBRACKET
                {
                mRBRACKET(); 

                }
                break;

        }

    }


    protected DFA3 dfa3 = new DFA3(this);
    protected DFA4 dfa4 = new DFA4(this);
    protected DFA8 dfa8 = new DFA8(this);
    static final String DFA3_eotS =
        "\23\uffff";
    static final String DFA3_eofS =
        "\23\uffff";
    static final String DFA3_minS =
        "\1\40\22\uffff";
    static final String DFA3_maxS =
        "\1\176\22\uffff";
    static final String DFA3_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14\1"+
        "\15\1\16\1\17\1\20\1\21\1\22";
    static final String DFA3_specialS =
        "\1\0\22\uffff}>";
    static final String[] DFA3_transitionS = {
            "\1\1\1\2\1\4\2\uffff\1\5\1\6\1\3\2\uffff\2\22\1\21\2\uffff\1"+
            "\7\12\uffff\1\10\1\11\1\12\1\13\1\14\1\22\1\15\36\uffff\1\16"+
            "\1\17\35\uffff\1\20",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA3_eot = DFA.unpackEncodedString(DFA3_eotS);
    static final short[] DFA3_eof = DFA.unpackEncodedString(DFA3_eofS);
    static final char[] DFA3_min = DFA.unpackEncodedStringToUnsignedChars(DFA3_minS);
    static final char[] DFA3_max = DFA.unpackEncodedStringToUnsignedChars(DFA3_maxS);
    static final short[] DFA3_accept = DFA.unpackEncodedString(DFA3_acceptS);
    static final short[] DFA3_special = DFA.unpackEncodedString(DFA3_specialS);
    static final short[][] DFA3_transition;

    static {
        int numStates = DFA3_transitionS.length;
        DFA3_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA3_transition[i] = DFA.unpackEncodedString(DFA3_transitionS[i]);
        }
    }

    class DFA3 extends DFA {

        public DFA3(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 3;
            this.eot = DFA3_eot;
            this.eof = DFA3_eof;
            this.min = DFA3_min;
            this.max = DFA3_max;
            this.accept = DFA3_accept;
            this.special = DFA3_special;
            this.transition = DFA3_transition;
        }
        public String getDescription() {
            return "178:1: SPECIALCHARACTER : ( ' ' | '!' | '\\'' | '\"' | '%' | '&' | '/' | ':' | ';' | '<' | '=' | '>' | '@' | '_' | '`' | '~' | {...}? => ',' | {...}? => ( '?' | '*' | '+' ) );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA3_0 = input.LA(1);

                         
                        int index3_0 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA3_0==' ') ) {s = 1;}

                        else if ( (LA3_0=='!') ) {s = 2;}

                        else if ( (LA3_0=='\'') ) {s = 3;}

                        else if ( (LA3_0=='\"') ) {s = 4;}

                        else if ( (LA3_0=='%') ) {s = 5;}

                        else if ( (LA3_0=='&') ) {s = 6;}

                        else if ( (LA3_0=='/') ) {s = 7;}

                        else if ( (LA3_0==':') ) {s = 8;}

                        else if ( (LA3_0==';') ) {s = 9;}

                        else if ( (LA3_0=='<') ) {s = 10;}

                        else if ( (LA3_0=='=') ) {s = 11;}

                        else if ( (LA3_0=='>') ) {s = 12;}

                        else if ( (LA3_0=='@') ) {s = 13;}

                        else if ( (LA3_0=='_') ) {s = 14;}

                        else if ( (LA3_0=='`') ) {s = 15;}

                        else if ( (LA3_0=='~') ) {s = 16;}

                        else if ( (LA3_0==',') && ((!numQuantifierMode))) {s = 17;}

                        else if ( ((LA3_0>='*' && LA3_0<='+')||LA3_0=='?') && ((classMode))) {s = 18;}

                         
                        input.seek(index3_0);
                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 3, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA4_eotS =
        "\1\2\23\uffff";
    static final String DFA4_eofS =
        "\24\uffff";
    static final String DFA4_minS =
        "\1\134\1\44\22\uffff";
    static final String DFA4_maxS =
        "\1\134\1\175\22\uffff";
    static final String DFA4_acceptS =
        "\2\uffff\1\5\1\1\1\2\1\3\1\4\1\6\1\7\1\10\1\11\1\12\1\13\1\14\1"+
        "\15\1\16\1\17\1\20\1\21\1\22";
    static final String DFA4_specialS =
        "\24\uffff}>";
    static final String[] DFA4_transitionS = {
            "\1\1",
            "\1\3\1\uffff\1\4\1\uffff\1\5\1\6\1\21\1\22\1\7\1\10\1\11\20"+
            "\uffff\1\23\33\uffff\1\12\1\17\1\13\1\14\34\uffff\1\15\1\20"+
            "\1\16",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA4_eot = DFA.unpackEncodedString(DFA4_eotS);
    static final short[] DFA4_eof = DFA.unpackEncodedString(DFA4_eofS);
    static final char[] DFA4_min = DFA.unpackEncodedStringToUnsignedChars(DFA4_minS);
    static final char[] DFA4_max = DFA.unpackEncodedStringToUnsignedChars(DFA4_maxS);
    static final short[] DFA4_accept = DFA.unpackEncodedString(DFA4_acceptS);
    static final short[] DFA4_special = DFA.unpackEncodedString(DFA4_specialS);
    static final short[][] DFA4_transition;

    static {
        int numStates = DFA4_transitionS.length;
        DFA4_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA4_transition[i] = DFA.unpackEncodedString(DFA4_transitionS[i]);
        }
    }

    class DFA4 extends DFA {

        public DFA4(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 4;
            this.eot = DFA4_eot;
            this.eof = DFA4_eof;
            this.min = DFA4_min;
            this.max = DFA4_max;
            this.accept = DFA4_accept;
            this.special = DFA4_special;
            this.transition = DFA4_transition;
        }
        public String getDescription() {
            return "186:1: ESCAPEDEDCHARACTER : ( '\\\\$' | '\\\\&' | '\\\\(' | '\\\\)' | | '\\\\,' | '\\\\-' | '\\\\.' | '\\\\[' | '\\\\]' | '\\\\^' | '\\\\{' | '\\\\}' | '\\\\\\\\' | '\\\\|' | '\\\\*' | '\\\\+' | '\\\\?' );";
        }
    }
    static final String DFA8_eotS =
        "\1\16\6\uffff\1\23\3\uffff\1\30\1\uffff\1\32\17\uffff";
    static final String DFA8_eofS =
        "\35\uffff";
    static final String DFA8_minS =
        "\1\40\6\uffff\1\0\1\uffff\1\44\1\uffff\1\60\1\uffff\1\0\5\uffff"+
        "\1\0\4\uffff\1\0\1\uffff\1\0\2\uffff";
    static final String DFA8_maxS =
        "\1\176\6\uffff\1\0\1\uffff\1\175\1\uffff\1\71\1\uffff\1\0\5\uffff"+
        "\1\0\4\uffff\1\0\1\uffff\1\0\2\uffff";
    static final String DFA8_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\uffff\1\10\1\uffff\1\11\1\uffff"+
        "\1\12\1\uffff\1\13\1\22\1\23\1\24\1\25\1\uffff\1\14\1\15\1\16\1"+
        "\17\1\uffff\1\21\1\uffff\1\7\1\20";
    static final String DFA8_specialS =
        "\1\1\12\uffff\1\0\1\uffff\1\4\5\uffff\1\5\4\uffff\1\3\1\uffff\1"+
        "\2\2\uffff}>";
    static final String[] DFA8_transitionS = {
            "\3\14\1\uffff\1\2\3\14\1\5\1\6\2\15\1\7\1\4\1\10\1\14\12\13"+
            "\5\14\1\15\1\14\32\12\1\21\1\11\1\22\1\1\2\14\32\12\1\17\1\3"+
            "\1\20\1\14",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "\1\16\1\uffff\1\16\1\uffff\7\16\1\uffff\1\25\16\uffff\1\16"+
            "\4\uffff\1\10\16\uffff\1\10\3\uffff\1\10\3\uffff\4\16\2\uffff"+
            "\1\24\1\uffff\1\27\1\10\2\24\7\uffff\1\24\3\uffff\1\24\1\10"+
            "\1\24\1\26\1\uffff\1\10\1\26\2\uffff\3\16",
            "",
            "\12\31",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "\1\uffff",
            "",
            ""
    };

    static final short[] DFA8_eot = DFA.unpackEncodedString(DFA8_eotS);
    static final short[] DFA8_eof = DFA.unpackEncodedString(DFA8_eofS);
    static final char[] DFA8_min = DFA.unpackEncodedStringToUnsignedChars(DFA8_minS);
    static final char[] DFA8_max = DFA.unpackEncodedStringToUnsignedChars(DFA8_maxS);
    static final short[] DFA8_accept = DFA.unpackEncodedString(DFA8_acceptS);
    static final short[] DFA8_special = DFA.unpackEncodedString(DFA8_specialS);
    static final short[][] DFA8_transition;

    static {
        int numStates = DFA8_transitionS.length;
        DFA8_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA8_transition[i] = DFA.unpackEncodedString(DFA8_transitionS[i]);
        }
    }

    class DFA8 extends DFA {

        public DFA8(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 8;
            this.eot = DFA8_eot;
            this.eof = DFA8_eof;
            this.min = DFA8_min;
            this.max = DFA8_max;
            this.accept = DFA8_accept;
            this.special = DFA8_special;
            this.transition = DFA8_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | PREDEFINEDCLASS | ALPHANUM | SPECIALCHARACTER | ESCAPEDEDCHARACTER | NONTYPEABLECHARACTER | OCTALCHAR | HEXCHAR | CODEDCHAR | SIMPLEQUANTIFIER | INT | LBRACE | RBRACE | LBRACKET | RBRACKET );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA8_11 = input.LA(1);

                         
                        int index8_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((LA8_11>='0' && LA8_11<='9')) && ((numQuantifierMode))) {s = 25;}

                        else s = 24;

                         
                        input.seek(index8_11);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA8_0 = input.LA(1);

                         
                        int index8_0 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA8_0=='^') ) {s = 1;}

                        else if ( (LA8_0=='$') ) {s = 2;}

                        else if ( (LA8_0=='|') ) {s = 3;}

                        else if ( (LA8_0=='-') ) {s = 4;}

                        else if ( (LA8_0=='(') ) {s = 5;}

                        else if ( (LA8_0==')') ) {s = 6;}

                        else if ( (LA8_0==',') ) {s = 7;}

                        else if ( (LA8_0=='.') ) {s = 8;}

                        else if ( (LA8_0=='\\') ) {s = 9;}

                        else if ( ((LA8_0>='A' && LA8_0<='Z')||(LA8_0>='a' && LA8_0<='z')) ) {s = 10;}

                        else if ( ((LA8_0>='0' && LA8_0<='9')) && (((!numQuantifierMode)||(numQuantifierMode)))) {s = 11;}

                        else if ( ((LA8_0>=' ' && LA8_0<='\"')||(LA8_0>='%' && LA8_0<='\'')||LA8_0=='/'||(LA8_0>=':' && LA8_0<='>')||LA8_0=='@'||(LA8_0>='_' && LA8_0<='`')||LA8_0=='~') ) {s = 12;}

                        else if ( ((LA8_0>='*' && LA8_0<='+')||LA8_0=='?') && (((classMode)||(!classMode)))) {s = 13;}

                        else if ( (LA8_0=='{') ) {s = 15;}

                        else if ( (LA8_0=='}') ) {s = 16;}

                        else if ( (LA8_0=='[') ) {s = 17;}

                        else if ( (LA8_0==']') ) {s = 18;}

                        else s = 14;

                         
                        input.seek(index8_0);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA8_26 = input.LA(1);

                         
                        int index8_26 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((classMode)) ) {s = 12;}

                        else if ( ((!classMode)) ) {s = 28;}

                         
                        input.seek(index8_26);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA8_24 = input.LA(1);

                         
                        int index8_24 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((!numQuantifierMode)) ) {s = 10;}

                        else if ( ((numQuantifierMode)) ) {s = 25;}

                         
                        input.seek(index8_24);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA8_13 = input.LA(1);

                         
                        int index8_13 = input.index();
                        input.rewind();
                        s = -1;
                        s = 26;

                         
                        input.seek(index8_13);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA8_19 = input.LA(1);

                         
                        int index8_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (!(((!numQuantifierMode)))) ) {s = 27;}

                        else if ( ((!numQuantifierMode)) ) {s = 12;}

                         
                        input.seek(index8_19);
                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 8, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

}