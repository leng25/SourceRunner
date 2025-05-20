package com.leng25.sourcerunner;

import java.lang.reflect.InvocationTargetException;

/**
 * SourceRunner
 */
public class SourceRunner {

    Class<?> clazz;
    Object instance;

    public SourceRunner(String sourceCode) {
        try {
            try {
                String fullyQualifiedClassName = extractFQCN(sourceCode);
                this.clazz = InMemoryCompiler.compile(fullyQualifiedClassName, sourceCode);
                this.instance = clazz.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public <T> T run(String methodName){
			try {				
                Object result = clazz.getMethod(methodName).invoke(instance);
                return (T) result; 
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
                throw new RuntimeException("Failed to run method: " + methodName, e);
			}
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
