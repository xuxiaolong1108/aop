package com.example.plugin;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Created by xxl53946 on 2021/11/23.
 */
public class ToastAnnotationVisitor extends AnnotationVisitor {
    String value = "";

    public ToastAnnotationVisitor(AnnotationVisitor annotationVisitor) {
        super(Opcodes.ASM5, annotationVisitor);
    }

    @Override
    public void visit(String name, Object value) {
        this.value = (String)value;
        super.visit(name, value);
    }


    public String getValue() {
        return value;
    }
}
