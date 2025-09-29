package com.darunkar.design_patterns;/*
 01_SingletonDemo.java
 Step-by-step Singleton implementations + tests
 - ConnectionPoolDCL: double-checked locking (lazy init) + readResolve
 - ConnectionPoolEnum: enum-based singleton (recommended)
 - SimpleConnection: dummy serializable resource
 - SingletonDemo (public): contains main() that runs three tests:
     1) concurrencyTest: concurrent threads call getInstance()
     2) serializationTest: serialize/deserialize to verify singleton after serialization
     3) reflectionTest: attempt to create another instance via reflection

 How to run:
 1. Copy this file into your workspace as 01_SingletonDemo.java
 2. Compile: javac 01_SingletonDemo.java
 3. Run: java SingletonDemo

 Expected behaviour:
 - Concurrency test should report 1 unique instance for both DCL and Enum
 - Serialization test: with readResolve in DCL, deserialized object equals original
 - Reflection test: reflection should be prevented for enum; for DCL the constructor guard prevents reflective creation after the singleton exists
*/

import java.io.*;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.*;

// Dummy connection object to simulate a resource (serializable to test readResolve)
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

// Double-checked locking singleton (lazy init)
class ConnectionPoolDCL implements Serializable {
    private static final long serialVersionUID = 1L;
    private static volatile ConnectionPoolDCL instance;
    private final SimpleConnection connection;

    private ConnectionPoolDCL() {
        // Guard against reflection: prevent creating a second instance after instance already exists
        if (instance != null) {
            throw new IllegalStateException("Singleton already created. Use getInstance().");
        }
        // simulate expensive init
        this.connection = new SimpleConnection();
    }

    public static ConnectionPoolDCL getInstance() {
        if (instance == null) {
            synchronized (ConnectionPoolDCL.class) {
                if (instance == null) {
                    instance = new ConnectionPoolDCL();
                }
            }
        }
        return instance;
    }

    public SimpleConnection getConnection() { return connection; }

    // fix for serialization: ensure deserialized object resolves to the same singleton instance
    private Object readResolve() {
        return getInstance();
    }
}

// Enum-based singleton (most robust: protects against serialization and reflection)
enum ConnectionPoolEnum {
    INSTANCE;

    private final SimpleConnection connection;

    ConnectionPoolEnum() {
        this.connection = new SimpleConnection();
    }

    public SimpleConnection getConnection() { return connection; }
}

// Driver / tests
public class SingletonDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("== Concurrency test ==");
        concurrencyTest();

        System.out.println("\n== Serialization test ==");
        serializationTest();

        System.out.println("\n== Reflection test ==");
        reflectionTest();
    }

    // Spawn threads to call getInstance() concurrently and collect unique identityHashCodes
    static void concurrencyTest() throws Exception {
        final int threads = 100;
        ExecutorService exec = Executors.newFixedThreadPool(Math.min(threads, 16));
        Set<Integer> dclIds = Collections.synchronizedSet(new HashSet<>());
        Set<Integer> enumIds = Collections.synchronizedSet(new HashSet<>());
        CountDownLatch latch = new CountDownLatch(threads);

        for (int i = 0; i < threads; i++) {
            exec.submit(() -> {
                ConnectionPoolDCL d = ConnectionPoolDCL.getInstance();
                ConnectionPoolEnum e = ConnectionPoolEnum.INSTANCE;
                dclIds.add(System.identityHashCode(d));
                enumIds.add(System.identityHashCode(e));
                latch.countDown();
            });
        }

        latch.await();
        exec.shutdown();

        System.out.println("Unique ConnectionPoolDCL instances seen: " + dclIds.size());
        System.out.println("Unique ConnectionPoolEnum instances seen: " + enumIds.size());
    }

    // Serialize and deserialize and check identity
    static void serializationTest() throws Exception {
        // DCL
        ConnectionPoolDCL d1 = ConnectionPoolDCL.getInstance();
        byte[] bytes;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(d1);
            bytes = bos.toByteArray();
        }

        ConnectionPoolDCL d2;
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes); ObjectInputStream ois = new ObjectInputStream(bis)) {
            d2 = (ConnectionPoolDCL) ois.readObject();
        }

        System.out.println("DCL: d1 == d2 ? " + (d1 == d2));
        System.out.println("DCL: d1.connection = " + d1.getConnection());
        System.out.println("DCL: d2.connection = " + d2.getConnection());

        // Enum
        ConnectionPoolEnum e1 = ConnectionPoolEnum.INSTANCE;
        byte[] ebytes;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(e1);
            ebytes = bos.toByteArray();
        }

        ConnectionPoolEnum e2;
        try (ByteArrayInputStream bis = new ByteArrayInputStream(ebytes); ObjectInputStream ois = new ObjectInputStream(bis)) {
            e2 = (ConnectionPoolEnum) ois.readObject();
        }

        System.out.println("Enum: e1 == e2 ? " + (e1 == e2));
    }

    // Try to use reflection to create another instance
    static void reflectionTest() throws Exception {
        System.out.println("-- Reflection on DCL --");
        ConnectionPoolDCL d1 = ConnectionPoolDCL.getInstance();
        try {
            Constructor<ConnectionPoolDCL> cons = ConnectionPoolDCL.class.getDeclaredConstructor();
            cons.setAccessible(true);
            ConnectionPoolDCL d2 = cons.newInstance();
            System.out.println("Reflection succeeded (unexpected): d1 == d2 ? " + (d1 == d2));
        } catch (Exception ex) {
            System.out.println("Reflection attempt failed as expected: " + ex);
        }

        System.out.println("-- Reflection on Enum --");
        try {
            Constructor<?>[] ctors = ConnectionPoolEnum.class.getDeclaredConstructors();
            Constructor<?> ctor = ctors[0];
            ctor.setAccessible(true);
            Object obj = ctor.newInstance();
            System.out.println("Enum reflection created: " + obj);
        } catch (Exception ex) {
            System.out.println("Enum reflection prevented: " + ex);
        }
    }
}
