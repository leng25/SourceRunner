package com.leng25.sourcerunner;

/**
 * LoadedInstance
 */
public class LoadedInstance {
    public final Class<?> clazz;
    public final Object instance;

    public LoadedInstance(Class<?> clazz, Object instance) {
        this.clazz = clazz;
        this.instance = instance;
    }
}
