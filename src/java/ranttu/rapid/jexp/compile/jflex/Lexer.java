/* The following code was generated by JFlex 1.6.1 */

package ranttu.rapid.jexp.compile.jflex;

import ranttu.rapid.jexp.compile.parse.Token;
import ranttu.rapid.jexp.compile.parse.TokenType;

/**
 * ### WARNING ###
 * this file is generated by src/jflex/jexp.flex, don't modify this directly
 *
 * the lexer generated by jflex
 * @author rapidhere@gmail.com
 */
@SuppressWarnings("all")

public class Lexer {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;
  public static final int STRING = 2;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0,  0,  1, 1
  };

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\4\1\2\1\1\1\0\1\2\1\1\16\4\4\0\1\2\1\0"+
    "\1\10\1\0\1\3\1\15\2\0\1\16\1\17\1\13\1\11\1\20"+
    "\1\12\1\0\1\14\1\5\11\6\7\0\27\3\1\7\2\3\1\0"+
    "\1\21\2\0\1\3\1\0\23\3\1\22\3\3\1\7\2\3\4\0"+
    "\41\4\2\0\4\3\4\0\1\3\2\0\1\4\7\0\1\3\4\0"+
    "\1\3\5\0\27\3\1\0\37\3\1\0\u01ca\3\4\0\14\3\16\0"+
    "\5\3\7\0\1\3\1\0\1\3\21\0\160\4\5\3\1\0\2\3"+
    "\2\0\4\3\10\0\1\3\1\0\3\3\1\0\1\3\1\0\24\3"+
    "\1\0\123\3\1\0\213\3\1\0\5\4\2\0\236\3\11\0\46\3"+
    "\2\0\1\3\7\0\47\3\7\0\1\3\1\0\55\4\1\0\1\4"+
    "\1\0\2\4\1\0\2\4\1\0\1\4\10\0\33\3\5\0\3\3"+
    "\15\0\5\4\6\0\1\3\4\0\13\4\5\0\53\3\37\4\4\0"+
    "\2\3\1\4\143\3\1\0\1\3\10\4\1\0\6\4\2\3\2\4"+
    "\1\0\4\4\2\3\12\4\3\3\2\0\1\3\17\0\1\4\1\3"+
    "\1\4\36\3\33\4\2\0\131\3\13\4\1\3\16\0\12\4\41\3"+
    "\11\4\2\3\4\0\1\3\5\0\26\3\4\4\1\3\11\4\1\3"+
    "\3\4\1\3\5\4\22\0\31\3\3\4\104\0\1\3\1\0\13\3"+
    "\67\0\33\4\1\0\4\4\66\3\3\4\1\3\22\4\1\3\7\4"+
    "\12\3\2\4\2\0\12\4\1\0\7\3\1\0\7\3\1\0\3\4"+
    "\1\0\10\3\2\0\2\3\2\0\26\3\1\0\7\3\1\0\1\3"+
    "\3\0\4\3\2\0\1\4\1\3\7\4\2\0\2\4\2\0\3\4"+
    "\1\3\10\0\1\4\4\0\2\3\1\0\3\3\2\4\2\0\12\4"+
    "\4\3\7\0\1\3\5\0\3\4\1\0\6\3\4\0\2\3\2\0"+
    "\26\3\1\0\7\3\1\0\2\3\1\0\2\3\1\0\2\3\2\0"+
    "\1\4\1\0\5\4\4\0\2\4\2\0\3\4\3\0\1\4\7\0"+
    "\4\3\1\0\1\3\7\0\14\4\3\3\1\4\13\0\3\4\1\0"+
    "\11\3\1\0\3\3\1\0\26\3\1\0\7\3\1\0\2\3\1\0"+
    "\5\3\2\0\1\4\1\3\10\4\1\0\3\4\1\0\3\4\2\0"+
    "\1\3\17\0\2\3\2\4\2\0\12\4\1\0\1\3\17\0\3\4"+
    "\1\0\10\3\2\0\2\3\2\0\26\3\1\0\7\3\1\0\2\3"+
    "\1\0\5\3\2\0\1\4\1\3\7\4\2\0\2\4\2\0\3\4"+
    "\10\0\2\4\4\0\2\3\1\0\3\3\2\4\2\0\12\4\1\0"+
    "\1\3\20\0\1\4\1\3\1\0\6\3\3\0\3\3\1\0\4\3"+
    "\3\0\2\3\1\0\1\3\1\0\2\3\3\0\2\3\3\0\3\3"+
    "\3\0\14\3\4\0\5\4\3\0\3\4\1\0\4\4\2\0\1\3"+
    "\6\0\1\4\16\0\12\4\11\0\1\3\7\0\3\4\1\0\10\3"+
    "\1\0\3\3\1\0\27\3\1\0\12\3\1\0\5\3\3\0\1\3"+
    "\7\4\1\0\3\4\1\0\4\4\7\0\2\4\1\0\2\3\6\0"+
    "\2\3\2\4\2\0\12\4\22\0\2\4\1\0\10\3\1\0\3\3"+
    "\1\0\27\3\1\0\12\3\1\0\5\3\2\0\1\4\1\3\7\4"+
    "\1\0\3\4\1\0\4\4\7\0\2\4\7\0\1\3\1\0\2\3"+
    "\2\4\2\0\12\4\1\0\2\3\17\0\2\4\1\0\10\3\1\0"+
    "\3\3\1\0\51\3\2\0\1\3\7\4\1\0\3\4\1\0\4\4"+
    "\1\3\10\0\1\4\10\0\2\3\2\4\2\0\12\4\12\0\6\3"+
    "\2\0\2\4\1\0\22\3\3\0\30\3\1\0\11\3\1\0\1\3"+
    "\2\0\7\3\3\0\1\4\4\0\6\4\1\0\1\4\1\0\10\4"+
    "\22\0\2\4\15\0\60\3\1\4\2\3\7\4\4\0\10\3\10\4"+
    "\1\0\12\4\47\0\2\3\1\0\1\3\2\0\2\3\1\0\1\3"+
    "\2\0\1\3\6\0\4\3\1\0\7\3\1\0\3\3\1\0\1\3"+
    "\1\0\1\3\2\0\2\3\1\0\4\3\1\4\2\3\6\4\1\0"+
    "\2\4\1\3\2\0\5\3\1\0\1\3\1\0\6\4\2\0\12\4"+
    "\2\0\4\3\40\0\1\3\27\0\2\4\6\0\12\4\13\0\1\4"+
    "\1\0\1\4\1\0\1\4\4\0\2\4\10\3\1\0\44\3\4\0"+
    "\24\4\1\0\2\4\5\3\13\4\1\0\44\4\11\0\1\4\71\0"+
    "\53\3\24\4\1\3\12\4\6\0\6\3\4\4\4\3\3\4\1\3"+
    "\3\4\2\3\7\4\3\3\4\4\15\3\14\4\1\3\17\4\2\0"+
    "\46\3\1\0\1\3\5\0\1\3\2\0\53\3\1\0\u014d\3\1\0"+
    "\4\3\2\0\7\3\1\0\1\3\1\0\4\3\2\0\51\3\1\0"+
    "\4\3\2\0\41\3\1\0\4\3\2\0\7\3\1\0\1\3\1\0"+
    "\4\3\2\0\17\3\1\0\71\3\1\0\4\3\2\0\103\3\2\0"+
    "\3\4\40\0\20\3\20\0\125\3\14\0\u026c\3\2\0\21\3\1\0"+
    "\32\3\5\0\113\3\3\0\3\3\17\0\15\3\1\0\4\3\3\4"+
    "\13\0\22\3\3\4\13\0\22\3\2\4\14\0\15\3\1\0\3\3"+
    "\1\0\2\4\14\0\64\3\40\4\3\0\1\3\3\0\2\3\1\4"+
    "\2\0\12\4\41\0\3\4\2\0\12\4\6\0\130\3\10\0\51\3"+
    "\1\4\1\3\5\0\106\3\12\0\35\3\3\0\14\4\4\0\14\4"+
    "\12\0\12\4\36\3\2\0\5\3\13\0\54\3\4\0\21\4\7\3"+
    "\2\4\6\0\12\4\46\0\27\3\5\4\4\0\65\3\12\4\1\0"+
    "\35\4\2\0\13\4\6\0\12\4\15\0\1\3\130\0\5\4\57\3"+
    "\21\4\7\3\4\0\12\4\21\0\11\4\14\0\3\4\36\3\15\4"+
    "\2\3\12\4\54\3\16\4\14\0\44\3\24\4\10\0\12\4\3\0"+
    "\3\3\12\4\44\3\122\0\3\4\1\0\25\4\4\3\1\4\4\3"+
    "\3\4\2\3\11\0\300\3\47\4\25\0\4\4\u0116\3\2\0\6\3"+
    "\2\0\46\3\2\0\6\3\2\0\10\3\1\0\1\3\1\0\1\3"+
    "\1\0\1\3\1\0\37\3\2\0\65\3\1\0\7\3\1\0\1\3"+
    "\3\0\3\3\1\0\7\3\3\0\4\3\2\0\6\3\4\0\15\3"+
    "\5\0\3\3\1\0\7\3\16\0\5\4\32\0\5\4\20\0\2\3"+
    "\23\0\1\3\13\0\5\4\5\0\6\4\1\0\1\3\15\0\1\3"+
    "\20\0\15\3\3\0\33\3\25\0\15\4\4\0\1\4\3\0\14\4"+
    "\21\0\1\3\4\0\1\3\2\0\12\3\1\0\1\3\3\0\5\3"+
    "\6\0\1\3\1\0\1\3\1\0\1\3\1\0\4\3\1\0\13\3"+
    "\2\0\4\3\5\0\5\3\4\0\1\3\21\0\51\3\u0a77\0\57\3"+
    "\1\0\57\3\1\0\205\3\6\0\4\3\3\4\2\3\14\0\46\3"+
    "\1\0\1\3\5\0\1\3\2\0\70\3\7\0\1\3\17\0\1\4"+
    "\27\3\11\0\7\3\1\0\7\3\1\0\7\3\1\0\7\3\1\0"+
    "\7\3\1\0\7\3\1\0\7\3\1\0\7\3\1\0\40\4\57\0"+
    "\1\3\u01d5\0\3\3\31\0\11\3\6\4\1\0\5\3\2\0\5\3"+
    "\4\0\126\3\2\0\2\4\2\0\3\3\1\0\132\3\1\0\4\3"+
    "\5\0\51\3\3\0\136\3\21\0\33\3\65\0\20\3\u0200\0\u19b6\3"+
    "\112\0\u51cd\3\63\0\u048d\3\103\0\56\3\2\0\u010d\3\3\0\20\3"+
    "\12\4\2\3\24\0\57\3\1\4\4\0\12\4\1\0\31\3\7\0"+
    "\1\4\120\3\2\4\45\0\11\3\2\0\147\3\2\0\4\3\1\0"+
    "\4\3\14\0\13\3\115\0\12\3\1\4\3\3\1\4\4\3\1\4"+
    "\27\3\5\4\20\0\1\3\7\0\64\3\14\0\2\4\62\3\21\4"+
    "\13\0\12\4\6\0\22\4\6\3\3\0\1\3\4\0\12\4\34\3"+
    "\10\4\2\0\27\3\15\4\14\0\35\3\3\0\4\4\57\3\16\4"+
    "\16\0\1\3\12\4\46\0\51\3\16\4\11\0\3\3\1\4\10\3"+
    "\2\4\2\0\12\4\6\0\27\3\3\0\1\3\1\4\4\0\60\3"+
    "\1\4\1\3\3\4\2\3\2\4\5\3\2\4\1\3\1\4\1\3"+
    "\30\0\3\3\2\0\13\3\5\4\2\0\3\3\2\4\12\0\6\3"+
    "\2\0\6\3\2\0\6\3\11\0\7\3\1\0\7\3\221\0\43\3"+
    "\10\4\1\0\2\4\2\0\12\4\6\0\u2ba4\3\14\0\27\3\4\0"+
    "\61\3\u2104\0\u016e\3\2\0\152\3\46\0\7\3\14\0\5\3\5\0"+
    "\1\3\1\4\12\3\1\0\15\3\1\0\5\3\1\0\1\3\1\0"+
    "\2\3\1\0\2\3\1\0\154\3\41\0\u016b\3\22\0\100\3\2\0"+
    "\66\3\50\0\15\3\3\0\20\4\20\0\7\4\14\0\2\3\30\0"+
    "\3\3\31\0\1\3\6\0\5\3\1\0\207\3\2\0\1\4\4\0"+
    "\1\3\13\0\12\4\7\0\32\3\4\0\1\3\1\0\32\3\13\0"+
    "\131\3\3\0\6\3\2\0\6\3\2\0\6\3\2\0\3\3\3\0"+
    "\2\3\3\0\2\3\22\0\3\4\4\0\14\3\1\0\32\3\1\0"+
    "\23\3\1\0\2\3\1\0\17\3\2\0\16\3\42\0\173\3\105\0"+
    "\65\3\210\0\1\4\202\0\35\3\3\0\61\3\57\0\37\3\21\0"+
    "\33\3\65\0\36\3\2\0\44\3\4\0\10\3\1\0\5\3\52\0"+
    "\236\3\2\0\12\4\u0356\0\6\3\2\0\1\3\1\0\54\3\1\0"+
    "\2\3\3\0\1\3\2\0\27\3\252\0\26\3\12\0\32\3\106\0"+
    "\70\3\6\0\2\3\100\0\1\3\3\4\1\0\2\4\5\0\4\4"+
    "\4\3\1\0\3\3\1\0\33\3\4\0\3\4\4\0\1\4\40\0"+
    "\35\3\203\0\66\3\12\0\26\3\12\0\23\3\215\0\111\3\u03b7\0"+
    "\3\4\65\3\17\4\37\0\12\4\20\0\3\4\55\3\13\4\2\0"+
    "\1\4\22\0\31\3\7\0\12\4\6\0\3\4\44\3\16\4\1\0"+
    "\12\4\100\0\3\4\60\3\16\4\4\3\13\0\12\4\u04a6\0\53\3"+
    "\15\4\10\0\12\4\u0936\0\u036f\3\221\0\143\3\u0b9d\0\u042f\3\u33d1\0"+
    "\u0239\3\u04c7\0\105\3\13\0\1\3\56\4\20\0\4\4\15\3\u4060\0"+
    "\2\3\u2163\0\5\4\3\0\26\4\2\0\7\4\36\0\4\4\224\0"+
    "\3\4\u01bb\0\125\3\1\0\107\3\1\0\2\3\2\0\1\3\2\0"+
    "\2\3\2\0\4\3\1\0\14\3\1\0\1\3\1\0\7\3\1\0"+
    "\101\3\1\0\4\3\2\0\10\3\1\0\7\3\1\0\34\3\1\0"+
    "\4\3\1\0\5\3\1\0\1\3\3\0\7\3\1\0\u0154\3\2\0"+
    "\31\3\1\0\31\3\1\0\37\3\1\0\31\3\1\0\37\3\1\0"+
    "\31\3\1\0\37\3\1\0\31\3\1\0\37\3\1\0\31\3\1\0"+
    "\10\3\2\0\62\4\u1600\0\4\3\1\0\33\3\1\0\2\3\1\0"+
    "\1\3\2\0\1\3\1\0\12\3\1\0\4\3\1\0\1\3\1\0"+
    "\1\3\6\0\1\3\4\0\1\3\1\0\1\3\1\0\1\3\1\0"+
    "\3\3\1\0\2\3\1\0\1\3\2\0\1\3\1\0\1\3\1\0"+
    "\1\3\1\0\1\3\1\0\1\3\1\0\2\3\1\0\1\3\2\0"+
    "\4\3\1\0\7\3\1\0\4\3\1\0\4\3\1\0\1\3\1\0"+
    "\12\3\1\0\21\3\5\0\3\3\1\0\5\3\1\0\21\3\u1144\0"+
    "\ua6d7\3\51\0\u1035\3\13\0\336\3\u3fe2\0\u021e\3\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\u05ee\0"+
    "\1\4\36\0\140\4\200\0\360\4\uffff\0\uffff\0\ufe12\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\2\0\1\1\1\2\1\3\2\4\1\5\1\6\1\7"+
    "\1\10\1\11\1\12\1\13\1\14\1\15\1\16\1\17"+
    "\1\20\1\21\1\0\1\22\1\23\1\24";

  private static int [] zzUnpackAction() {
    int [] result = new int[24];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\23\0\46\0\46\0\71\0\114\0\137\0\46"+
    "\0\46\0\46\0\46\0\46\0\46\0\46\0\46\0\46"+
    "\0\162\0\46\0\205\0\230\0\253\0\46\0\46\0\253";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[24];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\2\3\1\4\1\5\1\3\1\6\1\7\1\5\1\10"+
    "\1\11\1\12\1\13\1\14\1\15\1\16\1\17\1\20"+
    "\1\3\1\5\1\21\1\3\6\21\1\22\10\21\1\23"+
    "\1\21\26\0\5\5\12\0\1\5\5\0\2\24\1\25"+
    "\20\0\2\7\14\0\1\21\1\0\6\21\1\0\10\21"+
    "\1\0\1\21\10\0\1\26\11\0\1\27\5\0\2\24"+
    "\21\0\2\30\14\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[190];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unknown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\2\0\2\11\3\1\11\11\1\1\1\11\2\1\1\0"+
    "\2\11\1\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[24];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn;

  /** 
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;
  
  /** 
   * The number of occupied positions in zzBuffer beyond zzEndRead.
   * When a lead/high surrogate has been read from the input stream
   * into the final zzBuffer position, this will have a value of 1;
   * otherwise, it will have a value of 0.
   */
  private int zzFinalHighSurrogate = 0;

  /* user code: */
  private StringBuffer string = new StringBuffer();

  private Token token(TokenType type, Object val) {
      return new Token(type, yyline, yycolumn, val);
  }


  /**
   * Creates a new scanner
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public Lexer(java.io.Reader in) {
    this.zzReader = in;
  }


  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x110000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 2794) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   * 
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (zzStartRead > 0) {
      zzEndRead += zzFinalHighSurrogate;
      zzFinalHighSurrogate = 0;
      System.arraycopy(zzBuffer, zzStartRead,
                       zzBuffer, 0,
                       zzEndRead-zzStartRead);

      /* translate stored positions */
      zzEndRead-= zzStartRead;
      zzCurrentPos-= zzStartRead;
      zzMarkedPos-= zzStartRead;
      zzStartRead = 0;
    }

    /* is the buffer big enough? */
    if (zzCurrentPos >= zzBuffer.length - zzFinalHighSurrogate) {
      /* if not: blow it up */
      char newBuffer[] = new char[zzBuffer.length*2];
      System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
      zzBuffer = newBuffer;
      zzEndRead += zzFinalHighSurrogate;
      zzFinalHighSurrogate = 0;
    }

    /* fill the buffer with new input */
    int requested = zzBuffer.length - zzEndRead;
    int numRead = zzReader.read(zzBuffer, zzEndRead, requested);

    /* not supposed to occur according to specification of java.io.Reader */
    if (numRead == 0) {
      throw new java.io.IOException("Reader returned 0 characters. See JFlex examples for workaround.");
    }
    if (numRead > 0) {
      zzEndRead += numRead;
      /* If numRead == requested, we might have requested to few chars to
         encode a full Unicode character. We assume that a Reader would
         otherwise never return half characters. */
      if (numRead == requested) {
        if (Character.isHighSurrogate(zzBuffer[zzEndRead - 1])) {
          --zzEndRead;
          zzFinalHighSurrogate = 1;
        }
      }
      /* potentially more input available */
      return false;
    }

    /* numRead < 0 ==> end of stream */
    return true;
  }

    
  /**
   * Closes the input stream.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }


  /**
   * Resets the scanner to read from a new input stream.
   * Does not close the old reader.
   *
   * All internal variables are reset, the old input stream 
   * <b>cannot</b> be reused (internal buffer is discarded and lost).
   * Lexical state is set to <tt>ZZ_INITIAL</tt>.
   *
   * Internal scan buffer is resized down to its initial length, if it has grown.
   *
   * @param reader   the new input stream 
   */
  public final void yyreset(java.io.Reader reader) {
    zzReader = reader;
    zzAtBOL  = true;
    zzAtEOF  = false;
    zzEOFDone = false;
    zzEndRead = zzStartRead = 0;
    zzCurrentPos = zzMarkedPos = 0;
    zzFinalHighSurrogate = 0;
    yyline = yychar = yycolumn = 0;
    zzLexicalState = YYINITIAL;
    if (zzBuffer.length > ZZ_BUFFERSIZE)
      zzBuffer = new char[ZZ_BUFFERSIZE];
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final String yytext() {
    return new String( zzBuffer, zzStartRead, zzMarkedPos-zzStartRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer[zzStartRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public Token yylex() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char [] zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      boolean zzR = false;
      int zzCh;
      int zzCharCount;
      for (zzCurrentPosL = zzStartRead  ;
           zzCurrentPosL < zzMarkedPosL ;
           zzCurrentPosL += zzCharCount ) {
        zzCh = Character.codePointAt(zzBufferL, zzCurrentPosL, zzMarkedPosL);
        zzCharCount = Character.charCount(zzCh);
        switch (zzCh) {
        case '\u000B':
        case '\u000C':
        case '\u0085':
        case '\u2028':
        case '\u2029':
          yyline++;
          yycolumn = 0;
          zzR = false;
          break;
        case '\r':
          yyline++;
          yycolumn = 0;
          zzR = true;
          break;
        case '\n':
          if (zzR)
            zzR = false;
          else {
            yyline++;
            yycolumn = 0;
          }
          break;
        default:
          zzR = false;
          yycolumn += zzCharCount;
        }
      }

      if (zzR) {
        // peek one character ahead if it is \n (if we have counted one line too much)
        boolean zzPeek;
        if (zzMarkedPosL < zzEndReadL)
          zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        else if (zzAtEOF)
          zzPeek = false;
        else {
          boolean eof = zzRefill();
          zzEndReadL = zzEndRead;
          zzMarkedPosL = zzMarkedPos;
          zzBufferL = zzBuffer;
          if (eof) 
            zzPeek = false;
          else 
            zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        }
        if (zzPeek) yyline--;
      }
      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
  
      zzState = ZZ_LEXSTATE[zzLexicalState];

      // set up zzAction for empty match case:
      int zzAttributes = zzAttrL[zzState];
      if ( (zzAttributes & 1) == 1 ) {
        zzAction = zzState;
      }


      zzForAction: {
        while (true) {
    
          if (zzCurrentPosL < zzEndReadL) {
            zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);
            zzCurrentPosL += Character.charCount(zzInput);
          }
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);
              zzCurrentPosL += Character.charCount(zzInput);
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
        zzAtEOF = true;
        return null;
      }
      else {
        switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
          case 1: 
            { throw new Error("unexpected character at line "
                                            + yyline + ", column " + yycolumn + ": " + yytext());
            }
          case 21: break;
          case 2: 
            { /* ignore */
            }
          case 22: break;
          case 3: 
            { return token(TokenType.IDENTIFIER, yytext());
            }
          case 23: break;
          case 4: 
            { return token(TokenType.INTEGER, Integer.valueOf(yytext()));
            }
          case 24: break;
          case 5: 
            { string.setLength(0); yybegin(STRING);
            }
          case 25: break;
          case 6: 
            { return token(TokenType.PLUS, yytext());
            }
          case 26: break;
          case 7: 
            { return token(TokenType.SUBTRACT, yytext());
            }
          case 27: break;
          case 8: 
            { return token(TokenType.MULTIPLY, yytext());
            }
          case 28: break;
          case 9: 
            { return token(TokenType.DIVIDE, yytext());
            }
          case 29: break;
          case 10: 
            { return token(TokenType.MODULAR, yytext());
            }
          case 30: break;
          case 11: 
            { return token(TokenType.LEFT_PARENTHESIS, yytext());
            }
          case 31: break;
          case 12: 
            { return token(TokenType.RIGHT_PARENTHESIS, yytext());
            }
          case 32: break;
          case 13: 
            { return token(TokenType.COMMA, yytext());
            }
          case 33: break;
          case 14: 
            { string.append( yytext() );
            }
          case 34: break;
          case 15: 
            { yybegin(YYINITIAL);
                                   return token(TokenType.STRING, string.toString());
            }
          case 35: break;
          case 16: 
            { string.append('\\');
            }
          case 36: break;
          case 17: 
            { return token(TokenType.INTEGER, Integer.parseInt(yytext(), 8));
            }
          case 37: break;
          case 18: 
            { string.append('\"');
            }
          case 38: break;
          case 19: 
            { string.append('\t');
            }
          case 39: break;
          case 20: 
            { return token(TokenType.INTEGER, Integer.parseInt(yytext().substring(2), 16));
            }
          case 40: break;
          default:
            zzScanError(ZZ_NO_MATCH);
        }
      }
    }
  }


}
