package com.leng25.sourcerunner;

import java.util.Arrays;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

/**
 * InMemoryCompiler
 */
public class InMemoryCompiler {

    public static Class<?> compile(String fullyQualifiedClassName, String sourceCode) throws ClassNotFoundException {

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new IllegalStateException("JavaCompiler not available. Ensure the application is running on a JDK, not a JRE."); 
        }

        InMemoryFileManager fileManager = new InMemoryFileManager(
                compiler.getStandardFileManager(null, null, null));

        JavaFileObject source = new InMemorySource(fullyQualifiedClassName, sourceCode);

        String classpath = "target/classes:" + System.getProperty("java.class.path");

        JavaCompiler.CompilationTask task = compiler.getTask(
                null,
                fileManager,
                null,
                List.of("-classpath", classpath),
                null,
                Arrays.asList(source));

        boolean success = task.call();
        if (!success)
            throw new RuntimeException("copilation failed.");

        return fileManager
                .getClassLoader(null)
                .loadClass(fullyQualifiedClassName);
    }
}
