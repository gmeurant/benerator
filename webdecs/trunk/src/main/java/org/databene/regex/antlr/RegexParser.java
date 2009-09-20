// $ANTLR 3.1.2 /Users/volker/Desktop/Regex/Regex.g 2009-09-20 18:40:33

	package org.databene.regex.antlr;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.tree.*;

public class RegexParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "PREDEFINEDCLASS", "ALPHANUM", "SPECIALCHARACTER", "ESCAPEDEDCHARACTER", "NONTYPEABLECHARACTER", "OCTALCHAR", "HEXCHAR", "CODEDCHAR", "SIMPLEQUANTIFIER", "INT", "CHOICE", "SEQUENCE", "FACTOR", "CLASS", "RANGE", "INCL", "EXCL", "QUANT", "LETTER", "DIGIT", "OCTALDIGIT", "HEXDIGIT", "LBRACE", "RBRACE", "LBRACKET", "RBRACKET", "'^'", "'$'", "'|'", "'-'", "'('", "')'", "','"
    };
    public static final int HEXDIGIT=25;
    public static final int LETTER=22;
    public static final int RANGE=18;
    public static final int T__35=35;
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
    public static final int SEQUENCE=15;
    public static final int INT=13;
    public static final int T__31=31;
    public static final int EOF=-1;
    public static final int HEXCHAR=10;
    public static final int RBRACKET=29;
    public static final int T__32=32;
    public static final int SIMPLEQUANTIFIER=12;
    public static final int DIGIT=23;
    public static final int INCL=19;
    public static final int FACTOR=16;
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
            this.state.ruleMemo = new HashMap[39+1];
             
             
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

            if ( (LA1_0==30) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // /Users/volker/Desktop/Regex/Regex.g:0:0: '^'
                    {
                    char_literal1=(Token)match(input,30,FOLLOW_30_in_expression74); if (state.failed) return retval;

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

            if ( (LA2_0==31) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // /Users/volker/Desktop/Regex/Regex.g:0:0: '$'
                    {
                    char_literal3=(Token)match(input,31,FOLLOW_31_in_expression80); if (state.failed) return retval;

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
        RewriteRuleTokenStream stream_32=new RewriteRuleTokenStream(adaptor,"token 32");
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
            case 33:
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
            case 34:
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

                        if ( (LA3_0==32) ) {
                            alt3=1;
                        }


                        switch (alt3) {
                    	case 1 :
                    	    // /Users/volker/Desktop/Regex/Regex.g:81:14: '|' sequence
                    	    {
                    	    char_literal5=(Token)match(input,32,FOLLOW_32_in_choice94); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_32.add(char_literal5);

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
            case 33:
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
            case 34:
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

                        if ( ((LA5_0>=PREDEFINEDCLASS && LA5_0<=CODEDCHAR)||LA5_0==LBRACKET||(LA5_0>=33 && LA5_0<=34)) ) {
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
            case 33:
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
            case 34:
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
                    // elements: atom, quantifier
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
    // /Users/volker/Desktop/Regex/Regex.g:91:1: atom : ( classchar | '-' | charclass | PREDEFINEDCLASS | group );
    public final RegexParser.atom_return atom() throws RecognitionException {
        RegexParser.atom_return retval = new RegexParser.atom_return();
        retval.start = input.LT(1);
        int atom_StartIndex = input.index();
        Object root_0 = null;

        Token char_literal15=null;
        Token PREDEFINEDCLASS17=null;
        RegexParser.classchar_return classchar14 = null;

        RegexParser.charclass_return charclass16 = null;

        RegexParser.group_return group18 = null;


        Object char_literal15_tree=null;
        Object PREDEFINEDCLASS17_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 5) ) { return retval; }
            // /Users/volker/Desktop/Regex/Regex.g:91:6: ( classchar | '-' | charclass | PREDEFINEDCLASS | group )
            int alt8=5;
            switch ( input.LA(1) ) {
            case ALPHANUM:
            case SPECIALCHARACTER:
            case ESCAPEDEDCHARACTER:
            case NONTYPEABLECHARACTER:
            case OCTALCHAR:
            case HEXCHAR:
            case CODEDCHAR:
                {
                alt8=1;
                }
                break;
            case 33:
                {
                alt8=2;
                }
                break;
            case LBRACKET:
                {
                alt8=3;
                }
                break;
            case PREDEFINEDCLASS:
                {
                alt8=4;
                }
                break;
            case 34:
                {
                alt8=5;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;
            }

            switch (alt8) {
                case 1 :
                    // /Users/volker/Desktop/Regex/Regex.g:91:8: classchar
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_classchar_in_atom171);
                    classchar14=classchar();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) root_0 = (Object)adaptor.becomeRoot(classchar14.getTree(), root_0);

                    }
                    break;
                case 2 :
                    // /Users/volker/Desktop/Regex/Regex.g:92:4: '-'
                    {
                    root_0 = (Object)adaptor.nil();

                    char_literal15=(Token)match(input,33,FOLLOW_33_in_atom177); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal15_tree = (Object)adaptor.create(char_literal15);
                    root_0 = (Object)adaptor.becomeRoot(char_literal15_tree, root_0);
                    }

                    }
                    break;
                case 3 :
                    // /Users/volker/Desktop/Regex/Regex.g:93:4: charclass
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_charclass_in_atom183);
                    charclass16=charclass();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, charclass16.getTree());

                    }
                    break;
                case 4 :
                    // /Users/volker/Desktop/Regex/Regex.g:94:4: PREDEFINEDCLASS
                    {
                    root_0 = (Object)adaptor.nil();

                    PREDEFINEDCLASS17=(Token)match(input,PREDEFINEDCLASS,FOLLOW_PREDEFINEDCLASS_in_atom188); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    PREDEFINEDCLASS17_tree = (Object)adaptor.create(PREDEFINEDCLASS17);
                    root_0 = (Object)adaptor.becomeRoot(PREDEFINEDCLASS17_tree, root_0);
                    }

                    }
                    break;
                case 5 :
                    // /Users/volker/Desktop/Regex/Regex.g:95:4: group
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_group_in_atom194);
                    group18=group();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, group18.getTree());

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

    public static class classchar_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "classchar"
    // /Users/volker/Desktop/Regex/Regex.g:98:1: classchar : ( ALPHANUM | SPECIALCHARACTER | ESCAPEDEDCHARACTER | NONTYPEABLECHARACTER | OCTALCHAR | HEXCHAR | CODEDCHAR );
    public final RegexParser.classchar_return classchar() throws RecognitionException {
        RegexParser.classchar_return retval = new RegexParser.classchar_return();
        retval.start = input.LT(1);
        int classchar_StartIndex = input.index();
        Object root_0 = null;

        Token set19=null;

        Object set19_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 6) ) { return retval; }
            // /Users/volker/Desktop/Regex/Regex.g:99:2: ( ALPHANUM | SPECIALCHARACTER | ESCAPEDEDCHARACTER | NONTYPEABLECHARACTER | OCTALCHAR | HEXCHAR | CODEDCHAR )
            // /Users/volker/Desktop/Regex/Regex.g:
            {
            root_0 = (Object)adaptor.nil();

            set19=(Token)input.LT(1);
            if ( (input.LA(1)>=ALPHANUM && input.LA(1)<=CODEDCHAR) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set19));
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
            if ( state.backtracking>0 ) { memoize(input, 6, classchar_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "classchar"

    public static class charclass_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "charclass"
    // /Users/volker/Desktop/Regex/Regex.g:108:1: charclass : '[' ( includedelements )? ( '^' excludedelements )? ']' -> ^( CLASS ^( INCL ( includedelements )? ) ( ^( EXCL excludedelements ) )? ) ;
    public final RegexParser.charclass_return charclass() throws RecognitionException {
        RegexParser.charclass_return retval = new RegexParser.charclass_return();
        retval.start = input.LT(1);
        int charclass_StartIndex = input.index();
        Object root_0 = null;

        Token char_literal20=null;
        Token char_literal22=null;
        Token char_literal24=null;
        RegexParser.includedelements_return includedelements21 = null;

        RegexParser.excludedelements_return excludedelements23 = null;


        Object char_literal20_tree=null;
        Object char_literal22_tree=null;
        Object char_literal24_tree=null;
        RewriteRuleTokenStream stream_30=new RewriteRuleTokenStream(adaptor,"token 30");
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleSubtreeStream stream_excludedelements=new RewriteRuleSubtreeStream(adaptor,"rule excludedelements");
        RewriteRuleSubtreeStream stream_includedelements=new RewriteRuleSubtreeStream(adaptor,"rule includedelements");
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 7) ) { return retval; }
            // /Users/volker/Desktop/Regex/Regex.g:109:2: ( '[' ( includedelements )? ( '^' excludedelements )? ']' -> ^( CLASS ^( INCL ( includedelements )? ) ( ^( EXCL excludedelements ) )? ) )
            // /Users/volker/Desktop/Regex/Regex.g:109:4: '[' ( includedelements )? ( '^' excludedelements )? ']'
            {
            char_literal20=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_charclass260); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBRACKET.add(char_literal20);

            // /Users/volker/Desktop/Regex/Regex.g:109:8: ( includedelements )?
            int alt9=2;
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
                    alt9=1;
                    }
                    break;
                case 30:
                    {
                    int LA9_2 = input.LA(2);

                    if ( (synpred18_Regex()) ) {
                        alt9=1;
                    }
                    }
                    break;
                case RBRACKET:
                    {
                    int LA9_3 = input.LA(2);

                    if ( (synpred18_Regex()) ) {
                        alt9=1;
                    }
                    }
                    break;
            }

            switch (alt9) {
                case 1 :
                    // /Users/volker/Desktop/Regex/Regex.g:0:0: includedelements
                    {
                    pushFollow(FOLLOW_includedelements_in_charclass262);
                    includedelements21=includedelements();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_includedelements.add(includedelements21.getTree());

                    }
                    break;

            }

            // /Users/volker/Desktop/Regex/Regex.g:109:26: ( '^' excludedelements )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==30) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // /Users/volker/Desktop/Regex/Regex.g:109:27: '^' excludedelements
                    {
                    char_literal22=(Token)match(input,30,FOLLOW_30_in_charclass266); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_30.add(char_literal22);

                    pushFollow(FOLLOW_excludedelements_in_charclass268);
                    excludedelements23=excludedelements();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_excludedelements.add(excludedelements23.getTree());

                    }
                    break;

            }

            char_literal24=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_charclass272); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBRACKET.add(char_literal24);



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
            // 109:54: -> ^( CLASS ^( INCL ( includedelements )? ) ( ^( EXCL excludedelements ) )? )
            {
                // /Users/volker/Desktop/Regex/Regex.g:109:57: ^( CLASS ^( INCL ( includedelements )? ) ( ^( EXCL excludedelements ) )? )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(CLASS, "CLASS"), root_1);

                // /Users/volker/Desktop/Regex/Regex.g:109:65: ^( INCL ( includedelements )? )
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(INCL, "INCL"), root_2);

                // /Users/volker/Desktop/Regex/Regex.g:109:72: ( includedelements )?
                if ( stream_includedelements.hasNext() ) {
                    adaptor.addChild(root_2, stream_includedelements.nextTree());

                }
                stream_includedelements.reset();

                adaptor.addChild(root_1, root_2);
                }
                // /Users/volker/Desktop/Regex/Regex.g:109:91: ( ^( EXCL excludedelements ) )?
                if ( stream_excludedelements.hasNext() ) {
                    // /Users/volker/Desktop/Regex/Regex.g:109:91: ^( EXCL excludedelements )
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
            if ( state.backtracking>0 ) { memoize(input, 7, charclass_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "charclass"

    public static class includedelements_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "includedelements"
    // /Users/volker/Desktop/Regex/Regex.g:112:1: includedelements : ( classelement )* ;
    public final RegexParser.includedelements_return includedelements() throws RecognitionException {
        RegexParser.includedelements_return retval = new RegexParser.includedelements_return();
        retval.start = input.LT(1);
        int includedelements_StartIndex = input.index();
        Object root_0 = null;

        RegexParser.classelement_return classelement25 = null;



        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 8) ) { return retval; }
            // /Users/volker/Desktop/Regex/Regex.g:113:2: ( ( classelement )* )
            // /Users/volker/Desktop/Regex/Regex.g:113:4: ( classelement )*
            {
            root_0 = (Object)adaptor.nil();

            // /Users/volker/Desktop/Regex/Regex.g:113:4: ( classelement )*
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( ((LA11_0>=PREDEFINEDCLASS && LA11_0<=CODEDCHAR)) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // /Users/volker/Desktop/Regex/Regex.g:0:0: classelement
            	    {
            	    pushFollow(FOLLOW_classelement_in_includedelements303);
            	    classelement25=classelement();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, classelement25.getTree());

            	    }
            	    break;

            	default :
            	    break loop11;
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
            if ( state.backtracking>0 ) { memoize(input, 8, includedelements_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "includedelements"

    public static class excludedelements_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "excludedelements"
    // /Users/volker/Desktop/Regex/Regex.g:116:1: excludedelements : ( classelement )+ ;
    public final RegexParser.excludedelements_return excludedelements() throws RecognitionException {
        RegexParser.excludedelements_return retval = new RegexParser.excludedelements_return();
        retval.start = input.LT(1);
        int excludedelements_StartIndex = input.index();
        Object root_0 = null;

        RegexParser.classelement_return classelement26 = null;



        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 9) ) { return retval; }
            // /Users/volker/Desktop/Regex/Regex.g:117:2: ( ( classelement )+ )
            // /Users/volker/Desktop/Regex/Regex.g:117:4: ( classelement )+
            {
            root_0 = (Object)adaptor.nil();

            // /Users/volker/Desktop/Regex/Regex.g:117:4: ( classelement )+
            int cnt12=0;
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
            	    pushFollow(FOLLOW_classelement_in_excludedelements316);
            	    classelement26=classelement();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, classelement26.getTree());

            	    }
            	    break;

            	default :
            	    if ( cnt12 >= 1 ) break loop12;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(12, input);
                        throw eee;
                }
                cnt12++;
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
            if ( state.backtracking>0 ) { memoize(input, 9, excludedelements_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "excludedelements"

    public static class classelement_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "classelement"
    // /Users/volker/Desktop/Regex/Regex.g:120:1: classelement : ( classchar | charrange | PREDEFINEDCLASS );
    public final RegexParser.classelement_return classelement() throws RecognitionException {
        RegexParser.classelement_return retval = new RegexParser.classelement_return();
        retval.start = input.LT(1);
        int classelement_StartIndex = input.index();
        Object root_0 = null;

        Token PREDEFINEDCLASS29=null;
        RegexParser.classchar_return classchar27 = null;

        RegexParser.charrange_return charrange28 = null;


        Object PREDEFINEDCLASS29_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 10) ) { return retval; }
            // /Users/volker/Desktop/Regex/Regex.g:121:2: ( classchar | charrange | PREDEFINEDCLASS )
            int alt13=3;
            int LA13_0 = input.LA(1);

            if ( ((LA13_0>=ALPHANUM && LA13_0<=CODEDCHAR)) ) {
                int LA13_1 = input.LA(2);

                if ( (LA13_1==33) ) {
                    alt13=2;
                }
                else if ( (LA13_1==EOF||(LA13_1>=PREDEFINEDCLASS && LA13_1<=CODEDCHAR)||(LA13_1>=RBRACKET && LA13_1<=30)) ) {
                    alt13=1;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 13, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA13_0==PREDEFINEDCLASS) ) {
                alt13=3;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 13, 0, input);

                throw nvae;
            }
            switch (alt13) {
                case 1 :
                    // /Users/volker/Desktop/Regex/Regex.g:121:4: classchar
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_classchar_in_classelement329);
                    classchar27=classchar();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, classchar27.getTree());

                    }
                    break;
                case 2 :
                    // /Users/volker/Desktop/Regex/Regex.g:122:4: charrange
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_charrange_in_classelement334);
                    charrange28=charrange();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, charrange28.getTree());

                    }
                    break;
                case 3 :
                    // /Users/volker/Desktop/Regex/Regex.g:123:4: PREDEFINEDCLASS
                    {
                    root_0 = (Object)adaptor.nil();

                    PREDEFINEDCLASS29=(Token)match(input,PREDEFINEDCLASS,FOLLOW_PREDEFINEDCLASS_in_classelement339); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    PREDEFINEDCLASS29_tree = (Object)adaptor.create(PREDEFINEDCLASS29);
                    adaptor.addChild(root_0, PREDEFINEDCLASS29_tree);
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
            if ( state.backtracking>0 ) { memoize(input, 10, classelement_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "classelement"

    public static class charrange_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "charrange"
    // /Users/volker/Desktop/Regex/Regex.g:126:1: charrange : classchar '-' classchar -> ^( RANGE ( classchar )+ ) ;
    public final RegexParser.charrange_return charrange() throws RecognitionException {
        RegexParser.charrange_return retval = new RegexParser.charrange_return();
        retval.start = input.LT(1);
        int charrange_StartIndex = input.index();
        Object root_0 = null;

        Token char_literal31=null;
        RegexParser.classchar_return classchar30 = null;

        RegexParser.classchar_return classchar32 = null;


        Object char_literal31_tree=null;
        RewriteRuleTokenStream stream_33=new RewriteRuleTokenStream(adaptor,"token 33");
        RewriteRuleSubtreeStream stream_classchar=new RewriteRuleSubtreeStream(adaptor,"rule classchar");
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 11) ) { return retval; }
            // /Users/volker/Desktop/Regex/Regex.g:126:10: ( classchar '-' classchar -> ^( RANGE ( classchar )+ ) )
            // /Users/volker/Desktop/Regex/Regex.g:126:12: classchar '-' classchar
            {
            pushFollow(FOLLOW_classchar_in_charrange348);
            classchar30=classchar();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_classchar.add(classchar30.getTree());
            char_literal31=(Token)match(input,33,FOLLOW_33_in_charrange350); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_33.add(char_literal31);

            pushFollow(FOLLOW_classchar_in_charrange352);
            classchar32=classchar();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_classchar.add(classchar32.getTree());


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
            // 126:36: -> ^( RANGE ( classchar )+ )
            {
                // /Users/volker/Desktop/Regex/Regex.g:126:39: ^( RANGE ( classchar )+ )
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
            if ( state.backtracking>0 ) { memoize(input, 11, charrange_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "charrange"

    public static class group_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "group"
    // /Users/volker/Desktop/Regex/Regex.g:128:1: group : '(' expression ')' ;
    public final RegexParser.group_return group() throws RecognitionException {
        RegexParser.group_return retval = new RegexParser.group_return();
        retval.start = input.LT(1);
        int group_StartIndex = input.index();
        Object root_0 = null;

        Token char_literal33=null;
        Token char_literal35=null;
        RegexParser.expression_return expression34 = null;


        Object char_literal33_tree=null;
        Object char_literal35_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 12) ) { return retval; }
            // /Users/volker/Desktop/Regex/Regex.g:128:7: ( '(' expression ')' )
            // /Users/volker/Desktop/Regex/Regex.g:128:9: '(' expression ')'
            {
            root_0 = (Object)adaptor.nil();

            char_literal33=(Token)match(input,34,FOLLOW_34_in_group369); if (state.failed) return retval;
            pushFollow(FOLLOW_expression_in_group372);
            expression34=expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, expression34.getTree());
            char_literal35=(Token)match(input,35,FOLLOW_35_in_group374); if (state.failed) return retval;

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
            if ( state.backtracking>0 ) { memoize(input, 12, group_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "group"

    public static class quantifier_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "quantifier"
    // /Users/volker/Desktop/Regex/Regex.g:130:1: quantifier : ( SIMPLEQUANTIFIER | '{' min= INT ',' max= INT '}' -> ^( QUANT $min $max) | '{' INT ',' '}' -> ^( QUANT INT ) | '{' INT '}' -> ^( QUANT INT INT ) );
    public final RegexParser.quantifier_return quantifier() throws RecognitionException {
        RegexParser.quantifier_return retval = new RegexParser.quantifier_return();
        retval.start = input.LT(1);
        int quantifier_StartIndex = input.index();
        Object root_0 = null;

        Token min=null;
        Token max=null;
        Token SIMPLEQUANTIFIER36=null;
        Token char_literal37=null;
        Token char_literal38=null;
        Token char_literal39=null;
        Token char_literal40=null;
        Token INT41=null;
        Token char_literal42=null;
        Token char_literal43=null;
        Token char_literal44=null;
        Token INT45=null;
        Token char_literal46=null;

        Object min_tree=null;
        Object max_tree=null;
        Object SIMPLEQUANTIFIER36_tree=null;
        Object char_literal37_tree=null;
        Object char_literal38_tree=null;
        Object char_literal39_tree=null;
        Object char_literal40_tree=null;
        Object INT41_tree=null;
        Object char_literal42_tree=null;
        Object char_literal43_tree=null;
        Object char_literal44_tree=null;
        Object INT45_tree=null;
        Object char_literal46_tree=null;
        RewriteRuleTokenStream stream_RBRACE=new RewriteRuleTokenStream(adaptor,"token RBRACE");
        RewriteRuleTokenStream stream_INT=new RewriteRuleTokenStream(adaptor,"token INT");
        RewriteRuleTokenStream stream_LBRACE=new RewriteRuleTokenStream(adaptor,"token LBRACE");
        RewriteRuleTokenStream stream_36=new RewriteRuleTokenStream(adaptor,"token 36");

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 13) ) { return retval; }
            // /Users/volker/Desktop/Regex/Regex.g:131:2: ( SIMPLEQUANTIFIER | '{' min= INT ',' max= INT '}' -> ^( QUANT $min $max) | '{' INT ',' '}' -> ^( QUANT INT ) | '{' INT '}' -> ^( QUANT INT INT ) )
            int alt14=4;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==SIMPLEQUANTIFIER) ) {
                alt14=1;
            }
            else if ( (LA14_0==LBRACE) ) {
                int LA14_2 = input.LA(2);

                if ( (LA14_2==INT) ) {
                    int LA14_3 = input.LA(3);

                    if ( (LA14_3==36) ) {
                        int LA14_4 = input.LA(4);

                        if ( (LA14_4==INT) ) {
                            alt14=2;
                        }
                        else if ( (LA14_4==RBRACE) ) {
                            alt14=3;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 14, 4, input);

                            throw nvae;
                        }
                    }
                    else if ( (LA14_3==RBRACE) ) {
                        alt14=4;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 14, 3, input);

                        throw nvae;
                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 14, 2, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 14, 0, input);

                throw nvae;
            }
            switch (alt14) {
                case 1 :
                    // /Users/volker/Desktop/Regex/Regex.g:131:4: SIMPLEQUANTIFIER
                    {
                    root_0 = (Object)adaptor.nil();

                    SIMPLEQUANTIFIER36=(Token)match(input,SIMPLEQUANTIFIER,FOLLOW_SIMPLEQUANTIFIER_in_quantifier384); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    SIMPLEQUANTIFIER36_tree = (Object)adaptor.create(SIMPLEQUANTIFIER36);
                    adaptor.addChild(root_0, SIMPLEQUANTIFIER36_tree);
                    }

                    }
                    break;
                case 2 :
                    // /Users/volker/Desktop/Regex/Regex.g:132:4: '{' min= INT ',' max= INT '}'
                    {
                    char_literal37=(Token)match(input,LBRACE,FOLLOW_LBRACE_in_quantifier389); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBRACE.add(char_literal37);

                    min=(Token)match(input,INT,FOLLOW_INT_in_quantifier393); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_INT.add(min);

                    char_literal38=(Token)match(input,36,FOLLOW_36_in_quantifier395); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_36.add(char_literal38);

                    max=(Token)match(input,INT,FOLLOW_INT_in_quantifier399); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_INT.add(max);

                    char_literal39=(Token)match(input,RBRACE,FOLLOW_RBRACE_in_quantifier401); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBRACE.add(char_literal39);



                    // AST REWRITE
                    // elements: max, min
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
                    // 132:32: -> ^( QUANT $min $max)
                    {
                        // /Users/volker/Desktop/Regex/Regex.g:132:35: ^( QUANT $min $max)
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
                    // /Users/volker/Desktop/Regex/Regex.g:133:4: '{' INT ',' '}'
                    {
                    char_literal40=(Token)match(input,LBRACE,FOLLOW_LBRACE_in_quantifier418); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBRACE.add(char_literal40);

                    INT41=(Token)match(input,INT,FOLLOW_INT_in_quantifier420); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_INT.add(INT41);

                    char_literal42=(Token)match(input,36,FOLLOW_36_in_quantifier422); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_36.add(char_literal42);

                    char_literal43=(Token)match(input,RBRACE,FOLLOW_RBRACE_in_quantifier424); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBRACE.add(char_literal43);



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
                    // 133:20: -> ^( QUANT INT )
                    {
                        // /Users/volker/Desktop/Regex/Regex.g:133:23: ^( QUANT INT )
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
                    // /Users/volker/Desktop/Regex/Regex.g:134:4: '{' INT '}'
                    {
                    char_literal44=(Token)match(input,LBRACE,FOLLOW_LBRACE_in_quantifier437); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBRACE.add(char_literal44);

                    INT45=(Token)match(input,INT,FOLLOW_INT_in_quantifier439); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_INT.add(INT45);

                    char_literal46=(Token)match(input,RBRACE,FOLLOW_RBRACE_in_quantifier441); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBRACE.add(char_literal46);



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
                    // 134:16: -> ^( QUANT INT INT )
                    {
                        // /Users/volker/Desktop/Regex/Regex.g:134:19: ^( QUANT INT INT )
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
            if ( state.backtracking>0 ) { memoize(input, 13, quantifier_StartIndex); }
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
        int cnt15=0;
        loop15:
        do {
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==32) ) {
                alt15=1;
            }


            switch (alt15) {
        	case 1 :
        	    // /Users/volker/Desktop/Regex/Regex.g:81:14: '|' sequence
        	    {
        	    match(input,32,FOLLOW_32_in_synpred4_Regex94); if (state.failed) return ;
        	    pushFollow(FOLLOW_sequence_in_synpred4_Regex96);
        	    sequence();

        	    state._fsp--;
        	    if (state.failed) return ;

        	    }
        	    break;

        	default :
        	    if ( cnt15 >= 1 ) break loop15;
        	    if (state.backtracking>0) {state.failed=true; return ;}
                    EarlyExitException eee =
                        new EarlyExitException(15, input);
                    throw eee;
            }
            cnt15++;
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
        int cnt16=0;
        loop16:
        do {
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( ((LA16_0>=PREDEFINEDCLASS && LA16_0<=CODEDCHAR)||LA16_0==LBRACKET||(LA16_0>=33 && LA16_0<=34)) ) {
                alt16=1;
            }


            switch (alt16) {
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
        // /Users/volker/Desktop/Regex/Regex.g:109:8: ( includedelements )
        // /Users/volker/Desktop/Regex/Regex.g:109:8: includedelements
        {
        pushFollow(FOLLOW_includedelements_in_synpred18_Regex262);
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


 

    public static final BitSet FOLLOW_30_in_expression74 = new BitSet(new long[]{0x0000000610000FF0L});
    public static final BitSet FOLLOW_choice_in_expression78 = new BitSet(new long[]{0x0000000080000002L});
    public static final BitSet FOLLOW_31_in_expression80 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sequence_in_choice91 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_32_in_choice94 = new BitSet(new long[]{0x0000000610000FF0L});
    public static final BitSet FOLLOW_sequence_in_choice96 = new BitSet(new long[]{0x0000000100000002L});
    public static final BitSet FOLLOW_sequence_in_choice112 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_factor_in_sequence121 = new BitSet(new long[]{0x0000000610000FF0L});
    public static final BitSet FOLLOW_factor_in_sequence123 = new BitSet(new long[]{0x0000000610000FF2L});
    public static final BitSet FOLLOW_factor_in_sequence138 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_atom_in_factor146 = new BitSet(new long[]{0x0000000004001000L});
    public static final BitSet FOLLOW_quantifier_in_factor148 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_atom_in_factor163 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classchar_in_atom171 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_33_in_atom177 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_charclass_in_atom183 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PREDEFINEDCLASS_in_atom188 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_group_in_atom194 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_classchar0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_charclass260 = new BitSet(new long[]{0x0000000060000FF0L});
    public static final BitSet FOLLOW_includedelements_in_charclass262 = new BitSet(new long[]{0x0000000060000000L});
    public static final BitSet FOLLOW_30_in_charclass266 = new BitSet(new long[]{0x0000000000000FF0L});
    public static final BitSet FOLLOW_excludedelements_in_charclass268 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_RBRACKET_in_charclass272 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classelement_in_includedelements303 = new BitSet(new long[]{0x0000000000000FF2L});
    public static final BitSet FOLLOW_classelement_in_excludedelements316 = new BitSet(new long[]{0x0000000000000FF2L});
    public static final BitSet FOLLOW_classchar_in_classelement329 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_charrange_in_classelement334 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PREDEFINEDCLASS_in_classelement339 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classchar_in_charrange348 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_33_in_charrange350 = new BitSet(new long[]{0x0000000000000FE0L});
    public static final BitSet FOLLOW_classchar_in_charrange352 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_34_in_group369 = new BitSet(new long[]{0x0000000650000FF0L});
    public static final BitSet FOLLOW_expression_in_group372 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_35_in_group374 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SIMPLEQUANTIFIER_in_quantifier384 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_quantifier389 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_INT_in_quantifier393 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_36_in_quantifier395 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_INT_in_quantifier399 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_RBRACE_in_quantifier401 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_quantifier418 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_INT_in_quantifier420 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_36_in_quantifier422 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_RBRACE_in_quantifier424 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_quantifier437 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_INT_in_quantifier439 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_RBRACE_in_quantifier441 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sequence_in_synpred4_Regex91 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_32_in_synpred4_Regex94 = new BitSet(new long[]{0x0000000610000FF0L});
    public static final BitSet FOLLOW_sequence_in_synpred4_Regex96 = new BitSet(new long[]{0x0000000100000002L});
    public static final BitSet FOLLOW_factor_in_synpred6_Regex121 = new BitSet(new long[]{0x0000000610000FF0L});
    public static final BitSet FOLLOW_factor_in_synpred6_Regex123 = new BitSet(new long[]{0x0000000610000FF2L});
    public static final BitSet FOLLOW_atom_in_synpred7_Regex146 = new BitSet(new long[]{0x0000000004001000L});
    public static final BitSet FOLLOW_quantifier_in_synpred7_Regex148 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_includedelements_in_synpred18_Regex262 = new BitSet(new long[]{0x0000000000000002L});

}