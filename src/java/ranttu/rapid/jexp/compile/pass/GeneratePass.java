/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package ranttu.rapid.jexp.compile.pass;

import lombok.experimental.var;
import ranttu.rapid.jexp.common.$;
import ranttu.rapid.jexp.common.Types;
import ranttu.rapid.jexp.compile.CompileOption;
import ranttu.rapid.jexp.compile.CompilingContext;
import ranttu.rapid.jexp.compile.JExpByteCodeTransformer;
import ranttu.rapid.jexp.compile.JExpExpression;
import ranttu.rapid.jexp.compile.JExpImmutableExpression;
import ranttu.rapid.jexp.compile.PropertyTree;
import ranttu.rapid.jexp.compile.parse.TokenType;
import ranttu.rapid.jexp.compile.parse.ast.ArrayExpression;
import ranttu.rapid.jexp.compile.parse.ast.BinaryExpression;
import ranttu.rapid.jexp.compile.parse.ast.CallExpression;
import ranttu.rapid.jexp.compile.parse.ast.ExpressionNode;
import ranttu.rapid.jexp.compile.parse.ast.MemberExpression;
import ranttu.rapid.jexp.compile.parse.ast.PrimaryExpression;
import ranttu.rapid.jexp.compile.parse.ast.PropertyAccessNode;
import ranttu.rapid.jexp.exception.JExpCompilingException;
import ranttu.rapid.jexp.external.org.objectweb.asm.ClassWriter;
import ranttu.rapid.jexp.external.org.objectweb.asm.Label;
import ranttu.rapid.jexp.external.org.objectweb.asm.MethodVisitor;
import ranttu.rapid.jexp.external.org.objectweb.asm.Opcodes;
import ranttu.rapid.jexp.external.org.objectweb.asm.Type;
import ranttu.rapid.jexp.runtime.JExpClassLoader;
import ranttu.rapid.jexp.runtime.Runtimes;
import ranttu.rapid.jexp.runtime.accesor.Accessor;
import ranttu.rapid.jexp.runtime.accesor.AccessorFactory;
import ranttu.rapid.jexp.runtime.accesor.DummyAccessor;
import ranttu.rapid.jexp.runtime.function.FunctionInfo;
import ranttu.rapid.jexp.runtime.function.JExpFunctionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ranttu.rapid.jexp.external.org.objectweb.asm.Type.getDescriptor;
import static ranttu.rapid.jexp.external.org.objectweb.asm.Type.getInternalName;
import static ranttu.rapid.jexp.external.org.objectweb.asm.Type.getMethodDescriptor;
import static ranttu.rapid.jexp.external.org.objectweb.asm.Type.getType;

/**
 * the pass that generate byte codes
 *
 * @author dongwei.dq
 * @version $Id: GeneratePass.java, v0.1 2017-08-24 10:16 PM dongwei.dq Exp $
 */
public class GeneratePass extends NoReturnPass implements Opcodes {
    /**
     * class writer
     */
    private ClassWriter cw;

    /**
     * execute method visitor
     */
    private MethodVisitor mv;

    /**
     * constructor method visitor
     */
    private MethodVisitor conMv;

    public GeneratePass() {
        this.cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
    }

