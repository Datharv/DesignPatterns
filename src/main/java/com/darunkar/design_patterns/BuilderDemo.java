package com.darunkar.design_patterns;

import java.util.Locale;

class User{

    private final String name;
    private final int age;

    private final String city;
    private final String role;
    private final String email;
    private final String phone;

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getCity() {
        return city;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    private User(Builder builder){
        this.name = builder.name;
        this.age = builder.age;
        this.city = builder.city;
        this.role = builder.role;
        this.email = builder.email;
        this.phone = builder.phone;
    }

    public static class Builder{
        private final String name;
        private final int age;

        private String city;
        private String role;
        private String email;
        private String phone;

        public Builder(String name, int age){
            this.name = name;
            this.age = age;
        }

        public Builder setCity(String city){
            this.city = city;
            return this;
        }
        public Builder setRole(String role){
            this.role = role;
            return this;
        }
        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public User build(){
            return new User(this);
        }
    }
}

public class BuilderDemo {

    User user1 = new User.Builder("Atharv", 23).build();
}
