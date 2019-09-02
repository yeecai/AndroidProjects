package com.example.notes;

//1. Eager initialization P: simple; C: can't handle exception
class SingletonPattern {
    private static final SingletonPattern instance = new SingletonPattern();

    private SingletonPattern() {

    }

    public static SingletonPattern getInstance() {
        return instance;
    }
}

//2. Static block, can handle exception
class Singleton_static_block {
    public static Singleton_static_block instance;

    private Singleton_static_block(){

    }

    {
        instance = new Singleton_static_block();
    }
}

// 3. Lazy initializton
class Singleton_lazy {
    private static Singleton_lazy instance;

    private Singleton_lazy() {

    }

    synchronized public static Singleton_lazy getInstance() {
        if (instance==null) {
            instance = new Singleton_lazy();
        }
        return instance;
    }
}

// 4. Lazy but thread safe
class Singleton_lazy_safe {
    private static Singleton_lazy_safe instance;

    private Singleton_lazy_safe() {

    }

    synchronized public static Singleton_lazy_safe getInstance() {
        if (instance==null) {
            instance = new Singleton_lazy_safe();
        }
        return instance;
    }
}

