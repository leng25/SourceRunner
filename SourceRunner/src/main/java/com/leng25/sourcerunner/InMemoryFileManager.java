package com.leng25.sourcerunner;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;

/**
 * InMemoryFileManager
 */
public class InMemoryFileManager extends ForwardingJavaFileManager<JavaFileManager>{
    
    private final Map<String, InMemoryByteCode> compile = new ConcurrentHashMap<>(); 

	protected InMemoryFileManager(JavaFileManager fileManager) {
		super(fileManager);
	}

    @Override
    public JavaFileObject getJavaFileForOutput(Location location,
                                               String className,
                                               Kind kind,
                                               FileObject sibling)
        throws IOException
    {
        InMemoryByteCode byteCode =  new InMemoryByteCode(className);
        compile.put(className, byteCode);
        return byteCode;
    }

    @Override
    public ClassLoader getClassLoader(Location location) {
        return new ClassLoader() {
            @Override
            protected Class<?> findClass(String name){
                byte[] bytes =  compile.get(name).getBytes();
                return defineClass(name, bytes, 0, bytes.length);
            }
        };
    }
    
}
