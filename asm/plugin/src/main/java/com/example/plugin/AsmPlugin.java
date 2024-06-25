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

import org.codehaus.groovy.runtime.ResourceGroovyMethods;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

public class AsmPlugin extends Transform implements Plugin<Project> {
    @Override
    public void apply(Project project) {

        AppExtension appExtension = project.getExtensions().getByType(AppExtension.class);
        appExtension.registerTransform(this);
    }

    @Override
    public String getName() {
        return "AsmTransform";
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
            Collection<TransformInput> inputs = transformInvocation.getInputs();
            TransformOutputProvider transformOutputProvider = transformInvocation.getOutputProvider();


            for (TransformInput input : inputs) {
                Collection<DirectoryInput> directoryInputs = input.getDirectoryInputs(); // 获取代码的.class文件
                for (DirectoryInput directoryInput : directoryInputs) {
                    File dir = new File(directoryInput.getFile().getAbsolutePath());
                    if (dir.isDirectory()) {
                        for (File file : FileUtils.getAllFiles(dir)) {

                            System.out.println("nole--------"+file.getAbsolutePath());
                            String classPath = file.getAbsolutePath();

                            ClassReader reader = new ClassReader(ResourceGroovyMethods.getBytes(file)); // 获取file字节流

                            ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS);

                            DemoClassVisitor visitor = new DemoClassVisitor(writer);
                            reader.accept(visitor, ClassReader.EXPAND_FRAMES);

                            byte[] bytes = writer.toByteArray();
                            FileOutputStream fos = new FileOutputStream(classPath);
                            fos.write(bytes);
                            fos.close();
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



    }
}