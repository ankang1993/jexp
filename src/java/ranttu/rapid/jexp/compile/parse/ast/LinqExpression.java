/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2020 All Rights Reserved.
 */
package ranttu.rapid.jexp.compile.parse.ast;

import ranttu.rapid.jexp.compile.closure.NameClosure;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rapid
 * @version : LinqExpression.java, v 0.1 2020-11-09 4:16 PM rapid Exp $
 */
@Type(AstType.LINQ_EXP)
public class LinqExpression extends ExpressionNode {
    /**
     * query body
     */
    public List<LinqQueryBodyClause> queryBodyClauses = new ArrayList<>();

    /**
     * final query
     */
    public LinqFinalQueryClause finalQueryClause;

    /**
     * name of this linq expression
     */
    public NameClosure names;
}