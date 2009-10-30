// $ANTLR 3.1.2 /Users/volker/Desktop/Regex/Regex.g 2009-09-23 12:36:19

	package org.databene.regex.antlr;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.tree.*;

@SuppressWarnings("all")
public class RegexParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "PREDEFINEDCLASS", "ALPHANUM", "SPECIALCHARACTER", "ESCAPEDEDCHARACTER", "NONTYPEABLECHARACTER", "OCTALCHAR", "HEXCHAR", "CODEDCHAR", "SIMPLEQUANTIFIER", "INT", "GROUP", "CHOICE", "SEQUENCE", "FACTOR", "CLASS", "RANGE", "INCL", "EXCL", "QUANT", "LETTER", "DIGIT", "OCTALDIGIT", "HEXDIGIT", "LBRACE", "RBRACE", "LBRACKET", "RBRACKET", "'^'", "'$'", "'|'", "'-'", "'('", "')'", "','"
    };
    public static final int HEXDIGIT=26;
    public static final int LETTER=23;
    public static final int RANGE=19;
    public static final int T__35=35;
    public static final int T__36=36;
    public static final int ALPHANUM=5;
    public static final int EXCL=21;
    public static final int OCTALCHAR=9;
    public static final int CHOICE=15;
    public static final int LBRACKET=29;
    public static final int T__33=33;
    public static final int ESCAPEDEDCHARACTER=7;
    public static final int LBRACE=27;
    public static final int RBRACE=28;
    public static final int CLASS=18;
    public static final int QUANT=22;
    public static final int OCTALDIGIT=25;
    public static final int CODEDCHAR=11;
    public static final int SEQUENCE=16;
    public static final int INT=13;
    public static final int T__31=31;
    public static final int EOF=-1;
    public static final int HEXCHAR=10;
    public static final int RBRACKET=30;
    public static final int GROUP=14;
    public static final int T__32=32;
    public static final int T__37=37;
    public static final int SIMPLEQUANTIFIER=12;
    public static final int DIGIT=24;
    public static final int INCL=20;
    public static final int FACTOR=17;
    public static final int PREDEFINEDCLASS=4;
    public static final int SPECIALCHARACTER=6;
    public static final int T__34=34;
    public static final int NONTYPEABLECHARACTER=8;

    // delegates
    // delegators


        public RegexParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public RegexParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
            this.state.ruleMemo = new HashMap[40+1];
             
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return RegexParser.tokenNames; }
    public String getGrammarFileName() { return "/Users/volker/Desktop/Regex/Regex.g"; }


    protected void mismatch(IntStream input, int ttype, BitSet follow)
      throws RecognitionException
    {
      throw new MismatchedTokenException(ttype, input);
    }

    public Object recoverFromMismatchedSet(IntStream input, RecognitionException e, BitSet follow)
      throws RecognitionException
    {
      throw e;
    }


    public static class expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "expression"
    // /Users/volker/Desktop/Regex/Regex.g:73:1: expression : ( '^' )? choice ( '$' )? ;
    public final RegexParser.expression_return expression() throws RecognitionException {
        RegexParser.expression_return retval = new RegexParser.expression_return();
        retval.start = input.LT(1);
        int expression_StartIndex = input.index();
        Object root_0 = null;

        Token char_literal1=null;
        Token char_literal3=null;
        RegexParser.choice_return choice2 = null;


        Object char_literal1_tree=null;
        Object char_literal3_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 1) ) { return retval; }
            // /Users/volker/Desktop/Regex/Regex.g:78:2: ( ( '^' )? choice ( '$' )? )
            // /Users/volker/Desktop/Regex/Regex.g:78:4: ( '^' )? choice ( '$' )?
            {
            root_0 = (Object)adaptor.nil();

            // /Users/volker/Desktop/Regex/Regex.g:78:7: ( '^' )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==31) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // /Users/volker/Desktop/Regex/Regex.g:0:0: '^'
                    {
                    char_literal1=(Token)match(input,31,FOLLOW_31_in_expression74); if (state.failed) return retval;

                    }
                    break;

            }

            pushFollow(FOLLOW_choice_in_expression78);
            choice2=choice();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, choice2.getTree());
            // /Users/volker/Desktop/Regex/Regex.g:78:20: ( '$' )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==32) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // /Users/volker/Desktop/Regex/Regex.g:0:0: '$'
                    {
                    char_literal3=(Token)match(input,32,FOLLOW_32_in_expression80); if (state.failed) return retval;

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 1, expression_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "expression"

    public static class choice_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "choice"
    // /Users/volker/Desktop/Regex/Regex.g:80:1: choice : ( sequence ( '|' sequence )+ -> ^( CHOICE ( sequence )+ ) | sequence );
    public final RegexParser.choice_return choice() throws RecognitionException {
        RegexParser.choice_return retval = new RegexParser.choice_return();
        retval.start = input.LT(1);
        int choice_StartIndex = input.index();
        Object root_0 = null;

        Token char_literal5=null;
        RegexParser.sequence_return sequence4 = null;

        RegexParser.sequence_return sequence6 = null;

        RegexParser.sequence_return sequence7 = null;


        Object char_literal5_tree=null;
        RewriteRuleTokenStream stream_33=new RewriteRuleTokenStream(adaptor,"token 33");
        RewriteRuleSubtreeStream stream_sequence=new RewriteRuleSubtreeStream(adaptor,"rule sequence");
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 2) ) { return retval; }
            // /Users/volker/Desktop/Regex/Regex.g:81:2: ( sequence ( '|' sequence )+ -> ^( CHOICE ( sequence )+ ) | sequence )
            int alt4=2;
            switch ( input.LA(1) ) {
            case ALPHANUM:
            case SPECIALCHARACTER:
            case ESCAPEDEDCHARACTER:
            case NONTYPEABLECHARACTER:
            case OCTALCHAR:
            case HEXCHAR:
            case CODEDCHAR:
                {
                int LA4_1 = input.LA(2);

                if ( (synpred4_Regex()) ) {
                    alt4=1;
                }
                else if ( (true) ) {
                    alt4=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 4, 1, input);

                    throw nvae;
                }
                }
                break;
            case 34:
                {
                int LA4_2 = input.LA(2);

                if ( (synpred4_Regex()) ) {
                    alt4=1;
                }
                else if ( (true) ) {
                    alt4=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 4, 2, input);

                    throw nvae;
                }
                }
                break;
            case LBRACKET:
                {
                int LA4_3 = input.LA(2);

                if ( (synpred4_Regex()) ) {
                    alt4=1;
                }
                else if ( (true) ) {
                    alt4=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 4, 3, input);

                    throw nvae;
                }
                }
                break;
            case PREDEFINEDCLASS:
                {
                int LA4_4 = input.LA(2);

                if ( (synpred4_Regex()) ) {
                    alt4=1;
                }
                else if ( (true) ) {
                    alt4=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 4, 4, input);

                    throw nvae;
                }
                }
                break;
            case 35:
                {
                int LA4_5 = input.LA(2);

                if ( (synpred4_Regex()) ) {
                    alt4=1;
                }
                else if ( (true) ) {
                    alt4=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 4, 5, input);

                    throw nvae;
                }
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }

            switch (alt4) {
                case 1 :
                    // /Users/volker/Desktop/Regex/Regex.g:81:4: sequence ( '|' sequence )+
                    {
                    pushFollow(FOLLOW_sequence_in_choice91);
                    sequence4=sequence();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_sequence.add(sequence4.getTree());
                    // /Users/volker/Desktop/Regex/Regex.g:81:13: ( '|' sequence )+
                    int cnt3=0;
                    loop3:
                    do {
                        int alt3=2;
                        int LA3_0 = input.LA(1);

                        if ( (LA3_0==33) ) {
                            alt3=1;
                        }


                        switch (alt3) {
                    	case 1 :
                    	    // /Users/volker/Desktop/Regex/Regex.g:81:14: '|' sequence
                    	    {
                    	    char_literal5=(Token)match(input,33,FOLLOW_33_in_choice94); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_33.add(char_literal5);

                    	    pushFollow(FOLLOW_sequence_in_choice96);
                    	    sequence6=sequence();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_sequence.add(sequence6.getTree());

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt3 >= 1 ) break loop3;
                    	    if (state.backtracking>0) {state.failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(3, input);
                                throw eee;
                        }
                        cnt3++;
                    } while (true);



                    // AST REWRITE
                    // elements: sequence
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 81:29: -> ^( CHOICE ( sequence )+ )
                    {
                        // /Users/volker/Desktop/Regex/Regex.g:81:32: ^( CHOICE ( sequence )+ )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(CHOICE, "CHOICE"), root_1);

                        if ( !(stream_sequence.hasNext()) ) {
                            throw new RewriteEarlyExitException();
                        }
                        while ( stream_sequence.hasNext() ) {
                            adaptor.addChild(root_1, stream_sequence.nextTree());

                        }
                        stream_sequence.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/volker/Desktop/Regex/Regex.g:82:4: sequence
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_sequence_in_choice112);
                    sequence7=sequence();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, sequence7.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 2, choice_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "choice"

    public static class sequence_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "sequence"
    // /Users/volker/Desktop/Regex/Regex.g:85:1: sequence : ( factor ( factor )+ -> ^( SEQUENCE ( factor )+ ) | factor );
    public final RegexParser.sequence_return sequence() throws RecognitionException {
        RegexParser.sequence_return retval = new RegexParser.sequence_return();
        retval.start = input.LT(1);
        int sequence_StartIndex = input.index();
        Object root_0 = null;

        RegexParser.factor_return factor8 = null;

        RegexParser.factor_return factor9 = null;

        RegexParser.factor_return factor10 = null;


        RewriteRuleSubtreeStream stream_factor=new RewriteRuleSubtreeStream(adaptor,"rule factor");
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 3) ) { return retval; }
            // /Users/volker/Desktop/Regex/Regex.g:85:9: ( factor ( factor )+ -> ^( SEQUENCE ( factor )+ ) | factor )
            int alt6=2;
            switch ( input.LA(1) ) {
            case ALPHANUM:
            case SPECIALCHARACTER:
            case ESCAPEDEDCHARACTER:
            case NONTYPEABLECHARACTER:
            case OCTALCHAR:
            case HEXCHAR:
            case CODEDCHAR:
                {
                int LA6_1 = input.LA(2);

                if ( (synpred6_Regex()) ) {
                    alt6=1;
                }
                else if ( (true) ) {
                    alt6=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 6, 1, input);

                    throw nvae;
                }
                }
                break;
            case 34:
                {
                int LA6_2 = input.LA(2);

                if ( (synpred6_Regex()) ) {
                    alt6=1;
                }
                else if ( (true) ) {
                    alt6=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 6, 2, input);

                    throw nvae;
                }
                }
                break;
            case LBRACKET:
                {
                int LA6_3 = input.LA(2);

                if ( (synpred6_Regex()) ) {
                    alt6=1;
                }
                else if ( (true) ) {
                    alt6=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 6, 3, input);

                    throw nvae;
                }
                }
                break;
            case PREDEFINEDCLASS:
                {
                int LA6_4 = input.LA(2);

                if ( (synpred6_Regex()) ) {
                    alt6=1;
                }
                else if ( (true) ) {
                    alt6=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 6, 4, input);

                    throw nvae;
                }
                }
                break;
            case 35:
                {
                int LA6_5 = input.LA(2);

                if ( (synpred6_Regex()) ) {
                    alt6=1;
                }
                else if ( (true) ) {
                    alt6=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 6, 5, input);

                    throw nvae;
                }
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }

            switch (alt6) {
                case 1 :
                    // /Users/volker/Desktop/Regex/Regex.g:85:11: factor ( factor )+
                    {
                    pushFollow(FOLLOW_factor_in_sequence121);
                    factor8=factor();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_factor.add(factor8.getTree());
                    // /Users/volker/Desktop/Regex/Regex.g:85:18: ( factor )+
                    int cnt5=0;
                    loop5:
                    do {
                        int alt5=2;
                        int LA5_0 = input.LA(1);

                        if ( ((LA5_0>=PREDEFINEDCLASS && LA5_0<=CODEDCHAR)||LA5_0==LBRACKET||(LA5_0>=34 && LA5_0<=35)) ) {
                            alt5=1;
                        }


                        switch (alt5) {
                    	case 1 :
                    	    // /Users/volker/Desktop/Regex/Regex.g:0:0: factor
                    	    {
                    	    pushFollow(FOLLOW_factor_in_sequence123);
                    	    factor9=factor();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_factor.add(factor9.getTree());

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt5 >= 1 ) break loop5;
                    	    if (state.backtracking>0) {state.failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(5, input);
                                throw eee;
                        }
                        cnt5++;
                    } while (true);



                    // AST REWRITE
                    // elements: factor
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 85:26: -> ^( SEQUENCE ( factor )+ )
                    {
                        // /Users/volker/Desktop/Regex/Regex.g:85:29: ^( SEQUENCE ( factor )+ )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(SEQUENCE, "SEQUENCE"), root_1);

                        if ( !(stream_factor.hasNext()) ) {
                            throw new RewriteEarlyExitException();
                        }
                        while ( stream_factor.hasNext() ) {
                            adaptor.addChild(root_1, stream_factor.nextTree());

                        }
                        stream_factor.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/volker/Desktop/Regex/Regex.g:86:4: factor
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_factor_in_sequence138);
                    factor10=factor();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, factor10.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 3, sequence_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "sequence"

    public static class factor_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "factor"
    // /Users/volker/Desktop/Regex/Regex.g:88:1: factor : ( atom quantifier -> ^( FACTOR atom quantifier ) | atom );
    public final RegexParser.factor_return factor() throws RecognitionException {
        RegexParser.factor_return retval = new RegexParser.factor_return();
        retval.start = input.LT(1);
        int factor_StartIndex = input.index();
        Object root_0 = null;

        RegexParser.atom_return atom11 = null;

        RegexParser.quantifier_return quantifier12 = null;

        RegexParser.atom_return atom13 = null;


        RewriteRuleSubtreeStream stream_quantifier=new RewriteRuleSubtreeStream(adaptor,"rule quantifier");
        RewriteRuleSubtreeStream stream_atom=new RewriteRuleSubtreeStream(adaptor,"rule atom");
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 4) ) { return retval; }
            // /Users/volker/Desktop/Regex/Regex.g:88:8: ( atom quantifier -> ^( FACTOR atom quantifier ) | atom )
            int alt7=2;
            switch ( input.LA(1) ) {
            case ALPHANUM:
            case SPECIALCHARACTER:
            case ESCAPEDEDCHARACTER:
            case NONTYPEABLECHARACTER:
            case OCTALCHAR:
            case HEXCHAR:
            case CODEDCHAR:
                {
                int LA7_1 = input.LA(2);

                if ( (synpred7_Regex()) ) {
                    alt7=1;
                }
                else if ( (true) ) {
                    alt7=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 7, 1, input);

                    throw nvae;
                }
                }
                break;
            case 34:
                {
                int LA7_2 = input.LA(2);

                if ( (synpred7_Regex()) ) {
                    alt7=1;
                }
                else if ( (true) ) {
                    alt7=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 7, 2, input);

                    throw nvae;
                }
                }
                break;
            case LBRACKET:
                {
                int LA7_3 = input.LA(2);

                if ( (synpred7_Regex()) ) {
                    alt7=1;
                }
                else if ( (true) ) {
                    alt7=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 7, 3, input);

                    throw nvae;
                }
                }
                break;
            case PREDEFINEDCLASS:
                {
                int LA7_4 = input.LA(2);

                if ( (synpred7_Regex()) ) {
                    alt7=1;
                }
                else if ( (true) ) {
                    alt7=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 7, 4, input);

                    throw nvae;
                }
                }
                break;
            case 35:
                {
                int LA7_5 = input.LA(2);

                if ( (synpred7_Regex()) ) {
                    alt7=1;
                }
                else if ( (true) ) {
                    alt7=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 7, 5, input);

                    throw nvae;
                }
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;
            }

            switch (alt7) {
                case 1 :
                    // /Users/volker/Desktop/Regex/Regex.g:88:10: atom quantifier
                    {
                    pushFollow(FOLLOW_atom_in_factor146);
                    atom11=atom();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_atom.add(atom11.getTree());
                    pushFollow(FOLLOW_quantifier_in_factor148);
                    quantifier12=quantifier();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_quantifier.add(quantifier12.getTree());


                    // AST REWRITE
                    // elements: quantifier, atom
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 88:26: -> ^( FACTOR atom quantifier )
                    {
                        // /Users/volker/Desktop/Regex/Regex.g:88:29: ^( FACTOR atom quantifier )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(FACTOR, "FACTOR"), root_1);

                        adaptor.addChild(root_1, stream_atom.nextTree());
                        adaptor.addChild(root_1, stream_quantifier.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /Users/volker/Desktop/Regex/Regex.g:89:4: atom
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_atom_in_factor163);
                    atom13=atom();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, atom13.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 4, factor_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "factor"

    public static class atom_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "atom"
    // /Users/volker/Desktop/Regex/Regex.g:91:1: atom : ( singlechar | group );
    public final RegexParser.atom_return atom() throws RecognitionException {
        RegexParser.atom_return retval = new RegexParser.atom_return();
        retval.start = input.LT(1);
        int atom_StartIndex = input.index();
        Object root_0 = null;

        RegexParser.singlechar_return singlechar14 = null;

        RegexParser.group_return group15 = null;



        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 5) ) { return retval; }
            // /Users/volker/Desktop/Regex/Regex.g:91:6: ( singlechar | group )
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( ((LA8_0>=PREDEFINEDCLASS && LA8_0<=CODEDCHAR)||LA8_0==LBRACKET||LA8_0==34) ) {
                alt8=1;
            }
            else if ( (LA8_0==35) ) {
                alt8=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;
            }
            switch (alt8) {
                case 1 :
                    // /Users/volker/Desktop/Regex/Regex.g:91:8: singlechar
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_singlechar_in_atom171);
                    singlechar14=singlechar();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, singlechar14.getTree());

                    }
                    break;
                case 2 :
                    // /Users/volker/Desktop/Regex/Regex.g:92:4: group
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_group_in_atom176);
                    group15=group();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, group15.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 5, atom_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "atom"

    public static class singlechar_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "singlechar"
    // /Users/volker/Desktop/Regex/Regex.g:95:1: singlechar : ( classchar | '-' | charclass | PREDEFINEDCLASS );
    public final RegexParser.singlechar_return singlechar() throws RecognitionException {
        RegexParser.singlechar_return retval = new RegexParser.singlechar_return();
        retval.start = input.LT(1);
        int singlechar_StartIndex = input.index();
        Object root_0 = null;

        Token char_literal17=null;
        Token PREDEFINEDCLASS19=null;
        RegexParser.classchar_return classchar16 = null;

        RegexParser.charclass_return charclass18 = null;


        Object char_literal17_tree=null;
        Object PREDEFINEDCLASS19_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 6) ) { return retval; }
            // /Users/volker/Desktop/Regex/Regex.g:96:2: ( classchar | '-' | charclass | PREDEFINEDCLASS )
            int alt9=4;
            switch ( input.LA(1) ) {
            case ALPHANUM:
            case SPECIALCHARACTER:
            case ESCAPEDEDCHARACTER:
            case NONTYPEABLECHARACTER:
            case OCTALCHAR:
            case HEXCHAR:
            case CODEDCHAR:
                {
                alt9=1;
                }
                break;
            case 34:
                {
                alt9=2;
                }
                break;
            case LBRACKET:
                {
                alt9=3;
                }
                break;
            case PREDEFINEDCLASS:
                {
                alt9=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;
            }

            switch (alt9) {
                case 1 :
                    // /Users/volker/Desktop/Regex/Regex.g:96:4: classchar
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_classchar_in_singlechar187);
                    classchar16=classchar();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) root_0 = (Object)adaptor.becomeRoot(classchar16.getTree(), root_0);

                    }
                    break;
                case 2 :
                    // /Users/volker/Desktop/Regex/Regex.g:97:4: '-'
                    {
                    root_0 = (Object)adaptor.nil();

                    char_literal17=(Token)match(input,34,FOLLOW_34_in_singlechar193); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal17_tree = (Object)adaptor.create(char_literal17);
                    root_0 = (Object)adaptor.becomeRoot(char_literal17_tree, root_0);
                    }

                    }
                    break;
                case 3 :
                    // /Users/volker/Desktop/Regex/Regex.g:98:4: charclass
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_charclass_in_singlechar199);
                    charclass18=charclass();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, charclass18.getTree());

                    }
                    break;
                case 4 :
                    // /Users/volker/Desktop/Regex/Regex.g:99:4: PREDEFINEDCLASS
                    {
                    root_0 = (Object)adaptor.nil();

                    PREDEFINEDCLASS19=(Token)match(input,PREDEFINEDCLASS,FOLLOW_PREDEFINEDCLASS_in_singlechar204); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    PREDEFINEDCLASS19_tree = (Object)adaptor.create(PREDEFINEDCLASS19);
                    root_0 = (Object)adaptor.becomeRoot(PREDEFINEDCLASS19_tree, root_0);
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 6, singlechar_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "singlechar"

    public static class classchar_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "classchar"
    // /Users/volker/Desktop/Regex/Regex.g:102:1: classchar : ( ALPHANUM | SPECIALCHARACTER | ESCAPEDEDCHARACTER | NONTYPEABLECHARACTER | OCTALCHAR | HEXCHAR | CODEDCHAR );
    public final RegexParser.classchar_return classchar() throws RecognitionException {
        RegexParser.classchar_return retval = new RegexParser.classchar_return();
        retval.start = input.LT(1);
        int classchar_StartIndex = input.index();
        Object root_0 = null;

        Token set20=null;

        Object set20_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 7) ) { return retval; }
            // /Users/volker/Desktop/Regex/Regex.g:103:2: ( ALPHANUM | SPECIALCHARACTER | ESCAPEDEDCHARACTER | NONTYPEABLECHARACTER | OCTALCHAR | HEXCHAR | CODEDCHAR )
            // /Users/volker/Desktop/Regex/Regex.g:
            {
            root_0 = (Object)adaptor.nil();

            set20=(Token)input.LT(1);
            if ( (input.LA(1)>=ALPHANUM && input.LA(1)<=CODEDCHAR) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set20));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 7, classchar_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "classchar"

    public static class charclass_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "charclass"
    // /Users/volker/Desktop/Regex/Regex.g:112:1: charclass : '[' ( includedelements )? ( '^' excludedelements )? ']' -> ^( CLASS ^( INCL ( includedelements )? ) ( ^( EXCL excludedelements ) )? ) ;
    public final RegexParser.charclass_return charclass() throws RecognitionException {
        RegexParser.charclass_return retval = new RegexParser.charclass_return();
        retval.start = input.LT(1);
        int charclass_StartIndex = input.index();
        Object root_0 = null;

        Token char_literal21=null;
        Token char_literal23=null;
        Token char_literal25=null;
        RegexParser.includedelements_return includedelements22 = null;

        RegexParser.excludedelements_return excludedelements24 = null;


        Object char_literal21_tree=null;
        Object char_literal23_tree=null;
        Object char_literal25_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_31=new RewriteRuleTokenStream(adaptor,"token 31");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleSubtreeStream stream_excludedelements=new RewriteRuleSubtreeStream(adaptor,"rule excludedelements");
        RewriteRuleSubtreeStream stream_includedelements=new RewriteRuleSubtreeStream(adaptor,"rule includedelements");
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 8) ) { return retval; }
            // /Users/volker/Desktop/Regex/Regex.g:113:2: ( '[' ( includedelements )? ( '^' excludedelements )? ']' -> ^( CLASS ^( INCL ( includedelements )? ) ( ^( EXCL excludedelements ) )? ) )
            // /Users/volker/Desktop/Regex/Regex.g:113:4: '[' ( includedelements )? ( '^' excludedelements )? ']'
            {
            char_literal21=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_charclass272); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBRACKET.add(char_literal21);

            // /Users/volker/Desktop/Regex/Regex.g:113:8: ( includedelements )?
            int alt10=2;
            switch ( input.LA(1) ) {
                case PREDEFINEDCLASS:
                case ALPHANUM:
                case SPECIALCHARACTER:
                case ESCAPEDEDCHARACTER:
                case NONTYPEABLECHARACTER:
                case OCTALCHAR:
                case HEXCHAR:
                case CODEDCHAR:
                    {
                    alt10=1;
                    }
                    break;
                case 31:
                    {
                    int LA10_2 = input.LA(2);

                    if ( (synpred18_Regex()) ) {
                        alt10=1;
                    }
                    }
                    break;
                case RBRACKET:
                    {
                    int LA10_3 = input.LA(2);

                    if ( (synpred18_Regex()) ) {
                        alt10=1;
                    }
                    }
                    break;
            }

            switch (alt10) {
                case 1 :
                    // /Users/volker/Desktop/Regex/Regex.g:0:0: includedelements
                    {
                    pushFollow(FOLLOW_includedelements_in_charclass274);
                    includedelements22=includedelements();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_includedelements.add(includedelements22.getTree());

                    }
                    break;

            }

            // /Users/volker/Desktop/Regex/Regex.g:113:26: ( '^' excludedelements )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==31) ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // /Users/volker/Desktop/Regex/Regex.g:113:27: '^' excludedelements
                    {
                    char_literal23=(Token)match(input,31,FOLLOW_31_in_charclass278); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_31.add(char_literal23);

                    pushFollow(FOLLOW_excludedelements_in_charclass280);
                    excludedelements24=excludedelements();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_excludedelements.add(excludedelements24.getTree());

                    }
                    break;

            }

            char_literal25=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_charclass284); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBRACKET.add(char_literal25);



            // AST REWRITE
            // elements: includedelements, excludedelements
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 113:54: -> ^( CLASS ^( INCL ( includedelements )? ) ( ^( EXCL excludedelements ) )? )
            {
                // /Users/volker/Desktop/Regex/Regex.g:113:57: ^( CLASS ^( INCL ( includedelements )? ) ( ^( EXCL excludedelements ) )? )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(CLASS, "CLASS"), root_1);

                // /Users/volker/Desktop/Regex/Regex.g:113:65: ^( INCL ( includedelements )? )
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(INCL, "INCL"), root_2);

                // /Users/volker/Desktop/Regex/Regex.g:113:72: ( includedelements )?
                if ( stream_includedelements.hasNext() ) {
                    adaptor.addChild(root_2, stream_includedelements.nextTree());

                }
                stream_includedelements.reset();

                adaptor.addChild(root_1, root_2);
                }
                // /Users/volker/Desktop/Regex/Regex.g:113:91: ( ^( EXCL excludedelements ) )?
                if ( stream_excludedelements.hasNext() ) {
                    // /Users/volker/Desktop/Regex/Regex.g:113:91: ^( EXCL excludedelements )
                    {
                    Object root_2 = (Object)adaptor.nil();
                    root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(EXCL, "EXCL"), root_2);

                    adaptor.addChild(root_2, stream_excludedelements.nextTree());

                    adaptor.addChild(root_1, root_2);
                    }

                }
                stream_excludedelements.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 8, charclass_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "charclass"

    public static class includedelements_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "includedelements"
    // /Users/volker/Desktop/Regex/Regex.g:116:1: includedelements : ( classelement )* ;
    public final RegexParser.includedelements_return includedelements() throws RecognitionException {
        RegexParser.includedelements_return retval = new RegexParser.includedelements_return();
        retval.start = input.LT(1);
        int includedelements_StartIndex = input.index();
        Object root_0 = null;

        RegexParser.classelement_return classelement26 = null;



        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 9) ) { return retval; }
            // /Users/volker/Desktop/Regex/Regex.g:117:2: ( ( classelement )* )
            // /Users/volker/Desktop/Regex/Regex.g:117:4: ( classelement )*
            {
            root_0 = (Object)adaptor.nil();

            // /Users/volker/Desktop/Regex/Regex.g:117:4: ( classelement )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( ((LA12_0>=PREDEFINEDCLASS && LA12_0<=CODEDCHAR)) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // /Users/volker/Desktop/Regex/Regex.g:0:0: classelement
            	    {
            	    pushFollow(FOLLOW_classelement_in_includedelements315);
            	    classelement26=classelement();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, classelement26.getTree());

            	    }
            	    break;

            	default :
            	    break loop12;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 9, includedelements_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "includedelements"

    public static class excludedelements_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "excludedelements"
    // /Users/volker/Desktop/Regex/Regex.g:120:1: excludedelements : ( classelement )+ ;
    public final RegexParser.excludedelements_return excludedelements() throws RecognitionException {
        RegexParser.excludedelements_return retval = new RegexParser.excludedelements_return();
        retval.start = input.LT(1);
        int excludedelements_StartIndex = input.index();
        Object root_0 = null;

        RegexParser.classelement_return classelement27 = null;



        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 10) ) { return retval; }
            // /Users/volker/Desktop/Regex/Regex.g:121:2: ( ( classelement )+ )
            // /Users/volker/Desktop/Regex/Regex.g:121:4: ( classelement )+
            {
            root_0 = (Object)adaptor.nil();

            // /Users/volker/Desktop/Regex/Regex.g:121:4: ( classelement )+
            int cnt13=0;
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( ((LA13_0>=PREDEFINEDCLASS && LA13_0<=CODEDCHAR)) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // /Users/volker/Desktop/Regex/Regex.g:0:0: classelement
            	    {
            	    pushFollow(FOLLOW_classelement_in_excludedelements328);
            	    classelement27=classelement();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, classelement27.getTree());

            	    }
            	    break;

            	default :
            	    if ( cnt13 >= 1 ) break loop13;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(13, input);
                        throw eee;
                }
                cnt13++;
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 10, excludedelements_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "excludedelements"

    public static class classelement_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "classelement"
    // /Users/volker/Desktop/Regex/Regex.g:124:1: classelement : ( classchar | charrange | PREDEFINEDCLASS );
    public final RegexParser.classelement_return classelement() throws RecognitionException {
        RegexParser.classelement_return retval = new RegexParser.classelement_return();
        retval.start = input.LT(1);
        int classelement_StartIndex = input.index();
        Object root_0 = null;

        Token PREDEFINEDCLASS30=null;
        RegexParser.classchar_return classchar28 = null;

        RegexParser.charrange_return charrange29 = null;


        Object PREDEFINEDCLASS30_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 11) ) { return retval; }
            // /Users/volker/Desktop/Regex/Regex.g:125:2: ( classchar | charrange | PREDEFINEDCLASS )
            int alt14=3;
            int LA14_0 = input.LA(1);

            if ( ((LA14_0>=ALPHANUM && LA14_0<=CODEDCHAR)) ) {
                int LA14_1 = input.LA(2);

                if ( (LA14_1==34) ) {
                    alt14=2;
                }
                else if ( (LA14_1==EOF||(LA14_1>=PREDEFINEDCLASS && LA14_1<=CODEDCHAR)||(LA14_1>=RBRACKET && LA14_1<=31)) ) {
                    alt14=1;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 14, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA14_0==PREDEFINEDCLASS) ) {
                alt14=3;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 14, 0, input);

                throw nvae;
            }
            switch (alt14) {
                case 1 :
                    // /Users/volker/Desktop/Regex/Regex.g:125:4: classchar
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_classchar_in_classelement341);
                    classchar28=classchar();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, classchar28.getTree());

                    }
                    break;
                case 2 :
                    // /Users/volker/Desktop/Regex/Regex.g:126:4: charrange
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_charrange_in_classelement346);
                    charrange29=charrange();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, charrange29.getTree());

                    }
                    break;
                case 3 :
                    // /Users/volker/Desktop/Regex/Regex.g:127:4: PREDEFINEDCLASS
                    {
                    root_0 = (Object)adaptor.nil();

                    PREDEFINEDCLASS30=(Token)match(input,PREDEFINEDCLASS,FOLLOW_PREDEFINEDCLASS_in_classelement351); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    PREDEFINEDCLASS30_tree = (Object)adaptor.create(PREDEFINEDCLASS30);
                    adaptor.addChild(root_0, PREDEFINEDCLASS30_tree);
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 11, classelement_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "classelement"

    public static class charrange_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "charrange"
    // /Users/volker/Desktop/Regex/Regex.g:130:1: charrange : classchar '-' classchar -> ^( RANGE ( classchar )+ ) ;
    public final RegexParser.charrange_return charrange() throws RecognitionException {
        RegexParser.charrange_return retval = new RegexParser.charrange_return();
        retval.start = input.LT(1);
        int charrange_StartIndex = input.index();
        Object root_0 = null;

        Token char_literal32=null;
        RegexParser.classchar_return classchar31 = null;

        RegexParser.classchar_return classchar33 = null;


        Object char_literal32_tree=null;
        RewriteRuleTokenStream stream_34=new RewriteRuleTokenStream(adaptor,"token 34");
        RewriteRuleSubtreeStream stream_classchar=new RewriteRuleSubtreeStream(adaptor,"rule classchar");
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 12) ) { return retval; }
            // /Users/volker/Desktop/Regex/Regex.g:130:10: ( classchar '-' classchar -> ^( RANGE ( classchar )+ ) )
            // /Users/volker/Desktop/Regex/Regex.g:130:12: classchar '-' classchar
            {
            pushFollow(FOLLOW_classchar_in_charrange360);
            classchar31=classchar();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_classchar.add(classchar31.getTree());
            char_literal32=(Token)match(input,34,FOLLOW_34_in_charrange362); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_34.add(char_literal32);

            pushFollow(FOLLOW_classchar_in_charrange364);
            classchar33=classchar();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_classchar.add(classchar33.getTree());


            // AST REWRITE
            // elements: classchar
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 130:36: -> ^( RANGE ( classchar )+ )
            {
                // /Users/volker/Desktop/Regex/Regex.g:130:39: ^( RANGE ( classchar )+ )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(RANGE, "RANGE"), root_1);

                if ( !(stream_classchar.hasNext()) ) {
                    throw new RewriteEarlyExitException();
                }
                while ( stream_classchar.hasNext() ) {
                    adaptor.addChild(root_1, stream_classchar.nextTree());

                }
                stream_classchar.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 12, charrange_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "charrange"

    public static class group_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "group"
    // /Users/volker/Desktop/Regex/Regex.g:132:1: group : '(' expression ')' -> ^( GROUP expression ) ;
    public final RegexParser.group_return group() throws RecognitionException {
        RegexParser.group_return retval = new RegexParser.group_return();
        retval.start = input.LT(1);
        int group_StartIndex = input.index();
        Object root_0 = null;

        Token char_literal34=null;
        Token char_literal36=null;
        RegexParser.expression_return expression35 = null;


        Object char_literal34_tree=null;
        Object char_literal36_tree=null;
        RewriteRuleTokenStream stream_35=new RewriteRuleTokenStream(adaptor,"token 35");
        RewriteRuleTokenStream stream_36=new RewriteRuleTokenStream(adaptor,"token 36");
        RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 13) ) { return retval; }
            // /Users/volker/Desktop/Regex/Regex.g:132:7: ( '(' expression ')' -> ^( GROUP expression ) )
            // /Users/volker/Desktop/Regex/Regex.g:132:9: '(' expression ')'
            {
            char_literal34=(Token)match(input,35,FOLLOW_35_in_group381); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_35.add(char_literal34);

            pushFollow(FOLLOW_expression_in_group383);
            expression35=expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_expression.add(expression35.getTree());
            char_literal36=(Token)match(input,36,FOLLOW_36_in_group385); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_36.add(char_literal36);



            // AST REWRITE
            // elements: expression
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 132:28: -> ^( GROUP expression )
            {
                // /Users/volker/Desktop/Regex/Regex.g:132:31: ^( GROUP expression )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(GROUP, "GROUP"), root_1);

                adaptor.addChild(root_1, stream_expression.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 13, group_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "group"

    public static class quantifier_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "quantifier"
    // /Users/volker/Desktop/Regex/Regex.g:134:1: quantifier : ( SIMPLEQUANTIFIER | '{' min= INT ',' max= INT '}' -> ^( QUANT $min $max) | '{' INT ',' '}' -> ^( QUANT INT ) | '{' INT '}' -> ^( QUANT INT INT ) );
    public final RegexParser.quantifier_return quantifier() throws RecognitionException {
        RegexParser.quantifier_return retval = new RegexParser.quantifier_return();
        retval.start = input.LT(1);
        int quantifier_StartIndex = input.index();
        Object root_0 = null;

        Token min=null;
        Token max=null;
        Token SIMPLEQUANTIFIER37=null;
        Token char_literal38=null;
        Token char_literal39=null;
        Token char_literal40=null;
        Token char_literal41=null;
        Token INT42=null;
        Token char_literal43=null;
        Token char_literal44=null;
        Token char_literal45=null;
        Token INT46=null;
        Token char_literal47=null;

        Object min_tree=null;
        Object max_tree=null;
        Object SIMPLEQUANTIFIER37_tree=null;
        Object char_literal38_tree=null;
        Object char_literal39_tree=null;
        Object char_literal40_tree=null;
        Object char_literal41_tree=null;
        Object INT42_tree=null;
        Object char_literal43_tree=null;
        Object char_literal44_tree=null;
        Object char_literal45_tree=null;
        Object INT46_tree=null;
        Object char_literal47_tree=null;
        RewriteRuleTokenStream stream_RBRACE=new RewriteRuleTokenStream(adaptor,"token RBRACE");
        RewriteRuleTokenStream stream_37=new RewriteRuleTokenStream(adaptor,"token 37");
        RewriteRuleTokenStream stream_INT=new RewriteRuleTokenStream(adaptor,"token INT");
        RewriteRuleTokenStream stream_LBRACE=new RewriteRuleTokenStream(adaptor,"token LBRACE");

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 14) ) { return retval; }
            // /Users/volker/Desktop/Regex/Regex.g:135:2: ( SIMPLEQUANTIFIER | '{' min= INT ',' max= INT '}' -> ^( QUANT $min $max) | '{' INT ',' '}' -> ^( QUANT INT ) | '{' INT '}' -> ^( QUANT INT INT ) )
            int alt15=4;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==SIMPLEQUANTIFIER) ) {
                alt15=1;
            }
            else if ( (LA15_0==LBRACE) ) {
                int LA15_2 = input.LA(2);

                if ( (LA15_2==INT) ) {
                    int LA15_3 = input.LA(3);

                    if ( (LA15_3==37) ) {
                        int LA15_4 = input.LA(4);

                        if ( (LA15_4==INT) ) {
                            alt15=2;
                        }
                        else if ( (LA15_4==RBRACE) ) {
                            alt15=3;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 15, 4, input);

                            throw nvae;
                        }
                    }
                    else if ( (LA15_3==RBRACE) ) {
                        alt15=4;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 15, 3, input);

                        throw nvae;
                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 2, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 15, 0, input);

                throw nvae;
            }
            switch (alt15) {
                case 1 :
                    // /Users/volker/Desktop/Regex/Regex.g:135:4: SIMPLEQUANTIFIER
                    {
                    root_0 = (Object)adaptor.nil();

                    SIMPLEQUANTIFIER37=(Token)match(input,SIMPLEQUANTIFIER,FOLLOW_SIMPLEQUANTIFIER_in_quantifier402); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    SIMPLEQUANTIFIER37_tree = (Object)adaptor.create(SIMPLEQUANTIFIER37);
                    adaptor.addChild(root_0, SIMPLEQUANTIFIER37_tree);
                    }

                    }
                    break;
                case 2 :
                    // /Users/volker/Desktop/Regex/Regex.g:136:4: '{' min= INT ',' max= INT '}'
                    {
                    char_literal38=(Token)match(input,LBRACE,FOLLOW_LBRACE_in_quantifier407); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBRACE.add(char_literal38);

                    min=(Token)match(input,INT,FOLLOW_INT_in_quantifier411); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_INT.add(min);

                    char_literal39=(Token)match(input,37,FOLLOW_37_in_quantifier413); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_37.add(char_literal39);

                    max=(Token)match(input,INT,FOLLOW_INT_in_quantifier417); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_INT.add(max);

                    char_literal40=(Token)match(input,RBRACE,FOLLOW_RBRACE_in_quantifier419); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBRACE.add(char_literal40);



                    // AST REWRITE
                    // elements: min, max
                    // token labels: min, max
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_min=new RewriteRuleTokenStream(adaptor,"token min",min);
                    RewriteRuleTokenStream stream_max=new RewriteRuleTokenStream(adaptor,"token max",max);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 136:32: -> ^( QUANT $min $max)
                    {
                        // /Users/volker/Desktop/Regex/Regex.g:136:35: ^( QUANT $min $max)
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(QUANT, "QUANT"), root_1);

                        adaptor.addChild(root_1, stream_min.nextNode());
                        adaptor.addChild(root_1, stream_max.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // /Users/volker/Desktop/Regex/Regex.g:137:4: '{' INT ',' '}'
                    {
                    char_literal41=(Token)match(input,LBRACE,FOLLOW_LBRACE_in_quantifier436); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBRACE.add(char_literal41);

                    INT42=(Token)match(input,INT,FOLLOW_INT_in_quantifier438); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_INT.add(INT42);

                    char_literal43=(Token)match(input,37,FOLLOW_37_in_quantifier440); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_37.add(char_literal43);

                    char_literal44=(Token)match(input,RBRACE,FOLLOW_RBRACE_in_quantifier442); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBRACE.add(char_literal44);



                    // AST REWRITE
                    // elements: INT
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 137:20: -> ^( QUANT INT )
                    {
                        // /Users/volker/Desktop/Regex/Regex.g:137:23: ^( QUANT INT )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(QUANT, "QUANT"), root_1);

                        adaptor.addChild(root_1, stream_INT.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 4 :
                    // /Users/volker/Desktop/Regex/Regex.g:138:4: '{' INT '}'
                    {
                    char_literal45=(Token)match(input,LBRACE,FOLLOW_LBRACE_in_quantifier455); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBRACE.add(char_literal45);

                    INT46=(Token)match(input,INT,FOLLOW_INT_in_quantifier457); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_INT.add(INT46);

                    char_literal47=(Token)match(input,RBRACE,FOLLOW_RBRACE_in_quantifier459); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBRACE.add(char_literal47);



                    // AST REWRITE
                    // elements: INT, INT
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 138:16: -> ^( QUANT INT INT )
                    {
                        // /Users/volker/Desktop/Regex/Regex.g:138:19: ^( QUANT INT INT )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(QUANT, "QUANT"), root_1);

                        adaptor.addChild(root_1, stream_INT.nextNode());
                        adaptor.addChild(root_1, stream_INT.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
          throw e;
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 14, quantifier_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "quantifier"

    // $ANTLR start synpred4_Regex
    public final void synpred4_Regex_fragment() throws RecognitionException {   
        // /Users/volker/Desktop/Regex/Regex.g:81:4: ( sequence ( '|' sequence )+ )
        // /Users/volker/Desktop/Regex/Regex.g:81:4: sequence ( '|' sequence )+
        {
        pushFollow(FOLLOW_sequence_in_synpred4_Regex91);
        sequence();

        state._fsp--;
        if (state.failed) return ;
        // /Users/volker/Desktop/Regex/Regex.g:81:13: ( '|' sequence )+
        int cnt16=0;
        loop16:
        do {
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==33) ) {
                alt16=1;
            }


            switch (alt16) {
        	case 1 :
        	    // /Users/volker/Desktop/Regex/Regex.g:81:14: '|' sequence
        	    {
        	    match(input,33,FOLLOW_33_in_synpred4_Regex94); if (state.failed) return ;
        	    pushFollow(FOLLOW_sequence_in_synpred4_Regex96);
        	    sequence();

        	    state._fsp--;
        	    if (state.failed) return ;

        	    }
        	    break;

        	default :
        	    if ( cnt16 >= 1 ) break loop16;
        	    if (state.backtracking>0) {state.failed=true; return ;}
                    EarlyExitException eee =
                        new EarlyExitException(16, input);
                    throw eee;
            }
            cnt16++;
        } while (true);


        }
    }
    // $ANTLR end synpred4_Regex

    // $ANTLR start synpred6_Regex
    public final void synpred6_Regex_fragment() throws RecognitionException {   
        // /Users/volker/Desktop/Regex/Regex.g:85:11: ( factor ( factor )+ )
        // /Users/volker/Desktop/Regex/Regex.g:85:11: factor ( factor )+
        {
        pushFollow(FOLLOW_factor_in_synpred6_Regex121);
        factor();

        state._fsp--;
        if (state.failed) return ;
        // /Users/volker/Desktop/Regex/Regex.g:85:18: ( factor )+
        int cnt17=0;
        loop17:
        do {
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( ((LA17_0>=PREDEFINEDCLASS && LA17_0<=CODEDCHAR)||LA17_0==LBRACKET||(LA17_0>=34 && LA17_0<=35)) ) {
                alt17=1;
            }


            switch (alt17) {
        	case 1 :
        	    // /Users/volker/Desktop/Regex/Regex.g:0:0: factor
        	    {
        	    pushFollow(FOLLOW_factor_in_synpred6_Regex123);
        	    factor();

        	    state._fsp--;
        	    if (state.failed) return ;

        	    }
        	    break;

        	default :
        	    if ( cnt17 >= 1 ) break loop17;
        	    if (state.backtracking>0) {state.failed=true; return ;}
                    EarlyExitException eee =
                        new EarlyExitException(17, input);
                    throw eee;
            }
            cnt17++;
        } while (true);


        }
    }
    // $ANTLR end synpred6_Regex

    // $ANTLR start synpred7_Regex
    public final void synpred7_Regex_fragment() throws RecognitionException {   
        // /Users/volker/Desktop/Regex/Regex.g:88:10: ( atom quantifier )
        // /Users/volker/Desktop/Regex/Regex.g:88:10: atom quantifier
        {
        pushFollow(FOLLOW_atom_in_synpred7_Regex146);
        atom();

        state._fsp--;
        if (state.failed) return ;
        pushFollow(FOLLOW_quantifier_in_synpred7_Regex148);
        quantifier();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred7_Regex

    // $ANTLR start synpred18_Regex
    public final void synpred18_Regex_fragment() throws RecognitionException {   
        // /Users/volker/Desktop/Regex/Regex.g:113:8: ( includedelements )
        // /Users/volker/Desktop/Regex/Regex.g:113:8: includedelements
        {
        pushFollow(FOLLOW_includedelements_in_synpred18_Regex274);
        includedelements();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred18_Regex

    // Delegated rules

    public final boolean synpred7_Regex() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred7_Regex_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred6_Regex() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred6_Regex_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred18_Regex() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred18_Regex_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred4_Regex() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred4_Regex_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


 

    public static final BitSet FOLLOW_31_in_expression74 = new BitSet(new long[]{0x0000000C20000FF0L});
    public static final BitSet FOLLOW_choice_in_expression78 = new BitSet(new long[]{0x0000000100000002L});
    public static final BitSet FOLLOW_32_in_expression80 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sequence_in_choice91 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_33_in_choice94 = new BitSet(new long[]{0x0000000C20000FF0L});
    public static final BitSet FOLLOW_sequence_in_choice96 = new BitSet(new long[]{0x0000000200000002L});
    public static final BitSet FOLLOW_sequence_in_choice112 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_factor_in_sequence121 = new BitSet(new long[]{0x0000000C20000FF0L});
    public static final BitSet FOLLOW_factor_in_sequence123 = new BitSet(new long[]{0x0000000C20000FF2L});
    public static final BitSet FOLLOW_factor_in_sequence138 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_atom_in_factor146 = new BitSet(new long[]{0x0000000008001000L});
    public static final BitSet FOLLOW_quantifier_in_factor148 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_atom_in_factor163 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_singlechar_in_atom171 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_group_in_atom176 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classchar_in_singlechar187 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_34_in_singlechar193 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_charclass_in_singlechar199 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PREDEFINEDCLASS_in_singlechar204 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_classchar0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_charclass272 = new BitSet(new long[]{0x00000000C0000FF0L});
    public static final BitSet FOLLOW_includedelements_in_charclass274 = new BitSet(new long[]{0x00000000C0000000L});
    public static final BitSet FOLLOW_31_in_charclass278 = new BitSet(new long[]{0x0000000000000FF0L});
    public static final BitSet FOLLOW_excludedelements_in_charclass280 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_RBRACKET_in_charclass284 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classelement_in_includedelements315 = new BitSet(new long[]{0x0000000000000FF2L});
    public static final BitSet FOLLOW_classelement_in_excludedelements328 = new BitSet(new long[]{0x0000000000000FF2L});
    public static final BitSet FOLLOW_classchar_in_classelement341 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_charrange_in_classelement346 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PREDEFINEDCLASS_in_classelement351 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classchar_in_charrange360 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_34_in_charrange362 = new BitSet(new long[]{0x0000000000000FE0L});
    public static final BitSet FOLLOW_classchar_in_charrange364 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_35_in_group381 = new BitSet(new long[]{0x0000000CA0000FF0L});
    public static final BitSet FOLLOW_expression_in_group383 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_36_in_group385 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SIMPLEQUANTIFIER_in_quantifier402 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_quantifier407 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_INT_in_quantifier411 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_37_in_quantifier413 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_INT_in_quantifier417 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_RBRACE_in_quantifier419 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_quantifier436 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_INT_in_quantifier438 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_37_in_quantifier440 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_RBRACE_in_quantifier442 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_quantifier455 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_INT_in_quantifier457 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_RBRACE_in_quantifier459 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sequence_in_synpred4_Regex91 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_33_in_synpred4_Regex94 = new BitSet(new long[]{0x0000000C20000FF0L});
    public static final BitSet FOLLOW_sequence_in_synpred4_Regex96 = new BitSet(new long[]{0x0000000200000002L});
    public static final BitSet FOLLOW_factor_in_synpred6_Regex121 = new BitSet(new long[]{0x0000000C20000FF0L});
    public static final BitSet FOLLOW_factor_in_synpred6_Regex123 = new BitSet(new long[]{0x0000000C20000FF2L});
    public static final BitSet FOLLOW_atom_in_synpred7_Regex146 = new BitSet(new long[]{0x0000000008001000L});
    public static final BitSet FOLLOW_quantifier_in_synpred7_Regex148 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_includedelements_in_synpred18_Regex274 = new BitSet(new long[]{0x0000000000000002L});

}