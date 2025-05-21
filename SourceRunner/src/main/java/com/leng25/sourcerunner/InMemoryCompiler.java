package com.leng25.sourcerunner;

import java.util.ArrayList;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

/**
 * InMemoryCompiler
 */
public class InMemoryCompiler {


    public static void compile(List<String> sourceCodesList, InMemoryFileManager fileManager) {

      JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new IllegalStateException(
                    "JavaCompiler not available. Ensure the application is running on a JDK, not a JRE.");
        }

        List<JavaFileObject> javaFileObjectsList = new ArrayList<>();

        sourceCodesList.forEach(sourceCode -> {
            String fullyQualifiedClassName = extractFQCN(sourceCode);
            javaFileObjectsList.add(new InMemorySource(fullyQualifiedClassName, sourceCode));
        });

        String classpath = "target/classes:" + System.getProperty("java.class.path");

        JavaCompiler.CompilationTask task = compiler.getTask(
                null,
                fileManager,
                null,
                List.of("-classpath", classpath),
                null,
                javaFileObjectsList);

        boolean success = task.call();
        if (!success)
            throw new RuntimeException("copilation failed.");

    }

    private static String extractFQCN(String sourceCode) {
        String packageName = "";
        String className = "";

        for (String line : sourceCode.split("\\R")) {
            line = line.strip();

            if (line.startsWith("package ")) {
                packageName = line.substring(8, line.indexOf(";")).strip();
            }

            if (line.startsWith("public ")) {
                String[] tokens = line.split("\\s+");

                // Look for: public class|interface|enum|record Name {
                for (int i = 1; i < tokens.length - 1; i++) {
                    if (tokens[i].equals("class") ||
                            tokens[i].equals("interface") ||
                            tokens[i].equals("enum") ||
                            tokens[i].equals("record")) {
                        className = tokens[i + 1];
                        break;
                    }
                }
            }

            if (!className.isEmpty()) {
                break;
            }
        }

        if (className.isEmpty()) {
            throw new IllegalArgumentException("No public top-level class/interface/enum/record found.");
        }

        return packageName.isEmpty() ? className : packageName + "." + className;
    }
}
