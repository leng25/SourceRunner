package com.leng25.sourcerunner;

import java.util.Arrays;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

/**
 * InMemoryCompiler
 */
public class InMemoryCompiler {

    public static Class<?> compile(String fullyQualifiedClassName, String sourceCode) throws ClassNotFoundException {

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        InMemoryFileManager fileManager = new InMemoryFileManager(
                compiler.getStandardFileManager(null, null, null));

        JavaFileObject source = new InMemorySource(fullyQualifiedClassName, sourceCode);

        JavaCompiler.CompilationTask task = compiler.getTask(
                null,
                fileManager,
                null,
                null,
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
