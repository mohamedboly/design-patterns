package com.numeriquepro;

public class MultithreadSingleton {
    private static volatile MultithreadSingleton instance;

    private MultithreadSingleton() {}

    public static MultithreadSingleton getInstance() {
        MultithreadSingleton result = instance;
        if (result == null) {
            synchronized (MultithreadSingleton.class) {
                result = instance;
                if (result == null) {
                    instance = result = new MultithreadSingleton();
                }
            }
        }
        return result;
    }
}
