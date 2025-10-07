package com.darunkar.design_patterns.flyweight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComputeIfAbsentDemo {
    public static void main(String[] args) {

        Map<String , String> cache = new HashMap<>();

        String value = cache.computeIfAbsent("user123", key-> {
            System.out.println("Creating new entry for : " + key);
            return "UserData_" + key;
        });

//        System.out.println(value);
//        System.out.println(cache.get("user123"));

        String again = cache.computeIfAbsent("user1234", key -> "NewData");
//        System.out.println(again);

//        2. Word Count

        Map<String, Integer> wordCount = new HashMap<>();
        String[] words = {"java", "python", "java", "go", "java"};

        for(String word: words){
            wordCount.computeIfAbsent(word, k -> 0);
            wordCount.put(word, wordCount.get(word)+1);
        }

//        System.out.println(wordCount);

        Map<String, List<String>> deptStudents = new HashMap<>();
        addStudent(deptStudents, "CS", "Atharv");
        addStudent(deptStudents, "CS", "Sagar");
        addStudent(deptStudents, "ENTC", "Martand");

        System.out.println(deptStudents);


    }
    private static void addStudent(Map<String, List<String>> map, String dept, String student){

        map.computeIfAbsent(dept, key-> new ArrayList<>()).add(student);
    }

}
