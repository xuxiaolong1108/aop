package com.example.plugin;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.RETURN;

public class ClickMethodVisitor extends MethodVisitor {


    private ToastAnnotationVisitor annotationVisitor;

    public ClickMethodVisitor(MethodVisitor methodVisitor) {
        super(Opcodes.ASM5, methodVisitor);
    }

    @Override
    public void visitCode() {


        super.visitCode();

//        System.out.println("nole-------visitCode");
//
//        mv.visitLdcInsn("nole");
//        mv.visitLdcInsn("nole ----------我是click");
//        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/util/Log", "i", "(Ljava/lang/String;Ljava/lang/String;)I", false);
//        mv.visitInsn(Opcodes.POP);

//        mv.visitVarInsn(ALOAD, 1);
//        mv.visitMethodInsn(INVOKEVIRTUAL, "android/view/View", "getContext", "()Landroid/content/Context;", false);
//        mv.visitLdcInsn("dddddd");
//        mv.visitInsn(ICONST_0);
//        mv.visitMethodInsn(INVOKESTATIC, "android/widget/Toast", "makeText", "(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;", false);
//        mv.visitMethodInsn(INVOKEVIRTUAL, "android/widget/Toast", "show", "()V", false);


//        String temp = "dddddd88885222";
//        if (annotationVisitor != null) {
//            temp =   annotationVisitor.getValue();
//        }
////
//        Label label0 = new Label();
//        mv.visitLabel(label0);
//        mv.visitMethodInsn(INVOKESTATIC, "com/example/asm/Utils", "isFastClick", "()Z", false);
//        Label label1 = new Label();
//        mv.visitJumpInsn(IFNE, label1);
//        Label label2 = new Label();
//        mv.visitLabel(label2);
//        mv.visitVarInsn(ALOAD, 1);
//        mv.visitMethodInsn(INVOKEVIRTUAL, "android/view/View", "getContext", "()Landroid/content/Context;", false);
//        mv.visitLdcInsn(temp);
//        mv.visitInsn(ICONST_0);
//        mv.visitMethodInsn(INVOKESTATIC, "android/widget/Toast", "makeText", "(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;", false);
//        mv.visitMethodInsn(INVOKEVIRTUAL, "android/widget/Toast", "show", "()V", false);
//        mv.visitLabel(label1);
//        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
//        mv.visitInsn(RETURN);


        mv.visitMethodInsn(INVOKESTATIC, "com/example/asm/Utils", "isFastClick", "()Z", false);
        Label label1 = new Label();
        mv.visitJumpInsn(IFEQ, label1);
        Label label2 = new Label();
        mv.visitLabel(label2);
//        mv.visitLineNumber(31, label2);
        mv.visitInsn(RETURN);
        mv.visitLabel(label1);
//        mv.visitLineNumber(33, label1);
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);


    }

    @Override
    public void visitInsn(int opcode) {


        String temp = "dddddd88885222";
        if (annotationVisitor != null) {
            temp =   annotationVisitor.getValue();
        }


        if (opcode == ARETURN || opcode == RETURN) {

            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEVIRTUAL, "android/view/View", "getContext", "()Landroid/content/Context;", false);
            mv.visitLdcInsn(temp);
            mv.visitInsn(ICONST_0);
            mv.visitMethodInsn(INVOKESTATIC, "android/widget/Toast", "makeText", "(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "android/widget/Toast", "show", "()V", false);

        }

        super.visitInsn(opcode);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        System.out.println("nole-------visitAnnotation" + descriptor);

        if (descriptor.contains("Toast")) {
            annotationVisitor = new ToastAnnotationVisitor(new ToastAnnotationVisitor(super.visitAnnotation(descriptor, visible)));
            return annotationVisitor;
        } else {
            return super.visitAnnotation(descriptor, visible);
        }

    }

}
