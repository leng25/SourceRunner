package com.leng25.sourcerunner;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

/**
 * SourceRunner
 */
public class SourceRunner {

    InMemoryFileManager fileManager;

    public SourceRunner(List<String> sourceCodesList) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        this.fileManager = new InMemoryFileManager(
                compiler.getStandardFileManager(null, null, null));
        InMemoryCompiler.compile(sourceCodesList, fileManager);
    }

    public Object instanciate(String fullyQualifiteClassName, Object... args) {
        try {
            Class<?> clazz = fileManager.loadClass(fullyQualifiteClassName);

            // get args Types
            Class<?>[] paramTypes = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++) {
                paramTypes[i] = args[i].getClass();
            }

            Object instance = clazz.getDeclaredConstructor(paramTypes).newInstance(args);
            
            return instance;


            //LoadedInstance loadedInstance = getLoadedInstance(fullyQualifiteClassName);

            //System.out.println("📦 [INSTANCIATE] " + fullyQualifiteClassName + " instance class: " + loadedInstance.instance.getClass());
            //System.out.println("🧠 [INSTANCIATE] " + fullyQualifiteClassName + " instance loader: " + loadedInstance.instance.getClass().getClassLoader());

        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
            e.printStackTrace();
        }
		return null;
    }

    public <T> T run(Object instance, String methodName, Object... args) {
        try {
            // get Instance
            Class<?> clazz = instance.getClass();

            // get args Types
            Class<?>[] paramTypes = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++) {
                paramTypes[i] = args[i].getClass();
                //System.out.println("📦 [RUN] paramTypes[" + i + "] class: " + paramTypes[i] + paramTypes[i].getClassLoader());
                //System.out.println("📦 [RUN] args[" + i + "] class: " + args[i].getClass() + args[i].getClass().getClassLoader());
            }

            // run
            Object result = clazz.getMethod(methodName, paramTypes).invoke(instance, args);
            return (T) result;

        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to run method: " + methodName, e);

        }
    }

}
