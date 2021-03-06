/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package ranttu.rapid.jexp.runtime;

import lombok.experimental.var;
import ranttu.rapid.jexp.JExpExpression;

/**
 * a jexp expression that return constant value
 *
 * @author rapid
 * @version $Id: JExpImmutableExpression.java, v 0.1 2017年10月01日 11:20 AM rapid Exp $
 */
public class JExpImmutableExpression implements JExpExpression {
    private Object val;

    protected JExpImmutableExpression() {
    }

    public static JExpImmutableExpression of(Object val) {
        var exp = new JExpImmutableExpression();
        exp.val = val;

        return exp;
    }

    @Override
    public Object execute(Object context) {
        return val;
    }
}