package com.example.plugin;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.AALOAD;
import static org.objectweb.asm.Opcodes.AASTORE;
import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACONST_NULL;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ANEWARRAY;
import static org.objectweb.asm.Opcodes.ARRAYLENGTH;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.IF_ICMPGE;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.ISTORE;
import static org.objectweb.asm.Opcodes.RETURN;


public class ReplaceMethodVisitor extends MethodVisitor {
    ClassVisitor classVisitor;
    String name;
    String signature;
    String replaceMethodName;
    MethodVisitor replaceMethodVisitor = new EmptyMethodVisitor(0);
    String[] exceptions;
    String desc;
    private static int methodIndex;

    private ConfirmAnnotationVisitor confirmAnnotationVisitor;

    static class EmptyMethodVisitor extends MethodVisitor {

        public EmptyMethodVisitor(int api) {
            super(Opcodes.ASM5);
        }

    }

    public ReplaceMethodVisitor(MethodVisitor mv, ClassVisitor cv, String name, String desc, String signature, String[] exceptions) {
        super(Opcodes.ASM5, mv);
        classVisitor = cv;
        this.name = name;
        this.signature = signature;
        this.exceptions = exceptions;
        this.desc = desc;
        methodIndex++;//使用这个参数可以区分相同名字的方法

    }


    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {

        // desc Lcom/example/asm/ConfirmAnnotation;

        System.out.println("nole--------- visitAnnotation name" + desc);

        if (desc.equals("Lcom/example/asm/ConfirmAnnotation;")) {
            String name = desc.substring(desc.lastIndexOf("/") + 1, desc.length() - 1).toLowerCase();
            //创建修改原有方法的名字
            replaceMethodName = "_" + name + "_xxl" + methodIndex + "_" + this.name;
            replaceMethodVisitor = classVisitor.visitMethod(ACC_PRIVATE, replaceMethodName, this.desc, signature, exceptions);
            confirmAnnotationVisitor = new ConfirmAnnotationVisitor(super.visitAnnotation(desc, visible));
            return confirmAnnotationVisitor;
        }

        return super.visitAnnotation(desc, visible);
    }


    @Override
    public void visitCode() {

        if (confirmAnnotationVisitor != null) {
            MethodVisitor methodVisitor = mv;
            Label label0 = new Label();
            methodVisitor.visitLabel(label0);
            methodVisitor.visitInsn(ACONST_NULL);
            methodVisitor.visitVarInsn(ASTORE, 2);
            Label label1 = new Label();
            methodVisitor.visitLabel(label1);
            methodVisitor.visitLdcInsn(replaceMethodName);
            methodVisitor.visitVarInsn(ASTORE, 3);
            Label label2 = new Label();
            methodVisitor.visitLabel(label2);
            methodVisitor.visitInsn(ICONST_0);
            methodVisitor.visitVarInsn(ISTORE, 4);
            Label label3 = new Label();
            methodVisitor.visitLabel(label3);
            methodVisitor.visitFrame(Opcodes.F_APPEND, 3, new Object[]{"java/lang/reflect/Method", "java/lang/String", Opcodes.INTEGER}, 0, null);
            methodVisitor.visitVarInsn(ILOAD, 4);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;", false);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getDeclaredMethods", "()[Ljava/lang/reflect/Method;", false);
            methodVisitor.visitInsn(ARRAYLENGTH);
            Label label4 = new Label();
            methodVisitor.visitJumpInsn(IF_ICMPGE, label4);
            Label label5 = new Label();
            methodVisitor.visitLabel(label5);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;", false);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getDeclaredMethods", "()[Ljava/lang/reflect/Method;", false);
            methodVisitor.visitVarInsn(ILOAD, 4);
            methodVisitor.visitInsn(AALOAD);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/reflect/Method", "getName", "()Ljava/lang/String;", false);
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
            Label label6 = new Label();
            methodVisitor.visitJumpInsn(IFEQ, label6);
            Label label7 = new Label();
            methodVisitor.visitLabel(label7);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;", false);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getDeclaredMethods", "()[Ljava/lang/reflect/Method;", false);
            methodVisitor.visitVarInsn(ILOAD, 4);
            methodVisitor.visitInsn(AALOAD);
            methodVisitor.visitVarInsn(ASTORE, 2);
            methodVisitor.visitLabel(label6);
            methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            methodVisitor.visitIincInsn(4, 1);
            methodVisitor.visitJumpInsn(GOTO, label3);
            methodVisitor.visitLabel(label4);
            methodVisitor.visitFrame(Opcodes.F_CHOP, 1, null, 0, null);
            methodVisitor.visitLdcInsn(confirmAnnotationVisitor.getValue());
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "android/view/View", "getContext", "()Landroid/content/Context;", false);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitInsn(ICONST_1);
            methodVisitor.visitTypeInsn(ANEWARRAY, "java/lang/Object");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitInsn(ICONST_0);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitInsn(AASTORE);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "com/example/asm/Utils", "onMethodIntercepted", "(Ljava/lang/String;Landroid/content/Context;Ljava/lang/Object;Ljava/lang/reflect/Method;[Ljava/lang/Object;)V", false);
            Label label8 = new Label();
            methodVisitor.visitLabel(label8);
            methodVisitor.visitInsn(RETURN);
        }
        super.visitCode();
        replaceMethodVisitor.visitCode();
        System.out.println("nole--------- visitCode " + desc);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        super.visitMethodInsn(opcode, owner, name, desc, itf);
        replaceMethodVisitor.visitMethodInsn(opcode, owner, name, desc, itf);
        System.out.println("nole--------- visitMethodInsn " + desc);

    }

    @Override
    public void visitLabel(Label label) {
        super.visitLabel(label);
        replaceMethodVisitor.visitLabel(label);
        System.out.println("nole--------- visitLabel " + desc);
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        super.visitJumpInsn(opcode, label);
        replaceMethodVisitor.visitJumpInsn(opcode, label);
        System.out.println("nole--------- visitJumpInsn " + desc);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        super.visitMaxs(maxStack, maxLocals);
        replaceMethodVisitor.visitMaxs(maxStack, maxLocals);
        System.out.println("nole--------- visitMaxs " + desc);
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        super.visitVarInsn(opcode, var);
        replaceMethodVisitor.visitVarInsn(opcode, var);
        System.out.println("nole--------- visitVarInsn " + desc);
    }


    @Override
    public void visitInsn(int opcode) {
        super.visitInsn(opcode);
        replaceMethodVisitor.visitInsn(opcode);
        System.out.println("nole--------- visitInsn " + desc);
    }

    @Override
    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        super.visitLocalVariable(name, desc, signature, start, end, index);
        replaceMethodVisitor.visitLocalVariable(name, desc, signature, start, end, index);
        System.out.println("nole--------- visitLocalVariable " + desc);
    }

    @Override
    public void visitLdcInsn(Object cst) {
        super.visitLdcInsn(cst);
        replaceMethodVisitor.visitLdcInsn(cst);
        System.out.println("nole--------- visitLdcInsn " + desc);
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        super.visitTypeInsn(opcode, type);
        replaceMethodVisitor.visitTypeInsn(opcode, type);
        System.out.println("nole--------- visitTypeInsn " + desc);
    }