    @Override
    public void apply(ExpressionNode root, CompilingContext context) {
        if (root.isConstant) {
            context.compiledStub = JExpImmutableExpression.of(root.constantValue);
            return;
        }

        this.context = context;
        // reserved 2
        this.context.variableCount = 2;

        // prepare class
        visitClass();

        // prepare access tree
        prepareAccessorSlot();
        prepareAccessTree();

        // visit execute method
        visit(root);

        // end of execute method
        mathOpValConvert(mv, root.valueType);
        mv.visitInsn(ARETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        // end of construct
        conMv.visitInsn(RETURN);
        conMv.visitMaxs(0, 0);
        conMv.visitEnd();

        // end of all visit
        cw.visitEnd();

        // generate
        // write class
        byte[] byteCodes = cw.toByteArray();

        // for debug
        $.printClass(context.className, byteCodes);

        try {
            Class<JExpExpression> klass = JExpClassLoader.define(context.className, byteCodes);
            context.compiledStub = klass.newInstance();
        } catch (Exception e) {
            throw new JExpCompilingException("error when instance compiled class", e);
        }
    }

    /**
     * visit and put the ast on the stack
     */
    public void visitOnStack(ExpressionNode astNode) {
        visit(astNode);
    }

    private String nextConstantSlot() {
        return "constant$" + context.constantCount++;
    }

    private void prepareAccessorSlot() {
        for (int i = 0; i < context.accessorSlotCount; i++) {
            String slot = "accessor$" + i;

            // add a field to the impl
            cw.visitField(ACC_PRIVATE + ACC_SYNTHETIC, slot, getDescriptor(Accessor.class), null,
                    null);

            // add field init
            conMv.visitVarInsn(ALOAD, 0);
            conMv.visitFieldInsn(GETSTATIC, getInternalName(DummyAccessor.class), "ACCESSOR",
                    getDescriptor(DummyAccessor.class));
            conMv.visitFieldInsn(PUTFIELD, context.classInternalName, slot,
                    getDescriptor(Accessor.class));

            prepareAccessorSlotFitter(slot);
            prepareAccessorSlotGetter(slot);
            prepareAccessorSlotInvoker(slot);
        }
    }

    private void prepareAccessorSlotInvoker(String slot) {
        String invokerName = slot + "Invoke";
        var mv = cw.visitMethod(ACC_PRIVATE + ACC_SYNTHETIC, invokerName,
                getMethodDescriptor(getType(Object.class),
                        getType(Object.class), getType(String.class), getType(Object[].class)),
                null, null);

        //~~~ fit accessor
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(INVOKESPECIAL, context.classInternalName, slot + "Fit",
                getMethodDescriptor(getType(Accessor.class), getType(Object.class)), false);

        //~~~ invoke
        mv.visitVarInsn(ALOAD, 1);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(INVOKEINTERFACE, getInternalName(Accessor.class), "invoke",
                getMethodDescriptor(getType(Object.class), getType(Object.class), getType(String.class),
                        getType(Object[].class)),
                true);
        mv.visitInsn(ARETURN);

        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    private void prepareAccessorSlotGetter(String slot) {
        String getterName = slot + "Get";
        var mv = cw.visitMethod(ACC_PRIVATE + ACC_SYNTHETIC, getterName, getMethodDescriptor(
                getType(Object.class), getType(Object.class), getType(String.class)), null, null);

        //~~~ fit accessor
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(INVOKESPECIAL, context.classInternalName, slot + "Fit",
                getMethodDescriptor(getType(Accessor.class), getType(Object.class)), false);

        //~~~ get
        mv.visitVarInsn(ALOAD, 1);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitMethodInsn(INVOKEINTERFACE, getInternalName(Accessor.class), "get",
                "(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;", true);
        mv.visitInsn(ARETURN);

        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    private void prepareAccessorSlotFitter(String slot) {
        String fitterName = slot + "Fit";
        var mv = cw.visitMethod(ACC_PRIVATE + ACC_SYNTHETIC, fitterName,
                getMethodDescriptor(getType(Accessor.class), getType(Object.class)), null, null);
        var successLabel = new Label();

        // check isSatisfied
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, context.classInternalName, slot, getDescriptor(Accessor.class));
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(INVOKEINTERFACE, getInternalName(Accessor.class), "isSatisfied",
                "(Ljava/lang/Object;)Z", true);
        mv.visitJumpInsn(IFNE, successLabel);

        // failed, change accessor
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(INVOKESTATIC, getInternalName(AccessorFactory.class), "getAccessor",
                getMethodDescriptor(getType(Accessor.class), getType(Object.class)), false);
        mv.visitFieldInsn(PUTFIELD, context.classInternalName, slot, getDescriptor(Accessor.class)); // [ object ]

        // put accessor on stack
        mv.visitLabel(successLabel);
        mv.visitFrame(F_SAME, 0, null, 0, null);

        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, context.classInternalName, slot, getDescriptor(Accessor.class)); // [ object, accessor ]

        // return accessor
        mv.visitInsn(ARETURN);

        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    private void prepareAccessTree() {
        context.propertyTree.visit(idNode -> {
            // for root node, load context on stack
            if (idNode.isRoot) {
                // for a empty tree, do nothing
                if (!idNode.children.isEmpty()) {
                    loadContext();
                }
            }
            // invoke the accessor to get the property
            else {
                // dynamic identifier, just return
                if (idNode.identifier == null) {
                    return;
                }
                invokeAccessorGetter(idNode.accessorSlot, () -> mv.visitLdcInsn(idNode.identifier));
            }

            // dup for each child
            int dupSize = idNode.children.size() + (idNode.isAccessPoint ? 1 : 0);
            for (int i = 1; i < dupSize; i++) {
                mv.visitInsn(DUP);
            }

            // if this a access point, dup and store
            if (idNode.isAccessPoint) {
                idNode.variableIndex = context.nextVariableIndex();
                mv.visitVarInsn(ASTORE, idNode.variableIndex);
            }
        });
    }

    private void visitClass() {
        if (context.option.targetJavaVersion.equals(CompileOption.JAVA_VERSION_16)) {
            cw.visit(V1_6, ACC_SYNTHETIC + ACC_SUPER + ACC_PUBLIC, context.classInternalName, null,
                    getInternalName(Object.class),
                    new String[]{getInternalName(JExpExpression.class)});
            cw.visitSource("<jexp-expression>", null);

            // construct method
            conMv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            conMv.visitCode();
            conMv.visitVarInsn(ALOAD, 0);
            conMv.visitMethodInsn(INVOKESPECIAL, getInternalName(Object.class), "<init>", "()V",
                    false);

            // `execute` method
            mv = cw.visitMethod(ACC_SYNTHETIC + ACC_PUBLIC, "execute",
                    getMethodDescriptor(getType(Object.class), getType(Object.class)), null, null);
            mv.visitParameter("this", 0);
            mv.visitParameter("context", 0);
            mv.visitCode();
        } else {
            throw new JExpCompilingException(
                    "unknown java version" + context.option.targetJavaVersion);
        }
    }

    private void invokeAccessorInvoker(String accessorSlot, CallExpression func) {
        mv.visitVarInsn(ALOAD, 0);
        mv.visitInsn(SWAP);

        //~~~ then, call invoke method
        // put method name
        mv.visitLdcInsn(func.methodName); // [ accessor, object, methodName ]

        // put arguments
        mv.visitLdcInsn(func.parameters.size());
        mv.visitTypeInsn(ANEWARRAY, getInternalName(Object.class));
        for (int i = 0; i < func.parameters.size(); i++) {
            mv.visitInsn(DUP);
            mv.visitLdcInsn(i);
            visit(func.parameters.get(i));
            mv.visitInsn(AASTORE);
        }

        mv.visitMethodInsn(INVOKESPECIAL, context.classInternalName, accessorSlot + "Invoke",
                getMethodDescriptor(getType(Object.class), getType(Object.class), getType(String.class),
                        getType(Object[].class)),
                false);
    }

    private void invokeAccessorGetter(String accessorSlot, Runnable invokePropertyName) {
        mv.visitVarInsn(ALOAD, 0);
        mv.visitInsn(SWAP);
        invokePropertyName.run();
        mv.visitMethodInsn(INVOKESPECIAL, context.classInternalName, accessorSlot + "Get",
                getMethodDescriptor(getType(Object.class), getType(Object.class),
                        getType(String.class)),
                false);
    }

    @Override
    protected void visit(ExpressionNode astNode) {
        // pass through
        if (!astNode.isConstant) {
            super.visit(astNode);
            return;
        }

        // load constant
        Object val = astNode.constantValue;

        // string, directly load
        if (val instanceof String) {
            mv.visitLdcInsn(astNode.constantValue);
        }
        // integer, double, store in slot
        else if (val instanceof Integer || val instanceof Double || val instanceof Boolean) {
            String slot = context.constantSlots.computeIfAbsent(val, v -> {
                String newSlot = nextConstantSlot();

                // field
                cw.visitField(ACC_SYNTHETIC + ACC_PRIVATE, newSlot, getDescriptor(val.getClass()),
                        null, null);

                // field init
                conMv.visitVarInsn(ALOAD, 0);
                conMv.visitLdcInsn(val);
                mathOpValConvert(conMv, Types.getPrimitive(val.getClass()));
                conMv.visitFieldInsn(PUTFIELD, context.classInternalName, newSlot,
                        getDescriptor(val.getClass()));

                return newSlot;
            });

            // get field
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, context.classInternalName, slot,
                    getDescriptor(val.getClass()));
        }
        // unknown constant
        else {
            $.shouldNotReach(val + ": " + val.getClass());
        }
    }

    @Override
    protected void visit(PrimaryExpression exp) {
        // should not be a constant
        if (exp.isConstant) {
            $.shouldNotReach();
        }

        if (exp.token.is(TokenType.IDENTIFIER)) {
            accessOnTree(exp);
        }
    }

    @Override
    @SuppressWarnings("Duplicates")
    protected void visit(BinaryExpression exp) {
        if (exp.op.is(TokenType.OR) || exp.op.is(TokenType.AND)) {
            onCondOp(exp);
            return;
        }

        // for float type
        if (Types.isFloat(exp.valueType)) {
            visit(exp.left);
            if (Types.isInt(exp.left.valueType)) {
                mv.visitInsn(I2D);
            }

            visit(exp.right);
            if (Types.isInt(exp.right.valueType)) {
                mv.visitInsn(I2D);
            }

            switch (exp.op.type) {
                case PLUS:
                    mv.visitInsn(DADD);
                    break;
                case SUBTRACT:
                    mv.visitInsn(DSUB);
                    break;
                case MULTIPLY:
                    mv.visitInsn(DMUL);
                    break;
                case DIVIDE:
                    mv.visitInsn(DDIV);
                    break;
                case MODULAR:
                    mv.visitInsn(DREM);
                    break;
            }
        }
        // for int type
        else if (Types.isInt(exp.valueType)) {
            visit(exp.left);
            visit(exp.right);

            switch (exp.op.type) {
                case PLUS:
                    mv.visitInsn(IADD);
                    break;
                case SUBTRACT:
                    mv.visitInsn(ISUB);
                    break;
                case MULTIPLY:
                    mv.visitInsn(IMUL);
                    break;
                case DIVIDE:
                    mv.visitInsn(IDIV);
                    break;
                case MODULAR:
                    mv.visitInsn(IREM);
                    break;
            }
        }
        // for any runtime types
        else {
            // build arguments
            List<ExpressionNode> args = new ArrayList<>();
            args.add(exp.left);
            args.add(exp.right);

            switch (exp.op.type) {
                case PLUS:
                    applyFunction("math", "add", args);
                    break;
                case SUBTRACT:
                    applyFunction("math", "sub", args);
                    break;
                case MULTIPLY:
                    applyFunction("math", "mul", args);
                    break;
                case DIVIDE:
                    applyFunction("math", "div", args);
                    break;
                case MODULAR:
                    applyFunction("math", "mod", args);
                    break;
            }
        }
    }

    private void onCondOp(BinaryExpression exp) {
        visit(exp.left);
        mv.visitInsn(DUP);
        // call Runtimes.booleanValue
        mv.visitMethodInsn(INVOKESTATIC, getInternalName(Runtimes.class), "booleanValue",
                getMethodDescriptor(getType(boolean.class), getType(Object.class)), false);

        switch (exp.op.type) {
            case OR:
                var trueLabel = new Label();
                mv.visitJumpInsn(IFNE, trueLabel);
                mv.visitInsn(POP);
                visit(exp.right);
                mv.visitLabel(trueLabel);
                mv.visitFrame(F_SAME1, 0, null, 1, new Object[]{Types.getFrameDesc(Object.class)});
                break;
            case AND:
                var falseLabel = new Label();
                mv.visitJumpInsn(IFEQ, falseLabel);
                mv.visitInsn(POP);
                visit(exp.right);
                mv.visitLabel(falseLabel);
                mv.visitFrame(F_SAME1, 0, null, 1, new Object[]{Types.getFrameDesc(Object.class)});
                break;
        }
    }


    @Override
    protected void visit(CallExpression func) {
        // that is, a static call of inner methods
        if (!func.isBounded) {
            applyFunction(func.functionInfo, func.parameters);
        } else {
            // get caller on stack
            visit(func.caller);

            // call
            invokeAccessorInvoker(func.accessorSlot, func);
        }
    }

    @Override
    protected void visit(MemberExpression exp) {
        // for static member expression
        if (exp.isStatic) {
            accessOnTree(exp);
        }
        // for dynamic member expression
        else {
            visit(exp.owner);
            invokeAccessorGetter(exp.propertyNode.accessorSlot, () -> {
                visit(exp.propertyName);
                // to string
                mv.visitMethodInsn(INVOKESTATIC, getInternalName(String.class), "valueOf",
                        getMethodDescriptor(getType(String.class), getType(Object.class)), false);
            });
        }
    }

    @Override
    protected void visit(ArrayExpression exp) {
        mv.visitTypeInsn(NEW, getInternalName(ArrayList.class));
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, getInternalName(ArrayList.class), "<init>", "()V", false);

        exp.items.forEach(item -> {
            mv.visitInsn(DUP);
            visit(item);
            mv.visitMethodInsn(INVOKEINTERFACE, getInternalName(List.class), "add",
                    getMethodDescriptor(getType(boolean.class), getType(Object.class)), true);
            mv.visitInsn(POP);
        });
    }

