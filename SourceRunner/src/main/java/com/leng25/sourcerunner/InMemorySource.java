package com.leng25.sourcerunner;

import java.net.URI;

import javax.tools.SimpleJavaFileObject;

/**
 * InMemorySource
 */
public class InMemorySource extends SimpleJavaFileObject{
    private final String code;

    protected InMemorySource(String name, String code){
        super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension),Kind.SOURCE);
        this.code = code;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return code;
    }
    
}
