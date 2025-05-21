package com.leng25.sourcerunner;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

/**
 * SourceRunner
 */
public class SourceRunner {

    InMemoryFileManager fileManager;

    public SourceRunner(List<String> sourceCodesList) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        InMemoryFileManager fileManager = new InMemoryFileManager(
                compiler.getStandardFileManager(null, null, null));
        InMemoryCompiler.compile(sourceCodesList, fileManager);
        this.fileManager = fileManager;
    }

    public <T> T run(String fullyQualifiteClassName, String methodName, Object... args) {
        try {
            // get Instance
            Class<?> clazz = fileManager.loadClass(fullyQualifiteClassName);
            Object instance = clazz.getDeclaredConstructor().newInstance();
           
            // get args Types
            Class<?>[] paramTypes = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++) {
               paramTypes[i] = args[i].getClass(); 
            }
            
            //run
            Object result = clazz.getMethod(methodName, paramTypes).invoke(instance, args);
            return (T) result;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException
                | ClassNotFoundException | InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to run method: " + methodName, e);

        }
    }

}
