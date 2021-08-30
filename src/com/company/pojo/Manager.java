package com.company.pojo;

import com.company.annotation.Component;

@Component
public class Manager implements JobType {
    String name;
    int age;

    public Manager() {
    }

    public Manager(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public void work() {
        System.out.println("I'm a manager, I am in charge of a team of 5 engineers and 5 sales");
    }
}
