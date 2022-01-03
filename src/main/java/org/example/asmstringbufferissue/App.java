package org.example.asmstringbufferissue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * @author Diptopol
 * @since 1/3/2022 1:34 AM
 */
public class App {

    public static void main(String[] args) {
        String jarLocation = "jarDirectory/rt.jar";
        JarFile jarFile = getJarFile(jarLocation);

        Enumeration<JarEntry> entries = jarFile.entries();
        ClassNode classNode;

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String entryName = entry.getName();

            if (entryName.endsWith(".class")) {
                classNode = new ClassNode();
                InputStream classFileInputStream;

                try {
                    classFileInputStream = jarFile.getInputStream(entry);
                    try {
                        ClassReader classReader = new ClassReader(
                                classFileInputStream);
                        classReader.accept(classNode, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        classFileInputStream.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String qualifiedName = classNode.name.replace('/', '.');

                if (qualifiedName.equals("java.lang.StringBuffer")) {
                    List<MethodNode> methodNodes = classNode.methods;

                    for (MethodNode methodNode : methodNodes) {
                        if (methodNode.name.equals("delete")) {
                            System.out.println("Method Name : " + methodNode.name);
                            System.out.println("Desc : " + methodNode.desc);
                            System.out.println("Return Type : " + Type.getReturnType(methodNode.desc));

                            /*
                            Method Name : delete
                            Desc : (II)Ljava/lang/StringBuffer;
                            Return Type : Ljava/lang/StringBuffer;
                            Method Name : delete
                            Desc : (II)Ljava/lang/AbstractStringBuilder;
                            Return Type : Ljava/lang/AbstractStringBuilder;
                            */
                        }
                    }
                }
            }
        }
    }

    private static JarFile getJarFile(String jarLocation) {
        File file = new File(jarLocation);
        if (file.exists()) {
            try {
                return new JarFile(new File(jarLocation));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
