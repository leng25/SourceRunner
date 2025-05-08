package com.leng25.sourcerunner;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

/**
 * InMemoryByteCode
 */
public class InMemoryByteCode extends SimpleJavaFileObject{
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
	
    protected InMemoryByteCode(String className) {
		super(URI.create("bytes:///"+ className), Kind.CLASS);
	}

    @Override
    public OutputStream openOutputStream(){
        return out;
    }
    
    public byte[] getBytes() {
        return out.toByteArray();
    }
}
