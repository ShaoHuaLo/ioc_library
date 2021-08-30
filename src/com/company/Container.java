package com.company;

import com.company.annotation.Autowired;
import com.company.annotation.Qualifier;
import com.company.annotation.Value;
import com.company.pojo.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Container implements ApplicationContext{
    private Map<String, Object> name2obj;
    private Map<Class<?>, Object> class2obj;
    private Map<Class<?>, Integer> class2count;
    private Map<Class<?>, Class<?>> interface2impl;

    public Container() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        name2obj = new HashMap<>();
        class2obj = new HashMap<>();
        class2count = new HashMap<>();
        interface2impl = new HashMap<>();

        List<Class<?>> classes = getComponentClasses();
        for (Class<?> clazz : classes) {
            createBean(clazz);
        }
    }

    private Object createBean(String className) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Class<?> aClass = Class.forName(className);
        return createBean(aClass);
    }

    private Object createBean(Class<?> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        if (class2obj.containsKey(clazz)) {
            return class2obj.get(clazz);
        }

        Constructor<?> constructor = clazz.getConstructor(null);
        Object object = constructor.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Autowired autowired = field.getAnnotation(Autowired.class);
            if (autowired != null) {
                Object dependency = null;
                Class<?> type = field.getType();
                // interface
                if (type.isInterface()) {
                    if (class2count.get(type) > 1 && field.isAnnotationPresent(Qualifier.class)){
                        String name = field.getAnnotation(Qualifier.class).value();
                        dependency = name2obj.get(name) == null ? createBean(name) : name2obj.get(name);
                    }
                    else if (class2count.get(type) == 1) {
                        Class<?> implementation = interface2impl.get(type);
                        dependency = class2obj.get(implementation) == null ? createBean(implementation) : class2obj.get(implementation);
                    }
                    else {
                        throw new RuntimeException("more than 1 implementation and no qualifier used");
                    }
                }
                // class
                else {
                    dependency = class2obj.get(type) == null ? createBean(type) : class2obj.get(type);
                }

                field.setAccessible(true);
                field.set(object, dependency);
            }
            else {
                Value value = field.getAnnotation(Value.class);
                if (value != null) {
                    field.setAccessible(true);
                    Class<?> type = field.getType();
                    // here I assume we only have int/integer and String type
                    if (type == int.class || type == Integer.class) {
                        field.set(object, Integer.valueOf(value.value()));
                    }
                    else {
                        field.set(object, value.value());
                    }
                }
            }
        }

        class2obj.put(clazz, object);
        name2obj.put(clazz.getSimpleName(), object);

        return object;
    }

    @Override
    public <T> T getBean(Class<?> clazz) {
        return (T)class2obj.get(clazz);
    }

    @Override
    public Object getBean(String className) {
        return name2obj.get(className);
    }

    // adjust manually
    private List<Class<?>> getComponentClasses() {
        class2count.put(JobType.class, 3);
        return Arrays.asList(Person.class, Address.class, Engineer.class, Sales.class, Manager.class);
    }

}
