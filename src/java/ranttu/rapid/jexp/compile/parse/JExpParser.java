package ranttu.rapid.jexp.compile.parse;

import lombok.experimental.var;
import ranttu.rapid.jexp.common.AstUtil;
import ranttu.rapid.jexp.compile.jflex.Lexer;
import ranttu.rapid.jexp.compile.parse.ast.ArrayExpression;
import ranttu.rapid.jexp.compile.parse.ast.AstType;
import ranttu.rapid.jexp.compile.parse.ast.BinaryExpression;
import ranttu.rapid.jexp.compile.parse.ast.CallExpression;
import ranttu.rapid.jexp.compile.parse.ast.CommaExpression;
import ranttu.rapid.jexp.compile.parse.ast.ExpressionNode;
import ranttu.rapid.jexp.compile.parse.ast.LambdaExpression;
import ranttu.rapid.jexp.compile.parse.ast.MemberExpression;
import ranttu.rapid.jexp.compile.parse.ast.PrimaryExpression;
import ranttu.rapid.jexp.exception.JExpCompilingException;
import ranttu.rapid.jexp.exception.UnexpectedEOF;
import ranttu.rapid.jexp.exception.UnexpectedToken;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * the parser
 * <p>
 * EXP |=
 * BINARY_EXP
 * UNARY_EXP
 * MEMBER_EXP
 * FUNCTION_EXP
 * PRIMARY_EXP
 * LAMBDA_EXP
 * COMMA_EXP
 * <p>
 * COMMA_EXP |=
 * EXP
 * EXP,COMMA_EXP
 * <p>
 * BINARY_EXP |=
 * UNARY_EXP + UNARY_EXP
 * UNARY_EXP - UNARY_EXP
 * UNARY_EXP * UNARY_EXP
 * UNARY_EXP / UNARY_EXP
 * UNARY_EXP % UNARY_EXP
 * <p>
 * UNARY_EXP |=
 * MEMBER_EXP
 * FUNCTION_EXP
 * PRIMARY_EXP
 * LAMBDA_EXP
 * (EXP)
 * <p>
 * MEMBER_EXP |=
 * UNARY_EXP.IDENTIFIER
 * UNARY_EXP[EXP]
 * <p>
 * FUNCTION_EXP |=
 * UNARY_EXP(ARGUMENT_LIST)
 * <p>
 * LAMBDA_EXP |=
 * (IDENTIFIER_LIST)=>EXP
 * (IDENTIFIER_LIST)=>{EXP}
 * <p>
 * IDENTIFIER_LIST |=
 * NIL
 * IDENTIFIER_LIST, IDENTIFIER
 * <p>
 * ARGUMENT_LIST |=
 * NIL
 * ARGUMENT_LIST, EXP
 * <p>
 * PRIMARY_EXP |=
 * INTEGER
 * FLOAT
 * STRING
 * IDENTIFIER
 * ARRAY_EXP
 * <p>
 * ARRAY_EXP |=
 * NIL
 * ARRAY_EXP, EXP
 *
 * @author rapidhere@gmail.com
 * @version $Id: JExpParser.java, v0.1 2017-07-28 2:58 PM dongwei.dq Exp $
 */
public class JExpParser {
    /**
     * parse the input and return a ast
     *
     * @param input the input text
     * @return ast
     */
    public static ExpressionNode parse(String input) throws JExpCompilingException {
        return new JExpParser(new StringReader(input)).parse();
    }

    // ~~ impl
    // the lexer
    private Lexer lexer;

    private Stack<Token> tokenStack = new Stack<>();

    private JExpParser(Reader input) {
        this.lexer = new Lexer(input);
    }

    private ExpressionNode parse() {
        ExpressionNode ret = parseExp();

        Token t = nextOrNull();
        if (t != null) {
            throw new UnexpectedToken(t);
        }

        return ret;
    }

    private ExpressionNode parseExp() {
        var res = new ArrayList<ExpressionNode>();

        do {
            res.add(parseBinary());
            var t = peekOrNull();

            if (t == null || !t.is(TokenType.COMMA)) {
                break;
            }
            next(TokenType.COMMA);
        } while (true);

        if (res.size() == 1) {
            return res.get(0);
        } else {
            return new CommaExpression(res);
        }
    }

    private ExpressionNode parseBinary() {
        Stack<Token> ops = new Stack<>();
        Stack<ExpressionNode> exps = new Stack<>();
        exps.push(parseUnary());

        while (true) {
            Token t = peekOrNull();
            // not a math oper, break
            if (t == null || !t.type.binaryOp) {
                break;
            }
            // eat the token
            next();

            reduceStack(ops, exps, t);
            ops.push(t);
            exps.push(parseUnary());
        }

        reduceStack(ops, exps, Token.FAKE_TOKEN);
        return exps.pop();
    }

    private void reduceStack(Stack<Token> ops, Stack<ExpressionNode> exps, Token curOp) {
        while (exps.size() > 1) {
            if (curOp.type.priority <= ops.peek().type.priority) {
                ExpressionNode right = exps.pop(), left = exps.pop();
                exps.push(new BinaryExpression(ops.pop(), left, right));
            } else {
                break;
            }
        }
    }

