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
    Map<String, LoadedInstance> instancMap =  new HashMap<>();

    public SourceRunner(List<String> sourceCodesList) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        InMemoryFileManager fileManager = new InMemoryFileManager(
                compiler.getStandardFileManager(null, null, null));
        InMemoryCompiler.compile(sourceCodesList, fileManager);
        this.fileManager = fileManager;
    }

    public void instanciate(String fullyQualifiteClassName, Object... args){
            try {
                Class<?> clazz = fileManager.loadClass(fullyQualifiteClassName);
				
                // get args Types
                Class<?>[] paramTypes = new Class<?>[args.length];
                for (int i = 0; i < args.length; i++) {
                    paramTypes[i] = args[i].getClass(); 
                }
                
                Object instance = clazz.getDeclaredConstructor(paramTypes).newInstance(args);
                
                instancMap.put(fullyQualifiteClassName, new LoadedInstance(clazz, instance));
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
				e.printStackTrace();
			}
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
            }
            
            //run
            Object result = clazz.getMethod(methodName, paramTypes).invoke(instance, args);
            return (T) result;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to run method: " + methodName, e);

        }
    }

}
