package com.darunkar.design_patterns.complex;/*
 02_EnumSingletonDemo.java
 This is a clean enum-based singleton demo, simplified version of 01_SingletonDemo.java.
 It demonstrates:
 - Singleton creation using enum
 - Thread safety
 - Serialization safety
 - Reflection prevention

 How to run:
 1. Copy this file into your workspace as 02_EnumSingletonDemo.java
 2. Compile: javac 02_EnumSingletonDemo.java
 3. Run: java EnumSingletonDemo

 Expected behaviour:
 - Only one instance ever exists.
 - Serialization / deserialization preserves singleton.
 - Reflection cannot create a new instance.
*/

import java.io.*;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.*;

// Dummy connection object for demonstration
class SimpleConnection implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String id;

    public SimpleConnection() {
        this.id = "conn-" + System.identityHashCode(this) + "-" + UUID.randomUUID().toString().substring(0, 8);
    }

    public String getId() { return id; }

    @Override
    public String toString() { return "SimpleConnection{" + id + "}"; }
}

// Enum singleton
enum ConnectionPoolEnum {
    INSTANCE;

    private final SimpleConnection connection;

    ConnectionPoolEnum() {
        this.connection = new SimpleConnection();
    }

    public SimpleConnection getConnection() {
        return connection;
    }
}

// Driver class
public class EnumSingletonDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("== Concurrency test ==");
        concurrencyTest();

        System.out.println("\n== Serialization test ==");
        serializationTest();

        System.out.println("\n== Reflection test ==");
        reflectionTest();
    }

    static void concurrencyTest() throws InterruptedException {
        final int threads = 50;
        ExecutorService exec = Executors.newFixedThreadPool(Math.min(threads, 16));
        Set<Integer> instanceIds = Collections.synchronizedSet(new HashSet<>());
        CountDownLatch latch = new CountDownLatch(threads);

        for (int i = 0; i < threads; i++) {
            exec.submit(() -> {
                ConnectionPoolEnum e = ConnectionPoolEnum.INSTANCE;
                instanceIds.add(System.identityHashCode(e));
                latch.countDown();
            });
        }

        latch.await();
        exec.shutdown();

        System.out.println("Unique ConnectionPoolEnum instances seen: " + instanceIds.size());
    }

    static void serializationTest() throws Exception {
        ConnectionPoolEnum e1 = ConnectionPoolEnum.INSTANCE;

        // Serialize
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(e1);

        // Deserialize
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
        ConnectionPoolEnum e2 = (ConnectionPoolEnum) ois.readObject();

        System.out.println("e1 == e2 ? " + (e1 == e2));
        System.out.println("e1.connection = " + e1.getConnection());
        System.out.println("e2.connection = " + e2.getConnection());
    }

    static void reflectionTest() {
        try {
            Constructor<?>[] ctors = ConnectionPoolEnum.class.getDeclaredConstructors();
            Constructor<?> ctor = ctors[0];
            ctor.setAccessible(true);
            Object obj = ctor.newInstance();
            System.out.println("Reflection succeeded (unexpected): " + obj);
        } catch (Exception ex) {
            System.out.println("Reflection prevented: " + ex);
        }
    }
}