    /**
     * unary-class expressions are made with those basic elements:
     * <p>
     * PRIMARY_EXP
     * (EXP)
     * `.`
     * `[...]`
     * `(...)`
     */
    private ExpressionNode parseUnary() {
        var exp = parseUnaryClassElement();
        while (true) {
            var t = peekOrNull();

            // EOF, end of parse
            if (t == null) {
                break;
            }

            // static member expression
            if (t.is(TokenType.DOT)) {
                next();
                var token = peek();
                var identifier = parsePrimary();
                if (!AstUtil.isIdentifier(identifier)) {
                    throw new UnexpectedToken(token);
                }

                // cast id to str
                Token idToken = ((PrimaryExpression) identifier).token;
                Token strToken = new Token(TokenType.STRING, idToken.line, idToken.column,
                    idToken.value);

                exp = new MemberExpression(exp, new PrimaryExpression(strToken));
            }
            // dynamic member expression
            else if (t.is(TokenType.LEFT_BRACKET)) {
                next();
                exp = new MemberExpression(exp, parseExp());
                next(TokenType.RIGHT_BRACKET);
            }
            // function expression
            else if (t.is(TokenType.LEFT_PARENTHESIS)) {
                next();
                exp = new CallExpression(exp, parseItems(TokenType.RIGHT_PARENTHESIS));
            }
            // lambda expression
            else if (t.is(TokenType.POINTER)) {
                next();
                List<String> pars;
                if (exp.is(AstType.PRIMARY_EXP)) {
                    pars = new ArrayList<>();
                    if (!AstUtil.isIdentifier(exp)) {
                        throw new UnexpectedToken(t);
                    }
                    pars.add(AstUtil.asId(exp));
                } else if (exp.is(AstType.COMMA_EXP)) {
                    //noinspection ConstantConditions
                    pars = ((CommaExpression) exp).expressions.stream()
                        .map(AstUtil::asId).collect(Collectors.toList());
                } else {
                    throw new UnexpectedToken(t);
                }

                t = peek();
                if (t.is(TokenType.LEFT_BRACE)) {
                    next();
                    var items = parseItems(TokenType.RIGHT_BRACE);
                    exp = new LambdaExpression(pars, new CommaExpression(items));
                } else {
                    exp = new LambdaExpression(pars, parseExp());
                }
            }
            // or, parse is end
            else {
                break;
            }
        }

        return exp;
    }

    /**
     * the element of unary-class expressions, can be:
     * <p>
     * (EXP)
     * PRIMARY_EXP
     */
    private ExpressionNode parseUnaryClassElement() {
        var t = peek();

        if (t.is(TokenType.LEFT_PARENTHESIS)) {
            // eat up `(`
            next();
            List<ExpressionNode> exps = parseItems(TokenType.RIGHT_PARENTHESIS);

            if (exps.size() == 1) {
                return exps.get(0);
            } else {
                return new CommaExpression(exps);
            }
        } else {
            return parsePrimary();
        }
    }

    private List<ExpressionNode> parseItems(TokenType endTokenType) {
        var t = peek();
        // meet end, break
        if (t.is(endTokenType)) {
            next();
            return new ArrayList<>();
        }

        // parse as a comma expression
        ExpressionNode exp = parseExp();
        next(endTokenType);
        if (exp.is(AstType.COMMA_EXP)) {
            return ((CommaExpression) exp).expressions;
        } else {
            List<ExpressionNode> pars = new ArrayList<>();
            pars.add(exp);
            return pars;
        }
    }

    private ExpressionNode parsePrimary() {
        var t = peek();

        // i.e., an array expression
        if (t.is(TokenType.LEFT_BRACKET)) {
            next();
            return new ArrayExpression(parseItems(TokenType.RIGHT_BRACKET));
        }
        // common primary expression
        else {
            t = next(TokenType.INTEGER, TokenType.STRING, TokenType.IDENTIFIER, TokenType.FLOAT);
            return new PrimaryExpression(t);
        }
    }

    private Token peek() {
        try {
            var t = _peek();
            if (t == null) {
                throw new UnexpectedEOF();
            }
            return t;

        } catch (IOException e) {
            throw new JExpCompilingException(e.getMessage(), e);
        }
    }

    private Token peekOrNull() {
        try {
            return _peek();

        } catch (IOException e) {
            throw new JExpCompilingException(e.getMessage(), e);
        }
    }

    private Token nextOrNull(TokenType... types) {
        try {
            var t = _next();
            if (t == null) {
                return null;
            }

            if (types.length != 0) {
                for (var type : types) {
                    if (t.is(type)) {
                        return t;
                    }
                }
                throw new UnexpectedToken(t);
            } else {
                return t;
            }
        } catch (Exception e) {
            throw new JExpCompilingException(e.getMessage(), e);
        }
    }

    private Token next(TokenType... types) {
        var t = nextOrNull(types);
        if (t == null) {
            throw new UnexpectedEOF();
        }
        return t;
    }

    private Token _peek() throws IOException {
        if (tokenStack.isEmpty()) {
            tokenStack.push(lexer.yylex());
        }

        return tokenStack.peek();
    }

    private Token _next() throws IOException {
        if (tokenStack.isEmpty()) {
            tokenStack.push(lexer.yylex());
        }

        return tokenStack.pop();
    }
}