//    visitParameter


    @Override
    public void visitParameter(String name, int access) {
        super.visitParameter(name, access);
        replaceMethodVisitor.visitParameter(name, access);
        System.out.println("nole--------- visitParameter " + desc);
    }

    //visitAttribute


    @Override
    public void visitAttribute(Attribute attr) {
        super.visitAttribute(attr);
        replaceMethodVisitor.visitAttribute(attr);
        System.out.println("nole--------- visitAttribute " + desc);
    }

    //visitFrame


    @Override
    public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
        super.visitFrame(type, nLocal, local, nStack, stack);
        replaceMethodVisitor.visitFrame(type, nLocal, local, nStack, stack);
        System.out.println("nole--------- visitFrame " + desc);
    }

    //visitIntInsn


    @Override
    public void visitIntInsn(int opcode, int operand) {
        super.visitIntInsn(opcode, operand);
        replaceMethodVisitor.visitIntInsn(opcode, operand);
        System.out.println("nole--------- visitIntInsn " + desc);
    }

    //visitFieldInsn


    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        super.visitFieldInsn(opcode, owner, name, desc);
        replaceMethodVisitor.visitFieldInsn(opcode, owner, name, desc);
        System.out.println("nole--------- visitFieldInsn " + desc);
    }

    //visitInvokeDynamicInsn


    @Override
    public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
        super.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
        replaceMethodVisitor.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
        System.out.println("nole--------- visitInvokeDynamicInsn " + desc);
    }

    //visitIincInsn


    @Override
    public void visitIincInsn(int var, int increment) {
        super.visitIincInsn(var, increment);
        replaceMethodVisitor.visitIntInsn(var, increment);
        System.out.println("nole--------- visitIincInsn " + desc);
    }

    //visitTableSwitchInsn


    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
        super.visitTableSwitchInsn(min, max, dflt, labels);
        replaceMethodVisitor.visitTableSwitchInsn(min, max, dflt, labels);
        System.out.println("nole--------- visitTableSwitchInsn " + desc);
    }


    //visitLookupSwitchInsn


    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        super.visitLookupSwitchInsn(dflt, keys, labels);
        replaceMethodVisitor.visitLookupSwitchInsn(dflt, keys, labels);
        System.out.println("nole--------- visitLookupSwitchInsn " + desc);
    }


    //visitMultiANewArrayInsn


    @Override
    public void visitMultiANewArrayInsn(String desc, int dims) {
        super.visitMultiANewArrayInsn(desc, dims);
        replaceMethodVisitor.visitMultiANewArrayInsn(desc, dims);
        System.out.println("nole--------- visitMultiANewArrayInsn " + desc);
    }

    //visitTryCatchBlock


    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        super.visitTryCatchBlock(start, end, handler, type);
        replaceMethodVisitor.visitTryCatchBlock(start, end, handler, type);
        System.out.println("nole--------- visitTryCatchBlock " + desc);
    }

    //visitLineNumber


    @Override
    public void visitLineNumber(int line, Label start) {
        super.visitLineNumber(line, start);
        replaceMethodVisitor.visitLineNumber(line, start);
        System.out.println("nole--------- visitLineNumber " + desc);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
        replaceMethodVisitor.visitEnd();
        System.out.println("nole--------- visitEnd " + desc);
    }


}
