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
    Map<String, LoadedInstance> instancMap = new HashMap<>();

    public SourceRunner(List<String> sourceCodesList) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        this.fileManager = new InMemoryFileManager(
                compiler.getStandardFileManager(null, null, null));
        InMemoryCompiler.compile(sourceCodesList, fileManager);
    }

    public void instanciate(String fullyQualifiteClassName, Object... args) {
        try {
            Class<?> clazz = fileManager.loadClass(fullyQualifiteClassName);

            // get args Types
            Class<?>[] paramTypes = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++) {
                paramTypes[i] = args[i].getClass();
            }

            Object instance = clazz.getDeclaredConstructor(paramTypes).newInstance(args);

            instancMap.put(fullyQualifiteClassName, new LoadedInstance(clazz, instance));

            //LoadedInstance loadedInstance = getLoadedInstance(fullyQualifiteClassName);

            //System.out.println("📦 [INSTANCIATE] " + fullyQualifiteClassName + " instance class: " + loadedInstance.instance.getClass());
            //System.out.println("🧠 [INSTANCIATE] " + fullyQualifiteClassName + " instance loader: " + loadedInstance.instance.getClass().getClassLoader());

        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public LoadedInstance getLoadedInstance(String fullyQualifiteClassName) {
        return instancMap.get(fullyQualifiteClassName);
    }

    public <T> T run(String fullyQualifiteClassName, String methodName, Object... args) {
        try {
            // get Instance
            Class<?> clazz = instancMap.get(fullyQualifiteClassName).clazz;
            Object instance = instancMap.get(fullyQualifiteClassName).instance;

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
