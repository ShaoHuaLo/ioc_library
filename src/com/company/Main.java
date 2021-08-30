package com.company;

import com.company.pojo.JobType;
import com.company.pojo.Person;

import java.lang.reflect.InvocationTargetException;

public class Main {

    public static void main(String[] args) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException {
	// write your code here
        ApplicationContext context = new Container();
        Person obj = context.getBean(Person.class);
        obj.Work();

        System.out.println(obj);
    }
}
