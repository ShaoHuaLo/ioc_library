package com.company.pojo;

import com.company.annotation.Autowired;
import com.company.annotation.Component;
import com.company.annotation.Qualifier;
import com.company.annotation.Value;

@Component
public class Person {
    @Value("willy")
    String name;

    @Value("27")
    int age;

    @Autowired
    Address addr;

    @Autowired
    @Qualifier("com.company.pojo.Engineer")
    JobType job;

    public Person() {
    }

    public Person(String name, int age, Address addr) {
        this.name = name;
        this.age = age;
        this.addr = addr;
    }

    public void Work() {
        this.job.work();
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
