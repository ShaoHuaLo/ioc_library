package com.company.pojo;

import com.company.annotation.Autowired;
import com.company.annotation.Value;

public class Person {
    @Value("willy")
    String name;

    @Value("27")
    int age;

    @Autowired
    Address addr;

    public Person() {
    }

    public Person(String name, int age, Address addr) {
        this.name = name;
        this.age = age;
        this.addr = addr;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", addr=" + addr +
                '}';
    }
}
