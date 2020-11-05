/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2020 All Rights Reserved.
 */
package ranttu.rapid.jexp.compile.closure;

import lombok.experimental.var;
import ranttu.rapid.jexp.exception.DuplicatedName;
import ranttu.rapid.jexp.runtime.indy.JExpIndyFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * the name closure is a package contains all names under a scope
 *
 * @author rapid
 * @version : NameClosure.java, v 0.1 2020-11-05 9:02 AM rapid Exp $
 */
public class NameClosure {
    /**
     * the parent closure
     */
    final public NameClosure parent;

    /**
     * the property tree root under this closure
     * a property tree is a tree of identifier
     * for example, we have such id lists:
     * - a.b.c.d.e
     * - a.b.f
     * - a.g
     * <p>
     * then we have the identifier tree:
     * a - b - c - d - e
     * | |
     * | -- b - f
     * ---- g
     */
    final private PropertyNode rootNode = new PropertyNode(this);

    /**
     * root properties under this closure
     */
    final public Map<String, PropertyNode> properties = new HashMap<>();

    public NameClosure(NameClosure parent) {
        this.parent = parent;
    }

    /**
     * declare a name under this scope
     */
    public void declareName(String id) {
        if (properties.containsKey(id)) {
            throw new DuplicatedName(id);
        }

        properties.put(id, newNode(id));
    }

    /**
     * access a name under this closure
     */
    public PropertyNode addNameAccess(String id) {
        // if can find name in current closure
        // then access the name under this closure
        if (properties.containsKey(id)) {
            return properties.get(id);
        }
        // for root closure, declare the name, add access to propertyTree
        else if (parent == null) {
            var node = addNameAccess(rootNode, id);
            properties.put(id, node);
            return node;
        }
        // otherwise, name is access via parent closure
        else {
            return parent.addNameAccess(id);
        }
    }

    /**
     * add a name access to a owner
     */
    public PropertyNode addNameAccess(PropertyNode owner, String id) {
        return owner.children.computeIfAbsent(id, this::newNode);
    }

    /**
     * travel through the property tree, DFS
     */
    public void visitTree(Consumer<PropertyNode> tv) {
        rootNode.visit(tv);
    }

    //~~~ impl
    private PropertyNode newNode(String id) {
        var newNode = new PropertyNode(this);
        newNode.identifier = id;
        newNode.isRoot = false;
        newNode.slotNo = JExpIndyFactory.nextSlotNo();

        return newNode;
    }
}