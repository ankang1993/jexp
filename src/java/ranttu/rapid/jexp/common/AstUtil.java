/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package ranttu.rapid.jexp.common;

import lombok.experimental.UtilityClass;
import ranttu.rapid.jexp.compile.parse.TokenType;
import ranttu.rapid.jexp.compile.parse.ast.AstNode;
import ranttu.rapid.jexp.compile.parse.ast.AstType;
import ranttu.rapid.jexp.compile.parse.ast.PrimaryExpression;

/**
 * ast related utilities
 * @author rapid
 * @version $Id: AstUtil.java, v 0.1 2018年02月23日 4:25 PM rapid Exp $
 */
@UtilityClass
public class AstUtil {
    public boolean isIdentifier(AstNode astNode) {
        return astNode.is(AstType.PRIMARY_EXP)
               && ((PrimaryExpression) astNode).token.is(TokenType.IDENTIFIER);
    }

    public String asId(AstNode astNode) {
        $.should(isIdentifier(astNode));

        return ((PrimaryExpression) astNode).token.getString();
    }

    public boolean isExactString(AstNode astNode) {
        return astNode.is(AstType.PRIMARY_EXP)
               && ((PrimaryExpression) astNode).token.is(TokenType.STRING);
    }

    public String asExactString(AstNode astNode) {
        $.should(isExactString(astNode));

        return ((PrimaryExpression) astNode).token.getString();
    }

    public String asConstantString(AstNode astNode) {
        if (astNode.isConstant) {
            return String.valueOf(astNode.constantValue);
        } else {
            return null;
        }
    }
}