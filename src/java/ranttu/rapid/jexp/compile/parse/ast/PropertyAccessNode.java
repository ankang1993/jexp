/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package ranttu.rapid.jexp.compile.parse.ast;

import ranttu.rapid.jexp.compile.PropertyTree;

/**
 * a node that can fetch a property
 *
 * @author rapid
 * @version $Id: PropertyAccessNode.java, v 0.1 2018年02月24日 5:56 PM rapid Exp $
 */
public abstract class PropertyAccessNode extends ExpressionNode {
    /**
     * access node on the access tree of this ast node
     */
    public PropertyTree.PropertyNode propertyNode;

    /**
     * if this is a static access?
     */
    public boolean isStatic = false;
}