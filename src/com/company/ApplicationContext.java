package com.company;

public interface ApplicationContext {
    /**
     * getBean by type(Class<?>)
     */
    <T> T getBean(Class<?> clazz);

    /**
     * get Bean by className(String)
    */
    Object getBean(String className);
}
