/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2020 All Rights Reserved.
 */
package ranttu.rapid.jexp.compile.parse.ast;

import lombok.RequiredArgsConstructor;

/**
 * @author rapid
 * @version : LinqLetClause.java, v 0.1 2020-11-09 4:20 PM rapid Exp $
 */
@Type(AstType.LINQ_LET_CLAUSE)
@RequiredArgsConstructor
public class LinqLetClause extends LinqQueryBodyClause {
    final public String itemName;

    final public ExpressionNode sourceExp;

    public int linqParameterIndex;

    public LambdaExpression lambdaExp;
}