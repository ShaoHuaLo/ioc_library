package com.company.pojo;

import com.company.annotation.Component;

@Component
public class Engineer implements JobType {
    String name;
    int age;

    public Engineer() {
    }

    public Engineer(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public void work() {
        System.out.println("I'm an engineer, I develop and design products");
    }
}
