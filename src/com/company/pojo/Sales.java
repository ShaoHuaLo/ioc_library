package com.company.pojo;

import com.company.annotation.Component;

@Component
public class Sales implements JobType {
    String name;
    int age;

    public Sales() {
    }

    public Sales(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public void work() {
        System.out.println("I'm a sales, I sell products!");
    }
}