    /**
     * access a property on access tree
     */
    private void accessOnTree(PropertyAccessNode astNode) {
        $.should(astNode.isStatic);

        PropertyTree.PropertyNode propertyNode = astNode.propertyNode;
        mv.visitVarInsn(ALOAD, propertyNode.variableIndex);
    }

    private void mathOpValConvert(MethodVisitor mv, Type valueType) {
        if (Types.isInt(valueType)) {
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf",
                    "(I)Ljava/lang/Integer;", false);
        } else if (Types.isFloat(valueType)) {
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;",
                    false);
        } else if (valueType == Type.BOOLEAN_TYPE) {
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf",
                    "(Z)Ljava/lang/Boolean;", false);
        } else if (Types.isType(valueType, Object.class)) {
            mv.visitInsn(DUP);
            Label l = new Label();
            mv.visitTypeInsn(INSTANCEOF, getInternalName(StringBuilder.class));
            mv.visitJumpInsn(IFEQ, l);
            mv.visitMethodInsn(INVOKEVIRTUAL, getInternalName(Object.class), "toString",
                    getMethodDescriptor(getType(String.class)), false);
            mv.visitLabel(l);
            mv.visitFrame(F_SAME1, 0, null, 1,
                    new Object[]{Types.getFrameDesc(Object.class)});
        }
    }

    private void loadContext() {
        mv.visitVarInsn(ALOAD, 1);
    }

    // function apply util
    private void applyFunction(String lib, String functionName, List<ExpressionNode> args) {
        Optional<FunctionInfo> info = JExpFunctionFactory.getInfo(lib, functionName);
        if (!info.isPresent()) {
            throw new JExpCompilingException("function name not found: " + functionName);
        }

        applyFunction(info.get(), args);
    }

    private void applyFunction(FunctionInfo info, List<ExpressionNode> args) {
        if (info.inline && context.option.inlineFunction) {
            // inline the function
            JExpByteCodeTransformer.transform(info, this, mv, args, context);
        } else {
            // load stack
            for (ExpressionNode astNode : args) {
                visitOnStack(astNode);
            }

            // call
            mv.visitMethodInsn(INVOKESTATIC, Type.getInternalName(info.method.getDeclaringClass()),
                    info.method.getName(), getMethodDescriptor(info.method), false);
        }
    }
}
