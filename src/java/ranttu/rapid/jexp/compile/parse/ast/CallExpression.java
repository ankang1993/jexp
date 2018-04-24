/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package ranttu.rapid.jexp.compile.parse.ast;

import java.util.List;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ranttu.rapid.jexp.runtime.function.FunctionInfo;

/**
 * @author rapid
 * @version $Id: UnboundedCallExpression.java, v 0.1 2018年04月24日 4:45 PM rapid Exp $
 */
@Type(AstType.CALL_EXP)
@RequiredArgsConstructor
public class CallExpression extends AstNode {
    @NonNull
    public AstNode       caller;

    @NonNull
    public List<AstNode> parameters;

    public boolean             isBounded = false;

    //~~~ unbounded invoke
    public FunctionInfo        functionInfo;

    //~~~ bounded invoke
    public String              methodName;
}