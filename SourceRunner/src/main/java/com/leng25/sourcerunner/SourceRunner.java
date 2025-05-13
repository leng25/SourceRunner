package com.leng25.sourcerunner;

import java.lang.reflect.InvocationTargetException;

/**
 * SourceRunner
 */
public class SourceRunner {

    Class<?> clazz;
    Object instance;

    public SourceRunner(String fullyQualifiedClassName, String sourceCode) {
        try {
            try {
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
}
