package com.example.plugin;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Created by xxl53946 on 2021/11/22.
 */
public class DemoClassVisitor extends ClassVisitor {


    public DemoClassVisitor(ClassWriter classWriter) {
        super(Opcodes.ASM5, classWriter);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        System.out.println("nole-------- visitMethod " + name + "  " + descriptor);

//        if (name.equals("onClick")) {
//            return new ClickMethodVisitor(methodVisitor);
//        }

        if (name.equals("onClick")) {
            return new ReplaceMethodVisitor(methodVisitor, this, name, descriptor, signature, exceptions);
        }

        return methodVisitor;
    }


}
