package com.example.plugin;

import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.AppExtension;
import com.android.build.gradle.internal.pipeline.TransformManager;
import com.android.utils.FileUtils;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Properties;
import java.util.Set;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;

public class JavaassistPlugin extends Transform implements Plugin<Project> {
    private Project project;
    private final static ClassPool pool = ClassPool.getDefault();

    @Override
    public void apply(Project project) {
        AppExtension appExtension = project.getExtensions().getByType(AppExtension.class);
        appExtension.registerTransform(this);
        this.project = project;
    }

    @Override
    public String getName() {
        return "JavaassistTransform";
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    public boolean isIncremental() {
        return false;
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation);

        // 遍历所有字节码文件
        try {
            Collection<TransformInput> inputs = transformInvocation.getInputs();
            TransformOutputProvider transformOutputProvider = transformInvocation.getOutputProvider();

            //project.android.bootClasspath 加入android.jar，不然找不到android相关的所有类// "E:\\environment\\android\\sdk\\platforms\\android-31\\android.jar"

            Properties properties = new Properties();
            InputStream inputStream = new FileInputStream(project.getRootProject().file("local.properties"));
            properties.load(inputStream);
            String sdkDir = properties.getProperty("sdk.dir");
            pool.appendClassPath(sdkDir + "\\platforms\\android-30\\android.jar");


            for (TransformInput input : inputs) {
                Collection<DirectoryInput> directoryInputs = input.getDirectoryInputs(); // 获取代码的.class文件
                for (DirectoryInput directoryInput : directoryInputs) {

                    //将当前路径加入类池,不然找不到这个类
                    pool.appendClassPath(directoryInput.getFile().getAbsolutePath());

                    File dir = new File(directoryInput.getFile().getAbsolutePath());
                    if (dir.isDirectory()) {

                        for (File file : FileUtils.getAllFiles(dir)) {

                            // 绝对路径E:\work1\Javaassist\app\build\intermediates\javac\debug\classes\com\example\javaassist\MainActivity.class 转化 com.example.javaassist.MainActivity

                            String className = file.getAbsolutePath().replace("\\", ".").replace("/", ".").replace(".class", "");
                            String name = className.substring(className.indexOf("com"));

                            CtClass ctClass = pool.getCtClass(name);

                            CtClass[] interfaces = ctClass.getInterfaces(); // 获取实现的接口

                            // 是否实现了 android.view.View$OnClickListener

                            boolean hasClickInterface = false;
                            for (CtClass anInterface : interfaces) {
                                if ("android.view.View$OnClickListener".equals(anInterface.getName())) {
                                    hasClickInterface = true;
                                }
                            }

                            if (!hasClickInterface) {
                                continue;
                            }

                            // 找到切入点 onclick
                            if (name.contains("$")) {
                                // 内部类
                                if (ctClass.isFrozen()) {
                                    ctClass.defrost();
                                }
                                CtMethod ctMethod = ctClass.getDeclaredMethod("onClick");

                                CtField outer = null;
                                for (int i = 0; i < ctClass.getFields().length; i++) {
                                    if (pool.get(name.substring(0, name.indexOf("$"))) == ctClass.getFields()[i].getType()) {
                                        outer = ctClass.getFields()[i]; // 获取外部类信息
                                    }
                                }

                                AnnotationsAttribute annotationsAttribute = (AnnotationsAttribute) ctMethod.getMethodInfo2().getAttribute(AnnotationsAttribute.invisibleTag);
                                Annotation[] annotations = annotationsAttribute.getAnnotations();

                                StringMemberValue value = null;
                                for (int i = 0; i < annotations.length; i++) {
                                    if ("com.example.javaassist.ToastAnnotation".equals(annotations[i].getTypeName())) {
                                        value = (StringMemberValue) annotations[i].getMemberValue("value");
                                    }
                                }

                                String insetBeforeStr = "if (com.example.javaassist.Utils.isFastClick()) { return; }";
                                if (value != null) {
                                    System.out.println("nole----------" + value.getValue());
                                    insetBeforeStr += "android.widget.Toast.makeText($1.getContext(),\" " + value.getValue() + "  \",android.widget.Toast.LENGTH_LONG).show();";

                                }

                                //在方法开头插入代码
                                ctMethod.insertBefore(insetBeforeStr);
                                ctClass.writeFile(directoryInput.getFile().getAbsolutePath()); // 写入文件夹
                                ctClass.detach();//释放

                            } else {
                                // 外部类
                                if (ctClass.isFrozen()) {
                                    ctClass.defrost();
                                }
                                CtMethod ctMethod = ctClass.getDeclaredMethod("onClick");

                                String insetBeforeStr = "if (com.example.javaassist.Utils.isFastClick()) { return; }";

                                //在方法开头插入代码
                                ctMethod.insertBefore(insetBeforeStr);
                                ctClass.writeFile(directoryInput.getFile().getAbsolutePath());
                                ctClass.detach();//释放

                            }
                        }
                    }

                    File dest = transformOutputProvider.getContentLocation(directoryInput.getName(),
                            directoryInput.getContentTypes(), directoryInput.getScopes(), Format.DIRECTORY);
                    FileUtils.copyDirectory(directoryInput.getFile(), dest);
                }
                Collection<JarInput> jarInputs = input.getJarInputs();

                for (JarInput jarInput : jarInputs) { // 获取三方jar的字节码文件 直接原封不动写入 不处理
                    File dest = transformOutputProvider.getContentLocation(
                            jarInput.getName(),
                            jarInput.getContentTypes(),
                            jarInput.getScopes(),
                            Format.JAR
                    );
                    FileUtils.copyFile(jarInput.getFile(), dest);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}