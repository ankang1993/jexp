/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package ranttu.rapid.jexp.compile.pass;

import ranttu.rapid.jexp.common.$;
import ranttu.rapid.jexp.compile.CompilingContext;
import ranttu.rapid.jexp.compile.closure.NameClosure;
import ranttu.rapid.jexp.compile.parse.ast.ArrayExpression;
import ranttu.rapid.jexp.compile.parse.ast.BinaryExpression;
import ranttu.rapid.jexp.compile.parse.ast.CallExpression;
import ranttu.rapid.jexp.compile.parse.ast.ExpressionNode;
import ranttu.rapid.jexp.compile.parse.ast.LambdaExpression;
import ranttu.rapid.jexp.compile.parse.ast.MemberExpression;
import ranttu.rapid.jexp.compile.parse.ast.PrimaryExpression;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author dongwei.dq
 * @version $Id: NoReturnPass.java, v0.1 2017-08-24 6:08 PM dongwei.dq Exp $
 */
public abstract class NoReturnPass implements Pass {
    protected CompilingContext context;

    protected Deque<NameClosure> nameStack = new ArrayDeque<>();

    @Override
    public void apply(ExpressionNode astNode, CompilingContext context) {
        this.context = context;
        prepare();
        in(context.names, () -> visit(astNode));
    }

    protected void prepare() {
        // default left as blank
    }

    protected void visit(ExpressionNode astNode) {
        switch (astNode.type) {
            case PRIMARY_EXP:
                visit((PrimaryExpression) astNode);
                break;
            case BINARY_EXP:
                visit((BinaryExpression) astNode);
                break;
            case CALL_EXP:
                visit((CallExpression) astNode);
                break;
            case MEMBER_EXP:
                visit((MemberExpression) astNode);
                break;
            case ARRAY_EXP:
                visit((ArrayExpression) astNode);
                break;
            case LAMBDA_EXP:
                visit((LambdaExpression) astNode);
                break;
            default:
                $.notSupport(astNode.type);
        }
    }

    protected NameClosure names() {
        return nameStack.peek();
    }

    protected void in(NameClosure names, Runnable runnable) {
        try {
            nameStack.push(names);
            runnable.run();
        } finally {
            nameStack.pop();
        }
    }

    protected abstract void visit(PrimaryExpression exp);

    protected abstract void visit(BinaryExpression exp);

    protected abstract void visit(CallExpression exp);

    protected abstract void visit(MemberExpression exp);

    protected abstract void visit(ArrayExpression exp);

    protected abstract void visit(LambdaExpression exp);
}
