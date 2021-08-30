package com.company;

public interface ApplicationContext {
    <T> T getBean(Class<?> clazz);
    Object getBean(String className);
}
